package openDemo.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> {

	T getById(String id) throws SQLException;

	List<T> getAll() throws SQLException;

	int getAllCount() throws SQLException;;

	void insert(T t) throws SQLException;

	void update(T t) throws SQLException;

	void deleteById(String id) throws SQLException;

	void insertBatch(List<T> t) throws SQLException;

	void updateBatch(List<T> t) throws SQLException;

	void deleteByIds(String[] id) throws SQLException;
}
