package com.wizzdi.flexicore.boot.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.graphql.app.App;
import com.wizzdi.flexicore.boot.graphql.pluginA.Post;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import graphql.kickstart.spring.webclient.boot.GraphQLResponse;
import graphql.kickstart.spring.webclient.boot.GraphQLWebClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")

public class PluginLoadingTest {

	private static final Logger logger = LoggerFactory.getLogger(PluginLoadingTest.class);
	private static final String pluginsPath;
	private static final String entitiesPath;
	private static final String PLUGIN_ID = "myPlugin";

	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;

	@LocalServerPort
	private int randomServerPort;

	@Autowired
	private ObjectMapper objectMapper;

	private GraphQLWebClient graphqlClient;

	@BeforeAll
	void beforeEach() {
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:" + randomServerPort + "/graphql")
				.build();
		graphqlClient = GraphQLWebClient.newInstance(webClient, objectMapper);
	}


	static {
		pluginsPath = getPluginsDir("plugins");
		entitiesPath = getPluginsDir("entities");
		;
		try {
			File pluginsDir = new File(pluginsPath);
			if (!pluginsDir.exists()) {
				if (!pluginsDir.mkdirs()) {
					logger.error("failed creating plugins dir");
				}
			}
			PluginJar pluginZip = new PluginJar.Builder(pluginsDir.toPath().resolve("my-plugin-1.2.3.zip"), PLUGIN_ID)
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.Author")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.AuthorDao")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.AuthorResolver")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.GraphqlConfiguration")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.Mutation")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.Post")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.PostDao")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.PostResolver")
					.extension("com.wizzdi.flexicore.boot.graphql.pluginA.Query")

					.pluginVersion("1.2.3")
					.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) throws IOException {

		registry.add("flexicore.plugins", () -> pluginsPath);
		registry.add("flexicore.entities", () -> entitiesPath);

	}

	private static String getPluginsDir(String prefix) {
		try {
			return Files.createTempDirectory(prefix).toFile().getAbsolutePath();

		} catch (Exception e) {
			logger.error("failed getting " + prefix + " dir", e);
			return null;
		}

	}

	@Test
	public void testGraphqlPlugin() {
		GraphQLRequest request = GraphQLRequest.builder().query("query { recentPosts(count:10,offset:0){id,title} }").build();
		GraphQLResponse response = graphqlClient.post(request).block();
		List<Post> firstList = response.getFirstList(Post.class);
		Assertions.assertNotNull(firstList);
		Assertions.assertFalse(firstList.isEmpty());

	}

}
