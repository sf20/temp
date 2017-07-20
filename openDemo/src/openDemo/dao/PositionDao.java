package openDemo.dao;

import openDemo.entity.PositionEntity;

public class PositionDao extends GenericDaoImpl<PositionEntity> {

	@Override
	String generateGetByIdSql() {
		return "SELECT t.* FROM `position_57dac39f-aa0c-42dc-a64f-eae4618dd128` t where t.pNo = ?";
	}

	@Override
	String generateGetAllSql() {
		return "SELECT t.* FROM `position_57dac39f-aa0c-42dc-a64f-eae4618dd128` t";
	}

	@Override
	String generateGetAllCountSql() {
		return "SELECT count(*) FROM `position_57dac39f-aa0c-42dc-a64f-eae4618dd128`";
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO `position_57dac39f-aa0c-42dc-a64f-eae4618dd128` ");
		buffer.append("(pNo, pNames) VALUES(?, ?)");

		return buffer.toString();
	}

	@Override
	String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE `position_57dac39f-aa0c-42dc-a64f-eae4618dd128` SET ");
		buffer.append("pNames = ?");
		buffer.append(" WHERE pNo = ?");

		return buffer.toString();
	}

	@Override
	String generateDeleteByIdSql() {
		return "DELETE FROM `position_57dac39f-aa0c-42dc-a64f-eae4618dd128` WHERE pNo = ?";
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
