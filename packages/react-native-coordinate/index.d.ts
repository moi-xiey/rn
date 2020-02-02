declare module '@moi-xiey/react-native-coordinate' {

  export interface Point {
    latitude: number,
    longitude: number,
  }

  type Callback = (point: Point) => void;

  export default class Coordinate {
    static wgs84tobd09: (point: Point, cb: Callback) => void;
    static wgs84togcj02: (point: Point, cb: Callback) => void;
    static gcj02tobd09: (point: Point, cb: Callback) => void;
    static gcj02towgs84: (point: Point, cb: Callback) => void;
    static bd09togcj02: (point: Point, cb: Callback) => void;
    static bd09towgs84: (point: Point, cb: Callback) => void;
  }
}
