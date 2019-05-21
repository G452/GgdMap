//package test.mfy.com.gdmaps;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.UiSettings;
//import com.amap.api.maps.model.BitmapDescriptor;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.Poi;
//import com.amap.api.navi.AmapNaviPage;
//import com.amap.api.navi.AmapNaviParams;
//import com.amap.api.navi.AmapNaviType;
//import com.amap.api.navi.AmapPageType;
//import com.amap.api.navi.INaviInfoCallback;
//import com.amap.api.navi.model.AMapNaviLocation;
//import com.amap.api.services.core.AMapException;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.route.BusRouteResult;
//import com.amap.api.services.route.DrivePath;
//import com.amap.api.services.route.DriveRouteResult;
//import com.amap.api.services.route.RideRouteResult;
//import com.amap.api.services.route.RouteSearch;
//import com.amap.api.services.route.WalkRouteResult;
//
//public class MapHelper {
//    private Activity mActivity;
//    //地图控件
//    private com.amap.api.maps.MapView mGMapView = null;
//    private AMap agMap = null;
//    private LocationSource.OnLocationChangedListener mListener = null;
//    private AMapLocationClient mLocationClient = null;
//    boolean isFirstLoc = true; // 是否首次定位
//    private boolean isFirstIn = false;
//    //路线规划
//    private RouteSearch mRouteSearch;
//    private DriveRouteResult mDriveRouteResult;
//    private DrivingARouteAOverlay drivingARouteOverlay;
//    private final int ROUTE_TYPE_DRIVE = 2;
//    //初始化高德地图全局bitmap信息，不用时及时回收
//    private BitmapDescriptor charge_station_normala;
//    private BitmapDescriptor charge_station_presseda;
//    private BitmapDescriptor charge_station_pre_normala;
//    private BitmapDescriptor charge_station_pre_presseda;
//    private BitmapDescriptor charge_station_bad_normala;
//    private BitmapDescriptor charge_station_bad_presseda;
//    private BitmapDescriptor bdFa;
//    private BitmapDescriptor bdEa;
//
//    public MapHelper(Activity mActivity, Bundle savedInstanceState, com.amap.api.maps.MapView mGMapView, KLocationListener locationListener) {
//        this.mGMapView = mGMapView;
//        this.mActivity = mActivity;
//        mGMapView.onCreate(savedInstanceState);
//        initMap(locationListener);//地图初始化
//        initBitmapDescriptor();//地图蒙层
//    }
//
//    /*
//     * 初始化地图&定位&路线规划
//     * */
//    public void initMap(KLocationListener locationListener) {
//        if (agMap == null) {
//            agMap = mGMapView.getMap();
//            //设置显示定位按钮 并且可以点击
//            agMap.setLocationSource(new LocationSource() {
//                /**
//                 * 激活定位
//                 */
//                @Override
//                public void activate(OnLocationChangedListener onLocationChangedListener) {
//                    mListener = onLocationChangedListener;
//                }
//                @Override
//                public void deactivate() {
//                    mListener = null;
//                }
//            });//设置了定位的监听,这里要实现LocationSource接口
//            // 是否显示定位按钮
//            agMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
//            UiSettings uiSettings = agMap.getUiSettings();//实例化UiSettings
//            uiSettings.setMyLocationButtonEnabled(false);
//            uiSettings.setRotateGesturesEnabled(false);//旋转手势
//            uiSettings.setZoomControlsEnabled(false);//缩放手势
//        }
//
//        //初始化定位
//        mLocationClient = new AMapLocationClient(mActivity);
//        //设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
//        mLocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (aMapLocation != null) {
//                    if (aMapLocation.getErrorCode() == 0) {
//                        // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
//                        if (isFirstLoc) {
//                            //设置缩放级别
//                            agMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//                            //将地图移动到定位点
//                            agMap.moveCamera(CameraUpdateFactory.changeLatLng(new com.amap.api.maps.model.LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
//                            KConstant.latitude = aMapLocation.getLatitude();
//                            KConstant.longitude = aMapLocation.getLongitude();
//                            KConstant.CITY=aMapLocation.getCity();
//                            isFirstIn = true;
//                            isFirstLoc = false;
////                            initData();
//                            locationListener.onLocationChanged(aMapLocation);//接口监听
//                            isFirstIn = true;
//                            //点击定位按钮 能够将地图的中心移动到定位点
//                            mListener.onLocationChanged(aMapLocation);
//                        }
//                    } else {
//                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError", "location Error, ErrCode:"
//                                + aMapLocation.getErrorCode() + ", errInfo:"
//                                + aMapLocation.getErrorInfo());
////                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//        //初始化定位参数
//        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
//
//        //路线规划
//        mRouteSearch = new RouteSearch(mActivity);
//        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
//            @Override
//            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
//
//            }
//            @Override
//            public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
////        agMap.clear();// 清理地图上的所有覆盖物
//                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
//                    if (result != null && result.getPaths() != null) {
//                        if (result.getPaths().size() > 0) {
//                            mDriveRouteResult = result;
//                            final DrivePath drivePath = mDriveRouteResult.getPaths()
//                                    .get(0);
//                            if (drivePath == null) {
//                                return;
//                            }
//                            drivingARouteOverlay = new DrivingARouteAOverlay(
//                                    mActivity, agMap, drivePath,
//                                    mDriveRouteResult.getStartPos(),
//                                    mDriveRouteResult.getTargetPos(), null,R.drawable.ic_charge_guowang);
//                            drivingARouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
//                            drivingARouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
//                            drivingARouteOverlay.removeFromMap();
//                            drivingARouteOverlay.addToMap(R.drawable.ic_charge_guowang);
//                            drivingARouteOverlay.zoomToSpan();
//
////                    int dis = (int) drivePath.getDistance();
////                    int dur = (int) drivePath.getDuration();
////                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
////                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
//
//                        } else if (result != null && result.getPaths() == null) {
//                            Toast.makeText(mActivity, "定位中，终点未no_result设置", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(mActivity, "定位中，终点未no_result设置", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(mActivity, "errorCode", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
//
//            }
//            @Override
//            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
//
//            }
//        });
//
//        //地图设置点击事件
//        agMap.setOnMapClickListener(latLng -> {
//            locationListener.setOnMapClickListener(latLng);//点击监听
//        });
//
//    }
//
//    /*
//     * 点击事件接口监听
//     * */
//    public interface KLocationListener {
//        void onLocationChanged(AMapLocation aMapLocation);
//        void setOnMapClickListener(LatLng latLng);
//    }
//
//    /**
//     * 高德
//     * 开始搜索路径规划方案
//     */
//    public void searchRouteResult(int routeType, int mode, LatLonPoint mEndPoint) {
//        LatLonPoint mStartPoint = new LatLonPoint(KConstant.latitude, KConstant.longitude);//起点，39.942295,116.335891
//        if (mStartPoint == null) {
//            Toast.makeText(mActivity, "定位中，稍后再试", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (mEndPoint == null) {
//            Toast.makeText(mActivity, "定位中，终点未设置", Toast.LENGTH_SHORT).show();
//        }
//        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
//                mStartPoint, mEndPoint);
//        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
//            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
//                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
//        }
//    }
//
//    /**
//     * 高德导航
//     * 传入起点经纬度
//     *
//     * @param alatitude
//     * @param alongitude
//     */
//    public void startAMapNavigational(double alatitude, double alongitude, double endalatitude, double endalongitude) {
//        com.amap.api.maps.model.LatLng startLatLng = new com.amap.api.maps.model.LatLng(alatitude, alongitude);
//        com.amap.api.maps.model.LatLng endLatLng = new com.amap.api.maps.model.LatLng(endalatitude, endalongitude);
//        Poi start = new Poi("起点", startLatLng, "");//起点
//        Poi end = new Poi("终点", endLatLng, "");//终点
//        AmapNaviParams amapNaviParams = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI);
//        amapNaviParams.setUseInnerVoice(true);
//        AmapNaviPage.getInstance().showRouteActivity(mActivity, amapNaviParams, new INaviInfoCallback() {
//            @Override
//            public void onInitNaviFailure() {
//            }
//            @Override
//            public void onGetNavigationText(String s) {
//            }
//            @Override
//            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
//            }
//            @Override
//            public void onArriveDestination(boolean b) {
//            }
//            @Override
//            public void onStartNavi(int i) {
//            }
//            @Override
//            public void onCalculateRouteSuccess(int[] ints) {
//            }
//            @Override
//            public void onCalculateRouteFailure(int i) {
//            }
//            @Override
//            public void onStopSpeaking() {
//            }
//            @Override
//            public void onReCalculateRoute(int i) {
//            }
//            @Override
//            public void onExitPage(int i) {
//            }
//            @Override
//            public void onStrategyChanged(int i) {
//            }
//            @Override
//            public View getCustomNaviBottomView() {
//                return null;
//            }
//            @Override
//            public View getCustomNaviView() {
//                return null;
//            }
//            @Override
//            public void onArrivedWayPoint(int i) {
//            }
//        });
//    }
//
//    /**
//     * 重置marker图片
//     */
//    public void resetMarker(Marker lastMarker) {
//        if (lastMarker != null) {
//            if (lastMarker.getIcons().get(0) == charge_station_presseda) {
//                lastMarker.setIcon(charge_station_normala);
//            } else if (lastMarker.getIcons().get(0) == charge_station_pre_presseda) {
//                lastMarker.setIcon(charge_station_pre_normala);
//            } else if (lastMarker.getIcons().get(0) == charge_station_bad_presseda) {
//                lastMarker.setIcon(charge_station_bad_normala);
//            } else if (lastMarker.getIcons().get(0) == bdFa) {
//                lastMarker.setIcon(bdEa);
//            }
//            lastMarker = null;
//        }
//    }
//
//    /*
//     * 地图回收图片
//     * */
//    private void initBitmapDescriptor() {
//        charge_station_normala = BitmapDescriptorFactory.fromResource(R.drawable.charge_station_normal);
//        charge_station_presseda = BitmapDescriptorFactory.fromResource(R.drawable.charge_station_pressed);
//        charge_station_pre_normala = BitmapDescriptorFactory.fromResource(R.drawable.charge_station_pre_normal);
//        charge_station_pre_presseda = BitmapDescriptorFactory.fromResource(R.drawable.charge_station_pre_pressed);
//        charge_station_bad_normala = BitmapDescriptorFactory.fromResource(R.drawable.charge_station_bad_normal);
//        charge_station_bad_presseda = BitmapDescriptorFactory.fromResource(R.drawable.charge_station_bad_pressed);
//        bdFa = BitmapDescriptorFactory.fromResource(R.drawable.parking_map_click);
//        bdEa = BitmapDescriptorFactory.fromResource(R.drawable.parking_map);
//    }
//
//    public void onCreate(Bundle savedInstanceState) {
//        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
//        mGMapView.onCreate(savedInstanceState);
//    }
//
//    public void onSaveInstanceState(Bundle outState) {
//        mGMapView.onSaveInstanceState(outState);
//    }
//
//    public void onResume() {
//        mLocationClient.startLocation();
//        mGMapView.onResume();
//    }
//
//    public void onPause() {
//        mLocationClient.stopLocation();
//        mGMapView.onPause();
//    }
//
//    public void onDestroy() {
//        mLocationClient.stopLocation();//停止定位
//        mLocationClient.onDestroy();//销毁定位客户端。
//        mGMapView.onDestroy();
//        // 回收 bitmap 资源
//        charge_station_normala.recycle();
//        charge_station_presseda.recycle();
//        charge_station_pre_normala.recycle();
//        charge_station_pre_presseda.recycle();
//        charge_station_bad_normala.recycle();
//        charge_station_bad_presseda.recycle();
//
//        charge_station_pre_normala = null;
//        charge_station_pre_presseda = null;
//        charge_station_normala = null;
//        charge_station_presseda = null;
//        charge_station_bad_normala = null;
//        charge_station_bad_presseda = null;
//    }
//
//
//    public BitmapDescriptor getCharge_station_normala() {
//        return charge_station_normala;
//    }
//
//    public BitmapDescriptor getCharge_station_presseda() {
//        return charge_station_presseda;
//    }
//
//    public BitmapDescriptor getCharge_station_pre_normala() {
//        return charge_station_pre_normala;
//    }
//
//    public BitmapDescriptor getCharge_station_pre_presseda() {
//        return charge_station_pre_presseda;
//    }
//
//    public BitmapDescriptor getCharge_station_bad_normala() {
//        return charge_station_bad_normala;
//    }
//
//    public BitmapDescriptor getCharge_station_bad_presseda() {
//        return charge_station_bad_presseda;
//    }
//
//    public BitmapDescriptor getBdFa() {
//        return bdFa;
//    }
//
//    public BitmapDescriptor getBdEa() {
//        return bdEa;
//    }
//
//    public AMap getAgMap() {
//        return agMap;
//    }
//
//    public void setFirstLoc(boolean firstLoc) {
//        isFirstLoc = firstLoc;
//    }
//
//    public boolean isFirstIn() {
//        return isFirstIn;
//    }
//
//    public DrivingARouteAOverlay getDrivingARouteOverlay() {
//        return drivingARouteOverlay;
//    }
//
//    public int getROUTE_TYPE_DRIVE() {
//        return ROUTE_TYPE_DRIVE;
//    }
//}
