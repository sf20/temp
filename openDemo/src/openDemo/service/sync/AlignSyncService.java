package openDemo.service.sync;

import java.util.ArrayList;
import java.util.List;

import openDemo.config.AlignConfig;
import openDemo.dao.UserInfoDao;
import openDemo.entity.StudyPlanDetail;
import openDemo.entity.UserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncStudyPlanService;

public class AlignSyncService implements AlignConfig, CustomTimerTask {
	// 学习计划
	private static final String STUDYPLAN_ID = "cb1fbf1b-e9f1-43d3-b9eb-c7ffedec3b24";
	private static final String STUDYPLAN_STATUS = "2";
	private static final String STUDYPLAN_PASSED = "1";
	// 阶段二组织id
	private static final String STAGE2_OUID = "26fe4203-7ccb-4401-aa4f-ac88d80274a9";
	// 学习计划类型
	private static final String MASTER_TYPE_KNOWLEDGE = "knowledge";
	private static final String MASTER_TYPE_EXAM = "exam";

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
			List<StudyPlanDetail> studyKnowledgeList = userDao.getStudyPlanDetailByUserIdPlanID(apikey, STUDYPLAN_ID,
					user.getID(), MASTER_TYPE_KNOWLEDGE);
			List<StudyPlanDetail> studyExamList = userDao.getStudyPlanDetailByUserIdPlanID(apikey, STUDYPLAN_ID,
					user.getID(), MASTER_TYPE_EXAM);

			int studyKnowledgeCount = studyKnowledgeList.size();
			int studyExamCount = studyExamList.size();

			// 人员暂未关联学习计划
			if (studyKnowledgeCount == 0 && studyExamCount == 0) {
				stage1UserNames.add(user.getUserName());
			}
			// 人员已关联学习计划
			else {
				// 人员学习情况判断
				// 学习计划中既有知识又有考试
				if (studyKnowledgeCount > 0 && studyExamCount > 0) {
					if (isKnowledgeStudied(studyKnowledgeList) && isExamPassed(studyExamList)) {
						// 添加至stage2
						stage2UserNames.add(user.getUserName());
					}
				}
				// 学习计划中只有知识
				else if (studyKnowledgeCount > 0) {
					if (isKnowledgeStudied(studyKnowledgeList)) {
						// 添加至stage2
						stage2UserNames.add(user.getUserName());
					}
				}
				// 学习计划中只有考试
				else if (studyExamCount > 0) {
					if (isExamPassed(studyExamList)) {
						// 添加至stage2
						stage2UserNames.add(user.getUserName());
					}
				}
			}
		}

		// 新入系统人员自动同步到学习计划里面
		int stage1UserCount = stage1UserNames.size();
		if (stage1UserCount > 0) {
			// 处理一次最多添加100个账号
			int userMaxSize = 100;
			int methodCallTimes = stage1UserCount / userMaxSize + (stage1UserCount % userMaxSize == 0 ? 0 : 1);
			for (int i = 0; i < methodCallTimes; i++) {
				studyPlanService.addPersonToPlan(STUDYPLAN_ID,
						getStrUserNames(stage1UserNames.subList(i * userMaxSize,
								(i + 1) * userMaxSize > stage1UserCount ? stage1UserCount : (i + 1) * userMaxSize)),
						apikey, secretkey, baseUrl);
			}
		}

		// 满足条件 跳转到阶段二
		if (stage2UserNames.size() > 0) {
			orgService.batchchangeorgou(stage2UserNames, STAGE2_OUID, apikey, secretkey, baseUrl);
		}
	}

	/**
	 * 已有学习计划人员的知识是否学完
	 * 
	 * @param studyPlanDetail
	 * @return
	 */
	private boolean isKnowledgeStudied(List<StudyPlanDetail> studyKnowledgeList) {
		boolean isStudied = true;
		for (StudyPlanDetail study : studyKnowledgeList) {
			// 只要有一个知识没学完就返回false
			if (!STUDYPLAN_STATUS.equals(study.getStatus())) {
				isStudied = false;
				break;
			}
		}
		return isStudied;
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

}
