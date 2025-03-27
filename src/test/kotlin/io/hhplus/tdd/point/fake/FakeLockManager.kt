package io.hhplus.tdd.point.fake

import io.hhplus.tdd.util.LockManager

class FakeLockManager : LockManager {
    override fun <T> executeWithLock(key: Long, action: () -> T): T {
        return action()
    }
}
