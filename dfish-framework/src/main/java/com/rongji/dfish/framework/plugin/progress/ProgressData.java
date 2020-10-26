package com.rongji.dfish.framework.plugin.progress;

import java.io.Serializable;

/**
 * 进度条数据对象
 *
 * @author lamontYu
 * @since DFish3.0
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
     * 进度条异常
     */
    private Error error;

    /**
     * 进度编号
     *
     * @return String
     */
    public String getProgressKey() {
        return progressKey;
    }

    /**
     * 进度编号
     *
     * @param progressKey 进度编号
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setProgressKey(String progressKey) {
        this.progressKey = progressKey;
        return this;
    }

    /**
     * 进度显示文本
     *
     * @return 进度显示文本
     */
    public String getProgressText() {
        return progressText;
    }

    /**
     * 进度显示文本
     *
     * @param progressText 进度显示文本
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setProgressText(String progressText) {
        this.progressText = progressText;
        return this;
    }

    /**
     * 总步骤数,默认只有1个步骤
     *
     * @return int 总步骤数
     */
    public int getSteps() {
        return steps;
    }

    /**
     * 总步骤数,默认只有1个步骤
     *
     * @param steps 总步骤数
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    /**
     * 步骤比例;若不设置,比例将按照步骤数均分
     *
     * @return 步骤比例
     */
    public double[] getStepScales() {
        return stepScales;
    }

    /**
     * 步骤比例;若不设置,比例将按照步骤数均分
     *
     * @param stepScales 步骤比例
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setStepScales(double[] stepScales) {
        this.stepScales = stepScales;
        return this;
    }

    /**
     * 当前步骤
     *
     * @return int 当前步骤下标
     */
    public int getStepIndex() {
        return stepIndex;
    }

    /**
     * 当前步骤
     *
     * @param stepIndex 当前步骤下标
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
        return this;
    }

    /**
     * 当前步骤完成进度
     *
     * @return 当前步骤完成进度
     */
    public double getStepPercent() {
        return stepPercent;
    }

    /**
     * 当前步骤完成进度
     *
     * @param stepPercent 当前步骤完成进度
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setStepPercent(double stepPercent) {
        this.stepPercent = stepPercent;
        return this;
    }

    /**
     * 所有步骤总完成进度,单步骤时等于{@link #stepPercent}
     *
     * @return 所有步骤总完成进度
     */
    public double getDonePercent() {
        return donePercent;
    }

    /**
     * 所有步骤总完成进度,单步骤时等于{@link #stepPercent}
     *
     * @param donePercent 所有步骤总完成进度
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setDonePercent(double donePercent) {
        this.donePercent = donePercent;
        return this;
    }

    /**
     * 下次加载时间(毫秒)
     *
     * @return long 下载加载时间
     */
    public long getDelay() {
        return delay;
    }

    /**
     * 下次加载时间
     *
     * @param delay 下次加载时间
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    /**
     * 是否进度完成,就算进度已经100%,该属性如果还是false,进度条还是不会关闭
     *
     * @return boolean 是否完成
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * 是否进度完成,就算进度已经100%,该属性如果还是false,进度条还是不会关闭
     *
     * @param finish 是否完成
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setFinish(boolean finish) {
        this.finish = finish;
        return this;
    }

    /**
     * 完成之后的结果
     *
     * @return 完成结果对象
     */
    public Serializable getComplete() {
        return complete;
    }

    /**
     * 完成之后的结果
     *
     * @param complete 完成结果对象
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setComplete(Serializable complete) {
        this.complete = complete;
        return this;
    }

    /**
     * 错误提示
     *
     * @return 本身，这样可以继续设置属性 错误提示
     */
    public Error getError() {
        return error;
    }

    /**
     * 错误提示
     *
     * @param error 错误提示
     * @return 本身，这样可以继续设置属性
     */
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

    /**
     * 错误信息
     *
     * @param errorMsg 错误信息
     * @return 本身，这样可以继续设置属性
     */
    public ProgressData setErrorMessage(String errorMsg) {
        error().setMessage(errorMsg);
        return this;
    }

    /**
     * 错误代码
     *
     * @param errorCode 错误代码
     * @return 本身，这样可以继续设置属性
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

        private String message;
        private String code;

        public Error() {
        }

        public Error(String message) {
            this.message = message;
        }

        public Error(String message, String code) {
            this.message = message;
            this.code = code;
        }

        /**
         * 错误信息
         *
         * @return 错误信息
         */
        public String getMessage() {
            return message;
        }

        /**
         * 错误信息
         *
         * @param message 错误信息
         * @return 本身，这样可以继续设置属性
         */
        public Error setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 错误代码
         *
         * @return 错误代码
         */
        public String getCode() {
            return code;
        }

        /**
         * 错误代码
         *
         * @param code 错误代码
         * @return 本身，这样可以继续设置属性
         */
        public Error setCode(String code) {
            this.code = code;
            return this;
        }

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
