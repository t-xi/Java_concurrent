import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName :   reflact
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/19 10:23
 * @Version: 1.0
 */
class Apple{
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Apple() {
    }
}
public class reflact {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1.通过Class.forName("全路径名")
        Class clz = Class.forName("java.lang.String");
        //2通过类.Class
        Class clz2 = String.class;
        //3通过对象.getClass()
        String str = "Hello";
        Class clz3 = str.getClass();

        //使用反射的三个步骤
        //1.获取Class对象
        //2.调用Class中的方法，反射使用
        //3.使用反射API操作这些信息
        Apple apple = new Apple();
        apple.setPrice(5);
        System.out.println(apple.getPrice());
        //使用反射
        Class clz4 = apple.getClass();
//        Class clz4 = Class.forName("JUC.java.Apple");
//        Class clz4 = Apple.class;
        Method setPriceMethod =  clz4.getMethod("setPrice", int.class);
        Constructor appleConstructor = clz4.getConstructor();
//        Object appleObj = appleConstructor.newInstance();
        Apple appleObj = (Apple)appleConstructor.newInstance();
        setPriceMethod.invoke(appleObj,14);
        Method getPriceMethod = clz4.getMethod("getPrice");
        System.out.println(getPriceMethod.invoke(appleObj));
    }
}
