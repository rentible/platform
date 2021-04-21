package hu.lanoga.flatshares.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping(path = "/pages/settings")
    public String open() {

        return "pages/settings";
    }
}
