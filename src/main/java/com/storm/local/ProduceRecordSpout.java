package com.storm.local;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Random;

public  class ProduceRecordSpout extends BaseRichSpout {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ProduceRecordSpout.class);
    private SpoutOutputCollector collector;
    private Random random;
    private String[] records;

    public ProduceRecordSpout() {
        this.records = new String[] {
                "A Storm cluster is superficially similar to a Hadoop cluster",
                "All coordination between Nimbus and the Supervisors is done through a Zookeeper cluster",
                "The core abstraction in Storm is the stream"
        };
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        random = new Random();
    }

    public void nextTuple() {
        Utils.sleep(500);
        String record = records[random.nextInt(records.length)];
        List<Object> values = new Values(record);
        collector.emit(values, values);
        LOG.info("Record emitted: record=" + record);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("record"));
    }
}