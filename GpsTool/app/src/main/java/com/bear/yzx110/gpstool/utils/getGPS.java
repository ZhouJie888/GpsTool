package com.bear.yzx110.gpstool.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.bear.yzx110.gpstool.MainActivity;
import com.bear.yzx110.gpstool.entity.WxEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yzx110 on 2018/8/3.
 */

public class getGPS {
    private Activity activity;
    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>();
    private List<WxEntity> list;
    private List<WxEntity> listSnr;

    public getGPS(){

    }

    public getGPS(Activity activity){
        this.activity = activity;
    }

    // 卫星数据
    public List<WxEntity> updateGpsStatus(int i, GpsStatus status) {
        if (i == GpsStatus.GPS_EVENT_FIRST_FIX) {
            Toast.makeText(activity, "第一次定位", Toast.LENGTH_SHORT).show();
        } else if (i == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> iterator = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;
            while (iterator.hasNext() && count <= maxSatellites) {
                GpsSatellite s = iterator.next();
                numSatelliteList.add(s);
                count++;
            }

            //输出卫星信息
            list = new ArrayList<WxEntity>();
            listSnr = new ArrayList<WxEntity>();
            for (int j = 0; j < numSatelliteList.size(); j++) {
                //将数据存到实体类
                WxEntity w = new WxEntity();
                //卫星的方位角，浮点型数据
                System.out.println("方位角" + numSatelliteList.get(j).getAzimuth());
                w.setAzimuth(numSatelliteList.get(j).getAzimuth());
                //卫星的高度角，浮点型数据
                System.out.println("高度角" + numSatelliteList.get(j).getElevation());
                w.setElevation(numSatelliteList.get(j).getElevation());
                //卫星的伪随机噪声码，整形数据
                System.out.println("伪随机噪声码" + numSatelliteList.get(j).getPrn());
                //卫星的信噪比，浮点型数据
                System.out.println("信噪比" + numSatelliteList.get(j).getSnr());
                w.setSnr(numSatelliteList.get(j).getSnr());
                //卫星是否有年历表，布尔型数据
                System.out.println("年历表" + numSatelliteList.get(j).hasAlmanac());
                //卫星是否有星历表，布尔型数据
                System.out.println("星历表" + numSatelliteList.get(j).hasEphemeris());
                //卫星是否被用于近期的GPS修正计算
                System.out.println("近期的GPS修正计算" + numSatelliteList.get(j).hasAlmanac());

                if (numSatelliteList.get(j).getSnr() > 0) {
                    list.add(w);
                }
                if (numSatelliteList.get(j).getSnr() >= 30) {
                    listSnr.add(w);
                }
                /*if (location != null) {

                    tv_satellite.setText("卫星：" + numSatelliteList.size());
                }

                if (listSnr != null) {
                    tv_lock.setText("锁定：" + listSnr.size());
                    if (listSnr.size() >= 3) {
                        tv_mode.setText("锁定");
                        tv_mode.setTextColor(Color.GREEN);
                    } else {
                        tv_mode.setText("未锁定");
                        tv_mode.setTextColor(Color.RED);
                    }
                }*/
                System.out.println(j);
            }

            System.out.println("\n\n-------------\n-------------\n-------------\n\n");
           /* for (WxEntity w : list) {
                Log.i("aaa", "1:" + w.getAzimuth() + "\n2:" + w.getElevation() + "\n3:" + w.getSnr());
            }
            if (list != null) {
                compass.setList(list);
                Log.i("MainAcitiviy", "list的值不为空 - 在页面传值的过程中");
            } else {
                Log.i("MainAcitiviy", "list的值为空 - 在页面传值的过程中");
            }

            gv_quality.setAdapter(new MainActivity.WxAdapter()); */
        } else if (i == GpsStatus.GPS_EVENT_STARTED) {

        } else if (i == GpsStatus.GPS_EVENT_STOPPED) {

        }
        return list;
    }
}
