package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_user")
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column(name = "sex")
    private Integer sex;
    @Column(name = "age")
    private Integer age;
    @Column(name = "vip_level")
    private Integer vipLevel;
//    @Column(name = "total_recharge")
//    private Long totalRecharge;
    @Column(name = "total_consume")
    private Long totalConsume;
//    @Column(name = "cur_money")
//    private Long curMoney;
    @Column(name = "visit_num")
    private Integer visitNum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

//    public Long getTotalRecharge() {
//        return totalRecharge;
//    }
//
//    public void setTotalRecharge(Long totalRecharge) {
//        this.totalRecharge = totalRecharge;
//    }

    public Long getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(Long totalConsume) {
        this.totalConsume = totalConsume;
    }

//    public Long getCurMoney() {
//        return curMoney;
//    }
//
//    public void setCurMoney(Long curMoney) {
//        this.curMoney = curMoney;
//    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }
}
