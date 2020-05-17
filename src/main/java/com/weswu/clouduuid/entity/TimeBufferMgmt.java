package com.weswu.clouduuid.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Setter
@Getter
@AllArgsConstructor
public class TimeBufferMgmt {
    private volatile int bufferPos;
    private volatile TimeBuffer[] timeBuffers;
    private final ReadWriteLock lock;

    public TimeBufferMgmt(TimeBuffer timeBuffer, int bufferPos) {
        this.bufferPos = bufferPos;
        this.timeBuffers = new TimeBuffer[2];
        this.timeBuffers[bufferPos] = timeBuffer;
        TimeBuffer idleBuffer = new TimeBuffer(0l);
        this.timeBuffers[1-bufferPos] = idleBuffer;
        this.lock = new ReentrantReadWriteLock();
    }
    public TimeBuffer getActiveBuffer(){
        return this.timeBuffers[this.bufferPos];
    }
    public TimeBuffer getIdleBuffer(){
        return this.timeBuffers[1-this.bufferPos];
    }

    public void setIdleBuffer(TimeBuffer timeBuffer){
        this.timeBuffers[1-this.bufferPos] = timeBuffer;
    }

    public boolean switchBufferPos() {
        this.wLock().lock();
        try{
            int nbPos = 1- this.bufferPos;
            if(getActiveBuffer().getCurtTimeInMills().get() < getIdleBuffer().getCurtTimeInMills().get()) {
                this.bufferPos = nbPos;
                return true;
            }
        }finally {
            this.wLock().unlock();
        }
        return false;
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TimeBufferMgmt{");
        sb.append("bufferPos=").append(String.valueOf(bufferPos));
        sb.append(", TimeBuffer0=").append(timeBuffers[0].toString());
        sb.append(", TimeBuffer1=").append(timeBuffers[1].toString());
        sb.append('}');
        return sb.toString();
    }
}
