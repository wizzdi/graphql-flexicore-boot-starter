package com.wizzdi.flexicore.boot.graphql.pluginA;


import graphql.kickstart.tools.GraphQLResolver;

import java.util.List;

public class AuthorResolver implements GraphQLResolver<Author> {
    private PostDao postDao;

    public AuthorResolver(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getPosts(Author author) {
        return postDao.getAuthorPosts(author.getId());
    }
}
