package com.nrjh.iop.modules.sys.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import com.nrjh.iop.modules.sys.dict.model.IopDuplicateCheckVo;

public interface IIopDuplicateCheckService extends IService<IopSysDictItem> {
    public Long duplicateCheckCountSql(IopDuplicateCheckVo iopDuplicateCheckVo);

    public Long duplicateCheckCountSqlNoDataId(IopDuplicateCheckVo iopDuplicateCheckVo);

}
