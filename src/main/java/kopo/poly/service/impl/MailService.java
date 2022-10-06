package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


@Slf4j
@RequiredArgsConstructor
@Service("MailService")

public class MailService implements IMailService {

    private final JavaMailSender mailSender;

        @Value("${spring.mail.username}")
        private String fromMail;


    @Override
    public int doSendMail(MailDTO pDTO) {

        log.info(this.getClass().getName() + "메일 발송 서비스 시작!");

        int res = 1;

        if (pDTO == null) {
            pDTO = new MailDTO();
        }

        String toMail = CmmUtil.nvl(pDTO.getToMail());
        String title = CmmUtil.nvl(pDTO.getTitle());
        String contents = CmmUtil.nvl(pDTO.getContents());

        log.info("toMail: " + toMail);
        log.info("title: " + title);
        log.info("contents: " + contents);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

        try {

            messageHelper.setTo(toMail);
            messageHelper.setFrom(fromMail);
            messageHelper.setSubject(title);
            messageHelper.setText(contents);

            mailSender.send(message);



        } catch (Exception e) {
            res = 0;
            log.info("[ERROR]" + this.getClass().getName() + "발송 메일: " + e);

        }

        log.info(this.getClass().getName() + "메일 발송 끝");

        return res;
    }
}

