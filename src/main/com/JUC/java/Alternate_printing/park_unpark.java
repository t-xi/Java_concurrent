package Alternate_printing;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName :   park_unpark
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/19 22:29
 * @Version: 1.0
 */
@Slf4j
public class park_unpark {
    static Thread a;
    static Thread b;
    static Thread c;
    public static void main(String[] args) {
        ParkUnpark parkUnpark = new ParkUnpark(5);

        a = new Thread(() -> {
            parkUnpark.print("a", b);
        }, "a");

        b = new Thread(() -> {
            parkUnpark.print("b", c);
        }, "b");

        c = new Thread(() -> {
            parkUnpark.print("c", a);
        }, "c");

        a.start();
        b.start();
        c.start();

        LockSupport.unpark(a);

    }


}
class ParkUnpark{
    private final int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread nextThread){
        for(int i = 0; i < loopNumber; i++){
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(nextThread);
        }
    }
}