package com.rongji.dfish.base.batch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class AbstractBaseAction<I,O> implements BatchAction<I,O>{
    /**
     * 返回缓存的值
     *
     * @param input
     * @return V
     */
    public O act(I input) {
        HashSet<I> temp=new HashSet<>();
        temp.add(input);
        Map<I,O> rst=act(temp);
        if(rst==null){
            return null;
        }
        return rst.get(input);
    }

    /**
     *
     * @param input
     * @return
     */
    public List<O> act(List<I> input) {
        HashSet<I> keys=new HashSet<>(input);
        Map<I,O> rst=act(keys);
        List<O> output=new ArrayList<>(keys.size());
        for(I item:input){
            output.add(rst.get(item));
        }
        return output;
    }

}
