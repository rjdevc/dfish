package com.rongji.dfish.framework.mvc.response;

import java.io.Serializable;

/**
 * 接口响应头部的调用者信息
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class ResponseHeaderPrincipal implements ResponsePrincipal, Serializable {
    private static final long serialVersionUID = -7352733138575430482L;
    private String name;
    private String natureName;
    private String fullName;

    /**
     * 构造函数
     *
     * @param name 调用者主信息
     */
    public ResponseHeaderPrincipal(String name) {
        this.name = name;
    }

    /**
     * 构造函数
     *
     * @param name       调用者主信息
     * @param natureName 调用者名称
     */
    public ResponseHeaderPrincipal(String name, String natureName) {
        this.name = name;
        this.natureName = natureName;
    }

    /**
     * 调用者主信息
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 调用者主信息
     *
     * @param name
     * @return
     */
    public ResponseHeaderPrincipal setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getNatureName() {
        return natureName;
    }

    /**
     * 调用者名称
     *
     * @param natureName
     * @return
     */
    public ResponseHeaderPrincipal setNatureName(String natureName) {
        this.natureName = natureName;
        return this;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    /**
     * 调用者的完整名称
     *
     * @param fullName
     * @return
     */
    public ResponseHeaderPrincipal setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
}