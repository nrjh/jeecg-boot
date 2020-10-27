package com.nrjh.iop.modules.alarm.alarmtype.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiyiteng on 2020/8/25.
 */
public class EquipIdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 对应中的id字段,前端数据树中的key*/
    private String key;

    /** 对应中的id字段,前端数据树中的value*/
    private String value;

    /** 对应name字段,前端数据树中的title*/
    private String title;

    private boolean isLeaf;

    private String id;

    private String paramId;

    private String paramName;

    private String equipId;

    private String areaId;

    private String name;

    private String equipNo;

    private String equipType;

    private String typeName;

    private String parentId;

    List<EquipIdModel> children = new ArrayList<>();


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<EquipIdModel> getChildren() {
        return children;
    }

    public void setChildren(List<EquipIdModel> children) {
        this.children = children;
    }


    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipNo() {
        return equipNo;
    }

    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
