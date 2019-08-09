package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ea_case_type")
public class CaseType {
    @Id
    private Integer typeId;

    private String typeName;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}