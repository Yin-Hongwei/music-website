package com.yin.music.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地媒体根目录配置（{@code app.media.*}）。
 * 相对路径相对进程工作目录解析；生产可改为绝对路径。
 */
@ConfigurationProperties(prefix = "app.media")
public class MediaProperties {

    /**
     * 本地媒体根目录，默认 {@code data}（即 {@code {user.dir}/data}）。
     */
    private String root = "data";

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Path rootPath() {
        Path path = Paths.get(root == null || root.isEmpty() ? "data" : root);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(path);
        }
        return path.toAbsolutePath().normalize();
    }

    public Path dataDir(String first, String... more) {
        if (more == null || more.length == 0) {
            return rootPath().resolve(first).normalize();
        }
        Path path = rootPath().resolve(first);
        for (String part : more) {
            path = path.resolve(part);
        }
        return path.normalize();
    }

    public Path imgDir(String... parts) {
        if (parts == null || parts.length == 0) {
            return dataDir("img");
        }
        Path path = dataDir("img");
        for (String part : parts) {
            path = path.resolve(part);
        }
        return path.normalize();
    }

    public Path songDir() {
        return dataDir("song");
    }

    /** Spring {@code ResourceHandler} 用的 {@code file:} 位置（带尾斜杠）。 */
    public String resourceLocation(Path directory) {
        String uri = directory.toAbsolutePath().normalize().toUri().toString();
        return uri.endsWith("/") ? uri : uri + "/";
    }
}
