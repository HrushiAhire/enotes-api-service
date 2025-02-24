package com.enotes.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig 
{
	@Bean
	public OpenAPI openAPI()
	{
		OpenAPI openAPI = new OpenAPI();
		
		Info info = new Info();
		info.setTitle("Enotes API");
		info.setDescription("Enotes API");
		info.setVersion("1.0.0");
		info.setContact(new Contact().email("hrushikesh.ahire@gmail.com").name("Hrushikesh"));
		info.setLicense(new License().name("Enotes 1.0"));
		
		List<Server> listOfServers = List.of(new Server().description("Dev").url("http://localhost:8081"),
		new Server().description("Test").url("http://localhost:8082"),
		new Server().description("Prod").url("http://localhost:8083"));
		
		SecurityScheme securityScheme = new SecurityScheme().name("Authorization")
				.scheme("bearer").type(Type.HTTP).bearerFormat("JWT").in(In.HEADER);
		
		Components components = new Components().addSecuritySchemes("Token", securityScheme);
		
		openAPI.setInfo(info);
		openAPI.setServers(listOfServers);
		openAPI.setComponents(components);
		openAPI.setSecurity(List.of(new SecurityRequirement().addList("Token")));
		
		return openAPI;
	}
}
