package com.storm.kafka;

import com.storm.local.WordSplitterBolt;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public  class LogBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(WordSplitterBolt.class);
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        String str = tuple.getString(0);
        //String str = new String(tuple.getBinaryByField("bytes"));
        Fields fields = tuple.getFields();
        Iterator<String> iterator = fields.iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String field = iterator.next();
           // String value = tuple.(field);
            sb.append("field="+field+"|");//.append(",value="+value).append("|");
        }
        sb.append("\n");
        LOG.info("DEBUG: message=" + sb.toString()+"--"+str);
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void cleanup() {
    }

}