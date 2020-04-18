package com.nrjh.iop.modules.message.message.service;

import com.nrjh.iop.modules.message.message.entity.MessageMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IMessageMessageService extends IService<MessageMessage> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addMessageMessage(MessageMessage messageMessage);
	
	/**修改节点*/
	void updateMessageMessage(MessageMessage messageMessage) throws JeecgBootException;
	
	/**删除节点*/
	void deleteMessageMessage(String id) throws JeecgBootException;

}
