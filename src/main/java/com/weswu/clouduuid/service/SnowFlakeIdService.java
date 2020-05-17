package com.weswu.clouduuid.service;
import com.weswu.clouduuid.dao.UuidSpaceDao;
import com.weswu.clouduuid.entity.UuidRequest;
import com.weswu.clouduuid.utils.LoadProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SnowFlakeIdService  implements UuidService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static SnowFlakeIdService instance;
    private static Properties props;
    // 2020-05-01
    private final long twepoch = 1588291200000L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long workerId;
    private long datacenterId;
    private AtomicLong sequence = new AtomicLong(0l);
    private AtomicLong lastTimestamp = new AtomicLong(0l);
    private static final Random RANDOM = new Random();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();


    public static SnowFlakeIdService getInstance() {
        if (instance == null) {
            synchronized (SnowFlakeIdService.class) {
                if (instance == null) {
                    instance = new SnowFlakeIdService();
                }
            }
        }
        return instance;
    }
    private SnowFlakeIdService(){
        try{
            this.props = LoadProps.fromAppPros();
            this.workerId = Long.parseLong(props.getProperty("worker.id"));
            this.datacenterId = Long.parseLong(props.getProperty("datacenter.id"));
        }catch (Exception ex){
            logger.error("Failed to initialize the class {}, {}", this.getClass().getName(), ex.getMessage());
        }finally {

        }

    }

    private long tilNextMillis(long lastTimestamp) throws Exception {
        long timestamp = UuidSpaceDao.getUnixMillisNow();
        while (timestamp <= lastTimestamp) {
            timestamp = UuidSpaceDao.getUnixMillisNow();
        }
        return timestamp;
    }

    @Override
    public long getUuid(String key) throws Exception {
        long timestamp = UuidSpaceDao.getUnixMillisNow();
        if (lastTimestamp.get() == timestamp) {
            sequence.set((sequence.get() +1 ) & sequenceMask);
            if (sequence.get() == 0) {
                sequence.set(RANDOM.nextInt(100));
                timestamp = tilNextMillis(lastTimestamp.get());
            }
        } else {
            sequence.set(RANDOM.nextInt(100));
        }
        lastTimestamp.set(timestamp);
        long id = ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence.get();
        return id;
    }

    @Override
    public void deleteKey(UuidRequest uuidRequest) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        throw new Exception("Does not support the function: " + name + ".");
    }

    @Override
    public void createKey(UuidRequest uuidRequest) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        throw new Exception("Does not support the function: " + name + ".");
    }

    @Override
    public void deleteAll() throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        throw new Exception("Does not support the function: " + name + ".");
    }
}
