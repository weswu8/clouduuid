package com.weswu.clouduuid.utils;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Credentials {
    private static Properties props;
    private static GoogleCredentials googleCredentials;
    public static GoogleCredentials getGoogleCredentials() throws IOException {
        if(null != googleCredentials){return googleCredentials;}
        props = LoadProps.fromAppPros();
        File file = ResourceUtils.getFile("classpath:"+ props.getProperty("gcp.service.account.file") + "");
        googleCredentials =GoogleCredentials.fromStream(new FileInputStream(file));
        return googleCredentials;
    }
}
