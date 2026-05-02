<h1 align="center">music-website</h1>

<br/>

<p align="center"><a href="README.md">中文版 README</a></p>

<br/>

<h1 align="center"><font color="red">Notice</font></h1>

**This project is shared for learning and technical discussion only (copyright remains with the author). You are welcome to use it for study and exchange; commercial use is not permitted. Please respect the work that went into it—thank you.**

<br/>

## Overview

The public web client and the admin console are built with **Vue**. The server uses **Spring Boot + MyBatis**, with **MySQL** as the database. For design notes, see **[this article (Chinese)](https://www.yuque.com/yinhongwei-ya0u7/bk3hhk/nu3i7emefxvetpz8)**. Setup steps are below.

<br/>

## Preview

> Public site

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

> Admin console

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h158xvsdvij21c00u0wi8.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159x0re56j21c00u077a.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159xzbi85j21c00u0whn.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159zewsh4j21c00u079f.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yz5x8hj21c00u0win.jpg)<br/>

![](https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h159yo2nzmj21c00u0djp.jpg)<br/>

## Features

- User sign-in and sign-up
- User profile editing
- Playlist recommendations; playlists and artists
- Search for songs and playlists
- Comments and favorites on playlists and songs
- Synced lyrics display
- Playback, favorites, download, seek, and volume control
- Admin: banners, users, songs, artists, and playlists

<br/>

## Stack

### Backend

**Spring Boot + MyBatis + Redis + MinIO**

### Frontend

**Vue 3 + TypeScript + Vue Router + Pinia + Axios + Element Plus**

### Deployment

**Docker**

<br/>

## Environment

- **JDK:** e.g. jdk-8u141
- **MySQL:** 5.7.21+ (or newer)
- **Redis:** 5.0.8+ or run via Docker — [example (Chinese)](https://nanshaws.github.io/docker/docker启动redis(完美过程).html)
- **Node.js:** 14+
- **IDE:** IntelliJ IDEA, VS Code, etc.
- **MinIO:** local install or Docker — [example (Chinese)](https://nanshaws.github.io/docker/docker完美启动minio(完美过程).html)

<br/>

## Clone and run

### 1. Clone the repository

```bash
git clone git@github.com:Yin-Hongwei/music-website.git
```

### 2. Download media assets referenced by the database

Use Baidu Netdisk — link: https://pan.baidu.com/s/1Qv0ohAIPeTthPK_CDwpfWg, code: **gwa4**. Download the songs and images the site expects, then put the contents of the `data` folder under the `music-server` directory.

> **Note:** Lay out files as shown in the screenshot below.

<img src="https://i0.wp.com/tva1.sinaimg.cn/large/e6c9d24ely1h6gz1le9wxj20fo0gggmh.jpg" height="200px"/>

### 3. Configure the app

1. **Create the database**  
   Import `sql/yin_music.sql` from the repo into MySQL.

2. **Set DB credentials**  
   Edit `music-server/src/main/resources/application.properties` and set `spring.datasource.username` and `spring.datasource.password`.

### 4. Start services

- **Backend (Spring Boot):** from the `music-server` folder:

```bash
# macOS / Linux (recommended: project Maven Wrapper)
./mvnw clean spring-boot:run

# Windows (recommended)
mvnw.cmd clean spring-boot:run

# Alternative: system Maven
mvn clean spring-boot:run
```

- **Redis:**

```
redis-server
```

> Downloads: https://redis.io/  
> macOS setup example (Chinese): https://www.jianshu.com/p/ce27d9ab4f8c

- **Public client:** from `music-client`:

```bash
npm install
npm run serve
```

- **Admin console:** from `music-manage`:

```bash
npm install
npm run serve
```

### 5. Troubleshooting

1. **Images or audio fail to load**  
   Move the `img` and `song` folders from `music-server` to the repository root (`music-website`).

2. **Playback issues**  
   The file may be corrupt; replace it with a good copy from the asset pack.

<br/>

### 6. Deploy on Linux with Docker (optional for local dev)

Copy the items below onto your Linux host:

![Deploy layout 1](https://i0.wp.com/tvax2.sinaimg.cn/large/007mxWTugy1icr99ij9s5j30bs06hq4m.jpg)

Include the built JAR in the same directory:

![Deploy layout 2](https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99ioipxj3082076my5.jpg)

```bash
docker compose up --build
```

Expected result:

![Docker run result](https://i0.wp.com/tvax4.sinaimg.cn/large/007mxWTugy1icr99iumhpj319y0hjwyi.jpg)

<br/>

## Contributors

Thanks to everyone who has contributed code and improvement suggestions to this repository.

<a href="https://github.com/Yin-Hongwei/music-website/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Yin-Hongwei/music-website" alt="Contributors" />
</a>

<br/>

## Support

If this project helped you, you are welcome to buy the author a coffee.

<img src="https://piccdn2.umiwi.com/fe-oss/default/MTc1NTU4NzgxOTM3.png" width="300px"/>

<br/>

## License

This project is licensed under the Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0) license. The project cannot be used for commercial purposes.

Copyright (c) 2018 Yin-Hongwei
