package com.rongji.dfish.misc.origin;

import org.junit.Test;

import com.rongji.dfish.misc.origin.OriginMatcher.MatchResult;

public class OriginMatcherTest {
	@Test
	public void  testLegacyMode(){
		OriginMatcher om=OriginMatcher.getInstance();
		MatchResult mr=om.match("福建鼓楼");
		System.out.println(mr.getCode()+"\t"+mr.getConfidence());
	}
	
	@Test
	public void  testNewMode(){
		OriginMatcher om=OriginMatcher.createInstance("3501");
		MatchResult mr=om.match("小沧");
		System.out.println(mr.getCode()+"\t"+mr.getConfidence());
		 mr=om.match("小沧畲族乡");
		System.out.println(mr.getCode()+"\t"+mr.getConfidence());
		mr=om.match("梅雄");
		System.out.println(mr.getCode()+"\t"+mr.getConfidence());
		mr=om.match("台江万象");
		System.out.println(mr.getCode()+"\t"+mr.getConfidence());
	}
}
