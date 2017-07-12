package openDemo.test;

import java.util.ArrayList;
import java.util.List;

import openDemo.entity.GroupInfoEntity;
import openDemo.entity.OuInfoEntity;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserEntity;
import openDemo.service.OrgService;
import openDemo.service.RoleService;
import openDemo.service.SsoService;
import openDemo.service.UserService;

public class OpenDemoTest {
	
	public static void main(String[] args) {
		new OpenDemoTest();
	}
	
	public OpenDemoTest() {
		//1.单点登录:el/sso
		ssoTest();
		
		//2.同步用户: el/sync/users
		userSyncTest();
		
		//3.同步禁用用户: el/sync/disabledusers
		disabledusersTest();
		
		//4.同步启用用户: el/sync/enabledusers
		enabledusersTest();
		
		//5.同步删除用户: el/sync/deletedusers
		deletedusersTest();
		
		//6.同步组织单位: el/sync/ous
		ousTest();
		
		//7.同步删除组织单位: el/sync/deleteous  返回信息msg在data中
		deleteousTest();
		
		//8.同步用户移除组织单位: el/sync/removeusersfromou
		removeusersfromouTest();
		
		//9.同步用户更改组织单位: el/sync/batchchangeorgou
		batchchangeorgouTest();
		
		//10.同步(角色)组: el/sync/roles
		rolesTest();
		
		//11.同步删除(角色)组: el/sync/deletedroles
		deletedrolesTest();
		
		//12.同步用户移除(角色)组: el/sync/removeusersfromrole
		removeusersfromroleTest();
	}
	
	//单点登录:el/sso
	private void ssoTest(){
		SsoService ssoService = new SsoService();
		ResultEntity resultEntity = ssoService.sso("lily11");
		print("单点登录",resultEntity);
	}
	
	//同步用户: el/sync/users
	private void userSyncTest() {
		UserService userService = new UserService();
		List<UserEntity> users = new ArrayList<UserEntity>();
		UserEntity userEntity = new UserEntity();
		userEntity.setOrgOuCode("506799895");
		userEntity.setID("ya01");
		userEntity.setUserName("ya01");
		userEntity.setCnName("ya01_1");
		users.add(userEntity);
		ResultEntity resultEntity = userService.userSync(false, users);
		print("同步用户",resultEntity);
	}
	
	//同步禁用用户: el/sync/disabledusers
	private void disabledusersTest() {
		UserService userService = new UserService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily11");
		ResultEntity resultEntity = userService.disabledusersSync(userNames);
		print("同步禁用用户",resultEntity);
	}
	
	//同步启用用户: el/sync/enabledusers
	private void enabledusersTest() {
		UserService userService = new UserService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily11");
		ResultEntity resultEntity = userService.enabledusersSync(userNames);
		print("同步启用用户",resultEntity);
	}
	
	//同步删除用户: el/sync/deletedusers
	private void deletedusersTest() {
		UserService userService = new UserService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily11");
		ResultEntity resultEntity = userService.deletedusersSync(userNames);
		print("同步删除用户",resultEntity);
	}
	
	//同步组织单位: el/sync/ous
	private void ousTest() {
		OrgService orgService = new OrgService();
		
		List<OuInfoEntity> ouInfos = new ArrayList<OuInfoEntity>();
		OuInfoEntity ouInfoEntity = new OuInfoEntity();
		ouInfoEntity.setID("java01");
		List<String> users = new ArrayList<String>();
		users.add("zhao12");
		users.add("zhao11");
		ouInfoEntity.setUsers(users);
		ouInfoEntity.setOuName("JAVA一部");
		ouInfoEntity.setIsSub(false);
		ouInfos.add(ouInfoEntity);
		ResultEntity resultEntity = orgService.ous(false, ouInfos);
		print("同步组织单位",resultEntity);
	}
	
	//同步删除组织单位: el/sync/deleteous
	private void deleteousTest() {
		OrgService orgService = new OrgService();
		List<String> ouCodeOrThirdSystemIDs = new ArrayList<String>();
		ouCodeOrThirdSystemIDs.add("java01");
		ResultEntity resultEntity = orgService.deleteous(ouCodeOrThirdSystemIDs);
		print("同步删除组织单位",resultEntity);
	}
	
	//同步用户移除组织单位: el/sync/removeusersfromou
	private void removeusersfromouTest() {
		OrgService orgService = new OrgService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily12");
		ResultEntity resultEntity = orgService.removeusersfromou(userNames);
		print("同步用户移除组织单位",resultEntity);
	}

	//同步用户更改组织单位: el/sync/batchchangeorgou
	private void batchchangeorgouTest() {
		OrgService orgService = new OrgService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("lily12");
		String newOuID = "506799895";
		ResultEntity resultEntity = orgService.batchchangeorgou(userNames, newOuID);
		print("同步用户更改组织单位",resultEntity);
	}
	
	//同步(角色)组: el/sync/roles
	private void rolesTest() {
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
		print("同步(角色)组",resultEntity);
	}
	
	//同步删除(角色)组: el/sync/deletedroles
	private void deletedrolesTest() {
		RoleService roleService = new RoleService();
		List<String> roleThirdSystemIDs = new ArrayList<String>();
		roleThirdSystemIDs.add("1234");
		roleThirdSystemIDs.add("4321");
		ResultEntity resultEntity = roleService.deletedroles(roleThirdSystemIDs);
		print("同步删除(角色)组",resultEntity);
	}

	//同步用户移除(角色)组: el/sync/removeusersfromrole
	private void removeusersfromroleTest() {
		RoleService roleService = new RoleService();
		List<String> userNames = new ArrayList<String>();
		userNames.add("yaoj");
		userNames.add("lili");
		ResultEntity resultEntity = roleService.removeusersfromrole(userNames);
		print("同步用户移除(角色)组",resultEntity);
	}
	
	private void print(String name, ResultEntity resultEntity){
		System.out.println(name+":"+resultEntity.getCode()+"=="+resultEntity.getData()+"=="+resultEntity.getMessage()+"=="+resultEntity.getTotalCount());
	}

}
