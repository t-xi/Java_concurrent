import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   ReentrantTest2
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/17 20:16
 * @Version: 1.0
 */
@Slf4j
public class ReentrantTest3 {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("t1线程启动...");
            lock.lock();

            try {
                log.debug("t1线程获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        // 主线程获得锁(此锁不可打断)
        lock.lock();
        log.debug("main线程获得了锁");
        // 启动t1线程
        t1.start();
        try {
            Thread.sleep(1000);
            t1.interrupt();            //打断t1线程--不可打断
            log.debug("main执行打断");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
