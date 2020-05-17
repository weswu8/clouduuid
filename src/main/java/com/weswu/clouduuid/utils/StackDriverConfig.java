package com.weswu.clouduuid.utils;

import com.google.auth.oauth2.GoogleCredentials;
import io.opencensus.common.Duration;
import io.opencensus.exporter.stats.stackdriver.StackdriverStatsConfiguration;
import io.opencensus.exporter.stats.stackdriver.StackdriverStatsExporter;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.trace.Tracing;
import io.opencensus.contrib.grpc.metrics.RpcViews;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.samplers.Samplers;
import java.io.IOException;
import java.util.Properties;

public class StackDriverConfig {
    public static void setupOpenCensusAndStackDriver() throws IOException {
        Properties props = LoadProps.fromAppPros();
        TraceConfig traceConfig = Tracing.getTraceConfig();
        traceConfig.updateActiveTraceParams(
                traceConfig.getActiveTraceParams().toBuilder().setSampler(Samplers.alwaysSample()).build());

        RpcViews.registerAllGrpcViews();
        String projectId = props.getProperty("gcp.project.id");
        GoogleCredentials credentials = Credentials.getGoogleCredentials();
        StackdriverStatsExporter.createAndRegister(
                StackdriverStatsConfiguration.builder()
                        .setProjectId(projectId)
                        .setCredentials(credentials)
                        .setExportInterval(Duration.create(10, 0))
                        .build());

        StackdriverTraceExporter.createAndRegister(
                StackdriverTraceConfiguration.builder()
                        .setProjectId(projectId)
                        .setCredentials(credentials)
                        .build());

    }
}
