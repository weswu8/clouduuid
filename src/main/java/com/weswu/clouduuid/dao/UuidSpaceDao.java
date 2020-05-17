package com.weswu.clouduuid.dao;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.*;
import com.weswu.clouduuid.entity.UuidSpace;
import com.weswu.clouduuid.utils.LoadProps;
import com.weswu.clouduuid.utils.SpannerInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class UuidSpaceDao {
    private static DatabaseClient dbClient;
    private static Properties props;
    private static DatabaseClient getDbClient() throws Exception {
        if(null == dbClient){
            dbClient = SpannerInstance.getInstance().getDbClient();
        }
        if(null == props){
            props = LoadProps.fromAppPros();
        }
        return dbClient;
    }

    public static List<UuidSpace> getAll() throws Exception {
        getDbClient();
        List<UuidSpace> uuidSpaces = new ArrayList<UuidSpace>(){};
        try (ReadOnlyTransaction transaction = dbClient.readOnlyTransaction()) {
            String table = props.getProperty("spanner.database.table");
            String sql = "SELECT * FROM " + table + "";
            ResultSet queryResultSet = transaction.executeQuery(Statement.of(sql));
            while (queryResultSet.next()) {
                UuidSpace tmpUuidSpace = UuidSpace.fromSqlResultSet(queryResultSet);
                uuidSpaces.add(tmpUuidSpace);
            }
        }
        return uuidSpaces;
    }

    public static List<UuidSpace> updateAll() throws Exception {
        getDbClient();
        List<UuidSpace> uuidSpaces = new ArrayList<UuidSpace>(){};
        dbClient
            .readWriteTransaction()
            .run(
                new TransactionRunner.TransactionCallable<Void>() {
                    @Override
                    public Void run(TransactionContext transaction) throws Exception {
                        String table = props.getProperty("spanner.database.table");
                        String workerId = props.getProperty("worker.id");
                        long stride = Long.parseLong(props.getProperty("uuid.range.stride"));
                        List<Statement> statements = new ArrayList<Statement>();
                        String sql = "SELECT * FROM "+ table +"";
                        try (ResultSet resultSet = transaction.executeQuery(Statement.of(sql))) {
                            while (resultSet.next()) {
                                UuidSpace tmpUuidSpace = UuidSpace.fromSqlResultSet(resultSet);
                                Statement statement = Statement.newBuilder(
                                        "UPDATE "+table+" SET MinId=@MinId, MaxId=@MaxId, WorkerId=@WorkerId, Updated=@Updated " +
                                                "WHERE NameSpace=@NameSpace AND Tag=@Tag ")
                                        .bind("NameSpace")
                                        .to(tmpUuidSpace.getNameSpace())
                                        .bind("Tag")
                                        .to(tmpUuidSpace.getTag())
                                        .bind("MinId")
                                        .to(tmpUuidSpace.getMaxId())
                                        .bind("MaxId")
                                        .to(tmpUuidSpace.getMaxId() + stride)
                                        .bind("WorkerId")
                                        .to(workerId)
                                        .bind("Updated")
                                        .to(System.currentTimeMillis())
                                        .build();
                                statements.add(statement);
                            }
                        }
                        if(statements.size() == 0){
                            return  null;
                        }
                        transaction.batchUpdate(statements);
                        try (ResultSet resultSet = transaction.executeQuery(Statement.of(sql))) {
                            while (resultSet.next()) {
                                UuidSpace tmpUuidSpace = UuidSpace.fromSqlResultSet(resultSet);
                                uuidSpaces.add(tmpUuidSpace);
                            }
                        }
                        return null;
                    }
                });
        return uuidSpaces;
    }
    /***
     * update the uuid space
     * @param uuidSpace
     * @throws Exception
     */
    public static UuidSpace update(UuidSpace uuidSpace) throws Exception {
        getDbClient();
        final UuidSpace[] resUuidSpace = {null};
        dbClient
            .readWriteTransaction()
            .run(
                new TransactionRunner.TransactionCallable<Void>() {
                    @Override
                    public Void run(TransactionContext transaction) throws Exception {
                        String table = props.getProperty("spanner.database.table");
                        String workerId = props.getProperty("worker.id");
                        long stride = Long.parseLong(props.getProperty("uuid.range.stride"));
                        Statement statement = null;
                        Struct row = transaction.readRow(table,  Key.of(uuidSpace.getNameSpace(), uuidSpace.getTag()), Arrays.asList("MaxId"));
                        if (null == row){
                            String err = String.format("the key %s does not exists", uuidSpace.getSpaceKey());
                            throw new Exception(err);
                        }else{
                            long curtMaxId = row.getLong(0);
                            statement = Statement.newBuilder(
                                    "UPDATE "+table+" SET MinId=@MinId, MaxId=@MaxId, WorkerId=@WorkerId, Updated=@Updated " +
                                            "WHERE NameSpace=@NameSpace AND Tag=@Tag ")
                                    .bind("NameSpace")
                                    .to(uuidSpace.getNameSpace())
                                    .bind("Tag")
                                    .to(uuidSpace.getTag())
                                    .bind("MinId")
                                    .to(curtMaxId)
                                    .bind("MaxId")
                                    .to(curtMaxId + stride)
                                    .bind("WorkerId")
                                    .to(workerId)
                                    .bind("Updated")
                                    .to(System.currentTimeMillis())
                                    .build();
                        }
                        //System.out.println(statement.toString());
                        transaction.executeUpdate(statement);
                        statement = Statement.newBuilder(
                                "SELECT * FROM "+table+ " WHERE NameSpace=@NameSpace AND Tag=@Tag ")
                                .bind("NameSpace")
                                .to(uuidSpace.getNameSpace())
                                .bind("Tag")
                                .to(uuidSpace.getTag())
                                .build();
                        try (ResultSet resultSet = transaction.executeQuery(statement)) {
                            while (resultSet.next()) {
                                resUuidSpace[0] = UuidSpace.fromSqlResultSet(resultSet);
                            }
                        }

                        return null;
                    }
                });
        return resUuidSpace[0];
    }

    /***
     * save uuid space
     * @param uuidSpace
     * @throws Exception
     */
    public static UuidSpace save(UuidSpace uuidSpace) throws Exception {
        getDbClient();
        final UuidSpace[] resUuidSpace = {null};
        dbClient
                .readWriteTransaction()
                .run(
                        new TransactionRunner.TransactionCallable<Void>() {
                            @Override
                            public Void run(TransactionContext transaction) throws Exception {
                                String table = props.getProperty("spanner.database.table");
                                String workerId = props.getProperty("worker.id");
                                long stride = Long.parseLong(props.getProperty("uuid.range.stride"));
                                Statement statement = null;
                                Struct row = transaction.readRow(table,  Key.of(uuidSpace.getNameSpace(), uuidSpace.getTag()), Arrays.asList("MaxId"));
                                if (null == row){
                                    statement = Statement.newBuilder(
                                            "INSERT INTO "+table+" (NameSpace, Tag, MinId, MaxId, Description, WorkerId, Updated) "
                                                    + "VALUES (@NameSpace, @Tag, @MinId, @MaxId, @Description, @WorkerId, @Updated) ")
                                            .bind("NameSpace")
                                            .to(uuidSpace.getNameSpace())
                                            .bind("Tag")
                                            .to(uuidSpace.getTag())
                                            .bind("MinId")
                                            .to(1l)
                                            .bind("MaxId")
                                            .to(stride)
                                            .bind("Description")
                                            .to(uuidSpace.getDescription())
                                            .bind("WorkerId")
                                            .to(workerId)
                                            .bind("Updated")
                                            .to(System.currentTimeMillis())
                                            .build();
                                }else {
                                    String err = String.format("the key %s already exists", uuidSpace.getSpaceKey());
                                    throw new IllegalArgumentException(err);
                                }
                                transaction.executeUpdate(statement);
                                statement = Statement.newBuilder(
                                        "SELECT * FROM "+table+ " WHERE NameSpace=@NameSpace AND Tag=@Tag ")
                                        .bind("NameSpace")
                                        .to(uuidSpace.getNameSpace())
                                        .bind("Tag")
                                        .to(uuidSpace.getTag())
                                        .build();
                                try (ResultSet resultSet = transaction.executeQuery(statement)) {
                                    while (resultSet.next()) {
                                        resUuidSpace[0] = UuidSpace.fromSqlResultSet(resultSet);
                                    }
                                }
                                return null;
                            }
                        });
        return resUuidSpace[0];
    }

    /***
     * delete the uuid space
     * @param uuidSpace
     * @throws Exception
     */
    public static void delete(UuidSpace uuidSpace) throws Exception {
        getDbClient();
        dbClient
            .readWriteTransaction()
            .run(
                new TransactionRunner.TransactionCallable<Void>() {
                    @Override
                    public Void run(TransactionContext transaction) throws Exception {
                        String table = props.getProperty("spanner.database.table");
                        Statement statement = Statement.newBuilder(
                                "DELETE FROM " +table+
                                        " WHERE NameSpace=@NameSpace AND Tag=@Tag ")
                                .bind("NameSpace")
                                .to(uuidSpace.getNameSpace())
                                .bind("Tag")
                                .to(uuidSpace.getTag())
                                .build();
                        transaction.executeUpdate(statement);
                        return null;
                    }
                });
    }

    public static void deleteAll() throws Exception {
        getDbClient();
        dbClient
            .readWriteTransaction()
            .run(
                new TransactionRunner.TransactionCallable<Void>() {
                    @Override
                    public Void run(TransactionContext transaction) throws Exception {
                        String table = props.getProperty("spanner.database.table");
                        Statement statement = Statement.newBuilder(
                                "DELETE FROM " +table+ " WHERE true" )
                                .build();
                        transaction.executeUpdate(statement);
                        return null;
                    }
                });
    }

    public static Timestamp getTimeStampNow() throws Exception {
        getDbClient();
        Timestamp ts = null;
        Statement statement = Statement.newBuilder(
                "SELECT CURRENT_TIMESTAMP() as now")
                .build();
        try (ResultSet resultSet = dbClient.singleUse().executeQuery(statement)) {
            while (resultSet.next()) {
                ts = resultSet.getTimestamp("now");
            }
        }
        return ts;
    }

    public static long getUnixMillisNow() throws Exception {
        getDbClient();
        long millis = 0L;
        Timestamp timeStampNow = getTimeStampNow();
        if (null == timeStampNow){throw new Exception("failed to get time form spanner.");}
        Statement statement = Statement.newBuilder(
                "SELECT UNIX_MILLIS(TIMESTAMP '" + timeStampNow.toString() + "') as millis;")
                .build();
        try (ResultSet resultSet = dbClient.singleUse().executeQuery(statement)) {
            while (resultSet.next()) {
                millis = resultSet.getLong("millis");
            }
        }
        return millis;
    }

}
