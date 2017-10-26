package com.algaworks.brewer.config;

import com.algaworks.brewer.controllers.*;
import com.algaworks.brewer.controllers.converter.CidadeConverter;
import com.algaworks.brewer.controllers.converter.EstadoConverter;
import com.algaworks.brewer.controllers.converter.EstiloConverter;
import com.algaworks.brewer.controllers.converter.GrupoConverter;
import com.algaworks.brewer.session.TabelaItensSession;
import com.algaworks.brewer.thymeleaf.dialect.BrewerDialect;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.google.common.cache.CacheBuilder;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackageClasses = {CervejasController.class, ClientesController.class, UsuariosController.class, CidadesController.class, TabelaItensSession.class, VendasController.class})
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableCaching
public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        engine.addDialect(new LayoutDialect());
        engine.addDialect(new BrewerDialect());
        engine.addDialect(new DataAttributeDialect());
        engine.addDialect(new SpringSecurityDialect());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:/templates/");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

    }

    @Bean
    public FormattingConversionService mvcConversionService() {

        DefaultFormattingConversionService defaultFormattingConversionService = new DefaultFormattingConversionService();
        defaultFormattingConversionService.addConverter(new EstiloConverter());
        defaultFormattingConversionService.addConverter(new EstadoConverter());
        defaultFormattingConversionService.addConverter(new CidadeConverter());
        defaultFormattingConversionService.addConverter(new GrupoConverter());

        NumberStyleFormatter bigdecimaFormater = new NumberStyleFormatter("#,##0.00");
        NumberStyleFormatter integerFormater = new NumberStyleFormatter("#,##0");

        defaultFormattingConversionService.addFormatterForFieldType(Integer.class, integerFormater);
        defaultFormattingConversionService.addFormatterForFieldType(BigDecimal.class, bigdecimaFormater);

        //API de datas a partir de java 8
        DateTimeFormatterRegistrar dateTimeFormater = new DateTimeFormatterRegistrar();
        dateTimeFormater.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dateTimeFormater.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm"));
        dateTimeFormater.registerFormatters(defaultFormattingConversionService);

        return defaultFormattingConversionService;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(new Locale("pt", "BR"));
    }

    @Bean
    public CacheManager cacheManager() {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(3)
                .expireAfterAccess(20, TimeUnit.SECONDS);
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(cacheBuilder);
        return cacheManager;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
        bundle.setBasename("classpath:/messages");
        bundle.setDefaultEncoding("UTF-8");
        return bundle;
    }

    @Bean
    public DomainClassConverter<FormattingConversionService> domainClassConverter(){
        return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
    }
}
