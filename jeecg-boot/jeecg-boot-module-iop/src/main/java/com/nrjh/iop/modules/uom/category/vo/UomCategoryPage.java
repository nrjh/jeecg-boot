package com.nrjh.iop.modules.uom.category.vo;

import java.util.List;
import com.nrjh.iop.modules.uom.category.entity.UomCategory;
import com.nrjh.iop.modules.uom.category.entity.UomUom;
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
 * @Description: 计量单位类别
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
@Data
@ApiModel(value="uom_categoryPage对象", description="计量单位类别")
public class UomCategoryPage {
	
	/**标识*/
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private String name;
	/**计量类型*/
	@Excel(name = "计量类型", width = 15)
	@ApiModelProperty(value = "计量类型")
	private String measureType;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
	private String writeBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
	private Date writeTime;
	
	@ExcelCollection(name="计量单位")
	@ApiModelProperty(value = "计量单位")
	private List<UomUom> uomUomList;
	
}
