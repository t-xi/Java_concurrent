package Sequential_control;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   park_unpark
 * @Description: 使用apark_unpark来实现顺序打印 2, 1
 * @Author: TX
 * @Cre
 * ateDate: 2021/10/19 21:27
 * @Version: 1.0
 */
@Slf4j
public class park_unpark {
    public static void main(String[] args){
        //创建t1线程
        Thread t1 = new Thread(()->{
            //使线程一阻塞
            LockSupport.park();
            log.debug("1");
        },"t1");
        Thread t2 = new Thread(()->{
            log.debug("2");
            LockSupport.unpark(t1);//可以唤醒指定线程
        },"t2");
        t1.start();
        t2.start();
    }
}
