package org.jeecg.common.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:  数字自增    maxNum为传入开始自增值；count为自增位数
 * @author: lei-li
 * @create: 2020-09-03 14:44
 */
public class NumberIncrementUtils {
    //静态变量存储最大值
    private static final AtomicInteger atomicNum = new AtomicInteger();
    //初始化分组编号
    private static final int INIT_NUM = 0;
    public static String getAutoIncrementNum(Integer maxNum,String count ){
        if(maxNum == null){
            maxNum = INIT_NUM;
        }
        atomicNum.set(maxNum);
        //自增
        int newNum = atomicNum.incrementAndGet();
        //数字长度为5位，长度不够数字前面补0
        String newStrNum = String.format("%0"+count+"d", newNum);
        return newStrNum;
    }
}
