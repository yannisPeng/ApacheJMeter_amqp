package org.apache.jmeter.protocol.amqp.gui;

import org.apache.jmeter.testelement.TestElement;

public class RabbitMqProducer extends AMQPSamplerGui {

    @Override
    public String getStaticLabel() {
        return "AMQP Producer";
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
