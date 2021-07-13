package com.nodece.apkinstallern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RNApkInstallerNModule extends ReactContextBaseJavaModule implements ActivityEventListener {

  private Promise mPromise;
  private final ReactApplicationContext reactContext;

  private static final int REQUEST_SETTING_CODE = 6101;

  public RNApkInstallerNModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(this);
  }

  @Override
  public String getName() {
    return "RNApkInstaller";
  }

  @SuppressLint("PackageManagerGetSignatures")
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    PackageManager pManager = reactContext.getPackageManager();
    PackageInfo pInfo;
    try {
      pInfo = pManager.getPackageInfo(reactContext.getPackageName(), PackageManager.GET_SIGNATURES);
      constants.put("versionName", pInfo.versionName);
      constants.put("versionCode", pInfo.versionCode);
      constants.put("packageName", pInfo.packageName);
      constants.put("firstInstallTime", pInfo.firstInstallTime);
      constants.put("lastUpdateTime", pInfo.lastUpdateTime);
      constants.put("packageInstaller", pManager.getInstallerPackageName(pInfo.packageName));
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return constants;
  }

  @ReactMethod
  public void install(String filePath, Promise promise) {
    File apkFile = new File(filePath);
    if (!apkFile.exists()) {
      promise.reject("101", "file not exist. " + filePath);
      return;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Uri contentUri;
      String authority = reactContext.getPackageName() + ".fileprovider";
      try {
        contentUri = FileProvider.getUriForFile(getReactApplicationContext(), authority, apkFile);
      } catch (Exception e) {
        promise.reject("102", "installApk exception with authority name '" + authority + "'");
        return;
      }
      Intent installApp = new Intent(Intent.ACTION_INSTALL_PACKAGE);
      installApp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      installApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      installApp.setData(contentUri);
      installApp.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, reactContext.getApplicationInfo().packageName);
      reactContext.startActivity(installApp);
    } else {
      // Old APIs do not handle content:// URIs, so use an old file:// style
      String cmd = "chmod 777 " + apkFile;
      try {
        Runtime.getRuntime().exec(cmd);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setDataAndType(Uri.parse("file://" + apkFile), "application/vnd.android.package-archive");
      reactContext.startActivity(intent);
    }
    promise.resolve(true);
  }

  @ReactMethod
  public void haveUnknownAppSourcesPermission(Promise promise) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      // Android 8 及以上才有
      promise.reject("-1", "");
      return;
    }
    promise.resolve(reactContext.getPackageManager().canRequestPackageInstalls());
  }

  @ReactMethod
  public void showUnknownAppSourcesPermission(Promise promise) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      // Android 8 及以上才有
      promise.reject("-1", "");
      return;
    }
    this.mPromise = promise;
    try {
      Uri packageURI = Uri.parse("package:" + reactContext.getPackageName());
      Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
      this.reactContext.startActivityForResult(intent, REQUEST_SETTING_CODE, null);
    } catch(Exception e) {
      promise.reject("101", "START_ACTIVITY_ERROR");
      this.mPromise = null;
    }
  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SETTING_CODE && this.mPromise != null) {
      this.mPromise.resolve(true);
      this.mPromise = null;
    }
  }

  @Override
  public void onNewIntent(Intent intent) {

  }
}
