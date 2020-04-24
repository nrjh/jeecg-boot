package com.nrjh.iop.modules.msg.message.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.msg.message.entity.MsgTrackingValue;
import com.nrjh.iop.modules.msg.message.mapper.MsgTrackingValueMapper;
import com.nrjh.iop.modules.msg.message.service.IMsgTrackingValueService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 消息值变更记录
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Service
@DS("iop")
public class MsgTrackingValueServiceImpl extends ServiceImpl<MsgTrackingValueMapper, MsgTrackingValue> implements IMsgTrackingValueService {
	
	@Autowired
	private MsgTrackingValueMapper msgTrackingValueMapper;
	
	@Override
	public List<MsgTrackingValue> selectByMainId(String mainId) {
		return msgTrackingValueMapper.selectByMainId(mainId);
	}
}
