package openDemo.service.sync;

import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;

public abstract class AbstractSyncService {
	// 请求同步接口的service
	public static SyncPositionService positionService = new SyncPositionService();
	public static SyncOrgService orgService = new SyncOrgService();
	public static SyncUserService userService = new SyncUserService();

	public abstract void sync() throws Exception;
}
