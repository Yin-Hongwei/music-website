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

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec0a2vd9j31c00u0n4z.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec0qtdxrj31c00u07wj.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec19x0e6j31c00u0npe.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec1nmbt4j31c00u0hcf.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec1yc0gkj31c00u0kjm.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec29vvdtj31c00u0nok.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec2ixqk1j31c00u0qf8.jpg)

<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec31i06gj31c00u0wtw.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec3ozxt9j31c00u0qbv.jpg)<br/>

![](https://tva1.sinaimg.cn/large/007S8ZIlly1geec41r7onj31c00u047y.jpg)<br/>

> 后台截图预览

![](https://tva1.sinaimg.cn/large/006tNbRwly1g9hhhu4n7tj31c00u04qq.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdj8jf3uusj31c00u0n5b.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie89mujrj31c00u07kx.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie8sox6uj31c00u01gb.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie9beckpj31c00u0qh9.jpg)<br/>

![](https://tva1.sinaimg.cn/large/00831rSTly1gdie9qq7yhj31c00u0ttq.jpg)<br/>

## 功能

- 音乐播放
- 用户登录注册
- 用户信息编辑、头像修改
- 歌曲、歌单搜索
- 歌单打分
- 歌单、歌曲评论
- 歌单列表、歌手列表分页显示
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

<img src="https://tva1.sinaimg.cn/large/007S8ZIlly1gekwp2wqxuj311v0u0du2.jpg" height="200px"/>

#### 3、修改
1）数据库：将sql文件夹中的 tp_music.sql 文件导入数据库。

2）music-server：启动后端服务之前，有一些地方需要修改，先去 /music-website/music-server/src/main/resources 这个目录下的文件里修改自己的 spring.datasource.username 和 spring.datasource.password，并且修改下圈出来的的文件中 MyPicConfig 类下的 addResourceLocations方法中的路径，否则资源加载不了。（Mac 和 win 下路径有些差异，我的是 Mac 上的路径，win 上需要在 file: 后标明是哪个盘，例如："file:C:\\\user\\\XXX\\\\"）

![](https://tva1.sinaimg.cn/large/00831rSTly1gd38cq6yhrj31zk0juk02.jpg)

#### 4、启动项目

music-server 是本项目的后端，用于监听前端发来的请求，提供接口。music-client 和 music-manage 都是本项目的前端部分，前者是前台，后者是后台。运行时后端必须启动，两个前端项目可以都启动，也可以只启动其中一个，他们是独立的。

然后进入 music-server 文件夹，运行下面命令启动服务器

```js
// 方法一
./mvnw spring-boot:run

// 方法二
mvn spring-boot:run // 前提装了 maven
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

## 声明

这个项目我拿出来是纯分享技术的，没有在任何地方收费，但凡发现有收费的地方，有发现的人把那个链接发我邮箱 yinhongwei96@126.com ，或者在 Issues 中补充一下，谢谢。也希望大家和我一起举报这些人，那些盗了项目还死不要脸的我就走法律程序。

下面是目前发现拿我项目骗钱的人及证据。

### 1、[第一个骗钱的]

- B站：https://space.bilibili.com/356629729?spm_id_from=333.788.b_765f7570696e666f.1

![](https://tva1.sinaimg.cn/large/007S8ZIlly1gekw732jwpj31f60u0b2a.jpg)

- csdn：https://blog.csdn.net/qq_40985788/

![](https://tva1.sinaimg.cn/large/007S8ZIlly1gekwcta099j31gb0u01ae.jpg)



## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2018 Yin-Hongwei 

