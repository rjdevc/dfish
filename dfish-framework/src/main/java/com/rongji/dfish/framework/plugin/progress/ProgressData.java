package com.rongji.dfish.framework.plugin.progress;

import java.io.Serializable;

/**
 * 进度条数据对象
 * @author lamontYu
 * @create 2018-08-03 before
 * @since 3.0
 */
public class ProgressData implements Serializable, Cloneable {
	private static final long serialVersionUID = -727048341094069751L;

	/**
	 * 进度百分百
	 */
	public static final double PERCENT_FULL = 100.0;
	/**
	 * 进度编号
	 */
	private String progressKey;
	/**
	 * 进度显示文本
	 */
	private String progressText;
	/**
	 * 总步骤数,默认只有1个步骤
	 */
	private int steps = 1;
	/**
	 * 步骤比例
	 */
	private double[] stepScales;
	/**
	 * 当前步骤
	 */
	private int stepIndex;
	/**
	 * 当前步骤完成情况
	 */
	private double stepPercent = 0.0;
	/**
	 * 所有步骤总完成情况,单步骤时等于{@link #stepPercent}
	 */
	private double donePercent = 0.0;
	/**
	 * 下次加载时间
	 */
	private long delay = 1000L;
	/**
	 * 是否进度完成,就算进度已经100%,该属性如果还是false,进度条还是不会关闭
	 */
	private boolean finish;
	/**
	 * 完成之后的结果
	 */
	private Serializable complete;
	/**
	 *
	 */
	private Error error;

	public String getProgressKey() {
		return progressKey;
	}

	public void setProgressKey(String progressKey) {
		this.progressKey = progressKey;
	}

	public String getProgressText() {
		return progressText;
	}

	public void setProgressText(String progressText) {
		this.progressText = progressText;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public double[] getStepScales() {
		return stepScales;
	}

	public void setStepScales(double[] stepScales) {
		this.stepScales = stepScales;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public double getStepPercent() {
		return stepPercent;
	}

	public void setStepPercent(double stepPercent) {
		this.stepPercent = stepPercent;
	}

	public double getDonePercent() {
		return donePercent;
	}

	public void setDonePercent(double donePercent) {
		this.donePercent = donePercent;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public Serializable getComplete() {
		return complete;
	}

	public void setComplete(Serializable complete) {
		this.complete = complete;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public Error error() {
		if (this.error == null) {
			error = new Error();
		}
		return error;
	}
	
	public ProgressData setErrorMsg(String errorMsg) {
		error().setMsg(errorMsg);
		return this;
	}

	public ProgressData setErrorCode(String errorCode) {
		error().setCode(errorCode);
		return this;
	}

	static class Error implements Serializable {
		private static final long serialVersionUID = 4655326902090143895L;

		private String code;
		private String msg;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
