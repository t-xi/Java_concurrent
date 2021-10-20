import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
/**
 * @ClassName :   GuardedObjectTest2
 * @Description: 同步模式保护性暂停模式 (多任务版)
 * @Author: TX
 * @CreateDate: 2021/10/16 17:21
 * @Version: 1.0
 */

@Slf4j(topic = "java.GuardedObjectTest")
class GuardedObjectTest {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Thread.sleep(6000);
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
}

@Slf4j(topic = "guizy.People")
class People extends Thread {
    @Override
    public void run() {
        // 收信
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
    }
}

@Slf4j(topic = "java.Postman")
// 邮寄员类
class Postman extends Thread {
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }
}
// 信箱类
class Mailboxes {
    //存放GuardedObject
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;

    // 产生唯一 id
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject getGuardedObject(int id) {
        //根据id获取到box并删除对应的key和value,避免堆内存爆了
        return boxes.remove(id);
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

// 用来传递信息的作用, 当多个类使用GuardedObject,就很不方便,此时需要一个设计一个解耦的中间类
class GuardedObject {
    // 标记GuardedObject
    private int id;
    // 结果
    private Object response;

    public int getId() {
        return id;
    }

    public GuardedObject(int id) {
        this.id = id;
    }

    // 获取结果
    // timeout表示等待多久. 这里假如是2s
    public Object get(long timeout) {
        synchronized (this) {
            // 假如开始时间为 15:00:00
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                // 这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                // 经历的时间超过了最大等待时间, 退出循环
                if (waitTime <= 0) {
                    break;
                }
                try {
                    // this.wait(timeout)的问题: 虚假唤醒在15:00:01的时候,此时response还null, 此时经历时间就为1s,
                    // 进入while循环的时候response还是空,此时判断1s<=timeout 2s,此时再次this.wait(2s)吗,此时已经经历了
                    // 1s,所以只要再等1s就可以了. 所以等待的时间应该是 超时时间(timeout) - 经历的时间(passedTime)
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 经历时间
                passedTime = System.currentTimeMillis() - begin; // 15:00:02
            }
            return response;
        }
    }

    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            // 给结果变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}