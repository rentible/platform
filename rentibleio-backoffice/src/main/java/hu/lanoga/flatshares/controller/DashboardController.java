package hu.lanoga.flatshares.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DashboardController {

    @GetMapping(path = "/pages/dashboard")
    public String open() {
        return "pages/dashboard";
    }
}
