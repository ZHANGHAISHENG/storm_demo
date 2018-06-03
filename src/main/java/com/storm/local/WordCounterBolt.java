package com.storm.local;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public  class WordCounterBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(WordSplitterBolt.class);
    private OutputCollector collector;
    private final Map<String, AtomicInteger> counterMap = new HashMap<String, AtomicInteger>();

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String word = input.getString(0);
        int count = input.getIntegerByField("count"); // 通过Field名称取出对应字段的数据
        AtomicInteger ai = counterMap.get(word);
        if(ai == null) {
            ai = new AtomicInteger(0);
            counterMap.put(word, ai);
        }
        ai.addAndGet(count);
        LOG.info("DEBUG: word=" + word + ", count=" + ai.get());
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void cleanup() {
        // print count results
        LOG.info("Word count results:");
        for(Map.Entry<String, AtomicInteger> entry : counterMap.entrySet()) {
            LOG.info("\tword=" + entry.getKey() + ", count=" + entry.getValue().get());
        }
    }

}