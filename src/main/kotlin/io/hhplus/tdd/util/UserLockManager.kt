package io.hhplus.tdd.util

interface LockManager {
    fun <T> executeWithLock(key: Long, action: () -> T): T
}