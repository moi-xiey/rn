//
//  CoordinateTransform.h
//  Coordinate
//
//  Created by xieyang on 2018/8/23.
//  Copyright © 2018 xieyang. All rights reserved.
//

#import "math.h"
#import <Foundation/Foundation.h>

@interface CoordinateTransform : NSObject

/**
 *  判断是否已经超出中国范围
 */
bool outOfChina(NSDictionary* wgLoc);

/**
 *  纬度转换
 */
double transformlat(NSDictionary* wgLoc);

/**
 *  经度转换
 */
double transformlng(NSDictionary* wgLoc);

/**
 *  百度坐标-》标准坐标
 */
NSDictionary* bd09towgs84(NSDictionary* wgLoc);

/**
 *  标准坐标-》百度坐标
 */
NSDictionary* wgs84tobd09(NSDictionary* wgLoc);

/**
 *  火星坐标-》百度坐标
 */
NSDictionary* gcj02tobd09(NSDictionary* wgLoc);

/**
 *  百度坐标-》火星坐标
 */
NSDictionary* bd09togcj02(NSDictionary* wgLoc);

/**
 *  标准坐标-》火星坐标
 */
NSDictionary* wgs84togcj02(NSDictionary* wgLoc);

/**
 *  火星坐标-》标准坐标
 */
NSDictionary* gcj02towgs84(NSDictionary* wgLoc);

@end
