package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: T_DETECT_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("MIDHD_MDS.t_detect_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_detect_task对象", description="T_DETECT_TASK")
public class TDetectTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**任务下发失败原因*/
	@Excel(name = "任务下发失败原因", width = 15)
    @ApiModelProperty(value = "任务下发失败原因")
    private String failReason;
	/**任务状态。00 制定中01 待确认02 待接收03 已接收04 待分配05 已分配06 待检定…*/
	@Excel(name = "任务状态。00 制定中 01 待确认 02 待接收 03 已接收 04 待分配 05 已分配 06 待检定…", width = 15)
    @ApiModelProperty(value = "任务状态。00 制定中 01 待确认 02 待接收 03 已接收 04 待分配 05 已分配 06 待检定…")
    private String taskSatus;
	/**任务优先级*/
	@Excel(name = "任务优先级", width = 15)
    @ApiModelProperty(value = "任务优先级")
    private String taskPrio;
	/**结果是否分析。01 是，02 否*/
	@Excel(name = "结果是否分析。01 是，02 否", width = 15)
    @ApiModelProperty(value = "结果是否分析。01 是，02 否")
    private String analFlag;
	/**是否出具证书。 01：是、02：否*/
	@Excel(name = "是否出具证书。 01：是、02：否", width = 15)
    @ApiModelProperty(value = "是否出具证书。 01：是、02：否")
    private String certFlag;
	/**是否出库。01是、02否*/
	@Excel(name = "是否出库。01是、02否", width = 15)
    @ApiModelProperty(value = "是否出库。01是、02否")
    private String ioFlag;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String supplyNo;
	/**任务开始时间*/
	@Excel(name = "任务开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任务开始时间")
    private Date taskBgnDate;
	/**任务结束时间*/
	@Excel(name = "任务结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任务结束时间")
    private Date taskEndDate;
	/**委托的单位*/
	@Excel(name = "委托的单位", width = 15)
    @ApiModelProperty(value = "委托的单位")
    private String consignOrg;
	/**委托类别。01：室内，02：现场*/
	@Excel(name = "委托类别。01：室内，02：现场", width = 15)
    @ApiModelProperty(value = "委托类别。01：室内，02：现场")
    private String consignType;
	/**委托人姓名*/
	@Excel(name = "委托人姓名", width = 15)
    @ApiModelProperty(value = "委托人姓名")
    private String consigner;
	/**委托的日期*/
	@Excel(name = "委托的日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "委托的日期")
    private Date consignDate;
	/**送检人*/
	@Excel(name = "送检人", width = 15)
    @ApiModelProperty(value = "送检人")
    private String sendName;
	/**送检人电话*/
	@Excel(name = "送检人电话", width = 15)
    @ApiModelProperty(value = "送检人电话")
    private String sendTel;
	/**任务执行负责人姓名*/
	@Excel(name = "任务执行负责人姓名", width = 15)
    @ApiModelProperty(value = "任务执行负责人姓名")
    private String execRespName;
	/**任务执行负责人编号*/
	@Excel(name = "任务执行负责人编号", width = 15)
    @ApiModelProperty(value = "任务执行负责人编号")
    private String execRespNo;
	/**任务执行工作人员组*/
	@Excel(name = "任务执行工作人员组", width = 15)
    @ApiModelProperty(value = "任务执行工作人员组")
    private String execGroup;
	/**执行时间*/
	@Excel(name = "执行时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "执行时间")
    private Date execDate;
	/**进行派工操作的人员编号*/
	@Excel(name = "进行派工操作的人员编号", width = 15)
    @ApiModelProperty(value = "进行派工操作的人员编号")
    private String arrangeNo;
	/**派工日期*/
	@Excel(name = "派工日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "派工日期")
    private Date arrangeDate;
	/**计划制定人员*/
	@Excel(name = "计划制定人员", width = 15)
    @ApiModelProperty(value = "计划制定人员")
    private String makerName;
	/**计划制定人员工号*/
	@Excel(name = "计划制定人员工号", width = 15)
    @ApiModelProperty(value = "计划制定人员工号")
    private String makerNo;
	/**计划制定日期*/
	@Excel(name = "计划制定日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划制定日期")
    private Date madeDate;
	/**招标批次号*/
	@Excel(name = "招标批次号", width = 15)
    @ApiModelProperty(value = "招标批次号")
    private String bidBatchNo;
	/**设备类别。01 电能表，02 电压互感器，03 电流互感器，04 组合互感器，05 计量箱*/
	@Excel(name = "设备类别。01 电能表，02 电压互感器，03 电流互感器，04 组合互感器，05 计量箱", width = 15)
    @ApiModelProperty(value = "设备类别。01 电能表，02 电压互感器，03 电流互感器，04 组合互感器，05 计量箱")
    private String equipCateg;
	/**设备数量*/
	@Excel(name = "设备数量", width = 15)
    @ApiModelProperty(value = "设备数量")
    private Integer equipQty;
	/**关联标识*/
	@Excel(name = "关联标识", width = 15)
    @ApiModelProperty(value = "关联标识")
    private Integer relaId;
	/**关联说明*/
	@Excel(name = "关联说明", width = 15)
    @ApiModelProperty(value = "关联说明")
    private String relaNoRemark;
	/**系统编号*/
	@Excel(name = "系统编号", width = 15)
    @ApiModelProperty(value = "系统编号")
    private String sysNo;
	/**复检方案标识*/
	@Excel(name = "复检方案标识", width = 15)
    @ApiModelProperty(value = "复检方案标识")
    private Integer redetectSchemaId;
	/**复检标志。01 抽检复检，02 全检复检*/
	@Excel(name = "复检标志。01 抽检复检，02 全检复检", width = 15)
    @ApiModelProperty(value = "复检标志。01 抽检复检，02 全检复检")
    private String redetectFlag;
	/**复检数量*/
	@Excel(name = "复检数量", width = 15)
    @ApiModelProperty(value = "复检数量")
    private Integer redetectQty;
	/**类别码*/
	@Excel(name = "类别码", width = 15)
    @ApiModelProperty(value = "类别码")
    private String sortCode;
	/**类型。01电能表：VW_METER_TYPE_CODE、02、03、04互感器：VW_IT_TYPE_CODE、09采集终端：VW_TMNL_TYPE_CODE。如电能表。11 感应式-普通型12 感应式-长寿命13 感应式-亚长寿命…*/
	@Excel(name = "类型。01电能表：VW_METER_TYPE_CODE、02、03、04互感器：VW_IT_TYPE_CODE、09采集终端：VW_TMNL_TYPE_CODE。如电能表。11 感应式-普通型 12 感应式-长寿命 13 感应式-亚长寿命…", width = 15)
	@ApiModelProperty(value = "类型。01电能表：VW_METER_TYPE_CODE、02、03、04互感器：VW_IT_TYPE_CODE、09采集终端：VW_TMNL_TYPE_CODE。如电能表。11 感应式-普通型 12 感应式-长寿命 13 感应式-亚长寿命…")
    private String typeCode;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private String modelCode;
	/**总垛数*/
	@Excel(name = "总垛数", width = 15)
    @ApiModelProperty(value = "总垛数")
    private Integer pileQty;
	/**检定线台体编号*/
	@Excel(name = "检定线台体编号", width = 15)
    @ApiModelProperty(value = "检定线台体编号")
    private String detectEquipNo;
	/**设备码*/
	@Excel(name = "设备码", width = 15)
    @ApiModelProperty(value = "设备码")
    private String equipCode;
	/**关联单号*/
	@Excel(name = "关联单号", width = 15)
    @ApiModelProperty(value = "关联单号")
    private String relaNo;
	/**记录暂停、中止前的任务状态:0-未执行；1-执行中；2-执行完毕；3-暂停；4-中止*/
	@Excel(name = "记录暂停、中止前的任务状态:0-未执行；1-执行中；2-执行完毕；3-暂停；4-中止", width = 15)
    @ApiModelProperty(value = "记录暂停、中止前的任务状态:0-未执行；1-执行中；2-执行完毕；3-暂停；4-中止")
    private String oldTaskStatus;
	/**流水线不合格表计回库状态。01：不合格待复检，02：不合格在库 VW_LINE_DETECT_STATUS*/
	@Excel(name = "流水线不合格表计回库状态。01：不合格待复检，02：不合格在库 VW_LINE_DETECT_STATUS", width = 15)
    @ApiModelProperty(value = "流水线不合格表计回库状态。01：不合格待复检，02：不合格在库 VW_LINE_DETECT_STATUS")
    private String detectStatus;
	/**审核人姓名*/
	@Excel(name = "审核人姓名", width = 15)
    @ApiModelProperty(value = "审核人姓名")
    private String apprName;
	/**上传的文件ID*/
	@Excel(name = "上传的文件ID", width = 15)
    @ApiModelProperty(value = "上传的文件ID")
    private String fileId;
	/**施封时间 01-有号施封；02-无号施封；03-回库后施封；04-无需施封*/
	@Excel(name = "施封时间 01-有号施封；02-无号施封；03-回库后施封；04-无需施封", width = 15)
    @ApiModelProperty(value = "施封时间 01-有号施封；02-无号施封；03-回库后施封；04-无需施封")
    private String sealTime;
	/**是否是确定明细出库。1：是、0：否。如果选择是，则必须选择资产明细。*/
	@Excel(name = "是否是确定明细出库。1：是、0：否。如果选择是，则必须选择资产明细。", width = 15)
    @ApiModelProperty(value = "是否是确定明细出库。1：是、0：否。如果选择是，则必须选择资产明细。")
    private String ioDetFlag;
	/**存放区分类。01 待检定02 待领03 待装06 待报废*/
	@Excel(name = "存放区分类。01 待检定 02 待领 03 待装 06 待报废", width = 15)
    @ApiModelProperty(value = "存放区分类。01 待检定 02 待领 03 待装 06 待报废")
    private String storeAreaSort;
	/**反配送任务单Id*/
	@Excel(name = "反配送任务单Id", width = 15)
    @ApiModelProperty(value = "反配送任务单Id")
    private String dIoTaskid;
	/**父任务ID*/
	@Excel(name = "父任务ID", width = 15)
    @ApiModelProperty(value = "父任务ID")
    private Integer pTaskId;
	/**已分配数量*/
	@Excel(name = "已分配数量", width = 15)
    @ApiModelProperty(value = "已分配数量")
    private Integer assignQty;
	/**已出库数量*/
	@Excel(name = "已出库数量", width = 15)
    @ApiModelProperty(value = "已出库数量")
    private Integer stockOutQty;
	/**已回库数量*/
	@Excel(name = "已回库数量", width = 15)
    @ApiModelProperty(value = "已回库数量")
    private Integer stockInQty;
	/**检定数量*/
	@Excel(name = "检定数量", width = 15)
    @ApiModelProperty(value = "检定数量")
    private Integer detectQty;
	/**检定合格数量*/
	@Excel(name = "检定合格数量", width = 15)
    @ApiModelProperty(value = "检定合格数量")
    private Integer detectConcP;
	/**检定不合格数量*/
	@Excel(name = "检定不合格数量", width = 15)
    @ApiModelProperty(value = "检定不合格数量")
    private Integer detectConcU;
	/**检定未检数量*/
	@Excel(name = "检定未检数量", width = 15)
    @ApiModelProperty(value = "检定未检数量")
    private Integer detectConcN;
	/**任务下发的目标立库系统，在制定检定任务时对于单立库，页面默认展示该立库系统，对于多立库，页面设计下拉框，选择某个立库，数据来源于vw_wh_sys_no，制定任务将立库编号写入此字段*/
	@Excel(name = "任务下发的目标立库系统，在制定检定任务时对于单立库，页面默认展示该立库系统， 对于多立库，页面设计下拉框，选择某个立库，数据来源于vw_wh_sys_no，制定任务将立库编号写入此字段", width = 15)
    @ApiModelProperty(value = "任务下发的目标立库系统，在制定检定任务时对于单立库，页面默认展示该立库系统， 对于多立库，页面设计下拉框，选择某个立库，数据来源于vw_wh_sys_no，制定任务将立库编号写入此字段")
    private String whSysNo;
	/**接收时间*/
	@Excel(name = "接收时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "接收时间")
    private Date revDate;
	/**是否二次抽检标记。01是、02否*/
	@Excel(name = "是否二次抽检标记。01是、02否", width = 15)
    @ApiModelProperty(value = "是否二次抽检标记。01是、02否")
    private Integer isSecond;
	/**流程实例标识*/
	@Excel(name = "流程实例标识", width = 15)
    @ApiModelProperty(value = "流程实例标识")
    private String processinstid;
	/**审核状态。01 初始02 待审核03 待审批04 审核退回05 审批退回06 审批通过07 已汇总08 已报送09 已退换10 已生效11 失效*/
	@Excel(name = "审核状态。01 初始 02 待审核 03 待审批 04 审核退回 05 审批退回 06 审批通过 07 已汇总 08 已报送 09 已退换 10 已生效 11 失效 ", width = 15)
    @ApiModelProperty(value = "审核状态。01 初始 02 待审核 03 待审批 04 审核退回 05 审批退回 06 审批通过 07 已汇总 08 已报送 09 已退换 10 已生效 11 失效 ")
    private String auditStatus;
	/**审核人员编号*/
	@Excel(name = "审核人员编号", width = 15)
    @ApiModelProperty(value = "审核人员编号")
    private String auditNo;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核时间")
    private Date auditDate;
	/**审核说明*/
	@Excel(name = "审核说明", width = 15)
    @ApiModelProperty(value = "审核说明")
    private String auditRemark;
	/**任务终止人员编号*/
	@Excel(name = "任务终止人员编号", width = 15)
    @ApiModelProperty(value = "任务终止人员编号")
    private String suspendNo;
	/**任务终止时间*/
	@Excel(name = "任务终止时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任务终止时间")
    private Date suspendDate;
	/**流水线是否支持自动施封，0：不支持(否)，1：支持(是)*/
	@Excel(name = "流水线是否支持自动施封，0：不支持(否)，1：支持(是)", width = 15)
    @ApiModelProperty(value = "流水线是否支持自动施封，0：不支持(否)，1：支持(是)")
    private Integer isAutoSeal;
	/**目标设备码*/
	@Excel(name = "目标设备码", width = 15)
    @ApiModelProperty(value = "目标设备码")
    private String tarEquipCode;
	/**出库立库系统编号*/
	@Excel(name = "出库立库系统编号", width = 15)
    @ApiModelProperty(value = "出库立库系统编号")
    private String outWhSysNo;
	/**合格入立库系统编号*/
	@Excel(name = "合格入立库系统编号", width = 15)
    @ApiModelProperty(value = "合格入立库系统编号")
    private String quaWhSysNo;
	/**不合格入立库系统编号*/
	@Excel(name = "不合格入立库系统编号", width = 15)
    @ApiModelProperty(value = "不合格入立库系统编号")
    private String qlfWhSysNo;
	/**所属检定基地*/
	@Excel(name = "所属检定基地", width = 15)
    @ApiModelProperty(value = "所属检定基地")
    private String orgNo;
	/**批量信息ID*/
	@Excel(name = "批量信息ID", width = 15)
    @ApiModelProperty(value = "批量信息ID")
    private Integer batchId;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private String contactMethod;
	/**招标结果标识*/
	@Excel(name = "招标结果标识", width = 15)
    @ApiModelProperty(value = "招标结果标识")
    private Integer bidRsltId;
	/**是否一体化设备：0-否，1-是*/
	@Excel(name = "是否一体化设备：0-否，1-是", width = 15)
    @ApiModelProperty(value = "是否一体化设备：0-否，1-是")
    private String isUnify;
	/**数据来源：0-平台，1-营销，2-其他*/
	@Excel(name = "数据来源：0-平台，1-营销，2-其他", width = 15)
    @ApiModelProperty(value = "数据来源：0-平台，1-营销，2-其他")
    private String dataSource;
	/**人工台+平库模式，检定出库时是否需要扫条码：0-出库不需要扫码(否)；1-出库需要扫码(是)*/
	@Excel(name = "人工台+平库模式，检定出库时是否需要扫条码：0-出库不需要扫码(否)；1-出库需要扫码(是)", width = 15)
    @ApiModelProperty(value = "人工台+平库模式，检定出库时是否需要扫条码：0-出库不需要扫码(否)；1-出库需要扫码(是)")
    private Integer isDetOut;
	/**新设备码*/
	@Excel(name = "新设备码", width = 15)
    @ApiModelProperty(value = "新设备码")
    private String equipCodeNew;
	/**变更参数类型 01-不变、02-类型、03-费率、04-电价*/
	@Excel(name = "变更参数类型 01-不变、02-类型、03-费率、04-电价", width = 15)
    @ApiModelProperty(value = "变更参数类型 01-不变、02-类型、03-费率、04-电价")
    private String paramType;
	/**检定任务分配后，工作流是否指定到人；0：不指定到人，工作流配置的角色都可以看到；1：指定到人，只有工作流配置了该人员才能看到*/
	@Excel(name = "检定任务分配后，工作流是否指定到人；0：不指定到人，工作流配置的角色都可以看到；1：指定到人，只有工作流配置了该人员才能看到", width = 15)
    @ApiModelProperty(value = "检定任务分配后，工作流是否指定到人；0：不指定到人，工作流配置的角色都可以看到；1：指定到人，只有工作流配置了该人员才能看到")
    private Integer isAppointor;
	/**实际完成时间*/
	@Excel(name = "实际完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际完成时间")
    private Date finialDate;
	/**锁表成功标识(1-成功，0-初始，2-失败)*/
	@Excel(name = "锁表成功标识(1-成功，0-初始，2-失败)", width = 15)
    @ApiModelProperty(value = "锁表成功标识(1-成功，0-初始，2-失败)")
    private Integer isLocked;
	/**设备模块绑定标志：01:不支持模块、02:支持模块无档案（老设备）、03:支持模块未绑定、04:支持模块已绑定*/
	@Excel(name = "设备模块绑定标志：01:不支持模块、02:支持模块无档案（老设备）、03:支持模块未绑定、04:支持模块已绑定", width = 15)
    @ApiModelProperty(value = "设备模块绑定标志：01:不支持模块、02:支持模块无档案（老设备）、03:支持模块未绑定、04:支持模块已绑定")
    private String commIdentification;
	/**通信模块关联主体设备的检定任务单号，默认-1。*/
	@Excel(name = "通信模块关联主体设备的检定任务单号，默认-1。", width = 15)
    @ApiModelProperty(value = "通信模块关联主体设备的检定任务单号，默认-1。")
    private String mainEquipTaskNo;
	/**标识主体设备和通信模块是否分开检定，1：是，0否，默认为0*/
	@Excel(name = "标识主体设备和通信模块是否分开检定，1：是，0否，默认为0", width = 15)
    @ApiModelProperty(value = "标识主体设备和通信模块是否分开检定，1：是，0否，默认为0")
    private Integer isSeparateDetect;
	/**通信模块检定后的后续流程，1：立即装配，2：检完入库（适用于通信模块检定任务）VW_COMM_AFTER_PROC*/
	@Excel(name = "通信模块检定后的后续流程，1：立即装配，2：检完入库（适用于通信模块检定任务）VW_COMM_AFTER_PROC", width = 15)
    @ApiModelProperty(value = "通信模块检定后的后续流程，1：立即装配，2：检完入库（适用于通信模块检定任务）VW_COMM_AFTER_PROC")
    private Integer commAfterProc;
	/**辅助方案标志，参见T_ASSIST_SCHEME表中主键*/
	@Excel(name = "辅助方案标志，参见T_ASSIST_SCHEME表中主键", width = 15)
    @ApiModelProperty(value = "辅助方案标志，参见T_ASSIST_SCHEME表中主键")
    private Integer assistSchemeId;
	/**组箱码 引用F_EQUIP_GROUP_CODE表EQUIP_GROUP_CODE*/
	@Excel(name = "组箱码 引用F_EQUIP_GROUP_CODE表EQUIP_GROUP_CODE", width = 15)
    @ApiModelProperty(value = "组箱码 引用F_EQUIP_GROUP_CODE表EQUIP_GROUP_CODE")
    private String equipGroupCode;
	/**产权单位*/
	@Excel(name = "产权单位", width = 15)
    @ApiModelProperty(value = "产权单位")
    private String prOrg;
	/**库房标识*/
	@Excel(name = "库房标识", width = 15)
    @ApiModelProperty(value = "库房标识")
    private Integer whId;
	/**是否按明细出库*/
	@Excel(name = "是否按明细出库", width = 15)
    @ApiModelProperty(value = "是否按明细出库")
    private String isDetailOut;
	/**返修入库任务单号*/
	@Excel(name = "返修入库任务单号", width = 15)
    @ApiModelProperty(value = "返修入库任务单号")
    private String exchgeInTaskNo;
	/**预置电费*/
	@Excel(name = "预置电费", width = 15)
    @ApiModelProperty(value = "预置电费")
    private String preElecCharge;
	/**本实体记录的唯一标识号*/
	@Excel(name = "本实体记录的唯一标识号", width = 15)
    @ApiModelProperty(value = "本实体记录的唯一标识号")
    private Integer taskId;
	/**检定方案标识*/
	@Excel(name = "检定方案标识", width = 15)
    @ApiModelProperty(value = "检定方案标识")
    private Integer schemaId;
	/**检定任务对应的检定计划明细标识*/
	@Excel(name = "检定任务对应的检定计划明细标识", width = 15)
    @ApiModelProperty(value = "检定任务对应的检定计划明细标识")
    private Integer planDetId;
	/**检验任务的工单申请编号*/
	@Excel(name = "检验任务的工单申请编号", width = 15)
    @ApiModelProperty(value = "检验任务的工单申请编号")
    private String taskNo;
	/**合同标识*/
	@Excel(name = "合同标识", width = 15)
    @ApiModelProperty(value = "合同标识")
    private Integer ctrtId;
	/**到货批次号*/
	@Excel(name = "到货批次号", width = 15)
    @ApiModelProperty(value = "到货批次号")
    private String arriveBatchNo;
	/**样品批次号*/
	@Excel(name = "样品批次号", width = 15)
    @ApiModelProperty(value = "样品批次号")
    private String sampleBatchNo;
	/**任务类型。01 制定中02 已制定03 已出库04 已检定05 检定完成06 已拒收07 拒收完成*/
	@Excel(name = "任务类型。01 制定中 02 已制定 03 已出库 04 已检定 05 检定完成 06 已拒收 07 拒收完成", width = 15)
    @ApiModelProperty(value = "任务类型。01 制定中 02 已制定 03 已出库 04 已检定 05 检定完成 06 已拒收 07 拒收完成")
    private String taskType;
	/**检定类别：01 样品比对，02 全性能试验，03 抽样检定检测，04 检定检测校准，05 计量标准器检定校准，06 测试设备检定校准，07 计量标准装置检定校准，08 仲裁检测，09 委托检定，10 临时检测申校检测，11 招标选型产品检测，12 招标前批次检测,13 检定质量核查，14 库存复检，15 监督抽检方案，16 适应性检查，17 人工复检*/
	@Excel(name = "检定类别：01 样品比对，02 全性能试验，03 抽样检定检测，04 检定检测校准，05 计量标准器检定校准，06 测试设备检定校准，07 计量标准装置检定校准，08 仲裁检测，09 委托检定，10 临时检测申校检测，11 招标选型产品检测，12 招标前批次检测,13 检定质量核查，14 库存复检，15 监督抽检方案，16 适应性检查，17 人工复检", width = 15)
    @ApiModelProperty(value = "检定类别：01 样品比对，02 全性能试验，03 抽样检定检测，04 检定检测校准，05 计量标准器检定校准，06 测试设备检定校准，07 计量标准装置检定校准，08 仲裁检测，09 委托检定，10 临时检测申校检测，11 招标选型产品检测，12 招标前批次检测,13 检定质量核查，14 库存复检，15 监督抽检方案，16 适应性检查，17 人工复检")
    private String detectType;
	/**检定方式:01自动,02人工,03复合检*/
	@Excel(name = "检定方式:01自动,02人工,03复合检", width = 15)
    @ApiModelProperty(value = "检定方式:01自动,02人工,03复合检")
    private String detectMode;
	/**任务下发状态，参照视图VW_TDT_SEND_STATUS。01 未下发02 已下发03 下发失败*/
	@Excel(name = "任务下发状态，参照视图VW_TDT_SEND_STATUS。01 未下发 02 已下发 03 下发失败", width = 15)
    @ApiModelProperty(value = "任务下发状态，参照视图VW_TDT_SEND_STATUS。01 未下发 02 已下发 03 下发失败")
    private String sendStatus;
	/**任务下发时间*/
	@Excel(name = "任务下发时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "任务下发时间")
    private Date sendDate;
	@TableLogic
	private Integer isDel;
}
