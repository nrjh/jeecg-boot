package com.nrjh.iop.modules.alarm.bind.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import com.nrjh.iop.modules.stk.in.entity.StockInOrder;
import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
public interface FaultPrdProductInfoMapper extends BaseMapper<FaultPrdProductInfo> {

    public List<FaultPrdProductInfoVO> pageAlarmByFaultTypeAndId(FaultPrdProductInfoVO faultPrdProductInfoVO);
    public List<FaultPrdProductInfoVO> pageAlarmByLineNoAndId(FaultPrdProductInfoVO faultPrdProductInfoVO);

    public List<FaultPrdProductInfoVO> queryFaultPrdProduct(FaultPrdProductInfoVO faultPrdProductInfoVO);
    public String queryExplanInfo(String lineNo);
}
