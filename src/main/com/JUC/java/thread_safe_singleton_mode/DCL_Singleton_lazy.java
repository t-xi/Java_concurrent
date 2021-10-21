package thread_safe_singleton_mode;

/**
 * @ClassName :   Singleton_lazy
 * @Description: 双重检查——懒汉模式
 * @Author: TX
 * @CreateDate: 2021/10/21 12:49
 * @Version: 1.0
 */
public final class DCL_Singleton_lazy {
    //私有构造器
    private DCL_Singleton_lazy(){};
    //私有静态实例
    // 问题1：解释为什么要加 volatile ?为了防止重排序问题
    private static volatile DCL_Singleton_lazy INSTANCE = null;
    // 问题2：对比实现3, 说出这样做的意义：提高了效率
    public static DCL_Singleton_lazy getInstance(){
        if(INSTANCE!=null){
            if(INSTANCE!=null){
                return INSTANCE;
            }
        }
        synchronized(DCL_Singleton_lazy.class){//是静态的
            if(INSTANCE!=null){// 问题3：为什么还要在这里加为空判断, 之前不是判断过了吗？这是为了第一次判断时的并发问题。
                return INSTANCE;
            }
            INSTANCE = new DCL_Singleton_lazy();
            return INSTANCE;
        }

    }
}
//问题1 : 因为在synchronized外部使用到了共享变量INSTANCE, 所以synchronized无法保证instance的有序性, 又因为instance = new Singleton()不是一个原子操作, 可分为多个指令. 此时通过指令重排, 可能会造成INSTANCE还未初始化, 就赋值的现象, 所以要给共享变量INSTANCE加上volatile,禁止指令重排
//问题2 : 增加了双重判断, 如果存在了单例对象, 别的线程再进来就无需加锁判断, 大大提高性能
//问题3 : 防止多线程并发导致不安全的问题:防止单例对象被重复创建. 当t1,t2线程都调用getInstance()方法, 它们都判断单例对象为空, 还没有创建;
//
//此时t1先获取到锁对象, 进入到synchronized中, 此时创建对象, 返回单例对象, 释放锁;
//这时候t2获得了锁对象, 如果在代码块中没有if判断, 则线程2认为没有单例对象, 因为在代码块外判断的时候就没有, 所以t2就还是会创建单例对象. 此时就重复创建了
