import { NativeModules, NativeEventEmitter } from 'react-native';

const SDK = NativeModules.BaiduFace;
const eventEmitter = new NativeEventEmitter(SDK);

const doCapture = (method) => {
    return new Promise(resolve => {
        const subscript = eventEmitter.addEventListener('complete', (data) => {
            if (!data) {
                resolve({ success: false });
                subscript.remove();
                return;
            }
            data.bestImage0 = Array.isArray(data.bestImage) ? data.bestImage[0] : '';
            resolve({ success: !!data.success, images: data });
            subscript.remove();
        });
        SDK[method]();
    });
};

const BaiduFace = {
    detect: () => {
        return doCapture('detect');
    },
    liveness: () => {
        return doCapture('liveness');
    },
    config: (opt) => {
        return new Promise(resolve => {
            const subscript = eventEmitter.addEventListener('complete', (data) => {
                resolve(true);
                subscript.remove();
            });
            SDK.config(opt);
        });
    },
    LivenessType: SDK.LivenessType,
};

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
     * 人像配置(代码)
     *
     * @param {Object} opt 配置
     * @param {Boolean} [opt.livenessRandom] 随机活体动作
     * @param {Number} [opt.livenessRandomCount] 随机活体检测动作数
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
