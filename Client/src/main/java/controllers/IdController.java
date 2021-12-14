package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Id;

public class IdController {

    private String idUrl = "http://zipcode.rocks:8085/ids";

    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpClient client = HttpClient.newHttpClient();

    private HashMap<String, Id> allIds;

    private Id myId;

    public ArrayList<Id> getIds() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Id>>(){});
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Id postId(Id id) {

        Id createdUser = null;
        // create json from id
        // call server, get json result Or error
        // result json to Id obj
        try {
            String jsonContent = objectMapper.writeValueAsString(id);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonContent))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                createdUser = objectMapper.readValue(response.body(), Id.class);
            }
            else {
                System.out.println(response.statusCode() + " --> " + response.body());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myId = createdUser;
        return createdUser;
    }

    public Id putId(Id id) {
        Id updatedUser = null;
        try {
            String jsonContent = objectMapper.writeValueAsString(id);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonContent))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                updatedUser = objectMapper.readValue(response.body(), Id.class);
            }
            else {
                System.out.println(response.statusCode() + " --> " + response.body());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myId = updatedUser;
        return updatedUser;
    }
 
}