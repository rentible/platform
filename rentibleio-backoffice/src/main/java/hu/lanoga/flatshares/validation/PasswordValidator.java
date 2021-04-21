package hu.lanoga.flatshares.validation;

import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;

@Component
@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {


    private String passwordConfirm;

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
//		FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Validation failed", "Passwords does not match");
//		throw new ValidatorException(fmsg);
    }

}