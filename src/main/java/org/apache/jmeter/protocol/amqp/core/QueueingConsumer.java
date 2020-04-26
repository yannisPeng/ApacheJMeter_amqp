/**
 * @(#)QueueingConsumer.java, 2020/4/26.
 * <p/>
 * Copyright 2020 Woof, Inc. All rights reserved.
 * WOOF PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.apache.jmeter.protocol.amqp.core;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @Title: QueueingConsumer
 * @Package org.apache.jmeter.protocol.amqp.override
 * @Description:
 * @Author pengy
 * @Date 2020/4/26 14:38
 * @Version v1.0
 */
public class QueueingConsumer extends DefaultConsumer {

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
    }

    public QueueingConsumer(Channel channel) {
        super(channel);
    }

}