package com.baidu.idl.face;

import android.content.Context;
import com.baidu.idl.face.BuildConfig;
import com.baidu.idl.face.platform.FaceSDKManager;

public class BaiduFace {

    public static void init(Context context) {
        FaceSDKManager.getInstance().initialize(context, BuildConfig.LICENSE_ID, BuildConfig.LICENSE_FILE_NAME);
    }
}
