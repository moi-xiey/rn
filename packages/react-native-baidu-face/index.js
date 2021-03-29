import { NativeModules, NativeEventEmitter } from 'react-native';

const SDK = NativeModules.BaiduFace;
const eventEmitter = new NativeEventEmitter(SDK);

const doCapture = (method) => {
  return new Promise(resolve => {
    const subscript = eventEmitter.addListener('complete', (result) => {
      resolve(result);
      subscript.remove();
    });
    SDK[method]();
  });
};

export default {
  config: (licenseID, licenseFile) => {
    return new Promise(resolve => {
      const subscript = eventEmitter.addListener('config', (result) => {
        resolve(result);
        subscript.remove();
      });
      SDK.config(licenseID, licenseFile);
    });
  },
  detect: () => {
    return doCapture('detect');
  },
  liveness: () => {
    return doCapture('liveness');
  },
};
