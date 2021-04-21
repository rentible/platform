package org.wallride.support;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    //TODO Azért kell ez nekünk, mert jelenleg csak a war build megy és war esetén(standalone tomcat-tel) nem megy a sima contextprovider...
    //https://stackoverflow.com/questions/38795287/webapplicationcontextutils-getwebapplicationcontextservletcontext-returns-null
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (ContextProvider.context == null) {
            ContextProvider.context = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
