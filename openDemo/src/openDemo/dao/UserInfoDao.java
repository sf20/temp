package openDemo.dao;

import openDemo.entity.UserInfoEntity;

public class UserInfoDao extends GenericDaoImpl<UserInfoEntity> {

	@Override
	String generateGetByIdSql() {
		return "SELECT t.* FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` t where t.ID = ?";
	}

	@Override
	String generateGetAllSql() {
		return "SELECT t.* FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` t";
	}

	@Override
	String generateGetAllCountSql() {
		return "SELECT count(*) FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128`";
	}

	@Override
	String generateInsertSql() {
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

	@Override
	String generateUpdateSql() {
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
		buffer.append(" WHERE ID = ?");

		return buffer.toString();
	}

	@Override
	String generateDeleteByIdSql() {
		return "DELETE FROM `userinfo_57dac39f-aa0c-42dc-a64f-eae4618dd128` WHERE ID = ?";
	}

	@Override
	Object[] getInsertObjectParamArray(UserInfoEntity user) {
		Object[] params = { user.getID(), user.getUserName(), user.getCnName(), user.getPassword(), user.getSex(),
				user.getMobile(), user.getMail(), user.getOrgOuCode(), user.getEncryptionType(), user.getPostionNo(),
				user.getEntryTime(), user.getBirthday(), user.getExpireDate(), user.getSpare1(), user.getSpare2(),
				user.getSpare3(), user.getSpare4(), user.getSpare5(), user.getSpare6(), user.getSpare7(),
				user.getSpare8(), user.getSpare9(), user.getSpare10() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(UserInfoEntity user) {
		Object[] params = { user.getUserName(), user.getCnName(), user.getPassword(), user.getSex(), user.getMobile(),
				user.getMail(), user.getOrgOuCode(), user.getEncryptionType(), user.getPostionNo(), user.getEntryTime(),
				user.getBirthday(), user.getExpireDate(), user.getSpare1(), user.getSpare2(), user.getSpare3(),
				user.getSpare4(), user.getSpare5(), user.getSpare6(), user.getSpare7(), user.getSpare8(),
				user.getSpare9(), user.getSpare10(), user.getID() };
		return params;
	}
}
