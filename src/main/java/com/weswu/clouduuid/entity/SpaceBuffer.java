package com.weswu.clouduuid.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Setter
@Getter
@AllArgsConstructor
public class SpaceBuffer {
    private AtomicLong curtPos;
    private volatile long minId;
    private volatile long maxId;
    private volatile boolean available;
    private final ReadWriteLock lock;

    public SpaceBuffer(long minId, long maxId){
        this.curtPos = new AtomicLong(minId);
        this.minId = minId;
        this.maxId = maxId;
        this.available = true;
        this.lock = new ReentrantReadWriteLock();
    }

    public void update(long minId, long maxId){
        this.curtPos = new AtomicLong(minId);
        this.minId = minId;
        this.maxId = maxId;
        this.available = true;
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpaceBuffer{");
        sb.append("curtPos=").append(String.valueOf(curtPos));
        sb.append(", minId=").append(String.valueOf(minId));
        sb.append(", maxId=").append(String.valueOf(maxId));
        sb.append(", available=").append(String.valueOf(available));
        sb.append('}');
        return sb.toString();
    }

    public SpaceBuffer copy(){
        SpaceBuffer spaceBuffer = new SpaceBuffer(this.minId, this.maxId);
        return spaceBuffer;
    }
}
