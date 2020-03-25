package com.nrjh.iop.modules.reserve.store.service;

import com.nrjh.iop.modules.reserve.store.entity.IopOReserveStore;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 库房维护
 * @Author: jeecg-boot
 * @Date:   2020-03-23
 * @Version: V1.0
 */
public interface IIopOReserveStoreService extends IService<IopOReserveStore> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addIopOReserveStore(IopOReserveStore iopOReserveStore);
	
	/**修改节点*/
	void updateIopOReserveStore(IopOReserveStore iopOReserveStore) throws JeecgBootException;
	
	/**删除节点*/
	void deleteIopOReserveStore(String id) throws JeecgBootException;

}
