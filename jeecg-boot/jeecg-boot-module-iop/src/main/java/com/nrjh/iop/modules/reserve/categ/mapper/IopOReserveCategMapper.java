package com.nrjh.iop.modules.reserve.categ.mapper;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.reserve.categ.entity.IopOReserveCateg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 类别信息
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface IopOReserveCategMapper extends BaseMapper<IopOReserveCateg> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
