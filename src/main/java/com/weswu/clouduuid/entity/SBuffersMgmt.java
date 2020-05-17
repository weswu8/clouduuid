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
public class SBuffersMgmt {
    private volatile int bufferPos;
    private volatile SpaceBuffer[] spaceBuffers;
    private final ReadWriteLock lock;

    public SBuffersMgmt(SpaceBuffer spaceBuffer, int bufferPos) {
        this.bufferPos = bufferPos;
        this.spaceBuffers = new SpaceBuffer[2];
        this.spaceBuffers[bufferPos] = spaceBuffer;
        SpaceBuffer idleBuffer = new SpaceBuffer(0l, 0l);
        idleBuffer.setAvailable(false);
        this.spaceBuffers[1-bufferPos] = idleBuffer;
        this.lock = new ReentrantReadWriteLock();
    }

    public static SBuffersMgmt fromUuidSpace(UuidSpace uuidSpace, int bufferPos){
        SpaceBuffer spaceBuffer = new SpaceBuffer(uuidSpace.getMinId(), uuidSpace.getMaxId());
        SBuffersMgmt usb = new SBuffersMgmt(spaceBuffer, 0);
        return  usb;
    }

    public SpaceBuffer getActiveBuffer(){
        return this.spaceBuffers[this.bufferPos];
    }
    public SpaceBuffer getIdleBuffer(){
        return this.spaceBuffers[1-this.bufferPos];
    }

    public void setIdleBuffer(SpaceBuffer spaceBuffer){
        this.spaceBuffers[1-this.bufferPos] = spaceBuffer;
    }

    public void switchBufferPos() {
        while (true){
            this.wLock().lock();
            try{
                int nbPos = 1- this.bufferPos;
                if(spaceBuffers[nbPos].isAvailable()) {
                    this.bufferPos = nbPos;
                    getIdleBuffer().setAvailable(false);
                    break;
                }
            }finally {
                this.wLock().unlock();
            }
            try{
                Thread.sleep(500);
            }catch (Exception ex){}

        }

    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpaceBuffersMgmt{");
        sb.append("bufferPos=").append(String.valueOf(bufferPos));
        sb.append(", SpaceBuffer0=").append(spaceBuffers[0].toString());
        sb.append(", SpaceBuffer1=").append(spaceBuffers[1].toString());
        sb.append('}');
        return sb.toString();
    }
}