package balking_model_test;
//希望 doInit() 方法仅被调用一次，下面的实现是否有问题，为什么？
/**
 * @ClassName :   TestVolatile
 * @Description: 有问题: volatile无法保证原子性; 当多个线程同时调用init()方法时, 此时都进入到if判断, 因为都为false, 所以都调用doInit()方法, 此时就调用了多次
 * @Author: TX
 * @CreateDate: 2021/10/21 11:46
 * @Version: 1.0
 */
public class TestVolatile {
    volatile boolean initialized  = false;
    void init(){
        if(initialized){
            return;
        }
        doInit();
        initialized = true;
    }
    private void doInit(){}
}
