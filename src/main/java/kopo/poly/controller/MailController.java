package kopo.poly.controller;

import kopo.poly.dto.MailDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j

public class MailController {
    @Resource(name = "MailService")
    private IMailService mailService;

    @GetMapping(value = "mail/mailForm")
    public String mailForm() throws Exception {
        return "/mail/mailForm";
    }
    @PostMapping(value = "mail/sendMail")
    public String sendMail(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + "메일 발송 스타트!");

        String toMail = CmmUtil.nvl(request.getParameter("toMail"));
        String title = CmmUtil.nvl(request.getParameter("title"));
        String contents = CmmUtil.nvl(request.getParameter("contents"));

        MailDTO pDTO = new MailDTO();


        pDTO.setToMail(toMail);
        pDTO.setTitle(title);
        pDTO.setContents(contents);

        int res = mailService.doSendMail(pDTO);

        if (res == 1){
            log.info(this.getClass().getName() + "메일 발송 성공!");
        } else {
            log.info(this.getClass().getName() + "메일 발송 실패!");
        }

        model.addAttribute("res", String.valueOf(res));

        log.info(this.getClass().getName() + "메일 발송 끝!");

        return "/mail/sendMailResult";
        }
    }
