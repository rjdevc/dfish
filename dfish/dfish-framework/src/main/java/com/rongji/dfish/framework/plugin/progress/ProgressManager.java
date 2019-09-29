package com.rongji.dfish.framework.plugin.progress;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.JsonNode;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.LoadingCommand;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Progress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgressManager {
    private Cache<String, ProgressData> progressCache;

    private CryptProvider cryptProvider;

    public Cache<String, ProgressData> getProgressCache() {
        return progressCache;
    }

    public void setProgressCache(Cache<String, ProgressData> progressCache) {
        this.progressCache = progressCache;
    }

    public CryptProvider getCryptProvider() {
        return cryptProvider;
    }

    public void setCryptProvider(CryptProvider cryptProvider) {
        this.cryptProvider = cryptProvider;
    }

    /**
     * 加载命令编号
     */
    public static final String ID_LOADING = "progressLoading";

    /**
     * 进度条加载时间间隔
     */
    private double maxDelay = 3.0;
    /**
     * 进度条显示文本
     */
    private String loadingText = "loading..";
    /**
     * 进度条存在时背景是否置灰禁用
     */
    private Boolean cover;

    public double getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(double maxDelay) {
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
     *
     * @param progressKey
     * @return
     */
    private ProgressData getProgressData(String progressKey) {
        return progressCache.get(progressKey);
    }

    public ProgressData reloadProgressData(String progressKey) {
        ProgressData progressData = getProgressData(progressKey);
        double nexDelay = progressData.getDelay();
        nexDelay *= 1.5; // 每次访问时间是上次的1.5倍
        if (nexDelay > maxDelay) { // 最长延时
            nexDelay = maxDelay;
        }
        progressData.setDelay(nexDelay);
        setProgressData(progressData);
        return progressData;
    }

    /**
     * 将进度条数据放到缓存中
     *
     * @param progressData
     * @return
     */
    public boolean setProgressData(ProgressData progressData) {
        if (progressData == null || Utils.isEmpty(progressData.getProgressKey())) {
            return false;
        }
        ProgressData old = progressCache.put(progressData.getProgressKey(), progressData);
        return progressData != old;
    }

    /**
     * 设置完成时调用的动作
     *
     * @param progressKey
     * @param completeNode
     * @return boolean
     */
    public boolean setCompleteNode(String progressKey, JsonNode completeNode) {
        ProgressData progressData = getProgressData(progressKey);
        if (progressData != null) {
            progressData.setCompleteResult(completeNode);
        }
        return setProgressData(progressData);
    }

    /**
     * 设置完成时调用的命令
     *
     * @param progressKey
     * @param completeCommand
     * @return boolean
     * @see #setCompleteNode(String, JsonNode)
     */
    @Deprecated
    public boolean setCompleteCommand(String progressKey, Command<?> completeCommand) {
        return setCompleteNode(progressKey, completeCommand);
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
     *
     * @param runnable
     * @param progressKey
     * @return
     */
    public Command<?> registerProgress(final Runnable runnable, String progressKey) {
        return registerProgress(runnable, progressKey, null);
    }

    /**
     * 注册进度条,业务方法调用
     *
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
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param completeNode
     * @return
     */
    public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText, JsonNode completeNode) {
        return registerProgress(runnable, progressKey, progressText, completeNode, 1);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param steps
     * @return
     */
    public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText, int steps) {
        return registerProgress(runnable, progressKey, progressText, null, 1);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param completeNode
     * @param steps
     * @return
     */
    public Command<?> registerProgress(final Runnable runnable, final String progressKey, String progressText, JsonNode completeNode, int steps) {
        return registerProgress(runnable, progressKey, progressText, completeNode, getStepScales(steps));
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param completeNode
     * @param stepScale
     * @return
     */
    public Command<?> registerProgress(final Runnable runnable, final String progressKey, String progressText, JsonNode completeNode, Number[] stepScale) {
        ProgressData progressData = register(runnable, progressKey, progressText, completeNode, stepScale);
        VerticalLayout progressGroup = getProgressGroup(progressData);
        if (progressGroup != null) {
            return new LoadingCommand(null).setId(ID_LOADING).setNode(progressGroup).setCover(cover);
        } else {
            String errorMsg = "添加进度条队列失败@" + System.currentTimeMillis();
            FrameworkHelper.LOG.warn(errorMsg + "[" + progressKey + "]");
            return new AlertCommand(errorMsg);
        }
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @return
     */
    public ProgressData register(final Runnable runnable, final String progressKey) {
        return register(runnable, progressKey, null);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @return
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText) {
        return register(runnable, progressKey, progressText, null, 1);
    }


    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param steps
     * @return
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, int steps) {
        return register(runnable, progressKey, progressText, null, steps);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param completeResult
     * @return
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, Serializable completeResult) {
        return register(runnable, progressKey, progressText, completeResult, 1);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param completeResult
     * @param steps
     * @return
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, Serializable completeResult, int steps) {
        return register(runnable, progressKey, progressText, completeResult, getStepScales(steps));
    }

    private Number[] getStepScales(int steps) {
        if (steps < 1) {
            throw new UnsupportedOperationException("The steps must greater than one.");
        }
        Number[] stepScales = new Number[steps];
        for (int i = 0; i < steps; i++) {
            stepScales[i] = 1;
        }
        return stepScales;
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable
     * @param progressKey
     * @param progressText
     * @param completeResult
     * @param stepScale
     * @return
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, Serializable completeResult, Number[] stepScale) {
        // 空处理
        if (runnable == null) {
            throw new UnsupportedOperationException("The progress register failed, the Runnable can not be null.");
        }
        ProgressData progressData = getProgressData(progressKey);
        if (progressData == null) {
            progressData = new ProgressData();
            progressData.setProgressKey(progressKey);
            progressData.setProgressText(progressText);
            progressData.setCompleteResult(completeResult);
            stepScale = stepScale == null ? new Number[]{1} : stepScale;
            boolean success = resetStepScale(progressData, stepScale);
            if (success) {
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
                            String errorMsg = null;
                            String errorCode = null;
                            if (e instanceof ProgressException) {
                                errorMsg = e.getMessage();
                                errorCode = ((ProgressException) e).getCode();
                            } else {
                                errorMsg = "进度条运行异常@" + System.currentTimeMillis();
                                FrameworkHelper.LOG.error(errorMsg, e);
                            }
                            ProgressData progressData = getProgressData(progressKey);
                            if (progressData != null) {
                                // 进度条运行异常,异常信息展示
                                progressData.setErrorCode(errorCode);
                                progressData.setErrorMsg(errorMsg);
                                // 异常数据提示需要放到缓存中
                                setProgressData(progressData);
                            }
                        } finally {
                            // 执行线程数减1
                            executingCount.decrementAndGet();
                            // 强制结束流程进度
                            finishProgress(progressKey);
                        }
                    }
                });
            }
        }
        return progressData;
    }

