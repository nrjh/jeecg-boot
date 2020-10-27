package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DevMonitorVo implements Serializable {
    private  String  lineNo;
    private  String  equipType;
    private  String  equipNo;
    private  String  areaName;
    private  String  equipName;
    private  String  isHandled;
    private  String  direction;
    private  String  faultNum;

    private List<DevMonitorVo> listSimplexOne;
    private List<DevMonitorVo> listSimplexTwo;
    private List<DevMonitorVo> listCollectionOne;
    private List<DevMonitorVo> listCollectionTwo;
    private List<DevMonitorVo> listThreephaseOne;
    private List<DevMonitorVo> listThreephaseTwo;
    private List<DevMonitorVo> listTransformer;
    private List<DevMonitorVo> listcangchu;

}
