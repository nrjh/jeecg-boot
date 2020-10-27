package com.nrjh.iop.modules.alarm.bind.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String pid;

    private String isEquip;
}
