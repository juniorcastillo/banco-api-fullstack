package com.banco.bancoapi.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String mensaje;
    private String detalles;

    public ErrorResponse(int status, String mensaje, String detalles) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.mensaje = mensaje;
        this.detalles = detalles;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getDetalles() {
        return detalles;
    }
}
