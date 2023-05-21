module.exports = {
  // 配置开发服务器
  devServer: {
    // 配置代理
    proxy: {
      // 代理所有以 '/api' 开头的请求
      "/api": {
        // 将请求代理到目标服务器
        // target: 'http://40f3d3d.nat123.fun',
        // target: 'https://43.249.192.204:48724',
        // target: 'http://192.168.31.206:8000',
        target: "http://119.3.184.138:80",
        // 允许改变请求的主机名
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
};
