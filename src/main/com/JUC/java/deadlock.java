/**
 * @ClassName :   deadlock
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/17 16:54
 * @Version: 1.0
 */
public class deadlock {
    public static void main(String[] args) {
        final Object A = new Object();
        final Object B = new Object();

        new Thread(()->{
            synchronized (A) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {

                }
            }
        }).start();

        new Thread(()->{
            synchronized (B) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {

                }
            }
        }).start();
    }
}
