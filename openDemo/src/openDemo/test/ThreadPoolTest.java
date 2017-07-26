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
	private static final long PERIOD = 60 * 1000;// 定时器间隔执行时间 单位毫秒

	// 每次定时器执行时间参数
	private static final int TIMER_EXEC_TIME_HOUR = 0;
	private static final int TIMER_EXEC_TIME_MINUTE = 15;
	private static final int TIMER_EXEC_TIME_SECOND = 00;

	private static final int CORE_POOL_SIZE = 1;// 线程池数量

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final Logger logger = LogManager.getLogger();

	private Calendar calendar;
	private Date initDate;// 定时器首次执行时间
	private Date baseDate;// 定时器间隔执行计算基准日

	ThreadPoolTest() {
		calendar = Calendar.getInstance();
		// 设置定时器首次执行时间
		calendar.set(Calendar.HOUR_OF_DAY, TIMER_EXEC_TIME_HOUR);
		calendar.set(Calendar.MINUTE, TIMER_EXEC_TIME_MINUTE);
		calendar.set(Calendar.SECOND, TIMER_EXEC_TIME_SECOND);

		initDate = calendar.getTime();
		// 间隔执行基于首次执行时间
		baseDate = initDate;
	}

	public static void main(String[] args) {

		logger.info("定时器开始时间：" + dateFormat.format(new Date()));
		new ThreadPoolTest().multiSyncTask();

		// for (int i = 0; i < 10; i++) {
		// System.out.println(randomTime());
		// }
	}

	public void multiSyncTask() {
		final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					logger.info("开始执行task：" + dateFormat.format(new Date()));
					// 预定下次执行时间
					Date nextTime = getNextTime();
					logger.info("定时器预定下次执行时间: " + dateFormat.format(nextTime));

					// 模拟任务执行耗时
					long randomTime = randomTime();
					logger.info("本次执行需要：" + randomTime / 1000 + "秒");
					Thread.sleep(randomTime);
					logger.info("task执行完了：" + dateFormat.format(new Date()));

					// 实际任务结束时间
					Date taskEndTime = new Date();

					// 预定与实际进行比较
					long delay = compareGetDelay(taskEndTime, nextTime);
					logger.info("修改后预定下次执行时间: " + dateFormat.format(addTime(taskEndTime, delay)));
					logger.info("距离下次执行时间还有: " + delay / (1000 * 60) + "分" + delay / 1000 + "秒");
					logger.info(System.lineSeparator());

					threadPool.schedule(this, delay, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					e.printStackTrace();
					threadPool.shutdown();
				}
			}
		};

		// 定时器初始化
		threadPool.schedule(task, compareGetDelay(new Date(), initDate), TimeUnit.MILLISECONDS);

		// threadPool.execute(task2);
		// threadPool.scheduleWithFixedDelay(task2, 1, 1000,
		// TimeUnit.MILLISECONDS);
		// threadPool.shutdown();
	}

	/**
	 * 比较当前时间和设定执行时间得到定时器延时执行时间差
	 * 
	 * @param nowTime
	 * @param execTimeExpected
	 * @return
	 */
	public long compareGetDelay(Date nowTime, Date execTimeExpected) {
		if (nowTime.compareTo(execTimeExpected) <= 0) {
			return execTimeExpected.getTime() - nowTime.getTime();
		} else {

			long delay = 0;
			long timeGap = nowTime.getTime() - execTimeExpected.getTime();
			// 整除的处理
			if (timeGap % PERIOD > 0) {
				delay = (timeGap / PERIOD + 1) * PERIOD;
			} else {
				delay = timeGap / PERIOD * PERIOD;
			}
			return addTime(execTimeExpected, delay).getTime() - nowTime.getTime();
		}

	}

	public Date getNextTime() {
		Date nextTime = addTime(baseDate, PERIOD);
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
		calendar.setTime(baseDate);
		calendar.add(Calendar.MILLISECOND, (int) period);

		return calendar.getTime();
	}

	public static long randomTime() {
		return (new Random().nextInt(60) + 20) * 1000;
	}

}
