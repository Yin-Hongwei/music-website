# 使用官方的Node.js基础镜像作为基础镜像
FROM node:14
# 设置工作目录
WORKDIR /appManage
# 将package.json和package-lock.json复制到工作目录
COPY package*.json ./
# 安装依赖
RUN npm install
# 将前端项目（通常是dist文件夹）复制到工作目录,记住要提前将vue打包成dist文件夹
COPY dist /appManage/dist
# 暴露端口
EXPOSE 8081
# 运行命令
CMD ["npm", "start"]
