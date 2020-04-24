package com.nrjh.iop.modules.msg.message.service;

import com.nrjh.iop.modules.msg.message.entity.MsgTrackingValue;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 消息值变更记录
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface IMsgTrackingValueService extends IService<MsgTrackingValue> {

	public List<MsgTrackingValue> selectByMainId(String mainId);
}
