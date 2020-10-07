package com.spring.assignment.service;

import com.spring.assignment.model.UserPost;

import java.util.LinkedHashMap;
import java.util.List;

public interface IUsersBlogService {
    List<LinkedHashMap> getUsersDetails();

    List<LinkedHashMap> getUsersPosts();

    UserPost createUserPost(UserPost userPost);
    public String getUsersBlogs();
}
