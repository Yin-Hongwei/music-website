<h1 align="center">music-website</h1>

<br/>

<h1 align="center"><font color="red">声明</font></h1>

**这项目我一直作为技术分享，不做收费（版权归我个人独有，大家拿来学习交流随时欢迎，拒绝商用）。希望大家可以尊重下我的劳动成果，谢谢。**

<br/>

## 项目说明

本音乐网站的客户端和管理端使用 **Vue** 框架来实现，服务端使用 **Spring Boot + MyBatis** 来实现，数据库使用了 **MySQL**。实现思路可以看 **[这里](https://yin-hongwei.github.io/2019/03/04/music/#more)**；项目启动方法看文章末尾。

<br/>

## 项目预览

> 前台截图预览

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gjdm8x3jj21c00u00ui.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gje55hgxj21c00u0n3v.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gk5fxmwxj21c00u0wm2.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gk5rtelgj21c00u00w7.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gk6a1b8wj21c00u0tf2.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gkl6bu35j21c00u00wb.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gklntw77j21c00u077j.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gkokestbj21c00u0ju8.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gknhg12sj21c00u00v4.jpg)

<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gknu0rszj21c00u0jto.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h1gkoxoehnj21c00u0q5j.jpg)<br/>

> 后台截图预览

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h158xvsdvij21c00u0wi8.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159x0re56j21c00u077a.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159xzbi85j21c00u0whn.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159zewsh4j21c00u079f.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159yz5x8hj21c00u0win.jpg)<br/>

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h159yo2nzmj21c00u0djp.jpg)<br/>

## 项目功能

- 音乐播放
- 用户登录注册
- 用户信息编辑、头像修改
- 歌曲、歌单搜索
- 歌单打分
- 歌单、歌曲评论
- 歌单列表、歌手列表分页显示
- 歌词同步显示
- 音乐收藏、下载、拖动控制、音量控制
- 后台对用户、歌曲、歌手、歌单信息的管理

<br/>

## 技术栈

### 后端

**SpringBoot + MyBatis**

### 前端

**Vue3.0 + TypeScript + Vue-Router + Vuex + Axios + ElementPlus + Echarts**

<br/>

## 开发环境

JDK： jdk-8u141

mysql：mysql-5.7.21-1-macos10.13-x86_64（或者更高版本）

node：v14.17.3

IDE：IntelliJ IDEA 2018、VSCode


<br/>

## 下载运行

### 1、下载项目到本地

```bash
git clone git@github.com:Yin-Hongwei/music-website.git

# 上面下载慢可以用下面这个
git clone git@gitee.com:Yin-hongwei/music-website.git
```

### 2、下载数据库中记录的资源

去【链接: https://pan.baidu.com/s/1Qv0ohAIPeTthPK_CDwpfWg 提取码: gwa4 】下载网站依赖的歌曲及图片，将 data 夹里的文件放到 music-server 文件夹下。

> 注意：资源整理了一下，按照下面的截图存放。

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gsk65seqd8j30y00ocjt4.jpg" height="200px"/>

### 3、修改配置文件

1）创建数据库
将 `music-website/music-server/sql` 文件夹中的 `tp_music.sql` 文件导入数据库。

2）修改用户名密码
修改 `music-website/music-server/src/main/resources/application.properties` 文件里的 `spring.datasource.username` 和 `spring.datasource.password`；

### 4、启动项目

- **启动管理端**：进入 music-server 文件夹，运行下面命令启动服务器

```js
// 方法一
./mvnw spring-boot:run

// 方法二
mvn spring-boot:run // 前提装了 maven
```

- **启动客户端**：进入 music-client 目录，运行下面命令

```js
npm install // 安装依赖

npm run serve // 启动前台项目
```

- **启动管理端**：进入 music-manage 目录，运行下面命令

```js
npm install // 安装依赖

npm run serve // 启动后台管理项目
```

<br/>

## 赞助

如果此项目对你确实有帮助，欢迎给我打赏一杯咖啡～😄

<img src="https://tva1.sinaimg.cn/large/008i3skNgy1gsgm6jokvaj30gs0feadd.jpg" height="300px"/>

<br/>

## License

Copyright (c) 2018 Yin-Hongwei 

