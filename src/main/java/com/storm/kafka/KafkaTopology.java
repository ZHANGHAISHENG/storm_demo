package com.storm.kafka;

import org.apache.storm.kafka.*;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2018-06-01 23:27
 * bin/storm jar  /usr/local/app/storm_demo.jar com.storm.kafka.KafkaTopology kafka_test
 **/
public class KafkaTopology {

    public static void main(String[] args) throws Exception {
        // configure & build topology
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout-producer", buildKafkaSpout(), 1)
                .setNumTasks(3);
        builder.setBolt("bolt-log", new LogBolt(), 2)
                .shuffleGrouping("spout-producer")
                .setNumTasks(2);
        // submit topology
        Config conf = new Config();
       // conf.setDebug(true);
        String name = KafkaTopology.class.getSimpleName();
        if (args != null && args.length > 0) {
            conf.setNumWorkers(2);
            name = args[0];
            StormSubmitter.submitTopologyWithProgressBar(name, conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, conf, builder.createTopology());
            Thread.sleep(60000);
            cluster.shutdown();
        }
    }

    private static KafkaSpout buildKafkaSpout() {
        /*Broker brokerForPartition1 = new Broker("192.168.0.104", 9092);//localhost:9092 but we specified the port explicitly
        GlobalPartitionInformation partitionInfo = new GlobalPartitionInformation("topic1");
        partitionInfo.addPartition(1, brokerForPartition1);//mapping from partition 1 to brokerForPartition1
        BrokerHosts brokerHosts = new StaticHosts(partitionInfo);*/

        String brokerZkStr = "192.168.0.104:2181";
        //String brokerZkPath = "/kafka/brokers";//topic在zookeeper上根目录
        //String zkRoot = "/kafka";//用来保存消费者的偏离值
        BrokerHosts brokerHosts = new ZkHosts(brokerZkStr);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts,"topic1", "/tmp/zookeeper", "kafkaspout");
        List<String> zkServers = new ArrayList<String>() ;
        zkServers.add("192.168.0.104");
        spoutConfig.zkServers = zkServers;
        spoutConfig.zkPort = 2181;
        spoutConfig.socketTimeoutMs = 60 * 1000 ;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme()) ;
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        return kafkaSpout;
    }
}
