package com.wizzdi.flexicore.boot.graphql.service;

import com.coxautodev.graphql.tools.*;
import com.oembedler.moon.graphql.boot.SchemaStringProvider;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import graphql.schema.GraphQLScalarType;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class FlexiCoreGraphQLResolvers {

	@Lazy
	@Autowired
	private FlexiCorePluginManager pluginManager;
	@Autowired(required = false)
	private SchemaParserDictionary dictionary;

	@Autowired(required = false)
	private GraphQLScalarType[] scalars;

	@Autowired(required = false)
	private SchemaParserOptions options;

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean(SchemaParser.class)
	public SchemaParser schemaParser( SchemaStringProvider schemaStringProvider) throws IOException {
		List<GraphQLResolver<?>> resolvers=new ArrayList<>();
		for (PluginWrapper startedPlugin : pluginManager.getStartedPlugins()) {

			ApplicationContext applicationContext=pluginManager.getApplicationContext(startedPlugin);
			Map<String, GraphQLResolver> beansOfType = applicationContext.getBeansOfType(GraphQLResolver.class);
			resolvers.addAll(beansOfType.values().stream().map(f->(GraphQLResolver<?>)f).collect(Collectors.toList()));

		}
		SchemaParserBuilder builder = dictionary != null ? new SchemaParserBuilder(dictionary) : new SchemaParserBuilder();

		List<String> schemaStrings = schemaStringProvider.schemaStrings();
		schemaStrings.forEach(builder::schemaString);

		if (scalars != null) {
			builder.scalars(scalars);
		}

		if (options != null) {
			builder.options(options);
		}

		return builder.resolvers(resolvers)
				.build();
	}
}
