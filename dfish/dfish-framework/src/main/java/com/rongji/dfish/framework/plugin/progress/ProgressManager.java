package com.rongji.dfish.framework.plugin.progress;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.LoadingCommand;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Progress;

public class ProgressManager {
	private Cache<String, ProgressData> progressCache;

	public Cache<String, ProgressData> getProgressCache() {
		return progressCache;
	}

	public void setProgressCache(Cache<String, ProgressData> progressCache) {
		this.progressCache = progressCache;
	}

	/**
	 * 加载命令编号
	 */
	public static final String ID_LOADING = "progressLoading";
	/**
	 * 进度百分百
	 */
	private static final Double PERCENT_FULL = 100.0;
	
	/**
	 * 进度条加载时间间隔
	 */
	private Double maxDelay = 3.0;
	/**
	 * 进度条显示文本
	 */
	private String loadingText = "loading..";
	/**
	 * 进度条存在时背景是否置灰禁用
	 */
	private Boolean cover;
	
	public Double getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(Double maxDelay) {
		this.maxDelay = maxDelay;
	}

	public String getLoadingText() {
		return loadingText;
	}

	public void setLoadingText(String loadingText) {
		this.loadingText = loadingText;
	}

	public Boolean getCover() {
		return cover;
	}

	public void setCover(Boolean cover) {
		this.cover = cover;
	}

	/**
	 * 获取进度条数据
	 * @param progressKey
	 * @return
	 */
	public ProgressData getProgressData(String progressKey) {
		return progressCache.get(progressKey);
	}
	
	/**
	 * 将进度条数据放到缓存中
	 * @param progressData
	 * @return
	 * @author YuLM
	 */
	public boolean setProgressData(ProgressData progressData) {
		if (progressData == null || Utils.isEmpty(progressData.getProgressKey())) {
			return false;
		}
		// FIXME 需要做容错处理,进度编号冲突不允许添加
//		if (progressMap.containsKey(progressData.getProgressKey())) {
//			return false;
//		}
		ProgressData old = progressCache.put(progressData.getProgressKey(), progressData);
		return progressData != old;
	}
	
	/**
	 * 设置完成时调用的命令
	 * @param progressKey
	 * @param completeCommand
	 * @return
	 * @author YuLM
	 */
	public boolean setCompleteCommand(String progressKey, Command<?> completeCommand) {
		if (completeCommand == null) {
			return false;
		}
		ProgressData progressData = getProgressData(progressKey);
		progressData.setCompleteCommand(completeCommand);
		return setProgressData(progressData);
	}
	
	private static ExecutorService EXECUTOR_SERVICE;
	
	private int executorSize = 50;
	
	public int getExecutorSize() {
		return executorSize;
	}

	public void setExecutorSize(int executorSize) {
		this.executorSize = executorSize;
	}
	
	private AtomicInteger executingCount = new AtomicInteger(0);
	
	public int getExecutingCount() {
		return executingCount.intValue();
	}

	/**
	 * 注册进度条,业务方法调用
	 * @param runnable
	 * @param progressKey
	 * @return
	 */
	public Command<?> registerProgress(final Runnable runnable, String progressKey) {
		return registerProgress(runnable, progressKey, null);
	}
	
