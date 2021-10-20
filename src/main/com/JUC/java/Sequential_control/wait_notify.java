package Sequential_control;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName :   wait_notify
 * @Description: 使用wait/notify来实现顺序打印 2, 1
 * @Author: TX
 * @Cre
 * ateDate: 2021/10/19 21:27
 * @Version: 1.0
 */
@Slf4j
public class wait_notify {
    //锁对象
    public static final Object lock = new Object();
    //t2线程释放执行过了
    //保证t2先于t1执行
    public static boolean t2Runed = false;
    public static void main(String[] args){
        //创建t1线程
        Thread t1 = new Thread(()->{
            synchronized (lock){
                //如果t2没有执行过，wait
                while(t2Runed==false){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        },"t1");
        Thread t2 = new Thread(()->{
            synchronized (lock){
                log.debug("2");
                t2Runed = true;
                lock.notifyAll();
            }
        },"t2");
        t1.start();
        t2.start();
    }
}
