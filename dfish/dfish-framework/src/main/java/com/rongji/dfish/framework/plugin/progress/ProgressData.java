package com.rongji.dfish.framework.plugin.progress;

import java.io.Serializable;

import com.rongji.dfish.ui.Command;

public class ProgressData implements Serializable {
	private static final long serialVersionUID = -727048341094069751L;
	private String progressKey;
	private double[] stepScales;
	private int stepIndex;
	private double offsetPercent = 0.0;
	private double stepPercent = 0.0;
	
	private boolean finish;
	private String progressText;
	private Command<?> completeCommand;
	
	private double delay;

	public String getProgressKey() {
		return progressKey;
	}

	public void setProgressKey(String progressKey) {
		this.progressKey = progressKey;
	}

	public double getOffsetPercent() {
		return offsetPercent;
	}

	public void setOffsetPercent(double offsetPercent) {
		this.offsetPercent = offsetPercent;
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
	
	public boolean isFinish() {
		return this.finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public String getProgressText() {
		return progressText;
	}

	public void setProgressText(String progressText) {
		this.progressText = progressText;
	}

	public Command<?> getCompleteCommand() {
		return completeCommand;
	}

	public void setCompleteCommand(Command<?> completeCommand) {
		this.completeCommand = completeCommand;
	}
	
	public int getSteps() {
		return this.stepScales.length;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}
	
}
