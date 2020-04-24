package com.nrjh.iop.modules.msg.subtype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.msg.subtype.entity.MsgSubtype;
import com.nrjh.iop.modules.msg.subtype.mapper.MsgSubtypeMapper;
import com.nrjh.iop.modules.msg.subtype.service.IMsgSubtypeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Service
@DS("iop")
public class MsgSubtypeServiceImpl extends ServiceImpl<MsgSubtypeMapper, MsgSubtype> implements IMsgSubtypeService {

	@Override
	public void addMsgSubtype(MsgSubtype msgSubtype) {
		if(oConvertUtils.isEmpty(msgSubtype.getPid())){
			msgSubtype.setPid(IMsgSubtypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			MsgSubtype parent = baseMapper.selectById(msgSubtype.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(msgSubtype);
	}
	
	@Override
	public void updateMsgSubtype(MsgSubtype msgSubtype) {
		MsgSubtype entity = this.getById(msgSubtype.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = msgSubtype.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				msgSubtype.setPid(IMsgSubtypeService.ROOT_PID_VALUE);
			}
			if(!IMsgSubtypeService.ROOT_PID_VALUE.equals(msgSubtype.getPid())) {
				baseMapper.updateTreeNodeStatus(msgSubtype.getPid(), IMsgSubtypeService.HASCHILD);
			}
		}
		baseMapper.updateById(msgSubtype);
	}
	
	@Override
	public void deleteMsgSubtype(String id) throws JeecgBootException {
		MsgSubtype msgSubtype = this.getById(id);
		if(msgSubtype==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(msgSubtype.getPid());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IMsgSubtypeService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<MsgSubtype>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IMsgSubtypeService.NOCHILD);
			}
		}
	}

}
