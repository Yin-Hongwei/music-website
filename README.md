<div align="center">

<h1 align="center">music-website</h1>

<p align="center">
  <strong>基于 Vue 3 + Spring Boot 的全栈音乐网站 · 学习交流</strong>
</p>

<br/>

<p align="center">
  <a href="https://github.com/Yin-Hongwei/music-website/stargazers"><img alt="GitHub stars" src="https://img.shields.io/github/stars/Yin-Hongwei/music-website?style=flat-square"></a>
  <a href="https://github.com/Yin-Hongwei/music-website/network/members"><img alt="GitHub forks" src="https://img.shields.io/github/forks/Yin-Hongwei/music-website?style=flat-square"></a>
  <a href="https://github.com/Yin-Hongwei/music-website/graphs/contributors"><img alt="Contributors" src="https://img.shields.io/github/contributors/Yin-Hongwei/music-website?style=flat-square"></a>
  <a href="https://github.com/Yin-Hongwei/music-website/issues"><img alt="Issues" src="https://img.shields.io/github/issues-search/Yin-Hongwei/music-website?query=is%3Aissue&label=issues&style=flat-square"></a>
  <a href="https://github.com/Yin-Hongwei/music-website/pulls"><img alt="Pull requests" src="https://img.shields.io/github/issues-search/Yin-Hongwei/music-website?query=is%3Apr&label=pull%20requests&style=flat-square"></a>
  <a href="https://github.com/Yin-Hongwei/music-website/discussions"><img alt="Discussions" src="https://img.shields.io/github/discussions/Yin-Hongwei/music-website?style=flat-square"></a>
  <a href="https://github.com/Yin-Hongwei/music-website/blob/master/LICENSE"><img alt="License: CC BY-NC 4.0" src="https://img.shields.io/badge/license-CC%20BY--NC%204.0-lightgrey?style=flat-square"></a>
</p>

<p align="center">
  <img alt="Vue 3" src="https://img.shields.io/badge/Vue_3-4FC08D?style=flat-square&logo=vuedotjs&logoColor=white">
  <img alt="TypeScript" src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white">
  <img alt="Pinia" src="https://img.shields.io/badge/Pinia-FFD859?style=flat-square&logo=pinia&logoColor=black">
  <img alt="Element Plus" src="https://img.shields.io/badge/Element_Plus-409EFF?style=flat-square&logo=element&logoColor=white">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white">
  <img alt="MyBatis" src="https://img.shields.io/badge/MyBatis-red?style=flat-square">
  <img alt="MySQL" src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white">
  <img alt="Redis" src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white">
  <img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white">
</p>

<br/>

<p align="center">
  <strong>中文</strong> · <a href="README.en.md">English</a>
</p>

</div>

<h3 align="center"><font color="red">声明</font></h3>

**这项目我一直作为技术分享，不做收费（版权归我个人独有，大家拿来学习交流随时欢迎，拒绝商用）。希望大家可以尊重下我的劳动成果，谢谢。**

<br/>


## 项目说明

