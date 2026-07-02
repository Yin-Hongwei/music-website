<div align="center">

<h1 align="center">music-website</h1>

<p align="center">
  <strong>Full-stack music website with Vue 3 + Spring Boot · For learning</strong>
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
  <a href="README.md">中文</a> · <strong>English</strong>
</p>

</div>

<h3 align="center"><font color="red">Notice</font></h3>

**This project is shared for learning and technical discussion only (copyright remains with the author). You are welcome to use it for study and exchange; commercial use is not permitted. Please respect the work that went into it—thank you.**

<br/>


## Project overview

The public web client and admin console are built with **Vue**. The server uses **Spring Boot + MyBatis**, with **MySQL** as the database. For design notes, see **[Yuque (Chinese)](https://www.yuque.com/yinhongwei-ya0u7/bk3hhk/nu3i7emefxvetpz8)** or the **[blog post](https://yin-hongwei.github.io/2019/03/04/music/)**. Local setup steps are in [Quick start](#quick-start) below.


## Project structure

```
music-website/
├── music-client/     # Public web client (Vue 3)
├── music-manage/     # Admin console (Vue 3)
├── music-server/     # Backend API (Spring Boot)
├── sql/              # Database initialization scripts
└── docs/             # Documentation
```


## Preview

<details open>
<summary><b>Public site</b> · click to collapse / expand</summary>
<br/>

<table>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icrcbfjswyj32c01ik4a1.jpg" width="100%" alt="Home"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7ma2nisj32c01ik7wi.jpg" width="100%" alt="Playlists"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7mwfp2sj32c01ikqv6.jpg" width="100%" alt="Artist detail"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax1.sinaimg.cn/large/007mxWTugy1icr7pxg6wlj32c01ik7tu.jpg" width="100%" alt="Player"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr7nn7l7tj32c01ik1ky.jpg" width="100%" alt="Lyrics"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr7r9yn98j32c01iku0x.jpg" width="100%" alt="Search"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax3.sinaimg.cn/large/007mxWTugy1icr7s5lnmjj32c01iknd9.jpg" width="100%" alt="Profile"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr7sv48ohj32c01ikqe5.jpg" width="100%" alt="Sign in"/></td>
  </tr>
</table>

</details>

<details>
<summary><b>Admin console</b> · click to collapse / expand</summary>
<br/>

<table>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h158xvsdvij21c00u0wi8.jpg" width="100%" alt="Admin home"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159x0re56j21c00u077a.jpg" width="100%" alt="Users"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159xzbi85j21c00u0whn.jpg" width="100%" alt="Songs"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159zewsh4j21c00u079f.jpg" width="100%" alt="Artists"/></td>
  </tr>
  <tr>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yz5x8hj21c00u0win.jpg" width="100%" alt="Playlists"/></td>
    <td width="50%" align="center"><img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yo2nzmj21c00u0djp.jpg" width="100%" alt="Comments"/></td>
  </tr>
</table>

</details>


## Features

| Module | Capabilities |
| --- | --- |
| **Users** | Sign-in, sign-up, profile editing, personal center |
| **Discovery** | Playlist recommendations, playlists and artists, song/playlist search |
| **Interaction** | Comments and favorites on playlists and songs |
| **Player** | Play, pause, seek, volume, synced lyrics, download |
| **Admin** | Banners, users, songs, artists, playlists, comments |


## Stack

| Layer | Technologies |
| --- | --- |
| **Backend** | Spring Boot · MyBatis · Redis · MinIO |
| **Frontend** | Vue 3 · TypeScript · Vue Router · Pinia · Axios · Element Plus |
| **Deployment** | Docker · Docker Compose |


## Environment

| Dependency | Version / notes |
| --- | --- |
| **JDK** | 8+ (e.g. jdk-8u141) |
| **MySQL** | 5.7.21+ |
| **Redis** | 5.0.8+, or run via [Docker (Chinese guide)](https://nanshaws.github.io/docker/docker启动redis(完美过程).html) |
| **Node.js** | 14+ |
| **MinIO** | Local install, or [Docker (Chinese guide)](https://nanshaws.github.io/docker/docker完美启动minio(完美过程).html) |
| **IDE** | IntelliJ IDEA · VS Code |


## Quick start

### 1. Clone the repository

```bash
git clone git@github.com:Yin-Hongwei/music-website.git
cd music-website
```

### 2. Download media assets

Download songs and images from Baidu Netdisk:

- Link: https://pan.baidu.com/s/1Qv0ohAIPeTthPK_CDwpfWg
- Code: `gwa4`

Place the contents of the `data` folder under the `music-server` directory, following the layout in the screenshot below.

> **Note:** Match the directory layout shown in the screenshot.

<p align="left">
  <img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h6gz1le9wxj20fo0gggmh.jpg" height="200px" alt="Asset directory layout"/>
</p>

### 3. Configure the database

1. Create a MySQL database and import `sql/yin_music.sql`
2. Edit `music-server/src/main/resources/application.properties` and set `spring.datasource.username` and `spring.datasource.password`

### 4. Start services

Start each service in order (use separate terminal windows):

| Service | Directory | Command |
| --- | --- | --- |
| **Backend API** | `music-server` | `./mvnw clean spring-boot:run` (Windows: `mvnw.cmd`) |
| **Redis** | — | `redis-server` |
| **Public client** | `music-client` | `npm install && npm run serve` |
| **Admin console** | `music-manage` | `npm install && npm run serve` |

<details>
<summary><b>Backend startup commands</b></summary>

```bash
# macOS / Linux (recommended: project Maven Wrapper)
./mvnw clean spring-boot:run

# Windows (recommended)
mvnw.cmd clean spring-boot:run

# Alternative: system Maven
mvn clean spring-boot:run
```

</details>

<details>
<summary><b>Redis installation references</b></summary>

- Downloads: https://redis.io/
- macOS setup example (Chinese): https://www.jianshu.com/p/ce27d9ab4f8c

</details>


## Troubleshooting

| Symptom | Fix |
| --- | --- |
| Images or audio fail to load | Move the `img` and `song` folders from `music-server` to the repository root (`music-website/`) |
| Playback issues | The file may be corrupt; replace it from the asset pack |


## Docker deployment

> Optional for local development. Intended for Linux server deployment.

Copy the items below onto your Linux host:

<p align="left">
  <img src="https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr99ij9s5j30bs06hq4m.jpg" height="240px" alt="Deploy layout"/>
</p>

Place the built JAR in the same directory, as shown below:

<p align="left">
  <img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99ioipxj3082076my5.jpg" height="240px" alt="JAR placement"/>
</p>

```bash
docker compose up --build
```

Run result:

<p align="left">
  <img src="https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99iumhpj319y0hjwyi.jpg" height="280px" alt="Docker run result"/>
</p>


## Contributors

Thanks to everyone who has contributed code and improvement suggestions to this repository.

<a href="https://github.com/Yin-Hongwei/music-website/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Yin-Hongwei/music-website" alt="Contributors" />
</a>


## Support

If this project helped you, consider buying the author a coffee.

<img src="./docs/assets/sponsor-qr.png" height="300px" alt="WeChat tip QR code"/>

<br/>

## Contact

**1. Email: [yinhongwei96@126.com](mailto:yinhongwei96@126.com)**

**2. WeChat official account**

<img src="./docs/assets/wechat-official-account-qr.png" alt="WeChat official account YinHongwei"/>

<br/>


## License

This project is licensed under the [Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)](https://creativecommons.org/licenses/by-nc/4.0/) license. **Commercial use is not permitted.**

Copyright (c) 2018 Yin-Hongwei
