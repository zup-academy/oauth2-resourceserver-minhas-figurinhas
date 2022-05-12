package base;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * This advice is necessary because MockMvc is not a real servlet environment, therefore it does not redirect error
 * responses to [ErrorController], which produces validation response. So we need to fake it in tests.
 * It's not ideal, but at least we can use classic MockMvc tests for testing error response + document it.
 */
@Profile("test")
@ControllerAdvice
public class KeepErrorMessagesWithMockMvcAdvice {

    private final BasicErrorController errorController;

    public KeepErrorMessagesWithMockMvcAdvice(BasicErrorController errorController) {
        this.errorController = errorController;
    }

    // add any exceptions/validations/binding problems
    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class } )
    public ResponseEntity<?> defaultErrorHandler(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", 400);
        request.setAttribute("javax.servlet.error.request_uri", request.getPathInfo());
        return errorController.error(request);
    }
}
