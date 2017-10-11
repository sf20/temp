package openDemo.test;

import openDemo.service.sync.xingdou.XingdouSyncService;

public class XingdouSyncServiceTest {
	public static void main(String[] args) {
		XingdouSyncService service = new XingdouSyncService();
		try {
			service.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
