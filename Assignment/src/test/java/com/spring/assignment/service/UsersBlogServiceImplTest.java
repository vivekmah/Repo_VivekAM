package com.spring.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.assignment.exception.UsersAndPostsNotFoundException;
import com.spring.assignment.model.UserPost;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UsersBlogServiceImplTest {

    @InjectMocks
    IUsersBlogService usersBlogService = new UsersBlogServiceImpl();

    @Mock
    RestTemplate restTemplate;

    @Mock
    ObjectMapper mapper;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(usersBlogService, "getUsersRestUrl", "https://jsonplaceholder.typicode.com/users");
        ReflectionTestUtils.setField(usersBlogService, "usersPostsUrl", "https://jsonplaceholder.typicode.com/posts");
    }

    @Test
    public void getUserDetailsTest() {
        HttpHeaders headers = new HttpHeaders();
        List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
        LinkedHashMap user1 = new LinkedHashMap();
        linkedHashMapList.add(user1);
        String jsondata = getJson(linkedHashMapList);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(jsondata, HttpStatus.ACCEPTED);
        Mockito.when(restTemplate.exchange("https://jsonplaceholder.typicode.com/users", HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);
        List<LinkedHashMap> usersDataLst = usersBlogService.getUsersDetails();
        Assert.assertEquals(linkedHashMapList, usersDataLst);

    }

    @Test
    public void getUsersPosts() {
        HttpHeaders headers = new HttpHeaders();
        List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
        LinkedHashMap user1 = new LinkedHashMap();
        user1.put("userId", "1");
        user1.put("title", "Test blog");
        linkedHashMapList.add(user1);
        String jsondata = getJson(linkedHashMapList);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(jsondata, HttpStatus.ACCEPTED);
        Mockito.when(restTemplate.exchange("https://jsonplaceholder.typicode.com/posts", HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);
        List<LinkedHashMap> usersPostLst = usersBlogService.getUsersPosts();
        Assert.assertEquals(linkedHashMapList, usersPostLst);
    }

    @Test
    public void getUserBlogs() {

        List<LinkedHashMap> postsList = createUserPostData();
        List<LinkedHashMap> userDataList = createUserData();
        UsersBlogServiceImpl spyClass = Mockito.spy(UsersBlogServiceImpl.class);
        Mockito.doReturn(userDataList).when(spyClass).getUsersDetails();
        Mockito.doReturn(postsList).when(spyClass).getUsersPosts();
        String usersPostsDetailsJson = spyClass.getUsersBlogs();
        List usersPosts = getObject(usersPostsDetailsJson);

        Assert.assertEquals(1, usersPosts.size());
    }

    @Test(expected = UsersAndPostsNotFoundException.class)
    public void getUserBlogs_withException() {

        List<LinkedHashMap> postsList = createUserPostData();
        UsersBlogServiceImpl spyClass = Mockito.spy(UsersBlogServiceImpl.class);
        Mockito.doReturn(null).when(spyClass).getUsersDetails();
        Mockito.doReturn(postsList).when(spyClass).getUsersPosts();
        String usersPostsDetailsJson = spyClass.getUsersBlogs();
    }

    @Test
    public void createUserPost() {
        UserPost post = new UserPost();
        post.setBody("Empty");
        post.setTitle("Test Title");
        post.setUserId("1");
        Mockito.when(restTemplate.postForObject("https://jsonplaceholder.typicode.com/posts", post, UserPost.class)).thenReturn(post);
        UserPost postMessage = usersBlogService.createUserPost(post);
        Assert.assertEquals(post.getUserId(), postMessage.getUserId());

    }

    private List<LinkedHashMap> createUserData() {
        List<LinkedHashMap> userDataList = new ArrayList<>();
        LinkedHashMap user = new LinkedHashMap();
        user.put("userId", "1");
        user.put("id", "1");
        user.put("name", "Vivek");
        userDataList.add(user);
        return userDataList;
    }

    private List<LinkedHashMap> createUserPostData() {
        List<LinkedHashMap> postsList = new ArrayList<>();
        LinkedHashMap post = new LinkedHashMap();
        post.put("userId", "1");
        post.put("title", "Test blog");
        postsList.add(post);
        return postsList;
    }

    private String getJson(List<LinkedHashMap> linkedHashMapList) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(linkedHashMapList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private List getObject(String json) {
        ObjectMapper mapper = new ObjectMapper();
        List dataLst = null;
        try {
            dataLst = mapper.readValue(json, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataLst;
    }
}
