package de.unibox.client.test;

import org.junit.Assert;
import org.junit.Test;

import de.unibox.client.api.ClientProvider;

/**
 * The Class ClientLayerTest.
 */
public class ClientLayerTest {

    /**
     * Test login request.
     */
    @Test
    public void testLoginRequest() {
        ClientProvider.setUsername("user");
        ClientProvider.setPassword("user");
        ClientProvider
                .setFullUrl("http://10.10.10.4:7637/UniBox/Communicator/Serial");
        ClientProvider.login();
        Assert.assertNotNull(ClientProvider.getCookie());
    }

}
