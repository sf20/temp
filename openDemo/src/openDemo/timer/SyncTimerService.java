package openDemo.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.service.sync.opple.OppleSyncService;

/**
 * 同步定时器
 * 
 * @author yanl
 *
 */
public class SyncTimerService {
	// 定时器间隔执行时间 单位毫秒
	private static final long PERIOD = 60 * 60 * 1000;
	// 每次定时器执行时间参数
	private static final int TIMER_EXEC_TIME_HOUR = 9;
	private static final int TIMER_EXEC_TIME_MINUTE = 10;
	private static final int TIMER_EXEC_TIME_SECOND = 00;
	// 线程池数量
	private static final int CORE_POOL_SIZE = 1;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = LogManager.getLogger(SyncTimerService.class);

	// 用于执行定时任务的定时器
	private ScheduledExecutorService executor;
	// 定时器间隔执行计算基准日
	private Date baseDate;

	public SyncTimerService() {
		Calendar calendar = Calendar.getInstance();
		// 设置定时器首次执行时间
		calendar.set(Calendar.HOUR_OF_DAY, TIMER_EXEC_TIME_HOUR);
		calendar.set(Calendar.MINUTE, TIMER_EXEC_TIME_MINUTE);
		calendar.set(Calendar.SECOND, TIMER_EXEC_TIME_SECOND);

		// 间隔执行基于首次执行时间
		baseDate = calendar.getTime();
	}

	public static void main(String[] args) {

		SyncTimerService syncTimerService = new SyncTimerService();

		logger.info("程序初始化::定时器启动...");
		syncTimerService.singleAddTimingService(new OppleSyncService());
		syncTimerService.singleAddTimingService(new OppleSyncService());

		logger.info("====测试优先执行====");
	}

	/**
	 * 增加定时服务
	 * 
	 * @param syncService
	 */
	public void singleAddTimingService(OppleSyncService syncService) {
		// 保证只有一个定时器在运行
		if (executor == null) {
			addTimingService(syncService);
		}
	}

	private void addTimingService(final OppleSyncService syncService) {
		executor = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					// 预定下次执行时间
					Date nextTime = getNextTime(new Date());

					// 执行同步方法
					syncService.sync();

					// 实际任务结束时间
					Date taskEndTime = new Date();
					// 预定与实际进行比较
					long delay = compareGetDelay(taskEndTime, nextTime);
					logger.info(System.lineSeparator());

					// 继续设置下一个定时任务
					executor.schedule(this, delay, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					shutdowmAndPrintLog(executor, e);
				}
			}
		};

		// 定时器首次任务立即执行
		executor.schedule(task, 0, TimeUnit.MILLISECONDS);
		// threadPool.schedule(task1, compareGetDelay(new Date(), initDate),
		// TimeUnit.MILLISECONDS);
	}

	/**
	 * 比较当前时间和设定执行时间得到定时器延时执行时间差
	 * 
	 * @param nowTime
	 * @param execTimeExpected
	 * @return
	 */
	private long compareGetDelay(Date nowTime, Date execTimeExpected) {
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
		if (nowTime.compareTo(baseDate) < 0) {

			return baseDate;
		} else {
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
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.MILLISECOND, (int) period);

		return calendar.getTime();
	}

	/**
	 * 将毫秒数转化为相应的时分秒格式：hh小时mm分钟ss秒
	 * 
	 * @param delay
	 * @return
	 */
	private String timeMillsToHMS(long delay) {
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

	/**
	 * 关闭定时器同时记录异常日志
	 * 
	 * @param executor
	 *            定时器执行器
	 * @param e
	 */
	private void shutdowmAndPrintLog(ScheduledExecutorService executor, Exception e) {
		executor.shutdown();
		logger.info("发生异常::定时器已停止");
		logger.error("定时同步[OpSyncService]出现异常", e);
	}
}
