package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: DEV_PRODUCE_CYCLE
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("dev_produce_cycle")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dev_produce_cycle对象", description="DEV_PRODUCE_CYCLE")
public class DevProduceCycle implements Serializable {
    private static final long serialVersionUID = 1L;

	/**0正常 1删除*/
	@Excel(name = "0正常 1删除", width = 15)
    @ApiModelProperty(value = "0正常 1删除")
    private java.lang.String isDel = "0";
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private java.lang.String cycleYear;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private java.lang.String cycleMonth;
	/**单相*/
	@Excel(name = "单相", width = 15)
    @ApiModelProperty(value = "单相")
    private java.lang.Integer cycleSumD = 0;
	/**三相*/
	@Excel(name = "三相", width = 15)
    @ApiModelProperty(value = "三相")
    private java.lang.Integer cycleSumS = 0;
	/**采集*/
	@Excel(name = "采集", width = 15)
    @ApiModelProperty(value = "采集")
    private java.lang.Integer cycleSumC = 0;
	/**互感器	*/
	@Excel(name = "互感器	", width = 15)
    @ApiModelProperty(value = "互感器	")
    private java.lang.Integer cycleSumH = 0;

	@ApiModelProperty("父节点")
    private String parentId;
}
