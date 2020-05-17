package com.weswu.clouduuid.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProps {
    public static Properties props;
    public static Properties fromAppPros() throws IOException {
        if (null == props) {
            Properties tmpProps = new Properties();
            File appPropsFile = ResourceUtils.getFile("classpath:application.properties");
            tmpProps.load(new FileInputStream(appPropsFile));
            props = tmpProps;
            props.setProperty("class.path", "classpath");
        }
        return props;
    }
}
