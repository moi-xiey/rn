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

bool outOfChina(Location wgLoc) {
    if (wgLoc.longitude < 72.004 || wgLoc.longitude > 137.8347) {
        return true;
    } else if (wgLoc.latitude < 0.8293 || wgLoc.latitude > 55.8271) {
        return true;
    }
    return false;
}

double transformlat(Location wgLoc) {
    double lng = wgLoc.longitude;
    double lat = wgLoc.latitude;
    double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(fabs(lng));
    ret += (20.0 * sin(6.0 * lng * pi) + 20.0 * sin(2.0 * lng * pi)) * 2.0 / 3.0;
    ret += (20.0 * sin(lat * pi) + 40.0 * sin(lat / 3.0 * pi)) * 2.0 / 3.0;
    ret += (160.0 * sin(lat / 12.0 * pi) + 320 * sin(lat * pi / 30.0)) * 2.0 / 3.0;
    return ret;
}

double transformlng(Location wgLoc) {
    double lng = wgLoc.longitude;
    double lat = wgLoc.latitude;
    double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(fabs(lng));
    ret += (20.0 * sin(6.0 * lng * pi) + 20.0 * sin(2.0 * lng * pi)) * 2.0 / 3.0;
    ret += (20.0 * sin(lng * pi) + 40.0 * sin(lng / 3.0 * pi)) * 2.0 / 3.0;
    ret += (150.0 * sin(lng / 12.0 * pi) + 300.0 * sin(lng / 30.0 * pi)) * 2.0 / 3.0;
    return ret;
}

Location bd09towgs84(Location wgLoc) {
    Location gcj = bd09togcj02(wgLoc);
    Location wgs84 = gcj02towgs84(gcj);
    return wgs84;
}

Location wgs84tobd09(Location wgLoc) {
    Location gcj = wgs84togcj02(wgLoc);
    Location bd09 = gcj02tobd09(gcj);
    return bd09;
}

Location gcj02tobd09(Location wgLoc) {
    double z = sqrt(wgLoc.longitude * wgLoc.longitude + wgLoc.latitude * wgLoc.latitude) + 0.00002 * sin(wgLoc.latitude * x_pi);
    double theta = atan2(wgLoc.latitude, wgLoc.longitude) + 0.000003 * cos(wgLoc.longitude * x_pi);
    double bd_lng = z * cos(theta) + 0.0065;
    double bd_lat = z * sin(theta) + 0.006;
    return LocationMake(bd_lat, bd_lng);
}

Location bd09togcj02(Location wgLoc) {
    double x = wgLoc.longitude - 0.0065;
    double y = wgLoc.latitude - 0.006;
    double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
    double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
    double gg_lng = z * cos(theta);
    double gg_lat = z * sin(theta);
    return LocationMake(gg_lat, gg_lng);
}

Location wgs84togcj02(Location wgLoc) {
    if (outOfChina(wgLoc)) {
        return wgLoc;
    }
    double dlat = transformlat(LocationMake(wgLoc.longitude - 105.0, wgLoc.latitude - 35.0));
    double dlng = transformlng(LocationMake(wgLoc.longitude - 105.0, wgLoc.latitude - 35.0));
    double radlat = wgLoc.latitude / 180.0 * pi;
    double magic = sin(radlat);
    magic = 1 - ee * magic * magic;
    double sqrtmagic = sqrt(magic);
    dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
    dlng = (dlng * 180.0) / (a / sqrtmagic * cos(radlat) * pi);
    double mglat = wgLoc.latitude + dlat;
    double mglng = wgLoc.longitude + dlng;
    return LocationMake(mglat, mglng);
}

Location gcj02towgs84(Location wgLoc) {
    if (outOfChina(wgLoc)) {
        return wgLoc;
    }
    double dlat = transformlat(LocationMake(wgLoc.longitude - 105.0, wgLoc.latitude - 35.0));
    double dlng = transformlng(LocationMake(wgLoc.longitude - 105.0, wgLoc.latitude - 35.0));
    double radlat = wgLoc.latitude / 180.0 * pi;
    double magic = sin(radlat);
    magic = 1 - ee * magic * magic;
    double sqrtmagic = sqrt(magic);
    dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
    dlng = (dlng * 180.0) / (a / sqrtmagic * cos(radlat) * pi);
    double mglat = wgLoc.latitude + dlat;
    double mglng = wgLoc.longitude + dlng;
    return LocationMake(wgLoc.longitude * 2 - mglng, wgLoc.latitude * 2 - mglat);
}

@end
