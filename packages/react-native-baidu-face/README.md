# React Native 百度人像活体检测

### 使用前准备
- 创建授权[人脸识别 - 离线SDK管理]. [参考](https://ai.baidu.com/docs#/Face-Android-SDK/d4035af4)

### 安装步骤
- `yarn add react-native-baidu-face`
- `react-native link react-native-baidu-face`
- <details>
    <summary>Android 步骤</summary>

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
  </details>
- <details>
    <summary>iOS 步骤</summary>

    - xcode打开项目, 在 Frameworks 文件右键 Add Files to "xxx", 在弹出的框中选择 ${项目根目录}/node_modules/react-native-baidu-face/ios/IDLFaceSDK.framework 文件, 点击 add 按钮
    - 确认 General/Identity/Bundle Identifier 与创建授权时填入的要一致
    - General/Embedded Binaries 下点击 + 号, 在弹窗中选中 Frameworks/IDLFaceSDK.framework, 点击 add 按钮
    - General/Linked Frameworks and Libraries 下点击 + 号, 选择 libc++.tbd, 点击 add 按钮
    - Build Settings/Search Paths/Framework Search Paths 添加 $(SRCROOT)/../node_modules/react-native-baidu-face/ios(选择recursive)
    - Build Settings/Search Paths/Library Search Paths 添加 $(inherited) 和 $(SRCROOT)/../node_modules/react-native-baidu-face/ios(选择recursive)
    - Build Phases/Copy Bundle Resources 下点击 + 号, 在弹框中点击 Add Other... 按钮, 在弹出的框中跳转到 ${项目根目录}/node_modules/react-native-baidu-face/ios/ 目录选择添加 com.baidu.idl.face.faceSDK.bundle 和 com.baidu.idl.face.model.bundle 文件
    - 把 license 文件的名称改为 idl-license.face-ios, 然后按照上一个步骤把该文件添加到 Build Phases/Copy Bundle Resources 中
    - 在 info.plist 中添加 NSCameraUsageDescription
    - 在 info.plist 中添加 BAIDU_FACE_LICENSE_ID, 值为*创建授权*是填入的*授权标识*
    - 在 AppDelegate.m 中
    ```objective-c
    #import "BaiduFace.h" // 引入头文件

    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
        [BaiduFace initSDK]; // 初始化人像SDK
    }

    ```
  </details>

### 使用
```javascript
import BaiduFace from "react-native-baidu-face";

// 人脸跟踪检测
BaiduFace.detect();

// 活体检测
BaiduFace.liveness()
```
