package com.nrjh.iop.modules.sys.dict.vo;

import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

@Data
public class IopSysDictPage {

    /**
     * 主键
     */
    private String id;
    /**
     * 字典名称
     */
    @Excel(name = "字典名称", width = 20)
    private String dictName;

    /**
     * 字典编码
     */
    @Excel(name = "字典编码", width = 30)
    private String dictCode;
    /**
     * 删除状态
     */
    private Integer delFlag;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 30)
    private String description;

    @ExcelCollection(name = "字典列表")
    private List<IopSysDictItem> sysDictItemList;

}
