package com.nrjh.iop.modules.stk.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shiyiteng on 2020/8/5.
 */
public class StkInventoryModel implements Serializable {
    private static final long serialVersionUID = 4013193970046502756L;

    private java.lang.String name;
    private Date dateDone;
    private java.lang.String companyId;
    /**任务编号*/
    private java.lang.String taskNo;
    /**计划开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date planStartTime;
    /**计划结束时间	*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date planEndTime;
    /**备注*/
    private java.lang.String remark;

    private java.lang.String execBy;

    private java.lang.String stockLocationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateDone() {
        return dateDone;
    }

    public void setDateDone(Date dateDone) {
        this.dateDone = dateDone;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExecBy() {
        return execBy;
    }

    public void setExecBy(String execBy) {
        this.execBy = execBy;
    }

    public String getStockLocationId() {
        return stockLocationId;
    }

    public void setStockLocationId(String stockLocationId) {
        this.stockLocationId = stockLocationId;
    }
}
