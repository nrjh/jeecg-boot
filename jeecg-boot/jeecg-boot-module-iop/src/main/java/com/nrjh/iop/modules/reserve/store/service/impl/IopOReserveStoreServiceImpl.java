package com.nrjh.iop.modules.reserve.store.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.reserve.store.entity.IopOReserveStore;
import com.nrjh.iop.modules.reserve.store.mapper.IopOReserveStoreMapper;
import com.nrjh.iop.modules.reserve.store.service.IIopOReserveStoreService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 库房维护
 * @Author: jeecg-boot
 * @Date:   2020-03-23
 * @Version: V1.0
 */
@Service
@DS("iop")
public class IopOReserveStoreServiceImpl extends ServiceImpl<IopOReserveStoreMapper, IopOReserveStore> implements IIopOReserveStoreService {

	@Override
	public void addIopOReserveStore(IopOReserveStore iopOReserveStore) {
		if(oConvertUtils.isEmpty(iopOReserveStore.getPid())){
			iopOReserveStore.setPid(IIopOReserveStoreService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			IopOReserveStore parent = baseMapper.selectById(iopOReserveStore.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(iopOReserveStore);
	}
	
	@Override
	public void updateIopOReserveStore(IopOReserveStore iopOReserveStore) {
		IopOReserveStore entity = this.getById(iopOReserveStore.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = iopOReserveStore.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				iopOReserveStore.setPid(IIopOReserveStoreService.ROOT_PID_VALUE);
			}
			if(!IIopOReserveStoreService.ROOT_PID_VALUE.equals(iopOReserveStore.getPid())) {
				baseMapper.updateTreeNodeStatus(iopOReserveStore.getPid(), IIopOReserveStoreService.HASCHILD);
			}
		}
		baseMapper.updateById(iopOReserveStore);
	}
	
	@Override
	public void deleteIopOReserveStore(String id) throws JeecgBootException {
		IopOReserveStore iopOReserveStore = this.getById(id);
		if(iopOReserveStore==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(iopOReserveStore.getPid());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IIopOReserveStoreService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<IopOReserveStore>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IIopOReserveStoreService.NOCHILD);
			}
		}
	}

}
