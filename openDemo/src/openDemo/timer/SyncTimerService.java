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

import openDemo.service.sync.AlignSyncService;
import openDemo.service.sync.CustomTimerTask;
import openDemo.service.sync.LeoSyncService;
import openDemo.service.sync.OppleSyncService;

/**
 * 同步定时器
 * 
 * @author yanl
 *
 */
public class SyncTimerService {
	// 定时器间隔执行时间 单位毫秒
	private static final long PERIOD = 5 * 60 * 1000;// 60 * 60 * 1000
	// 每次定时器执行时间参数
	private int timerExecHour = 23;
	private int timerExecMinute = 00;
	private int timerExecSecond = 00;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = LogManager.getLogger(SyncTimerService.class);

	// 用于执行定时任务的定时器
	private ScheduledExecutorService executor;
	// 定时器间隔执行计算基准日
	private Date baseDate;

	public SyncTimerService(int execHour, int execMinute) {
		this.timerExecHour = execHour;
		this.timerExecMinute = execMinute;

		Calendar calendar = Calendar.getInstance();
		// 设置定时器首次执行时间
		calendar.set(Calendar.HOUR_OF_DAY, timerExecHour);
		calendar.set(Calendar.MINUTE, timerExecMinute);
		calendar.set(Calendar.SECOND, timerExecSecond);

		// 间隔执行基于首次执行时间
		baseDate = calendar.getTime();
	}

	public static void main(String[] args) {

		SyncTimerService syncTimerService = new SyncTimerService(22, 5);
		SyncTimerService syncTimerService2 = new SyncTimerService(22, 8);
		SyncTimerService syncTimerService3 = new SyncTimerService(12, 0);

		logger.info("程序初始化::定时器启动...");
		syncTimerService.singleAddTimingService(new OppleSyncService());
		syncTimerService.singleAddTimingService(new OppleSyncService());

		syncTimerService2.singleAddTimingService(new LeoSyncService());
		syncTimerService2.singleAddTimingService(new LeoSyncService());

		syncTimerService3.singleAddTimingService(new AlignSyncService());
		logger.info("====测试优先执行====");
	}

	/**
	 * 增加定时服务
	 * 
	 * @param syncService
	 */
	public void singleAddTimingService(CustomTimerTask timerTask) {
		// 保证只有一个定时器在运行
		if (executor == null) {
			addTimingService(timerTask);
		}
	}

	private void addTimingService(final CustomTimerTask timerTask) {
		final String className = timerTask.getClass().getSimpleName();
		executor = Executors.newSingleThreadScheduledExecutor();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					// 预定下次执行时间
					Date nextTime = getNextTime(new Date());

					// 执行同步方法
					logger.info("定时同步[" + className + "]开始");
					timerTask.execute();
					logger.info("定时同步[" + className + "]结束");

					// 实际任务结束时间
					Date taskEndTime = new Date();
					// 预定与实际进行比较
					long delay = compareGetDelay(taskEndTime, nextTime);
					logger.info(System.lineSeparator());

					// 继续设置下一个定时任务
					executor.schedule(this, delay, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					shutdowmAndPrintLog(executor, e, className);
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
	 * @param className
	 */
	private void shutdowmAndPrintLog(ScheduledExecutorService executor, Exception e, String className) {
		executor.shutdown();
		logger.info("发生异常::定时器[" + className + "]已停止");
		logger.error("定时同步[" + className + "]出现异常", e);
	}
}
