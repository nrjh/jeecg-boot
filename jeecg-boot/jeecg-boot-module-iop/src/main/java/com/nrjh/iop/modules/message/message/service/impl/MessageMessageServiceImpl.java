package com.nrjh.iop.modules.message.message.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.message.message.entity.MessageMessage;
import com.nrjh.iop.modules.message.message.mapper.MessageMessageMapper;
import com.nrjh.iop.modules.message.message.service.IMessageMessageService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Service
@DS("iop")
public class MessageMessageServiceImpl extends ServiceImpl<MessageMessageMapper, MessageMessage> implements IMessageMessageService {

	@Override
	public void addMessageMessage(MessageMessage messageMessage) {
		if(oConvertUtils.isEmpty(messageMessage.getPid())){
			messageMessage.setPid(IMessageMessageService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			MessageMessage parent = baseMapper.selectById(messageMessage.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(messageMessage);
	}
	
	@Override
	public void updateMessageMessage(MessageMessage messageMessage) {
		MessageMessage entity = this.getById(messageMessage.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = messageMessage.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				messageMessage.setPid(IMessageMessageService.ROOT_PID_VALUE);
			}
			if(!IMessageMessageService.ROOT_PID_VALUE.equals(messageMessage.getPid())) {
				baseMapper.updateTreeNodeStatus(messageMessage.getPid(), IMessageMessageService.HASCHILD);
			}
		}
		baseMapper.updateById(messageMessage);
	}
	
	@Override
	public void deleteMessageMessage(String id) throws JeecgBootException {
		MessageMessage messageMessage = this.getById(id);
		if(messageMessage==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(messageMessage.getPid());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IMessageMessageService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<MessageMessage>().eq("parent_id", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IMessageMessageService.NOCHILD);
			}
		}
	}

}
