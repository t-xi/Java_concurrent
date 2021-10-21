package thread_safe_singleton_mode;

/**
 * @ClassName :   Singleton_lazy
 * @Description: 懒汉模式
 * @Author: TX
 * @CreateDate: 2021/10/21 12:49
 * @Version: 1.0
 */
public final class Singleton_lazy {
    //私有构造器
    private Singleton_lazy(){};
    //私有静态实例
    private static Singleton_lazy INSTANCE = null;
    public static synchronized Singleton_lazy getInstance(){
        if(INSTANCE!=null){
            return INSTANCE;
        }
        INSTANCE = new Singleton_lazy();
        return INSTANCE;
    }
}
//上面是一个懒汉式的单例, 代码存在性能问题: 当单例对象已经创建好了, 多个线程访问getInstance()方法, 仍然会获取锁, 同步操作, 性能很低, 此时出现重复判断, 因此要使用双重检查