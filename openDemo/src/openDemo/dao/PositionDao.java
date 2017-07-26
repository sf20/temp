package openDemo.dao;

import openDemo.entity.PositionEntity;

public class PositionDao extends GenericDaoImpl<PositionEntity> {

	@Override
	String generateGetByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(generateTableName()).append(" WHERE pNo = ?");
		return sql.toString();
	}

	@Override
	String generateDeleteByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(generateTableName()).append(" WHERE pNo = ?");
		return sql.toString();
	}

	@Override
	String getTableNamePrefix() {
		return TABLENAME_PREFIX_POSITION;
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(generateTableName());
		buffer.append("(pNo, pNames) VALUES(?, ?)");

		return buffer.toString();
	}

	@Override
	String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(generateTableName()).append(" SET ");
		buffer.append("pNames = ?");
		buffer.append(" WHERE pNo = ?");

		return buffer.toString();
	}

	@Override
	Object[] getInsertObjectParamArray(PositionEntity position) {
		Object[] params = { position.getpNo(), position.getpNames() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(PositionEntity position) {
		Object[] params = { position.getpNames(), position.getpNo() };
		return params;
	}

}
