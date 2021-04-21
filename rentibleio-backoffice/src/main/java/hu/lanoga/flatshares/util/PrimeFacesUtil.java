package hu.lanoga.flatshares.util;

import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

@Slf4j
public class PrimeFacesUtil {

    /**
     * JSF based redirect
     * TODO: kell ez? Nem jó a springes redirect?
     *
     * @param path Redirect to path
     */
    public static void redirect(String path) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    externalContext.redirect(externalContext.getRequestContextPath() + path);
        } catch (IOException e) {
            log.error(Encode.forJava("An error has occurred during redirection. Path: " + path), e);
        }
    }

    private PrimeFacesUtil() {
    }
}
