package openDemo.service.sync;

public abstract class AbstractSyncService implements CustomTimerTask {
	// // 全量模式/增量模式
	// public static final String MODE_FULL = "1";
	// public static final String MODE_UPDATE = "2";
	// // json请求及转换时字符集类型
	// public static final String CHARSET_UTF8 = "UTF-8";
	// // 请求同步接口成功返回码
	// public static final String SYNC_CODE_SUCCESS = "0";
	// // 岗位类别的默认值
	// public static final String POSITION_CLASS_DEFAULT = "未分类";
	// public static final String POSITION_CLASS_SEPARATOR = ";";
	// // 日期格式化用
	// public static final SimpleDateFormat JAVA_DATE_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd");
	//
	// // 请求同步接口的service
	// protected SyncPositionService positionService = new SyncPositionService();
	// protected SyncOrgService orgService = new SyncOrgService();
	// protected SyncUserService userService = new SyncUserService();
	// // 用于存放请求获取到的数据的集合
	// protected List<PositionModel> positionList = new LinkedList<PositionModel>();
	// protected List<OuInfoModel> ouInfoList = new LinkedList<OuInfoModel>();
	// protected List<UserInfoModel> userInfoList = new LinkedList<UserInfoModel>();

	@Override
	public void execute() throws Exception {
		sync();
	}

	public abstract void sync() throws Exception;

}