本音乐网站的客户端和管理端使用 **Vue** 框架来实现，服务端使用 **Spring Boot + MyBatis** 来实现，数据库使用了 **MySQL**。实现思路可参考 **[语雀文档](https://www.yuque.com/yinhongwei-ya0u7/bk3hhk/nu3i7emefxvetpz8)** 或 **[博客文章](https://yin-hongwei.github.io/2019/03/04/music/)**。本地启动步骤见下方 [快速开始](#快速开始)。

<br/>

## 项目结构

```
music-website/
├── music-client/     # 前台 Web 客户端（Vue 3）
├── music-manage/     # 后台管理端（Vue 3）
├── music-server/     # 后端 API（Spring Boot）
├── sql/              # 数据库初始化脚本
└── docs/             # 文档
```

<br/>

## 项目预览

<details open>
<summary><b>前台截图</b> · 点击折叠 / 展开</summary>
<br/>

<table>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icrcbfjswyj32c01ik4a1.jpg" width="100%" alt="前台首页"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7ma2nisj32c01ik7wi.jpg" width="100%" alt="歌单列表"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7mwfp2sj32c01ikqv6.jpg" width="100%" alt="歌手详情"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icr7pxg6wlj32c01ik7tu.jpg" width="100%" alt="播放器"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr7nn7l7tj32c01ik1ky.jpg" width="100%" alt="歌词页"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr7r9yn98j32c01iku0x.jpg" width="100%" alt="搜索页"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7s5lnmjj32c01iknd9.jpg" width="100%" alt="个人中心"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr7sv48ohj32c01ikqe5.jpg" width="100%" alt="登录注册"/></td>
  </tr>
</table>

</details>

<details>
<summary><b>后台截图</b> · 点击折叠 / 展开</summary>
<br/>

<table>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h158xvsdvij21c00u0wi8.jpg" width="100%" alt="后台首页"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159x0re56j21c00u077a.jpg" width="100%" alt="用户管理"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159xzbi85j21c00u0whn.jpg" width="100%" alt="歌曲管理"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159zewsh4j21c00u079f.jpg" width="100%" alt="歌手管理"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yz5x8hj21c00u0win.jpg" width="100%" alt="歌单管理"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yo2nzmj21c00u0djp.jpg" width="100%" alt="评论管理"/></td>
  </tr>
</table>

</details>

<br/>

## 项目主要功能

| 模块 | 能力 |
| --- | --- |
| **用户** | 登录注册、资料编辑、个人中心 |
| **发现** | 歌单推荐、歌单与歌手展示、歌曲/歌单搜索 |
| **互动** | 歌单与歌曲评论、收藏 |
| **播放器** | 播放、暂停、进度拖动、音量控制、歌词同步、下载 |
| **管理后台** | Banner、用户、歌曲、歌手、歌单、评论管理 |

<br/>

## 技术栈

| 层级 | 技术 |
| --- | --- |
| **后端** | Spring Boot · MyBatis · Redis · MinIO |
| **前端** | Vue 3 · TypeScript · Vue Router · Pinia · Axios · Element Plus |
| **部署** | Docker · Docker Compose |

<br/>

## 开发环境

| 依赖 | 版本 / 说明 |
| --- | --- |
| **JDK** | 8+（如 jdk-8u141） |
| **MySQL** | 5.7.21+ |
| **Redis** | 5.0.8+，或使用 [Docker 启动 Redis](https://nanshaws.github.io/docker/docker启动redis(完美过程).html) |
| **Node.js** | 14+ |
| **MinIO** | 本地安装最新版，或使用 [Docker 启动 MinIO](https://nanshaws.github.io/docker/docker完美启动minio(完美过程).html) |
| **IDE** | IntelliJ IDEA · VS Code |

<br/>

## 快速开始

### 1. 克隆仓库

```bash
git clone git@github.com:Yin-Hongwei/music-website.git
cd music-website
```

### 2. 下载媒体资源

从百度网盘下载歌曲与图片资源：

- 链接：https://pan.baidu.com/s/1Qv0ohAIPeTthPK_CDwpfWg
- 提取码：`gwa4`

将 `data` 文件夹中的内容放到 `music-server` 目录下，目录结构参考下方截图。

> **注意：** 请按截图所示路径存放资源文件。

<p align="left">
  <img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h6gz1le9wxj20fo0gggmh.jpg" height="200px" alt="资源目录结构"/>
</p>

### 3. 配置数据库

1. 在 MySQL 中创建数据库，并导入 `sql/yin_music.sql`
2. 编辑 `music-server/src/main/resources/application.properties`，修改 `spring.datasource.username` 与 `spring.datasource.password`

### 4. 启动服务

按以下顺序启动各服务（可开多个终端窗口）：

| 服务 | 目录 | 命令 |
| --- | --- | --- |
| **后端 API** | `music-server` | `./mvnw clean spring-boot:run`（Windows 使用 `mvnw.cmd`） |
| **Redis** | — | `redis-server` |
| **前台客户端** | `music-client` | `npm install && npm run serve` |
| **管理后台** | `music-manage` | `npm install && npm run serve` |

<details>
<summary><b>后端启动命令详情</b></summary>

```bash
# macOS / Linux（推荐，使用项目自带 Maven Wrapper）
./mvnw clean spring-boot:run

# Windows（推荐）
mvnw.cmd clean spring-boot:run

# 备选：本机已安装 Maven 时
mvn clean spring-boot:run
```

</details>

<details>
<summary><b>Redis 安装参考</b></summary>

- 下载：https://redis.io/
- Mac 安装示例：https://www.jianshu.com/p/ce27d9ab4f8c

</details>

<br/>

## 常见问题

| 现象 | 处理方式 |
| --- | --- |
| 图片或音乐加载失败 | 将 `music-server` 下的 `img`、`song` 目录移动到仓库根目录 `music-website/` |
| 音乐无法播放 | 资源文件可能损坏，请从网盘重新下载并替换 |

<br/>

## Docker 部署

> 本地开发可跳过本节。适用于 Linux 服务器部署。

将以下文件放到 Linux 服务器：

<p align="left">
  <img src="https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr99ij9s5j30bs06hq4m.jpg" height="240px" alt="部署目录结构"/>
</p>

还有编译好的 jar 包，都放到同一目录里面，如下：

<p align="left">
  <img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99ioipxj3082076my5.jpg" height="240px" alt="JAR 包放置"/>
</p>

```bash
docker compose up --build
```

运行结果：

<p align="left">
  <img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99iumhpj319y0hjwyi.jpg" height="280px" alt="Docker 运行结果"/>
</p>

<br/>

## 贡献者

感谢所有为本仓库提交过代码与改进建议的贡献者。

<a href="https://github.com/Yin-Hongwei/music-website/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Yin-Hongwei/music-website" alt="Contributors" />
</a>

<br/>

## 赞助

如果此项目对你确实有帮助，欢迎给我打赏一杯咖啡～

<img src="./docs/assets/sponsor-qr.png" height="300px" alt="微信打赏二维码"/>

<br/>


## 联系方式

**1、邮箱📮：[yinhongwei96@126.com](mailto:yinhongwei96@126.com)**

**2、微信公众号**

<img src="./docs/assets/wechat-official-account-qr.png" alt="微信公众号 YinHongwei"/>

<br/>

## Git History

<a href="https://www.star-history.com/#Yin-Hongwei/music-website&Date">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=Yin-Hongwei/music-website&type=Date&theme=dark" />
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=Yin-Hongwei/music-website&type=Date" />
    <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=Yin-Hongwei/music-website&type=Date" />
  </picture>
</a>

<br/>

## License

This project is licensed under the [Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)](https://creativecommons.org/licenses/by-nc/4.0/) license. **Commercial use is not permitted.**

Copyright (c) 2018 Yin-Hongwei
