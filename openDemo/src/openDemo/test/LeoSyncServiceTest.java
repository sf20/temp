package openDemo.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import openDemo.utils.HttpClientUtil4Sync;

public class LeoSyncServiceTest {

	public static void main(String[] args) throws UnsupportedOperationException, IOException {
		String url = "https://open.leo.cn/v1/authentication/oauth2/get-token";

		Map<String, Object> map = new HashMap<>();
		map.put("access_key", "oleo_42db6ee396eb8765435e44446befad8e");
		map.put("secret_key", "5f81f9a50e7c4043efece652b7a82be2d0d90839b9b550b66c1fb865480a6aad");

		System.out.println(HttpClientUtil4Sync.doPost(url, map));
	}

}
