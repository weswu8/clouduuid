package com.weswu.clouduuid.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.1)",
    comments = "Source: UuidService.proto")
public class UuidControllerGrpc {

  private UuidControllerGrpc() {}

  public static final String SERVICE_NAME = "com.weswu.clouduuid.grpc.UuidController";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq,
      com.weswu.clouduuid.grpc.CommonResp> METHOD_CREATE_SEQUENTIALL_ID_SPACE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.weswu.clouduuid.grpc.UuidController", "createSequentiallIdSpace"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.CommonResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.weswu.clouduuid.grpc.SequentialIdReq,
      com.weswu.clouduuid.grpc.CommonResp> METHOD_DELETE_SEQUENTIALL_ID_SPACE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.weswu.clouduuid.grpc.UuidController", "deleteSequentiallIdSpace"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.SequentialIdReq.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.CommonResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.weswu.clouduuid.grpc.Empty,
      com.weswu.clouduuid.grpc.CommonResp> METHOD_DELETE_ALL_ID_SPACES =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.weswu.clouduuid.grpc.UuidController", "deleteAllIdSpaces"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.Empty.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.CommonResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.weswu.clouduuid.grpc.SequentialIdReq,
      com.weswu.clouduuid.grpc.UuidResp> METHOD_GET_SEQUENTIAL_ID =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.weswu.clouduuid.grpc.UuidController", "getSequentialId"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.SequentialIdReq.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.UuidResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.weswu.clouduuid.grpc.Empty,
      com.weswu.clouduuid.grpc.UuidResp> METHOD_GET_SNOW_FLAKE_ID =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.weswu.clouduuid.grpc.UuidController", "getSnowFlakeId"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.Empty.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.UuidResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.weswu.clouduuid.grpc.Empty,
      com.weswu.clouduuid.grpc.UuidResp> METHOD_GET_CACHED_SNOW_FLAKE_ID =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.weswu.clouduuid.grpc.UuidController", "getCachedSnowFlakeId"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.Empty.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.weswu.clouduuid.grpc.UuidResp.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UuidControllerStub newStub(io.grpc.Channel channel) {
    return new UuidControllerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UuidControllerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new UuidControllerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static UuidControllerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new UuidControllerFutureStub(channel);
  }

  /**
   */
  public static abstract class UuidControllerImplBase implements io.grpc.BindableService {

    /**
     */
    public void createSequentiallIdSpace(com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREATE_SEQUENTIALL_ID_SPACE, responseObserver);
    }

    /**
     */
    public void deleteSequentiallIdSpace(com.weswu.clouduuid.grpc.SequentialIdReq request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_SEQUENTIALL_ID_SPACE, responseObserver);
    }

    /**
     */
    public void deleteAllIdSpaces(com.weswu.clouduuid.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_ALL_ID_SPACES, responseObserver);
    }

    /**
     */
    public void getSequentialId(com.weswu.clouduuid.grpc.SequentialIdReq request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SEQUENTIAL_ID, responseObserver);
    }

    /**
     */
    public void getSnowFlakeId(com.weswu.clouduuid.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_SNOW_FLAKE_ID, responseObserver);
    }

    /**
     */
    public void getCachedSnowFlakeId(com.weswu.clouduuid.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_CACHED_SNOW_FLAKE_ID, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CREATE_SEQUENTIALL_ID_SPACE,
            asyncUnaryCall(
              new MethodHandlers<
                com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq,
                com.weswu.clouduuid.grpc.CommonResp>(
                  this, METHODID_CREATE_SEQUENTIALL_ID_SPACE)))
          .addMethod(
            METHOD_DELETE_SEQUENTIALL_ID_SPACE,
            asyncUnaryCall(
              new MethodHandlers<
                com.weswu.clouduuid.grpc.SequentialIdReq,
                com.weswu.clouduuid.grpc.CommonResp>(
                  this, METHODID_DELETE_SEQUENTIALL_ID_SPACE)))
          .addMethod(
            METHOD_DELETE_ALL_ID_SPACES,
            asyncUnaryCall(
              new MethodHandlers<
                com.weswu.clouduuid.grpc.Empty,
                com.weswu.clouduuid.grpc.CommonResp>(
                  this, METHODID_DELETE_ALL_ID_SPACES)))
          .addMethod(
            METHOD_GET_SEQUENTIAL_ID,
            asyncUnaryCall(
              new MethodHandlers<
                com.weswu.clouduuid.grpc.SequentialIdReq,
                com.weswu.clouduuid.grpc.UuidResp>(
                  this, METHODID_GET_SEQUENTIAL_ID)))
          .addMethod(
            METHOD_GET_SNOW_FLAKE_ID,
            asyncUnaryCall(
              new MethodHandlers<
                com.weswu.clouduuid.grpc.Empty,
                com.weswu.clouduuid.grpc.UuidResp>(
                  this, METHODID_GET_SNOW_FLAKE_ID)))
          .addMethod(
            METHOD_GET_CACHED_SNOW_FLAKE_ID,
            asyncUnaryCall(
              new MethodHandlers<
                com.weswu.clouduuid.grpc.Empty,
                com.weswu.clouduuid.grpc.UuidResp>(
                  this, METHODID_GET_CACHED_SNOW_FLAKE_ID)))
          .build();
    }
  }

  /**
   */
  public static final class UuidControllerStub extends io.grpc.stub.AbstractStub<UuidControllerStub> {
    private UuidControllerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UuidControllerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UuidControllerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UuidControllerStub(channel, callOptions);
    }

    /**
     */
    public void createSequentiallIdSpace(com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CREATE_SEQUENTIALL_ID_SPACE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteSequentiallIdSpace(com.weswu.clouduuid.grpc.SequentialIdReq request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_SEQUENTIALL_ID_SPACE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteAllIdSpaces(com.weswu.clouduuid.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_ALL_ID_SPACES, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSequentialId(com.weswu.clouduuid.grpc.SequentialIdReq request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SEQUENTIAL_ID, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSnowFlakeId(com.weswu.clouduuid.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_SNOW_FLAKE_ID, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCachedSnowFlakeId(com.weswu.clouduuid.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_CACHED_SNOW_FLAKE_ID, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UuidControllerBlockingStub extends io.grpc.stub.AbstractStub<UuidControllerBlockingStub> {
    private UuidControllerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UuidControllerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UuidControllerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UuidControllerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.weswu.clouduuid.grpc.CommonResp createSequentiallIdSpace(com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CREATE_SEQUENTIALL_ID_SPACE, getCallOptions(), request);
    }

    /**
     */
    public com.weswu.clouduuid.grpc.CommonResp deleteSequentiallIdSpace(com.weswu.clouduuid.grpc.SequentialIdReq request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_SEQUENTIALL_ID_SPACE, getCallOptions(), request);
    }

    /**
     */
    public com.weswu.clouduuid.grpc.CommonResp deleteAllIdSpaces(com.weswu.clouduuid.grpc.Empty request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_ALL_ID_SPACES, getCallOptions(), request);
    }

    /**
     */
    public com.weswu.clouduuid.grpc.UuidResp getSequentialId(com.weswu.clouduuid.grpc.SequentialIdReq request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SEQUENTIAL_ID, getCallOptions(), request);
    }

    /**
     */
    public com.weswu.clouduuid.grpc.UuidResp getSnowFlakeId(com.weswu.clouduuid.grpc.Empty request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_SNOW_FLAKE_ID, getCallOptions(), request);
    }

    /**
     */
    public com.weswu.clouduuid.grpc.UuidResp getCachedSnowFlakeId(com.weswu.clouduuid.grpc.Empty request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_CACHED_SNOW_FLAKE_ID, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UuidControllerFutureStub extends io.grpc.stub.AbstractStub<UuidControllerFutureStub> {
    private UuidControllerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UuidControllerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UuidControllerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UuidControllerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.weswu.clouduuid.grpc.CommonResp> createSequentiallIdSpace(
        com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CREATE_SEQUENTIALL_ID_SPACE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.weswu.clouduuid.grpc.CommonResp> deleteSequentiallIdSpace(
        com.weswu.clouduuid.grpc.SequentialIdReq request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_SEQUENTIALL_ID_SPACE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.weswu.clouduuid.grpc.CommonResp> deleteAllIdSpaces(
        com.weswu.clouduuid.grpc.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_ALL_ID_SPACES, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.weswu.clouduuid.grpc.UuidResp> getSequentialId(
        com.weswu.clouduuid.grpc.SequentialIdReq request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SEQUENTIAL_ID, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.weswu.clouduuid.grpc.UuidResp> getSnowFlakeId(
        com.weswu.clouduuid.grpc.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_SNOW_FLAKE_ID, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.weswu.clouduuid.grpc.UuidResp> getCachedSnowFlakeId(
        com.weswu.clouduuid.grpc.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_CACHED_SNOW_FLAKE_ID, getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_SEQUENTIALL_ID_SPACE = 0;
  private static final int METHODID_DELETE_SEQUENTIALL_ID_SPACE = 1;
  private static final int METHODID_DELETE_ALL_ID_SPACES = 2;
  private static final int METHODID_GET_SEQUENTIAL_ID = 3;
  private static final int METHODID_GET_SNOW_FLAKE_ID = 4;
  private static final int METHODID_GET_CACHED_SNOW_FLAKE_ID = 5;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UuidControllerImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(UuidControllerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_SEQUENTIALL_ID_SPACE:
          serviceImpl.createSequentiallIdSpace((com.weswu.clouduuid.grpc.CreateSequentialIdSpaceReq) request,
              (io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp>) responseObserver);
          break;
        case METHODID_DELETE_SEQUENTIALL_ID_SPACE:
          serviceImpl.deleteSequentiallIdSpace((com.weswu.clouduuid.grpc.SequentialIdReq) request,
              (io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp>) responseObserver);
          break;
        case METHODID_DELETE_ALL_ID_SPACES:
          serviceImpl.deleteAllIdSpaces((com.weswu.clouduuid.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.CommonResp>) responseObserver);
          break;
        case METHODID_GET_SEQUENTIAL_ID:
          serviceImpl.getSequentialId((com.weswu.clouduuid.grpc.SequentialIdReq) request,
              (io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp>) responseObserver);
          break;
        case METHODID_GET_SNOW_FLAKE_ID:
          serviceImpl.getSnowFlakeId((com.weswu.clouduuid.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp>) responseObserver);
          break;
        case METHODID_GET_CACHED_SNOW_FLAKE_ID:
          serviceImpl.getCachedSnowFlakeId((com.weswu.clouduuid.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<com.weswu.clouduuid.grpc.UuidResp>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_CREATE_SEQUENTIALL_ID_SPACE,
        METHOD_DELETE_SEQUENTIALL_ID_SPACE,
        METHOD_DELETE_ALL_ID_SPACES,
        METHOD_GET_SEQUENTIAL_ID,
        METHOD_GET_SNOW_FLAKE_ID,
        METHOD_GET_CACHED_SNOW_FLAKE_ID);
  }

}
