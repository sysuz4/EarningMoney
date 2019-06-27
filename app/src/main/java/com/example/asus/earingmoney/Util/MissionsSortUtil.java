package com.example.asus.earingmoney.Util;

import com.example.asus.earingmoney.model.Mission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//用于通过mission的各个属性来进行比较
public class MissionsSortUtil {

    //通过id比较
    static Comparator<Mission> missionComparatorById = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            if(lhs.getMissionId() > rhs.getMissionId())
                return 1;
            return -1;       //注意此处不是0
        }
    };

    //通过赏金比较
    static Comparator<Mission> missionComparatorByPriceUp = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            if(lhs.getAvemoney() < rhs.getAvemoney())
                return 1;
            return -1;       //注意此处不是0
        }
    };

    static Comparator<Mission> missionComparatorByPriceDown = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            if(lhs.getAvemoney() > rhs.getAvemoney())
                return 1;
            return -1;       //注意此处不是0
        }
    };

    //通过截止时间比较
    static Comparator<Mission> missionComparatorByTimeUp = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if(sdf.parse(lhs.getDeadLine()).getTime() < sdf.parse(rhs.getDeadLine()).getTime())
                    return 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }
    };

    static Comparator<Mission> missionComparatorByTimeDown = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if(sdf.parse(lhs.getDeadLine()).getTime() > sdf.parse(rhs.getDeadLine()).getTime())
                    return 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }
    };

    public static void sortById(List<Mission> missionsArray) {
        Collections.sort(missionsArray, missionComparatorById);
    }
    public static void sortByPriceUp(List<Mission> missionsArray) {
        Collections.sort(missionsArray, missionComparatorByPriceUp);
    }
    public static void sortByPriceDown(List<Mission> missionsArray) {
        Collections.sort(missionsArray, missionComparatorByPriceDown);
    }
    public static void sortByTimeUp(List<Mission> missionsArray) {
        Collections.sort(missionsArray, missionComparatorByTimeUp);
    }
    public static void sortByTimeDown(List<Mission> missionsArray) {
        Collections.sort(missionsArray, missionComparatorByTimeDown);
    }
}
