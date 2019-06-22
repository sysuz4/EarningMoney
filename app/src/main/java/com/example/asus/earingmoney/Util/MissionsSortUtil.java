package com.example.asus.earingmoney.Util;

import com.example.asus.earingmoney.model.Mission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MissionsSortUtil {

    static Comparator<Mission> missionComparatorById = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            if(lhs.getMissionId() > rhs.getMissionId())
                return 1;
            return -1;       //注意此处不是0
        }
    };

    static Comparator<Mission> missionComparatorByPriceUp = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            if(lhs.getMoney() < rhs.getMoney())
                return 1;
            return -1;       //注意此处不是0
        }
    };

    static Comparator<Mission> missionComparatorByPriceDown = new Comparator<Mission>() {
        @Override
        public int compare(Mission lhs, Mission rhs) {
            if(lhs.getMoney() > rhs.getMoney())
                return 1;
            return -1;       //注意此处不是0
        }
    };

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
