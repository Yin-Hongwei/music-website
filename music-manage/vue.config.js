const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    // 固定music-manage的端口，与后端CORS白名单(app.security.cors.allowed-origins)保持一致
    port: 8081,
  },
  chainWebpack: (config) => {
    config.plugin("define").tap((definitions) => {
      Object.assign(definitions[0]["process.env"], {
        NODE_HOST: JSON.stringify(process.env.NODE_HOST || "http://localhost:8888"),
      });
      return definitions;
    });
  },
});
