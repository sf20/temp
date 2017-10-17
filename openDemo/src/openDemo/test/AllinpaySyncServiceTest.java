package openDemo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.service.sync.AllinpaySyncService;

public class AllinpaySyncServiceTest {
	private static final String HOST = "116.228.64.54";
	private static final int PORT = 8002;
	private static final String USERNAME = "sftpuser";
	private static final String PASSWORD = "1qaz@WSX";
	// 字符集编码
	private static final String CHARSET_GBK = "GBK";
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String POSITION_FILE = "ywlinkstation.txt";
	private static final String OUINFO_FILE = "ywlinkdept.txt";
	private static final String USERINFO_FILE = "ywlinkman.txt";
	// sftp连接对象
	private static Session session;
	private static ChannelSftp channelSftp;

	public static void main(String[] args) throws Exception {
		// getDataTest();
		// getDataModelTest();
		syncTest();
	}

	static void syncTest() {
		AllinpaySyncService service = new AllinpaySyncService();
		try {
			service.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void getDataModelTest() throws Exception {
		AllinpaySyncService service = new AllinpaySyncService();

		List<PositionModel> positionModelList = service.getPositionModelList(null);
		System.out.println(positionModelList.size());
		// for (PositionModel pos : positionModelList) {
		// System.out.println(pos.getpNo() + "=" + pos.getpNames());
		// }

		List<OuInfoModel> ouInfoModelList = service.getOuInfoModelList(null);
		System.out.println(ouInfoModelList.size());
		// for (OuInfoModel dept : ouInfoModelList) {
		// System.out
		// .println(dept.getID() + "=" + dept.getOuName() + "=" + dept.getParentID() +
		// "=" + dept.getStatus());
		// }

		List<UserInfoModel> userInfoModelList = service.getUserInfoModelList(null);
		System.out.println(userInfoModelList.size());
		// for (UserInfoModel user : userInfoModelList) {
		// System.out.println(user.getID() + "=" + user.getUserName() + "=" +
		// user.getCnName() + "=" + user.getSex()
		// + "=" + user.getBirthday() + "=" + user.getOrgOuCode() + "=" +
		// user.getPostionName() + "="
		// + user.getMobile() + "=" + user.getMail() + "=" + user.getEntryTime() + "=" +
		// user.getStatus() + "="
		// + user.getDeleteStatus());
		// }
	}

	static void getDataTest() throws Exception {
		List<String> list = downloadAsString(POSITION_FILE);
		System.out.println(list.size());
		for (String s : list) {
			System.out.println(s);
		}
		downloadAsString(OUINFO_FILE);
		downloadAsString(USERINFO_FILE);

		sftpDisconnect();
	}

	private static void sftpDisconnect() {
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}

		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	public static void sftpConnect(String host, int port, String username, String password) throws JSchException {
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

	public static List<String> downloadAsString(String fileName) throws Exception {
		// 建立连接
		sftpConnect(HOST, PORT, USERNAME, PASSWORD);
		// 获取文件
		return readLines(channelSftp.get(fileName), CHARSET_GBK, CHARSET_UTF8);
	}

	private static List<String> readLines(InputStream in, String fromCharset, String toCharset) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, fromCharset));

			String tempLine = null;
			while ((tempLine = reader.readLine()) != null) {
				// GBK编码转UTF-8编码
				lines.add(new String(tempLine.getBytes(), toCharset));
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return lines;
	}

}
