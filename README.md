# java-prometheus-reload
prometheus registered agent

prometheus注册代理，该服务暴露一个http接口进行对prometheus.yml配置文件的修改和刷新

使用方法

### 一、先安装一个Prometheus 
1.下载 
https://prometheus.io/download/

2.解压 
tar xvfz prometheus-*.tar.gz

3.启动 prometheus
nohup ./prometheus --config.file=prometheus.yml --web.enable-lifecycle &


### 二、安装java-prometheus-reload
1.下载源码

2.编译java-prometheus-reload 
mvn assembly:assembly

3.启动（和Prometheus部署在同一台机器）
nohup java -Dprometheus.yml.path=/workspace/service/prometheus-2.3.2.linux-amd64/prometheus.yml -Dprometheus.port=9090 -Dport=9091  -jar java-prometheus-reload-jar-with-dependencies.jar &

-Dprometheus.yml.path= prometheus.yml文件地址
-Dprometheus.port=prometheus的端口号
-Dport=java-prometheus-reload启动的端口号
