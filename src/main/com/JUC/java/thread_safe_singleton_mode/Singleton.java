package thread_safe_singleton_mode;

import java.io.Serializable;

/**
 * @ClassName :   Singleton
 * @Description: 饿汉模式
 * @Author: TX
 * @CreateDate: 2021/10/21 11:57
 * @Version: 1.0
 */
// 问题1：为什么加 final，防止子类继承后更改
// 问题2：如果实现了序列化接口, 还要做什么来防止反序列化破坏单例，如果进行反序列化的时候会生成新的对象，这样跟单例模式生成的对象是不同的。
//       要解决直接加上readResolve()方法就行了，如下所示
public final class Singleton implements Serializable {
    // 问题3：为什么设置为私有? 放弃其它类中使用new生成新的实例，是否能防止反射创建新的实例?不能。
    private Singleton(){}
    // 问题4：这样初始化是否能保证单例对象创建时的线程安全?没有，这是类变量，是jvm在类加载阶段就进行了初始化，jvm保证了此操作的线程安全性
    private static final Singleton INSTANCE = new Singleton();
    // 问题5：为什么提供静态方法而不是直接将 INSTANCE 设置为 public, 说出你知道的理由。
    //1.提供更好的封装性；2.提供范型的支持
    public static Singleton getInstance(){
        return INSTANCE;
    }
    public Object readResolve(){
        return INSTANCE;
    }
}
//问题1 : 加final为了防止有子类, 因为子类可以重写父类的方法
//问题2 : 首先通过反序列化操作, 也是可以创建一个对象的, 破坏了单例, 可以使用readResolve方法并返回instance对象, 当反序列化的时候就会调用自己写的readResolve方法
//问题3 : 私有化构造器, 防止外部通过构造器来创建对象; 但不能防止反射来创建对象
//问题4 : 因为单例对象是static的, 静态成员变量的初始化操作是在类加载阶段完成, 由JVM保证其线程安全 (这其实是利用了ClassLoader的线程安全机制。ClassLoader的loadClass方法在加载类的时候使用了synchronized关键字。)
//问题5 : 通过向外提供公共方法, 体现了更好的封装性, 可以在方法内实现懒加载的单例; 可以提供泛型等
//补充 : 任何一个readObject方法，不管是显式的还是默认的，它都会返回一个新建的实例，这个新建的实例不同于该类初始化时创建的实例。
