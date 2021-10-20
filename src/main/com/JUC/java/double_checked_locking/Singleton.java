package double_checked_locking;

/**
 * @ClassName :   Singleton
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/20 16:38
 * @Version: 1.0
 */
//一般的单例模式--懒汉
public class Singleton {
    private Singleton(){}
    private static Singleton INSTANCE = null;
    public  static Singleton getInstance(){
        /*
	      多线程同时调用getInstance(), 如果不加synchronized锁, 此时两个线程同时
	      判断INSTANCE为空, 此时都会new Singleton(), 此时就破坏单例了.所以要加锁,
	      防止多线程操作共享资源,造成的安全问题
	     */
        synchronized (Singleton.class){
            if(INSTANCE == null){
                INSTANCE = new Singleton();
            }
            return INSTANCE;
        }
    }
}
/*
	首先上面代码的效率是有问题的, 因为当我们创建了一个单例对象后, 又来一个线程获取到锁了,还是会加锁,
	严重影响性能,再次判断INSTANCE==null吗, 此时肯定不为null, 然后就返回刚才创建的INSTANCE;
	这样导致了很多不必要的判断;

	所以要双重检查, 在第一次线程调用getInstance(), 直接在synchronized外,判断instance对象是否存在了,
	如果不存在, 才会去获取锁,然后创建单例对象,并返回; 第二个线程调用getInstance(), 会进行
	if(instance==null)的判断, 如果已经有单例对象, 此时就不会再去同步块中获取锁了. 提高效率
*/
final class Singleton2 {
    private Singleton2() { }
    private static Singleton2 INSTANCE = null;
    public static Singleton2 getInstance() {
        if(INSTANCE == null) { // t2
            // 首次访问会同步，而之后的使用没有 synchronized
            synchronized(Singleton2.class) {
                if (INSTANCE == null) { // t1
                    INSTANCE = new Singleton2();
                }
            }
        }
        return INSTANCE;
    }
}
//但是上面的if(INSTANCE == null)判断代码没有在同步代码块synchronized中，
// 不能享有synchronized保证的原子性、可见性、以及有序性。所以可能会导致 指令重排
