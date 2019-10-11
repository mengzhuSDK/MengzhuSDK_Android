# GMMengzhuSDK_Android
国民健康定制版android SDK
# 1. SDK简介
盟主直播SDK是一款辅助于盟主直播的推流及拉流工具，此工具仅限用于盟主直播业务，需在盟主开放平台进行认证授权方可使用。盟主直播SDK主要实现功能有视频直播推流、视频直播、点播拉流及聊天等业务功能，其推流功能采用摄像头数据抓取并进行编码后推送至盟主服务器，播放功能实现对盟主直播及点播视频的编解码播放。盟主直播SDK接入方式简洁、方便，其功能实现效果稳定、高效。
# 2. 盟主直播Android SDK架构设计
盟主直播Android SDK架构以CBU设计模式进行搭建，Core为核心层面技术，内部实现了推流编码及网络请求等功能，Business业务层处理了播放器控制器及MVP业务架构等所有业务控制工作，UserInterface为用户对接层，此层面封装了所有面向接入端所需要的业务接口及管理器。具体结构请看下图。
![](https://wmz.zmengzhu.com/uploads/201811/5bdac3a253f9d_5bdac3a2.png)
# 3. SDK功能及支持
### 3.1.  设备和系统要求
> 支持Android 4.3及以上系统
支持所有装有Android系统的硬件设备
###  3.2 功能特性
> 支持推流到主流 RTMP 服务器  
支持 H.264 和 AAC 编码  
支持MP4录制  
支持音视频采集，编码，打包，传输  
支持 armv7、arm64 架构  
资源占用率低，库文件小  
画质清晰，延时低  
支持闪光灯开启操作  
支持摄像头缩放操作  
支持前后置摄像头动态切换  
支持分辨率动态切换  
支持自动对焦  
支持摄像头焦距调节  
支持视频镜像操作  
支持视频截图  
支持多款滤镜  
支持磨皮  
支持直播过程中帧率调节  
支持直播过程中码率调节  
提供固定ui  
支持直播间聊天  
支持历史消息获取  
支持商品列表  
支持直播间信息获取  
支持在线观众展示  
支持主播信息获取  

# 4. 固定UI版本配置及实现方式
#### 固定UI版本使用Module android-library方式提供，内部实现业务上提供的所有功能逻辑及UI。使用时将其library导入项目进行简单配置即可使用。具体配置代码请查看下发示例.
###  4.1 manifest配置

    <uses-feature android:name="android.hardware.camera"/>
    <uses-feature android:name="android.hardware.camera.autofocus"/>
    <uses-feature android:name="android.hardware.camera.flash"/>
    <uses-feature android:glEsVersion="0x00020000" android:required="true"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    
###  4.2 build.gradle配置
    dependencies {
      ...
      implementation project(':MZMediaSDK')
    }
###  4.3 代码配置
#### Application类内onCreate调用以下代码：  
      MZSDKInitManager.getInstance().initApplication(this);

#### 播放直播间fragment实现
  
