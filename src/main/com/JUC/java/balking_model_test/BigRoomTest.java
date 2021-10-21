package balking_model_test;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName :   BigRoomTest
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/21 11:49
 * @Version: 1.0
 */
@Slf4j
public class BigRoomTest {
    volatile static boolean initialized = false;
    final static Object obj = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            init();
        }, "t1").start();

        new Thread(() -> {
            init();
        }, "t2").start();
    }

    static void init(){
        synchronized (obj){
            if(initialized){
                return;
            }
            doInit();
            initialized = true;
        }
    }
    private static void doInit(){
        log.debug("init....");
    }
}
