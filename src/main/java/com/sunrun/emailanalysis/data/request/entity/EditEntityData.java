package com.sunrun.emailanalysis.data.request.entity;

import com.sunrun.emailanalysis.po.EmailEntity;

import java.util.HashMap;
import java.util.List;

public class EditEntityData {
    // Email Id or attach id.
    private Long id;

    // The entity list.
    private List<EmailEntity> entities;

    public EditEntityData() {
    }

    public EditEntityData(Long id, List<EmailEntity> entities) {
        this.id = id;
        this.entities = entities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EmailEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<EmailEntity> entities) {
        this.entities = entities;
    }
}
