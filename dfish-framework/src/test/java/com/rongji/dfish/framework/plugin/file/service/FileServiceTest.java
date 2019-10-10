package com.rongji.dfish.framework.plugin.file.service;

import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.form.UploadItem;

public class FileServiceTest {

	public static void main(String[] args) {
		String itemJson = "[{\"id\":\"v0oc2POsgVZsoo9CWABs9BMAHUsrBe46vgWy-8MpJ53-ykKUzf3e3w..\",\"name\":\"阿里巴巴Java开发手册(终极版).pdf\",\"size\":1056366}]";
		List<UploadItem> items = FileService.parseUploadItems(itemJson);
		System.out.println(items.size());

		String enFileId = "rV3URGH9fc6XT1tVI2bMUImVNXT_pdOo1uVnBN2oSiP-ykKUzf3e3w..";

		System.out.println(decId(enFileId));
		
	}

	protected static final String SECRET_KEY = "DFISH";

	protected static StringCryptor CRY = CryptFactory.getStringCryptor(
			CryptFactory.BLOWFISH, CryptFactory.UTF8,
			CryptFactory.URL_SAFE_BASE64, SECRET_KEY);

	/**
	 * 加密文件编号
	 *
	 * @param id 文件编号
	 * @return 加密的文件编号
	 */
	public static String encId(String id) {
		if (Utils.isEmpty(id)) {
			return id;
		}
		return CRY.encrypt(id);
	}

	/**
	 * 解密编号
	 *
	 * @param encId 加密的编号
	 * @return 编号
	 */
	public static String decId(String encId) {
		if (Utils.isEmpty(encId)) {
			return encId;
		}
		try {
			return CRY.decrypt(encId);
		} catch (Exception e) {
			FrameworkHelper.LOG.error("解密编号出错", e);
			return null;
		}
	}
	
}
