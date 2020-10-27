package com.nrjh.iop.modules.stk.in.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javafx.scene.control.Pagination;
import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.stk.in.entity.StockInOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date: 2020-08-16
 * @Version: V1.0
 */
public interface StockInOrderMapper extends BaseMapper<StockInOrder> {
    public List<StockInOrder> selectByInOrderAndDetail( Map map);

}
