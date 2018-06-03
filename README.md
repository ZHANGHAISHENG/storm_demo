集群规划：
	主机名	   IP		           安装的软件				        运行的进程
	window     192.168.0.104       jdk、kafka、zookeeper		 
	c7         192.168.0.107       jdk、storm、zookeeper            nimbus logviewer ui QuorumPeerMain
	c8         192.168.0.108       jdk、storm、zookeeper            nimbus  supervisor logviewer  QuorumPeerMain
	c9         192.168.0.109       jdk、storm、zookeeper            supervisor logviewer QuorumPeerMain

其中QuorumPeerMain 是 zookeeper进程

开发环境搭建：
修改主机名和IP的映射关系
关闭防火墙
ssh免登陆
安装JDK，配置环境变量等
zookeeper
-- 以上参考hadoop搭建

修改apache-storm-1.2.1\conf\storm.yaml
 storm.zookeeper.servers:
     - "c7"
     - "c8"
     - "c9"
#指定哪些服务器可作为nimbus
 nimbus.seeds: ["c7", "c8"]

分别拷贝到c7、c8、c9

启动：
启动zookeeper:
目录：/usr/local/app/zookeeper-3.4.5/
c7、c8、c9  分别运行：./zkServer.sh start
查看状态：./zkServer.sh status

启动storm:
目录：/usr/local/app/apache-storm-1.2.1
c7:
nimbus：nohup bin/storm nimbus &
ui：nohup bin/storm ui  &
logviewer：nohup bin/storm logviewer  &
c8:
nimbus：nohup bin/storm nimbus  &
logviewer：nohup bin/storm logviewer  &
supervisor：nohup bin/storm supervisor &
c9:
supervisor：nohup bin/storm supervisor  &
logviewer：nohup bin/storm logviewer  &

集群模式运行jar:
idea打包移除storm-core （依赖jar会打包进class里面）
bin/storm jar  /usr/local/app/storm_demo.jar com.storm.kafka.KafkaTopology kafka_test
输出日志：查看logs/workers-artifacts 下的worker.log 日志

关闭topology:
bin/storm kill topology名称(kafka_test) 或在c7:8080 界面直接kill

关闭集群：
jps kill 进程号

本地模式：
直接运行KafkaTopology main既可

注意： kafka集成1.0+ 官网读取数据编写方式无法实现，部分类不存在

参考文档：
原理：
https://blog.csdn.net/weiyongle1996/article/details/77142245?utm_source=gold_browser_extension

集群环境搭建：
https://blog.csdn.net/bbaiggey/article/details/77017230

编程示例：
简单之美：http://shiyanjun.cn/archives/977.html

kafka集成：
https://blog.csdn.net/m0_38003171/article/details/79760487（弃用）
https://blog.csdn.net/liuxiao723846/article/details/78738451
