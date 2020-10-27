package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

/**
 * Created by shiyiteng on 2020/8/15.
 */
@Slf4j
public class OrderNoUtil {
    public static String getOrderNo(String str){
        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddHHmmss");
        String dateS= DateUtils.date2Str(sd);
        if(StringUtils.isEmpty(str)){
            str="ZNYWNO";
        }
        str=str+dateS;
        return str;
    }
}
