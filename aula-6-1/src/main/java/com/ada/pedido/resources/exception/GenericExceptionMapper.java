package com.ada.pedido.resources.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;


@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        } else if (exception instanceof IllegalArgumentException) {
            final ErrorResponse errorResponse = new ErrorResponse(
                    exception.getClass().getName(),
                    "Parametro invalido: " + exception.getMessage(),
                    LocalDateTime.now()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();

        } else if (exception instanceof org.hibernate.exception.ConstraintViolationException) {
            final ErrorResponse errorResponse = new ErrorResponse(
                    exception.getClass().getName(),
                    "Registro já existe",
                    LocalDateTime.now()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();

        } else {
            final ErrorResponse errorResponse = new ErrorResponse(
                    exception.getClass().getName(),
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();

        }

    }
}
