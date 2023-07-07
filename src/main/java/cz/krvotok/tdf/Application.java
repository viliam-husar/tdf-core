package cz.krvotok.tdf;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "Tour de Felvídek RouteSearcher",
            version = "0.666",
            license = @License(name = "Apache 2.0", url = "https://foo.bar"),
            contact = @Contact(name = "Ludvík Krvotok", email = "ludvik.krvotok@temnesousta.cz")

    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}