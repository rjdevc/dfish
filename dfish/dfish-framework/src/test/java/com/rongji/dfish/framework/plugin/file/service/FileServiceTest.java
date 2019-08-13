package com.rongji.dfish.framework.plugin.file.service;

import java.util.List;

import com.rongji.dfish.ui.form.UploadItem;

public class FileServiceTest {

	public static void main(String[] args) {
		String itemJson = "[{\"id\":\"v0oc2POsgVZsoo9CWABs9BMAHUsrBe46vgWy-8MpJ53-ykKUzf3e3w..\",\"name\":\"阿里巴巴Java开发手册(终极版).pdf\",\"size\":1056366}]";
		List<UploadItem> items = FileService.parseUploadItems(itemJson);
		System.out.println(items.size());

		String enFileId = "fFC0LZPBKkAddlfMvrdSVomVNXT_pdOoY4mZhtcj1rL-ykKUzf3e3w..";

		FileService fileService = new FileService();
		System.out.println(fileService.decId(enFileId));
		
	}
	
}
