# ChatRoom
环信 SDK 集成，根据开发文档，简单实现及时通讯，在项目开发更容易使用



1）集成说明
该项目是为了，更好的在请他项目集成环信，实现及时通讯而做的Demo，更容易理解和集成。
2)	环信的Demo演示
导入环信demo的注意事项
（1）根据自己android studio 版本修改gradle配置 和版本号（demo中默认是2.1.0的gradle）

     将C:\Users\Administrator\Desktop\easemob-sdk-3.1.3_R2\easemob-sdk-3.1.3\examples\ChatDemoUI3.0\gradle\wrapper下的
     distributionUrl=https\://services.gradle.org/distributions/gradle-2.10-all.zip
      修改为distributionUrl=https\://services.gradle.org/distributions/gradle-2.8-all.zip
（2）修改项目中gradle的版本（demo中默认是2.1.0的gradle）

     将C:\Users\Administrator\Desktop\easemob-sdk-3.1.3_R2\easemob-sdk-3.1.3\examples\ChatDemoUI3.0下的classpath         'com.android.tools.build:gradle:2.1.0'修改为classpath 'com.android.tools.build:gradle:1.5.0'
（3）将项目中的setting.gradle中如下内容注释掉

     //release时需要把下面的include相关的两行注释掉
     include ':hyphenatechatsdk'
     project(':hyphenatechatsdk').projectDir = new File('../emclient-android/hyphenatechatsdk')
（4）将easeui中的build.gradle如下内容注释掉

      compile project(':hyphenatechatsdk')

3)	错误解决
编译程序报如下错误

    Error:Execution failed for task ':app:transformClassesWithDexForDebug'.
    > com.android.build.api.transform.TransformException: com.android.ide.common.process.ProcessException:          org.gradle.process.internal.ExecException: Process 'command 'D:\Program Files\Java\jdk1.7.0_45\bin\java.exe'' finished with non-     zero exit value 2


错误原因：EaseUI中的v4包和主项目中的v7包冲突，
解决办法：在项目的build.gradle将v7包中的v4包移除

    dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile ('com.android.support:appcompat-v7:23.4.0'){
        exclude module: 'support-v4'
    }
    compile project(':easeui')
     }
4)	集成EaseUI
（1）将EaseUI模块导入到项目中，并管理到主项目中
（2）配置AndroidManifest.xml清单文件
// 配置权限

     <uses-permission android:name="android.permission.VIBRATE" />
     <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
          <uses-permission android:name="android.permission.CAMERA" />
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.GET_TASKS" />
     <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.USE_CREDENTIALS" />
     <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
     <uses-permission android:name="android.permission.BROADCAST_STICKY" />
     <uses-permission android:name="android.permission.WRITE_SETTINGS" />
     <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />


      <!-- 设置环信应用的appkey -->
     <meta-data
    android:name="EASEMOB_APPKEY"
    android:value="appkey" />
    <!-- 声明sdk所需的service -->
    <service android:name="com.hyphenate.chat.EMChatService"
    android:exported="true"
    />
     <!-- 声明sdk所需的receiver -->
     <receiver android:name="com.hyphenate.chat.EMMonitorReceiver">
    <intent-filter>
        <action android:name="android.intent.action.PACKAGE_REMOVED"/>
        <data android:scheme="package"/>
    </intent-filter>
    <!-- 可选filter -->
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.USER_PRESENT" />
    </intent-filter>
    </receiver>

（3）创建IMApplication，初始化EaseUI

     public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化环信EaseUI参数
        EMOptions options = new EMOptions();
        options.setAutoAcceptGroupInvitation(false);
        options.setAcceptInvitationAlways(false);

        EaseUI.getInstance().init(this, options);
    }
}

     // 在AndroidManifest.xml中关联
     <application
    android:name=".MyApplication"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    </application>




