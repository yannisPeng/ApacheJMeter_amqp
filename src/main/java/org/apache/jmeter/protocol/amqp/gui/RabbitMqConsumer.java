package org.apache.jmeter.protocol.amqp.gui;

import org.apache.jmeter.testelement.TestElement;

public class RabbitMqConsumer extends AMQPSamplerGui {

    @Override
    public String getStaticLabel() {
        return "AMQP Consumer";
    }

    public String getLabelResource() {
        return null;
    }

    public TestElement createTestElement() {
        return null;
    }

    public void modifyTestElement(TestElement testElement) {

    }

    public boolean canBeAdded() {
        return false;
    }

}
