package com.nrjh.iop.modules.msg.subtype.service;

import com.nrjh.iop.modules.msg.subtype.entity.MsgSubtype;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface IMsgSubtypeService extends IService<MsgSubtype> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addMsgSubtype(MsgSubtype msgSubtype);
	
	/**修改节点*/
	void updateMsgSubtype(MsgSubtype msgSubtype) throws JeecgBootException;
	
	/**删除节点*/
	void deleteMsgSubtype(String id) throws JeecgBootException;

}
