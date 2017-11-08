package com.algaworks.brewer.mail;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.ItemVenda;
import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.storage.FotoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class Mailer {

    private static Logger logger = LoggerFactory.getLogger(Mailer.class);

    @Autowired
    private FotoStorage fotoStorage;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    @Async
    public void enviar(Venda venda) {
        Context context = new Context(new Locale("pt","BR"));
        context.setVariable("venda", venda);
        context.setVariable("logo", "logo");

        Map<String, String> fotos = new HashMap<>();

        boolean adicionarMockCerveja = false;
        for (ItemVenda item : venda.getItens()) {
            Cerveja cerveja = item.getCerveja();
            if (!StringUtils.isEmpty(cerveja.getFoto())) {
                String cid = "foto-" + cerveja.getCodigo();
                context.setVariable(cid, cid);
                fotos.put(cid, cerveja.getFoto() + "|" + cerveja.getContentType());
            }else {
                adicionarMockCerveja = true;
                context.setVariable("mockCerveja","mockCerveja" );
            }
        }

        try {
            String email = thymeleaf.process("mail/ResumoVenda", context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("jillohh@gmail.com");
            helper.setTo(venda.getCliente().getEmail());
            helper.setSubject(String.format("Brewer - Venda nº %d", venda.getCodigo()));
            helper.setText(email, true);
            helper.addInline("logo", new ClassPathResource("static/layout/images/logo-gray.png"));

            if (adicionarMockCerveja) {
                helper.addInline("mockCerveja", new ClassPathResource("static/layout/images/cerveja-mock.png"));

            }

            for (String cid : fotos.keySet()) {
                String fotoContentType[] =  fotos.get(cid).split("\\|");
                String foto = fotoContentType[0];
                String contentType = fotoContentType[1];

                byte[] arrayFoto = fotoStorage.recuperarThumbnail(foto);
                helper.addInline(cid, new ByteArrayResource(arrayFoto), contentType);
            }

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Erro enviando email", e);
        }

    }
}
