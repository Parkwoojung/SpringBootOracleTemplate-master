package kopo.poly.service;

import kopo.poly.dto.MailDTO;

import javax.mail.internet.AddressException;

public interface IMailService {

    int doSendMail(MailDTO pDTO); //메일 발송
}
