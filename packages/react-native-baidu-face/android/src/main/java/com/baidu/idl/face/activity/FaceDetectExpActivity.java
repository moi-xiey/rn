package com.baidu.idl.face.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.BaiduFaceModule;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;

import java.util.HashMap;

public class FaceDetectExpActivity extends FaceDetectActivity {

    private Boolean success = false;
    private HashMap<String, String> images = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            success = true;
            images = base64ImageMap;
            finish();
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_StartPreview ||
                status == FaceStatusEnum.Error_Timeout) {
            success = false;
            images = new HashMap<>();
            BaiduFaceModule.message = message;
            finish();
        }
    }

    @Override
    public void finish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("success", success);
        setResult(success ? Activity.RESULT_OK : Activity.RESULT_CANCELED, resultIntent);
        BaiduFaceModule.images = images;
        super.finish();
    }

}
