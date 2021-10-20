import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   ReentrantTest
 * @Description: reentrantLock是可重入锁，线程在获取锁后可以再次获取这把锁
 * @Author: TX
 * @CreateDate: 2021/10/17 20:10
 * @Version: 1.0
 */

@Slf4j
public class ReentrantTest {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // 如果有竞争就进入`阻塞队列`, 一直等待着,不能被打断
        lock.lock();
        try {
            log.debug("entry main...");
            m1();
        } finally {
            lock.unlock();
        }
    }

    private static void m1() {
        lock.lock();
        try {
            log.debug("entry m1...");
            m2();
        } finally {
            lock.unlock();
        }
    }

    private static void m2() {
        log.debug("entry m2....");
    }
}
