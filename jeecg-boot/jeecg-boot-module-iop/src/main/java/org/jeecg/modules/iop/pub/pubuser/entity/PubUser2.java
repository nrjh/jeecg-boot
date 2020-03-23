package org.jeecg.modules.iop.pub.pubuser.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: pub_user
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@Data
@TableName("pub_user")
public class PubUser2 implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**姓名*/
	@Excel(name = "姓名", width = 50)
    private String name;
	/**年龄*/
	@Excel(name = "年龄", width = 3)
    private Integer age;
	/**备注*/
	@Excel(name = "备注", width = 200)
    private transient String remarkString;

    private byte[] remark;

    public byte[] getRemark(){
        if(remarkString==null){
            return null;
        }
        try {
            return remarkString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRemarkString(){
        if(remark==null || remark.length==0){
            return "";
        }
        try {
            return new String(remark,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
