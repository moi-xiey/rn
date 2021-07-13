declare class RNApkInstaller {
    static install(filePath:string): Promise<string>;
    static haveUnknownAppSourcesPermission(): Promise<string>;
    static showUnknownAppSourcesPermission(): Promise<string>;
    static firstInstallTime: number;
    static lastUpdateTime: number;
    static versionName: string;
    static versionCode: number;
    static packageName: string;
    static packageInstaller: string;
}
export default RNApkInstaller;
