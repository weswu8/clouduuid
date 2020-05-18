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
* the default bits of that three fields are as delta seconds, datacenter id, worker id, sequence

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
	you should have a gcp account, and you should create and download the service account json file.
	[how to do this?](https://cloud.google.com/docs/authentication/getting-started). pls put the file 
	into the class resource directory
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

### 3.final.Start the service
    java -jar clouduuid-1.0.0-SNAPSHOT.jar
	

## Reference
*  Leaf from meituan
*  uid-gernartor from baidu

## Limitation and known issues:
* bug of the imported plug "opencensus-exporter-trace-stackdriver"

