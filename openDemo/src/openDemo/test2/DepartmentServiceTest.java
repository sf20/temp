package openDemo.test2;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 部门服务
 * 
 * @author Yang.Yinghai
 * @date 2014-8-20上午9:48:52 @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class DepartmentServiceTest {

	private static final String URL = "http://oa.lonch.com.cn:8081";
	private static final String USERNAME = "xinyue";
	private static final String PASSWORD = "654321";

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// getDepartmentById("-6662234105694827272");
		String acconutname = "致医健康";
		// String acconutname = "单位" ;
		getAllDepartmentByAccountName(acconutname, true);
		// getDepartmentByCode("1111111");
		// createDepartment();
	}

	/**
	 * 创建部门
	 */
	public static void createDepartment() {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				Department dept = new Department();
				dept.setOrgAccountId(5732891513391105903L);
				dept.setId(7313901301646381000L);
				dept.setName("炼狱殿");
				dept.setEnabled(true);
				dept.setSortId(5L);
				dept.setIsGroup(false);
				dept.setSuperior(5732891513391105903L);
				dept.setPath("000000050002");
				Map<String, Object> map = new HashMap<>();
				map.put("orgDepartment", dept);

				String json = HttpclientUtil.post(null, URL);
				if (json != null && !"".equals(json)) {
					System.out.println("创建部门成功 ！！");
					System.out.println(json);
				} else {
					System.out.println("创建部门失败！！");
				}
			} catch (Exception e) {
				System.out.println("创建部门异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 根据部门ID获取部门
	 * 
	 * @param deptId
	 *            部门ID
	 */
	public static void getDepartmentById(String deptId) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				Department dept = new Department();// AuthorityServiceTest.getInstence().getClient().get("orgDepartment/"
													// + deptId,
				// Department.class);
				if (dept != null) {
					System.out.println("获取部门信息成功！！");
					System.out.println(dept.getName());
				} else {
					System.out.println("没有满足条件的部门信息！！");
				}
			} catch (Exception e) {
				System.out.println("获取部门信息异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 根据部门代码获取部门
	 * 
	 * @param code
	 *            部门代码
	 */
	public static void getDepartmentByCode(String code) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				Department[] depts = null;// AuthorityServiceTest.getInstence().getClient().get("orgDepartment/code/" +
											// code, Department[].class);
				if (depts != null && depts.length > 0) {
					System.out.println("获取部门信息成功！！");
					System.out.println(depts[0].getName());
				} else {
					System.out.println("没有满足条件的部门信息！！");
				}
			} catch (Exception e) {
				System.out.println("获取部门信息异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 获取指定单位的所有部门
	 * 
	 * @param accountName
	 *            单位名称
	 * @param isContainDisable
	 *            是否包含停用部门
	 */
	// TODO
	public static void getAllDepartmentByAccountName(String accountName, boolean isContainDisable) {
		// AuthorityServiceTest.getInstence().authenticate();
		// System.out.println("111获取单位所有部门信息成功！！");

		// if (AuthorityServiceTest.getInstence().authenticate()) {
		try {
			long accountId = 1L;// AccountServiceTest.getAccountId(accountName);
			// long accountId = Long.parseLong(accountName);

			String json = HttpclientUtil.get(URL + "/seeyon/rest/orgDepartments/all/" + accountId + "/token/E-learning/YUNxuetang123");
			System.out.println(json);
			// Department[] depts = null;//
			// AuthorityServiceTest.getInstence().getClient().get(
			// isContainDisable ? "orgDepartments/all/" + accountId : "orgDepartments/" +
			// accountId,
			// Department[].class);

			/*
			 * System.out.println(depts.length); for (Department dep : depts) {
			 * System.out.println("=部门名称==" + dep.getName()); } if (depts != null &&
			 * depts.length > 0) { System.out.println("获取单位所有部门信息成功！！");
			 * System.out.println(depts[0].getName()); } else {
			 * System.out.println("没有满足条件的部门信息！！"); }
			 */
		} catch (Exception e) {
			System.out.println("获取单位所有部门信息异常！！异常信息：" + e.getMessage());
			e.printStackTrace();
		}
		// } else {
		// System.out.println("身份验证失败！！！");
		// }
	}

	/**
	 * 获取指定单位的所有部门
	 * 
	 * @param accountName
	 *            单位名称
	 * @param isContainDisable
	 *            是否包含停用部门
	 */
	public static void getAllDepartmentByAccountNameCount(String accountName, boolean isContainDisable) {
		// AuthorityServiceTest.getInstence().authenticate();
		// System.out.println("111获取单位所有部门信息成功！！");

		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				long accountId = 0;// AccountServiceTest.getAccountId(accountName);
				// long accountId = Long.parseLong(accountName);
				Department[] depts = null;// AuthorityServiceTest.getInstence().getClient()
				// .get(isContainDisable ? "orgDepartments/all/" + accountId +
				// "?pageNo=1&pageSize=300"
				// : "orgDepartments/" + accountId + "?pageNo=1&pageSize=300",
				// Department[].class);
				// 84962047
				System.out.println("=========" + depts.length);

				for (Department dep : depts) {
					// System.out.println("=部门名称=="+dep.getEnabled());
					System.out.println("=部门名称==" + dep.getName() + "==" + dep.isEnabled());
				}
				if (depts != null && depts.length > 0) {
					System.out.println("获取单位所有部门信息成功！！");
					System.out.println(depts[0].getName());
				} else {
					System.out.println("没有满足条件的部门信息！！");
				}
			} catch (Exception e) {
				System.out.println("获取单位所有部门信息异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 获取指定单位的部门个数
	 * 
	 * @param accountName
	 *            单位名称
	 * @param isContainDisable
	 *            是否包含停用部门
	 */
	public static void getDepartmentCountByAccountName(String accountName, boolean isContainDisable) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				long accountId = 0;// AccountServiceTest.getAccountId(accountName);
				String json = null;// AuthorityServiceTest.getInstence().getClient()
									// .get("orgDepartments/all/count/" + accountId, String.class);
				if (json != null && !"".equals(json)) {
					System.out.println("获取单位部门个数成功！！");
					System.out.println(json);
				} else {
					System.out.println("没有满足条件的部门信息！！");
				}
			} catch (Exception e) {
				System.out.println("获取单位部门个数异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 获取指定单位的部门个数
	 * 
	 * @param accountName
	 *            单位名称
	 * @param isContainDisable
	 *            是否包含停用部门
	 */
	public static void getDepartmentCountByAccountNameOne(String accountName, boolean isContainDisable) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				long accountId = 0;// AccountServiceTest.getAccountId(accountName);
				String json = null;// AuthorityServiceTest.getInstence().getClient()
									// .get("orgDepartments/all/count/" + accountId + "?pageNo=1&pageSize=500",
									// String.class);
				if (json != null && !"".equals(json)) {
					System.out.println("获取单位部门个数成功！！");
					System.out.println(json);
				} else {
					System.out.println("没有满足条件的部门信息！！");
				}
			} catch (Exception e) {
				System.out.println("获取单位部门个数异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 根据部门ID修改部门
	 * 
	 * @param deptId
	 *            部门ID
	 */
	public static void updateDepartmentById(String deptId) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				// Account account = new Account();
				// account.setId(Long.parseLong(accountId));
				// account.setName("海海测试用");
				// String json =
				// AuthorityServiceTest.getInstence().getClient().post("orgAccount/",
				// objectMapper.writeValueAsString(account), String.class);
				Map<String, Object> beanMap = new HashMap<String, Object>();
				beanMap.put("id", deptId);
				beanMap.put("name", "REST接口修改");
				String json = null; // AuthorityServiceTest.getInstence().getClient().put("orgDepartment", beanMap,
									// String.class);
				if (json != null && !"".equals(json)) {
					System.out.println("更新部门信息成功！！");
					System.out.println(json);
				} else {
					System.out.println("更新部门信息失败！！");
				}
			} catch (Exception e) {
				System.out.println("更新部门信息异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 根据部门ID 启用/停用 部门
	 * 
	 * @param deptId
	 *            部门ID
	 * @param enabled
	 *            是否启用 true:启用 false:停用
	 */
	public static void enableDepartmentById(String deptId, boolean enabled) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				// Account account = new Account();
				// account.setId(Long.parseLong(accountId));
				// account.setName("海海测试用");
				// String json =
				// AuthorityServiceTest.getInstence().getClient().post("orgAccount/",
				// objectMapper.writeValueAsString(account), String.class);
				Map<String, Object> beanMap = new HashMap<String, Object>();
				beanMap.put("id", deptId);
				beanMap.put("enabled", enabled);
				String json = null;// AuthorityServiceTest.getInstence().getClient() .put("orgDepartment/" + deptId
									// + "/enabled/" + enabled, beanMap, String.class);
				if (json != null && !"".equals(json)) {
					System.out.println("启用/停用  部门成功！！");
					System.out.println(json);
				} else {
					System.out.println("启用/停用  部门失败！！");
				}
			} catch (Exception e) {
				System.out.println("启用/停用  部门异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}

	/**
	 * 根据部门ID删除部门
	 * 
	 * @param deptId
	 *            部门ID
	 */
	public static void deleteDepartmentById(String deptId) {
		if (AuthorityServiceTest.getInstence().authenticate()) {
			try {
				String json = null;// AuthorityServiceTest.getInstence().getClient().delete("orgDepartment/" +
									// deptId, null, String.class);
				if (json != null && !"".equals(json)) {
					System.out.println("删除部门成功！！");
					System.out.println(json);
				} else {
					System.out.println("删除部门失败！！");
				}
			} catch (Exception e) {
				System.out.println("删除部门异常！！异常信息：" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("身份验证失败！！！");
		}
	}
}
