package com.nrjh.iop.modules.stk.location.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.mapper.StkLocationMapper;
import com.nrjh.iop.modules.stk.location.service.IStkLocationService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 位置
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkLocationServiceImpl extends ServiceImpl<StkLocationMapper, StkLocation> implements IStkLocationService {

	@Override
	public void addStkLocation(StkLocation stkLocation) {
		if(oConvertUtils.isEmpty(stkLocation.getPid())){
			stkLocation.setPid(IStkLocationService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			StkLocation parent = baseMapper.selectById(stkLocation.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(stkLocation);
	}

	@Override
	public void updateStkLocation(StkLocation stkLocation) {
		StkLocation entity = this.getById(stkLocation.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = stkLocation.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				stkLocation.setPid(IStkLocationService.ROOT_PID_VALUE);
			}
			if(!IStkLocationService.ROOT_PID_VALUE.equals(stkLocation.getPid())) {
				baseMapper.updateTreeNodeStatus(stkLocation.getPid(), IStkLocationService.HASCHILD);
			}
		}
		baseMapper.updateById(stkLocation);
	}

	@Override
	public void deleteStkLocation(String id) throws JeecgBootException {
		StkLocation stkLocation = this.getById(id);
		if(stkLocation==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(stkLocation.getPid());
		baseMapper.deleteById(id);
	}

	/**
	 * 更新全名称和全路径
	 * @param stkLocation
	 */
	@Override
	public void updateComplateName(StkLocation stkLocation) {
		StkLocation parentStkLocation = getById(stkLocation.getPid());
		stkLocation.setCompleteName(parentStkLocation.getCompleteName()+"/"+stkLocation.getName());
		stkLocation.setParentPath(parentStkLocation.getParentPath()+stkLocation.getId()+"/");
		updateStkLocation(stkLocation);
	}


	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IStkLocationService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<StkLocation>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IStkLocationService.NOCHILD);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public void deleteBatchByPid(String pid) {
		baseMapper.deleteById(pid);
		baseMapper.deleteBatchByPid(pid);
	}

	@Override
	public List<StkLocation> getStkLocationByPid(String pid) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("pid",pid);
		return baseMapper.selectList(queryWrapper);
	}
}
