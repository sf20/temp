package openDemo.dao;

import openDemo.entity.OuInfoEntity;

public class OuInfoDao extends GenericDaoImpl<OuInfoEntity> {

	@Override
	String generateGetByIdSql() {
		return "SELECT t.* FROM `ouinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` t where t.ID = ?";
	}

	@Override
	String generateGetAllSql() {
		return "SELECT t.* FROM `ouinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` t";
	}

	@Override
	String generateGetAllCountSql() {
		return "SELECT count(*) FROM `ouinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128`";
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO `ouinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` ");
		buffer.append("(ID, OuName, ParentID, Description, Users, isSub, OrderIndex)");
		buffer.append(" VALUES(?, ?, ?, ?, ?, ?, ?)");

		return buffer.toString();
	}

	@Override
	String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE `ouinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` SET ");
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
	String generateDeleteByIdSql() {
		return "DELETE FROM `ouinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` WHERE ID = ?";
	}

	@Override
	Object[] getInsertObjectParamArray(OuInfoEntity org) {
		Object[] params = { org.getID(), org.getOuName(), org.getParentID(), org.getDescription(), org.getUsers(),
				org.getIsSub(), org.getOrderIndex() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(OuInfoEntity org) {
		Object[] params = { org.getOuName(), org.getParentID(), org.getDescription(), org.getUsers(), org.getIsSub(),
				org.getOrderIndex(), org.getID() };
		return params;
	}

}
