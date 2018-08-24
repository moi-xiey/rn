package com.moi.rn;

import com.facebook.react.bridge.*;
import com.moi.rn.util.CoordinateTransform;

public class CoordinateModule extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "Coordinate";
    }

    public CoordinateModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void wgs84tobd09(ReadableMap param, Callback callback) {
        double lng = param.getDouble("longitude");
        double lat = param.getDouble("latitude");
        double[] result = CoordinateTransform.wgs84tobd09(lng, lat);
        WritableMap data = Arguments.createMap();
        data.putDouble("longitude", result[0]);
        data.putDouble("latitude", result[1]);
        callback.invoke(data);
    }

    @ReactMethod
    public void wgs84togcj02(ReadableMap param, Callback callback) {
        double lng = param.getDouble("longitude");
        double lat = param.getDouble("latitude");
        double[] result = CoordinateTransform.wgs84togcj02(lng, lat);
        WritableMap data = Arguments.createMap();
        data.putDouble("longitude", result[0]);
        data.putDouble("latitude", result[1]);
        callback.invoke(data);
    }

    @ReactMethod
    public void gcj02tobd09(ReadableMap param, Callback callback) {
        double lng = param.getDouble("longitude");
        double lat = param.getDouble("latitude");
        double[] result = CoordinateTransform.gcj02tobd09(lng, lat);
        WritableMap data = Arguments.createMap();
        data.putDouble("longitude", result[0]);
        data.putDouble("latitude", result[1]);
        callback.invoke(data);
    }

    @ReactMethod
    public void gcj02towgs84(ReadableMap param, Callback callback) {
        double lng = param.getDouble("longitude");
        double lat = param.getDouble("latitude");
        double[] result = CoordinateTransform.gcj02towgs84(lng, lat);
        WritableMap data = Arguments.createMap();
        data.putDouble("longitude", result[0]);
        data.putDouble("latitude", result[1]);
        callback.invoke(data);
    }

    @ReactMethod
    public void bd09togcj02(ReadableMap param, Callback callback) {
        double lng = param.getDouble("longitude");
        double lat = param.getDouble("latitude");
        double[] result = CoordinateTransform.bd09togcj02(lng, lat);
        WritableMap data = Arguments.createMap();
        data.putDouble("longitude", result[0]);
        data.putDouble("latitude", result[1]);
        callback.invoke(data);
    }

    @ReactMethod
    public void bd09towgs84(ReadableMap param, Callback callback) {
        double lng = param.getDouble("longitude");
        double lat = param.getDouble("latitude");
        double[] result = CoordinateTransform.bd09towgs84(lng, lat);
        WritableMap data = Arguments.createMap();
        data.putDouble("longitude", result[0]);
        data.putDouble("latitude", result[1]);
        callback.invoke(data);
    }
}
