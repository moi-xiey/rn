import {NativeModules} from 'react-native';

const {Coordinate} = NativeModules;

export default {
  /**
   * @param {Object} point
   * @param {Number} point.latitude
   * @param {Number} point.longitude
   * @param {Function} cb
   */
  wgs84tobd09: (point, cb) => Coordinate.wgs84tobd09(point, cb),
  /**
   * @param {Object} point
   * @param {Number} point.latitude
   * @param {Number} point.longitude
   * @param {Function} cb
   */
  wgs84togcj02: (point, cb) => Coordinate.wgs84togcj02(point, cb),
  /**
   * @param {Object} point
   * @param {Number} point.latitude
   * @param {Number} point.longitude
   * @param {Function} cb
   */
  gcj02tobd09: (point, cb) => Coordinate.gcj02tobd09(point, cb),
  /**
   * @param {Object} point
   * @param {Number} point.latitude
   * @param {Number} point.longitude
   * @param {Function} cb
   */
  gcj02towgs84: (point, cb) => Coordinate.gcj02towgs84(point, cb),
  /**
   * @param {Object} point
   * @param {Number} point.latitude
   * @param {Number} point.longitude
   * @param {Function} cb
   */
  bd09togcj02: (point, cb) => Coordinate.bd09togcj02(point, cb),
  /**
   * @param {Object} point
   * @param {Number} point.latitude
   * @param {Number} point.longitude
   * @param {Function} cb
   */
  bd09towgs84: (point, cb) => Coordinate.bd09towgs84(point, cb),
};
