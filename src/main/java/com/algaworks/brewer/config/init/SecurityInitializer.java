package com.algaworks.brewer.config.init;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        //Máximo de tempo que uma sessão pode durar
        //        servletContext.getSessionCookieConfig().setMaxAge(20);

        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        FilterRegistration.Dynamic charCode = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
        charCode.setInitParameter("encoding", "UTF-8");
        charCode.setInitParameter("forceEncoding", "true");
        charCode.addMappingForUrlPatterns(null, false, "/*");

    }
}
