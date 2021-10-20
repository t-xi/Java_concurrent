import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;

/**
 * @ClassName :   Livelock
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/17 17:29
 * @Version: 1.0
 */

@Slf4j(topic = "juc.Livelock")
public class  Livelock {
    static volatile int count =10;
    static final Object lock = new Object();

    public static void main(String[] atgs){
        new Thread(()->{
            //期望减到0退出循环
            while(count > 0){
                try {
                    Thread.sleep(100);
                    count--;
                    log.debug("count:{}",count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        new Thread(()->{
            //期望减到0退出循环
            while(count < 20){
                try {
                    Thread.sleep(500);
                    count++;
                    log.debug("count:{}",count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2").start();
    }
}
