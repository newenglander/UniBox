package de.unibox.core.test;

import org.junit.Assert;
import org.junit.Test;

import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.CommunicatorMessage.MessageType;
import de.unibox.core.provider.ObjectSerializerImpl;

/**
 * The Class ObjectSerializerImplTest.
 */
public class ObjectSerializerImplTest {

    /**
     * Object to string.
     *
     * @return the string
     */
    public String objectToString() {
        return ObjectSerializerImpl.objectToString(new CommunicatorMessage(
                MessageType.SYSTEM, "DUMMY", "joined"));
    }

    /**
     * String to object.
     *
     * @param data
     *            the data
     * @return the communicator message
     */
    public CommunicatorMessage stringToObject(final String data) {
        return ObjectSerializerImpl.stringToObject(data,
                CommunicatorMessage.class);
    }

    /**
     * Test object accessible.
     */
    @Test
    public void testObjectAccessible() {
        final CommunicatorMessage concreteObject = this.stringToObject(this
                .objectToString());
        Assert.assertNotNull(concreteObject.getMessage());
    }

    /**
     * Test object to string.
     */
    @Test
    public void testObjectToString() {
        final String objectString = this.objectToString();
        Assert.assertTrue(objectString.getClass().getSimpleName()
                .equals(String.class.getSimpleName()));
    }

    /**
     * Test string to object.
     */
    @Test
    public void testStringToObject() {
        final CommunicatorMessage concreteObject = this.stringToObject(this
                .objectToString());
        Assert.assertTrue(concreteObject.getClass().getSimpleName()
                .equals(CommunicatorMessage.class.getSimpleName()));
    }

}
