package com.nrjh.iop.modules.reserve.categ.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.reserve.categ.entity.IopOReserveCateg;
import com.nrjh.iop.modules.reserve.categ.mapper.IopOReserveCategMapper;
import com.nrjh.iop.modules.reserve.categ.service.IIopOReserveCategService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 类别信息
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Service
@DS("iop")
public class IopOReserveCategServiceImpl extends ServiceImpl<IopOReserveCategMapper, IopOReserveCateg> implements IIopOReserveCategService {

	@Override
	public void addIopOReserveCateg(IopOReserveCateg iopOReserveCateg) {
		if(oConvertUtils.isEmpty(iopOReserveCateg.getPid())){
			iopOReserveCateg.setPid(IIopOReserveCategService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			IopOReserveCateg parent = baseMapper.selectById(iopOReserveCateg.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(iopOReserveCateg);
	}
	
	@Override
	public void updateIopOReserveCateg(IopOReserveCateg iopOReserveCateg) {
		IopOReserveCateg entity = this.getById(iopOReserveCateg.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = iopOReserveCateg.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				iopOReserveCateg.setPid(IIopOReserveCategService.ROOT_PID_VALUE);
			}
			if(!IIopOReserveCategService.ROOT_PID_VALUE.equals(iopOReserveCateg.getPid())) {
				baseMapper.updateTreeNodeStatus(iopOReserveCateg.getPid(), IIopOReserveCategService.HASCHILD);
			}
		}
		baseMapper.updateById(iopOReserveCateg);
	}
	
	@Override
	public void deleteIopOReserveCateg(String id) throws JeecgBootException {
		IopOReserveCateg iopOReserveCateg = this.getById(id);
		if(iopOReserveCateg==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(iopOReserveCateg.getPid());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IIopOReserveCategService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<IopOReserveCateg>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IIopOReserveCategService.NOCHILD);
			}
		}
	}

}
