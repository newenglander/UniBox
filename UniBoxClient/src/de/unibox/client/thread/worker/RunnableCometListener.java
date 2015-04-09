package de.unibox.client.thread.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import de.unibox.client.api.ClientProvider;
import de.unibox.client.thread.implementation.ThreadTaskImpl;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.provider.ObjectSerializerImpl;

/**
 * The listener interface for receiving runnableComet events. The class that is
 * interested in processing a runnableComet event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addRunnableCometListener<code> method. When
 * the runnableComet event occurs, that object's appropriate
 * method is invoked.
 *
 * @see RunnableCometEvent
 */
public class RunnableCometListener extends ThreadTaskImpl {

    /*
     * (non-Javadoc)
     * 
     * @see de.unibox.client.thread.implementation.ThreadTaskImpl#process()
     */
    @Override
    protected void process() {

        URL url;
        HttpURLConnection connection;

        try {

            final String urlString = ClientProvider.getUrl()
                    + ClientProvider.getCometURL() + "?0";

            ThreadTaskImpl.log.debug("RunnableCometListener is connecting to: "
                    + urlString);

            url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setUseCaches(false);
            connection.addRequestProperty("Cookie", ClientProvider.getCookie());

            for (final Entry<String, List<String>> header : connection
                    .getHeaderFields().entrySet()) {
                ThreadTaskImpl.log.debug("ResponseHeader: " + header.getKey()
                        + "=" + header.getValue());
            }

            final BufferedReader areader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            for (String line; (line = areader.readLine()) != null;) {
                final CommunicatorMessage message = ObjectSerializerImpl
                        .stringToObject(line, CommunicatorMessage.class);
                ThreadTaskImpl.log.debug("RunnableCometListener recieving: "
                        + message);
                ClientProvider.recieveMessage(message);
            }

        } catch (final IOException e) {
            ThreadTaskImpl.log.warn(RunnableMessageSender.class.getSimpleName()
                    + ": Fatal error due HTTP transaction.");
            if (e instanceof ConnectException) {
                ThreadTaskImpl.log.warn(RunnableMessageSender.class
                        .getSimpleName()
                        + ": maybe server offline? "
                        + this.url);
            }
            e.printStackTrace();
        }
    }

}
