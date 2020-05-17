package com.weswu.clouduuid.controller;


import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.Status;
import com.weswu.clouduuid.entity.UuidRequest;
import com.weswu.clouduuid.grpc.*;
import com.weswu.clouduuid.service.SequentialIdService;
import com.weswu.clouduuid.service.CachedSnowFlakeIdService;
import com.weswu.clouduuid.service.SnowFlakeIdService;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import io.opencensus.common.Scope;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@GRpcService
@Component
public class UuidController extends UuidControllerGrpc.UuidControllerImplBase {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    SequentialIdService sequentialIdService = SequentialIdService.getInstance();
    CachedSnowFlakeIdService cachedSnowFlakeIdService = CachedSnowFlakeIdService.getInstance();
    SnowFlakeIdService snowFlakeIdService = SnowFlakeIdService.getInstance();
    private static final Tracer tracer = Tracing.getTracer();
    private static String className = UuidController.class.getName();
    @Override
    public void createSequentiallIdSpace(CreateSequentialIdSpaceReq request, StreamObserver<CommonResp> responseObserver) {
        CommonResp commonResp = CommonResp.newBuilder().build();
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try (Scope scope = tracer.spanBuilder(className + "." +methodName).startScopedSpan()) {
            try {
                if (!request.getDomain().trim().isEmpty() && !request.getTag().trim().isEmpty()) {
                    UuidRequest uuidRequest = new UuidRequest(request.getDomain(), request.getTag(), request.getDescription());
                    sequentialIdService.createKey(uuidRequest);
                    String respContent = String.format("successfully created the key space %s ", uuidRequest.getSpaceKey());
                    commonResp = CommonResp.newBuilder().setContent(respContent).build();
                    responseObserver.onNext(commonResp);
                    responseObserver.onCompleted();
                } else {
                    Status status = Status.newBuilder()
                            .setCode(Code.INVALID_ARGUMENT.getNumber())
                            .setMessage("Domain or tag is empty.")
                            .addDetails(Any.pack(commonResp))
                            .build();
                    responseObserver.onError(StatusProto.toStatusRuntimeException(status));
                }
            } catch (Throwable t) {
                responseObserver.onError(new StatusRuntimeException(io.grpc.Status.INTERNAL.withDescription(t.getMessage())));
                return;
            }
        }
    }

    @Override
    public void deleteSequentiallIdSpace(SequentialIdReq request, StreamObserver<CommonResp> responseObserver) {
        CommonResp commonResp = CommonResp.newBuilder().build();
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try (Scope scope = tracer.spanBuilder(className + "." +methodName).startScopedSpan()) {
            try {
                if (!request.getDomain().trim().isEmpty() && !request.getTag().trim().isEmpty()) {
                    UuidRequest uuidRequest = new UuidRequest(request.getDomain(), request.getTag());
                    sequentialIdService.deleteKey(uuidRequest);
                    String respContent = String.format("successfully deleted the key space %s ", uuidRequest.getSpaceKey());
                    commonResp = CommonResp.newBuilder().setContent(respContent).build();
                    responseObserver.onNext(commonResp);
                    responseObserver.onCompleted();
                } else {
                    Status status = Status.newBuilder()
                            .setCode(Code.INVALID_ARGUMENT.getNumber())
                            .setMessage("Domain or tag is empty.")
                            .addDetails(Any.pack(commonResp))
                            .build();
                    responseObserver.onError(StatusProto.toStatusRuntimeException(status));
                }
            } catch (Throwable t) {
                responseObserver.onError(new StatusRuntimeException(io.grpc.Status.INTERNAL.withDescription(t.getMessage())));
                return;
            }
        }
    }

    @Override
    public void deleteAllIdSpaces(Empty request, StreamObserver<CommonResp> responseObserver) {
        CommonResp commonResp;
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try (Scope scope = tracer.spanBuilder(className + "." +methodName).startScopedSpan()) {
            try {
                sequentialIdService.deleteAll();
                String respContent = String.format("successfully deleted all key spaces");
                commonResp = CommonResp.newBuilder().setContent(respContent).build();
                responseObserver.onNext(commonResp);
                responseObserver.onCompleted();
            } catch (Throwable t) {
                responseObserver.onError(new StatusRuntimeException(io.grpc.Status.INTERNAL.withDescription(t.getMessage())));
                return;
            }
        }
    }

    @Override
    public void getSequentialId(SequentialIdReq request, StreamObserver<UuidResp> responseObserver) {
        UuidResp uuidResp = UuidResp.newBuilder().build();
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try (Scope scope = tracer.spanBuilder(className + "." +methodName).startScopedSpan()) {
            try {
                if (!request.getDomain().trim().isEmpty() && !request.getTag().trim().isEmpty()) {
                    UuidRequest uuidRequest = new UuidRequest(request.getDomain(), request.getTag());
                    long uuid = sequentialIdService.getUuid(uuidRequest.getSpaceKey());
                    uuidResp = UuidResp.newBuilder().setUuid(uuid).build();
                    responseObserver.onNext(uuidResp);
                    responseObserver.onCompleted();
                } else {
                    Status status = Status.newBuilder()
                            .setCode(Code.INVALID_ARGUMENT.getNumber())
                            .setMessage("Domain or tag is empty.")
                            .addDetails(Any.pack(uuidResp))
                            .build();
                    responseObserver.onError(StatusProto.toStatusRuntimeException(status));
                }
            } catch (Throwable t) {
                responseObserver.onError(new StatusRuntimeException(io.grpc.Status.INTERNAL.withDescription(t.getMessage())));
                return;
            }
        }
    }

    @Override
    public void getSnowFlakeId(Empty request, StreamObserver<UuidResp> responseObserver) {
        UuidResp uuidResp;
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try (Scope scope = tracer.spanBuilder(className + "." +methodName).startScopedSpan()) {
            try {
                long uuid = snowFlakeIdService.getUuid("");
                uuidResp = UuidResp.newBuilder().setUuid(uuid).build();
                responseObserver.onNext(uuidResp);
                responseObserver.onCompleted();
            } catch (Throwable t) {
                responseObserver.onError(new StatusRuntimeException(io.grpc.Status.INTERNAL.withDescription(t.getMessage())));
                return;
            }
        }
    }

    @Override
    public void getCachedSnowFlakeId(Empty request, StreamObserver<UuidResp> responseObserver) {
        UuidResp uuidResp;
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try (Scope scope = tracer.spanBuilder(className + "." +methodName).startScopedSpan()) {
            try {
                long uuid = cachedSnowFlakeIdService.getUuid("");
                uuidResp = UuidResp.newBuilder().setUuid(uuid).build();
                responseObserver.onNext(uuidResp);
                responseObserver.onCompleted();
            } catch (Throwable t) {
                responseObserver.onError(new StatusRuntimeException(io.grpc.Status.INTERNAL.withDescription(t.getMessage())));
                return;
            }
        }
    }

    @PreDestroy
    public void destroy() {
        Tracing.getExportComponent().shutdown();
    }

}
