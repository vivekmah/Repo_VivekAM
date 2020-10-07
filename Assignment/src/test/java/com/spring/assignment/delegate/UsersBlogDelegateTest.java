package com.spring.assignment.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.assignment.model.UserPost;
import com.spring.assignment.service.UsersBlogServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RunWith(SpringRunner.class)
public class UsersBlogDelegateTest {

    @Mock
    UsersBlogServiceImpl usersBlogService;

    @InjectMocks
    UsersBlogDelegate usersBlogDelegate;

    @Test
    public void getUsersBlogsTest() {
        String sampleJson = getUsersPostResponses();
        Mockito.when(usersBlogService.getUsersBlogs()).thenReturn(sampleJson);
        String result = usersBlogDelegate.getUsersBlogs();
        Assert.assertEquals(sampleJson, result);

    }

    @Test
    public void createUserPostTest() {
        UserPost post = new UserPost();
        post.setBody("Empty");
        post.setTitle("Test Title");
        post.setUserId("1");
        post.setId("1");
        String sampleJson = "Created Post for user id -1";
        Mockito.when(usersBlogService.createUserPost(post)).thenReturn(post);
        UserPost result = usersBlogDelegate.createUserPost(post);
        Assert.assertEquals(post.getUserId(), result.getUserId());
        Assert.assertEquals(post.getId(), result.getId());
        Assert.assertEquals(post.getTitle(), result.getTitle());
        Assert.assertEquals(post.getBody(), result.getBody());
    }

    private String getUsersPostResponses() {
        String json = "";
        List<LinkedHashMap> usersPostResponseList = new ArrayList<>();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", 1);
        linkedHashMap.put("username", "abc");
        usersPostResponseList.add(linkedHashMap);
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(usersPostResponseList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }


}
