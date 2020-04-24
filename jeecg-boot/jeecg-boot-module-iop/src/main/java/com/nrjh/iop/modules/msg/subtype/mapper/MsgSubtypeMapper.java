package com.nrjh.iop.modules.msg.subtype.mapper;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.msg.subtype.entity.MsgSubtype;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface MsgSubtypeMapper extends BaseMapper<MsgSubtype> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
