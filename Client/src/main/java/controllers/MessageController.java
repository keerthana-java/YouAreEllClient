package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Id;
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

public class MessageController {

    ObjectMapper objectMapper = new ObjectMapper();

    private HashSet<Message> messagesSeen;
    // why a HashSet??

    public ArrayList<Message> getMessages() {

        HttpGet request = new HttpGet("http://zipcode.rocks:8085/messages");

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<ArrayList<Message>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public ArrayList<Message> getMessagesForId(Id Id) {
        HttpGet request = new HttpGet("http://zipcode.rocks:8085/ids/"+ Id.getGithub() +"/messages");

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<ArrayList<Message>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Message getMessageForSequence(Id id, String seq) {
        HttpGet request = new HttpGet("http://zipcode.rocks:8085/ids/"+ id.getGithub() +"/messages/"+seq);

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
    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {

        HttpGet request = new HttpGet("http://zipcode.rocks:8085/ids/" + myId.getGithub() + "/from/" + friendId.getGithub());

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<ArrayList<Message>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message postMessage(Id myId, Id toId, Message msg) throws JsonProcessingException {

        HttpPost request = new HttpPost("http://zipcode.rocks:8085/ids/"+ myId.getGithub() +"/messages");

        String content = objectMapper.writeValueAsString(msg);
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
 
}