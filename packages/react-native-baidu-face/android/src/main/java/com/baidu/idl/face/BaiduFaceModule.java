package com.baidu.idl.face;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.baidu.idl.face.activity.FaceDetectExpActivity;
import com.baidu.idl.face.activity.FaceLivenessExpActivity;
import com.baidu.idl.face.activity.SettingsActivity;
import com.facebook.react.bridge.*;

import java.util.HashMap;
import java.util.Map;

public class BaiduFaceModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final String TAG = BaiduFaceModule.class.getSimpleName();

    private static final String MODULE_NAME = "BaiduFace";

    private static final int REQUEST_DETECT_CODE = 3101;
    private static final int REQUEST_LIVENESS_CODE = 3102;
    private static final int REQUEST_SETTING_CODE = 3103;

    private ReactApplicationContext context;
    private Promise p;

    public static HashMap<String, String> images = null;

    public BaiduFaceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        WritableMap resolveData = Arguments.createMap();
        if (null != data) {
            Boolean success = data.getBooleanExtra("success", false);
            resolveData.putBoolean("success", success);
        }
        if (null != images) {
            WritableMap imageData = Arguments.createMap();
            for (Map.Entry<String, String> entry : images.entrySet()) {
                imageData.putString(entry.getKey(), entry.getValue());
            }
            resolveData.putMap("images", imageData);
        }
        p.resolve(resolveData);
        images = null;
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    /**
     * 打开人像自动检测拍照
     */
    @ReactMethod
    public void detect(Promise promise) {
        startActivityForResult(FaceDetectExpActivity.class, REQUEST_DETECT_CODE);
        p = promise;
    }

    /**
     * 打开活体检测
     */
    @ReactMethod
    public void liveness(Promise promise) {
        startActivityForResult(FaceLivenessExpActivity.class, REQUEST_LIVENESS_CODE);
        p = promise;
    }

    /**
     * 打开活体检测设置
     */
    @ReactMethod
    public void setting(Promise promise) {
        startActivityForResult(SettingsActivity.class, REQUEST_SETTING_CODE);
        p = promise;
    }

    /**
     * 配置项
     */
    @ReactMethod
    public void config(Promise promise) {
        Toast.makeText(context, "人像配置", Toast.LENGTH_SHORT).show();
        p = promise;
    }

    private void startActivityForResult(Class<?> cls, int code) {
        images = null;
        Intent intent = new Intent(context, cls);
        context.startActivityForResult(intent, code, null);
    }
}
