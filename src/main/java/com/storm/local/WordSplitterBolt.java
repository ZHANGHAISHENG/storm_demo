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
import java.util.Map;

public class WordSplitterBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(WordSplitterBolt.class);
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        String record = input.getString(0);
        if(record != null && !record.trim().isEmpty()) {
            for(String word : record.split("\\s+")) {
                collector.emit(input, new Values(word, 1));
                LOG.info("Emitted: word=" + word);
                collector.ack(input);
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}