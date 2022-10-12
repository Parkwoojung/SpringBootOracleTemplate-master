package kopo.poly.controller;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(value = "/user")
@Controller

public class UserInfoController {

    @Resource(name = "UserInfoService")
    private IUserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + "userRegForm 시작");

        return "/user/UserRegForm";
    }

    @PostMapping(value = "insertUserInfo")
    public String insertUserInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + "insertUserInfo 시작!!");

        String msg = "";

        UserInfoDTO pDTO = null;

        try {
            String user_id = CmmUtil.nvl(request.getParameter("user_id"));
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

            log.info("user_id : " + user_id);
            log.info("user_name : " + user_name);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            pDTO = new UserInfoDTO();

            pDTO.setUser_id(user_id);
            pDTO.setUser_name(user_name);

            pDTO.setPassword((EncryUtil.encHashSHA256(password)));
            pDTO.setEmail((EncryUtil.encAES128CBC(email)));

            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            int res = userInfoService.insertUserInfo(pDTO);

            log.info("회원 가입 결과(res): " + res);

            if (res == 1) {
                msg = "회원가입이 완료 되었습니다.";
            } else if (res == 2) {
                msg = "이미 가입된 이메일 주소입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e;
            e.printStackTrace();
        } finally {

            log.info(this.getClass().getName() + "inserUserInfo 끝!!");

            model.addAttribute("msg", msg);

            model.addAttribute("pDTO", pDTO);

            pDTO = null;

        }
        return "/user/UserRegSuccess";
    }

    @GetMapping(value = "userLoginForm")
    public String userLoginForm() {
        log.info(this.getClass().getName() + "userLoginForm 시작");

        return "/user/userLoginForm";
    }

    @PostMapping(value = "getUserLoginCheck")
    public String getUserLoginCheck(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + "getUserLoginCheck 함수 시작!");

        int res = 0;

        UserInfoDTO pDTO = null;

        try {

            String user_id = CmmUtil.nvl(request.getParameter("user_id"));
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("user_id: " + user_id);
            log.info("password: " + password);

            pDTO = new UserInfoDTO();

            pDTO.setUser_id(user_id);

            pDTO.setPassword(EncryUtil.encHashSHA256(password));

            res = userInfoService.getUserLoginCheck(pDTO);

            log.info("res: " + res);

            if (res == 1) {
                session.setAttribute("SS_USER_ID", user_id);
            }
        }catch (Exception e){

                res = 2;
                log.info(e.toString());
                e.printStackTrace();
            } finally{
                log.info(this.getClass().getName() + ".insertUserInfo 끝!");

                model.addAttribute("res", String.valueOf(res));

                pDTO = null;
            }

            return "/user/LoginResult";

    }

}