package com.github.daweizhou89.reqlist;

/**
 * Created by daweizhou89 on 2017/2/17.
 */
public class Constant {

    /**
     * 天转小时
     */
    public static final long TIME_DAY_TO_HOUR = 24;
    /**
     * 时间小时到分钟转换
     */
    public static final long TIME_HOUR_TO_MINUTE = 60;
    /**
     * 时间分钟到秒转换
     */
    public static final long TIME_MINUTE_TO_SECOND = 60;
    /**
     * 时间1秒钟
     */
    public static final long TIME_ONE_SECOND = 1000;
    /**
     * 时间1分钟
     */
    public static final long TIME_ONE_MINUTE = TIME_MINUTE_TO_SECOND * TIME_ONE_SECOND;
    /**
     * 时间1小时
     */
    public static final long TIME_ONE_HOUR = TIME_HOUR_TO_MINUTE * TIME_MINUTE_TO_SECOND * TIME_ONE_SECOND;
    /**
     * 时间1天
     */
    public static final long TIME_ONE_DAY = TIME_DAY_TO_HOUR * TIME_HOUR_TO_MINUTE * TIME_MINUTE_TO_SECOND * TIME_ONE_SECOND;

}
