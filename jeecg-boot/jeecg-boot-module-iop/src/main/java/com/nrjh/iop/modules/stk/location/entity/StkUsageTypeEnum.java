package com.nrjh.iop.modules.stk.location.entity;


public enum StkUsageTypeEnum {

    SUPPLIER("supplier", "供应商位置"),
    VIEW("view", "视图"),
    INTERNAL("internal", "内部位置"),
    CUSTOMER("customer", "客户位置"),
    INVENTORY("inventory",
            "盘点位置"),
    PRODUCTION("production", "生产作业位置"),
    TRANIT("transit", "中转位置");


    private String code;
    private String name;

    private StkUsageTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
