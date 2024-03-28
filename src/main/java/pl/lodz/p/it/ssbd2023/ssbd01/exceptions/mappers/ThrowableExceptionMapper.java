package pl.lodz.p.it.ssbd2023.ssbd01.exceptions.mappers;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ExceptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

import java.util.logging.Level;

import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.EXCEPTION_UNKNOWN;
import static pl.lodz.p.it.ssbd2023.ssbd01.util.converters.ExceptionConverter.mapApplicationExceptionToResponse;

@Provider
@Log
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        try {
            throw throwable;
        } catch(ApplicationException e) {
            return mapApplicationExceptionToResponse(e);
        } catch(NotFoundException e) {
            ApplicationException ex = ApplicationException.createNotFoundException();
            return mapApplicationExceptionToResponse(ex);
        } catch(NotAllowedException e) {
            ApplicationException ex = ApplicationException.createMethodNotAllowedException();
            return mapApplicationExceptionToResponse(ex);
        } catch(ForbiddenException | EJBAccessException | AccessLocalException e) {
            ApplicationException ex = ApplicationException.createAccessDeniedException();
            return mapApplicationExceptionToResponse(ex);
        } catch(WebApplicationException e) {
            return Response.status(e.getResponse().getStatus())
                    .entity(new ExceptionDTO(e.getMessage()))
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Throwable e) {
            log.log(Level.SEVERE, EXCEPTION_UNKNOWN, throwable);
            ApplicationException ex = ApplicationException.createGeneralException(e);
            return mapApplicationExceptionToResponse(ex);
        }
    }
}
