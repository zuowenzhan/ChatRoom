# ChatRoom
环信 SDK 集成，根据开发文档，简单实现及时通讯，在项目开发更容易使用



## 1）集成说明
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
## 环信其他说明
### 1.环信本身不提供用户体系，环信既不保存任何 APP 业务数据，也不保存任何 APP 的用户信息。比如说，你的 APP 是一个婚恋交友 APP，那么你的 APP 用户的头像、昵称、身高、体重、三围、电话号码等信息是保存在你自己的 APP 业务服务器上，这些信息不需要告诉环信。
### 2.客户端登录集成
 APP 客户端在登录自己的 APP 服务器后台成功后，需要调用环信客户端 SDK 的登录方法。
### 3.客户端退出登录集成
APP 客户端在退出登录自己的 APP 服务器后台成功后，需要调用环信客户端 SDK 的退出登录方法。
### 4.好友体系集成
所谓好友体系，是指谁是谁的好友的关系体系。环信提供好友体系，但不是必须使用的。
比如对一个企业内部移动办公 APP 来说，因为企业内部同事是彼此认识的，那么此 APP 可能是不需要消息发送权限控制的。即任何人都可以给任何人发消息。
但一个交友类的 APP 就必须要控制只有我的好友才能给我发消息，不是我的好友的人需要向我发送加好友邀请，我批准后才能给我发消息。这种情况下，就需要启用环信提供的好友体系。
### 5.消息历史记录免费存储3天




