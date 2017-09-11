package openDemo.dao.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import openDemo.dao.PositionDao;
import openDemo.entity.PositionModel;

public class PositionDaoTest {
	PositionDao dao = new PositionDao();

	public static void main(String[] args) throws SQLException {
		new PositionDaoTest().test();
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

		getAllTest();
	}

	public void insertTest() throws SQLException {
		PositionModel position = new PositionModel();
		position.setpNo("pNo0");
		position.setpNames("pNames0");

		dao.insert(position);
	}

	public void insertListTest() throws SQLException {
		List<PositionModel> list = new ArrayList<>();

		for (int i = 1; i < 5; i++) {
			PositionModel position = new PositionModel();
			position.setpNo("pNo" + i);
			position.setpNames("pNames" + i);
			list.add(position);
		}

		dao.insertBatch(list);
	}

	public void updateTest() throws SQLException {
		PositionModel position = dao.getById("pNo0");
		if (position != null) {
			position.setpNames("testName");
			dao.update(position);
		} else {
			System.out.println("no position find!!!");
		}

	}

	public void updateListTest() throws SQLException {
		List<PositionModel> list = dao.getAll();

		for (PositionModel position : list) {
			position.setpNames("xxx");
		}

		dao.updateBatch(list);
	}

	public void deleteByIdTest() throws SQLException {
		dao.deleteById("pNo0");
	}

	public void deleteByIdsTest() throws SQLException {
		dao.deleteByIds(new String[] { "pNo0", "pNo1", "pNo2", "pNo3", "pNo4" });
	}

	public void getByIdTest() throws SQLException {
		PositionModel position = dao.getById("pNo4");
		if (position != null) {
			System.out.println(position.getpNo() + "==" + position.getpNames());
		} else {
			System.out.println("no data find!!");
		}
	}

	public void getAllCountTest() throws SQLException {
		System.out.println(dao.getAllCount());
	}

	public void getAllTest() throws SQLException {
		List<PositionModel> list = dao.getAll();
		System.out.println(list.size());
		for (PositionModel position : list) {
			System.out.println(position.getpNo() + "==" + position.getpNames());
		}
	}
}
