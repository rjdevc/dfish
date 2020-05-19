package com.rongji.dfish.base.batch;

import java.util.*;

/**
 * BatchFunction 是一个批量执行的的动作。
 * 可以根据输入的批量参数，计算得结果。
 * @param <T> 输入参数类型 type
 * @param <R> 输出参数类型 result
 */
public interface BatchFunction<T, R> extends java.util.function.Function<T, R>{
    /**
     * 执行动作
     * 这里是允许某个输入没有计算结果的。即结果数量比输入少。
     * @param input Set
     * @return Map
     */
    Map<T, R> apply(Set<T> input);


    /**
     * 批量动作可以兼容单个参数的运算。相当于单个一组。
     * 这里提供单个运算的简便做法。
     *
     * @param input 输入
     * @return R
     */
    default R apply(T input) {
        HashSet<T> temp=new HashSet<>();
        temp.add(input);
        Map<T, R> rst=apply(temp);
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
    default List<R> apply(List<T> input) {
        HashSet<T> keys=new HashSet<>(input);
        Map<T, R> rst=apply(keys);
        List<R> output=new ArrayList<>(keys.size());
        for(T item:input){
            output.add(rst.get(item));
        }
        return output;
    }
}
