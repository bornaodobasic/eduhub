package fer.progi.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping( "/**")
		.allowedOriginPatterns("*")
		.allowedMethods("GET", "POST", "PUT", "DELETE")
		.allowedHeaders("*")
		.allowCredentials(true);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Forward all unknown routes to index.html
		registry.addViewController("/{spring:[a-zA-Z0-9-_]+}")
				.setViewName("forward:/index.html");
		registry.addViewController("/**/{spring:[a-zA-Z0-9-_]+}")
				.setViewName("forward:/index.html");
		registry.addViewController("/{spring:[a-zA-Z0-9-_]+}/**{spring:[a-zA-Z0-9-_]+}")
				.setViewName("forward:/index.html");
	}
	
}

