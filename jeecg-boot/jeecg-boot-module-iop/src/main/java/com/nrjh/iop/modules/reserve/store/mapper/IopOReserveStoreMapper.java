package com.nrjh.iop.modules.reserve.store.mapper;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.reserve.store.entity.IopOReserveStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 库房维护
 * @Author: jeecg-boot
 * @Date:   2020-03-23
 * @Version: V1.0
 */
public interface IopOReserveStoreMapper extends BaseMapper<IopOReserveStore> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
