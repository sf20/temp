package openDemo.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadPoolTest {
	private static final Date FIRST_TIME = new Date();// 首次执行时间
	private static final long PERIOD = 10 * 1000;// 定时器间隔执行时间 单位毫秒
	// private static final long ONE_DAY_PERIOD = 10 * 1000;// 定时器间隔执行时间 单位毫秒
	private static final int CORE_POOL_SIZE = 1;// 线程池数量
	private static final SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

	private static final Logger logger = LogManager.getLogger();

	private Calendar c;
	private Date baseDate = FIRST_TIME;

	ThreadPoolTest() {
		c = Calendar.getInstance();
	}

	public static void main(String[] args) {

		logger.info("定时同步开始" + "=" + format.format(new Date()));
		new ThreadPoolTest().multiSyncTask();

		// for (int i = 0; i < 10; i++) {
		// logger.info(randomTime());
		// }
	}

	public void multiSyncTask() {
		final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					// 预定下次执行时间
					Date nextTime = getNextTime();

					// 模拟任务执行耗时
					long randomTime = randomTime();
					logger.info("开始执行task：" + format.format(new Date()));
					logger.info("本次执行需要：" + randomTime / 1000 + "秒");
					Thread.sleep(randomTime);
					logger.info("task执行完了：" + format.format(new Date()));

					// 实际任务结束时间
					Date taskEndTime = new Date();

					// 预定与实际进行比较
					long delay = compareGetDelay(taskEndTime, nextTime);
					logger.info("修改后预定下次执行时间: " + format.format(addTime(taskEndTime, delay)));
					threadPool.schedule(this, delay, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		threadPool.schedule(task, 0, TimeUnit.MILLISECONDS);

		// threadPool.execute(task2);
		// threadPool.scheduleWithFixedDelay(task2, 1, 1000, TimeUnit.MILLISECONDS);
		// threadPool.shutdown();
	}

	public long compareGetDelay(Date taskEndTime, Date nextTime) {
		if (taskEndTime.compareTo(nextTime) <= 0) {
			return nextTime.getTime() - taskEndTime.getTime();
		} else {

			return addTime(taskEndTime, PERIOD).getTime() - nextTime.getTime();
		}

	}

	public Date getNextTime() {
		Date nextTime = addTime(baseDate, PERIOD);
		logger.info("定时器预定下次执行时间: " + format.format(nextTime));

		baseDate = nextTime;
		return nextTime;
	}

	/**
	 * 在给定时间的基础上延迟一定时间
	 * 
	 * @param baseDate
	 *            给定时间
	 * @param period
	 *            延迟毫秒数
	 * @return
	 */
	private Date addTime(Date baseDate, long period) {
		c.setTime(baseDate);
		c.add(Calendar.MILLISECOND, (int) period);

		return c.getTime();
	}

	public static long randomTime() {
		return (new Random().nextInt(10) + 6) * 1000;
	}

}
