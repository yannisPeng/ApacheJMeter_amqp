/**
 * @(#)AMQPConsumer.java, 2020/4/26.
 * <p/>
 * Copyright 2020 Woof, Inc. All rights reserved.
 * WOOF PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.apache.jmeter.protocol.amqp;

import java.io.IOException;

import org.apache.jmeter.protocol.amqp.core.QueueingConsumer;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @Title: AMQPConsumer
 * @Package org.apache.jmeter.protocol.amqp
 * @Description:
 * @Author pengy
 * @Date 2020/4/26 10:08
 * @Version v1.0
 */
public class AMQPConsumer extends AMQPSampler {

    private static final Logger log = org.apache.log4j.Logger.getLogger(AMQPConsumer.class);

    private static final int DEFAULT_PREFETCH_COUNT = 0; // unlimited

    public static final boolean DEFAULT_READ_RESPONSE = true;
    public static final String DEFAULT_PREFETCH_COUNT_STRING = Integer.toString(DEFAULT_PREFETCH_COUNT);

    //++ These are JMX names, and must not be changed
    private static final String PREFETCH_COUNT = "AMQPConsumer.PrefetchCount";
    private static final String READ_RESPONSE = "AMQPConsumer.ReadResponse";
    private static final String PURGE_QUEUE = "AMQPConsumer.PurgeQueue";
    private static final String AUTO_ACK = "AMQPConsumer.AutoAck";
    private static final String RECEIVE_TIMEOUT = "AMQPConsumer.ReceiveTimeout";

    public static boolean DEFAULT_USE_TX = false;
    private final static String USE_TX = "AMQPConsumer.UseTx";

    private transient Channel channel;
    private transient String consumerTag;
    private transient QueueingConsumer consumer;

    public AMQPConsumer(){
        super();
    }

    /**
     * ∑¢ÀÕ«Î«Û
     */
    @Override
    public SampleResult sample(Entry entry) {

        String resultMsg = "success";

        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.setSuccessful(false);
        result.setResponseCode("500");

        trace("AMQPConsumer.sample()");

        try {
            initChannel();

            // only do this once per thread. Otherwise it slows down the consumption by appx 50%
            if (consumer == null) {
                log.info("Creating consumer");
                consumer = new QueueingConsumer(channel);
            }
            if (consumerTag == null) {
                log.info("Starting basic consumer");
                consumerTag = channel.basicConsume(getQueue(), autoAck(), consumer);
            }
        } catch (Exception ex) {
            log.error("Failed to initialize channel", ex);
            result.setResponseMessage(ex.toString());
            return result;
        }

        result.setSampleLabel(getTitle());

        result.sampleStart();
        try {
            if (!autoAck())
                channel.basicConsume(getQueue(), consumer);

            // commit the sample.
            if (getUseTx()) {
                channel.txCommit();
            }

            result.setResponseData(resultMsg, null);
            result.setDataType(SampleResult.TEXT);
            result.setResponseCodeOK();
            result.setSuccessful(true);

        } catch (ShutdownSignalException e) {
            consumer = null;
            consumerTag = null;
            log.warn("AMQP consumer failed to consume", e);
            result.setResponseCode("400");
            result.setResponseMessage(e.getMessage());
            interrupt();
        } catch (ConsumerCancelledException e) {
            consumer = null;
            consumerTag = null;
            log.warn("AMQP consumer failed to consume", e);
            result.setResponseCode("300");
            result.setResponseMessage(e.getMessage());
            interrupt();
        } catch (IOException e) {
            consumer = null;
            consumerTag = null;
            log.warn("AMQP consumer failed to consume", e);
            result.setResponseCode("100");
            result.setResponseMessage(e.getMessage());
        } finally {
            result.sampleEnd(); // End timimg
        }

        trace("AMQPConsumer.sample ended");

        return result;
    }

    public boolean interrupt() {
        testEnded();
        return true;
    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String s) {

    }

    @Override
    public void testEnded() {

        if (purgeQueue()) {
            log.info("Purging queue " + getQueue());
            try {
                channel.queuePurge(getQueue());
            } catch (IOException e) {
                log.error("Failed to purge queue " + getQueue(), e);
            }
        }
    }

    @Override
    public void testEnded(String s) {

    }

    /*
     * Helper method
     */
    private void trace(String s) {
        String tl = getTitle();
        String tn = Thread.currentThread().getName();
        String th = this.toString();
        log.debug(tn + " " + tl + " " + s + " " + th);
    }

    public String getPurgeQueue() {
        return getPropertyAsString(PURGE_QUEUE);
    }

    public void setPurgeQueue(String content) {
        setProperty(PURGE_QUEUE, content);
    }

    public void setPurgeQueue(Boolean purgeQueue) {
        setProperty(PURGE_QUEUE, purgeQueue.toString());
    }

    public boolean purgeQueue() {
        return Boolean.parseBoolean(getPurgeQueue());
    }

    /**
     * @return the whether or not to auto ack
     */
    public String getAutoAck() {
        return getPropertyAsString(AUTO_ACK);
    }

    public void setAutoAck(String content) {
        setProperty(AUTO_ACK, content);
    }

    public void setAutoAck(Boolean autoAck) {
        setProperty(AUTO_ACK, autoAck.toString());
    }

    public boolean autoAck() {
        return getPropertyAsBoolean(AUTO_ACK);
    }

    protected int getReceiveTimeoutAsInt() {
        if (getPropertyAsInt(RECEIVE_TIMEOUT) < 1) {
            return DEFAULT_TIMEOUT;
        }
        return getPropertyAsInt(RECEIVE_TIMEOUT);
    }

    public String getReceiveTimeout() {
        return getPropertyAsString(RECEIVE_TIMEOUT, DEFAULT_TIMEOUT_STRING);
    }


    public void setReceiveTimeout(String s) {
        setProperty(RECEIVE_TIMEOUT, s);
    }

    public String getPrefetchCount() {
        return getPropertyAsString(PREFETCH_COUNT, DEFAULT_PREFETCH_COUNT_STRING);
    }

    public void setPrefetchCount(String prefetchCount) {
        setProperty(PREFETCH_COUNT, prefetchCount);
    }

    public int getPrefetchCountAsInt() {
        return getPropertyAsInt(PREFETCH_COUNT);
    }

    public Boolean getUseTx() {
        return getPropertyAsBoolean(USE_TX, DEFAULT_USE_TX);
    }

    public void setUseTx(Boolean tx) {
        setProperty(USE_TX, tx);
    }

    /**
     * set whether the sampler should read the response or not
     *
     * @param read whether the sampler should read the response or not
     */
    public void setReadResponse(Boolean read) {
        setProperty(READ_RESPONSE, read);
    }

    /**
     * return whether the sampler should read the response
     *
     * @return whether the sampler should read the response
     */
    public String getReadResponse() {
        return getPropertyAsString(READ_RESPONSE);
    }

    /**
     * return whether the sampler should read the response as a boolean value
     *
     * @return whether the sampler should read the response as a boolean value
     */
    public boolean getReadResponseAsBoolean() {
        return getPropertyAsBoolean(READ_RESPONSE);
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    protected Channel getChannel() {
        return channel;
    }

}