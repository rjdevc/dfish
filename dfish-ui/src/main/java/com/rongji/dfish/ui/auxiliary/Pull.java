package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.command.Command;

/**
 * 拖动动作，一般是手机上使用，上拉加载更多，下拉刷新等
 */
public class Pull extends AbstractWidget<Pull> {

    public static final String FACE_NORMAL="normal";
    public static final String FACE_CIRCLE="circle";
    private static final long serialVersionUID = -7231041100690507602L;

    private boolean auto = true;
    private Command refresh;
    private String face;

    /**
     * 构造函数
     */
    public Pull() {
        super();
    }

    /**
     * 构造函数
     * @param refresh Command
     */
    public Pull(Command refresh) {
        super();
        this.refresh = refresh;
    }

    /**
     * 刷新动作
     * @return Command
     */
    public Command getRefresh() {
        return refresh;
    }

    /**
     * 刷新动作
     * @param refresh Command
     * @return this
     */
    public Pull setRefresh(Command refresh) {
        this.refresh = refresh;
        return this;
    }

    /**
     * 自动
     * @return boolean
     */
    public boolean isAuto() {
        return auto;
    }

    /**
     * 自动
     * @param auto boolean
     * @return this
     */
    public Pull setAuto(boolean auto) {
        this.auto = auto;
        return this;
    }


    /**
     * 效果。可选值: normal, circle
     * @return String
     */
    public String getFace() {
        return face;
    }

    /**
     * 效果。可选值: normal, circle
     * @param face  String
     * @return this
     */
    public Pull setFace(String face) {
        this.face = face;
        return this;
    }

}
