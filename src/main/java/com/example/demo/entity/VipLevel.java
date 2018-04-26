package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_vip_level")
public class VipLevel implements Serializable{
    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "level_name")
    private String levelName;
    @Column(name = "level_standard")
    private Long levelStandard;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getLevelStandard() {
        return levelStandard;
    }

    public void setLevelStandard(Long levelStandard) {
        this.levelStandard = levelStandard;
    }
}
