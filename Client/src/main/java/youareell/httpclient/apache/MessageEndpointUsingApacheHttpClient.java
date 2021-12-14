package youareell.httpclient.apache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public class MessageEndpointUsingApacheHttpClient {

    ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {

        String currentGithubId = "keerthana-java";
        String friendGithubId = "lab-admin";

        MessageEndpointUsingApacheHttpClient apacheHttpClient = new MessageEndpointUsingApacheHttpClient();

        // Get all user messages
        List<Message> allMessages = apacheHttpClient.getAllMessages();
        System.out.println(allMessages);

        // Get all current user's Messages
        List<Message> currentUserAccountMessages = apacheHttpClient.getUserMessage(currentGithubId);
        System.out.println(currentUserAccountMessages);

        // Create a message from current user account
        Message newMessage = new Message("Message via HttpClient", currentGithubId, friendGithubId);
        Message createdMessage = apacheHttpClient.createUserMessage(currentGithubId, newMessage);
        System.out.println(createdMessage);

        // Create a message from current user account to all
        Message announcement = new Message("Message via HttpClient", currentGithubId);
        Message createdAnnouncement = apacheHttpClient.createUserMessage(currentGithubId, announcement);
        System.out.println(createdAnnouncement);

        // Get message by created message sequnce
        Message foundMessage = apacheHttpClient.getMessageBySequence(currentGithubId, createdMessage.getSequence());
        System.out.println(foundMessage);

        // Get all current user account Messages from Friend
        List<Message> currentUserAccountMessagesFromFriend = apacheHttpClient.getUserMessageFromFriend(currentGithubId, friendGithubId);
        System.out.println(currentUserAccountMessagesFromFriend);

    }

    public List<Message> getAllMessages() {

        HttpGet request = new HttpGet("http://zipcode.rocks:8085/messages");

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<List<Message>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Message> getUserMessage(String userGitHubId) {

        HttpGet request = new HttpGet("http://zipcode.rocks:8085/ids/"+ userGitHubId +"/messages");

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<List<Message>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message createUserMessage(String userGitHubId, Message message) throws JsonProcessingException {

        HttpPost request = new HttpPost("http://zipcode.rocks:8085/ids/"+ userGitHubId +"/messages");

        String content = objectMapper.writeValueAsString(message);
        HttpEntity messageHttpEntity = new StringEntity(content, ContentType.APPLICATION_JSON);
        request.setEntity(messageHttpEntity);

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                Message createdMessage = objectMapper.readValue(response.getEntity().getContent(), Message.class);
                return createdMessage;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message getMessageBySequence(String userGitHubId, String sequence) throws JsonProcessingException {

        HttpGet request = new HttpGet("http://zipcode.rocks:8085/ids/"+ userGitHubId +"/messages/"+sequence);

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                Message receivedMessage = objectMapper.readValue(response.getEntity().getContent(), Message.class);
                return receivedMessage;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getUserMessageFromFriend(String userGitHubId, String friendGithubId) {

        HttpGet request = new HttpGet("http://zipcode.rocks:8085/ids/"+ userGitHubId +"/from/"+friendGithubId);

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<List<Message>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
