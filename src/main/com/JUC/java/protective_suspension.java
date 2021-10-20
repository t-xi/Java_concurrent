import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName :   protective_suspension
 * @Description: 两阶段终止模式--打断标记--interrupt /// volatile
 * @Author: TX
 * @CreateDate: 2021/10/20 11:59
 * @Version: 1.0
 */
@Slf4j
public class protective_suspension {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.start();
        Thread.sleep(3500);
        monitor.stop();
    }

}
@Slf4j
class Monitor{
    // private boolean stop = false; // 不会停止程序
    private volatile boolean stop = false; // 会停止程序
    /**
     * 启动监控器线程
     */
    public void start() {
        Thread monitor = new Thread(() -> {
            //开始不停的监控
            while (true) {
                if (stop) {
                    log.debug("料理后事---释放锁~~");
                    break;
                }
            }
        });
        monitor.start();
    }
    /**
     * 用于停止监控器线程
     */
    public void stop() {
        stop = true;
    }
}
//class Monitor {
//    Thread monitor;
//    /**
//     * 启动监控器线程
//     */
//    public void start() {
//        //设置线控器线程，用于监控线程状态
//        monitor = new Thread() {
//            @Override
//            public void run() {
//                //开始不停的监控
//                while (true) {
//                    //判断当前线程是否被打断了
//                    if(Thread.currentThread().isInterrupted()) {
//                        System.out.println("处理后续任务");
//                        //终止线程执行
//                        break;
//                    }
//                    System.out.println("监控器运行中...");
//                    try {
//                        //线程休眠
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        //如果是在休眠的时候被打断，不会将打断标记设置为true，这时要重新设置打断标记
//                        Thread.currentThread().interrupt();
//                    }
//                }
//            }
//        };
//        monitor.start();
//    }
//
//    /**
//     * 	用于停止监控器线程
//     */
//    public void stop() {
//        //打断线程
//        monitor.interrupt();
//    }
//}