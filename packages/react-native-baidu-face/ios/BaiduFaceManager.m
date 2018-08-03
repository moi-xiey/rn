#import "BaiduFaceManager.h"
#import <UIKit/UIKit.h>
#import "LivenessViewController.h"
#import "DetectionViewController.h"
#import "LivingConfigModel.h"
#import "IDLFaceSDK/IDLFaceSDK.h"
#import "FaceParameterConfig.h"
#import <React/RCTUtils.h>

@implementation BaiduFaceManager

RCT_EXPORT_MODULE(BaiduFace)

RCT_EXPORT_METHOD(detect)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        DetectionViewController* dvc = [[DetectionViewController alloc] init];
        UINavigationController *navi = [[UINavigationController alloc] initWithRootViewController:dvc];
        navi.navigationBarHidden = true;
        UIWindow *window = RCTSharedApplication().delegate.window;
        [[window rootViewController] presentViewController:navi animated:true completion:nil];
    });
}

RCT_EXPORT_METHOD(liveness)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        LivenessViewController* lvc = [[LivenessViewController alloc] init];
        LivingConfigModel* model = [LivingConfigModel sharedInstance];
        [lvc livenesswithList:model.liveActionArray order:model.isByOrder numberOfLiveness:model.numOfLiveness];
        UINavigationController *navi = [[UINavigationController alloc] initWithRootViewController:lvc];
        navi.navigationBarHidden = true;
        UIWindow *window = RCTSharedApplication().delegate.window;
        [[window rootViewController] presentViewController:navi animated:true completion:nil];
    });
}

RCT_EXPORT_METHOD(config)
{
    
}

@end