	/**
	 * 注册进度条,业务方法调用
	 * @param runnable
	 * @param progressKey
	 * @param progressText
	 * @return
	 */
	public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText) {
		return registerProgress(runnable, progressKey, progressText, null);
	}
	
	/**
	 * 注册进度条,业务方法调用
	 * @param runnable
	 * @param progressKey
	 * @param progressText
	 * @param completeCommand
	 * @return
	 */
	public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText, Command<?> completeCommand) {
		return registerProgress(runnable, progressKey, progressText, completeCommand, 1);
	}
	/**
	 * 注册进度条,业务方法调用
	 * @param runnable
	 * @param progressKey
	 * @param progressText
	 * @param completeCommand
	 * @return
	 */
	public Command<?> registerProgress(final Runnable runnable, final String progressKey, String progressText, Command<?> completeCommand, Number... stepScale) {
		// 空处理
		if (runnable == null) {
			throw new UnsupportedOperationException("系统进度条创建失败,请联系管理员");
		}
		ProgressData progressData = new ProgressData();
		progressData.setProgressKey(progressKey);
		progressData.setProgressText(progressText);
		progressData.setCompleteCommand(completeCommand);
		stepScale = stepScale == null ? new Number[]{ 1 } : stepScale;
		boolean sucess = setStepScale(progressData, stepScale);
//		boolean sucess = setProgressData(progressData);
		
		if (EXECUTOR_SERVICE == null) {
			EXECUTOR_SERVICE = Executors.newFixedThreadPool(executorSize);
		}
		
		EXECUTOR_SERVICE.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 执行线程数加1
					executingCount.incrementAndGet();
					// 开始执行
					runnable.run();
                } catch (Throwable e) {
                	String message = null;
                	if (e instanceof ProgressException) {
                		message = e.getMessage();
                	} else {
                		FrameworkHelper.LOG.error("进度条运行异常", e);
                		message = "进度条运行异常，请联系管理员";
                	}
                	ProgressData progressData = getProgressData(progressKey);
                	if (progressData != null) { // 进度条运行异常,原命令不应该执行,需要提示用户内部异常
                		progressData.setCompleteCommand(new AlertCommand(message));
                	}
                } finally {
                	// 执行线程数减1
                	executingCount.decrementAndGet();
                	// 强制结束流程进度
                	finishProgress(progressKey);
                }
			}
		});
		
		if (sucess) {
			VerticalLayout shell = new VerticalLayout(null);
			List<Progress> progressGroup = getProgressGroup(progressKey);
			for (Progress progress : progressGroup) {
				shell.add(progress);
			}
			return new LoadingCommand(null).setId(ID_LOADING).setNode(shell).setCover(cover);
		} else {
			FrameworkHelper.LOG.error("添加进度条队列失败@" + progressKey);
			return new AlertCommand("添加进度条队列失败，但后台仍在运行中，请稍后查看执行结果");
		}
	}
	
	private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##");
	
	List<Progress> getProgressGroup(String progressKey) {
		ProgressData progressData = getProgressData(progressKey);
		if (progressData == null) {
			return Collections.emptyList();
		}
		List<Progress> progressGroup = new ArrayList<Progress>();
		
		// 参数需要加密,担心key被猜测出来获取高权限的数据信息
		String src = "progress/reloadProgress?progressKey=" + encryptKey(progressKey);
		Progress stepProgress = new Progress("prgStep", getStepPercent(progressData)).setSrc(src).setText(progressData.getProgressText());
		progressGroup.add(stepProgress);
		
		
		double delay = progressData.getDelay();
		// 进度条数据改变
		boolean dataChange = false;
		if (delay < maxDelay) {
			delay += 0.5; // 每次访问增加0.5秒
			if (delay > maxDelay) {
				delay = maxDelay;
			}
			progressData.setDelay(delay);
			dataChange = true;
		}
		double donePercent = getDonePercent(progressData);
		if (donePercent >= PERCENT_FULL) {
			stepProgress.setPercent(PERCENT_FULL);
			progressData.setFinish(true);
			// 0.2秒之后再刷新,人可以识别到进度条到100%
			delay = 0.2;
			dataChange = true;
		}
		stepProgress.setDelay(delay);
		
		int steps = progressData.getSteps();
		if (steps > 1) {
			String stepText = Utils.isEmpty(stepProgress.getText()) ? this.loadingText : stepProgress.getText();
			int currStep = progressData.getStepIndex() + 1;
			stepProgress.setText("(" + (currStep > steps ? steps : currStep) + "/" + steps + ") " + stepText);
			
			Progress totalProgress = new Progress("prgTotal", donePercent).setSrc(src).setText("总进度");
			progressGroup.add(totalProgress);
		}
		
		if (dataChange) {
			// 将进度条数据放到缓存中
			setProgressData(progressData);
		}
		
		return progressGroup;
	}
	
	/**
	 * 设置进度
	 * @param progressKey
	 * @param stepPercent
	 */
	public void updateStepPercent(String progressKey, double stepPercent) {
		updateStepPercent(progressKey, stepPercent, null);
	}
	
	/**
	 * 设置进度
	 * @param progressKey 进度编号
	 * @param stepPercent 百分比
	 * @param progressText 进度显示文本
	 */
	public void updateStepPercent(String progressKey, double stepPercent, String progressText) {
		ProgressData progressData = getProgressData(progressKey);
		if (progressData != null) {
			if (stepPercent > progressData.getStepPercent()) { // 进度只能向前不能回退,防止业务模块调用出错
				progressData.setStepPercent(stepPercent);
			}
			if (Utils.notEmpty(progressText)) {
				progressData.setProgressText(progressText);
			}
			// 将进度条数据放到缓存中
			setProgressData(progressData);
		}
	}
	
	/**
	 * 增加进度
	 * @param progressKey 进度编号
	 * @param stepPercent 百分比
	 */
	public void addStepPercent(String progressKey, double stepPercent) {
		addStepPercent(progressKey, stepPercent, null);
	}
	
	/**
	 * 增加进度
	 * @param progressKey 进度编号
	 * @param stepPercent 百分比
	 * @param progressText 进度显示文本
	 */
	public void addStepPercent(String progressKey, double stepPercent, String progressText) {
		ProgressData progressData = getProgressData(progressKey);
		if (progressData != null) {
			if (progressData.isFinish()) {
				throw new UnsupportedOperationException("The progress has been finished.");
			}
			
			stepPercent = stepPercent < 0.0 ? 0.0 : stepPercent;
			stepPercent += progressData.getStepPercent();
			if (stepPercent > PERCENT_FULL) {
				stepPercent = PERCENT_FULL;
			}
			// 需要按照比例增加进度
			progressData.setStepPercent(stepPercent);
			if (Utils.notEmpty(progressText)) {
				progressData.setProgressText(progressText);
			}
			// 将进度条数据放到缓存中
			setProgressData(progressData);
		}
	}
	
	/**
	 * 移除进度
	 * @param progressKey 进度编号
	 * @return
	 */
	public ProgressData removeProgress(String progressKey) {
		return progressCache.remove(progressKey);
	}
	
	/**
	 * 结束进度
	 * @param progressKey 进度编号
	 * @return
	 */
	public void finishProgress(String progressKey) {
		ProgressData progressData = getProgressData(progressKey);
		if (progressData != null) {
			progressData.setStepPercent(PERCENT_FULL);
			progressData.setOffsetPercent(PERCENT_FULL);
			progressData.setStepIndex(progressData.getSteps());
			// 将进度条数据放到缓存中
			setProgressData(progressData);
		}
	}
	
	/**
	 * 设置步骤比例
	 * @param progressKey 进度编号
	 * @param stepScale 步骤比例组
	 */
	public boolean setStepScale(String progressKey, Number... stepScale) {
		ProgressData progressData = getProgressData(progressKey);
		return setStepScale(progressData, stepScale);
	}
	
	/**
	 * 设置步骤数
	 * @param progressKey
	 * @param steps
	 */
	public boolean setSteps(String progressKey, int steps) {
		if (steps < 1) {
			throw new UnsupportedOperationException("The steps must be greater than one.");
		}
		Number[] stepScale = new Number[steps];
		for (int i = 0; i < steps; i++) {
			stepScale[i] = 1;
		}
		ProgressData progressData = getProgressData(progressKey);
		if (progressData != null) {
			return setStepScale(progressData, stepScale);
		}
		return false;
	}
	
	/**
	 * 设置步骤比例
	 * @param progressData 进度编号
	 * @param stepScale 步骤比例组
	 */
	private boolean setStepScale(ProgressData progressData, Number... stepScale) {
		if (progressData == null) {
			throw new UnsupportedOperationException("The progressData is illegal.");
		}
		if (progressData.getStepIndex() > 0) {
			throw new UnsupportedOperationException("The progress has began, can not reset the stepScale.");
		}
		if (Utils.isEmpty(stepScale)) {
			stepScale = new Number[]{ 1 };
		} else {
			for (int i = 0; i < stepScale.length; i++) {
				if (stepScale[i] == null) {
					stepScale[i] = 1;
				}
			}
		}
		
		double total = 0.0;
		for (Number num : stepScale) {
			if (num != null) {
				total += num.doubleValue();
			}
		}
		if (total <= 0.0) {
			throw new UnsupportedOperationException("The sum of the scales must be greator than the zero.");
		}
		double[] stepScales = new double[stepScale.length];
		for (int i = 0; i < stepScales.length; i++) {
			Number num = stepScale[i];
			double value = num == null ? 0.0 : num.doubleValue();
			stepScales[i] = value / total;
		}
		progressData.setStepScales(stepScales);
		// 将进度条数据放到缓存中
		return setProgressData(progressData);
	}
	
	/**
	 * 进入下一步
	 * @param progressKey 进度编号
	 * @return boolean 是否还有下一步
	 */
	public boolean nextStep(String progressKey) {
		ProgressData progressData = getProgressData(progressKey);
		if (progressData != null) {
			if (progressData.isFinish()) {
				throw new UnsupportedOperationException("The progress has been finished.");
			}
			if (progressData.getStepIndex() >= progressData.getStepScales().length) {
				// 当前步骤已经走完
				progressData.setOffsetPercent(PERCENT_FULL);
				return false;
			}
			// 总偏移量增加
			double offsetPercent = progressData.getOffsetPercent() + progressData.getStepScales()[progressData.getStepIndex()] * PERCENT_FULL;
			progressData.setOffsetPercent(offsetPercent);
			progressData.setStepPercent(0.0);
			progressData.setStepIndex(progressData.getStepIndex() + 1);
			// 已达到最后一步,设置为结束
//			boolean hasNext = progressData.getStepIndex() < progressData.getSteps() && offsetPercent < PERCENT_FULL;
			// 暂且不设置结束,等界面刷新到100%再结束
//			progressData.setFinish(finish);
			// 将进度条数据放到缓存中
			setProgressData(progressData);
			return hasNext(progressData);
		}
		return false;
	}
	
	/**
	 * 是否还有下一步
	 * @param progressKey
	 * @return
	 */
	public boolean hasNext(String progressKey) {
		ProgressData progressData = getProgressData(progressKey);
		return hasNext(progressData);
	}
	
	/**
	 * 是否还有下一步
	 * @param progressData
	 * @return
	 */
	private boolean hasNext(ProgressData progressData) {
		return progressData != null && progressData.getStepIndex() < progressData.getSteps() && progressData.getOffsetPercent() < PERCENT_FULL;
	}
	
	/**
	 * 获取当前步骤完成进度
	 * @param progressData
	 * @return double
	 */
	public double getStepPercent(ProgressData progressData) {
		double donePercent = 0.0;
		if (progressData != null) {
			donePercent = progressData.getStepPercent();
			if (donePercent >= PERCENT_FULL) {
				donePercent = PERCENT_FULL;
			} else {
				try {
					donePercent = Double.parseDouble(NUMBER_FORMAT.format(donePercent));
				} catch (Exception e) {
				}
			}
		}
		return donePercent;
	}
	
	/**
	 * 获取总完成进度
	 * @return double
	 */
	public double getDonePercent(ProgressData progressData) {
		double donePercent = 0.0;
		// FIXME 获取不到进度情况该如何容错处理
		if (progressData != null) {
			donePercent = progressData.getOffsetPercent();
			if (donePercent >= PERCENT_FULL) {
				donePercent = PERCENT_FULL;
			} else {
				if (progressData.getStepIndex() < progressData.getSteps()) {
					donePercent += progressData.getStepScales()[progressData.getStepIndex()] * progressData.getStepPercent();
					try {
						donePercent = Double.parseDouble(NUMBER_FORMAT.format(donePercent));
					} catch (Exception e) {
					}
				} else {
					donePercent = PERCENT_FULL;
				}
			}
		}
		return donePercent;
	}
	
//	/**
//	 * 设置当前进度比例
//	 * @param progressKey
//	 * @param progressScale
//	 */
//	public void setProgressScale(String progressKey, Double progressScale) {
//		ProgressData progressData = getProgressData(progressKey);
//		if (progressData != null) {
//			progressData.setProgressScale(progressScale);
//		}
//	}
	
	private StringCryptor getCryptor() {
		String algorithms = CryptFactory.BLOWFISH;
		String secretKey = "DFISH";
//		String encoding = "UTF-8";
		int presentStyle = CryptFactory.BASE32;
		StringCryptor sc = CryptFactory.getStringCryptor(algorithms, StringCryptor.UTF8 , presentStyle, secretKey);// 密钥
		return sc;
	}
	
	/**
	 * 加密
	 * @param progressKey String
	 * @return String
	 */
	public String encryptKey(String progressKey) {
		if (Utils.notEmpty(progressKey)) {
			return getCryptor().encrypt(progressKey);
		}
		return progressKey;
	}
	
	/**
	 * 解密
	 * @param progressKey String
	 * @return String
	 */
	public String decryptKey(String progressKey) {
		if (Utils.notEmpty(progressKey)) {
			return getCryptor().decrypt(progressKey);
		}
		return progressKey;
	}

}
