#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import "PassDataDelegate.h"

@interface BaiduFace : RCTEventEmitter <RCTBridgeModule, PassDataDelegate>

@property (nonatomic, readwrite, assign) BOOL mIsInitSuccess;

@end
