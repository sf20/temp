package openDemo.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;

import openDemo.entity.StudyPlanDetail;
import openDemo.entity.UserInfoModel;

public class UserInfoDao extends GenericDaoImpl<UserInfoModel> {

	@Override
	String getTableNamePrefix() {
		return TABLENAME_PREFIX_USERINFO;
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(generateTableName());
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
		buffer.append("UPDATE ").append(generateTableName()).append(" SET ");
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
	Object[] getInsertObjectParamArray(UserInfoModel user) {
		Object[] params = { user.getID(), user.getUserName(), user.getCnName(), user.getPassword(), user.getSex(),
				user.getMobile(), user.getMail(), user.getOrgOuCode(), user.getEncryptionType(), user.getPostionNo(),
				user.getEntryTime(), user.getBirthday(), user.getExpireDate(), user.getSpare1(), user.getSpare2(),
				user.getSpare3(), user.getSpare4(), user.getSpare5(), user.getSpare6(), user.getSpare7(),
				user.getSpare8(), user.getSpare9(), user.getSpare10() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(UserInfoModel user) {
		Object[] params = { user.getUserName(), user.getCnName(), user.getPassword(), user.getSex(), user.getMobile(),
				user.getMail(), user.getOrgOuCode(), user.getEncryptionType(), user.getPostionNo(), user.getEntryTime(),
				user.getBirthday(), user.getExpireDate(), user.getSpare1(), user.getSpare2(), user.getSpare3(),
				user.getSpare4(), user.getSpare5(), user.getSpare6(), user.getSpare7(), user.getSpare8(),
				user.getSpare9(), user.getSpare10(), user.getID() };
		return params;
	}

	@Override
	String generateGetAllSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID, USERNAME FROM CORE_USERPROFILE WHERE ORGID = '71028353-7246-463f-ab12-995144fb4cb2' AND ISADMIN = 0 AND STATUS = 1 ORDER BY CREATEDATE DESC");
		return sql.toString();
	}

	public List<StudyPlanDetail> getStudyPlanDetailByUserIdPlanID(String orgID, String planID, String userID,
			String kngType) throws SQLException {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		List<StudyPlanDetail> list = new ArrayList<StudyPlanDetail>();

		sql.append("SELECT p1.parentplanid as PlanID,p1.planname,").append("       p1.MasterTitle,uk.ActualStudyScore,")
				.append("       uk.LastestStudyTime,uk.StudySchedule,")
				.append(" CASE WHEN p1.status=2 OR (p1.progress=100.0 AND p1.status=3) THEN 2")
				.append("      WHEN p1.status=1 THEN 1").append("      WHEN p1.status=0 THEN 0")
				.append("      ELSE 3 END status, ")
				.append(" CASE WHEN uk.filetype <>'OteExam'THEN uk.actualstudyfinishdate ")
				.append("      ELSE NULL END AS ActualFinishDate, ")
				.append(" CASE WHEN uk.knowledgetype = 'OteExam' THEN uk.exammaxscore")
				.append("      ELSE NULL END AS ExamTotalScore,")
				.append(" CASE WHEN uk.filetype= 'OteExam' AND uk.status = 'Completed' THEN uk.examscore")
				.append("      ELSE NULL END AS ExamScore,")
				.append(" CASE WHEN uk.filetype = 'OteExam'AND uk.status = 'Completed'AND uk.examispass = 1 THEN 1 ")
				.append("      WHEN uk.filetype = 'OteExam'AND uk.status = 'Completed'AND uk.examispass = 0 THEN 0 ")
				.append("      ELSE NULL END AS IsPassed")
				.append(" FROM (SELECT userplan.id,userplan.parentplanid,userplan.executoruserid,")
				.append("            p.planname,p.createusername,p.phasename, p.masterid,")
				.append("            p.mastertitle,p.filetype,p.orderindex,userplan.progress,userplan.status ")
				.append("       FROM (SELECT id, executoruserid, parentplanid,progress,status FROM sty_userstudyplan ")
				.append("              WHERE orgid = ? AND parentplanid = ?  ");
		params.add(orgID);
		params.add(planID);
		if (StringUtils.isNotBlank(userID)) {
			sql.append(" and executoruserid=? ");
			params.add(userID);
		}
		sql.append(" ) userplan").append(
				"       JOIN (SELECT splan.id, splan.name planname,splan.createusername,splanphase.name phasename, ")
				.append("             CASE WHEN splancontent.mastertype <>'' THEN splancontent.masterid ")
				.append("               ELSE splancontent.id END masterid,")
				.append("             splancontent.mastertitle, splancontent.filetype,rownum orderindex ")
				.append("             FROM ( SELECT id, name, createusername  ");
		sql.append("                      FROM sty_studyplan WHERE id = ?) splan ");
		params.add(planID);
		sql.append("             JOIN ( SELECT * FROM sty_studyplanphase ")
				.append("                     WHERE orgid= ? AND studyplanid = ? ORDER BY orderindex ) splanphase ");
		params.add(orgID);
		params.add(planID);
		sql.append("             JOIN (SELECT id, phaseid, masterid, mastertitle, mastertype, filetype, orderindex ")
				.append("                     FROM sty_studyplancontent WHERE orgid = ? ")
				.append("                     AND studyplanid = ? ");
		params.add(orgID);
		params.add(planID);
		if (StringUtils.isNotBlank(kngType)) {
			sql.append(" and mastertype=? ");
			params.add(kngType);
		}
		sql.append(" ) splancontent ").append("               ON splancontent.phaseid = splanphase.id ")
				.append("               ON splanphase.studyplanid = splan.id")
				.append("               ORDER BY splanphase.orderindex, splancontent.orderindex  ")
				.append("            ) p ").append("     ON p.id = userplan.parentplanid ) p1")
				.append(" LEFT OUTER JOIN (SELECT userid, knowledgeid, knowledgesourceid, ")
				.append("     knowledgetitle,knowledgetype, filetype, status,")
				.append("     actualobtainedscore as actualstudyscore, lasteststudytime,")
				.append("     studyschedule,actualstudyfinishdate, thirdmasterid,")
				.append("     examispass, examscore,exammaxscore ")
				.append("     FROM core_userknowledge WHERE orgid = ? ");
		params.add(orgID);
		if (StringUtils.isNotBlank(userID)) {
			sql.append(" and userid=? ");
			params.add(userID);
		}
		sql.append(") uk ").append(" ON uk.thirdmasterid = p1.id AND uk.knowledgeid = p1.masterid ")
				.append(" ORDER BY p1.orderindex asc");

		list = new QueryRunner(dataSource).query(sql.toString(), new BeanListHandler<>(StudyPlanDetail.class),
				params.toArray());

		return list;
	}
}
