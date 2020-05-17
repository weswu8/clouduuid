package com.weswu.clouduuid.entity;

import com.google.cloud.spanner.ResultSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UuidSpace {
    public String nameSpace;
    public String tag;
    public long minId;
    public long maxId;
    public String description;
    public String workerId;
    public long updated;

    public UuidSpace(String nameSpace, String tag, String description){
        this.nameSpace = nameSpace;
        this.tag = tag;
        this.description = description;
        this.workerId = null;
        this.updated = -1l;
    }

    public static UuidSpace fromSqlResultSet(ResultSet resultSet){
        UuidSpace tmpUuidSpace = new UuidSpace();
        tmpUuidSpace.setNameSpace(resultSet.getString("NameSpace"));
        tmpUuidSpace.setTag(resultSet.getString("Tag"));
        tmpUuidSpace.setMinId(resultSet.getLong("MinId"));
        tmpUuidSpace.setMaxId(resultSet.getLong("MaxId"));
        tmpUuidSpace.setDescription(resultSet.getString("Description"));
        tmpUuidSpace.setWorkerId(resultSet.getString("WorkerId"));
        tmpUuidSpace.setUpdated(resultSet.getLong("Updated"));
        return tmpUuidSpace;
    }

    public String getSpaceKey(){
        return this.nameSpace + "-" + this.tag;
    }

    public static UuidSpace fromSpaceKey(String spaceKey){
        UuidSpace tmpUuidSpace = new UuidSpace();
        tmpUuidSpace.setNameSpace(spaceKey.split("-")[0]);
        tmpUuidSpace.setTag(spaceKey.split("-")[1]);
        return tmpUuidSpace;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UuidSpace{");
        sb.append("NameSpace='").append(nameSpace);
        sb.append(", Tag=").append(tag);
        sb.append(", MinId=").append(String.valueOf(minId));
        sb.append(", MaxId=").append(String.valueOf(maxId));
        sb.append(", Description=").append(description);
        sb.append(", WorkId=").append(workerId);
        sb.append(", Updated=").append(String.valueOf(updated));
        sb.append('}');
        return sb.toString();
    }
}
