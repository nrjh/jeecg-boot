package com.nrjh.iop.modules.stk.location.entity;

public enum StkLocationEnum {
    LOCATION("试图位置",StkUsageTypeEnum.VIEW.getCode()),
    STOCK("库存区",StkUsageTypeEnum.VIEW.getCode()),
    STOCK_INPUT("接货区",StkUsageTypeEnum.INTERNAL.getCode()),
    STOCK_OUT("出库暂存区",StkUsageTypeEnum.INTERNAL.getCode()),
    STOCK_QC("质控区",StkUsageTypeEnum.INTERNAL.getCode()),
    STOCK_PACK("打包区",StkUsageTypeEnum.INTERNAL.getCode());


    private String name;
    private String usageType;

    StkLocationEnum(String name, String usageType) {
        this.name = name;
        this.usageType = usageType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }
}
