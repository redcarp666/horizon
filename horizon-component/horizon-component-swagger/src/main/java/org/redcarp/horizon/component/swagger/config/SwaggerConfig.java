package org.redcarp.horizon.component.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer{

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("org.redcarp"))
                .build()
                .securitySchemes(securitySchemes()).securityContexts(securityContexts());
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("redcarp horizon")
                .description("redcarp horizon")
                .version("1.0.0")
                .contact(new Contact("redcarp","http://www.baidu.com",""))
                .build();
    }

    private List<SecurityScheme> securitySchemes(){
        List<SecurityScheme> securitySchemeList = new ArrayList<SecurityScheme>();
        securitySchemeList.add(new ApiKey("token", "token", "header"));
        return securitySchemeList;
    }

    private List<SecurityContext> securityContexts()
    {
        return Arrays.asList(SecurityContext.builder()
                .securityReferences(Arrays.asList(
                        new SecurityReference("token", new AuthorizationScope[]{})
                )).build());
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** swagger-ui 地址 */
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/");
    }






}
