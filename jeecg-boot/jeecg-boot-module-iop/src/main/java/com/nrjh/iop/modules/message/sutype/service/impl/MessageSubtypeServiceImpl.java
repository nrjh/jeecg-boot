package com.nrjh.iop.modules.message.sutype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.message.sutype.entity.MessageSubtype;
import com.nrjh.iop.modules.message.sutype.mapper.MessageSubtypeMapper;
import com.nrjh.iop.modules.message.sutype.service.IMessageSubtypeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-10
 * @Version: V1.0
 */
@Service
@DS("iop")
public class MessageSubtypeServiceImpl extends ServiceImpl<MessageSubtypeMapper, MessageSubtype> implements IMessageSubtypeService {

	@Override
	public void addMessageSubtype(MessageSubtype messageSubtype) {
		if(oConvertUtils.isEmpty(messageSubtype.getPid())){
			messageSubtype.setPid(IMessageSubtypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			MessageSubtype parent = baseMapper.selectById(messageSubtype.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(messageSubtype);
	}
	
	@Override
	public void updateMessageSubtype(MessageSubtype messageSubtype) {
		MessageSubtype entity = this.getById(messageSubtype.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = messageSubtype.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				messageSubtype.setPid(IMessageSubtypeService.ROOT_PID_VALUE);
			}
			if(!IMessageSubtypeService.ROOT_PID_VALUE.equals(messageSubtype.getPid())) {
				baseMapper.updateTreeNodeStatus(messageSubtype.getPid(), IMessageSubtypeService.HASCHILD);
			}
		}
		baseMapper.updateById(messageSubtype);
	}
	
	@Override
	public void deleteMessageSubtype(String id) throws JeecgBootException {
		MessageSubtype messageSubtype = this.getById(id);
		if(messageSubtype==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(messageSubtype.getPid());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IMessageSubtypeService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<MessageSubtype>().eq("parent_id", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IMessageSubtypeService.NOCHILD);
			}
		}
	}

}
