package hu.lanoga.flatshares.controller;

import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.model.UserDetail;
import hu.lanoga.flatshares.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Controller
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegisterController {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UserService userService;
    private User user;
    private UserDetail userDetail;

    public RegisterController(UserService userService) {
        this.userService = userService;
        this.user = new User();
        this.user.setUserDetail(new UserDetail());
    }

    @GetMapping(path = "/pages/public/register")
    public String open() {

        return "pages/public/register";
    }

    public String qrCodePage() {

        return "pages/public/qrcode";
    }

    public void save() {
        userService.register(user);
    }
}
