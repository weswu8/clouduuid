package com.weswu.clouduuid.service;

import com.weswu.clouduuid.dao.UuidSpaceDao;
import com.weswu.clouduuid.entity.*;
import com.weswu.clouduuid.utils.LoadProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedSnowFlakeIdService implements UuidService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static CachedSnowFlakeIdService instance;
    private ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new updateThreadFactory());
    private Map<String, TimeBufferMgmt> cache = new ConcurrentHashMap<String, TimeBufferMgmt>();
    private final String timeSpaceKey = "clouduuid-snowflake";
    private final long timeToleranceInMills = 200;
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
    private AtomicLong sequence = new AtomicLong(0);
    private AtomicLong lastTrueTimestamp = new AtomicLong(0l);
    private static final Random RANDOM = new Random();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();


    public static CachedSnowFlakeIdService getInstance() {
        if (instance == null) {
            synchronized (CachedSnowFlakeIdService.class) {
                if (instance == null) {
                    instance = new CachedSnowFlakeIdService();
                }
            }
        }
        return instance;
    }
    private CachedSnowFlakeIdService(){
        try{
            this.props = LoadProps.fromAppPros();
            this.workerId = Long.parseLong(props.getProperty("worker.id"));
            this.datacenterId = Long.parseLong(props.getProperty("datacenter.id"));
            this.initializeTheCache();
            this.updateCacheBufferFromDbTask();
        }catch (Exception ex){
            logger.error("Failed to initialize the class {}, {}", this.getClass().getName(), ex.getMessage());
        }finally {

        }
    }

    private static class updateThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;
        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-TureTime-Update-" + nextThreadNum());
        }
    }

    private void initializeTheCache() throws Exception {
        long curtTimeInMills = UuidSpaceDao.getUnixMillisNow();
        TimeBuffer timeBuffer = new TimeBuffer(curtTimeInMills);
        lastTrueTimestamp.set(curtTimeInMills);
        cache.put(timeSpaceKey, new TimeBufferMgmt(timeBuffer, 0));
    }

    private void updateCacheBufferFromDbTask() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("update-true-time-buffer-thread");
                t.setDaemon(true);
                return t;
            }
        });
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                updateCacheBufferFromDb();
            }
        }, 3000, 200, TimeUnit.MILLISECONDS);
    }

    private void updateCacheBufferFromDb() {
        try {
            TimeBufferMgmt timeBufferMgmt = cache.get(timeSpaceKey);
            TimeBuffer idleBuffer = timeBufferMgmt.getIdleBuffer();
            idleBuffer.wLock().lock();
            try {
                long timestamp = UuidSpaceDao.getUnixMillisNow();
                idleBuffer.setCurtTimeInMills(new AtomicLong(timestamp));
            }finally {
                idleBuffer.wLock().unlock();
            }
        } catch (Exception ex) {
            logger.warn("update time buffer from db exception", ex.getMessage());
        } finally {
        }
    }

    @Override
    public long getUuid(String key) throws Exception {
        TimeBufferMgmt timeBufferMgmt = cache.get(timeSpaceKey);
        TimeBuffer activeBuffer = timeBufferMgmt.getActiveBuffer();
        lastTrueTimestamp = activeBuffer.getCurtTimeInMills();
        long curtTimestamp = System.currentTimeMillis();
        if (curtTimestamp - lastTrueTimestamp.get() < timeToleranceInMills ) {
            sequence.set((sequence.get() +1 ) & sequenceMask);
            if (sequence.get() == 0) {
                throw new Exception("reach the allowed limit in the time span");
            }
        } else {
            if(timeBufferMgmt.switchBufferPos()){
                sequence.set(RANDOM.nextInt(100));
            }else{
                sequence.set((sequence.get() +1 ) & sequenceMask);
                if (sequence.get() == 0) {
                    throw new Exception("reach the allowed limit in the time span");
                }
            }
        }
        long id = ((lastTrueTimestamp.get() - twepoch) << timestampLeftShift) //
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
