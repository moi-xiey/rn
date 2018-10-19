# React Native 百度地图
> fork from [qiuxiang/react-native-baidumap-sdk](https://github.com/qiuxiang/react-native-baidumap-sdk). 调整了目录结构

### 安装步骤
- `yarn add react-native-bmap-sdk`
- `react-native link react-native-bmap-sdk`
- <details>
    <summary>Android 步骤</summary>

    - 配置 AndroidMainifest.xml <meta-data android:name="com.baidu.lbsapi.API_KEY" android:value="xx"/>
  </details>
- <details>
    <summary>iOS 步骤</summary>

    - xcode打开项目, 在 Frameworks 文件右键 Add Files to "xxx", 在弹出的框中选择 ${项目根目录}/node_modules/react-native-bmap-sdk/ios/Frameworks 文件下所有的.framework 文件, 点击 add 按钮
    - 引入系统库文件, General/Linked Frameworks and Libraries 点击 + 号添加(MobileCoreServices.framework,CoreLocation.framework,QuartzCore.framework,OpenGLES.framework,SystemConfiguration.framework,CoreGraphics.framework,Security.framework,libsqlite3.0.tbd,CoreTelephony.framework,libc++.tbd)
    - 引入第三方 openssl 库, General/Linked Frameworks and Libraries 点击 + 号在弹窗框点击 Add Other... 按钮, 在弹出的框中跳转到 ${项目根目录}/node_modules/react-native-bmap-sdk/ios/Frameworks/thirdlibs 目录选择添加 libcrypto.a 和 libssl.a 文件
    - Build Settings/Search Paths/Framework Search Paths 添加 $(SRCROOT)/../node_modules/react-native-bmap-sdk/ios/Frameworks
    - Build Settings/Search Paths/Library Search Paths 添加 $(SRCROOT)/../node_modules/react-native-bmap-sdk/ios/Frameworks/thirdlibs
    - Build Phases/Copy Bundle Resources 点击 + 号/Add Other... 在弹出的框中跳转到 ${项目根目录}/node_modules/react-native-bmap-sdk/ios/Frameworks/BaiduMapAPI_Map.framework/Resources 目录选择添加 mapapi.bundle,images.bundle 文件(勾选“Copy items if needed”复选框)
  </details>

### 使用
> 参见 docs 目录
