package com.nrjh.iop.modules.message.sutype.service;

import com.nrjh.iop.modules.message.sutype.entity.MessageSubtype;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-10
 * @Version: V1.0
 */
public interface IMessageSubtypeService extends IService<MessageSubtype> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addMessageSubtype(MessageSubtype messageSubtype);
	
	/**修改节点*/
	void updateMessageSubtype(MessageSubtype messageSubtype) throws JeecgBootException;
	
	/**删除节点*/
	void deleteMessageSubtype(String id) throws JeecgBootException;

}
