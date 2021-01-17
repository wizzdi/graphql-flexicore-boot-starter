package com.wizzdi.flexicore.boot.graphql.annotations;

import com.wizzdi.flexicore.boot.graphql.init.GraphqlPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(GraphqlPluginHandlerModule.class)
@Configuration
public @interface EnableFlexiCoreGraphqlPlugins {
}
