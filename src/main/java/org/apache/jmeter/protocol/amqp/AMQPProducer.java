/**
 * @(#)AMQPProducer.java, 2020/4/26.
 * <p/>
 * Copyright 2020 Woof, Inc. All rights reserved.
 * WOOF PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.apache.jmeter.protocol.amqp;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.log4j.Logger;


import com.rabbitmq.client.Channel;

/**
 * @Title: AMQPProducer
 * @Package org.apache.jmeter.protocol.amqp
 * @Description:
 * @Author pengy
 * @Date 2020/4/26 10:15
 * @Version v1.0
 */
public class AMQPProducer extends AMQPSampler {

    private static final Logger log = org.apache.log4j.Logger.getLogger(AMQPProducer.class);

    private final static String MESSAGE = "AMQPPublisher.Message";
    private final static String MESSAGE_ROUTING_KEY = "AMQPPublisher.MessageRoutingKey";
    private final static String MESSAGE_TYPE = "AMQPPublisher.MessageType";
    private final static String REPLY_TO_QUEUE = "AMQPPublisher.ReplyToQueue";
    private final static String CONTENT_TYPE = "AMQPPublisher.ContentType";
    private final static String CORRELATION_ID = "AMQPPublisher.CorrelationId";
    private final static String MESSAGE_ID = "AMQPPublisher.MessageId";
    private final static String HEADERS = "AMQPPublisher.Headers";

    public static boolean DEFAULT_PERSISTENT = false;
    private final static String PERSISTENT = "AMQPPublisher.Persistent";

    public static boolean DEFAULT_USE_TX = false;
    private final static String USE_TX = "AMQPPublisher.UseTx";

    private transient Channel channel;

    public AMQPProducer() {
        super();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getMessageRoutingKey() {
        return getPropertyAsString(MESSAGE_ROUTING_KEY);
    }

    public void setMessageRoutingKey(String content) {
        setProperty(MESSAGE_ROUTING_KEY, content);
    }

    /**
     * @return the message for the sample
     */
    public String getMessage() {
        return getPropertyAsString(MESSAGE);
    }

    public void setMessage(String content) {
        setProperty(MESSAGE, content);
    }

    /**
     * @return the message type for the sample
     */
    public String getMessageType() {
        return getPropertyAsString(MESSAGE_TYPE);
    }

    public void setMessageType(String content) {
        setProperty(MESSAGE_TYPE, content);
    }

    /**
     * @return the reply-to queue for the sample
     */
    public String getReplyToQueue() {
        return getPropertyAsString(REPLY_TO_QUEUE);
    }

    public void setReplyToQueue(String content) {
        setProperty(REPLY_TO_QUEUE, content);
    }

    public String getContentType() {
        return getPropertyAsString(CONTENT_TYPE);
    }

    public void setContentType(String contentType) {
        setProperty(CONTENT_TYPE, contentType);
    }

    /**
     * @return the correlation identifier for the sample
     */
    public String getCorrelationId() {
        return getPropertyAsString(CORRELATION_ID);
    }

    public void setCorrelationId(String content) {
        setProperty(CORRELATION_ID, content);
    }

    /**
     * @return the message id for the sample
     */
    public String getMessageId() {
        return getPropertyAsString(MESSAGE_ID);
    }

    public void setMessageId(String content) {
        setProperty(MESSAGE_ID, content);
    }

    public Arguments getHeaders() {
        return (Arguments) getProperty(HEADERS).getObjectValue();
    }

    public void setHeaders(Arguments headers) {
        setProperty(new TestElementProperty(HEADERS, headers));
    }

    public Boolean getPersistent() {
        return getPropertyAsBoolean(PERSISTENT, DEFAULT_PERSISTENT);
    }

    public void setPersistent(Boolean persistent) {
        setProperty(PERSISTENT, persistent);
    }

    public Boolean getUseTx() {
        return getPropertyAsBoolean(USE_TX, DEFAULT_USE_TX);
    }

    public void setUseTx(Boolean tx) {
        setProperty(USE_TX, tx);
    }

    @Override
    public SampleResult sample(Entry entry) {
        return null;
    }

    protected Channel getChannel() {
        return channel;
    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String s) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String s) {

    }
}