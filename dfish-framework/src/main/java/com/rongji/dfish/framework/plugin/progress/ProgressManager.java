package com.rongji.dfish.framework.plugin.progress;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.exception.Marked;
import com.rongji.dfish.base.exception.MarkedRuntimeException;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.ThreadUtil;
import com.rongji.dfish.framework.config.CryptorFactoryBean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 进度条数据管理器
 *
 * @author lamontYu
 * @since DFish3.0
 */
public class ProgressManager {
    private Cache<String, ProgressData> progressCache = new MemoryCache<>();
    private Cryptor cryptor;

    /**
     * 进度条缓存
     *
     * @return 进度条缓存数据
     */
    public Cache<String, ProgressData> getProgressCache() {
        return progressCache;
    }

    /**
     * 进度条缓存
     *
     * @param progressCache 进度条缓存数据
     */
    public void setProgressCache(Cache<String, ProgressData> progressCache) {
        this.progressCache = progressCache;
    }

    /**
     * 加解密工具
     *
     * @return 加解密工具
     */
    public Cryptor getCryptor() {
        return cryptor;
    }

    /**
     * 加解密工具
     *
     * @param cryptor 加解密工具
     */
    public void setCryptor(Cryptor cryptor) {
        this.cryptor = cryptor;
    }

    /**
     * 加载命令编号
     */
    public static final String ID_LOADING = "progressLoading";

    /**
     * 进度条加载时间间隔,单位:毫秒
     */
    private long maxDelay = 5000L;

    public long getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(long maxDelay) {
        this.maxDelay = maxDelay;
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
        if (progressData != null) {
            long nextDelay = progressData.getDelay();
            if (nextDelay < maxDelay) {
                // 下次访问时间是上次的1.5倍
                nextDelay += nextDelay >> 1;
                if (nextDelay > maxDelay) {
                    nextDelay = maxDelay;
                }
            } else if (nextDelay > maxDelay) {
                nextDelay = maxDelay;
            }

            progressData.setDelay(nextDelay);
            setProgressData(progressData);
        }

        return getResponseProgressData(progressData);
    }

    private ProgressData getResponseProgressData(ProgressData progressData) {
        if (progressData == null) {
            return null;
        }
        ProgressData responseData = null;
        try {
            responseData = (ProgressData) progressData.clone();
            // 返回的结果编号需要加密
            responseData.setProgressKey(encrypt(responseData.getProgressKey()));
            // 进度比例无需显示
            responseData.setStepScales(null);
        } catch (Exception e) {
            String errorMsg = "进度数据异常@" + System.currentTimeMillis();
            // 正常情况一般不会报错
            LogUtil.error(errorMsg, e);
            throw new MarkedRuntimeException(errorMsg);
        }
        return responseData;
    }

