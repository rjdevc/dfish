package com.rongji.dfish.base.batch;

import java.util.*;

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


    /**
     * 批量动作可以兼容单个参数的运算。相当于单个一组。
     * 这里提供单个运算的简便做法。
     *
     * @param input 输入
     * @return O
     */
    default O act(I input) {
        HashSet<I> temp=new HashSet<>();
        temp.add(input);
        Map<I,O> rst=act(temp);
        if(rst==null){
            return null;
        }
        return rst.get(input);
    }

    /**
     * 批量动作也可以使用List来调用。结果则是和input长度一样顺序一样的数组。
     * 注意这种情况下List 的元素可能含有null
     * @param input 输入
     * @return List
     */
    default List<O> act(List<I> input) {
        HashSet<I> keys=new HashSet<>(input);
        Map<I,O> rst=act(keys);
        List<O> output=new ArrayList<>(keys.size());
        for(I item:input){
            output.add(rst.get(item));
        }
        return output;
    }
}
