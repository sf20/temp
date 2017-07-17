package openDemo.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 同步定时器
 * 
 * @author yanl
 *
 */
public class SyncTimer {
	private static final Date FIRST_TIME = new Date();// 首次执行时间
	private static final long PERIOD = 5000;// 定时器间隔执行时间 单位毫秒
	private static final int CORE_POOL_SIZE = 1;// 线程池数量

	public static void main(String[] args) {
		final Date date1 = new Date();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				multiSyncTask();
				System.out.println("执行时间: " + (new Date().getTime() - date1.getTime()));
			}
		}, FIRST_TIME, PERIOD);
	}

	static void multiSyncTask() {
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task1 = new TimerTask() {

			@Override
			public void run() {
				System.out.println("task1 excuting...");
				throw new RuntimeException();
			}
		};

		TimerTask task2 = new TimerTask() {

			@Override
			public void run() {
				System.out.println("task2 excuting...");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		threadPool.schedule(task1, 1000, TimeUnit.MILLISECONDS);
		threadPool.schedule(task2, 2000, TimeUnit.MILLISECONDS);

		threadPool.shutdown();
	}
}
