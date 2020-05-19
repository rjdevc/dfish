//package com.rongji.dfish.base.batch;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
///**
// * 抽象的批量动作
// * @param <I> 输入参数类型
// * @param <O> 输出参数类型
// */
//public abstract class AbstractBaseAction<I,O> implements BatchAction<I,O>{
//    /**
//     * 批量动作可以兼容单个参数的运算。相当于单个一组。
//     * 这里提供单个运算的简便做法。
//     *
//     * @param input 输入
//     * @return O
//     */
//    public O act(I input) {
//        HashSet<I> temp=new HashSet<>();
//        temp.add(input);
//        Map<I,O> rst=act(temp);
//        if(rst==null){
//            return null;
//        }
//        return rst.get(input);
//    }
//
//    /**
//     * 批量动作也可以使用List来调用。结果则是和input长度一样顺序一样的数组。
//     * 注意这种情况下List 的元素可能含有null
//     * @param input 输入
//     * @return List
//     */
//    public List<O> act(List<I> input) {
//        HashSet<I> keys=new HashSet<>(input);
//        Map<I,O> rst=act(keys);
//        List<O> output=new ArrayList<>(keys.size());
//        for(I item:input){
//            output.add(rst.get(item));
//        }
//        return output;
//    }
//
//}