//	private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##");

    VerticalLayout getProgressGroup(ProgressData progressData) {
        if (progressData == null) {
            return null;
        }
        VerticalLayout progressGroup = new VerticalLayout(null);

        // 参数需要加密,担心key被猜测出来获取高权限的数据信息
        String src = "progress/reloadProgress?progressKey=" + encrypt(progressData.getProgressKey());
        Progress stepProgress = new Progress("prgStep", progressData.getStepPercent()).setSrc(src).setText(progressData.getProgressText());
        progressGroup.add(stepProgress);
        stepProgress.setDelay(progressData.getDelay());

        int steps = progressData.getSteps();
        if (steps > 1) {
            String stepText = Utils.isEmpty(stepProgress.getText()) ? this.loadingText : stepProgress.getText();
            int currStep = progressData.getStepIndex() + 1;
            stepProgress.setText("(" + (currStep > steps ? steps : currStep) + "/" + steps + ") " + stepText);

            Progress doneProgress = new Progress("prgDone", progressData.getDonePercent()).setSrc(src).setText("总进度");
            progressGroup.add(doneProgress);
        }
        return progressGroup;
    }

//    /**
//     * 设置进度
//     *
//     * @param progressKey
//     * @param stepPercent
//     */
//    public void updateStepPercent(String progressKey, double stepPercent) {
//        updateStepPercent(progressKey, stepPercent, null);
//    }
//
//    /**
//     * 设置进度
//     *
//     * @param progressKey  进度编号
//     * @param stepPercent  百分比
//     * @param progressText 进度显示文本
//     */
//    public void updateStepPercent(String progressKey, double stepPercent, String progressText) {
//        ProgressData progressData = getProgressData(progressKey);
//        if (progressData != null) {
//            if (stepPercent > progressData.getStepPercent()) { // 进度只能向前不能回退,防止业务模块调用出错
//                progressData.setStepPercent(stepPercent);
//            }
//            if (Utils.notEmpty(progressText)) {
//                progressData.setProgressText(progressText);
//            }
//            // 将进度条数据放到缓存中
//            setProgressData(progressData);
//        }
//    }

    /**
     * 增加进度
     *
     * @param progressKey 进度编号
     * @param stepPercent 百分比
     */
    public void addStepPercent(String progressKey, Number stepPercent) {
        addStepPercent(progressKey, stepPercent, null);
    }

    /**
     * 增加进度
     *
     * @param progressKey  进度编号
     * @param stepPercent  百分比
     * @param progressText 进度显示文本,为null时不改变文本
     */
    public void addStepPercent(String progressKey, Number stepPercent, String progressText) {
        if (stepPercent == null || stepPercent.doubleValue() <= 0) {
            return;
        }
        ProgressData progressData = getProgressData(progressKey);
        if (progressData == null || progressData.isFinish()) {
            return;
        }
        double nextStepPercent = stepPercent.doubleValue();
        nextStepPercent += progressData.getStepPercent();
        // 需要按照比例增加进度
        progressData.setStepPercent(nextStepPercent);
        if (progressText != null) {
            progressData.setProgressText(progressText);
        }
        // 将进度条数据放到缓存中
        setProgressData(progressData);
    }

    /**
     * 移除进度
     *
     * @param progressKey 进度编号
     * @return
     */
    public ProgressData removeProgress(String progressKey) {
        return progressCache.remove(progressKey);
    }

    /**
     * 结束进度
     *
     * @param progressKey 进度编号
     * @return
     */
    public void finishProgress(String progressKey) {
        ProgressData progressData = getProgressData(progressKey);
        if (progressData != null) {
            progressData.setFinish(true);
            progressData.setStepIndex(progressData.getSteps());
            progressData.setStepPercent(ProgressData.PERCENT_FULL);
            progressData.setOffsetPercent(ProgressData.PERCENT_FULL);
            // 将进度条数据放到缓存中
            setProgressData(progressData);
        }
    }

    /**
     * 设置步骤比例
     *
     * @param progressKey 进度编号
     * @param stepScale   步骤比例组
     */
    public boolean resetStepScale(String progressKey, Number... stepScale) {
        ProgressData progressData = getProgressData(progressKey);
        return resetStepScale(progressData, stepScale);
    }

    /**
     * 设置步骤比例
     *
     * @param progressData 进度编号
     * @param stepScale    步骤比例组
     * @return boolean 是否设置成功
     */
    private boolean resetStepScale(ProgressData progressData, Number... stepScale) {
        if (progressData == null) {
            return false;
        }
        if (progressData.getDonePercent() > 0.0) {
            throw new UnsupportedOperationException("The progress has began, can not reset the stepScale.");
        }
        if (Utils.isEmpty(stepScale)) {
            stepScale = new Number[]{1};
        }

        double total = 0.0;
        for (Number num : stepScale) {
            if (num != null) {
                total += num.doubleValue();
            }
        }
        if (total <= 0.0) {
            throw new UnsupportedOperationException("The sum of the scales must be greater than the zero.");
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
     * 设置步骤数
     *
     * @param progressKey
     * @param steps
     */
    public boolean resetSteps(String progressKey, int steps) {
        Number[] stepScale = getStepScales(steps);
        ProgressData progressData = getProgressData(progressKey);
        return resetStepScale(progressData, stepScale);
    }


    /**
     * 进入下一步
     *
     * @param progressKey 进度编号
     * @return boolean 是否还有下一步
     */
    public boolean nextStep(String progressKey) {
        ProgressData progressData = getProgressData(progressKey);
        if (progressData == null || progressData.isFinish()) {
            return false;
        }
        if (progressData.getStepIndex() >= progressData.getSteps()) {
            return false;
        }
        // 总偏移量增加
        double offsetPercent = progressData.getOffsetPercent() + progressData.getStepScales()[progressData.getStepIndex()] * ProgressData.PERCENT_FULL;
        progressData.setOffsetPercent(offsetPercent);
        progressData.setStepPercent(0.0);
        progressData.setStepIndex(progressData.getStepIndex() + 1);
        // 已达到最后一步,暂且不设置结束,等界面刷新到100%再结束
        // 将进度条数据放到缓存中
        setProgressData(progressData);
        return hasNext(progressData);
    }

    /**
     * 是否还有下一步
     *
     * @param progressKey
     * @return
     */
    public boolean hasNext(String progressKey) {
        ProgressData progressData = getProgressData(progressKey);
        return hasNext(progressData);
    }

    /**
     * 是否还有下一步
     *
     * @param progressData
     * @return
     */
    private boolean hasNext(ProgressData progressData) {
        return progressData != null && !progressData.isFinish() && progressData.getStepIndex() < progressData.getSteps();
    }

    private StringCryptor getCryptor() {
        if (cryptProvider == null) {
            cryptProvider = new CryptProvider();
        }
        return cryptProvider.getCryptor();
    }

    /**
     * 加密
     *
     * @param progressKey String
     * @return String
     */
    public String encrypt(String progressKey) {
        if (Utils.notEmpty(progressKey)) {
            return getCryptor().encrypt(progressKey);
        }
        return progressKey;
    }

    /**
     * 解密
     *
     * @param progressKey String
     * @return String
     */
    public String decrypt(String progressKey) {
        if (Utils.notEmpty(progressKey)) {
            return getCryptor().decrypt(progressKey);
        }
        return progressKey;
    }

}