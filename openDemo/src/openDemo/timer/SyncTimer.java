package openDemo.timer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.service.sync.OpSyncService;

/**
 * 同步定时器
 * 
 * @author yanl
 *
 */
public class SyncTimer {
	private static final Date FIRST_TIME = new Date();// 首次执行时间
	private static final long PERIOD = 10 * 60 * 1000;// 定时器间隔执行时间 单位毫秒
	private static final int CORE_POOL_SIZE = 1;// 线程池数量

	private static final Logger logger = LogManager.getLogger(SyncTimer.class);

	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				logger.info("定时同步开始...");
				multiSyncTask();
			}
		}, FIRST_TIME, PERIOD);
	}

	static void multiSyncTask() {
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

		TimerTask task1 = new TimerTask() {

			@Override
			public void run() {
				try {
					logger.info("定时同步[OpSyncService]开始...");
					new OpSyncService().sync();
					logger.info("定时同步[OpSyncService]结束");
				} catch (IOException e) {
					printLog(e);
				} catch (ReflectiveOperationException e) {
					printLog(e);
				} catch (SQLException e) {
					printLog(e);
				} catch (Exception e) {
					printLog(e);
				}
			}

			private void printLog(Exception e) {
				logger.error("定时同步[OpSyncService]出现异常: " + e.getMessage());
			}
		};

		threadPool.schedule(task1, 1, TimeUnit.MILLISECONDS);
		// threadPool.schedule(task2, 2, TimeUnit.MILLISECONDS);

		threadPool.shutdown();
	}
}
