package openDemo.service.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import openDemo.config.AllinpayConfig;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;

public class AllinpaySyncService extends AbstractSyncService2 implements AllinpayConfig {
	// 全量增量区分
	private static final String MODE_FULL = "0";
	private static final String MODE_UPDATE = "1";
	// 登录参数
	private static final String HOST = "116.228.64.54";
	private static final int PORT = 8002;
	private static final String USERNAME = "sftpuser";
	private static final String PASSWORD = "1qaz@WSX";
	// 字符集编码
	private static final String CHARSET_GBK = "GBK";
	private static final String CHARSET_UTF8 = "UTF-8";
	// 文件名
	private static final String POSITION_FILE = "ywlinkstation.txt";
	private static final String OUINFO_FILE = "ywlinkdept.txt";
	private static final String USERINFO_FILE = "ywlinkman.txt";
	// 分隔符
	private static final String SEPARATOR = "|";
	// 分隔符正则转译
	private static final String SEPARATOR_REGEX = "\\|";
	// 记录日志
	private static final Logger LOGGER = LogManager.getLogger(AllinpaySyncService.class);
	// sftp连接对象
	private Session session;
	private ChannelSftp channelSftp;

	public AllinpaySyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		// 人员信息中未提供岗位id
		super.setIsPosIdProvided(false);
		super.setLogger(LOGGER);
	}

	/**
	 * 连接到sftp服务器
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @throws JSchException
	 */
	private void sftpConnect(String host, int port, String username, String password) throws JSchException {
		try {
			if (session == null || !session.isConnected()) {
				JSch jsch = new JSch();
				session = jsch.getSession(username, host, port);
				session.setPassword(password);
				Properties sshConfig = new Properties();
				sshConfig.put("StrictHostKeyChecking", "no");
				session.setConfig(sshConfig);
				session.connect();
			}

			if (channelSftp == null || channelSftp.isClosed() || !channelSftp.isConnected()) {
				channelSftp = (ChannelSftp) session.openChannel("sftp");
				channelSftp.connect();
			}
		} catch (JSchException e) {
			sftpDisconnect();
			throw e;
		}
	}

	/**
	 * 与sftp服务器断开连接 释放资源
	 */
	private void sftpDisconnect() {
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}

		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	/**
	 * 下载sftp上的文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private List<String> downloadAsString(String fileName) throws Exception {
		List<String> lines = null;
		try {
			// 建立连接
			sftpConnect(HOST, PORT, USERNAME, PASSWORD);
			// 获取文件
			lines = readLines(channelSftp.get(fileName), CHARSET_GBK, CHARSET_UTF8);
		} finally {
			// 断开连接
			sftpDisconnect();
		}

		return lines;
	}

	/**
	 * 读取文件流并获得行字符串集合
	 * 
	 * @param in
	 *            文件字节流
	 * @param fromCharset
	 *            文件原来的字符集编码
	 * @param toCharset
	 *            同步用的字符集编码
	 * @return
	 * @throws IOException
	 */
	private List<String> readLines(InputStream in, String fromCharset, String toCharset) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			// 文件为GBK编码
			reader = new BufferedReader(new InputStreamReader(in, fromCharset));

			String tempLine = null;
			// 一次读一行
			while ((tempLine = reader.readLine()) != null) {
				// 转为UTF-8编码
				lines.add(new String(tempLine.getBytes(), toCharset));
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return lines;
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		// 部门状态（0：有效/1:无效）
		String status = org.getStatus();

		if ("1".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		// 是否活动用户（0/禁用;1/活动）
		String status = user.getStatus();
		// 是否被删除（0/否;1/是）
		String deleteStatus = user.getDeleteStatus();

		if ("0".equals(status) || "1".equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为0 有多个根组织
			if ("0".equals(org.getParentID())) {
				org.setParentID(null);
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {

			// 性别字符串转换 0：男 1：女
			String sex = tempModel.getSex();
			if ("0".equals(sex)) {
				tempModel.setSex("男");
			} else if ("1".equals(sex)) {
				tempModel.setSex("女");
			}
		}
	}

	@Override
	public List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<PositionModel> modelList = new ArrayList<PositionModel>();
		List<String> lines = downloadAsString(POSITION_FILE);

		String[] tempStrArr = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = line.split(SEPARATOR_REGEX);

			PositionModel pos = new PositionModel();
			pos.setpNo(tempStrArr[0]);
			pos.setpNames(tempStrArr[1]);

			modelList.add(pos);
		}

		return modelList;
	}

	@Override
	public List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<OuInfoModel> modelList = new ArrayList<OuInfoModel>();
		List<String> lines = downloadAsString(OUINFO_FILE);

		String[] tempStrArr = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = line.split(SEPARATOR_REGEX);

			if (tempStrArr.length != 4) {
				continue;
			}

			OuInfoModel ouInfo = new OuInfoModel();
			ouInfo.setID(tempStrArr[0]);
			ouInfo.setOuName(tempStrArr[1]);
			ouInfo.setParentID(tempStrArr[2]);
			ouInfo.setStatus(tempStrArr[3]);

			modelList.add(ouInfo);
		}
		return modelList;
	}

	@Override
	public List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<UserInfoModel> modelList = new ArrayList<UserInfoModel>();
		List<String> lines = downloadAsString(USERINFO_FILE);

		String[] tempStrArr = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = line.split(SEPARATOR_REGEX);

			if (tempStrArr.length != 12) {
				continue;
			}

			UserInfoModel userInfo = new UserInfoModel();
			userInfo.setID(tempStrArr[0]);
			userInfo.setUserName(tempStrArr[1]);
			userInfo.setCnName(tempStrArr[2]);
			userInfo.setSex(tempStrArr[3]);
			userInfo.setBirthday(tempStrArr[4]);
			userInfo.setOrgOuCode(tempStrArr[5]);
			userInfo.setPostionName(tempStrArr[6]);
			userInfo.setMobile(tempStrArr[7]);
			userInfo.setMail(tempStrArr[8]);
			userInfo.setEntryTime(tempStrArr[9]);
			userInfo.setStatus(tempStrArr[10]);
			userInfo.setDeleteStatus(tempStrArr[11]);

			modelList.add(userInfo);
		}
		return modelList;
	}

}
