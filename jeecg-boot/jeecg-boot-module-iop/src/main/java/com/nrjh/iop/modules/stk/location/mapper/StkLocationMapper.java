package com.nrjh.iop.modules.stk.location.mapper;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 位置
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
public interface StkLocationMapper extends BaseMapper<StkLocation> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
