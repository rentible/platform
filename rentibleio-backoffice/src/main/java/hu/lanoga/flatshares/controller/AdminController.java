package hu.lanoga.flatshares.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "/pages/admin")
    public String open() {

        return "pages/admin";
    }
}
