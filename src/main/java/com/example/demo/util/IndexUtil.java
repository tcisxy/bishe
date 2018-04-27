package com.example.demo.util;

import com.example.demo.entity.VipLevel;
import com.example.demo.param.IndexData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexUtil {
    public static IndexData getIndexData(Map<String,Long> map) {
        IndexData indexData = new IndexData();
        List<String> dates = new ArrayList<>();
        List<Long> moneys = new ArrayList<>();

        for(String time : map.keySet()) {
            dates.add(time);
            moneys.add(map.get(time));
        }
        indexData.setMoney(moneys);
        indexData.setDate(dates);
        return indexData;
    }

    public static int getVipLevel(List<VipLevel> vipLevels, long consumeMoney){
        long stand = 0;
        long id = 0;
        for (VipLevel vipLevel : vipLevels) {
            if (vipLevel.getLevelStandard() <= consumeMoney && vipLevel.getLevelStandard() >= stand) {
                stand = vipLevel.getLevelStandard();
                id = vipLevel.getId();
            }
        }
        return (int) id;
    }
}
