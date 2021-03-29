#import "BaiduFace.h"
#import "BDFaceDetectionViewController.h"
#import "BDFaceLivingConfigModel.h"
#import "BDFaceLivenessViewController.h"
#import <React/RCTUtils.h>
#import "IDLFaceSDK/IDLFaceSDK.h"
#import "BDFaceAdjustParamsTool.h"

@implementation BaiduFace

- (void)passData:(NSObject *)data {
    [self sendEventWithName:@"complete" body:data];
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"complete", @"config"];
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(config: (NSString *) licenseID licenseFile: (NSString *)licenseFile)
{
    FaceSDKManager *manager = [FaceSDKManager sharedInstance];
    [manager setLicenseID:licenseID andLocalLicenceFile:licenseFile andRemoteAuthorize:false];
    BOOL success = [manager canWork];
    if (success) {
        [self initSDK];
        [self initLivenesswithList];
        self.mIsInitSuccess = YES;
    }
    [self sendEventWithName:@"config" body:@{@"success": [NSNumber numberWithBool:success]}];
}

RCT_EXPORT_METHOD(detect)
{
    if (!self.mIsInitSuccess) {
        [self sendEventWithName:@"complete" body:@{@"success": @0, @"message": @"初始化失败", @"code": @-1}];
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            BDFaceDetectionViewController* dvc = [[BDFaceDetectionViewController alloc] init];
            dvc.delegate = self;
            UINavigationController *navi = [[UINavigationController alloc] initWithRootViewController:dvc];
            navi.navigationBarHidden = true;
            UIWindow *window = RCTSharedApplication().delegate.window;
            [[window rootViewController] presentViewController:navi animated:true completion:nil];
        });
    }
}

RCT_EXPORT_METHOD(liveness)
{
    if (!self.mIsInitSuccess) {
        [self sendEventWithName:@"complete" body:@{@"success": @0, @"message": @"初始化失败", @"code": @-1}];
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            BDFaceLivenessViewController* lvc = [[BDFaceLivenessViewController alloc] init];
            lvc.delegate = self;
            BDFaceLivingConfigModel* model = [BDFaceLivingConfigModel sharedInstance];
            int index = arc4random() % model.liveActionArray.count;
            NSArray *liveness = [NSArray arrayWithObject:[model.liveActionArray objectAtIndex:index]];
            [lvc livenesswithList:liveness order:YES numberOfLiveness:liveness.count];
            UINavigationController *navi = [[UINavigationController alloc] initWithRootViewController:lvc];
            navi.navigationBarHidden = true;
            UIWindow *window = RCTSharedApplication().delegate.window;
            [[window rootViewController] presentViewController:navi animated:true completion:nil];
        });
    }
}

- (void) initSDK {
    
    if (![[FaceSDKManager sharedInstance] canWork]){
        NSLog(@"授权失败，请检测ID 和 授权文件是否可用");
        return;
    }
    
    // 初始化SDK配置参数，可使用默认配置
    // 设置最小检测人脸阈值
    [[FaceSDKManager sharedInstance] setMinFaceSize:200];
    // 设置截取人脸图片高
    [[FaceSDKManager sharedInstance] setCropFaceSizeWidth:480];
    // 设置截取人脸图片宽
    [[FaceSDKManager sharedInstance] setCropFaceSizeHeight:640];
    // 设置人脸遮挡阀值
    [[FaceSDKManager sharedInstance] setOccluThreshold:0.5];
    // 设置亮度阀值
    [[FaceSDKManager sharedInstance] setMinIllumThreshold:40];
    [[FaceSDKManager sharedInstance] setMaxIllumThreshold:240];
    // 设置图像模糊阀值
    [[FaceSDKManager sharedInstance] setBlurThreshold:0.3];
    // 设置头部姿态角度
    [[FaceSDKManager sharedInstance] setEulurAngleThrPitch:10 yaw:10 roll:10];
    // 设置人脸检测精度阀值
    [[FaceSDKManager sharedInstance] setNotFaceThreshold:0.6];
    // 设置抠图的缩放倍数
    [[FaceSDKManager sharedInstance] setCropEnlargeRatio:2.5];
    // 设置照片采集张数
    [[FaceSDKManager sharedInstance] setMaxCropImageNum:3];
    // 设置超时时间
    [[FaceSDKManager sharedInstance] setConditionTimeout:15];
    // 设置开启口罩检测，非动作活体检测可以采集戴口罩图片
    [[FaceSDKManager sharedInstance] setIsCheckMouthMask:true];
    // 设置开启口罩检测情况下，非动作活体检测口罩过滤阈值，默认0.8 不需要修改
    [[FaceSDKManager sharedInstance] setMouthMaskThreshold:0.8f];
    // 设置原始图缩放比例
    [[FaceSDKManager sharedInstance] setImageWithScale:0.8f];
    // 设置图片加密类型，type=0 基于base64 加密；type=1 基于百度安全算法加密
    [[FaceSDKManager sharedInstance] setImageEncrypteType:0];
    // 初始化SDK功能函数
    [[FaceSDKManager sharedInstance] initCollect];
    // 设置人脸过远框比例
    [[FaceSDKManager sharedInstance] setMinRect:0.4];
    
//    /// 设置用户设置的配置参数
    [BDFaceAdjustParamsTool setDefaultConfig];
}

- (void)initLivenesswithList {
    // 默认活体检测打开，顺序执行
    /*
     添加当前默认的动作，是否需要按照顺序，动作活体的数量（设置页面会根据这个numOfLiveness来判断选择了几个动作）
     */
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray removeAllObjects];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLiveEye)];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLiveMouth)];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLiveYawRight)];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLiveYawLeft)];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLivePitchUp)];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLivePitchDown)];
    [BDFaceLivingConfigModel.sharedInstance.liveActionArray addObject:@(FaceLivenessActionTypeLiveYaw)];
    BDFaceLivingConfigModel.sharedInstance.isByOrder = NO;
    BDFaceLivingConfigModel.sharedInstance.numOfLiveness = 3;
}


@end
