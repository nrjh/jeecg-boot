package com.nrjh.iop.modules.pub.pubuser.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户
 * @Author: jeecg-boot
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Data
@TableName("pub_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pub_user对象", description="用户")
public class PubUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**name*/
	@Excel(name = "姓名", width = 50)
    @ApiModelProperty(value = "name")
    private String name;
	/**sex*/
	@Excel(name = "性别", width = 1, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "sex")
    private String sex;
	/**age*/
	@Excel(name = "年龄", width = 3)
    @ApiModelProperty(value = "age")
    private Integer age;
	/**remark*/
	@Excel(name = "备注", width = 255)
    @ApiModelProperty(value = "remark")
    private String remark;
}
