package openDemo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import openDemo.common.JdbcUtil;
import openDemo.entity.UserInfoEntity;

public class UserInfoDao implements GenericDao<UserInfoEntity> {
	private Connection conn;

	public UserInfoDao() {
		conn = JdbcUtil.getConnection();
	}

	@Override
	public UserInfoEntity getById(String id) throws SQLException {
		String sql = "SELECT t.* FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` t where t.ID = ?";

		return new QueryRunner().query(conn, sql, new BeanHandler<>(UserInfoEntity.class), id);
	}

	@Override
	public List<UserInfoEntity> getAll() throws SQLException {
		String sql = "SELECT t.* FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` t";

		return new QueryRunner().query(conn, sql, new BeanListHandler<>(UserInfoEntity.class));
	}

	@Override
	public void insert(UserInfoEntity user) throws SQLException {
		new QueryRunner().update(conn, getInsertSql(), getInsertObjectParamArray(user));
	}

	@Override
	public void update(UserInfoEntity user) throws SQLException {
		new QueryRunner().update(conn, getUpdateSql(), getUpdateObjectParamArray(user));
	}

	@Override
	public void deleteById(String id) throws SQLException {
		String sql = "DELETE FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` WHERE ID = ?";

		new QueryRunner().update(conn, sql, id);
	}

	@Override
	public void insertBatch(List<UserInfoEntity> list) throws SQLException {
		int listSize = list.size();
		Object[][] params = new Object[listSize][];
		for (int i = 0; i < listSize; i++) {
			params[i] = getInsertObjectParamArray(list.get(i));
		}

		new QueryRunner().batch(conn, getInsertSql(), params);
	}

	@Override
	public void updateBatch(List<UserInfoEntity> list) throws SQLException {
		int listSize = list.size();
		Object[][] params = new Object[listSize][];
		for (int i = 0; i < listSize; i++) {
			params[i] = getUpdateObjectParamArray(list.get(i));
		}

		new QueryRunner().batch(conn, getUpdateSql(), params);
	}

	@Override
	public void deleteByIds(String[] ids) throws SQLException {
		String sql = "DELETE FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` WHERE ID = ?";

		int len = ids.length;
		Object[][] params = new Object[len][];
		for (int i = 0; i < len; i++) {
			params[i] = new Object[] { ids[i] };
		}

		new QueryRunner().batch(conn, sql, params);
	}

	/**
	 * 新增sql
	 * 
	 * @return
	 */
	private String getInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` ");
		buffer.append(
				"(ID, UserName, CnName, Password, Sex, Mobile, Mail, OrgOuCode, EncryptionType, PostionNo, Entrytime,");
		buffer.append(
				" Birthday, ExpireDate, Spare1, Spare2, Spare3, Spare4, Spare5, Spare6, Spare7, Spare8, Spare9, Spare10)");
		buffer.append(" VALUES ");
		buffer.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		return buffer.toString();
	}

	/**
	 * 更新sql
	 * 
	 * @return
	 */
	private String getUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` SET ");
		buffer.append("UserName = ?,");
		buffer.append("CnName = ?,");
		buffer.append("Password = ?,");
		buffer.append("Sex = ?,");
		buffer.append("Mobile = ?,");
		buffer.append("Mail = ?,");
		buffer.append("OrgOuCode = ?,");
		buffer.append("EncryptionType = ?,");
		buffer.append("PostionNo = ?,");
		buffer.append("Entrytime = ?,");
		buffer.append("Birthday = ?,");
		buffer.append("ExpireDate = ?,");
		buffer.append("Spare1 = ?,");
		buffer.append("Spare2 = ?,");
		buffer.append("Spare3 = ?,");
		buffer.append("Spare4 = ?,");
		buffer.append("Spare5 = ?,");
		buffer.append("Spare6 = ?,");
		buffer.append("Spare7 = ?,");
		buffer.append("Spare8 = ?,");
		buffer.append("Spare9 = ?,");
		buffer.append("Spare10 = ?");
		buffer.append(" WHERE ID = ?;");

		return buffer.toString();
	}

	/**
	 * 批量化新增的参数数组
	 * 
	 * @param user
	 * @return
	 */
	private Object[] getInsertObjectParamArray(UserInfoEntity user) {
		Object[] params = { user.getID(), user.getUserName(), user.getCnName(), user.getPassword(), user.getSex(),
				user.getMobile(), user.getMail(), user.getOrgOuCode(), user.getEncryptionType(), user.getPostionNo(),
				user.getEntryTime(), user.getBirthday(), user.getExpireDate(), user.getSpare1(), user.getSpare2(),
				user.getSpare3(), user.getSpare4(), user.getSpare5(), user.getSpare6(), user.getSpare7(),
				user.getSpare8(), user.getSpare9(), user.getSpare10() };
		return params;
	}

	/**
	 * 批量化更新的参数数组
	 * 
	 * @param user
	 * @return
	 */
	private Object[] getUpdateObjectParamArray(UserInfoEntity user) {
		Object[] params = { user.getUserName(), user.getCnName(), user.getPassword(), user.getSex(), user.getMobile(),
				user.getMail(), user.getOrgOuCode(), user.getEncryptionType(), user.getPostionNo(), user.getEntryTime(),
				user.getBirthday(), user.getExpireDate(), user.getSpare1(), user.getSpare2(), user.getSpare3(),
				user.getSpare4(), user.getSpare5(), user.getSpare6(), user.getSpare7(), user.getSpare8(),
				user.getSpare9(), user.getSpare10(), user.getID() };
		return params;
	}
}
