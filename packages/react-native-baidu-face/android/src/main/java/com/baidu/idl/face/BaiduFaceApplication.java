package com.baidu.idl.face;

import android.app.Application;

import com.baidu.idl.face.model.Const;
import com.baidu.idl.face.platform.LivenessTypeEnum;

import java.util.Arrays;
import java.util.List;

public class BaiduFaceApplication extends Application {
    // 动作活体条目集合
    public static List<LivenessTypeEnum> livenessList = Arrays.asList(
            LivenessTypeEnum.Eye,
            LivenessTypeEnum.Mouth,
            LivenessTypeEnum.HeadRight,
            LivenessTypeEnum.HeadLeft,
            LivenessTypeEnum.HeadUp,
            LivenessTypeEnum.HeadDown
    );
    // 活体随机开关
    public static boolean isLivenessRandom = true;
    // 语音播报开关
    public static boolean isOpenSound = true;
    // 活体检测开关
    public static boolean isActionLive = true;
    // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
    public static int qualityLevel = Const.QUALITY_NORMAL;

}
