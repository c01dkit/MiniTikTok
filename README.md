[TOC]

# MiniTikTok

## 项目要求

* 最低要求
  * 有视频列表，需联网
  * 点击单个视频能看视频播放
  * 上下滑动能切换视频, 有点赞功能需要有动效效果，可本地
  * 能拍摄视频, 在视频列表能看到
  * 返回能回到视频列表

* 加分项目
  * 其他功能比如注册,登录,收藏等算加分项目, 只做本地
  * 视觉效果也算加分
  * 其他

* 大作业最后一天评审，需要准备PPT介绍作品特色、演示 

## 分工与项目完成时间线

|时间        |作者  |更新内容               |
|:---------:|:-----:|:------------------:|
| 07-12 22:26 | 常博宇 | 初步完成loading界面基本布局，建立项目仓库 |
| 07-14 23:30 | 顾核金 | 完善“我”界面，新增登录界面，新建README |
| 07-16 00:40 | 常博宇 |初步完成视频拍摄及保存，util存在包导入的error|
| 07-16 01:30 | 顾核金 | 新增视频点击播放Activity，但没有做完，因为增加的库有问题；**修改CursorLoader为正确的库**|
| 07-16 17:20 | 常博宇 | 自定义视频和封面的拍摄及上传 |
| 07-16 18:00 | 顾核金 | 完善“首页”界面，实现滑动切换视频；修改ClickVideoActivity，消除bug |
| 07-16 20:30 | 常博宇 | 内置登录功能，更换缓冲lottie |
| 07-16 21:03 | 顾核金 | 完善“消息”界面和“西湖界面” |
| 07-16 22:36 | 常博宇 | 替换应用内各个图标，更换消息页面为recycler |
|07-17 0:22|顾核金|调整点赞图标间距，修复actionbar的bug，更新消息页面|
|07-17 1:31|常博宇|pagerview2+tablayout完成“我”页面，调成用户头像为随机网络图片|
|07-17 3:12|顾核金|制作PPT|
|07-17 6:57|常博宇|制作PPT，发布发行版app|

## 目前问题

* ~~首页的视频在用户切换到其他界面时依然在播放~~
* ~~视频页面的图标有灰色背景框~~
* ~~图标下应有数字~~ 由于传不进参数，无法做到随着点赞增加
* ~~视频页面需增加用户头像和名~~ 传不进用户名qwq
* 西湖界面应该增加用户头像（是否也要支持Recycler功能？
*  ~~消息界面需要Recycler~~
* ~~播放完第一个视频后会出现ActionBar~~ （在manifest文件中设置NoActionBar一劳永逸hhh）
* 做PPT hhhh





