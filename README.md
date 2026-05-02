<h1 align="center">music-website</h1>

<p align="center"><a href="README.en.md">English README</a></p>

<br/>

<h1 align="center"><font color="red">声明</font></h1>

**这项目我一直作为技术分享，不做收费（版权归我个人独有，大家拿来学习交流随时欢迎，拒绝商用）。希望大家可以尊重下我的劳动成果，谢谢。**

<br/>

## 项目说明

本音乐网站的客户端和管理端使用 **Vue** 框架来实现，服务端使用 **Spring Boot + MyBatis** 来实现，数据库使用了 **MySQL**。实现思路可以看 **[这里](https://www.yuque.com/yinhongwei-ya0u7/bk3hhk/nu3i7emefxvetpz8)**；项目启动方法看文章末尾。

<br/>

## 项目预览

> 前台截图预览

![](https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icrcbfjswyj32c01ik4a1.jpg)<br/>

![](https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7ma2nisj32c01ik7wi.jpg)<br/>

![](https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7mwfp2sj32c01ikqv6.jpg)<br/>

![](https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icr7pxg6wlj32c01ik7tu.jpg)<br/>

![](https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr7nn7l7tj32c01ik1ky.jpg)<br/>

![](https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icr7pxg6wlj32c01ik7tu.jpg)<br/>

![](https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr7r9yn98j32c01iku0x.jpg)<br/>

![](https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7s5lnmjj32c01iknd9.jpg)<br/>

![](https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr7sv48ohj32c01ikqe5.jpg)<br/>

![](https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr7un2vyvj32c01ikqht.jpg)<br/>


> 后台截图预览

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h158xvsdvij21c00u0wi8.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159x0re56j21c00u077a.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159xzbi85j21c00u0whn.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159zewsh4j21c00u079f.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yz5x8hj21c00u0win.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yo2nzmj21c00u0djp.jpg)<br/>

## 项目功能

- 用户登录注册
- 用户信息编辑
- 歌单推荐；歌单、歌手展示
- 歌曲、歌单搜索
- 歌单、歌曲评论、收藏
- 歌词同步显示
- 音乐播放、收藏、下载、拖动控制、音量控制
- 后台对 banner、用户、歌曲、歌手、歌单信息的管理

<br/>

## 技术栈

### 后端

**SpringBoot + MyBatis + Redis + Minio**

### 前端

**Vue3 + TypeScript + Vue-Router + Pinia + Axios + ElementPlus**

### 部署

**docker**

<br/>

## 开发环境

JDK： jdk-8u141

mysql：mysql-5.7.21-1-macos10.13-x86_64（或者更高版本）

redis：5.0.8 或 [docker启动redis | 想飞跃的鱼 (nanshaws.github.io)](https://nanshaws.github.io/docker/docker启动redis(完美过程).html)

node：14+

IDE：IntelliJ IDEA 2018、VSCode

minio: 下载本地最新 或者 [docker完美启动minio | 想飞跃的鱼 (nanshaws.github.io)](https://nanshaws.github.io/docker/docker完美启动minio(完美过程).html)

<br/>

## 下载运行

### 1、下载项目到本地

```bash
git clone git@github.com:Yin-Hongwei/music-website.git
```

### 2、下载数据库中记录的资源

去【链接: https://pan.baidu.com/s/1Qv0ohAIPeTthPK_CDwpfWg 提取码: gwa4 】下载网站依赖的歌曲及图片，将 data 夹里的文件放到 music-server 文件夹下。

> **注意：资源整理了一下，按照下面的截图存放。**

<img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h6gz1le9wxj20fo0gggmh.jpg" height="200px"/>

### 3、修改配置文件

1）创建数据库
将 `music-website/sql` 文件夹中的 `yin_music.sql` 文件导入数据库。

2）修改用户名密码
修改 `music-website/music-server/src/main/resources/application.properties` 文件里的 `spring.datasource.username` 和 `spring.datasource.password`；

### 4、启动项目

- **启动管理端**：进入 music-server 文件夹，运行下面命令启动服务器

```bash
# macOS / Linux（推荐，使用项目自带 Maven Wrapper）
./mvnw clean spring-boot:run

# Windows（推荐，使用项目自带 Maven Wrapper）
mvnw.cmd clean spring-boot:run

# 备选：本机已安装 Maven 时
mvn clean spring-boot:run
```

- **启动 redis**：直接在终端输入下面命令

```
redis-server
```

> 下载地址：https://redis.io/
>
> Mac 安装使用示例：https://www.jianshu.com/p/ce27d9ab4f8c

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

### 5、常见问题

1、图片、音乐加载失败
把 music-website/music-server 目录下的 img、song 目录移动到 music-website 目录。

2、音乐播放不了
可能是音乐损毁了，重新更换一下音乐资源。

<br/>

### 6、部署在linux上，用 docker【本地运行可以忽略】

将以下东西存储到 Linux 服务器上：

![image-20240108131746139](https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr99ij9s5j30bs06hq4m.jpg)

还有编译好的 jar 包，都放到同一目录里面，如下：

![image-20240108131824788](https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99ioipxj3082076my5.jpg)

```
docker compose up --build
```

运行结果：

![image-20240108131927175](https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99iumhpj319y0hjwyi.jpg)

<br/>

## 贡献者

感谢所有为本仓库提交过代码与改进建议的贡献者。

<a href="https://github.com/Yin-Hongwei/music-website/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Yin-Hongwei/music-website" alt="Contributors" />
</a>

<br/>

## 赞助

如果此项目对你确实有帮助，欢迎给我打赏一杯咖啡～😄

<img src="https://piccdn2.umiwi.com/fe-oss/default/MTc1NTU4NzgxOTM3.png" width="300px"/>

<br/>

## License

This project is licensed under the Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0) license. The project cannot be used for commercial purposes.

Copyright (c) 2018 Yin-Hongwei 
