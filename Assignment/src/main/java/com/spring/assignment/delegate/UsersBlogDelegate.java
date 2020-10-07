package com.spring.assignment.delegate;

import com.spring.assignment.model.UserPost;
import com.spring.assignment.service.IUsersBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersBlogDelegate {

    @Autowired
    IUsersBlogService usersBlogService;

    public String getUsersBlogs() {
        return usersBlogService.getUsersBlogs();
    }

    public UserPost createUserPost(UserPost userPost) {
        return usersBlogService.createUserPost(userPost);
    }


}
