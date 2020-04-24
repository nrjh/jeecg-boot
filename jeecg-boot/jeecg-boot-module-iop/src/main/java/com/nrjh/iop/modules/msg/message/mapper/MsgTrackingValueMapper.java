package com.nrjh.iop.modules.msg.message.mapper;

import java.util.List;
import com.nrjh.iop.modules.msg.message.entity.MsgTrackingValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 消息值变更记录
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface MsgTrackingValueMapper extends BaseMapper<MsgTrackingValue> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<MsgTrackingValue> selectByMainId(@Param("mainId") String mainId);
}
