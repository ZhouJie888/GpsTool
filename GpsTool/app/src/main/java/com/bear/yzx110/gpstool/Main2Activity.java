package com.bear.yzx110.gpstool;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.bear.yzx110.gpstool.demo.LocationApplication;
import com.bear.yzx110.gpstool.entity.Gps;
import com.bear.yzx110.gpstool.service.LocationService;
import com.bear.yzx110.gpstool.utils.PositionUtil;
import com.bear.yzx110.gpstool.utils.saveBaiDuGPS_gxt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private MapView mMapView = null;
    private Button btn, btnHF;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    private double latitude = 0.0, latitude1 = 0.0; // 纬度
    private double longitude = 0.0, longitude1 = 0.0; // 经度
//    private float radius = 0.0f; // 精度
    private boolean isFirstLoc = true;
    private LocationService locationService;
    public BDLocation alocation;
    private LatLng latLng, target;
    private int num = 1;
    private saveBaiDuGPS_gxt save;
    //将多点放到list集合中
//    List<LatLng> latLngs = new ArrayList<LatLng>();
    // 画点
    private DotOptions m;
//    List<OverlayOptions> options = new ArrayList<OverlayOptions>();

    public BDLocationListener myListener;
//    private MapStatus.Builder builder;

//    BitmapDescriptor startBD = BitmapDescriptorFactory.fromResource(R.mipmap.rad1);
//    private Marker mMarker;
//    private InfoWindow mInfoWindow;
    private LocationManager locationManager;
    private boolean gpsFlag = false;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        mMapView = (MapView) findViewById(R.id.bmapView);

        btn = (Button) findViewById(R.id.buttonText);
        btnHF = (Button) findViewById(R.id.buttonHF);

        /**
         * 轨迹纪录按钮
         *
         * 纪录GPS轨迹
         * 启动地图打点功能
         */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("开始记录".equals(btn.getText())) {
                    btn.setText("结束记录");
                    Toast.makeText(Main2Activity.this, "开始记录", Toast.LENGTH_SHORT).show();
                    save = new saveBaiDuGPS_gxt(Main2Activity.this);
                    // 画点
                    m = new DotOptions();

                    // 关闭定位监听 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//                    guanBiGpsJianTing();
                    gpsFlag = true;


                    String serviceName = Context.LOCATION_SERVICE;
//                    String provider = LocationManager.GPS_PROVIDER;
                    locationManager = (LocationManager) Main2Activity.this.getSystemService(serviceName);
                    // 判断GPS是否打开
                    GpsIsOr();

                    replyLocationListener(true);
                } else if ("结束记录".equals(btn.getText())){
                    btn.setText("开始记录");
                    gpsFlag = false;
//                    btn.setBackgroundColor(Color.parseColor("#f00"));
                    replyLocationListener(false);
                    Toast.makeText(Main2Activity.this, "记录结束", Toast.LENGTH_SHORT).show();
//                    locationManager.
                    // 开启定位监听
//                    kaiQiGpsJianTing();
                }
            }
        });

        /**
         * 回到原点定位功能
         */
        btnHF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPingMuCenter();
            }
        });
    }

    //重写返回方法
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if((System.currentTimeMillis()-mExitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出此页面",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            else
            {
                finish();

                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
        isFirstLoc = true;
        Log.i("text2", "onStart");

        /*LocationApplication applicationContext = (LocationApplication) getApplicationContext();
        locationService = applicationContext.getLocationService();
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int typepe = this.getIntent().getIntExtra("from", 0);
//        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
        locationService.start();*/
        kaiQiGpsJianTing();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd0911");
        int span = 1000;
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        // 可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        // 可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        // 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 判断GPS是否打开
     */
    private void GpsIsOr(){
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(Main2Activity.this, "请打开GPS", Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
            dialog.setTitle("请打开GPS连接");
            dialog.setMessage("为方便搜索卫星，请先打开GPS");
            dialog.setPositiveButton("设置", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                Toast.makeText(Main2Activity.this, "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                }
            });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 重复申请
                    GpsIsOr();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(Main2Activity.this, "gps模块正常", Toast.LENGTH_SHORT).show();
        }
    }

    // 是否使用纯GPS定位
    private void replyLocationListener(boolean bool){
        String provider = LocationManager.GPS_PROVIDER;
        if(bool) {
            if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);

            getPingMuCenter(); // 定位置
        } else {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
                Toast.makeText(Main2Activity.this,"已经移除",Toast.LENGTH_SHORT).show();
            }
        }
        mBaiduMap.getMapStatusLimit(); // ---------------------------------------------------------------------------------------------------------------
    }
    // 纯GPS定位
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("Main2Activity -->", "\n经度：" + location.getLongitude() + " - \t纬度：" + location.getLatitude());
//                Gps gps = PositionUtil.gps84_To_Gcj02(location.getLatitude(), location.getLongitude());
//                Gps gps1 = PositionUtil.gcj02_To_Bd09(gps.getWgLat(), gps.getWgLon());
//                latitude1 = gps1.getWgLat();
//                longitude1 = gps1.getWgLon();


                //将GPS设备采集的原始GPS坐标转换成百度坐标
                CoordinateConverter converter  = new CoordinateConverter();
                converter.from(CoordType.GPS);
                //sourceLatLng待转换坐标
                converter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
                LatLng desLatLng = converter.convert();
                latitude1 = desLatLng.latitude;
                longitude1 = desLatLng.longitude;

//                latitude1 = location.getLatitude();
//                longitude1 = location.getLongitude();

                // 点击记录按钮保存文件
                if(gpsFlag){
                    StringBuffer cc = new StringBuffer(1024);
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
                    Date date = new Date();
                    String format1 = format.format(date);
                    try {
                        date = format.parse(format.format(date).toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // 添加时间
                    cc.append("[" + format1 + "]");
                    // 添加时间戳
                    cc.append("," + date.getTime());
                    // 添加经度
                    cc.append("," + latitude1);
                    // 添加纬度
                    cc.append("," + longitude1);
                    // 添加速度
                    cc.append("," + location.getSpeed());
                    // 添加默认写0
                    cc.append("," + 0);
                    // 序号，随写入的gps数量递增 0，1，2，3...
                    cc.append("," + num++);
                    // 当前是否锁定，锁定状态判断，卫星的
                    cc.append("," + null);
                    // 高度
                    cc.append("," + location.getAltitude());
                    // gps更新时间
                    String time1 = format.format(location.getTime());
                    cc.append(",[" + time1 + "]\n");
                    save.saveData_gxt("skyruler/gps", "/" + time1.substring(0,8) + ".gxt", cc.toString());
                }
                target = desLatLng;
                Log.e("Main2Activity --|", "\n经度：" + longitude1 + " - \t纬度：" + latitude1 + "\n");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
//            updateToNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(Main2Activity.this, " gps已开启", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(Main2Activity.this, "gps已关闭", Toast.LENGTH_SHORT).show();
        }
    };

    // 定位监听
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            alocation = location;
            if(null != location && location.getLocType() != BDLocation.TypeServerError){
                StringBuffer sb = new StringBuffer(256);
                sb.append("时间：" + location.getTime());
                sb.append("\n纬度：" + location.getLatitude());
                latitude = location.getLatitude();
                sb.append("\n经度：" + location.getLongitude());
                longitude = location.getLongitude();

                Log.e("Main2Activity", "位置刷新" + latitude + " - " + longitude);


                /*target = new LatLng(latitude, longitude);  ------------------------------------------------------------------
                latLngs.add(target);*/


                sb.append("\n海拔：" + location.getAltitude());
                sb.append("Poi：");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                sb.append("\n");
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("gps定位成功");
                } else if(location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append("网络定位成功");
                } else if(location.getLocType() == BDLocation.TypeOffLineLocation) {
                    sb.append("离线定位成功");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                System.out.println("输出 -- " + sb);
            }
        }
    };

    // 画点
    private void getPoint(LatLng lat){
//        Log.i("text", "到画点的getPoint方法里面了");
        //        List<OverlayOptions> options = new ArrayList<>();
//        for (int i = 0; i < latLngs.size(); i++) {
//             OverlayOptions option = new MarkerOptions().position(latLngs.get(i)).icon(startBD);
//             options.add(option);

//            m.center(latLngs.get(i));
            m.center(lat);
            m.color(Color.parseColor("#03A9F4"));
            m.radius(4);
            mBaiduMap.addOverlay(m);
            /*DotOptions mDotOptions = new DotOptions()
                    .center(latLngs.get(i))
                    .color(Color.parseColor("#03A9F4"))
                    .radius(4);*/
//            final Overlay overlay = mBaiduMap.addOverlay(m);

//        }
//        List<Overlay> overlays = mBaiduMap.addOverlays(options);

//        List<OverlayOptions> options = new ArrayList<>();
        /*LatLng p1 = new LatLng(28.20882364809228, 112.87512765221578);
        LatLng p2 = new LatLng(28.208924488151702, 112.87506725612928);
        LatLng p3 = new LatLng(28.20908031517851, 112.87500679778111);
        LatLng p4 = new LatLng(28.209236140364236, 112.87494633986917);
        LatLng p5 = new LatLng(28.209391963702195, 112.87488588240387);
        LatLng p6 = new LatLng(28.209547785185954, 112.8748254253975);
        LatLng p7 = new LatLng(28.20970360480877, 112.87476496886448);
        OverlayOptions options1 = new MarkerOptions().position(p1).icon(startBD);
        OverlayOptions options2 = new MarkerOptions().position(p2).icon(startBD);
        OverlayOptions options3 = new MarkerOptions().position(p3).icon(startBD);
        OverlayOptions options4 = new MarkerOptions().position(p4).icon(startBD);
        OverlayOptions options5 = new MarkerOptions().position(p5).icon(startBD);
        OverlayOptions options6 = new MarkerOptions().position(p6).icon(startBD);
        OverlayOptions options7 = new MarkerOptions().position(p7).icon(startBD);
        options.add(options1);
        options.add(options2);
        options.add(options3);
        options.add(options4);
        options.add(options5);
        options.add(options6);
        options.add(options7);
        mBaiduMap.addOverlays(options);*/
        /*for (LatLng l:latLngs) {
            Log.i("text", "测试存储的Latlng " + latLngs.size());
        }*/
    }

    // 开启定位监听
    private void kaiQiGpsJianTing(){
        LocationApplication applicationContext = (LocationApplication) getApplicationContext();
        locationService = applicationContext.getLocationService();
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int typepe = this.getIntent().getIntExtra("from", 0);
//        if (type == 0) {
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
        locationService.start();

//      getPingMuCenter();
    }

    // 关闭定位监听
    private void guanBiGpsJianTing() {
        // 注销监听
//        locationService.unregisterListener(mListener);
        mLocationClient.unRegisterLocationListener(myListener);
        // 停止定位服务
        locationService.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);

    }

    // 定位屏幕中心
    private void getPingMuCenter(){
        if(gpsFlag)
            latLng = new LatLng(latitude1, longitude1);
        else
            latLng = new LatLng(latitude, longitude);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        // 控制屏幕将定点移动到中央
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    // 位置中心监听
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
//            latLng = new LatLng(latitude, longitude);
            // 构造定位数据
            MyLocationData locData;
            if(gpsFlag) {
                locData = new MyLocationData.Builder()
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .accuracy(location.getRadius())
                        .direction(100)
                        .latitude(latitude1)
                        .longitude(longitude1)
                        .build();

                // 画点
                getPoint(target);
            } else {
                locData = new MyLocationData.Builder()
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .accuracy(location.getRadius())
                        .direction(100)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();
            }
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                /*latLng = new LatLng(latitude, longitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(18.0f);
                // 控制屏幕将定点移动到中央
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/
                getPingMuCenter();

                Toast.makeText(getApplicationContext(), "到定位方法里面了", Toast.LENGTH_SHORT).show();

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(getApplicationContext(), location.getAddrStr() + " - GPS定位", Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(getApplicationContext(), location.getAddrStr() + " - 网络定位", Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(getApplication(), location.getAddrStr() + " - 离线定位", Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(getApplication(), "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(getApplication(), "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(getApplication(), "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getCenterImg(){
        /**
         * 中心定位
         */
        mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); //设置地图类型可选择基础地图，卫星地图和空白地图
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());

        // 配置定位SDK参数
        initLocation();
        // 注册监听函数
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        // 开启定位
        mLocationClient.start();
        // 图片点击事件，回到定位点
        mLocationClient.requestLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("text2", "onResume");
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        getCenterImg();
        /*latLng = new LatLng(latitude, longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        // 控制屏幕将定点移动到中央
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/
    }

    /**private void getPoint() {
        coordinateConvert();
        builder = new MapStatus.Builder();
        builder.target(target).zoom(18f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        // 地图标记覆盖物参数配置类
        MarkerOptions oStart = new MarkerOptions();
        // 覆盖物位置点，第一个点为起点
        oStart.position(latLngs.get(0));
        // 设置覆盖物图片
        oStart.icon(startBD);
        // 设置覆盖物Index
        oStart.zIndex(1);
        //
        mMarker = (Marker) mBaiduMap.addOverlay(oStart);
    }*/

    /**
     * 将google地图的wgs84坐标转化为百度地图坐标
     */
    /**private void  coordinateConvert(){
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordType.COMMON);
        double lanSum = 0;
        double lonSum = 0;
        for (int i = 0; i < Const.googleWGS84.length; i++) {
            String[] ll = Const.googleWGS84[i].split(",");
            LatLng sourceLatLng = new LatLng(Double.valueOf(ll[0]), Double.valueOf(ll[1]));
            converter.coord(sourceLatLng);
            LatLng desLatLng = converter.convert();
            latLngs.add(desLatLng);
            Log.d(desLatLng.toString(), "经纬度");
            lanSum += desLatLng.latitude;
            lonSum += desLatLng.longitude;
        }
        target = new LatLng(lanSum/latLngs.size(), lonSum/latLngs.size());
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("text2", "onPause");
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("text2", "onStop");
        guanBiGpsJianTing();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("text2", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("text2", "onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}
