package com.nrjh.iop.modules.sys.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface IOraSysDictItemService extends IService<IopSysDictItem> {
    public List<IopSysDictItem> selectItemsByMainId(String mainId);
}
