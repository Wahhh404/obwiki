package com.example.obwiki.rocketmq;

import com.example.obwiki.websocket.WebSocketServer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

//@Service//默认分组 topic 监听队列名称
//@RocketMQMessageListener(consumerGroup = "default", topic = "VOTE_TOPIC")
public class VoteTopicConsumer implements RocketMQListener<MessageExt> {

    private static final Logger LOG = LoggerFactory.getLogger(VoteTopicConsumer.class);

    @Resource
    public WebSocketServer webSocketServer;
    // 消息处理
    @Override
    public void onMessage(MessageExt messageExt) {
        //获取消息
        byte[] body = messageExt.getBody();
        LOG.info("ROCKETMQ收到消息：{}", new String(body));
        //调用通知方法
        webSocketServer.sendInfo(new String(body));
    }
}
