package com.nrjh.iop.modules.stk.location.service;

import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 位置
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
public interface IStkLocationService extends IService<StkLocation> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addStkLocation(StkLocation stkLocation);
	
	/**修改节点*/
	void updateStkLocation(StkLocation stkLocation) throws JeecgBootException;
	
	/**删除节点*/
	void deleteStkLocation(String id) throws JeecgBootException;

	void updateComplateName(StkLocation stkLocation);

}
