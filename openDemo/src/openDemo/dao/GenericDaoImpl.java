package openDemo.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import openDemo.common.JdbcUtil;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {
	private Connection conn;
	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		conn = JdbcUtil.getConnection();

		// 得到T.class
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public T getById(String id) throws SQLException {
		return new QueryRunner().query(conn, generateGetByIdSql(), new BeanHandler<>(entityClass), id);
	}

	@Override
	public List<T> getAll() throws SQLException {
		return new QueryRunner().query(conn, generateGetAllSql(), new BeanListHandler<>(entityClass));
	}

	@Override
	public int getAllCount() throws SQLException {
		return new QueryRunner().query(conn, generateGetAllCountSql(), new ScalarHandler<Long>()).intValue();
	}

	@Override
	public void insert(T t) throws SQLException {
		new QueryRunner().update(conn, generateInsertSql(), getInsertObjectParamArray(t));
	}

	@Override
	public void update(T t) throws SQLException {
		new QueryRunner().update(conn, generateUpdateSql(), getUpdateObjectParamArray(t));
	}

	@Override
	public void deleteById(String id) throws SQLException {
		new QueryRunner().update(conn, generateDeleteByIdSql(), id);
	}

	@Override
	public void insertBatch(List<T> list) throws SQLException {
		int listSize = list.size();
		Object[][] params = new Object[listSize][];
		for (int i = 0; i < listSize; i++) {
			params[i] = getInsertObjectParamArray(list.get(i));
		}

		new QueryRunner().batch(conn, generateInsertSql(), params);
	}

	@Override
	public void updateBatch(List<T> list) throws SQLException {
		int listSize = list.size();
		Object[][] params = new Object[listSize][];
		for (int i = 0; i < listSize; i++) {
			params[i] = getUpdateObjectParamArray(list.get(i));
		}

		new QueryRunner().batch(conn, generateUpdateSql(), params);
	}

	@Override
	public void deleteByIds(String[] ids) throws SQLException {
		int len = ids.length;
		Object[][] params = new Object[len][];
		for (int i = 0; i < len; i++) {
			params[i] = new Object[] { ids[i] };
		}

		new QueryRunner().batch(conn, generateDeleteByIdSql(), params);
	}

	abstract String generateGetByIdSql();

	abstract String generateGetAllSql();

	abstract String generateGetAllCountSql();

	abstract String generateInsertSql();

	abstract String generateUpdateSql();

	abstract String generateDeleteByIdSql();

	/**
	 * 批量化新增的参数数组
	 * 
	 * @param t
	 * @return
	 */
	abstract Object[] getInsertObjectParamArray(T t);

	/**
	 * 批量化更新的参数数组
	 * 
	 * @param t
	 * @return
	 */
	abstract Object[] getUpdateObjectParamArray(T t);

}
