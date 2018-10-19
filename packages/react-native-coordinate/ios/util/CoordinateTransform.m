//
//  CoordinateTransform.m
//  Coordinate
//
//  Created by xieyang on 2018/8/23.
//  Copyright © 2018 xieyang. All rights reserved.
//

#import "CoordinateTransform.h"

// 长半轴
const double a = 6378245.0;
// 扁率
const double ee = 0.00669342162296594323;
// π
const double pi = 3.1415926535897932384626;
const double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

@implementation CoordinateTransform

bool outOfChina(NSDictionary* wgLoc) {
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    if (lng < 72.004 || lng > 137.8347) {
        return true;
    } else if (lat < 0.8293 || lat > 55.8271) {
        return true;
    }
    return false;
}

double transformlat(NSDictionary* wgLoc) {
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(fabs(lng));
    ret += (20.0 * sin(6.0 * lng * pi) + 20.0 * sin(2.0 * lng * pi)) * 2.0 / 3.0;
    ret += (20.0 * sin(lat * pi) + 40.0 * sin(lat / 3.0 * pi)) * 2.0 / 3.0;
    ret += (160.0 * sin(lat / 12.0 * pi) + 320 * sin(lat * pi / 30.0)) * 2.0 / 3.0;
    return ret;
}

double transformlng(NSDictionary* wgLoc) {
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(fabs(lng));
    ret += (20.0 * sin(6.0 * lng * pi) + 20.0 * sin(2.0 * lng * pi)) * 2.0 / 3.0;
    ret += (20.0 * sin(lng * pi) + 40.0 * sin(lng / 3.0 * pi)) * 2.0 / 3.0;
    ret += (150.0 * sin(lng / 12.0 * pi) + 300.0 * sin(lng / 30.0 * pi)) * 2.0 / 3.0;
    return ret;
}

NSDictionary* bd09towgs84(NSDictionary* wgLoc) {
    NSDictionary* gcj = bd09togcj02(wgLoc);
    NSDictionary* wgs84 = gcj02towgs84(gcj);
    return wgs84;
}

NSDictionary* wgs84tobd09(NSDictionary* wgLoc) {
    NSDictionary* gcj = wgs84togcj02(wgLoc);
    NSDictionary* bd09 = gcj02tobd09(gcj);
    return bd09;
}

NSDictionary* gcj02tobd09(NSDictionary* wgLoc) {
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    double z = sqrt(lng * lng + lat * lat) + 0.00002 * sin(lat * x_pi);
    double theta = atan2(lat, lng) + 0.000003 * cos(lng * x_pi);
    double bd_lng = z * cos(theta) + 0.0065;
    double bd_lat = z * sin(theta) + 0.006;
    return @{@"longitude": @(bd_lng), @"latitude": @(bd_lat)};
}

NSDictionary* bd09togcj02(NSDictionary* wgLoc) {
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    double x = lng - 0.0065;
    double y = lat - 0.006;
    double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
    double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
    double gg_lng = z * cos(theta);
    double gg_lat = z * sin(theta);
    return @{@"longitude": @(gg_lng), @"latitude": @(gg_lat)};
}

NSDictionary* wgs84togcj02(NSDictionary* wgLoc) {
    if (outOfChina(wgLoc)) {
        return wgLoc;
    }
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    double dlat = transformlat(@{@"longitude": @(lng - 105.0), @"latitude": @(lat - 35.0)});
    double dlng = transformlng(@{@"longitude": @(lng - 105.0), @"latitude": @(lat - 35.0)});
    double radlat = lat / 180.0 * pi;
    double magic = sin(radlat);
    magic = 1 - ee * magic * magic;
    double sqrtmagic = sqrt(magic);
    dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
    dlng = (dlng * 180.0) / (a / sqrtmagic * cos(radlat) * pi);
    double mglat = lat + dlat;
    double mglng = lng + dlng;
    return @{@"longitude": @(mglng), @"latitude": @(mglat)};
}

NSDictionary* gcj02towgs84(NSDictionary* wgLoc) {
    if (outOfChina(wgLoc)) {
        return wgLoc;
    }
    double lng = [[wgLoc valueForKey:@"longitude"] doubleValue];
    double lat = [[wgLoc valueForKey:@"latitude"] doubleValue];
    double dlat = transformlat(@{@"longitude": @(lng - 105.0), @"latitude": @(lat - 35.0)});
    double dlng = transformlng(@{@"longitude": @(lng - 105.0), @"latitude": @(lat - 35.0)});
    double radlat = lat / 180.0 * pi;
    double magic = sin(radlat);
    magic = 1 - ee * magic * magic;
    double sqrtmagic = sqrt(magic);
    dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
    dlng = (dlng * 180.0) / (a / sqrtmagic * cos(radlat) * pi);
    double mglat = lat + dlat;
    double mglng = lng + dlng;
    return @{@"longitude": @(lng * 2 - mglng), @"latitude": @(lat * 2 - mglat)};
}

@end
