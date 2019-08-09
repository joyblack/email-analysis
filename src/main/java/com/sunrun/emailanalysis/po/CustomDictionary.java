package com.sunrun.emailanalysis.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ea_custom_dictionary")
public class CustomDictionary {

    @Id
    private Long id;

    private String value;

    private String nature;

    private Integer frequency;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}