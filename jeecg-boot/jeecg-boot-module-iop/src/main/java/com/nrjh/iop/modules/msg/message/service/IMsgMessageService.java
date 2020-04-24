package com.nrjh.iop.modules.msg.message.service;

import com.nrjh.iop.modules.msg.message.entity.MsgTrackingValue;
import com.nrjh.iop.modules.msg.message.entity.MsgMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface IMsgMessageService extends IService<MsgMessage> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(MsgMessage msgMessage, List<MsgTrackingValue> msgTrackingValueList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(MsgMessage msgMessage, List<MsgTrackingValue> msgTrackingValueList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
