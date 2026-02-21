package com.ada.pedido.resources.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String exceptionClass,
        String mensagem,
        LocalDateTime ocorreuEm
) {
}
