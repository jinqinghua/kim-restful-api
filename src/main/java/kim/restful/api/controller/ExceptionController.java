package kim.restful.api.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exceptions")
public class ExceptionController {

    @GetMapping(path = "/401")
    public void getAll() {
        throw new AccessDeniedException("401");
    }

}
