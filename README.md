# vue-spring-music

## 概述

本音乐网站的开发主要利用前端框架VUE开发用户和管理员界面，后端接口用Spring Boot来实现。数据用MySQL来进行存储。



## 功能概述

- WEB 端音乐播放
- 用户登录注册
- 用户信息编辑
- 歌曲、歌单搜索
- 歌单打分
- 歌单、歌曲评论
- 歌词同步显示
- 音乐收藏、下载、拖动控制

- 后台对用户、歌曲、歌手、歌单信息的管理



## 技术栈：

vue + vue-router + vuex + axios + spring boot + layui-src + element-ui + mint-ui + muse-ui + swiper + vue-aplayer



## 项目截图：

![denglu](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/denglu.png)

![shouye](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/shouye.png)

![gedan](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/gedan.png)

![geshou](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/geshou.png)

![my](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/my.png)

![gedanxiangqing](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/gedanxiangqing.png)

![geshouxiangqing](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/geshouxiangqing.png)

![geci](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/geci.png)

![sousuo](/Users/yhw/Documents/github-workspace/vue-spring-music/Explain/sousuo.png)





## 项目地址

https://github.com/Yin-Hongwei/vue-spring-music



## 使用

1. git clone https://github.com/Yin-Hongwei/vue-spring-music.git 下载本项目到本地
2. 将 tp_music.sql 文件导入数据库
3. 去.https://pan.baidu.com/s/1PjdvJ8SCJ0kUwrL1m7Dp7Q 下载网站依赖的歌曲及图片，将 data 夹里的文件直接放到 music-server 文件夹下
4. 进入 music-server 文件夹，运行 ./mvnw spring-boot:run 启动服务器
5. 进入 music-client 文件夹，运行 npm install 安装依赖，运行npm run dev 启动前台项目
6. 进入 music-manage 文件夹，运行 npm install 安装依赖，运行npm run dev 启动后台管理项目



## 注意

启动服务器前要修改蓝色显示的文件名中 MyPicConfig 类下的 addResourceLocations方法中的路径，否则资源加载不了。

<img src="https://github.com/Yin-Hongwei/vue-spring-music./blob/master/Explain.png" width="400"/><br/> 