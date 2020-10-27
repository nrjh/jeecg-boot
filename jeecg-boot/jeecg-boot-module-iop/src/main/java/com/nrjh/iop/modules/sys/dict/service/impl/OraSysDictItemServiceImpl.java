package com.nrjh.iop.modules.sys.dict.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import com.nrjh.iop.modules.sys.dict.mapper.IopSysDictItemMapper;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictItemService;
import com.nrjh.iop.modules.sys.dict.service.IOraSysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
@DS("ora")
public class OraSysDictItemServiceImpl extends ServiceImpl<IopSysDictItemMapper, IopSysDictItem> implements IOraSysDictItemService {

    @Autowired
    private IopSysDictItemMapper iopSysDictItemMapper;

    @Override
    public List<IopSysDictItem> selectItemsByMainId(String mainId) {
        return iopSysDictItemMapper.selectItemsByMainId(mainId);
    }
}
