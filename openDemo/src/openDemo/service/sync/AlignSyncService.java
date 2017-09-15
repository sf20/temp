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
	private static final String MASTER_TYPE_EXAM = "exam";
	private static final String MASTER_TYPE_KNOWLEDGE = "knowledge";

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
			List<StudyPlanDetail> studyPlanList = userDao.getStudyPlanDetailByUserIdPlanID(apikey, STUDYPLAN_ID,
					user.getID(), MASTER_TYPE_EXAM);

			// 人员暂未关联学习计划
			if (studyPlanList.size() == 0) {
				stage1UserNames.add(user.getUserName());
			}
			// 人员已关联学习计划
			else {
				// 人员考试情况判断
				if (isStageOnePassed(studyPlanList.get(0))) {
					// 添加至stage2
					stage2UserNames.add(user.getUserName());
				}
			}
		}

		// 新入系统人员自动同步到学习计划里面
		studyPlanService.addPersonToPlan(STUDYPLAN_ID, getStrUserNames(stage1UserNames), apikey, secretkey, baseUrl);

		// 完成满足条件 跳转到阶段二
		orgService.batchchangeorgou(stage2UserNames, STAGE2_OUID, apikey, secretkey, baseUrl);
	}

	/**
	 * 已有学习计划人员的学习计划是否完成
	 * 
	 * @param studyPlanDetail
	 * @return
	 */
	private boolean isStageOnePassed(StudyPlanDetail studyPlanDetail) {
		return STUDYPLAN_STATUS.equals(studyPlanDetail.getStatus())
				&& STUDYPLAN_PASSED.equals(studyPlanDetail.getIsPassed());
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
