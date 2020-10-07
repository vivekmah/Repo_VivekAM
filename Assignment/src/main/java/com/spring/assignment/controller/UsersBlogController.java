package com.spring.assignment.controller;

import com.spring.assignment.delegate.UsersBlogDelegate;
import com.spring.assignment.model.UserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersBlogController {

    @Autowired
    UsersBlogDelegate usersBlogDelegate;


    @RequestMapping(value = "/blogs/getusersblogs")
    public String getUserPosts() {
        String usersBlogs = "";
        usersBlogs = usersBlogDelegate.getUsersBlogs();
        return usersBlogs;

    }

    @PostMapping(path = "/blogs/createpost")
    public UserPost createPost(@RequestBody UserPost userPost) {

        return usersBlogDelegate.createUserPost(userPost);
    }


}
