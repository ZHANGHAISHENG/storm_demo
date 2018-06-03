package com.storm.local;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * @author Administrator
 * @date 2018-06-01 23:27
 **/
public class WordCountTopology {
    public static void main(String[] args) throws Exception {
        // configure & build topology
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout-producer", new ProduceRecordSpout(), 1)
                .setNumTasks(3);
        builder.setBolt("bolt-splitter", new WordSplitterBolt(), 2)
                .shuffleGrouping("spout-producer")
                .setNumTasks(2);
        builder.setBolt("bolt-counter", new WordCounterBolt(), 1)
                .fieldsGrouping("bolt-splitter", new Fields("word"))
                .setNumTasks(2);
        // submit topology
        Config conf = new Config();
       // conf.setDebug(true);
        String name = WordCountTopology.class.getSimpleName();
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
}
