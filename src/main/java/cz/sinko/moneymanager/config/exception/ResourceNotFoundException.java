package cz.sinko.moneymanager.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception for resource not found.
 *
 * @author Sinko Radovan
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private final String resourceName;

    private final String message;

    private ResourceNotFoundException(final String resourceName, final String message) {
        this.resourceName = resourceName;
        this.message = message;
    }

    /**
     * Create ResourceNotFoundException with resourceName and message.
     *
     * @param resourceName name of the resource
     * @param message message
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException createWith(final String resourceName, final String message) {
        return new ResourceNotFoundException(resourceName, message);
    }

    @Override
    public String getMessage() {
        return resourceName + " " + message;
    }
}
