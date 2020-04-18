package com.nrjh.iop.modules.prd.attribute.vo;

import java.util.List;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttribute;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 属性
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="prd_attributePage对象", description="属性")
public class PrdAttributePage {
	
	/**标识*/
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private String name;
	/**序号*/
	@Excel(name = "序号", width = 15)
	@ApiModelProperty(value = "序号")
	private Integer sequence;
	/**显示类型(radio:单选按钮; select:下拉框;color:颜色)*/
	@Excel(name = "显示类型", width = 15)
	@ApiModelProperty(value = "显示类型")
	private String displayType;
	/**SKU创建模式(一旦在至少一种产品上使用了属性，就无法更改变体创建模式。always:立即;dynamic:动态;no_variant:从不)*/
	@Excel(name = "SKU创建模式", width = 15)
	@ApiModelProperty(value = "SKU创建模式")
	private String createVariant;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	
	@ExcelCollection(name="属性值")
	@ApiModelProperty(value = "属性值")
	private List<PrdAttributeValue> prdAttributeValueList;
	
}
