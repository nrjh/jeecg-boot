package com.nrjh.iop.modules.reserve.categ.service;

import com.nrjh.iop.modules.reserve.categ.entity.IopOReserveCateg;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 类别信息
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface IIopOReserveCategService extends IService<IopOReserveCateg> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addIopOReserveCateg(IopOReserveCateg iopOReserveCateg);
	
	/**修改节点*/
	void updateIopOReserveCateg(IopOReserveCateg iopOReserveCateg) throws JeecgBootException;
	
	/**删除节点*/
	void deleteIopOReserveCateg(String id) throws JeecgBootException;

}
