package Sequential_control;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   await_signal
 * @Description: 使用await_signal来实现顺序打印 2, 1
 * @Author: TX
 * @Cre
 * ateDate: 2021/10/19 21:27
 * @Version: 1.0
 */
@Slf4j
public class await_signal {
    //锁对象
    public static final ReentrantLock lock = new ReentrantLock();
    //条件变量
    public static Condition condition = lock.newCondition();
    //t2线程释放执行过了
    //保证t2先于t1执行
    public static boolean t2Runed = false;
    public static void main(String[] args){
        //创建t1线程
        Thread t1 = new Thread(()->{
            lock.lock();
            try{
                //如果t2没有执行过，wait
                while(t2Runed==false){
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }finally {
                lock.unlock();
            }

        },"t1");
        Thread t2 = new Thread(()->{
            lock.lock();
            try{
                log.debug("2");
                t2Runed = true;
                condition.signal();
            }finally {
                lock.unlock();
            }
        },"t2");
        t1.start();
        t2.start();
    }
}
