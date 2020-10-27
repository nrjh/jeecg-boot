package com.nrjh.iop.modules.workOrder.entity;

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
 * @Description: 工单附件表
 * @Author: lei-li
 * @Date:   2020-09-02
 * @Version: V1.0
 */
@Data
@TableName("ord_enclosure_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ord_enclosure_info对象", description="工单附件表")
public class OrdEnclosureInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**工单单号*/
	@Excel(name = "工单单号", width = 15)
    @ApiModelProperty(value = "工单单号")
    private String workOrderId;
	/**工单状态*/
	@Excel(name = "工单状态", width = 15)
    @ApiModelProperty(value = "工单状态")
    private String processStatus;
	/**附件名称*/
	@Excel(name = "附件名称", width = 15)
    @ApiModelProperty(value = "附件名称")
    private String enclosureName;
	/**附件地址*/
	@Excel(name = "附件地址", width = 15)
    @ApiModelProperty(value = "附件地址")
    private String enclosureUrl;
//	/**所属部门*/
//	@Excel(name = "所属部门", width = 15)
//    @ApiModelProperty(value = "所属部门")
//    private String sysOrgCode;
}
