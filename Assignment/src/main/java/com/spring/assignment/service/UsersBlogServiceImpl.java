package com.spring.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.assignment.exception.UsersAndPostsNotFoundException;
import com.spring.assignment.model.UserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UsersBlogServiceImpl implements IUsersBlogService {

    @Autowired
    RestTemplate restTemplate;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String USER_NAME = "username";
    public static final String POSTS = "posts";
    public static final String USER_ID = "userId";

    @Value("${users.get.rest.url}")
    private String getUsersRestUrl;

    @Value("${users.posts.rest.url}")
    private String usersPostsUrl;

    @Override
    public List<LinkedHashMap> getUsersDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String usersJson = restTemplate.exchange(getUsersRestUrl, HttpMethod.GET, entity, String.class).getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap> userDetails = new ArrayList<LinkedHashMap>();
        try {
            userDetails = (List<LinkedHashMap>) mapper.readValue(usersJson, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userDetails;
    }

    @Override
    public List<LinkedHashMap> getUsersPosts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String postsJson = restTemplate.exchange(usersPostsUrl, HttpMethod.GET, entity, String.class).getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap> userPosts = new ArrayList<LinkedHashMap>();
        try {
            userPosts = (List<LinkedHashMap>) mapper.readValue(postsJson, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userPosts;
    }

    @Override
    public UserPost createUserPost(UserPost userPost) {
        UserPost responseBody = restTemplate.postForObject(usersPostsUrl, userPost, UserPost.class);
        return responseBody;
    }

    public String getUsersBlogs() {
        List<LinkedHashMap> usersDetails = getUsersDetails();
        List<LinkedHashMap> posts = getUsersPosts();
        String jsonResponse = "";
        if (null == usersDetails || usersDetails.size() == 0) {
            throw new UsersAndPostsNotFoundException();
        }
        Map<String, String> userPosts = new HashMap<>();
        List<LinkedHashMap> usersPostResponseList = new ArrayList<LinkedHashMap>();
        usersDetails.forEach(usersDetail -> {
            LinkedHashMap userPostMap = new LinkedHashMap();
            userPostMap.put(ID, usersDetail.get(ID));
            userPostMap.put(NAME, usersDetail.get(NAME));
            userPostMap.put(USER_NAME, usersDetail.get(USER_NAME));
            List<LinkedHashMap> userPostsList = posts.stream().filter(e -> e.get(USER_ID).toString().equalsIgnoreCase(
                    usersDetail.get(ID).toString())).collect(Collectors.toList());
            userPostMap.put(POSTS, userPostsList);
            usersPostResponseList.add(userPostMap);
        });
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersPostResponseList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }


}
