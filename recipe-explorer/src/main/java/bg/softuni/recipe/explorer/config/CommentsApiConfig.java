package bg.softuni.recipe.explorer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "comments.api")
public class CommentsApiConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public CommentsApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
