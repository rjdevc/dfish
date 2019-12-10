package com.rongji.dfish.framework.plugin.file.service;

import java.util.List;

import com.rongji.dfish.framework.plugin.file.service.impl.FileServiceImpl;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;

public class FileServiceTest {

	public static void main(String[] args) {
		FileService fileService = new FileServiceImpl();

		String itemJson = "[{\"id\":\"v0oc2POsgVZsoo9CWABs9BMAHUsrBe46vgWy-8MpJ53-ykKUzf3e3w..\",\"name\":\"阿里巴巴Java开发手册(终极版).pdf\",\"size\":1056366}]";
		List<UploadItem> items = fileService.parseUploadItems(itemJson);
		System.out.println(items.size());

		String enFileId = "rV3URGH9fc6XT1tVI2bMUImVNXT_pdOo1uVnBN2oSiP-ykKUzf3e3w..";
		System.out.println(fileService.decrypt(enFileId));
		
	}

}
