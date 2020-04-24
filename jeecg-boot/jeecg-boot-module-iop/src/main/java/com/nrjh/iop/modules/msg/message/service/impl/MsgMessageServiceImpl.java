package com.nrjh.iop.modules.msg.message.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.msg.message.entity.MsgMessage;
import com.nrjh.iop.modules.msg.message.entity.MsgTrackingValue;
import com.nrjh.iop.modules.msg.message.mapper.MsgTrackingValueMapper;
import com.nrjh.iop.modules.msg.message.mapper.MsgMessageMapper;
import com.nrjh.iop.modules.msg.message.service.IMsgMessageService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Service
@DS("iop")
public class MsgMessageServiceImpl extends ServiceImpl<MsgMessageMapper, MsgMessage> implements IMsgMessageService {

	@Autowired
	private MsgMessageMapper msgMessageMapper;
	@Autowired
	private MsgTrackingValueMapper msgTrackingValueMapper;
	
	@Override
	@Transactional
	public void saveMain(MsgMessage msgMessage, List<MsgTrackingValue> msgTrackingValueList) {
		msgMessageMapper.insert(msgMessage);
		if(msgTrackingValueList!=null && msgTrackingValueList.size()>0) {
			for(MsgTrackingValue entity:msgTrackingValueList) {
				//外键设置
				entity.setMessageId(msgMessage.getId());
				msgTrackingValueMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(MsgMessage msgMessage,List<MsgTrackingValue> msgTrackingValueList) {
		msgMessageMapper.updateById(msgMessage);
		
		//1.先删除子表数据
		msgTrackingValueMapper.deleteByMainId(msgMessage.getId());
		
		//2.子表数据重新插入
		if(msgTrackingValueList!=null && msgTrackingValueList.size()>0) {
			for(MsgTrackingValue entity:msgTrackingValueList) {
				//外键设置
				entity.setMessageId(msgMessage.getId());
				msgTrackingValueMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		msgTrackingValueMapper.deleteByMainId(id);
		msgMessageMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			msgTrackingValueMapper.deleteByMainId(id.toString());
			msgMessageMapper.deleteById(id);
		}
	}
	
}
