
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) Graphql FlexiCore Boot Starter [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fgraphql-flexicore-boot-starter%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/graphql-flexicore-boot-starter/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/graphql-flexicore-boot-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22graphql-flexicore-boot-starter%22)


For comprehensive information about Graphql FlexiCore Boot Starter please visit our [site](http://wizzdi.com/).

## What it does?

Graphql FlexiCore Boot Starter is a FlexiCore Module that enables Graphql inside FlexiCore Plugins.

## How to use?
Add the graphql-flexicore-boot-starter dependency using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>graphql-flexicore-boot-starter</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCoreGraphqlPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreGraphqlPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a Graphql Configuration inside a plugin:

    @Configuration  
    @Extension  
    public class GraphqlConfiguration implements Plugin {  
          
      
        @Bean  
      public Query query() {  
            return new Query();  
      }  
      
        @Bean  
      public Mutation mutation() {  
            return new Mutation();  
      }  
      
    }
and a Graphql schema file:

    # The Root Query for the application
    extend type Query {
        getSomething(count: Int, offset: Int): [Something]!
    }
    
    # The Root Mutation for the application
    extend type Mutation {
        createSomething(name: String!) : Something!
    }

## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)


[Spring Boot Starter Web](https://search.maven.org/artifact/org.springframework.boot/spring-boot-starter-web)