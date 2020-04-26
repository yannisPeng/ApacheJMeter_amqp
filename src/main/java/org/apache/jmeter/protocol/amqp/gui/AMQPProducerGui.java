package org.apache.jmeter.protocol.amqp.gui;

import java.awt.*;

import javax.swing.*;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.protocol.amqp.AMQPProducer;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.log4j.Logger;

public class AMQPProducerGui extends AMQPSamplerGui {

    private static final Logger log = org.apache.log4j.Logger.getLogger(AMQPProducerGui.class);

    private JPanel mainPanel;

    private JLabeledTextArea message = new JLabeledTextArea("Message Content");
    private JLabeledTextField messageRoutingKey = new JLabeledTextField("Routing Key");
    private JLabeledTextField messageType = new JLabeledTextField("Message Type");
    private JLabeledTextField replyToQueue = new JLabeledTextField("Reply-To Queue");
    private JLabeledTextField correlationId = new JLabeledTextField("Correlation Id");
    private JLabeledTextField contentType = new JLabeledTextField("ContentType");
    private JLabeledTextField messageId = new JLabeledTextField("Message Id");

    private JCheckBox persistent = new JCheckBox("Persistent?", AMQPProducer.DEFAULT_PERSISTENT);
    private JCheckBox useTx = new JCheckBox("Use Transactions?", AMQPProducer.DEFAULT_USE_TX);

    private ArgumentsPanel headers = new ArgumentsPanel("Headers");

    public AMQPProducerGui() {
        init();
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return "AMQP Producer";
    }


    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (!(element instanceof AMQPProducer)) return;
        AMQPProducer sampler = (AMQPProducer) element;

        persistent.setSelected(sampler.getPersistent());
        useTx.setSelected(sampler.getUseTx());

        messageRoutingKey.setText(sampler.getMessageRoutingKey());
        messageType.setText(sampler.getMessageType());
        replyToQueue.setText(sampler.getReplyToQueue());
        contentType.setText(sampler.getContentType());
        correlationId.setText(sampler.getCorrelationId());
        messageId.setText(sampler.getMessageId());
        message.setText(sampler.getMessage());
        configureHeaders(sampler);
    }

    @Override
    public TestElement createTestElement() {
        AMQPProducer sampler = new AMQPProducer();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        AMQPProducer sampler = (AMQPProducer) te;
        sampler.clear();
        configureTestElement(sampler);

        super.modifyTestElement(sampler);

        sampler.setPersistent(persistent.isSelected());
        sampler.setUseTx(useTx.isSelected());

        sampler.setMessageRoutingKey(messageRoutingKey.getText());
        sampler.setMessage(message.getText());
        sampler.setMessageType(messageType.getText());
        sampler.setReplyToQueue(replyToQueue.getText());
        sampler.setCorrelationId(correlationId.getText());
        sampler.setContentType(contentType.getText());
        sampler.setMessageId(messageId.getText());
        sampler.setHeaders((Arguments) headers.createTestElement());
    }

    @Override
    protected void setMainPanel(JPanel panel) {
        mainPanel = panel;
    }

    @Override
    protected final void init() {
        super.init();
        persistent.setPreferredSize(new Dimension(100, 25));
        useTx.setPreferredSize(new Dimension(100, 25));
        messageRoutingKey.setPreferredSize(new Dimension(100, 25));
        messageType.setPreferredSize(new Dimension(100, 25));
        replyToQueue.setPreferredSize(new Dimension(100, 25));
        correlationId.setPreferredSize(new Dimension(100, 25));
        contentType.setPreferredSize(new Dimension(100, 25));
        messageId.setPreferredSize(new Dimension(100, 25));
        message.setPreferredSize(new Dimension(400, 150));

        mainPanel.add(persistent);
        mainPanel.add(useTx);
        mainPanel.add(messageRoutingKey);
        mainPanel.add(messageType);
        mainPanel.add(replyToQueue);
        mainPanel.add(correlationId);
        mainPanel.add(contentType);
        mainPanel.add(messageId);
        mainPanel.add(headers);
        mainPanel.add(message);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        persistent.setSelected(AMQPProducer.DEFAULT_PERSISTENT);
        useTx.setSelected(AMQPProducer.DEFAULT_USE_TX);
        messageRoutingKey.setText("");
        messageType.setText("");
        replyToQueue.setText("");
        correlationId.setText("");
        contentType.setText("");
        messageId.setText("");
        headers.clearGui();
        message.setText("");
    }

    private void configureHeaders(AMQPProducer sampler) {
        Arguments sampleHeaders = sampler.getHeaders();
        if (sampleHeaders != null) {
            headers.configure(sampleHeaders);
        } else {
            headers.clearGui();
        }
    }

}
