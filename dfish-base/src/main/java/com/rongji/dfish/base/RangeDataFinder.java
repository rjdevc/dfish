package com.rongji.dfish.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * RangeDataFinder 用于查找日常数据。
 * 比如说 IP 数据
 * <pre>福州市 27.148.0.0  到27.149.255.255
 * 南平市 27.150.32.0 到 27.150.37.255
 * 福州市 27.155.41.0 到 27.156.127.255
 * 福州市  36.248.0.0 到 36.248.127.255
 * </pre>
 * 等，很多数据。我们现在得到一个IP，用该类，可以快速定位属于哪个KEY
 * <p>
 * userage:
 * //初始化
 * RangeDataFinder&lt;String ,Long&gt; finder=new RangeDataFinder();
 * finder.add("福州市", IPV4.stringToLong("27.148.0.0"), IPV4.stringToLong("27.149.255.255"));
 * finder.add("南平市", IPV4.stringToLong("27.150.32.0"), IPV4.stringToLong("27.150.37.255"));
 * // and many data
 * //平时使用
 * List&lt;K&gt; keys=finder.find(IPV4.stringToLong("27.150.33.14"));
 *
 * @param <K> 关键字的类型
 * @param <V> 范围的类型，必须是可以比较的类型。
 */
public class RangeDataFinder<K, V extends Comparable<V>> {
    public RangeDataFinder() {
        dataList = new ArrayList<>();
        SortedData seq = new SortedData();
        dataList.add(seq);
    }

    /**
     * 增加一个区间数据
     *
     * @param key   找到一行数据o 中关键字。注意关键字如果相同(equals )，并且范围有交叠的话，会合并。
     *              并且最终搜索返回结果也只返回Key 不会返回整行数据。
     *              这里说的交叠是要明显交叠。比如说区间[13-24]与区间[25-36]不会判定成交叠
     *              但[12-24]与区间[24-36] 会判定成交叠[12-36]与区间[13-35] 也会判定成交叠。
     * @param begin 范围开始值
     * @param end   范围结束值
     */
    public void add(K key, V begin, V end) {
        for (SortedData<K, V> seq : dataList) {
            if (seq.add(key, begin, end)) {
                return;
            }
        }
        SortedData<K, V> newSeq = new SortedData<>();
        dataList.add(newSeq);
        newSeq.add(key, begin, end);
    }


    /**
     * 根据 区间内某个数据找到 关键字
     * @param value
     * @return
     */
    public List<K> find(V value) {
        List<K> ret = new ArrayList<>();
        for (SortedData<K, V> seq : dataList) {
            K key = seq.findKey(value);
            if (key != null) {
                ret.add(key);
            }
        }
        return ret;
    }

    public String toString() {
        return dataList.toString();
    }

    private List<SortedData<K, V>> dataList;

    public static class Entry<K, V extends Comparable<V>> {
        private K key;
        private V begin;
        private V end;
        public Entry(){}
        public Entry(K key,V begin,V end){
            this.key=key;
            this.begin=begin;
            this.end=end;
        }
        public K getKey(){
            return key;
        }
        public V getBegin(){
            return begin;
        }
        public V getEnd(){
            return end;
        }
        public String toString() {
            return key + "(" + begin + "," + end + ")";
        }
    }

    /**
     * 排序号的数据。它可以接收数据，并快速寻找value
     * 如果数据有重复的时候，add可能返回false。这时候，可以放在另一个SortedData中
     * @param <K> 关键字
     * @param <V> 值
     */
    static class SortedData<K, V extends Comparable<V>> {

        private ArrayList<Entry<K, V>> entries = new ArrayList<>();

        public String toString() {
            return entries.toString();
        }

        /**
         * 将当前数据添加到这个序列中。
         * 如果这个数据和前后没有关联则直接加入。
         * 如果有关联连，则看key是否一致，如果一致，融合这些节点。
         * 如果不一致，添加失败返回false。 如果返回false，建议添加到另个一个Sequnce中去
         *
         * @param key
         * @param begin
         * @param end
         * @return
         */
        public boolean add(K key,V begin,V end){
            // entry容错，如果begin和end 异常，则交换
            Entry<K,V> entry;
            if(end.compareTo(begin)<0){
                entry=new Entry(key,end,begin);
            }else{
                entry=new Entry(key,begin,end);
            }
            //如果这个data里面还未有数据
            if( entries.isEmpty()){
                entries.add(entry);
            }

            // 根据 entry.begin 与已有各个数据的end比较，算出， 这个新添的数据 关联的最小元素的位置。
            // 如果没有任何元素的end 小于等于 entry.begin 则该位置 为 entries.size()
            int min=findFirst(en->entry.begin.compareTo(en.end)<=0);
            // 根据 entry.end 与已有各个数据的begin比较，算出， 这个新添的数据 关联的最大元素的位置。
            // 如果没有任何元素的begin 大于等于 entry.end 则该位置 为 -1
            int max=findFirst(en->entry.end.compareTo(en.begin)<0)-1;
            // 若最小位置大于最大位置 (通常也只大1)那么 直接在最小位置处插入  这个新的entry
            if(max<min){
                entries.add(min,entry);
                return true;
            }

            // 如果最小位置和最大位置相同，或者最小位置小于最大位置。
            // 判定这些位置的entry key是否与entry相同
            // 若有一个不同，则直接返回false 不再执行任何插入。
            for(int i=min;i<=max;i++){
                if(!entries.get(i).key.equals(entry.key)){
                    return false;
                }
            }

            // 则将entry 的begin end和 对应位置的begin end 合并。其中begin取最小位置，end取最大位置。
            // begin选择更小的那个。end选择更大的那个。 并把这个值设置回 最小位置的entry
            if(entry.begin.compareTo( entries.get(min).begin)<0){
                entries.get(min).begin=entry.begin;
            }
            if(entry.end.compareTo( entries.get(max).end)>0){
                entries.get(min).end=entry.end;
            }else{
                entries.get(min).end=entries.get(max).end;
            }
            // 如果最小位置小于最大位置。 则最小位置后续的几个位置要删除。
            if(max>min){
                for(int i=0;i<max-min;i++){
                    entries.remove(min+1);
                }
            }
            return true;
        }

        //找到第一个符合条件的位置，如果都找不到，则返回 entries.size()
        //因为是有序的，所以二分查找将会是有效的做法。
        private int findFirst(Predicate<Entry<K,V>> p){
            int low = 0;
            int high = entries.size() - 1;
            while (low <high-1) {
                int mid = (low + high) >>> 1;
                if (p.test(entries.get(mid))) {
                    high = mid ;
                } else {
                    low = mid + 1;
                }
            }
            if(!p.test(entries.get(low))){
                if(low==high||!p.test(entries.get(high))){
                    return high+1;
                }
                return high;
            }
            return low;
        }

        /**
         * 找到value 所在区间的 key
         * @param value 值
         * @return K
         */
        public K findKey(V value) {
            Entry<K,V> entry = findEntry(value);
            return entry== null ?null: entry.key;
        }

        /**
         * 找到value 所在区间的 Entry
         * @param value
         * @return
         */
        public Entry<K,V> findEntry(V value) {
            int low = 0;
            int high = entries.size() - 1;
            while (low <= high) {
                int mid = (low + high) >>> 1;
                Entry<K, V> midVal = entries.get(mid);
                if (value.compareTo(midVal.end) > 0) {
                    low = mid + 1;
                } else if (value.compareTo(midVal.begin) < 0) {
                    high = mid - 1;
                } else {
                    return midVal;
                }
            }
            return null;
        }
    }



}
