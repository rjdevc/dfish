package com.rongji.dfish.ui;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.rongji.dfish.ui.json.J;

public abstract class DFishUITestCase {

	// 已输出次数
	private AtomicInteger outputedCount = new AtomicInteger(0);
	/**
	 * 一般一个测试用例必须包含一个典型的当前对象的JSON输出，以供人工审查是否正确
	 */
	@Test
	public void testJson(){
		output(getWidget());
	}
	/**
	 * 构建默认对象
	 * @return
	 */
	protected abstract Object getWidget();
	protected void output(Object obj) {
		String call = "";
		try {
			// 这里的调用关系写法是否不规范,若不规范可进行调整
			StackTraceElement callStack = Thread.currentThread().getStackTrace()[2];
			// 取上级调用方法名
			call = callStack.getClassName() + "." + callStack.getMethodName();
        } catch (Throwable e) {
        	e.printStackTrace();
        }
		
		String outputString = "";
		if (obj != null) {
			if (obj instanceof JsonObject) {
				outputString = J.formatJson(J.toJson(obj));
			} else {
				outputString = obj.toString();
			}
		}
		
		System.out.println("==========↓" + call + "↓==========output@" + outputedCount.incrementAndGet() + "\r\n" + outputString);
	}
	
}
