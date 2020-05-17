package com.weswu.clouduuid.service;

import com.weswu.clouduuid.dao.UuidSpaceDao;
import com.weswu.clouduuid.entity.SpaceBuffer;
import com.weswu.clouduuid.entity.UuidRequest;
import com.weswu.clouduuid.entity.UuidSpace;
import com.weswu.clouduuid.entity.SBuffersMgmt;
import com.weswu.clouduuid.utils.LoadProps;
import com.weswu.clouduuid.utils.Miscs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class SequentialIdService implements UuidService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static SequentialIdService instance;
    private ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new updateThreadFactory());
    private Map<String, SBuffersMgmt> cache = new ConcurrentHashMap<String, SBuffersMgmt>();
    private static Properties props;

    public static SequentialIdService getInstance() {
        if (instance == null) {
            synchronized (SequentialIdService .class) {
                if (instance == null) {
                    instance = new SequentialIdService();
                }
            }
        }
        return instance;
    }
    private SequentialIdService(){
        try{
            this.props = LoadProps.fromAppPros();
            this.initializeTheCache();
            this.updateCacheBufferFromDbTask();

        }catch (Exception ex){
            logger.error("Failed to initialize the class {}, {}", this.getClass().getName(), ex.getMessage());
        }

    }

    private static class updateThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;
        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-UuidSpace-Update-" + nextThreadNum());
        }
    }

    private void initializeTheCache() throws Exception {
        List<UuidSpace> uuidSpaces = UuidSpaceDao.updateAll();
        if(null == uuidSpaces || uuidSpaces.size() ==0 ){
            return;
        }
        uuidSpaces.forEach(us -> cache.put(us.getSpaceKey(), SBuffersMgmt.fromUuidSpace(us,0)));
    }

    private void updateCacheBufferFromDbTask() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("update-id-space-buffer-thread");
                t.setDaemon(true);
                return t;
            }
        });
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                updateCacheBufferFromDb();
            }
        }, 30, 10, TimeUnit.SECONDS);
    }

    private void updateCacheBufferFromDb() {
        try {
            List<UuidSpace> uuidSpaces = UuidSpaceDao.getAll();
            List<String> allSpaceKeys = new ArrayList<String>(){};
            uuidSpaces.forEach(us -> { allSpaceKeys.add(us.getSpaceKey());});
            if (allSpaceKeys.isEmpty()) {
                return;
            }
            List<String> cachedSpaceKeys = new ArrayList<String>(cache.keySet());
            Set<String> newSpacesSet = new HashSet<>(allSpaceKeys);
            Set<String> invalidSpacesSet = new HashSet<>(cachedSpaceKeys);
            // figure out the new keys
            for(int i = 0; i < cachedSpaceKeys.size(); i++){
                String tmp = cachedSpaceKeys.get(i);
                if(newSpacesSet.contains(tmp)){
                    newSpacesSet.remove(tmp);
                }
            }
            // update the new keys directly
            for (String spaceKey : newSpacesSet) {
                UuidSpace curtUS = Miscs.findByProperty(uuidSpaces, us -> us.getSpaceKey().equals(spaceKey));
                UuidSpace newUs = UuidSpaceDao.update(curtUS);
                SBuffersMgmt bufferMgmt = SBuffersMgmt.fromUuidSpace(newUs,0);
                cache.put(spaceKey, bufferMgmt);
                logger.info("add uuid space {} {} from db to cache buffer", spaceKey, bufferMgmt.toString());
            }
            // figure out the invalid keys
            for(int i = 0; i < allSpaceKeys.size(); i++){
                String tmp = allSpaceKeys.get(i);
                if(invalidSpacesSet.contains(tmp)){
                    invalidSpacesSet.remove(tmp);
                }
            }
            // remove the invalid keys
            for (String spaceKey : invalidSpacesSet) {
                cache.remove(spaceKey);
                logger.info("remove uuid space {} from cache buffer", spaceKey);
            }
            // update all existing keys
            cachedSpaceKeys = new ArrayList<String>(cache.keySet());
            for(int i = 0; i < cachedSpaceKeys.size(); i++){
                String spaceKey = allSpaceKeys.get(i);
                if(newSpacesSet.contains(spaceKey)){
                    continue;
                }
                SBuffersMgmt sBuffersMgmt = cache.get(spaceKey);
                upateOneUuidSpaceBuffer(spaceKey, sBuffersMgmt);
            }

        } catch (Exception ex) {
            logger.warn("update id space buffer from db exception", ex.getMessage());
        } finally {
        }
    }

    private void upateOneUuidSpaceBuffer(String spaceKey, SBuffersMgmt sBuffersMgmt) throws Exception {
        long bufferBaseLv = Long.parseLong(props.getProperty("uuid.space.buufer.base.level"));
        SpaceBuffer activeBuffer = sBuffersMgmt.getActiveBuffer();
        SpaceBuffer idleBuffer = sBuffersMgmt.getIdleBuffer();
        long curtPos = activeBuffer.getCurtPos().get();
        if((activeBuffer.getMaxId()- curtPos ) <= bufferBaseLv
                && (idleBuffer.isAvailable() == false || activeBuffer.getMinId() >= idleBuffer.getMaxId())){
            UuidSpace newUS = UuidSpaceDao.update(UuidSpace.fromSpaceKey(spaceKey));
            idleBuffer.wLock().lock();
            try {
                idleBuffer.update(newUS.getMinId(), newUS.getMaxId());
                cache.put(spaceKey, sBuffersMgmt);
            }finally {
                idleBuffer.wLock().unlock();
            }
            logger.info("update uuid space {} {} from db to cache buffer", spaceKey, sBuffersMgmt.toString());
        }
    }

    @Override
    public long getUuid(String key) throws Exception {
        if (cache.containsKey(key)) {
            SBuffersMgmt sBuffersMgmt = cache.get(key);
            SpaceBuffer activeBuffer = sBuffersMgmt.getActiveBuffer();
            long nextUuid = activeBuffer.getCurtPos().getAndIncrement();
            if (nextUuid <= activeBuffer.getMaxId()) {
                return nextUuid;
            } else {
                upateOneUuidSpaceBuffer(key, sBuffersMgmt);
                sBuffersMgmt.switchBufferPos();
                activeBuffer = sBuffersMgmt.getActiveBuffer();
                nextUuid = activeBuffer.getCurtPos().getAndIncrement();
                return nextUuid;

            }
        }
        String errMsg = String.format("the key %s does not exist", key);
        throw new Exception(errMsg);
    }

    @Override
    public void deleteKey(UuidRequest uuidRequest) throws Exception {
        UuidSpace uuidSpace = new UuidSpace(uuidRequest.getNameSpace(), uuidRequest.getTag(), "");
        UuidSpaceDao.delete(uuidSpace);
        cache.remove(uuidRequest.getSpaceKey());
        logger.info("successfully deleted the uuid space {}.", uuidRequest.getSpaceKey());
    }

    @Override
    public void createKey(UuidRequest uuidRequest) throws Exception {
        UuidSpace uuidSpace = new UuidSpace(uuidRequest.getNameSpace(), uuidRequest.getTag(), uuidRequest.getDescription());
        UuidSpace uuidSpaceResp = UuidSpaceDao.save(uuidSpace);
        cache.put(uuidSpaceResp.getSpaceKey(), SBuffersMgmt.fromUuidSpace(uuidSpaceResp,0));
        logger.info("successfully created the uuid space {}.", uuidRequest.getSpaceKey());
    }

    @Override
    public void deleteAll() throws Exception {
        UuidSpaceDao.deleteAll();
        cache.clear();
        logger.info("successfully created all uuid spaces.");
    }


}
