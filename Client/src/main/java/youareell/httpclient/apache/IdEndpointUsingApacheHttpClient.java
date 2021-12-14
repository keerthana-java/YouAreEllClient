package youareell.httpclient.apache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Id;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public class IdEndpointUsingApacheHttpClient {

    ObjectMapper objectMapper = new ObjectMapper();

    String idUrl = "http://zipcode.rocks:8085/ids";

    public static void main(String[] args) throws JsonProcessingException {

        IdEndpointUsingApacheHttpClient apacheHttpClient = new IdEndpointUsingApacheHttpClient();

        // Get All Users via GET call
        List<Id> allUsers = apacheHttpClient.getAllUsers();

        // Create a user via POST call
        Id newUser = new Id("keerthis", "keerthana-java");
        Id createdUser = apacheHttpClient.createUser(newUser);

        // Update user-name and update user via PUT call
        Id userTobeUpdated = createdUser;
        userTobeUpdated.setName("keerthi");
        apacheHttpClient.updateUser(userTobeUpdated);
    }

    public List<Id> getAllUsers() {

        HttpGet request = new HttpGet(idUrl);

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                return objectMapper.readValue(entity.getContent(), new TypeReference<List<Id>>(){});
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Id createUser(Id userAccount) throws JsonProcessingException {

        HttpPost request = new HttpPost(idUrl);

        String content = objectMapper.writeValueAsString(userAccount);
        HttpEntity userAccountHttpEntity = new StringEntity(content, ContentType.APPLICATION_JSON);
        request.setEntity(userAccountHttpEntity);

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                Id createdUser = objectMapper.readValue(response.getEntity().getContent(), Id.class);
                return createdUser;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Id updateUser(Id userAccount) throws JsonProcessingException {

        HttpPut request = new HttpPut(idUrl);

        String content = objectMapper.writeValueAsString(userAccount);
        HttpEntity userAccountHttpEntity = new StringEntity(content, ContentType.APPLICATION_JSON);
        request.setEntity(userAccountHttpEntity);

        try  (CloseableHttpClient httpClient = HttpClients.createDefault();
              CloseableHttpResponse response = httpClient.execute(request)) {

            if (response.getStatusLine().getStatusCode() == 200) {

                Id updatedUser = objectMapper.readValue(response.getEntity().getContent(), Id.class);
                return updatedUser;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
