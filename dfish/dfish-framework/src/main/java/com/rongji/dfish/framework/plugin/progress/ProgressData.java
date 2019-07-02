package com.rongji.dfish.framework.plugin.progress;

import com.rongji.dfish.ui.JsonNode;

import java.io.Serializable;

public class ProgressData implements Serializable {
	private static final long serialVersionUID = -727048341094069751L;
	private String progressKey;
	private double[] stepScales;
	private int stepIndex;
	private double offsetPercent = 0.0;
	private double stepPercent = 0.0;
	
	private boolean finish;
	private String progressText;
	private JsonNode completeNode;
	private double delay;

	public String getProgressKey() {
		return progressKey;
	}

	public ProgressData setProgressKey(String progressKey) {
		this.progressKey = progressKey;
		return this;
	}

	public double getOffsetPercent() {
		return offsetPercent;
	}

	public ProgressData setOffsetPercent(double offsetPercent) {
		this.offsetPercent = offsetPercent;
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

	public double getStepPercent() {
		return stepPercent;
	}

	public ProgressData setStepPercent(double stepPercent) {
		this.stepPercent = stepPercent;
		return this;
	}
	
	public boolean isFinish() {
		return this.finish;
	}

	public ProgressData setFinish(boolean finish) {
		this.finish = finish;
		return this;
	}

	public String getProgressText() {
		return progressText;
	}

	public ProgressData setProgressText(String progressText) {
		this.progressText = progressText;
		return this;
	}

	public JsonNode getCompleteNode() {
		return completeNode;
	}

	public ProgressData setCompleteNode(JsonNode completeNode) {
		this.completeNode = completeNode;
		return this;
	}

	public double getDelay() {
		return delay;
	}

	public ProgressData setDelay(double delay) {
		this.delay = delay;
		return this;
	}

	public int getSteps() {
		return this.stepScales.length;
	}
	
}
