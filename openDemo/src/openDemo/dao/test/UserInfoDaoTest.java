package openDemo.dao.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import openDemo.config.TestConfig;
import openDemo.dao.UserInfoDao;
import openDemo.entity.StudyPlanDetail;
import openDemo.entity.UserInfoModel;

public class UserInfoDaoTest implements TestConfig {
	UserInfoDao dao = new UserInfoDao();

	public static void main(String[] args) throws SQLException {
		new UserInfoDaoTest().test();
	}

	public void test() throws SQLException {
		// insertTest();
		// insertListTest();

		// updateTest();
		// updateListTest();

		// deleteByIdTest();
		// deleteByIdsTest();

		// getByIdTest();
		// getAllCountTest();

		// getAllTest();
		getStudyPlanDetailByUserIdPlanID();
	}

	public void getStudyPlanDetailByUserIdPlanID() throws SQLException {
		List<StudyPlanDetail> list = dao.getStudyPlanDetailByUserIdPlanID(apikey,
				"cb1fbf1b-e9f1-43d3-b9eb-c7ffedec3b24", "8d6b3361-59a5-4032-8a89-298cf9501b0b", "exam");
		System.out.println(list.size());
		for (StudyPlanDetail studyPlan : list) {
			System.out.println(studyPlan.getStatus() + "=" + studyPlan.getIsPassed());
		}
	}

	public void insertTest() throws SQLException {
		UserInfoModel user = new UserInfoModel();
		user.setID("ID0");
		user.setUserName("userName");
		user.setCnName("cnName");
		user.setPassword("password");
		user.setSex("男");
		user.setMobile("12345678901");
		user.setMail("mail");
		user.setOrgOuCode("orgOuCode");
		user.setEncryptionType("encryptionType");
		user.setPostionNo("postionNo");
		// user.setEntryTime(new Date());
		// user.setBirthday(new Date());
		user.setExpireDate(null);
		user.setSpare1("spare1");
		user.setSpare2("spare2");
		user.setSpare3("spare3");
		user.setSpare4("spare4");
		user.setSpare5("spare5");
		user.setSpare6("spare6");
		user.setSpare7("spare7");
		user.setSpare8("spare8");
		user.setSpare9("spare9");
		user.setSpare10("spare10");

		dao.insert(user);
	}

	public void insertListTest() throws SQLException {
		List<UserInfoModel> list = new ArrayList<>();
		for (int i = 1; i < 10; i++) {
			UserInfoModel user = new UserInfoModel();
			user = new UserInfoModel();
			user.setID("ID" + i);
			user.setUserName("userName" + i);
			user.setCnName("cnName" + i);
			user.setPassword("password" + i);
			user.setSex("男");
			user.setMobile("12345678901");
			user.setMail("mail" + i);
			user.setOrgOuCode("orgOuCode" + i);
			user.setEncryptionType("encryptionType" + i);
			user.setPostionNo("postionNo" + i);
			// user.setEntryTime(new Date());
			// user.setBirthday(new Date());
			user.setExpireDate(null);
			user.setSpare1("spare1");
			user.setSpare2("spare2");
			user.setSpare3("spare3");
			user.setSpare4("spare4");
			user.setSpare5("spare5");
			user.setSpare6("spare6");
			user.setSpare7("spare7");
			user.setSpare8("spare8");
			user.setSpare9("spare9");
			user.setSpare10("spare10");

			list.add(user);
		}

		dao.insertBatch(list);
	}

	public void updateTest() throws SQLException {
		UserInfoModel user = dao.getById("ID0");
		if (user != null) {
			user.setCnName("testName");
			dao.update(user);
		} else {
			System.out.println("no user find!!!");
		}

	}

	public void updateListTest() throws SQLException {
		List<UserInfoModel> list = dao.getAll();

		for (UserInfoModel user : list) {
			user.setSpare2("");
			user.setSpare4("");
			user.setSpare6("");
			user.setSpare8("");
			user.setSpare10("");
		}

		dao.updateBatch(list);
	}

	public void deleteByIdTest() throws SQLException {
		dao.deleteById("ID0");
	}

	public void deleteByIdsTest() throws SQLException {
		dao.deleteByIds(new String[] { "ID1", "ID3", "ID5", "ID7", "ID9", "ID2", "ID4", "ID6", "ID8", "ID9" });
	}

	public void getByIdTest() throws SQLException {
		UserInfoModel user = dao.getById("ID0");
		System.out.println(user.getID() + "==" + user.getUserName() + "==" + user.getCnName() + "=="
				+ user.getPassword() + "==" + user.getSex() + "==" + user.getMobile() + "==" + user.getMail() + "=="
				+ user.getOrgOuCode() + "==" + user.getEncryptionType() + "==" + user.getPostionNo() + "=="
				+ user.getEntryTime() + "==" + user.getBirthday() + "==" + user.getExpireDate() + "=="
				+ user.getSpare1() + "==" + user.getSpare2() + "==" + user.getSpare3() + "==" + user.getSpare4() + "=="
				+ user.getSpare5() + "==" + user.getSpare6() + "==" + user.getSpare7() + "==" + user.getSpare8() + "=="
				+ user.getSpare8() + "==" + user.getSpare10());
	}

	public void getAllCountTest() throws SQLException {
		System.out.println(dao.getAllCount());
	}

	public void getAllTest() throws SQLException {
		List<UserInfoModel> list = dao.getAll();
		System.out.println(list.size());
		for (UserInfoModel user : list) {
			System.out.println(user.getID() + "==" + user.getUserName() + "==" + user.getCnName() + "=="
					+ user.getPassword() + "==" + user.getSex() + "==" + user.getMobile() + "==" + user.getMail() + "=="
					+ user.getOrgOuCode() + "==" + user.getEncryptionType() + "==" + user.getPostionNo() + "=="
					+ user.getEntryTime() + "==" + user.getBirthday() + "==" + user.getExpireDate() + "=="
					+ user.getSpare1() + "==" + user.getSpare2() + "==" + user.getSpare3() + "==" + user.getSpare4()
					+ "==" + user.getSpare5() + "==" + user.getSpare6() + "==" + user.getSpare7() + "=="
					+ user.getSpare8() + "==" + user.getSpare9() + "==" + user.getSpare10());
		}
	}

}
