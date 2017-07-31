package openDemo.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import openDemo.entity.GroupInfoEntity;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.service.OrgSyncService;
import openDemo.service.PositionSyncService;
import openDemo.service.RoleService;
import openDemo.service.SsoService;
import openDemo.service.UserSyncService;

public class OpenDemoTest {

	public static void main(String[] args) throws IOException {
		new OpenDemoTest();
	}

	public OpenDemoTest() throws IOException {
		// 岗位同步
		// posTest();

		// //1.单点登录:el/sso
		// ssoTest();

		// 2.同步用户: el/sync/users
		userSyncTest();

		// //3.同步禁用用户: el/sync/disabledusers
		// disabledusersTest();
		//
		// //4.同步启用用户: el/sync/enabledusers
		// enabledusersTest();
		//
		// //5.同步删除用户: el/sync/deletedusers
		// deletedusersTest();
		//
		// 6.同步组织单位: el/sync/ous
		// ousTest();

		// //7.同步删除组织单位: el/sync/deleteous 返回信息msg在data中
		// deleteousTest();
		//
		// //8.同步用户移除组织单位: el/sync/removeusersfromou
		// removeusersfromouTest();
		//
		// //9.同步用户更改组织单位: el/sync/batchchangeorgou
		// batchchangeorgouTest();
		//
		// //10.同步(角色)组: el/sync/roles
		// rolesTest();
		//
		// //11.同步删除(角色)组: el/sync/deletedroles
		// deletedrolesTest();
		//
		// //12.同步用户移除(角色)组: el/sync/removeusersfromrole
		// removeusersfromroleTest();
	}

	void posTest() throws IOException {
		PositionSyncService posService = new PositionSyncService();
		List<PositionModel> positionInfos = new ArrayList<>();
		PositionModel p = new PositionModel();
		p.setpNames("未分类;岗位修改");
		p.setpNo("201707241745489031");
		positionInfos.add(p);
		ResultEntity resultEntity = posService.syncPos(positionInfos);
		print("岗位同步", resultEntity);
	}

	// 单点登录:el/sso
	void ssoTest() throws IOException {
		SsoService ssoService = new SsoService();
		ResultEntity resultEntity = ssoService.sso("testId");
		print("单点登录", resultEntity);
	}

	// 同步用户: el/sync/users
	void userSyncTest() throws IOException {
		UserSyncService userService = new UserSyncService();
		List<UserInfoModel> users = new ArrayList<UserInfoModel>();
		UserInfoModel userEntity = new UserInfoModel();
		// userEntity.setOrgOuCode("506799895");
		userEntity.setID("testId");
		userEntity.setUserName("testId");
		userEntity.setCnName("testId");
		userEntity.setOrgOuCode("10086");
		users.add(userEntity);
		ResultEntity resultEntity = userService.userSync(true, users);
		print("同步用户", resultEntity);
	}

	// 同步禁用用户: el/sync/disabledusers
	void disabledusersTest() throws IOException {
		UserSyncService userService = new UserSyncService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily11");
		ResultEntity resultEntity = userService.disabledusersSync(userNames);
		print("同步禁用用户", resultEntity);
	}

	// 同步启用用户: el/sync/enabledusers
	void enabledusersTest() throws IOException {
		UserSyncService userService = new UserSyncService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily11");
		ResultEntity resultEntity = userService.enabledusersSync(userNames);
		print("同步启用用户", resultEntity);
	}

	// 同步删除用户: el/sync/deletedusers
	void deletedusersTest() throws IOException {
		UserSyncService userService = new UserSyncService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily11");
		ResultEntity resultEntity = userService.deletedusersSync(userNames);
		print("同步删除用户", resultEntity);
	}

	// 同步组织单位: el/sync/ous
	void ousTest() throws IOException {
		OrgSyncService orgService = new OrgSyncService();

		List<OuInfoModel> ouInfos = new ArrayList<OuInfoModel>();
		OuInfoModel ouInfoEntity = new OuInfoModel();
		ouInfoEntity.setID("10086");
		ouInfoEntity.setOuName("a&b");
		// ouInfoEntity.setParentID("1");
		ouInfoEntity.setIsSub("false");
		ouInfos.add(ouInfoEntity);
		ResultEntity resultEntity = null;
		try {
			resultEntity = orgService.getOucodeByName(URLEncoder.encode("a&b", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ous(false, ouInfos);//
		print("同步组织单位", resultEntity);
	}

	// 同步删除组织单位: el/sync/deleteous
	void deleteousTest() throws IOException {
		OrgSyncService orgService = new OrgSyncService();
		List<String> ouCodeOrThirdSystemIDs = new ArrayList<String>();
		ouCodeOrThirdSystemIDs.add("java01");
		ResultEntity resultEntity = orgService.deleteous(ouCodeOrThirdSystemIDs);
		print("同步删除组织单位", resultEntity);
	}

	// 同步用户移除组织单位: el/sync/removeusersfromou
	void removeusersfromouTest() throws IOException {
		OrgSyncService orgService = new OrgSyncService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily12");
		ResultEntity resultEntity = orgService.removeusersfromou(userNames);
		print("同步用户移除组织单位", resultEntity);
	}

	// 同步用户更改组织单位: el/sync/batchchangeorgou
	void batchchangeorgouTest() throws IOException {
		OrgSyncService orgService = new OrgSyncService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily12");
		String newOuID = "506799895";
		ResultEntity resultEntity = orgService.batchchangeorgou(userNames, newOuID);
		print("同步用户更改组织单位", resultEntity);
	}

	// 同步(角色)组: el/sync/roles
	void rolesTest() throws IOException {
		RoleService roleService = new RoleService();
		List<GroupInfoEntity> groupInfos = new ArrayList<GroupInfoEntity>();
		GroupInfoEntity groupInfoEntity = new GroupInfoEntity();
		groupInfoEntity.setID("role_kaifa");
		groupInfoEntity.setDescription("描述啊");
		groupInfoEntity.setGroupName("开发组");
		List<String> users = new ArrayList<String>();
		users.add("zhao12");
		users.add("zhao11");
		groupInfoEntity.setUsers(users);
		groupInfos.add(groupInfoEntity);
		ResultEntity resultEntity = roleService.roles(false, groupInfos);
		print("同步(角色)组", resultEntity);
	}

	// 同步删除(角色)组: el/sync/deletedroles
	void deletedrolesTest() throws IOException {
		RoleService roleService = new RoleService();
		List<String> roleThirdSystemIDs = new ArrayList<String>();
		roleThirdSystemIDs.add("1234");
		roleThirdSystemIDs.add("4321");
		ResultEntity resultEntity = roleService.deletedroles(roleThirdSystemIDs);
		print("同步删除(角色)组", resultEntity);
	}

	// 同步用户移除(角色)组: el/sync/removeusersfromrole
	void removeusersfromroleTest() throws IOException {
		RoleService roleService = new RoleService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("yaoj");
		userNames.add("lili");
		ResultEntity resultEntity = roleService.removeusersfromrole(userNames);
		print("同步用户移除(角色)组", resultEntity);
	}

	void print(String name, ResultEntity resultEntity) {
		System.out.println(name + ":" + resultEntity.getCode() + "==" + resultEntity.getData() + "=="
				+ resultEntity.getMessage() + "==" + resultEntity.getTotalCount());
	}

}
