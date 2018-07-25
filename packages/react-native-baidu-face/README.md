# React Native 百度人像活体检测

### 使用前准备
- 创建授权[人脸识别 - 离线SDK管理]. [参考](https://ai.baidu.com/docs#/Face-Android-SDK/d4035af4)
### 安装步骤
- `yarn add react-native-baidu-face-sdk`
- `react-native link react-native-baidu-face-sdk`
- 修改包名(AndroidManifest.xml 的 package 和 android/app/build.gradle > android > defaultConfig > applicationId 填入创建授权时输入的 android 包名)
- AndroidManifest.xml 中添加照相等权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.hardware.camera.autofocus" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- 需要使用Feature -->
<uses-feature android:name="android.hardware.camera" android:required="false" />
<uses-feature android:name="android.hardware.camera.front" android:required="false" />
<uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />
<uses-feature android:name="android.hardware.camera.flash" android:required="false" />
```
- 配置打包签名文件. 把 创建授权 时用到的签名文件放到 android/app 目录, 然后在 android/app/build.gradle 文件中配置
```
android {
    ...
    signingConfigs {

        def password = "*password*"
        def alias = "*alias*"
        def filePath = "*.keystore"  //如  ../facesharp.jks//签名文件路径

        debug {
            keyAlias alias
            keyPassword password
            storeFile file(filePath)
            storePassword(password)
        }
        release {
            keyAlias alias
            keyPassword password
            storeFile file(filePath)
            storePassword(password)
        }
    }
    buildTypes {
        debug {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.debug
            debuggable true
            jniDebuggable true
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
            debuggable false
            jniDebuggable false
        }
    }
}
```
- 在 android/gradle.properties 文件中配置
```
...
BAIDU_FACE_SDK_LICENSE_ID="*license*"
BAIDU_FACE_SDK_LICENSE_FILE_NAME="idl-license.face-android" # 可不配, 默认为 idl-license.face-android
...
```
- 在 MainApplication.java 中添加
```
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
- 在 AndroidManifest.xml 的 application 标签中添加(根据使用需求添加)
```
<!-- 人脸跟踪采集界面 -->
<activity
        android:name="com.baidu.idl.face.activity.FaceDetectExpActivity"
        android:hardwareAccelerated="true"
        android:launchMode="singleTop"
        android:screenOrientation="portrait"
        android:theme="@style/Theme_NoTitle"/>
<!-- 活体图像采集界面 -->
<activity
        android:name="com.baidu.idl.face.activity.FaceLivenessExpActivity"
        android:hardwareAccelerated="true"
        android:launchMode="singleTop"
        android:screenOrientation="portrait"
        android:theme="@style/Theme_NoTitle"/>
<!-- 设置相关 -->
<activity
        android:name="com.baidu.idl.face.activity.SettingsActivity"
        android:hardwareAccelerated="true"
        android:launchMode="singleTop"
        android:screenOrientation="portrait"
        android:theme="@style/Theme_NoTitle"/>
```
