//
//  CoordTransform.m
//  CoordTransform
//
//  Created by xieyang on 2018/8/23.
//  Copyright © 2018 xieyang. All rights reserved.
//

#import "CoordinateManager.h"

@implementation CoordinateManager

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(bd09towgs84:(nullable NSDictionary *)param callback:(RCTResponseSenderBlock)callback) {
    if(param == nil) {
        callback(nil);
        return;
    }
    id latitude = [param valueForKey:@"latitude"];
    id longitude = [param valueForKey:@"longitude"];
    if(latitude == nil || longitude == nil) {
        callback(nil);
        return;
    }
    Location loc = bd09towgs84(LocationMake([latitude doubleValue], [longitude doubleValue]));
    callback(@[@{@"latitude": @(loc.latitude), @"longitude":@(loc.longitude)}]);
}

RCT_EXPORT_METHOD(wgs84tobd09:(nullable NSDictionary *)param callback:(RCTResponseSenderBlock)callback) {
    if(param == nil) {
        callback(nil);
        return;
    }
    id latitude = [param valueForKey:@"latitude"];
    id longitude = [param valueForKey:@"longitude"];
    if(latitude == nil || longitude == nil) {
        callback(nil);
        return;
    }
    Location loc = wgs84tobd09(LocationMake([latitude doubleValue], [longitude doubleValue]));
    callback(@[@{@"latitude": @(loc.latitude), @"longitude":@(loc.longitude)}]);
}

RCT_EXPORT_METHOD(gcj02tobd09:(nullable NSDictionary *)param callback:(RCTResponseSenderBlock)callback) {
    if(param == nil) {
        callback(nil);
        return;
    }
    id latitude = [param valueForKey:@"latitude"];
    id longitude = [param valueForKey:@"longitude"];
    if(latitude == nil || longitude == nil) {
        callback(nil);
        return;
    }
    Location loc = gcj02tobd09(LocationMake([latitude doubleValue], [longitude doubleValue]));
    callback(@[@{@"latitude": @(loc.latitude), @"longitude":@(loc.longitude)}]);
}

RCT_EXPORT_METHOD(bd09togcj02:(nullable NSDictionary *)param callback:(RCTResponseSenderBlock)callback) {
    if(param == nil) {
        callback(nil);
        return;
    }
    id latitude = [param valueForKey:@"latitude"];
    id longitude = [param valueForKey:@"longitude"];
    if(latitude == nil || longitude == nil) {
        callback(nil);
        return;
    }
    Location loc = bd09togcj02(LocationMake([latitude doubleValue], [longitude doubleValue]));
    callback(@[@{@"latitude": @(loc.latitude), @"longitude":@(loc.longitude)}]);
}

RCT_EXPORT_METHOD(wgs84togcj02:(nullable NSDictionary *)param callback:(RCTResponseSenderBlock)callback) {
    if(param == nil) {
        callback(nil);
        return;
    }
    id latitude = [param valueForKey:@"latitude"];
    id longitude = [param valueForKey:@"longitude"];
    if(latitude == nil || longitude == nil) {
        callback(nil);
        return;
    }
    Location loc = wgs84togcj02(LocationMake([latitude doubleValue], [longitude doubleValue]));
    callback(@[@{@"latitude": @(loc.latitude), @"longitude":@(loc.longitude)}]);
}

RCT_EXPORT_METHOD(gcj02towgs84:(nullable NSDictionary *)param callback:(RCTResponseSenderBlock)callback) {
    if(param == nil) {
        callback(nil);
        return;
    }
    id latitude = [param valueForKey:@"latitude"];
    id longitude = [param valueForKey:@"longitude"];
    if(latitude == nil || longitude == nil) {
        callback(nil);
        return;
    }
    Location loc = gcj02towgs84(LocationMake([latitude doubleValue], [longitude doubleValue]));
    callback(@[@{@"latitude": @(loc.latitude), @"longitude":@(loc.longitude)}]);
}

@end
