package openDemo.test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TempTest {
	private static final Date FIRST_TIME = new Date();// 首次执行时间
	private static final long PERIOD = 1 * 1000;// 定时器间隔执行时间 单位毫秒
	private static final int CORE_POOL_SIZE = 1;// 线程池数量

	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				System.out.println("定时同步开始...");
				multiSyncTask();
			}
		}, FIRST_TIME, PERIOD);
	}

	static void multiSyncTask() {
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task1 = new TimerTask() {

			@Override
			public void run() {
				System.out.println("task1");
				throw new RuntimeException();
			}

		};

		TimerTask task2 = new TimerTask() {

			@Override
			public void run() {

				try {
					Thread.currentThread().sleep(5000);
					System.out.println("task2");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		threadPool.execute(task1);
		threadPool.execute(task2);
		// threadPool.schedule(task1, 1, TimeUnit.MILLISECONDS);
		// threadPool.schedule(task2, 2, TimeUnit.MILLISECONDS);

		threadPool.shutdown();
	}
}
