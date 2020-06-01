package com.nrjh.iop.modules.stk.move.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 移库单与移库单关系
 * @Author: zzy
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@ApiModel(value="stk_move_move_rel对象", description="移库单与移库单关系")
@Data
@TableName("stk_move_move_rel")
public class StkMoveMoveRel {

    /**移库单*/
    @ApiModelProperty(value = "源移库单")
    private Integer moveSrcId;

    /**移库单*/
    @ApiModelProperty(value = "目标移库单")
    private Integer moveDescId;


}
