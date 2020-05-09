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
提供固定ui模板  
支持直播间聊天  
支持历史消息获取  
支持商品列表  
支持直播间信息获取  
支持在线观众展示  
支持主播信息获取  

# 4. 固定UI模板配置及实现方式
#### 固定UI模板使用Module android-library方式提供，内部实现业务上提供的所有功能逻辑及UI。使用时将其library导入项目进行简单配置即可使用。具体配置代码请查看下发示例.
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
###  4.3 代码实现

#### Application类内onCreate调用以下代码：
      MZSDKInitManager.getInstance().initApplication(this);

#### 初始化关键数据
        //用户信息
    UserDto dto=new UserDto();
    dto.setAccountNo(用户Id);
    dto.setAppid(appId);
    dto.setAvatar(头像地址);
    dto.setNickname(昵称);
    URLParamsUtils.setDebug(true); //开启debug模式
    URLParamsUtils.setSecretKey(自己的Secret_Key);
    MyUserInfoPresenter.getInstance().saveUserinfo(dto); //保存用户信息
        
#### 播放直播间activity实现
    MUIImmerseUtils.setStatusTranslucent(getWindow(),this);//super.onCreate之前调用 设置顶部状态
    super.onCreate(savedInstanceState);
    setTheme(R.style.AppCompatTheme);//设置主题
    setContentView(...);
    MUIImmerseUtils.setStatusTextColor(false,this);//设置顶部状态栏字体颜色
    
#### 播放直播间SDK fragment实现
    mPlayerFragment = PlayerFragment.newInstance(String Uid,String Appid,String avatar,String nickName, String ticketId)
    mPlayerFragment.setIPlayerClickListener(IPlayerClickListener);//IPlayerClickListener 是fragmentUI点击回调 具体逻辑需要接入方自行实现。
    
#### IPlayerClickListener介绍
    void onAvatarClick()//点击主播头像
    void onAttentionClick()//关注
    void onOnlineClick()//在线人数
    void onCloseClick()//退出
    void onReportClick()//举报
    void onShareClick()//分享
    void onLikeClick()//点赞
    void onRecommendGoods()//推荐商品
### 4.3.1推流SDK
#### 推流Activity实现
        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTranslucent(getWindow(), this);
        //根据需要选择是竖屏推还是横屏推
        screen = getIntent().getIntExtra("screen",0);
        switch (screen){
            case 1:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);竖屏
                break;
            case 2:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);横屏
                break;
        }
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(...);
        MUIImmerseUtils.setStatusTextColor(false, this);
    }
#### 推流Fragment实现

    //推流前默认配置
    LiveConfigDto liveConfigDto = new LiveConfigDto();
    //码率 标清500 高清800 超清1000
    liveConfigDto.setBitrate(getIntent().getIntExtra("bitrate",0));
    //美颜
    liveConfigDto.setCbbeauty(getIntent().getBooleanExtra("cbbeauty",false));
    //前后置摄像头
    liveConfigDto.setCblater(getIntent().getBooleanExtra("cblater",false));
    //静音
    liveConfigDto.setCbAudio(getIntent().getBooleanExtra("cbAudio",false));
    //调用盟主提供的创建直播Api获取的live_tk
    liveConfigDto.setLive_tk(getIntent().getStringExtra("live_tk"));
    //活动id
    liveConfigDto.setTicketId(getIntent().getStringExtra("ticketId"));
    //全体禁言
    liveConfigDto.setAllBanChat(getIntent().getBooleanExtra("cbAllBanChat",false)?0:1);
    //FPS 15-30
    liveConfigDto.setFps(Integer.parseInt(getIntent().getStringExtra("fps")));
    //开始倒计时
    liveConfigDto.setTime(Integer.parseInt(getIntent().getStringExtra("time")));
    //活动信息
    mPlayInfoDto = new PlayInfoDto();
    //消息信息 具体请查看API文档
    mPlayInfoDto.setMsg_config(startBroadcastInfoDto.getMsg_conf());
    //长连接 具体请查看API文档
    mPlayInfoDto.setChat_config(startBroadcastInfoDto.getChat_conf());
    //活动id
    mPlayInfoDto.setTicket_id(startBroadcastInfoDto.getTicket_id());
    //频道id
    mPlayInfoDto.setChannel_id(startBroadcastInfoDto.getChannel_id());
    //参数1 推流地址 2活动id 3横竖屏 4活动必要信息 5推流前默认配置集合()
    mzPlugFlowFragement=MZPlugFlowFragement.newInstance(startBroadcastInfoDto.getPush_url(),startBroadcastInfoDto.getTicket_id()
                ,screen,mPlayInfoDto,liveConfigDto);
#### IPushClickListener推流UI点击回调
    /**
     * 点击结束直播
     */
    void onStopLive();
    /**
     * 点击聊天用户头像回调
     */
    void onChatAvatar(ChatTextDto chatTextDto);
    /**
     * 点击全体禁言
     */
    void onALLBanChat();
    /**
     * 点击单体禁言
     */
    void onBanChat();
    /**
     * 点击分享
     */
    void onShare(PlayInfoDto dto);
    /**
     * 点击主播头像
     */
    void onLiveAvatar();
    /**
     * 点击在线人数
     */
    void onOnlineNum(List<MZOnlineUserListDto> mzOnlineUserListDto);
#### 推流SDK build.gradle配置
    
    api 'com.github.mengzhuSDK:MZKitLiveSDK:1.0.2'

#### 下载SDK build.gradle配置
  
    api 'com.github.mengzhuSDK:MZKitDownloadSDK:1.0.5'
    
#### 项目build.gradle配置
    
![](https://s1.zmengzhu.com/upload/img/b5/e0/b5e074ae8ca9e0935a780c9f7246fe02.jpeg)    

    repositories {
      maven { url "https://jitpack.io"}
    }