package com.baidu.idl.face;

import android.app.Activity;
import android.content.Intent;

import com.baidu.idl.face.activity.FaceDetectExpActivity;
import com.baidu.idl.face.activity.FaceLivenessExpActivity;
import com.baidu.idl.face.manager.QualityConfigManager;
import com.baidu.idl.face.model.Const;
import com.baidu.idl.face.model.QualityConfig;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.listener.IInitCallback;
import com.baidu.idl.face.utils.SharedPreferencesUtil;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Random;

public class BaiduFaceModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final String TAG = BaiduFaceModule.class.getSimpleName();

    private static final int REQUEST_DETECT_CODE = 3101;
    private static final int REQUEST_LIVENESS_CODE = 3102;

    private final ReactApplicationContext reactContext;

    private boolean mIsInitSuccess;

    public BaiduFaceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "BaiduFace";
    }

    @ReactMethod
    public void config(String licenseID, String licenseFile) {
        boolean success = setFaceConfig();
        if (!success) {
            mIsInitSuccess = false;
            WritableMap result = Arguments.createMap();
            result.putBoolean("success", false);
            result.putString("message", "初始化失败(json配置文件解析出错)");
            result.putInt("code", 10001);
            sendEvent("config", result);
            return;
        }
        FaceSDKManager.getInstance().initialize(this.reactContext, licenseID, licenseFile, new IInitCallback() {
            @Override
            public void initSuccess() {
                reactContext.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsInitSuccess = true;
                        WritableMap result = Arguments.createMap();
                        result.putBoolean("success", true);
                        result.putString("message", "初始化成功");
                        sendEvent("config", result);
                    }
                });
            }

            @Override
            public void initFailure(final int errCode, final String errMsg) {
                reactContext.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsInitSuccess = false;
                        WritableMap result = Arguments.createMap();
                        result.putBoolean("success", false);
                        result.putString("message", "初始化失败");
                        result.putInt("code", errCode);
                        sendEvent("config", result);
                    }
                });
            }
        });
    }

    /**
     * 打开人像自动检测拍照
     */
    @ReactMethod
    public void detect() {
        if (!mIsInitSuccess) {
            sendFailEvent("初始化失败", -1);
            return;
        }
        startActivityForResult(FaceDetectExpActivity.class, REQUEST_DETECT_CODE);
    }

    /**
     * 打开活体检测
     */
    @ReactMethod
    public void liveness() {
        if (!mIsInitSuccess) {
            sendFailEvent("初始化失败", -1);
            return;
        }
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        Random random = new Random();
        int r = random.nextInt(BaiduFaceApplication.livenessList.size());
        config.setLivenessTypeList(Collections.singletonList(BaiduFaceApplication.livenessList.get(r)));
        config.setLivenessRandom(false);
        FaceSDKManager.getInstance().setFaceConfig(config);
        startActivityForResult(FaceLivenessExpActivity.class, REQUEST_LIVENESS_CODE);
    }

    private void startActivityForResult(Class<?> cls, int code) {
        Intent intent = new Intent(reactContext, cls);
        try {
            reactContext.startActivityForResult(intent, code, null);
        } catch (Exception ignore) {
            sendFailEvent("验证错误", 1);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        try {
            if (null != data) {
                WritableMap result = Arguments.createMap();
                boolean success = data.getBooleanExtra("success", false);
                String image = data.getStringExtra("image");
                String message = data.getStringExtra("message");
                result.putString("data", image);
                result.putString("message", message);
                result.putBoolean("success", success);
                sendEvent("complete", result);
            } else {
                sendFailEvent("验证错误", 3);
            }
        } catch (Exception ignore) {
            sendFailEvent("验证错误", 2);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    /**
     * 参数配置方法
     */
    private boolean setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），也可以根据实际需求进行数值调整
        // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
        // 获取保存的质量等级
        SharedPreferencesUtil util = new SharedPreferencesUtil(reactContext);
        int qualityLevel = (int) util.getSharedPreference(Const.KEY_QUALITY_LEVEL_SAVE, -1);
        if (qualityLevel == -1) {
            qualityLevel = BaiduFaceApplication.qualityLevel;
        }
        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(reactContext.getApplicationContext(), qualityLevel);
        QualityConfig qualityConfig = manager.getConfig();
        if (qualityConfig == null) {
            return false;
        }
        // 设置模糊度阈值
        config.setBlurnessValue(qualityConfig.getBlur());
        // 设置最小光照阈值（范围0-255）
        config.setBrightnessValue(qualityConfig.getMinIllum());
        // 设置最大光照阈值（范围0-255）
        config.setBrightnessMaxValue(qualityConfig.getMaxIllum());
        // 设置左眼遮挡阈值
        config.setOcclusionLeftEyeValue(qualityConfig.getLeftEyeOcclusion());
        // 设置右眼遮挡阈值
        config.setOcclusionRightEyeValue(qualityConfig.getRightEyeOcclusion());
        // 设置鼻子遮挡阈值
        config.setOcclusionNoseValue(qualityConfig.getNoseOcclusion());
        // 设置嘴巴遮挡阈值
        config.setOcclusionMouthValue(qualityConfig.getMouseOcclusion());
        // 设置左脸颊遮挡阈值
        config.setOcclusionLeftContourValue(qualityConfig.getLeftContourOcclusion());
        // 设置右脸颊遮挡阈值
        config.setOcclusionRightContourValue(qualityConfig.getRightContourOcclusion());
        // 设置下巴遮挡阈值
        config.setOcclusionChinValue(qualityConfig.getChinOcclusion());
        // 设置人脸姿态角阈值
        config.setHeadPitchValue(qualityConfig.getPitch());
        config.setHeadYawValue(qualityConfig.getYaw());
        config.setHeadRollValue(qualityConfig.getRoll());
        // 设置可检测的最小人脸阈值
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        // 设置可检测到人脸的阈值
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 设置闭眼阈值
        config.setEyeClosedValue(FaceEnvironment.VALUE_CLOSE_EYES);
        // 设置图片缓存数量
        config.setCacheImageNum(FaceEnvironment.VALUE_CACHE_IMAGE_NUM);
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(BaiduFaceApplication.livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(BaiduFaceApplication.isLivenessRandom);
        // 设置开启提示音
        config.setSound(BaiduFaceApplication.isOpenSound);
        // 原图缩放系数
        config.setScale(FaceEnvironment.VALUE_SCALE);
        // 抠图宽高的设定，为了保证好的抠图效果，建议高宽比是4：3
        config.setCropHeight(FaceEnvironment.VALUE_CROP_HEIGHT);
        config.setCropWidth(FaceEnvironment.VALUE_CROP_WIDTH);
        // 抠图人脸框与背景比例
        config.setEnlargeRatio(FaceEnvironment.VALUE_CROP_ENLARGERATIO);
        // 加密类型，0：Base64加密，上传时image_sec传false；1：百度加密文件加密，上传时image_sec传true
        config.setSecType(FaceEnvironment.VALUE_SEC_TYPE);
        // 检测超时设置
        config.setTimeDetectModule(FaceEnvironment.TIME_DETECT_MODULE);
        // 检测框远近比率
        config.setFaceFarRatio(FaceEnvironment.VALUE_FAR_RATIO);
        config.setFaceClosedRatio(FaceEnvironment.VALUE_CLOSED_RATIO);
        FaceSDKManager.getInstance().setFaceConfig(config);
        return true;
    }

    private void sendFailEvent(String message, Integer code) {
        WritableMap result = Arguments.createMap();
        result.putInt("code", code);
        result.putBoolean("success", false);
        result.putString("message", message);
        sendEvent("complete", result);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (null == reactContext) {
            return;
        }
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
