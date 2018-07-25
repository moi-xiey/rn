package com.baidu.idl.face.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.BaiduFaceModule;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;

import java.util.HashMap;

public class FaceLivenessExpActivity extends FaceLivenessActivity {

    private Boolean success = false;
    private HashMap<String, String> images = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            success = true;
            images = base64ImageMap;
            Toast.makeText(this, "活体检测-检测成功", Toast.LENGTH_SHORT).show();
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            Toast.makeText(this, "活体检测-采集超时", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("success", success);
        setResult(Activity.RESULT_OK, resultIntent);
        BaiduFaceModule.images = images;
        super.finish();
    }

}
