package thread_safe_singleton_mode;

/**
 * @ClassName :   Singleton5
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/21 13:03
 * @Version: 1.0
 */
public final class Singleton5 {
    private Singleton5(){};
    // 问题1：属于懒汉式还是饿汉式：懒汉式，这是一个静态内部类。
    // 类加载本身就是懒惰的，在没有调用getInstance方法时是没有执行LazyHolder内部类的类加载操作的。
    private static class LazyHolder{
        static final Singleton5 INSTANCE = new Singleton5();
    }
    // 问题2：在创建时是否有并发问题，这是线程安全的，类加载时，jvm保证类加载操作的线程安全
    public static Singleton5 getInstance() {
        return LazyHolder.INSTANCE;
    }

}
//问题 1 : 懒汉式，这是一个静态内部类。类加载本身就是懒惰的，在没有调用getInstance方法时是没有执行LazyHolder内部类的类加载操作的。静态内部类不会随着外部类的加载而加载, 这是静态内部类和静态变量的区别
//问题 2 : 不会有并发问题, 因为是通过类加载创建的单例, JVM保证不会出现线程安全。
