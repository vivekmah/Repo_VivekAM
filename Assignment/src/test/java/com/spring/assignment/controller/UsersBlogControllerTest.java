package com.spring.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.assignment.delegate.UsersBlogDelegate;
import com.spring.assignment.model.UserPost;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UsersBlogController.class)
@WithMockUser
public class UsersBlogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersBlogDelegate usersBlogDelegate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before()
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getUserPostsTest() throws Exception {
        String jsonResponse = getUsersPostResponses();
        Mockito.when(usersBlogDelegate.getUsersBlogs()).thenReturn(jsonResponse);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/blogs/getusersblogs").accept(
                APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(jsonResponse, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void createPostTest() throws Exception {
        UserPost postInput = new UserPost();
        postInput.setBody("Empty");
        postInput.setTitle("Test Title");
        postInput.setUserId("1");
        postInput.setId("1");
        UserPost postOutput = new UserPost();
        postOutput.setBody("Empty");
        postOutput.setTitle("Test Title");
        postOutput.setUserId("1");
        postOutput.setId("101");
        Mockito.when(usersBlogDelegate.createUserPost(postOutput)).thenReturn(postOutput);
        String userPostJson = getUserPostJson(postInput);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/blogs/createpost")
                .accept(MediaType.APPLICATION_JSON).content(userPostJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());

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

    private String getUserPostJson(UserPost userPost) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(userPost);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
