package com.bear.yzx110.gpstool;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bear.yzx110.gpstool.entity.WxEntity;
import com.bear.yzx110.gpstool.view.Compass;
//import com.bear.yzx110.gpstool.view.Yuan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Button bt_navigation;
    private Button bt_about;

    private TextView tv_longitude;
    private TextView tv_latitude;
    private TextView tv_altitude;
    private TextView tv_speed;
    private TextView tv_satellite;
    private TextView tv_lock;
    private TextView tv_mode;
    private TextView tv_accuracy;
    private GridView gv_quality;
    private TextView tv_degrees;

    private List<WxEntity> list;
    private List<WxEntity> listSnr;
    private Compass compass;
    private SensorManager sensorManagermanager;
    private SensorListener listener = new SensorListener();


    private int index = 1;
    private boolean falg1 = false;
    LocationManager locationManager;

    private static final int LOCATION = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        compass = (Compass) this.findViewById(R.id.yuan);
        compass.setKeepScreenOn(true);
        sensorManagermanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        //初始化控件
        bt_about = (Button) this.findViewById(R.id.bt_about);
        bt_navigation = (Button) this.findViewById(R.id.bt_navigation);

        tv_longitude = (TextView) this.findViewById(R.id.tv_longitude);
        tv_latitude = (TextView) this.findViewById(R.id.tv_latitude);
        tv_altitude = (TextView) this.findViewById(R.id.tv_altitude);
        tv_speed = (TextView) this.findViewById(R.id.tv_speed);
        tv_satellite = (TextView) this.findViewById(R.id.tv_satellite);
        tv_accuracy = (TextView) this.findViewById(R.id.tv_accuracy);
        tv_lock = (TextView) this.findViewById(R.id.tv_lock);
        tv_mode = (TextView) this.findViewById(R.id.tv_mode);

        gv_quality = (GridView) this.findViewById(R.id.gv_quality);


        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //当打开软件时判断gps是否打开
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //判断是否为android6.0系统版本，如果是，需要动态添加权限
            if (Build.VERSION.SDK_INT >= 23) {
                showContacts();
            } else {
                openGPSSettings();
            }
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showContacts();
            }
        }

        bt_navigation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        /*bt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });*/


    }

    //重写退出方法 ---------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //back key Constant Value: 4 (0x00000004)
            //创建退出对话框
            AlertDialog.Builder isExit = new AlertDialog.Builder(this);
            //设置对话框标题
            isExit.setTitle("消息提醒");
            //设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setPositiveButton("确定", diaListener);
            isExit.setNegativeButton("取消", diaListener);
            //对话框显示
            isExit.show();
        }
        return false;
    }

    DialogInterface.OnClickListener diaListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int buttonId) {
            // TODO Auto-generated method stub
            switch (buttonId) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addCategory(Intent.CATEGORY_HOME);
                    startActivity(i);
                    finish();
                    locationManager.removeUpdates(locationListener);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"按钮返回界面
                    break;
                default:
                    break;
            }
        }
    };


    //打开gps获取数据
    private void openGPSSettings() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "gps模块正常", Toast.LENGTH_SHORT).show();
            getLocation();
            return;
        } else {
            Toast.makeText(MainActivity.this, "请打开GPS", Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("请打开GPS连接");
            dialog.setMessage("为方便搜索卫星，请先打开GPS");
            dialog.setPositiveButton("设置", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    Toast.makeText(MainActivity.this, "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    getLocation();

                    return;
                }
            });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            dialog.show();
        }

    }

    //动态权限
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED

                ) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,}, LOCATION);
        } else {
            openGPSSettings();
            Toast.makeText(getApplicationContext(), "权限充足", Toast.LENGTH_SHORT).show();
        }
    }

    //Android6.0申请权限的回调方法 ---------------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    openGPSSettings();
                    Toast.makeText(getApplicationContext(), "权限充足2", Toast.LENGTH_SHORT).show();
                } else {
                    // 没有获取到权限，做特殊处理
                    openGPSSettings();
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                openGPSSettings();
                break;
        }
    }

    Location location;

    private void getLocation() {
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceName);
        String provider = LocationManager.GPS_PROVIDER;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updateToNewLocation(location);

        locationManager.requestLocationUpdates(provider, 3000, 3, locationListener);

        // 绑定监听状态
        locationManager.addGpsStatusListener(statusListener);

    }

    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>();

    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int i) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            GpsStatus status = locationManager.getGpsStatus(null);
            updateGpsStatus(i, status);
        }
    };

    // 卫星数据
    private void updateGpsStatus(int i, GpsStatus status) {
        if (i == GpsStatus.GPS_EVENT_FIRST_FIX) {
            Toast.makeText(this, "第一次定位", Toast.LENGTH_SHORT).show();
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
                if (location != null) {

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
                }
                System.out.println(j);
            }

            System.out.println("\n\n-------------\n-------------\n-------------\n\n");
            for (WxEntity w : list) {
                Log.i("aaa", "1:" + w.getAzimuth() + "\n2:" + w.getElevation() + "\n3:" + w.getSnr());
            }
            if (list != null) {
                compass.setList(list);
                Log.i("MainAcitiviy", "list的值不为空 - 在页面传值的过程中");
            } else {
                Log.i("MainAcitiviy", "list的值为空 - 在页面传值的过程中");
            }

            gv_quality.setAdapter(new WxAdapter());

        } else if (i == GpsStatus.GPS_EVENT_STARTED) {

        } else if (i == GpsStatus.GPS_EVENT_STOPPED) {

        }
    }

    // 更新数据
    private void updateToNewLocation(Location location) {

        if (location != null) {
            float accuracy = location.getAccuracy();//精度
            float accuracys = (float) (((int)(accuracy*1000))/1000.0);
            float speed = location.getSpeed();//速度
            double latitude = location.getLatitude();// 纬度
            double latitudes = ((int)(latitude*1000000))/1000000.0;
            double longitude = location.getLongitude();// 经度
            double longitudes = ((int)(longitude*1000000))/1000000.0;
            double altitude = location.getAltitude(); // 海拔
            double altitudes=((int)(altitude*100))/100.0;

            tv_longitude.setText("经度:" + longitudes);
            tv_latitude.setText("纬度:" + latitudes);
            tv_altitude.setText("高度:" + altitudes);
            tv_satellite.setText("卫星：" + numSatelliteList.size());
            tv_accuracy.setText("精度:" + accuracys);
            if(speed*3.6<5){
                tv_speed.setText("速度:0km/h");
            }else {
                tv_speed.setText("速度:" + speed+"km/h");
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
            }
        } else {
            tv_satellite.setText("搜索中...");
        }

    }

    // gps定位
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                updateToNewLocation(location);
                Toast.makeText(MainActivity.this, "你的位置发生改变!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            updateToNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(MainActivity.this, " gps已开启", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(MainActivity.this, "gps已关闭", Toast.LENGTH_SHORT).show();
        }
    };


    //罗盘
    @Override
    protected void onResume() { // ---------------------------------------------------------------------------
        Sensor sensor = sensorManagermanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManagermanager.registerListener((SensorEventListener) listener, sensor,
                SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() { // ----------------------------------------------------------
        super.onPause();
        sensorManagermanager.unregisterListener(listener);
    }

    // 监听罗盘
    private final class SensorListener implements SensorEventListener {
        private float predegree = 0;

        public void onSensorChanged(SensorEvent event) {
            float degree = event.values[0];// 存放了方向值 90
            RotateAnimation animation = new RotateAnimation(predegree, -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(200);
            compass.startAnimation(animation);
            predegree = -degree;

            tv_degrees = (TextView) findViewById(R.id.tv_degrees);
            if (predegree != 0) {
                tv_degrees.setText((int) degree + "°");
            } else {
                tv_degrees.setText("0°");
            }

            System.out.println("罗盘的度数" + (int) degree);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    //适配器
    class WxAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(MainActivity.this, R.layout.item_guality, null);
            TextView tv_item_snr = (TextView) view.findViewById(R.id.tv_item_snr);

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

            if (falg1) {
                index = 1;
                falg1 = false;
            }
            tv_item_snr.setText(list.get(i).getSnr() + "");
            progressBar.incrementProgressBy((int) list.get(i).getSnr());

            return view;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }

}

