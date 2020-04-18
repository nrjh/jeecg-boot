package com.nrjh.iop.modules.message.message.mapper;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.message.message.entity.MessageMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface MessageMessageMapper extends BaseMapper<MessageMessage> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
