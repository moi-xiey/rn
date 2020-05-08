declare module '@moi-xiey/react-native-baidu-face' {

  interface CompleteParams {
    success: boolean,
    images?: {
      bestImage0?: string,
      bestImage?: string,
      liveEye?: string,
      liveMouth?: string,
      yawRight?: string,
      yawLeft?: string,
      pitchUp?: string,
      pitchDown?: string,
    },
  }

  interface LivenessType {
    Eye,
    Mouth,
    HeadLeft,
    HeadRight,
    HeadLeftOrRight,
    HeadUp,
    HeadDown
  }

  interface ConfigOptions {
    livenessRandom: boolean,
    livenessRandomCount: number,
    livenessTypeList: LivenessType[],
  }

  export default class BaiduFace {
    static detect: () => Promise<CompleteParams>;

    static liveness: () => Promise<CompleteParams>;

    static config: (opt: ConfigOptions) => Promise<boolean>;

    static LivenessType: LivenessType;
  }
}
