<h1 align="center">music-website</h1>
<br/>

<p align="center">
  <a href=""><img alt="license" src="https://img.shields.io/github/license/Yin-Hongwei/music-website"></a>
</p>


## 项目说明

本音乐网站的开发主要利用 VUE 框架开发前台和后台，后端接口用 Spring Boot + MyBatis 来实现，数据库使用的是 MySQL。实现思路可以参考[这里](https://yin-hongwei.github.io/2019/03/04/music/)。

<br/>

## 项目截图

> 前台截图预览

![](https://tva1.sinaimg.cn/large/006tNbRwly1g9hkbfdtbej31c00u0jyu.jpg)<br/>

![](https://tva1.sinaimg.cn/large/006y8mN6ly1g9e97bwxifj31c00u0npe.jpg)<br/>

![](https://tva1.sinaimg.cn/large/006y8mN6ly1g9e98u647cj31c00u0x6q.jpg)<br/>

![](https://tva1.sinaimg.cn/large/006tNbRwly1g9hl4b9jjdj31c00u0nmn.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdj8iqeynwj31c00u0kjm.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gd3867tq24j31c00u0tnw.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gd386h28hqj31c00u07vj.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gd388evmh0j31c00u07ha.jpg)<br/>

![](https://tva1.sinaimg.cn/large/006y8mN6ly1g9e9d8b2ukj31c00u0dof.jpg)<br/>

> 后台截图预览

![](https://tva1.sinaimg.cn/large/006tNbRwly1g9hhhu4n7tj31c00u04qq.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdj8jf3uusj31c00u0n5b.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie89mujrj31c00u07kx.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie8sox6uj31c00u01gb.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie9beckpj31c00u0qh9.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie9qq7yhj31c00u0ttq.jpg)<br/>

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

SpringBoot + MyBatis

#### 前端

Vue + Vue-Router + Vuex + Axios +  ElementUI

<br/>

## 安装

#### 1、下载项目到本地

```
git clone https://github.com/Yin-Hongwei/music-website.git
```

#### 2、下载数据库中记录的资源

去【链接: https://pan.baidu.com/s/1Qv0ohAIPeTthPK_CDwpfWg 提取码: gwa4 】下载网站依赖的歌曲及图片，将 data 夹里的文件直接放到 music-server 文件夹下。

#### 3、修改
1）数据库：将sql文件夹中的 tp_music.sql 文件导入数据库。（mysql 版本不能低于5.7）

2）music-server：启动后端服务之前，有一些地方需要修改，先去 /music-website/music-server/src/main/resources 这个目录下的文件里修改自己的 spring.datasource.username 和 spring.datasource.password，并且修改下圈出来的的文件中 MyPicConfig 类下的 addResourceLocations方法中的路径，否则资源加载不了。（Mac 和 win 下路径有些差异，我的是 Mac 上的路径，win 上需要在 file: 后标明是哪个盘，例如："file:C:\\\user\\\XXX\\\\"）

![](https://tva1.sinaimg.cn/large/00831rSTly1gd38cq6yhrj31zk0juk02.jpg)

#### 4、启动项目

music-server 是本项目的后端，用于监听前端发来的请求，提供接口。music-client 和 music-manage 都是本项目的前端部分，前者是前台，后者是后台。运行时后端必须启动，两个前端项目可以都启动，也可以只启动其中一个，他们是独立的。

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

#### 5、最后

如果对这个项目有什么疑惑，可以联系我

📮：yinhongwei96@126.com

<br/>

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2018 Yin-Hongwei 

