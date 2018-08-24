//
//  CoordinateTransform.h
//  Coordinate
//
//  Created by xieyang on 2018/8/23.
//  Copyright © 2018 xieyang. All rights reserved.
//

#import "math.h"
#import <Foundation/Foundation.h>

typedef struct
{
    double latitude;
    double longitude;
} Location;

Location LocationMake(double latitude, double longitude);

@interface CoordinateTransform : NSObject

/**
 *  判断是否已经超出中国范围
 */
bool outOfChina(Location wgLoc);

/**
 *  纬度转换
 */
double transformlat(Location wgLoc);

/**
 *  经度转换
 */
double transformlng(Location wgLoc);

/**
 *  百度坐标-》标准坐标
 */
Location bd09towgs84(Location wgLoc);

/**
 *  标准坐标-》百度坐标
 */
Location wgs84tobd09(Location wgLoc);

/**
 *  火星坐标-》百度坐标
 */
Location gcj02tobd09(Location wgLoc);

/**
 *  百度坐标-》火星坐标
 */
Location bd09togcj02(Location wgLoc);

/**
 *  标准坐标-》火星坐标
 */
Location wgs84togcj02(Location wgLoc);

/**
 *  火星坐标-》标准坐标
 */
Location gcj02towgs84(Location wgLoc);

@end
