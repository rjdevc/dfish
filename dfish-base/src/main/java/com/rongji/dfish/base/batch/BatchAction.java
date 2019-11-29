package com.rongji.dfish.base.batch;

import java.util.Map;
import java.util.Set;

/**
 * BatchAction 是一个批量执行的的动作。
 * 可以根据输入的批量参数，计算得结果。
 * @param <I> 输入参数类型
 * @param <O> 输出参数类型
 */
public interface BatchAction<I,O> {
    /**
     * 执行动作
     * 这里是允许某个输入没有计算结果的。即结果数量比输入少。
     * @param input Set
     * @return Map
     */
    Map<I,O> act(Set<I> input);
}
