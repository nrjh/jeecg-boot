package com.nrjh.iop.modules.alarm.bind.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: fault_grade_info
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@Data
@TableName("fault_grade_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fault_grade_info对象", description="fault_grade_info")
public class FaultGradeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**故障等级*/
	@Excel(name = "故障等级", width = 15)
    @ApiModelProperty(value = "故障等级")
    @DictIop(dicCode = "FAULT_GRADE")
    private String faultGrade;
	/**工单生成时长*/
	@Excel(name = "工单生成时长", width = 15)
    @ApiModelProperty(value = "工单生成时长")
    @DictIop(dicCode = "ORDER_CRE_TIME")
    private String orderCreTime;
	/**故障催办时间*/
	@Excel(name = "故障催办时间", width = 15)
    @ApiModelProperty(value = "故障催办时间")
    @DictIop(dicCode = "ORDER_CHE_TIME")
    private String orderCheTime;
	/**故障消缺时间*/
	@Excel(name = "故障消缺时间", width = 15)
    @ApiModelProperty(value = "故障消缺时间")
    @DictIop(dicCode = "ORDER_CLEAR_TIME")
    private String orderClearTime;
    @TableLogic
    private Integer isDel;
}
