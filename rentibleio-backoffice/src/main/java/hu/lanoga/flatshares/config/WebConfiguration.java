package hu.lanoga.flatshares.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Value("${flatshares.public.media}")
	private String media;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("/public/media/**")
                .addResourceLocations("file:///" + media)
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
	}
}
