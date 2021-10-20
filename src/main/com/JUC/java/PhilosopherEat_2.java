import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName :   PhilosopherEat_2
 * @Description: TODO
 * @Author: TX
 * @CreateDate: 2021/10/17 21:49
 * @Version: 1.0
 */

@Slf4j(topic = "guizy.PhilosopherEat")
public class PhilosopherEat_2 {
    public static void main(String[] args) {
        Chopstick2 c1 = new Chopstick2("1");
        Chopstick2 c2 = new Chopstick2("2");
        Chopstick2 c3 = new Chopstick2("3");
        Chopstick2 c4 = new Chopstick2("4");
        Chopstick2 c5 = new Chopstick2("5");
        new Philosopher2("苏格拉底", c1, c2).start();
        new Philosopher2("柏拉图", c2, c3).start();
        new Philosopher2("亚里士多德", c3, c4).start();
        new Philosopher2("赫拉克利特", c4, c5).start();
        new Philosopher2("阿基米德", c5, c1).start();
    }
}

@Slf4j(topic = "guizy.Philosopher")
class Philosopher2 extends Thread {
    final Chopstick2 left;
    final Chopstick2 right;

    public Philosopher2(String name, Chopstick2 left, Chopstick2 right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            // 获得了左手边筷子 (针对五个哲学家, 它们刚开始肯定都可获得左筷子)
            if (left.tryLock()) {
                try {
                    // 此时发现它的right筷子被占用了, 使用tryLock(),
                    // 尝试获取失败, 此时它就会将自己左筷子也释放掉
                    // 临界区代码
                    if (right.tryLock()) { //尝试获取右手边筷子, 如果获取失败, 则会释放左边的筷子
                        try {
                            eat();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
//            if(left.tryLock()){
//                try{
//                    if(right.tryLock()){
//                        try{
//                            eat();
//                        }finally {
//                            right.unlock();
//                        }
//                    }
//                }finally {
//                    left.unlock();
//                }

//            }
        }
    }

    private void eat() throws InterruptedException {
        log.debug("eating...");
        Thread.sleep(500);
    }
}

// 继承ReentrantLock, 让筷子类称为锁
class Chopstick2 extends ReentrantLock {
    String name;

    public Chopstick2(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}
