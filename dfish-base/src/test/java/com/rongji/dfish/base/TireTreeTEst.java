package com.rongji.dfish.base;

import java.util.List;

import org.junit.Test;

import com.rongji.dfish.base.text.TrieTree;

public class TireTreeTEst {
	@Test
	public void testTree(){
		TrieTree<Boolean> t=new TrieTree<Boolean>();
		t.put("陈小平", true);
		t.put("陈中青", true);
		t.put("陈明远", true);
		t.put("陈明平", true);
		t.put("陈超", true);
		t.put("陈伟", true);
		t.put("陈伟达", true);
		t.put("黄俊旸", true);
		
		System.out.println(t);
		String text="榕基软件副总裁，技术总监陈明平同志对此发表了他的看法（编辑：陈超）";
		List<TrieTree.SearchResult<Boolean>> ret= t.search(text);
		for(TrieTree.SearchResult<Boolean> hit:ret){
			System.out.println(hit.getValue()+":"+text.substring(hit.getBegin(),hit.getEnd()));
		}
	}
}
