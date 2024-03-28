package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ExceptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

public class ExceptionConverter {

    public static Response mapApplicationExceptionToResponse(ApplicationException e) {
        return Response.status(e.getResponse().getStatus())
                .entity(new ExceptionDTO(e.getKey()))
                .header("Content-Type", "application/json")
                .build();
    }
}
