
# react-native-apk-installer-n

Support AndroidX and Android other version

## Getting started

AndroidX
`$ yarn add @moi-xiey/react-native-apk-installer`

### Mostly automatic install with react-native link (RN < 0.60)

`$ react-native link @moi-xiey/react-native-apk-installer`

## Usage

You can use react-native-fs to download the apk file:

```javascript
import RNFS from 'react-native-fs'
import RNApkInstallerN from 'react-native-apk-installer-n';

const filePath = RNFS.DocumentDirectoryPath + '/com.domain.example.apk';
const download = RNFS.downloadFile({
    fromUrl: 'apk file download url',
    toFile: filePath,
    progress: res => {
        console.log((res.bytesWritten / res.contentLength).toFixed(2));
    },
    progressDivider: 1
});

download.promise.then(result => {
    if(result.statusCode == 200){
        RNApkInstallerN.install(filePath)
    }
});
```
