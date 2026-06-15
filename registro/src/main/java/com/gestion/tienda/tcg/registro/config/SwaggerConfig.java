package com.gestion.tienda.tcg.registro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearerAuth")
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .name("bearerAuth")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )

                .info(new Info()
                        .title("Sistema de Microservicios: Tienda TCG")
                        .version("1.0")
                        .description("""
<pre>
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____
U /"___|uU |  _"\\ uU |"|u| |U|  _"\\ u  \\/"_ \\/              \\/"_ \\/U|' \\/ '|uU /"___|u
\\| |  _ / \\| |_) |/ \\| |\\| |\\| |_) |/  | | | |     U  u     | | | |\\| |\\/| |/\\| |  _ /
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\\.-,_| |_| | | |  | |  | |_| |
  \\____|   |_| \\_\\  <<\\___/  |_|    \\_)-\\___/     |__"__|\\_)-\\___/  |_|  |_|   \\____|
  _)(|_    //   \\\\_(__) )(   ||>>_       \\\\                   \\\\   <<,-,,-.    _)(|_
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \\.)  (__)__)

                                       API - [REGISTRO]
                                          GRUPO OMG
</pre>
"""));


    }


}
