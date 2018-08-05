package com.baidu.idl.face;

import android.app.Activity;
import android.content.Intent;
import com.baidu.idl.face.activity.FaceDetectExpActivity;
import com.baidu.idl.face.activity.FaceLivenessExpActivity;
import com.baidu.idl.face.activity.SettingsActivity;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.facebook.react.bridge.*;

import javax.annotation.Nullable;
import java.util.*;

public class BaiduFaceModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final String TAG = BaiduFaceModule.class.getSimpleName();

    private static final String MODULE_NAME = "BaiduFace";

    private static final String LIVENESS_RANDOM = "livenessRandom";
    private static final String LIVENESS_TYPE_LIST = "livenessTypeList";
    private static final String LIVENESS_RANDOM_COUNT = "livenessRandomCount";

    private static final int REQUEST_DETECT_CODE = 3101;
    private static final int REQUEST_LIVENESS_CODE = 3102;
    private static final int REQUEST_SETTING_CODE = 3103;

    private ReactApplicationContext context;
    private FaceConfig faceConfig;
    private Promise p = null;

    public static HashMap<String, String> images = null;

    public BaiduFaceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
        faceConfig = FaceSDKManager.getInstance().getFaceConfig();
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (p == null) {
            return;
        }
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
        p = null;
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        return Collections.unmodifiableMap(new HashMap<String, Object>() {
            {
                put("LivenessType", getTypeConstants());
            }

            private Map<String, Object> getTypeConstants() {
                Map<String, Object> map = new HashMap<>();
                for (LivenessTypeEnum i : LivenessTypeEnum.values()) map.put(i.name(), i.name());
                return Collections.unmodifiableMap(map);
            }
        });
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
    public void config(ReadableMap config, Promise promise) {
        try {
            if (config.hasKey(LIVENESS_RANDOM) && config.getType(LIVENESS_RANDOM).equals(ReadableType.Boolean)) {
                Boolean livenessRandom = config.getBoolean(LIVENESS_RANDOM);
                faceConfig.setLivenessRandom(livenessRandom);
            }
            if (config.hasKey(LIVENESS_TYPE_LIST) && config.getType(LIVENESS_TYPE_LIST).equals(ReadableType.Array)) {
                List<String> stringList = new ArrayList<>();
                for (LivenessTypeEnum type : Arrays.asList(LivenessTypeEnum.values())) {
                    stringList.add(type.name());
                }
                List<LivenessTypeEnum> list = new ArrayList<>();
                ReadableArray array = config.getArray(LIVENESS_TYPE_LIST);
                for (Object o : array.toArrayList()) {
                    if (!stringList.contains(o.toString())) {
                        continue;
                    }
                    list.add(LivenessTypeEnum.valueOf(o.toString()));
                }
                if (list.size() > 0) {
                    faceConfig.setLivenessTypeList(list);
                }
            }
            if (config.hasKey(LIVENESS_RANDOM_COUNT) && config.getType(LIVENESS_RANDOM_COUNT).equals(ReadableType.Number)) {
                Integer count = config.getInt(LIVENESS_RANDOM_COUNT);
                faceConfig.setLivenessRandomCount(count);
            }
            promise.resolve(true);
        } catch (Exception ignore) {
            promise.reject("error", "配置错误");
        }

    }

    private void startActivityForResult(Class<?> cls, int code) {
        images = null;
        Intent intent = new Intent(context, cls);
        context.startActivityForResult(intent, code, null);
    }
}
