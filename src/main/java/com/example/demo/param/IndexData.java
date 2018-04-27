package com.example.demo.param;

import java.util.Date;
import java.util.List;

public class IndexData {
    private List<String> date;
    private List<Long> money;

    public List<Long> getMoney() {
        return money;
    }

    public void setMoney(List<Long> money) {
        this.money = money;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }
}
