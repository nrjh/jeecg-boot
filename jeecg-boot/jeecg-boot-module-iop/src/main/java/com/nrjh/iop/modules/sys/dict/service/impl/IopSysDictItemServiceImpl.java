package com.nrjh.iop.modules.sys.dict.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import com.nrjh.iop.modules.sys.dict.mapper.IopSysDictItemMapper;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.IIOParamController;
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
@DS("iop")
public class IopSysDictItemServiceImpl extends ServiceImpl<IopSysDictItemMapper, IopSysDictItem> implements IIopSysDictItemService {

    @Autowired
    private IopSysDictItemMapper iopSysDictItemMapper;

    @Override
    public List<IopSysDictItem> selectItemsByMainId(String mainId) {
        return iopSysDictItemMapper.selectItemsByMainId(mainId);
    }
}
