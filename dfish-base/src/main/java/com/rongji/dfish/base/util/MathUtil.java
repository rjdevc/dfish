package com.rongji.dfish.base.util;

/**
 * 执行数学任务的工具类
 */
public class MathUtil {

    public static int max(int... i) {
        int result = Integer.MIN_VALUE;
        for (int inte : i) {
            if (result < inte) {
                result = inte;
            }
        }
        return result;
    }
    public static long max(long... l) {
        long result = Long.MIN_VALUE;
        for (long num : l) {
            if (result < num) {
                result = num;
            }
        }
        return result;
    }
    public static double max(double... d) {
        double result = Double.MIN_VALUE;
        for (double num : d) {
            if (result < num) {
                result = num;
            }
        }
        return result;
    }
    public static float max(float... f) {
        float result = Float.MIN_VALUE;
        for (float num : f) {
            if (result < num) {
                result = num;
            }
        }
        return result;
    }

    public static int min(int... i) {
        int result = Integer.MAX_VALUE;
        for (int num : i) {
            if (result > num) {
                result = num;
            }
        }
        return result;
    }
    public static long min(long... l) {
        long result = Long.MAX_VALUE;
        for (long num : l) {
            if (result > num) {
                result = num;
            }
        }
        return result;
    }
    public static double min(double... d) {
        double result = Double.MAX_VALUE;
        for (double num : d) {
            if (result > num) {
                result = num;
            }
        }
        return result;
    }
    public static float min(float... f) {
        float result = Float.MAX_VALUE;
        for (float num : f) {
            if (result > num) {
                result = num;
            }
        }
        return result;
    }
}
