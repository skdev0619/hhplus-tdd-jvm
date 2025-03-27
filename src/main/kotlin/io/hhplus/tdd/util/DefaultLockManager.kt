package io.hhplus.tdd.util

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

@Component
class DefaultLockManager : LockManager {

    private val lockMap: ConcurrentHashMap<Long, ReentrantLock> = ConcurrentHashMap()

    override fun <T> executeWithLock(key: Long, action: () -> T): T {
        val lock = lockMap.computeIfAbsent(key) { ReentrantLock() }
        lock.lock()

        return try {
            action()
        } finally {
            lock.unlock()
        }
    }
}
