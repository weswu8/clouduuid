package com.weswu.clouduuid.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spanner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SpannerInstance {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static SpannerInstance instance;
    private Spanner spanner;
    private DatabaseClient dbClient;

    private SpannerInstance() throws Exception {
        try {
            Properties props = LoadProps.fromAppPros();
            final SpannerOptions.Builder builder = SpannerOptions.newBuilder();
            builder.setProjectId(props.getProperty("gcp.project.id"));
            builder.setCredentials(Credentials.getGoogleCredentials());
            this.spanner =  builder.build().getService();
            this.dbClient = this.spanner.getDatabaseClient(DatabaseId.of(props.getProperty("gcp.project.id"), props.getProperty("spanner.instance"), props.getProperty("spanner.database")));
        } catch ( IOException | SpannerException ex) {
            logger.error("Spanner instance creation failed : " + ex.getMessage());
        }
    }

    public static SpannerInstance getInstance() throws Exception {
        if (instance == null) {
            instance = new SpannerInstance();
        }
        return instance;
    }

    public DatabaseClient getDbClient() {
        return this.dbClient;
    }

}
