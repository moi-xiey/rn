import { NativeModules, Platform } from 'react-native';

const unSupport = () => console.warn('该平台不支持');
const BaiduFace = Platform.select({
    android: NativeModules.BaiduFace,
    ios: {
        detect: unSupport,
        liveness: unSupport,
        setting: unSupport,
        config: unSupport,
    }
});

export default {
    /**
     * 人脸检测后会自动拍照
     */
    detect: BaiduFace.detect,
    /**
     * 活体检测
     */
    liveness: BaiduFace.liveness,
    /**
     * 人像配置(界面)
     */
    setting: BaiduFace.setting,
    /**
     * 人像配置(代码)
     */
    config: BaiduFace.config,
};
