package openDemo.dao;

import openDemo.entity.OuInfoModel;

public class OuInfoDao extends GenericDaoImpl<OuInfoModel> {

	@Override
	String getTableNamePrefix() {
		return TABLENAME_PREFIX_OUINFO;
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(generateTableName());
		buffer.append("(ID, OuName, ParentID, Description, Users, isSub, OrderIndex)");
		buffer.append(" VALUES(?, ?, ?, ?, ?, ?, ?)");

		return buffer.toString();
	}

	@Override
	String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(generateTableName()).append(" SET ");
		buffer.append("OuName = ?,");
		buffer.append("ParentID = ?,");
		buffer.append("Description = ?,");
		buffer.append("Users = ?,");
		buffer.append("isSub = ?,");
		buffer.append("OrderIndex = ?");
		buffer.append(" WHERE ID = ?");

		return buffer.toString();
	}

	@Override
	Object[] getInsertObjectParamArray(OuInfoModel org) {
		Object[] params = { org.getID(), org.getOuName(), org.getParentID(), org.getDescription(), org.getUsers(),
				org.getIsSub(), org.getOrderIndex() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(OuInfoModel org) {
		Object[] params = { org.getOuName(), org.getParentID(), org.getDescription(), org.getUsers(), org.getIsSub(),
				org.getOrderIndex(), org.getID() };
		return params;
	}

}
