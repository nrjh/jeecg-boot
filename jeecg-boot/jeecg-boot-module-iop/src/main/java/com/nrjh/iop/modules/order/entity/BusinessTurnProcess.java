package com.nrjh.iop.modules.order.entity;

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
 * @Description: business_turn_process
 * @Author: jeecg-boot
 * @Date:   2020-10-24
 * @Version: V1.0
 */
@Data
@TableName("business_turn_process")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="business_turn_process对象", description="business_turn_process")
public class BusinessTurnProcess implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**businessId*/
	@Excel(name = "businessId", width = 15)
    @ApiModelProperty(value = "businessId")
    private String businessId;
	/**businessName*/
	@Excel(name = "businessName", width = 15)
    @ApiModelProperty(value = "businessName")
    private String businessName;
	/**businessType*/
	@Excel(name = "businessType", width = 15)
    @ApiModelProperty(value = "businessType")
    private String businessType;
	/**roleId*/
	@Excel(name = "roleId", width = 15)
    @ApiModelProperty(value = "roleId")
    private String roleId;
	/**roleName*/
	@Excel(name = "roleName", width = 15)
    @ApiModelProperty(value = "roleName")
    private String roleName;
	/**businessSequence*/
	@Excel(name = "businessSequence", width = 15)
    @ApiModelProperty(value = "businessSequence")
    private Integer businessSequence;
}
