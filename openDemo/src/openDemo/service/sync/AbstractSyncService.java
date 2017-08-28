package openDemo.service.sync;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;

public abstract class AbstractSyncService {
	// 全量模式/增量模式
	public static final String MODE_FULL = "1";
	public static final String MODE_UPDATE = "2";
	// json请求及转换时字符集类型
	public static final String CHARSET_UTF8 = "UTF-8";
	// 请求同步接口成功返回码
	public static final String SYNC_CODE_SUCCESS = "0";
	// 岗位类别的默认值
	public static final String POSITION_CLASS_DEFAULT = "未分类";
	public static final String POSITION_CLASS_SEPARATOR = ";";
	// 日期格式化用
	public static final SimpleDateFormat JAVA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	// 请求同步接口的service
	protected SyncPositionService positionService = new SyncPositionService();
	protected SyncOrgService orgService = new SyncOrgService();
	protected SyncUserService userService = new SyncUserService();
	// 用于存放请求获取到的数据的集合
	protected List<PositionModel> positionList = new LinkedList<PositionModel>();
	protected List<OuInfoModel> ouInfoList = new LinkedList<OuInfoModel>();
	protected List<UserInfoModel> userInfoList = new LinkedList<UserInfoModel>();

	public abstract void sync() throws Exception;

	/**
	 * 返回带类别岗位名
	 * 
	 * @param posName
	 * @return
	 */
	protected String getFullPosNames(String posName) {
		return POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR + posName;
	}
}
