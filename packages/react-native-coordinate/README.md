# React Native 坐标系转换
> 一个提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换的工具模块。

### 安装步骤
- `yarn add react-native-coordinate`
- `react-native link react-native-coordinate`

### 使用
```javascript
import Coordinate from "react-native-coordinate";

const latitude = 29.583364;
const longitude = 106.52236;
// gps设备获取坐标转百度坐标
Coordinate.wgs84tobd09({latitude, longitude}, (coordinate) => {console.log(coordinate);});
```
