package openDemo.service.sync;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.config.TestConfig;
import openDemo.dao.UserInfoDao;
import openDemo.entity.ResultEntity;
import openDemo.entity.StudyPlanDetail;
import openDemo.entity.UserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncStudyPlanService;

public class AlignSyncService implements TestConfig, CustomTimerTask {
	// 学习计划
	private static final String STUDYPLAN_ID = "99b08000-1580-40e4-9a5b-7d76c4feded3";
	private static final String STUDYPLAN_STATUS = "2";
	private static final String STUDYPLAN_PASSED = "1";
	// 阶段二组织id
	private static final String STAGE2_OUID = "148580105";// 74123262-b227-4122-b996-65ad256a90ab
	private static final String MASTER_TYPE_EXAM = "exam";
	// 请求成功返回码
	private static final String RESPONSE_OK = "0";
	// 记录日志
	private static final Logger logger = LogManager.getLogger(AlignSyncService.class);

	private SyncOrgService orgService = new SyncOrgService();
	private SyncStudyPlanService studyPlanService = new SyncStudyPlanService();
	private UserInfoDao userDao = new UserInfoDao();

	@Override
	public void execute() throws Exception {
		List<String> stage1UserNames = new ArrayList<String>();
		List<String> stage2UserNames = new ArrayList<String>();

		// 获取人员数据
		List<UserInfoModel> userList = userDao.getAll();
		for (UserInfoModel user : userList) {
			List<StudyPlanDetail> studyExamList = userDao.getStudyPlanDetailByUserIdPlanID(apikey, STUDYPLAN_ID,
					user.getID(), MASTER_TYPE_EXAM);

			// 人员暂未关联学习计划
			if (studyExamList.size() < 1) {
				stage1UserNames.add(user.getUserName());
			}
			// 人员已关联学习计划
			else {
				// 人员考试情况判断
				if (isExamPassed(studyExamList)) {
					// 添加至stage2
					stage2UserNames.add(user.getUserName());
				}
			}
		}

		// 新入系统人员自动同步到学习计划里面
		int stage1UserCount = stage1UserNames.size();
		System.out.println("添加到学习计划人数：" + stage1UserCount);
		if (stage1UserCount > 0) {
			// 处理一次最多添加100个账号
			int userMaxSize = 100;
			int methodCallTimes = stage1UserCount / userMaxSize + (stage1UserCount % userMaxSize == 0 ? 0 : 1);
			for (int i = 0; i < methodCallTimes; i++) {
				int fromIndex = i * userMaxSize;
				int toIndex = (i + 1) * userMaxSize;
				ResultEntity resultEntity = studyPlanService
						.addPersonToPlan(STUDYPLAN_ID,
								getStrUserNames(stage1UserNames.subList(fromIndex,
										toIndex > stage1UserCount ? stage1UserCount : toIndex)),
								apikey, secretkey, baseUrl);

				if (!RESPONSE_OK.equals(resultEntity.getCode())) {
					logger.error("添加到学习计划错误：" + resultEntity.getCode() + "=" + resultEntity.getMessage());
				}
			}
		}

		// 满足条件 跳转到阶段二
		System.out.println("跳转到阶段二人数：" + stage2UserNames.size());
		if (stage2UserNames.size() > 0) {
			ResultEntity resultEntity = orgService.batchchangeorgou(stage2UserNames, STAGE2_OUID, apikey, secretkey,
					baseUrl);

			if (!RESPONSE_OK.equals(resultEntity.getCode())) {
				logger.error("同步用户更改组织单位错误：" + resultEntity.getCode() + "=" + resultEntity.getMessage());
			}
		}
	}

	/**
	 * 已有学习计划人员的考试是否通过
	 * 
	 * @param studyPlanDetail
	 * @return
	 */
	private boolean isExamPassed(List<StudyPlanDetail> studyExamList) {
		boolean isPassed = true;
		for (StudyPlanDetail study : studyExamList) {
			// 只要有一个考试没通过就返回false
			if (!(STUDYPLAN_STATUS.equals(study.getStatus()) && STUDYPLAN_PASSED.equals(study.getIsPassed()))) {
				isPassed = false;
				break;
			}
		}
		return isPassed;
	}

	/**
	 * 根据人员名集合返回字符串形式: 如user1;user2, 以;隔开
	 * 
	 * @param userNameList
	 * @return
	 */
	private String getStrUserNames(List<String> userNameList) {
		StringBuffer userNames = new StringBuffer();
		for (String userName : userNameList) {
			userNames.append(userName).append(";");
		}

		return userNames.toString();
	}

	public static void main(String[] args) throws Exception {
		// new AlignSyncService().execute();

		// List<String> stage2UserNames = new ArrayList<>();
		// stage2UserNames.add("aligntest1");
		// ResultEntity resultEntity = orgService.batchchangeorgou(stage2UserNames,
		// STAGE2_OUID, apikey, secretkey, baseUrl);
		// System.out.println(resultEntity.getCode() + "=" + resultEntity.getMessage());
	}

}
