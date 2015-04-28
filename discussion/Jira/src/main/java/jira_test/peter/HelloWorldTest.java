package jira_test.peter;

/**
 * Hello world!
 *
 */

import java.io.IOException;
import java.net.URI;
import java.util.ResourceBundle;

import org.junit.Test;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import static org.junit.Assert.assertEquals;

/**
 * Simple test that does nothing but connect to a JIRA server and verifies the
 * connection.
 * 
 * @author Peter Peng
 */
public class HelloWorldTest {
    private static final ResourceBundle bundle = ResourceBundle
            .getBundle(HelloWorldTest.class.getName());
    private static final URI serverUri = URI
            .create(bundle.getString("jiraUrl"));
    private static final String username = bundle.getString("username");
    private static final String password = bundle.getString("password");

    @Test
    public void connectToServer() throws IOException {
        final JiraRestClient restClient = new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(serverUri, username, password);

        try {
            final ServerInfo info = restClient.getMetadataClient().getServerInfo().claim();
            assertEquals(URI.create(bundle.getString("jiraUrl")), info.getBaseUri());
        } finally {
            if (restClient != null) {
                restClient.close();
            }
        }
    }
}
