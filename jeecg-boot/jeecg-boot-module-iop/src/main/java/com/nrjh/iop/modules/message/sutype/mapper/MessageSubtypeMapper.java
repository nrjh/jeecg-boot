package com.nrjh.iop.modules.message.sutype.mapper;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.message.sutype.entity.MessageSubtype;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-10
 * @Version: V1.0
 */
public interface MessageSubtypeMapper extends BaseMapper<MessageSubtype> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
