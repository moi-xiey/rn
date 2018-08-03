# React Native 百度人像活体检测

### 使用前准备
- 创建授权[人脸识别 - 离线SDK管理]. [参考](https://ai.baidu.com/docs#/Face-Android-SDK/d4035af4)

### 安装步骤
- `yarn add react-native-baidu-face`
- `react-native link react-native-baidu-face`
- 修改包名(AndroidManifest.xml 的 package 和 android/app/build.gradle > android > defaultConfig > applicationId 填入创建授权时输入的 android 包名)
- 在 android/gradle.properties 文件中配置
```profile
...
BAIDU_FACE_SDK_LICENSE_ID="*license*"
BAIDU_FACE_SDK_LICENSE_FILE_NAME="idl-license.face-android" # 可不配, 默认为 idl-license.face-android
...
```
- 在 MainApplication.java 中添加
```java
import com.baidu.idl.face.BaiduFace; /* 在顶部添加 */
...
@Override
public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    BaiduFace.init(this); /* 在 onCreate 中添加 */
}
```
- 把 license 文件放入 android/app/src/main/assets 目录

### 使用
```javascript
import BaiduFace from "react-native-baidu-face";

// 人脸跟踪检测
BaiduFace.detect();

// 活体检测
BaiduFace.liveness()
```
