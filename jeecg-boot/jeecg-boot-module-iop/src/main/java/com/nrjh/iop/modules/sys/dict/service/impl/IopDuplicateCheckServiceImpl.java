package com.nrjh.iop.modules.sys.dict.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import com.nrjh.iop.modules.sys.dict.mapper.IopSysDictItemMapper;
import com.nrjh.iop.modules.sys.dict.mapper.IopSysDictMapper;
import com.nrjh.iop.modules.sys.dict.model.IopDuplicateCheckVo;
import com.nrjh.iop.modules.sys.dict.service.IIopDuplicateCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS("iop")
public class IopDuplicateCheckServiceImpl extends ServiceImpl<IopSysDictItemMapper, IopSysDictItem> implements IIopDuplicateCheckService {

    @Autowired
    IopSysDictMapper iopsysDictMapper;

    @Override
    public Long duplicateCheckCountSql(IopDuplicateCheckVo iopDuplicateCheckVo) {
        return iopsysDictMapper.duplicateCheckCountSql(iopDuplicateCheckVo);
    }

    @Override
    public Long duplicateCheckCountSqlNoDataId(IopDuplicateCheckVo iopDuplicateCheckVo) {
        return iopsysDictMapper.duplicateCheckCountSqlNoDataId(iopDuplicateCheckVo);
    }
}
