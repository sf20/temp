package openDemo.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import openDemo.dao.OuInfoDao;
import openDemo.entity.OpOuInfoEntity;

public class OuInfoDaoTest {
	OuInfoDao dao = new OuInfoDao();

	public static void main(String[] args) throws SQLException {
		new OuInfoDaoTest().test();
	}

	public void test() throws SQLException {
		// insertTest();
		// insertListTest();

		// updateTest();
		// updateListTest();

		// deleteByIdTest();
		// deleteByIdsTest();

		// getByIdTest();
		getAllCountTest();

		getAllTest();
	}

	public void insertTest() throws SQLException {
		OpOuInfoEntity org = new OpOuInfoEntity();
		org.setID("org1");
		org.setOuName("orgName");
		org.setParentID("org0");
		org.setDescription("description");

		dao.insert(org);
	}

	public void insertListTest() throws SQLException {
		List<OpOuInfoEntity> list = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			OpOuInfoEntity org = new OpOuInfoEntity();
			org.setID("org" + i);
			org.setOuName("orgName" + i);
			org.setParentID("org0");
			org.setDescription("description" + i);
			list.add(org);
		}

		dao.insertBatch(list);
	}

	public void updateTest() throws SQLException {
		OpOuInfoEntity org = dao.getById("org0");
		if (org != null) {
			org.setOuName("testName");
			dao.update(org);
		} else {
			System.out.println("no org find!!!");
		}

	}

	public void updateListTest() throws SQLException {
		List<OpOuInfoEntity> list = dao.getAll();

		for (OpOuInfoEntity org : list) {
			org.setDescription("test");
		}

		dao.updateBatch(list);
	}

	public void deleteByIdTest() throws SQLException {
		dao.deleteById("org3");
	}

	public void deleteByIdsTest() throws SQLException {
		dao.deleteByIds(new String[] { "org1", "org2", "org4" });
	}

	public void getByIdTest() throws SQLException {
		OpOuInfoEntity org = dao.getById("org1");
		System.out
				.println(org.getID() + "==" + org.getOuName() + "==" + org.getParentID() + "==" + org.getDescription());
	}

	public void getAllCountTest() throws SQLException {
		System.out.println(dao.getAllCount());
	}

	public void getAllTest() throws SQLException {
		List<OpOuInfoEntity> list = dao.getAll();
		for (OpOuInfoEntity org : list) {
			System.out.println(
					org.getID() + "==" + org.getOuName() + "==" + org.getParentID() + "==" + org.getDescription());
		}
	}
}
