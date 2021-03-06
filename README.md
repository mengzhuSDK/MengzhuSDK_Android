# 1. SDK简介
盟主直播SDK是一款辅助于盟主直播的推流及拉流工具，此工具仅限用于盟主直播业务，需在盟主开放平台进行认证授权方可使用。盟主直播SDK主要实现功能有视频直播推流、视频直播、点播拉流及聊天等业务功能，其推流功能采用摄像头数据抓取并进行编码后推送至盟主服务器，播放功能实现对盟主直播及点播视频的编解码播放。盟主直播SDK接入方式简洁、方便，其功能实现效果稳定、高效。
# 2. 盟主直播Android SDK架构设计
盟主直播Android SDK架构以CBU设计模式进行搭建，Core为核心层面技术，内部实现了推流编码及网络请求等功能，Business业务层处理了播放器控制器及MVP业务架构等所有业务控制工作，UserInterface为用户对接层，此层面封装了所有面向接入端所需要的业务接口及管理器。具体结构请看下图。
![](https://wmz.zmengzhu.com/uploads/201811/5bdac3a253f9d_5bdac3a2.png)

# 3. SDK功能及支持
### 3.1.  设备和系统要求
支持Android 4.3及以上系统
支持所有装有Android系统的硬件设备

### 3.2 功能支持
直播

默认推流UI、普通视频直播、纯语音直播、横屏直播、竖屏直播、直播时静音、美颜动态调节、分辨率动态调节、镜像、闪光灯、前后置摄像头动态切换、直播开始倒计时、实时展示帧率、直播时长、设置直播所属分类、设置直播间禁言、设置直播的观看方式

播放

默认播放UI、竖屏播放、二分屏播放、横屏播放、投屏、倍速、防录屏、小窗口播放、弹幕组件、 视屏封面、开屏广告、片头视频广告、滚动广告、F码观看、白名单观看、自定义播放控制栏、活动配置实时更改

聊天

文字聊天、表情聊天(可自定义表情)、用户信息获取、聊天历史记录是否显示、在线观众列表、禁言用户、踢出用户、聊天公告设置、只显示主播聊天信息

互动

文档功能、问答功能、礼物功能、签到功能、投票功能、抽奖功能

商品

商品列表、循环播放推荐商品、

下载

视频多线程下载、实时展示进度、支持后台下载、直播下载完成后播放

上传

视频多线程上传、多任务上传，支持后台上传

# 4. 介绍
#### 运行MengzhuSDK_Android工程demo需要申请secretKey，Appid两个参数用以验证。
##### 定义
Appid：为接入方申请的唯一标识。
secretKey：用以加密验证。

# 4. 项目目录
#### 4.1 sdkdemo
sdkdemo 为接入盟主Android SDK实现方式的demo示例代码，调用了快速集成方式的模板。
#### 4.2 MZMediaSDK
MZMediaSDK 为快速集成方式提供的固定UI模板 module工程。以android-library方式提供，内部实现业务上提供的所有功能逻辑及UI的模板。使用时将其library导入项目进行简单配置即可使用。具体配置代码查看快速集成文档。
### 注意
MZMediaSDK 是一个 UI 库，封装了盟主SDK功能常用的控件、fragment 等等，旨在帮助开发者快速集成盟主SDK。
Fragment的使用更适用于ui改动较少或者着急出功能的开发者，建议开发者基于MZMediaSDK的控件做开发，这样更灵活。
MZMediaSDK包含一个最简单的使用demo，开发者可导出查看。
注意：因为这是一个 UI 库，后续很可能还会继续改动，新旧版本在 API 的兼容上不会像其他组件SDK那样绝对的兼容。
注意：单独使用MZMediaSDK时请参考demo中的实现，确保申请了相关的权限。

# 5. 混淆剔除配置

    -dontwarn com.mengzhu.**
    -keep class  com.mengzhu.** { *; }
    -dontwarn tv.mengzhu.**
    -keep class  tv.mengzhu.** { *; }
    -dontwarn org.fourthline.cling.**
    -keep class org.fourthline.cling.**{*;}
    //接入上传需要
    -dontwarn okio.**
    -dontwarn org.apache.commons.codec.binary.**

# 6.wiki文档
https://github.com/mengzhuSDK/MengzhuSDK_Android/wiki