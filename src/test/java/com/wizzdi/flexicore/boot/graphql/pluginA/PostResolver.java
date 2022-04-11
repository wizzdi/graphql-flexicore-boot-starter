package com.wizzdi.flexicore.boot.graphql.pluginA;


import graphql.kickstart.tools.GraphQLResolver;

public class PostResolver implements GraphQLResolver<Post> {
    private AuthorDao authorDao;

    public PostResolver(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public Author getAuthor(Post post) {
        return authorDao.getAuthor(post.getAuthorId()).orElseThrow(RuntimeException::new);
    }
}
