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
public class TimeBuffer {
    private AtomicLong curtTimeInMills;
    private final ReadWriteLock lock;

    public TimeBuffer(long curtTimeInMills){
        this.curtTimeInMills = new AtomicLong(curtTimeInMills);
        this.lock = new ReentrantReadWriteLock();
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TimeBuffer{");
        sb.append("curtTimeInMills=").append(String.valueOf(curtTimeInMills));
        sb.append('}');
        return sb.toString();
    }

}
