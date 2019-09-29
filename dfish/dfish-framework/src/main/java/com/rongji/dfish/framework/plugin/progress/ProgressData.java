package com.rongji.dfish.framework.plugin.progress;

import java.io.Serializable;

public class ProgressData implements Serializable {
	private static final long serialVersionUID = -727048341094069751L;

	/**
	 * 进度百分百
	 */
	public static final double PERCENT_FULL = 100.0;

	private String progressKey;
	private String progressText;

	private double[] stepScales;
	private int stepIndex;
	private double offsetPercent = 0.0;
	private double stepPercent = 0.0;
	private double donePercent = 0.0;

	private double delay = 0.2;
	private boolean finish;
	private Serializable completeResult;
	private Error error;

	public String getProgressKey() {
		return progressKey;
	}

	public ProgressData setProgressKey(String progressKey) {
		this.progressKey = progressKey;
		return this;
	}

	public String getProgressText() {
		return progressText;
	}

	public ProgressData setProgressText(String progressText) {
		this.progressText = progressText;
		return this;
	}

	public double[] getStepScales() {
		return stepScales;
	}

	public ProgressData setStepScales(double[] stepScales) {
		this.stepScales = stepScales;
		return this;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public ProgressData setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
		return this;
	}

	public double getOffsetPercent() {
		return offsetPercent;
	}

	public ProgressData setOffsetPercent(double offsetPercent) {
		this.offsetPercent = offsetPercent;
		// FIXME 本来这个代码应该在外面,这个类只做纯数据存储,现在先放在这里代码调用更简便,下同
		if (this.offsetPercent > PERCENT_FULL) {
			this.offsetPercent = PERCENT_FULL;
		}
		return recalculate();
	}

	public double getStepPercent() {
		return stepPercent;
	}

	public ProgressData setStepPercent(double stepPercent) {
		this.stepPercent = stepPercent;
		if (this.stepPercent > PERCENT_FULL) {
			this.stepPercent = PERCENT_FULL;
		}
		return recalculate();
	}

	private ProgressData recalculate() {
		if (donePercent >= PERCENT_FULL) {
			donePercent = PERCENT_FULL;
		} else {
			if (stepIndex < getSteps()) {
				donePercent += stepScales[stepIndex] * stepPercent;
				if (donePercent > PERCENT_FULL) {
					donePercent = PERCENT_FULL;
				}
			} else {
				donePercent = PERCENT_FULL;
			}
		}
		return this;
	}

	public double getDonePercent() {
		return donePercent;
	}

	public double getDelay() {
		return delay;
	}

	public ProgressData setDelay(double delay) {
		this.delay = delay;
		return this;
	}
	
	public boolean isFinish() {
		return this.finish;
	}

	public ProgressData setFinish(boolean finish) {
		this.finish = finish;
		return this;
	}

	public Serializable getCompleteResult() {
		return completeResult;
	}

	public ProgressData setCompleteResult(Serializable completeResult) {
		this.completeResult = completeResult;
		return this;
	}

	public int getSteps() {
		return this.stepScales.length;
	}

	public Error getError() {
		return error;
	}

	public ProgressData setError(Error error) {
		this.error = error;
		return this;
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
	
}
