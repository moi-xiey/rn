/**
 * 坐标系转换库: gcoord
 * @link https://github.com/hujiulong/gcoord
 */

/**
 * 地球半径
 */
const EARTH_RADIUS = 6370996.81;

/**
 * 将v值限定在a,b之间，经度使用
 */
function _getLoop(v, a, b) {
  while (v > b) {
    v -= b - a;
  }
  while (v < a) {
    v += b - a;
  }
  return v;
}

/**
 * 将v值限定在a,b之间，纬度使用
 */
function _getRange(v, a, b) {
  if (a != null) {
    v = Math.max(v, a);
  }
  if (b != null) {
    v = Math.min(v, b);
  }
  return v;
}

/**
 * 将度转化为弧度
 * @param {Number}  degree 度
 * @returns {Number} 弧度
 */
function degreeToRad(degree) {
  return Math.PI * degree / 180;
}

/**
 * 点到点距离
 * @param {Array} latLng1 [lat, lng]
 * @param {Array} latLng2 [lat, lng]
 * @return {number} 米
 */
const getDistance = (latLng1, latLng2) => {
  const lat1 = _getRange(latLng1[0], -74, 74);
  const lng1 = _getLoop(latLng1[1], -180, 180);
  const lat2 = _getRange(latLng2[0], -74, 74);
  const lng2 = _getLoop(latLng2[1], -180, 180);

  const x1 = degreeToRad(lng1);
  const y1 = degreeToRad(lat1);
  const x2 = degreeToRad(lng2);
  const y2 = degreeToRad(lat2);

  return EARTH_RADIUS * Math.acos((Math.sin(y1) * Math.sin(y2) + Math.cos(y1) * Math.cos(y2) * Math.cos(x2 - x1)));
};

/**
 * 点是否在区域范围内
 * @param {Array<Number>} point [lat, lng]
 * @param {Array<Array>} vs [[lat, lng], [lat, lng], ...]
 * @return {boolean}
 */
function isInside(point, vs) {
  // ray-casting algorithm based on
  // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html

  const x = point[0], y = point[1];

  let inside = false;
  for (let i = 0, j = vs.length - 1; i < vs.length; j = i++) {
    const xi = vs[i][0], yi = vs[i][1];
    const xj = vs[j][0], yj = vs[j][1];

    const intersect = ((yi > y) != (yj > y))
      && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
    if (intersect) inside = !inside;
  }

  return inside;
}

/**
 *
 * @param {Array<number>} point [lat, lng]
 * @param {Array<number>} lineStartPoint [lat, lng]
 * @param {Array<number>} lineEndPoint [lat, lng]
 */
function getPointToLineDistance(point, lineStartPoint, lineEndPoint) {
  const a = getDistance(lineStartPoint, lineEndPoint);
  const b = getDistance(point, lineStartPoint);
  const c = getDistance(point, lineEndPoint);
  if (Math.acos((b * b + a * a - c * c) / (2 * b * a)) * 180 / Math.PI >= 90) return b;
  if (Math.acos((c * c + a * a - b * b) / (2 * c * a)) * 180 / Math.PI >= 90) return c;
  const P = (a + b + c) / 2;
  const s = Math.pow(P * (P - a) * (P - b) * (P - c), 1 / 2);
  return 2 * s / a;
}

/**
 * 点到区域距离(点到多边形)
 * @param {Array<Number>} point [lat, lng]
 * @param {Array<Array>} vs [[lat, lng], [lat, lng], ...]
 * @return {number} 米
 */
function getPointToPolygonDistance(point, vs) {
  const length = vs.length;
  if (length <= 0) {
    return 0;
  }
  if (length === 1) {
    return getDistance(point, vs[0]);
  }
  if (length === 2) {
    return getPointToLineDistance(point, vs[0], vs[1]);
  }
  if (isInside(point, vs)) {
    return 0;
  }
  const distanceArr = [];
  let p = vs[length - 1];
  for (let i = 0; i < length; i++) {
    distanceArr.push(getPointToLineDistance(point, p, vs[i]));
    p = vs[i];
  }
  return Math.min.apply(null, distanceArr);
}
