package org.cstp.meowtool;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.SecurityScheme;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * The configuration of swagger. Using swagger.enabled in application.properties to control
 * whether this component active.
 * 
 * @version 1.0
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Value("${swagger.enabled}")
    Boolean swaggerEnabled;

    private ApiInfo apiInfo() {
        return new ApiInfo("Meowtool backend API",
            "Document of Meowtool backend API", "v1.0",
            null, null,
            "MIT License", "https://github.com/XiyanFlowC/CS353Project/License", new ArrayList<>());
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            .enable(swaggerEnabled)
            .select().apis(RequestHandlerSelectors.basePackage("org.cstp.meowtool.api"))
            .build()
            .securitySchemes(securitySchemes())
            .securityContexts(securityContexts());
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> list = new ArrayList<>();
        list.add(SecurityContext.builder()
            .securityReferences(globalReferences())
            .operationSelector(oc -> !oc.requestMappingPattern().matches("/auth/.*"))
            .build()
            );
        return list;
    }

    private static final String AUTHNAME = "Authorization";

    private List<SecurityReference> globalReferences() {
        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference(AUTHNAME, new AuthorizationScope[]{new AuthorizationScope("global", "")}));
        return list;
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> list = new ArrayList<>();
        list.add(new ApiKey(AUTHNAME, "Authorization", "header"));
        return list;
    }


}