    /**
     * 将进度条数据放到缓存中
     *
     * @param progressData 进度数据
     * @return 是否新缓存
     */
    public boolean setProgressData(ProgressData progressData) {
        if (progressData == null || Utils.isEmpty(progressData.getProgressKey())) {
            return false;
        }
        ProgressData old = progressCache.put(progressData.getProgressKey(), progressData);
        return progressData != old;
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

//    /**
//     * 设置完成时调用的动作
//     *
//     * @param progressKey
//     * @param completeNode
//     * @return boolean
//     */
//    public boolean setCompleteNode(String progressKey, JsonNode completeNode) {
//        ProgressData progressData = getProgressData(progressKey);
//        if (progressData != null) {
//            progressData.setComplete(completeNode);
//        }
//        return setProgressData(progressData);
//    }
//
//    /**
//     * 设置完成时调用的命令
//     *
//     * @param progressKey
//     * @param completeCommand
//     * @return boolean
//     * @see #setCompleteNode(String, JsonNode)
//     */
//    @Deprecated
//    public boolean setCompleteCommand(String progressKey, Command<?> completeCommand) {
//        return setCompleteNode(progressKey, completeCommand);
//    }
//
//    /**
//     * 注册进度条,业务方法调用
//     *
//     * @param runnable
//     * @param progressKey
//     * @return
//     */
//    public Command<?> registerProgress(final Runnable runnable, String progressKey) {
//        return registerProgress(runnable, progressKey, null);
//    }
//
//    /**
//     * 注册进度条,业务方法调用
//     *
//     * @param runnable
//     * @param progressKey
//     * @param progressText
//     * @return
//     */
//    public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText) {
//        return registerProgress(runnable, progressKey, progressText, null);
//    }
//
//    /**
//     * 注册进度条,业务方法调用
//     *
//     * @param runnable
//     * @param progressKey
//     * @param progressText
//     * @param completeNode
//     * @return
//     */
//    public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText, JsonNode completeNode) {
//        return registerProgress(runnable, progressKey, progressText, completeNode, 1);
//    }
//
//    /**
//     * 注册进度条,业务方法调用
//     *
//     * @param runnable
//     * @param progressKey
//     * @param progressText
//     * @param steps
//     * @return
//     */
//    public Command<?> registerProgress(final Runnable runnable, String progressKey, String progressText, int steps) {
//        return registerProgress(runnable, progressKey, progressText, null, 1);
//    }
//
//    /**
//     * 注册进度条,业务方法调用
//     *
//     * @param runnable
//     * @param progressKey
//     * @param progressText
//     * @param completeNode
//     * @param steps
//     * @return
//     */
//    public Command<?> registerProgress(final Runnable runnable, final String progressKey, String progressText, JsonNode completeNode, int steps) {
//        return registerProgress(runnable, progressKey, progressText, completeNode, getStepScales(steps));
//    }
//
//    /**
//     * 注册进度条,业务方法调用
//     *
//     * @param runnable
//     * @param progressKey
//     * @param progressText
//     * @param completeNode
//     * @param stepScale
//     * @return
//     */
//    public Command<?> registerProgress(final Runnable runnable, final String progressKey, String progressText, JsonNode completeNode, Number[] stepScale) {
//        ProgressData progressData = register(runnable, progressKey, progressText, completeNode, stepScale);
//        Progress progress = getProgress(progressData);
//        if (progress != null) {
//            return new Loading(null).setId(ID_LOADING).setNode(progress);
//        } else {
//            String errorMsg = "添加进度条队列失败@" + System.currentTimeMillis();
//            LogUtil.warn(errorMsg + "[" + progressKey + "]");
//            return new Alert(errorMsg);
//        }
//    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable    执行线程
     * @param progressKey 进度条编号
     * @return 进度数据
     */
    public ProgressData register(final Runnable runnable, final String progressKey) {
        return register(runnable, progressKey, null);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable     执行线程
     * @param progressKey  进度条编号
     * @param progressText 进度显示文本
     * @return 进度数据
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText) {
        return register(runnable, progressKey, progressText, null, 1);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable     执行线程
     * @param progressKey  进度条编号
     * @param progressText 进度显示文本
     * @param steps        总步骤数
     * @return 进度数据
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, int steps) {
        return register(runnable, progressKey, progressText, null, steps);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable     执行线程
     * @param progressKey  进度条编号
     * @param progressText 进度显示文本
     * @param complete     完成数据
     * @return 进度数据
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, Serializable complete) {
        return register(runnable, progressKey, progressText, complete, 1);
    }

    /**
     * 注册进度条,业务方法调用
     *
     * @param runnable     执行线程
     * @param progressKey  进度条编号
     * @param progressText 进度显示文本
     * @param complete     完成数据
     * @param steps        总步骤数
     * @return 进度数据
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, Serializable complete, int steps) {
        return register(runnable, progressKey, progressText, complete, getStepScales(steps));
    }

    private Number[] getStepScales(int steps) {
        if (steps < 1) {
            throw new UnsupportedOperationException("The steps must not less than one.");
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
     * @param runnable     执行线程
     * @param progressKey  进度条编号
     * @param progressText 进度显示文本
     * @param complete     完成数据
     * @param stepScale    步骤比例
     * @return 进度数据
     */
    public ProgressData register(final Runnable runnable, final String progressKey, String progressText, Serializable complete, Number[] stepScale) {
        // 空处理
        if (runnable == null) {
            throw new UnsupportedOperationException("The progress register failed, the Runnable can not be null.");
        }
        ProgressData progressData = getProgressData(progressKey);
        if (progressData == null) {
            progressData = new ProgressData();
            progressData.setProgressKey(progressKey);
            progressData.setProgressText(progressText);
            progressData.setComplete(complete);
            stepScale = stepScale == null ? new Number[]{1} : stepScale;
            boolean success = resetStepScale(progressData, stepScale);
            if (success) {
                if (EXECUTOR_SERVICE == null) {
                    EXECUTOR_SERVICE = ThreadUtil.newFixedThreadPool(executorSize);
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
                            if (e instanceof Marked) {
                                errorMsg = e.getMessage();
                                errorCode = ((Marked) e).getCode();
                            } else {
                                errorMsg = "进度条运行异常@" + System.currentTimeMillis();
                                LogUtil.error(errorMsg, e);
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
        return getResponseProgressData(progressData);
    }

//	private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##");

//    Progress getProgress(ProgressData progressData) {
//        if (progressData == null) {
//            return null;
//        }
//
//        // 参数需要加密,担心key被猜测出来获取高权限的数据信息
//        String src = "progress/reloadProgress?progressKey=" + progressData.getProgressKey();
//        Progress progress = new Progress("progress").setSrc(src).setText(progressData.getProgressText());
//        progress.setDelay(progressData.getDelay());
//
//        int steps = progressData.getSteps();
//        if (steps > 1) {
////            String stepText = Utils.isEmpty(stepProgress.getText()) ? this.loadingText : stepProgress.getText();
//            int currStep = progressData.getStepIndex() + 1;
////            stepProgress.setText();
////
////            Progress doneProgress = new Progress("prgDone", progressData.getDonePercent()).setSrc(src).setText("总进度");
////            progressGroup.add(doneProgress);
//            // "(" + (currStep > steps ? steps : currStep) + "/" + steps + ") " + stepText
//            String stepText = "(" + (currStep > steps ? steps : currStep) + "/" + steps + ")";
//            if (Utils.notEmpty(progressData.getProgressText())) {
//                stepText += " " + progressData.getProgressText();
//            }
//            progress.add(new ProgressItem(null, progressData.getStepPercent()).setText(stepText));
//            progress.add(new ProgressItem(null, progressData.getDonePercent()).setText("总进度"));
//        } else {
//            progress.add(new ProgressItem(null, progressData.getDonePercent()).setText(progressData.getProgressText()));
//        }
//        return progress;
//    }

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
        if (progressData == null || progressData.isFinish() || progressData.getStepIndex() >= progressData.getSteps()) {
            return;
        }
        double addStepPercent = stepPercent.doubleValue();
        // 需要按照比例增加进度
        progressData.setStepPercent(formatNumber(progressData.getStepPercent() + addStepPercent));
        if (progressData.getSteps() > 1) {
            progressData.setDonePercent(formatNumber(progressData.getDonePercent() + addStepPercent * progressData.getStepScales()[progressData.getStepIndex()]));
        } else {
            progressData.setDonePercent(progressData.getStepPercent());
        }
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
     * @return 进度数据
     */
    public ProgressData removeProgress(String progressKey) {
        return progressCache.remove(progressKey);
    }

    /**
     * 结束进度
     *
     * @param progressKey 进度编号
     */
    public void finishProgress(String progressKey) {
        ProgressData progressData = getProgressData(progressKey);
        if (progressData != null) {
            progressData.setFinish(true);
            progressData.setStepIndex(progressData.getSteps());
            progressData.setStepPercent(ProgressData.PERCENT_FULL);
            progressData.setDonePercent(ProgressData.PERCENT_FULL);
            // 将进度条数据放到缓存中
            setProgressData(progressData);
        }
    }

    /**
     * 设置步骤比例
     *
     * @param progressKey 进度编号
     * @param stepScale   步骤比例组
     * @return 是否设置成功
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

        if (stepScale.length > 1) {
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
        }
        progressData.setSteps(stepScale.length);
        // 将进度条数据放到缓存中
        return setProgressData(progressData);
    }

    /**
     * 设置步骤数
     *
     * @param progressKey 进度条编号
     * @param steps       步骤数
     * @return 是否设置成功
     */
    public boolean resetSteps(String progressKey, int steps) {
        if (steps < 1) {
            throw new UnsupportedOperationException("The steps must greater than one.");
        }
        ProgressData progressData = getProgressData(progressKey);
        if (steps == 1) {
            progressData.setSteps(1);
            progressData.setStepScales(null);
            return true;
        }
        Number[] stepScale = getStepScales(steps);
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

        if (progressData.getSteps() > 1) {
            // 总偏移量增加
            double doneScale = 0.0;
            for (int i = 0; i <= progressData.getStepIndex(); i++) {
                doneScale += progressData.getStepScales()[i];
            }
            progressData.setDonePercent(formatNumber(doneScale * ProgressData.PERCENT_FULL));
            progressData.setStepPercent(0.0);
        } else {
            progressData.setStepPercent(ProgressData.PERCENT_FULL);
            progressData.setDonePercent(ProgressData.PERCENT_FULL);
        }
        progressData.setStepIndex(progressData.getStepIndex() + 1);
        // 已达到最后一步,暂且不设置结束,等界面刷新到100%再结束
        // 将进度条数据放到缓存中
        setProgressData(progressData);
        return hasNext(progressData);
    }

    DecimalFormat DF = new DecimalFormat("#.##");

    private double formatNumber(Number num) {
        if (num == null) {
            return 0.0;
        }
        String numStr = DF.format(num);
        return Double.parseDouble(numStr);
    }

    /**
     * 是否还有下一步
     *
     * @param progressKey 进度条编号
     * @return 是否有下一步
     */
    public boolean hasNext(String progressKey) {
        ProgressData progressData = getProgressData(progressKey);
        return hasNext(progressData);
    }

    /**
     * 是否还有下一步
     *
     * @param progressData 进度数据
     * @return 是否还有下一步
     */
    private boolean hasNext(ProgressData progressData) {
        return progressData != null && !progressData.isFinish() && progressData.getStepIndex() < progressData.getSteps();
    }

    /**
     * 设置完成数据
     *
     * @param progressKey 进度条编号
     * @param complete    完成数据
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressManager setComplete(String progressKey, Serializable complete) {
        ProgressData progressData = getProgressData(progressKey);
        if (progressData != null) {
            progressData.setComplete(complete);
        }
        return this;
    }

    private boolean getDefaultCryptor = false;

    private Cryptor cryptor() {
        if (this.cryptor == null && !getDefaultCryptor) {
            try {
                this.cryptor = new CryptorFactoryBean().getObject();
            } catch (Exception e) {
                LogUtil.error(getClass(), "default cryptor can not be created.", e);
            }
            getDefaultCryptor = true;
        }
        return this.cryptor;
    }

    /**
     * 加密
     *
     * @param progressKey String 原进度编号
     * @return String 密文
     */
    public String encrypt(String progressKey) {
        if (Utils.notEmpty(progressKey)) {
            Cryptor cryptor = cryptor();
            // 若加密器为空,将以明文形式展示进度编号
            return cryptor != null ? cryptor.encrypt(progressKey) : progressKey;
        }
        return progressKey;
    }

    /**
     * 解密
     *
     * @param progressKey String 加密的进度编号
     * @return String 解密后的原进度编号
     */
    public String decrypt(String progressKey) {
        if (Utils.notEmpty(progressKey)) {
            Cryptor cryptor = cryptor();
            // 若加密器为空,将以明文形式展示进度编号
            return cryptor != null ? cryptor.decrypt(progressKey) : progressKey;
        }
        return progressKey;
    }

}