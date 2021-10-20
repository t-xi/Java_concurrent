import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName :   BalkingModel
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/20 14:33
 * @Version: 1.0
 */
public class BalkingModel {
    public static void main(String[] args) throws InterruptedException {

        Monitor monitor = new Monitor();
        monitor.start();
        monitor.start();
        monitor.start();
        Thread.sleep(3500);
        monitor.stop();
    }
}

@Slf4j
class MonitorBalking{

    Thread monitor;
    //设置标记，用于判断是否被终止了
    private volatile boolean stop = false;
    //设置标记，用于判断是否已经启动过了
    private boolean starting = false;
    /**
     * 启动监控器线程
     */
    public void start(){
        //上锁，避免多线程运行时出实现线程安全的问题
        synchronized (this){
            //已经启动，直接返回
            if(starting){
                return;
            }
            //启动监视器，改变标记
            starting = true;
        }
        //设置线控器线程，用于监控线程状态
        monitor = new Thread(() -> {
            //开始不同的监控
            while (true) {
                if(stop){
                    log.debug("料理后事");
                    break;
                }
                log.debug("监控器运行中...");
                try {
                    //线程休眠
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    log.debug("被打断了。。。");
                }
            }
        });
    }

    /**
     * 	用于停止监控器线程
     */
    public void stop() {
        //打断线程
        stop = true;
        monitor.interrupt();
    }
}
