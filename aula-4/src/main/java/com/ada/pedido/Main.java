package com.ada.pedido;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}

// http://localhost:8080/q/dev-ui/extensions
//  http://localhost:8080/h2-console
