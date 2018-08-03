package com.rongji.dfish.framework.plugin.work.service;

public interface DefaultWorkInterface {

	/**
	 * 根据步骤编号取得需要执行的具体东西
	 */
	public  Object getStep(String step);

}
