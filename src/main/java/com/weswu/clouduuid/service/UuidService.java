package com.weswu.clouduuid.service;

import com.weswu.clouduuid.entity.UuidRequest;

public interface UuidService {
    public long getUuid(String key) throws Exception;
    public void deleteKey(UuidRequest uuidRequest) throws Exception;
    public void createKey(UuidRequest uuidRequest) throws Exception;
    public void deleteAll() throws Exception;
}
