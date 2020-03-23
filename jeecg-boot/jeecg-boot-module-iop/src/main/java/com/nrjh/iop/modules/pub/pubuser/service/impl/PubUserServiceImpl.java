package com.nrjh.iop.modules.pub.pubuser.service.impl;

import com.nrjh.iop.modules.pub.pubuser.entity.PubUser;
import com.nrjh.iop.modules.pub.pubuser.mapper.PubUserMapper;
import com.nrjh.iop.modules.pub.pubuser.service.IPubUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户
 * @Author: jeecg-boot
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Service
public class PubUserServiceImpl extends ServiceImpl<PubUserMapper, PubUser> implements IPubUserService {

}
