package com.jinhe.tss.framework.mock.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.framework.persistence.IEntity;

@Entity
@Table(name = "test_group_role")
public class _GroupRole implements IEntity {

    @EmbeddedId  
    private _GroupRoleId id;

    public _GroupRoleId getId() {
        return id;
    }

    public void setId(_GroupRoleId id) {
        this.id = id;
    }

   
}
