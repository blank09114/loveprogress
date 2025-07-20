package packages.loveprogress.setting;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/web/tmp/uploads/");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.mediaType("png", MediaType.IMAGE_PNG);
        configurer.mediaType("jpg", MediaType.IMAGE_JPEG);
        configurer.mediaType("jpeg", MediaType.IMAGE_JPEG);
        configurer.mediaType("gif", MediaType.IMAGE_GIF);
    }
}