package com.wizzdi.flexicore.boot.graphql.pluginA;


import graphql.kickstart.tools.GraphQLQueryResolver;

import java.util.List;

public class Query implements GraphQLQueryResolver {
    private PostDao postDao;

    public Query(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getRecentPosts(int count, int offset) {
        return postDao.getRecentPosts(count, offset);
    }
}
