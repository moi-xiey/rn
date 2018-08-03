import { NativeModules, Platform } from 'react-native';

const unSupport = () => console.warn('该平台不支持');
const BaiduFace = Platform.select({
    android: NativeModules.BaiduFace,
    ios: {
        detect: unSupport,
        liveness: unSupport,
        setting: unSupport,
        config: unSupport,
        LivenessType: {},
    }
});

export default {
    /**
     * 人脸检测后会自动拍照
     */
    detect: () => BaiduFace.detect(),
    /**
     * 活体检测
     */
    liveness: () => BaiduFace.liveness(),
    /**
     * 人像配置(界面)
     */
    setting: () => BaiduFace.setting(),
    /**
     * 人像配置(代码)
     *
     * @param {Object} opt 配置
     * @param {Boolean} [opt.livenessRandom] 随机活体动作
     * @param {Array.<LivenessType>} [opt.livenessTypeList] 活体动作
     */
    config: (opt) => BaiduFace.config(opt),
    /**
     * 活体类型常量
     *
     * Eye: 眨眼
     * Mouth: 张嘴
     * HeadUp: 抬头
     * HeadDown: 低头
     * HeadLeft: 向左转头
     * HeadRight: 向右转头
     * HeadLeftOrRight: 摇头
     */
    LivenessType: BaiduFace.LivenessType
};
