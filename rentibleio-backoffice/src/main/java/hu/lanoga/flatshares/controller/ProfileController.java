package hu.lanoga.flatshares.controller;

import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Getter
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfileController {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final UserService userService;
    private User user;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/pages/profile")
    public String open() {
        user = userService.findOne(userService.getMyId());

        return "pages/profile";
    }


    public void modify() {
        userService.update(user);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Profile saved"));
    }
}
