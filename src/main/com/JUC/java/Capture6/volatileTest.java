package Capture6;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName :   volatileTest
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/20 10:53
 * @Version: 1.0
 */
@Slf4j
public class volatileTest {
    // 增加t1线程, 对主线程更改run变量的可见性
    // 一开始一直不结束, 是因为无限循环, run都是true, JIT及时编译器, 会对t1线程所执行的
    // run变量,进行缓存, 缓存到本地工作内存. 不去访问主存中的run. 这样可以提高性能; 也可以说是JVM打到一定阈值之后
    // while(true)变成了一个热点代码, 所以一直访问的都是缓存到本地工作内存(局部)中的run. 当主线程修改主存中的run变量的时候,
    // t1线程一直访问的是自己缓存的, 所以不认为run已经改为false了. 所以一直运行. 我们为主存(成员变量)进行volatile修饰, 增加
    // 变量的可见性, 当主线程修改run为false, t1线程对run的值可见. 这样就可以退出循环
//    volatile static boolean run = true;
    static boolean run = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (run) {
                // 如果打印一句话
                // 此时就可以结束, 因为println方法中, 使用到了synchronized
                // synchronized可以保证原子性、可见性、有序性
                // System.out.println("123");
            }
        });

        t1.start();
        Thread.sleep(1000);
        run = false;
        System.out.println(run);
    }
}
@Slf4j
class Test1 {
    static boolean run = true;
    final static Object obj = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            // 1s内,一直都在无限循环获取锁. 1s后主线程抢到锁,修改为false, 此时t1线程抢到锁对象,while循环也退出
            while (run) {
                synchronized (obj) {

                }
            }
        });

        t1.start();
        Thread.sleep(1);
        // 当主线程获取到锁的时候, 就修改为false了
        synchronized (obj) {
            run = false;
            System.out.println("false");
        }
    }
}
