package com.algaworks.brewer.mail;

import com.algaworks.brewer.models.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    @Async
    public void enviar(Venda venda) {
        Context context = new Context();
        context.setVariable("venda", venda);




        String email = thymeleaf.process("mail/ResumoVenda", context);


//        mailSender.send();


    }
}
