package org.jeecg.modules.iop.pub.pubuser.mapper;

import com.nrjh.iop.modules.workOrder.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.iop.pub.pubuser.entity.PubUser2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: pub_user
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@Repository
@Mapper
public interface PubUserMapper2 extends BaseMapper<PubUser2> {

   SysUser selectUserById(@Param("id")String id);

}
