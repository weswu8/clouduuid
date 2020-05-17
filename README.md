CloudUUID
=====

CloudUUID is a distributed unique ID generator grpc service backed by [Google cloud spanner service](https://cloud.google.com/spanner). it supprot three kinds of algrithm: sequential, snowflake and cache snowflake

Squential ID
-------------
### overview:
Each sequential id belongs to an unique id space, the space can be identified by the combination of the domain and tag. such as: (domain)order + (tag)prod will be the one unique Id space.
* Globally unique
* Scale to multiple workers
* 64 bits number(long)
* Sequential within one worker
* Preallocation and double buffered to achieve high qps

### gprc function:
	rpc createSequentiallIdSpace (CreateSequentialIdSpaceReq) returns (CommonResp) {}
  	rpc deleteSequentiallIdSpace (SequentialIdReq) returns (CommonResp) {}
  	rpc deleteAllIdSpaces (Empty) returns (CommonResp) {}
  	rpc getSequentialId(SequentialIdReq) returns (UuidResp) {}

SnowFlake ID
-------------
### overview:
An unique id consists of worker node, timestamp and sequence within that timestamp. Usually,
it is a 64 bits number(long), the timestamp is based on the true time of the spanner. 
* Based on true time came from spanner system
* Id is generated strictly by true time
* the default bits of that three fields are as follows:
 ***+------+----------------------+----------------+-----------+
 ***| sign |     delta seconds    | worker node id | sequence  |
 ***+------+----------------------+----------------+-----------+
 ***  1bit          41bits              10bits         12bits

### gprc function:
	rpc getSnowFlakeId(Empty) returns (UuidResp) {}

Cached SnowFlake ID
-------------
###  overview:
it is basically the same as the snowflake model except that it takes buffered time.
* Use cached true time from spanner system
* Id is generated within the time diff tolerance
* double buffered to achieve high qps

###  gprc function:
	rpc getCachedSnowFlakeId(Empty) returns (UuidResp) {}


------------

## Tech Features:
* Java with Spring boot framework
* Integration with stackdriver
* Integration with opencensus


## installation
### Precondition
	you should have a gcp account, and you should create and download the service account json file.[how to do this?](https://cloud.google.com/docs/authentication/getting-started). pls put the file into the class resource directory
### 1.create spanner
    create the spanner instance [spnner](https://cloud.google.com/spanner/docs/quickstart-console).
    and create the table:
```sql
CREATE TABLE uuidspace (
  NameSpace STRING(1024) NOT NULL,
  Tag STRING(1024) NOT NULL,
  MinId INT64 NOT NULL,
  MaxId INT64 NOT NULL,
  Description STRING(MAX),
  WorkerId STRING(1024) NOT NULL,
  Updated INT64 NOT NULL
) PRIMARY KEY (NameSpace, Tag)
```
### 2.config the application
	Open application.properties
	change the setting of :

	gcp.project.id:xxx
	gcp.service.account.file:xxx
	spanner.instance:xxx
	spanner.database:xx
	spanner.database.table:uuidspace
	uuid.range.stride:50000
	uuid.space.buufer.base.level:10000
	worker.id:1
	datacenter.id:1
	grpc.port: 8080

### final.Start the blobfs service
    lanuch gcsfuse-win.exe
	
It is highly recommended that you should config it as a windows services.

## Tips
* the block blob is read only by default. marked with read only flag in the popup properties windows.
* due to you can mount the multiple buckets/buckets/folders, so the size number of the mounted folder is not real.


## Performance Test
* The performance depends on the machine and the network. for the VMs within the same region with the blob service, The average bandwidth is 20 ~ 30MB/s

## Dependency
* [WinFsp](https://github.com/billziss-gh/winfsp): Great Windows File System Proxy - FUSE for Windows.


## Limitation and known issues:
* Due to the overhead of fuse system, the performance will be expected slower than native file system. 
* For the file copy, the blobfs will use read out - then write in to new blob mode. this will spent more time for large files/folders.
* In some cases for the desktop user, right-click these files (*.PPT(X), *.DOC(X)) may casue very slow response.
* In Windows UI, copy the folder with many small files will be very slow. you can zip it. use robocopy or other tools.

## Supported platforms
* windows


## Command Line Usage
	usage: Blobfs-Win OPTIONS

	options:
		-d DebugFlags       [-1: enable all debug logs]
		-D DebugLogFile     [file path; use - for stderr]
		-i                  [case insensitive file system]
		-t FileInfoTimeout  [millis]
		-n MaxFileNodes
		-s MaxFileSize      [bytes]
		-F FileSystemName]
		-m MountPoint       [X:|* (required if no UNC prefix)]s

## License
	Copyright (C) 2020 Wesley Wu jie1975.wu@gmail.com
	This code is licensed under The General Public License version 3
	
## FeedBack
	Your feedbacks are highly appreciated! :)
