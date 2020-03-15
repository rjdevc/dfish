package com.rongji.dfish.framework.plugin.progress;

import java.io.Serializable;

/**
 * 进度条数据对象
 * @author lamontYu
 * @date 2018-08-03 before
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

	/**
	 * 进度编号
	 * @return String
	 */
	public String getProgressKey() {
		return progressKey;
	}

	/**
	 * 进度编号
	 * @param progressKey
	 */
	public void setProgressKey(String progressKey) {
		this.progressKey = progressKey;
	}

	/**
	 * 进度显示文本
	 * @return
	 */
	public String getProgressText() {
		return progressText;
	}

	/**
	 * 进度显示文本
	 * @param progressText
	 */
	public void setProgressText(String progressText) {
		this.progressText = progressText;
	}

	/**
	 * 总步骤数,默认只有1个步骤
	 * @return
	 */
	public int getSteps() {
		return steps;
	}

	/**
	 * 总步骤数,默认只有1个步骤
	 * @param steps
	 */
	public void setSteps(int steps) {
		this.steps = steps;
	}

	/**
	 * 步骤比例
	 * @return
	 */
	public double[] getStepScales() {
		return stepScales;
	}

	/**
	 * 步骤比例
	 * @param stepScales
	 */
	public void setStepScales(double[] stepScales) {
		this.stepScales = stepScales;
	}

	/**
	 * 当前步骤
	 * @return
	 */
	public int getStepIndex() {
		return stepIndex;
	}

	/**
	 * 当前步骤
	 * @param stepIndex
	 */
	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	/**
	 * 当前步骤完成情况
	 * @return
	 */
	public double getStepPercent() {
		return stepPercent;
	}

	/**
	 * 当前步骤完成情况
	 * @param stepPercent
	 */
	public void setStepPercent(double stepPercent) {
		this.stepPercent = stepPercent;
	}

	/**
	 * 所有步骤总完成情况,单步骤时等于{@link #stepPercent}
	 * @return
	 */
	public double getDonePercent() {
		return donePercent;
	}

	/**
	 * 所有步骤总完成情况,单步骤时等于{@link #stepPercent}
	 * @param donePercent
	 */
	public void setDonePercent(double donePercent) {
		this.donePercent = donePercent;
	}

	/**
	 * 下次加载时间
	 * @return
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * 下次加载时间
	 * @param delay
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * 是否进度完成,就算进度已经100%,该属性如果还是false,进度条还是不会关闭
	 * @return
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * 是否进度完成,就算进度已经100%,该属性如果还是false,进度条还是不会关闭
	 * @param finish
	 */
	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	/**
	 * 完成之后的结果
	 * @return
	 */
	public Serializable getComplete() {
		return complete;
	}

	/**
	 * 完成之后的结果
	 * @param complete
	 */
	public void setComplete(Serializable complete) {
		this.complete = complete;
	}

	/**
	 * 错误提示
	 * @return
	 */
	public Error getError() {
		return error;
	}

	/**
	 * 错误提示
	 * @param error
	 */
	public void setError(Error error) {
		this.error = error;
	}

	public Error error() {
		if (this.error == null) {
			error = new Error();
		}
		return error;
	}

	/**
	 * 错误信息
	 * @param errorMsg
	 * @return
	 */
	public ProgressData setErrorMsg(String errorMsg) {
		error().setMsg(errorMsg);
		return this;
	}

	/**
	 * 错误代码
	 * @param errorCode
	 * @return
	 */
	public ProgressData setErrorCode(String errorCode) {
		error().setCode(errorCode);
		return this;
	}

	/**
	 * 进度条异常显示的错误提示
	 */
	static class Error implements Serializable {
		private static final long serialVersionUID = 4655326902090143895L;

		private String code;
		private String msg;

		/**
		 * 错误代码
		 * @return
		 */
		public String getCode() {
			return code;
		}

		/**
		 * 错误代码
		 * @param code
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 *错误信息
		 * @return
		 */
		public String getMsg() {
			return msg;
		}

		/**
		 * 错误信息
		 * @param msg
		 */
		public void setMsg(String msg) {
			this.msg = msg;
		}

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
