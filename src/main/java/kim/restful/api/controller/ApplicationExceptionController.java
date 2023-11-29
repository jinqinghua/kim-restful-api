package kim.restful.api.controller;

import kim.restful.api.common.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app-exceptions")
public class ApplicationExceptionController {

    @GetMapping(path = "/401")
    public void mock401() {
        throw new BadCredentialsException("401");
    }

    @GetMapping(path = "/403")
    public void mock403() {
        throw new AccessDeniedException("403");
    }

    @GetMapping(path = "/404")
    public void mock404() {
        throw new ResourceNotFoundException("404");
    }
}
