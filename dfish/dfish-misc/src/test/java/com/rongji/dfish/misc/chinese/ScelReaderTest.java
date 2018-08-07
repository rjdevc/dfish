package com.rongji.dfish.misc.chinese;

import java.io.File;

import org.junit.Test;

import com.rongji.dfish.misc.chinese.ScelReader.ScelFormat;

public class ScelReaderTest {
	@Test
	public void testRead() {
		ScelReader reader=new ScelReader();
		ScelFormat sf=reader.readFile(new File("./src/test/java/com/rongji/dfish/misc/chinese/fuzhou.scel"));
		System.out.println(sf);
	}
}
