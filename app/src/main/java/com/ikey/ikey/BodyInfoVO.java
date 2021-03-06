package com.ikey.ikey;

import java.math.BigDecimal;

/**
 * Created by fready on 2018-04-19.
 */

public class BodyInfoVO {

    String id;
    String name;
    String birth;
    String sex;
    String month;
    String weight;
    String height;
    String preHeight;
    String hpercent;
    String wpercent;
    String insertDate;
    BigDecimal hincre;
    BigDecimal wincre;

    public String getPreHeight() {
        return preHeight;
    }

    public void setPreHeight(String preHeight) {
        this.preHeight = preHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getHincre() {
        return hincre;
    }

    public void setHincre(BigDecimal hincre) {
        this.hincre = hincre;
    }

    public BigDecimal getWincre() {
        return wincre;
    }

    public void setWincre(BigDecimal wincre) {
        this.wincre = wincre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHpercent() {
        return hpercent;
    }

    public void setHpercent(String hpercent) {
        this.hpercent = hpercent;
    }

    public String getWpercent() {
        return wpercent;
    }

    public void setWpercent(String wpercent) {
        this.wpercent = wpercent;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }
}
