package youareell.httpclient.java;

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
import youareell.httpclient.apache.IdEndpointUsingApacheHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class IdEndpointUsingJavaHttpClient {

    HttpClient client = HttpClient.newHttpClient();

    ObjectMapper objectMapper = new ObjectMapper();

    String idUrl = "http://zipcode.rocks:8085/ids";

    public static void main(String[] args) throws IOException, InterruptedException {

        IdEndpointUsingJavaHttpClient javaHttpClient = new IdEndpointUsingJavaHttpClient();

        // Get All Users via GET call
        List<Id> allUsers = javaHttpClient.getAllUsers();
        System.out.println(allUsers);

        // Create a user via POST call
        Id newUser = new Id("keerthis", "keerthana-java");
        Id createdUser = javaHttpClient.createUser(newUser);
        System.out.println(createdUser);

        // Update user-name and update user via PUT call
        Id userTobeUpdated = createdUser;
        userTobeUpdated.setName("keerthi");
        Id updatedUser = javaHttpClient.updateUser(userTobeUpdated);
        System.out.println(updatedUser);
    }

    public List<Id> getAllUsers() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl))
                                    .GET()
                                    .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {

            return objectMapper.readValue(response.body(), new TypeReference<List<Id>>(){});
        }

        return null;
    }

    public Id createUser(Id userAccount) throws IOException, InterruptedException {

        String content = objectMapper.writeValueAsString(userAccount);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl))
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {

            Id createdUser = objectMapper.readValue(response.body(), Id.class);
            return createdUser;
        }

        return null;
    }

    public Id updateUser(Id userAccount) throws IOException, InterruptedException {

        String content = objectMapper.writeValueAsString(userAccount);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl))
                .PUT(HttpRequest.BodyPublishers.ofString(content))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {

            Id createdUser = objectMapper.readValue(response.body(), Id.class);
            return createdUser;
        }

        return null;
    }


}
