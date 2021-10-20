import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   ReentrantTest2
 * @Description: 锁超时 (lock.tryLock()) 直接退出阻塞队列, 获取锁失败
 *                bu
 * @Author: TX
 * @CreateDate: 2021/10/17 20:16
 * @Version: 1.0
 */
@Slf4j
public class ReentrantTest5 {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("t1尝试获得锁");
            // 此时肯定获取失败, 因为主线程已经获得了锁对象
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("获取锁超时，返回");
                    return;
                }
            } catch (InterruptedException e) {
                log.debug("被打断，获取所失败");
                e.printStackTrace();
                return;
            }
            try {
                log.debug("t1获得到锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("main获得到锁");
        t1.start();
        // 主线程2s之后才释放锁
//        t1.interrupt();
        Thread.sleep(3000);
        log.debug("释放了锁");
        lock.unlock();
    }
}
