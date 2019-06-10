package com.answer.test.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "base_user")
public class User {
    @Id
    public Integer id;

    public String username;

    public String password;

    public String name;

    public String birthday;

    public String address;

    @Column(name = "mobile_phone")
    public String mobilePhone;

    @Column(name = "tel_phone")
    public String telPhone;

    public String email;

    public String sex;

    public String type;

    public String description;

    @Column(name = "crt_time")
    public Date crtTime;

    @Column(name = "crt_user")
    public String crtUser;

    @Column(name = "crt_name")
    public String crtName;

    @Column(name = "crt_host")
    public String crtHost;

    @Column(name = "upd_time")
    public Date updTime;

    @Column(name = "upd_user")
    public String updUser;

    @Column(name = "upd_name")
    public String updName;

    @Column(name = "upd_host")
    public String updHost;

    public String attr1;

    public String attr2;

    public String attr3;

    public String attr4;

    public String attr5;

    public String attr6;

    public String attr7;

    public String attr8;

}
