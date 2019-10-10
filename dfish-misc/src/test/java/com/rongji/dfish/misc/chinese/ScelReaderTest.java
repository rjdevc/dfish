package com.rongji.dfish.misc.chinese;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.rongji.dfish.misc.chinese.ScelReader.ScelFormat;

public class ScelReaderTest {
	@Test
	public void testRead() {
		ScelReader reader=new ScelReader();
		ScelFormat sf=reader.readFile(new File("./src/test/java/com/rongji/dfish/misc/chinese/origin.scel"));
		ScelFormat sf2=reader.readFile(new File("./src/test/java/com/rongji/dfish/misc/chinese/fuzhou.scel"));
		ScelFormat sf3=reader.readFile(new File("./src/test/java/com/rongji/dfish/misc/chinese/food.scel"));
		show(sf);
		show(sf2);
		show(sf3);
//		System.out.println(sf3);
		StringBuilder sb=new StringBuilder();
		for(Map<String,Integer> m:sf3.getWords().values()){
			for(String word:m.keySet()){
				sb.append(word).append(' ');
				if(sb.length()>500){
					System.out.println(sb);
					sb=new StringBuilder();
				}
			}
		}
		
	}

	private void show(ScelFormat sf) {
		SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		System.out.println("===="+sf.getName()+"====");
		System.out.println("分类="+sf.getCatelog());
		System.out.println("备注="+sf.getReadme());
		System.out.println("备注2="+sf.getReadme2());
		System.out.println("发布时间="+SDF.format(sf.getPublishTime()));
	}
}
