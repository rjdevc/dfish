package com.rongji.dfish.base;

import jdk.nashorn.internal.ir.ReturnNode;

import java.util.ArrayList;
import java.util.Arrays;
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
 * @deprecated 未完成，临时提交换个环境开发。
 */
@Deprecated
public class RangeDataFinder<K, V extends Comparable<V>> {
    public RangeDataFinder() {
        sequnces = new ArrayList<>();
        NoRepeateSequnce seq = new NoRepeateSequnce();
        sequnces.add(seq);
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
        for (NoRepeateSequnce<K, V> seq : sequnces) {
            if (seq.add(key, begin, end)) {
                return;
            }
        }
        NoRepeateSequnce<K, V> newSeq = new NoRepeateSequnce<>();
        sequnces.add(newSeq);
        newSeq.add(key, begin, end);
    }


    public List<K> find(V value) {
        List<K> ret = new ArrayList<>();
        for (NoRepeateSequnce<K, V> seq : sequnces) {
            K key = seq.find(value);
            if (key != null) {
                ret.add(key);
            }
        }
        return ret;
    }

    public String toString() {
        return sequnces.toString();
    }

    private List<NoRepeateSequnce<K, V>> sequnces;

    static class Entry<K, V extends Comparable<V>> {
        K key;
        V begin;
        V end;

        public String toString() {
            return key + "(" + begin + "," + end + ")";
        }
    }

    static class NoRepeateSequnce<K, V extends Comparable<V>> {

        private List<Entry<K, V>> entries = new ArrayList<>();

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
        public boolean add(K key, V begin, V end) {
            Entry<K, V> entry = new Entry<>();
            entry.key = key;
            if (begin.compareTo(end) > 0) {
                entry.begin = end;
                entry.end = begin;
            } else {
                entry.begin = begin;
                entry.end = end;
            }
            //没有任何内容时直接添加
            if (entries.size() == 0) {
                entries.add(entry);
                return true;
            }
            //找到最小的节点，当前对象的begin<= 这个节点的end 没找到则为-1
            int left = findIndex(true, en -> en.end.compareTo(entry.begin) >=0);
            //找到最大的节点，当前对象的end>= 这个节点的begomg 没找到则为entries.size
            int right = findIndex(false, en -> en.begin.compareTo(entry.end) <= 0);
            // 有错误。
            System.out.println(this+"search " + key + "\tbegin=" + entry.begin + "\tend=" + entry.end + "\tleft=" + left + "\tright=" + right);
            if (right - left == 1) {
                entries.add(right, entry);
                return true;
            }
            for (int i = left + 1; i < right; i++) {
                if (!key.equals(entries.get(i).key)) {
                    return false;
                }
            }
            if (left >= 0 && entries.get(left).begin.compareTo(entry.begin) <= 0) {
                entry.begin = entries.get(left).begin;
            }
            if (right < entries.size() && entries.get(right).end.compareTo(entry.end) >= 0) {
                entry.end = entries.get(right).end;
            }
            entries.set(left + 1, entry);
            //清理多余的节点
            int clear = right - left - 2;
            for (int i = 0; i < clear; i++) {
                entries.remove(left + 2);
            }
            return true;
        }

        private int findIndex(boolean left, Predicate<Entry> p) {
            //找到最小的节点，当前对象的begin<= 这个节点的end 没找到则为-1
            int low = 0;
            int high = entries.size() - 1;
            while (low <= high-2) {
                int mid = (low + high) >>> 1;
                if (p.test(entries.get(mid))==left) {
                    high = mid ;
                } else {
                    low = mid ;
                }
            }
            return left ? high : low;
        }

        public K find(V value) {
            int i = findIndex(value);
            return i < 0 ? null : entries.get(i).key;
        }

        private int findIndex(V value) {
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
                    return mid;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        RangeDataFinder<String, Integer> rdf = new RangeDataFinder();
        rdf.add("福州", 1, 4);
        rdf.add("福州", 4, 6);
        rdf.add("福州", 7, 9);
        rdf.add("厦门", 10, 12);
        System.out.println(rdf);
        System.out.println(rdf.find(2));
    }

}
