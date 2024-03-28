package pl.lodz.p.it.ssbd2023.ssbd01.config;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.ExceptionConverter;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.PRECONDITION_FAILED;

@Provider
@ETagFilterBinding
@Log
public class EtagFilter implements ContainerRequestFilter {

    @Inject
    private EntityIdentitySignerVerifier entityIdentitySignerVerifier;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String header = requestContext.getHeaderString("If-Match");
        if (header == null || header.isEmpty()) {
            ApplicationException e = ApplicationException.createEtagEmptyException();
            Response response = ExceptionConverter.mapApplicationExceptionToResponse(e);
            requestContext.abortWith(response);
        } else if (!entityIdentitySignerVerifier.validateEntitySignature(header)) {
            ApplicationException e = ApplicationException.createEtagNotValidException();
            Response response = ExceptionConverter.mapApplicationExceptionToResponse(e);
            requestContext.abortWith(response);
        }
    }
}
