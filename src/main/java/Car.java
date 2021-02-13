import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static String winner = "";
    private static boolean isWinner = false;
    private Race race;
    private int speed;
    private CyclicBarrier start;
    private CountDownLatch ready;
    private CountDownLatch finish;
    private Lock win;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrier cb, CountDownLatch cdl,
               CountDownLatch cdlFin, Lock win) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.start = cb;
        this.ready = cdl;
        this.finish = cdlFin;
        this.win = win;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            ready.countDown();
            start.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if(win.tryLock() && !isWinner){
            winner = this.name;
            isWinner = true;
            win.unlock();
        }
        finish.countDown();
    }

    public static String getWinner() {
        return winner;
    }

    }

