package dio.innovation.beerAPI.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("dio.innovation.beerAPI.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo metaInfo() {
        return new ApiInfo(
                "Spring Boot Api Access Point Control",
                "API para gerenciamento de estoque de cerveja.",
                "1.0",
                null,
                new Contact("Ricardo Farias", "https://www.linkedin.com/in/ricardo14231/",
                        "ricardo14231@hotmail.com"),
                "License MIT",
                "https://opensource.org/licenses/MIT",
                new ArrayList<VendorExtension>()
        );
    }
}
