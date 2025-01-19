package com.springboot.booking_app.common;

public interface DistributedLockService {

    boolean acquireLock(String idempotentKey, Long lockTime);

    void releaseLock(String idempotentKey);
}
