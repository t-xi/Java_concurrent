
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
/**
 * @ClassName :   test20
 * @Description: 多线程同步模式 - 一个线程需要等待另一个线程的执行结果
 * @Author: TX
 * @CreateDate: 2021/10/16 16:08
 * @Version: 1.0
 */


class GuardeObjectTest {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(GuardeObjectTest.class);
        // 线程1等待线程2的下载结果
        GuardeObject guardeObject = new GuardeObject();
        new Thread(() -> {
            log.debug("等待结果");
            List<String> list = (List<String>) guardeObject.get();
            log.debug("结果大小:{}");
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行下载");

                List<String> list = Downloader.download();
                guardeObject.complete(list);
        }, "t2").start();
    }
}

class GuardeObject {
    // 结果
    private Object response;
    // 获取结果
    public Object get() {
        synchronized (this) {
            // 防止虚假唤醒
            // 没有结果
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }
    // 获取结果
    public Object get(long timeout) {
        synchronized (this) {
            // 防止虚假唤醒
            // 没有结果
            long begin = System.currentTimeMillis();
            //经历时间
            long passedTime = 0;
            while (response == null) {
                long waitTime = timeout - passedTime;
                if(waitTime<=0){
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            passedTime = System.currentTimeMillis() - begin;
            return response;
        }
    }
    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            // 给结果变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}

class Downloader {
    static  List<String> download(){
        System.out.println("下载...");
        List<String> res = new ArrayList<>();
        res.add("result");
        return res;
    }
}