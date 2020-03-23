package com.nrjh.iop.modules.sys.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface IopSysDictItemMapper extends BaseMapper<IopSysDictItem> {
    @Select("SELECT * FROM sys_dict_item WHERE DICT_ID = #{mainId} order by sort_order asc, item_value asc")
    public List<IopSysDictItem> selectItemsByMainId(String mainId);
}
