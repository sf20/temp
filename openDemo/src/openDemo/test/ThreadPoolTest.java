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
	private static final long PERIOD = 60 * 60 * 1000;// 定时器间隔执行时间 单位毫秒

	// 每次定时器执行时间参数
	private static final int TIMER_EXEC_TIME_HOUR = 11;
	private static final int TIMER_EXEC_TIME_MINUTE = 20;
	private static final int TIMER_EXEC_TIME_SECOND = 00;

	private static final int CORE_POOL_SIZE = 1;// 线程池数量

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = LogManager.getLogger(ThreadPoolTest.class);

	private Calendar calendar;
	private Date baseDate;// 定时器间隔执行计算基准日

	ThreadPoolTest() {
		calendar = Calendar.getInstance();
		// 设置定时器首次执行时间
		calendar.set(Calendar.HOUR_OF_DAY, TIMER_EXEC_TIME_HOUR);
		calendar.set(Calendar.MINUTE, TIMER_EXEC_TIME_MINUTE);
		calendar.set(Calendar.SECOND, TIMER_EXEC_TIME_SECOND);

		// 间隔执行基于首次执行时间
		baseDate = calendar.getTime();
	}

	public static void main(String[] args) {

		logger.info("定时器已启动...");
		new ThreadPoolTest().multiSyncTask();

		// for (int i = 0; i < 10; i++) {
		// System.out.println(randomTime());
		// }

		// System.out.println(timeMillsToHMS(2 * 60 * 60 * 1000 - 51 * 60 * 1000 +
		// 10000));
	}

	public void multiSyncTask() {
		final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					logger.info("开始执行task：" + dateFormat.format(new Date()));
					// 预定下次执行时间
					Date nextTime = getNextTime(new Date());
					// logger.info("定时器预定下次执行时间: " + dateFormat.format(nextTime));

					// 模拟任务执行耗时
					long randomTime = randomTime();
					logger.info("本次执行需要：" + randomTime / 1000 + "秒");
					Thread.sleep(randomTime);
					logger.info("task执行完了：" + dateFormat.format(new Date()));

					// 实际任务结束时间
					Date taskEndTime = new Date();

					// 预定与实际进行比较
					long delay = compareGetDelay(taskEndTime, nextTime);
					logger.info(System.lineSeparator());

					threadPool.schedule(this, delay, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					shutdowmAndPrintLog(threadPool, e);
				}
			}
		};

		// 定时器首次执行
		threadPool.schedule(task, 0, TimeUnit.MILLISECONDS);
		// threadPool.schedule(task, compareGetDelay(new Date(), initDate),
		// TimeUnit.MILLISECONDS);

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
		long delay = 0;

		if (nowTime.compareTo(execTimeExpected) <= 0) {
			delay = execTimeExpected.getTime() - nowTime.getTime();
		} else {
			// 超时时间差
			long timeGap = nowTime.getTime() - execTimeExpected.getTime();
			// 超时周期的处理
			delay = addTime(execTimeExpected, (timeGap / PERIOD + 1) * PERIOD).getTime() - nowTime.getTime();
		}

		logger.info("预定下次执行时间: " + dateFormat.format(addTime(nowTime, delay)));
		logger.info("距离下次执行还有: " + timeMillsToHMS(delay));

		return delay;
	}

	/**
	 * 返回定时器每次预定执行时间
	 * 
	 * @return
	 */
	private Date getNextTime(Date nowTime) {
		// 首次调用时处理
		if(nowTime.compareTo(baseDate) < 0){
			
			return baseDate;
		}else{
			Date nextTime = addTime(baseDate, PERIOD);
			// 调整下次计算基准日
			baseDate = nextTime;

			return nextTime;
		}
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
		int randomSecond = new Random().nextInt(60) + 20;
		// if (randomSecond > 60) {
		// throw new RuntimeException();
		// }
		return randomSecond * 1000;
	}

	/**
	 * 将毫秒数转化为相应的时分秒格式：hh小时mm分钟ss秒
	 * 
	 * @param delay
	 * @return
	 */
	public static String timeMillsToHMS(long delay) {
		long oneSecondMills = 1000;
		long oneMinuteMills = 60 * oneSecondMills;
		long oneHourMills = 60 * oneMinuteMills;

		long hour = delay / oneHourMills;
		long minute = (delay - hour * oneHourMills) / oneMinuteMills;
		long second = (delay - hour * oneHourMills - minute * oneMinuteMills) / oneSecondMills;

		StringBuffer hmsStr = new StringBuffer();
		if (hour > 0) {
			hmsStr.append(hour).append("小时");
		}
		if (minute > 0) {
			hmsStr.append(minute).append("分钟");
		}
		if (second > 0) {
			hmsStr.append(second).append("秒");
		}

		return hmsStr.toString();
	}

	private void shutdowmAndPrintLog(ScheduledExecutorService threadPool, Exception e) {
		threadPool.shutdown();
		e.printStackTrace();
	}
}
