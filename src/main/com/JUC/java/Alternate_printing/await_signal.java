package Alternate_printing;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   await_signal
 * @Description: 使用await/signal来实现三个线程交替打印abcabcabcabcabc
 * @Author: TX
 * @CreateDate: 2021/10/19 22:17
 * @Version: 1.0
 */
@Slf4j
public class await_signal {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a_condition = awaitSignal.newCondition();
        Condition b_condition = awaitSignal.newCondition();
        Condition c_condition = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", a_condition, b_condition);
        }, "a").start();

        new Thread(() -> {
            awaitSignal.print("b", b_condition, c_condition);
        }, "b").start();

        new Thread(() -> {
            awaitSignal.print("c", c_condition, a_condition);
        }, "c").start();
        Thread.sleep(1000);
        System.out.println("==========开始=========");
        awaitSignal.lock();
        try {
            a_condition.signal();  //首先唤醒a线程
        } finally {
            awaitSignal.unlock();
        }
    }

}
class AwaitSignal extends ReentrantLock{
    private final int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;

    }
    public void print(String str, Condition condition,Condition next){
        for(int i=0;i<loopNumber;i++){
            this.lock();
            try {

                condition.await();//当前线程等带
                System.out.print(str);
                next.signal();//唤醒下一个

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }

    }
}