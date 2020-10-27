package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String pid;

    private String isEnd;

    private String isYear;

    private String isMonth;
}
