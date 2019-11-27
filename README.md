<h1 align="center">music-website</h1>
<br/>

<p align="center">
  <a href=""><img alt="license" src="https://img.shields.io/github/license/mashape/apistatus.svg?style=flat"></a>
</p>

## 项目说明

本音乐网站的开发主要利用前端框架VUE开发用户和管理员界面，后端接口用Spring Boot来实现，数据持久化用的是 MyBatis，数据用MySQL来进行存储。具体实现可以参考[这里](https://yin-hongwei.github.io/2019/03/04/music/)。

<br/>

## 项目截图

> 前台模块

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/denglu.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/shouye.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/gedan.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/geshou.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/my.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/gedanxiangqing.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/geshouxiangqing.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/geci.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/sousuo.png"/><br/>

> 后台模块

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/l.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/y.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/s.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/g.png"/><br/>

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/gd.png"/><br/><br/>

## 功能

- WEB 端音乐播放
- 用户登录注册
- 用户信息编辑
- 歌曲、歌单搜索
- 歌单打分
- 歌单、歌曲评论
- 分页显示
- 歌词同步显示
- 音乐收藏、下载、拖动控制
- 后台对用户、歌曲、歌手、歌单信息的管理

<br/>

## 技术栈

#### 后端

spring boot + MyBatis

#### 前端

vue + vue-router + vuex + axios +  element-ui + swiper

<br/>

## 安装

#### 1、下载项目到本地

```
git clone https://github.com/Yin-Hongwei/music-website.git
```

#### 2、下载数据库中记录的资源

去https://pan.baidu.com/s/1PjdvJ8SCJ0kUwrL1m7Dp7Q 下载网站依赖的歌曲及图片，将 data 夹里的文件直接放到 music-server 文件夹下。下载资源可能有点儿慢，这是百度云破解版[https://pan.baidu.com/disk/home#/all?vmode=list&path=%2F%E8%BD%AF%E4%BB%B6%2FPanDownload](https://pan.baidu.com/disk/home#/all?vmode=list&path=%2F软件%2FPanDownload)（适合win用户，Mac上还没发现。

#### 3、修改
1）数据库：将sql文件夹中的 tp_music.sql 文件导入数据库。

2）music-server：启动后端服务之前，有一些地方需要修改，先去 /music-website/music-server/src/main/resources 这个目录下的文件里修改自己的 spring.datasource.username 和 spring.datasource.password，并且修改下面蓝色显示的文件名中 MyPicConfig 类下的 addResourceLocations方法中的路径，否则资源加载不了。

<img src="https://github.com/Yin-Hongwei/vue-spring-music/blob/master/img/Explain.png" width="600"/>

#### 4、启动项目

然后进入 music-server 文件夹，运行下面命令启动服务器

```js
./mvnw spring-boot:run
```

进入 music-client 文件夹，运行下面命令启动前台项目

```js
npm install // 安装依赖

npm run dev // 启动前台项目
```

进入 music-manage 文件夹，运行下面命令启动后台管理项目

```js
npm install // 安装依赖

npm run dev // 启动后台管理项目
```

<br/>

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2018 Yin-Hongwei
