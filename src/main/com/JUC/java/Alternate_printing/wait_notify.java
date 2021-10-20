package Alternate_printing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName :   wait_notify
 * @Description: 线程1 输出 a 5次, 线程2 输出 b 5次, 线程3 输出 c 5次。现在要求输出 abcabcabcabcabcabc
 * @Author: TX
 * @CreateDate: 2021/10/19 22:04
 * @Version: 1.0
 */
@Slf4j
public class wait_notify {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()->{
            waitNotify.print("a",1,2);
        },"t1").start();
        new Thread(()->{
            waitNotify.print("b",2,3);
        },"t2").start();
        new Thread(()->{
            waitNotify.print("c",3,1);
        },"t3").start();
    }
}
@Slf4j
@Data
@AllArgsConstructor
class WaitNotify{
    private int flag;
    // 循环次数
    private int loopNumber;
    /*
        输出内容    等待标记    下一个标记
        a           1          2
        b           2          3
        c           3          1
     */
    public void print(String str,int waitFlag,int nextFlag){
        for(int i=0;i<loopNumber;i++){
            synchronized (this){
                while(waitFlag != this.flag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //当前线程打印
                System.out.println(str);
                //标志位变为下一个线程
                this.flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}