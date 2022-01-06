//
//  BaiduFace.m
//  BaiduFace
//
//  Created by xieyang on 2018/8/3.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "BaiduFace.h"

#import "IDLFaceSDK/IDLFaceSDK.h"
#import "FaceParameterConfig.h"

@implementation BaiduFace

+ (void)initSDK {
    [BaiduFace initSDKWithResourceName:FACE_LICENSE_NAME];
}

+ (void)initSDKWithResourceName:(NSString *)resourceName {
    NSString* licensePath = [[NSBundle mainBundle] pathForResource:resourceName ofType:FACE_LICENSE_SUFFIX];
    NSAssert([[NSFileManager defaultManager] fileExistsAtPath:licensePath], @"license文件路径不对，请仔细查看文档");
    NSString *faceLicenseId = [[NSBundle mainBundle] objectForInfoDictionaryKey:FACE_LICENSE_ID];
    [[FaceSDKManager sharedInstance] setLicenseID:faceLicenseId andLocalLicenceFile:licensePath];
}

@end



