package sample.web.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sample.common.service.LoginService;
import sample.web.form.LoginForm;
import sample.web.form.RegisterForm;

@Controller
public class AuthController {
  private final LoginService loginService;

  public AuthController(LoginService loginService) {
    this.loginService = loginService;
  }

  @GetMapping("/login")
  public String loginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    return "login";
  }

  @PostMapping("/login")
  public String login(@Valid LoginForm form, BindingResult result,
      @RequestParam(value = "mode", defaultValue = "web") String mode,
      HttpSession session, Model model) {
    if (result.hasErrors()) {
      return "login";
    }
    boolean ok = loginService.authenticate(form.getUsername(), form.getPassword());
    if (!ok) {
      model.addAttribute("loginError", "ユーザー名またはパスワードが違います");
      return "login";
    }
    session.setAttribute("username", form.getUsername());
    if ("api".equals(mode)) {
      return "redirect:/api/tasks";
    }
    return "redirect:/tasks";
  }

  @GetMapping("/register")
  public String registerForm(Model model) {
    model.addAttribute("registerForm", new RegisterForm());
    return "register";
  }

  @PostMapping("/register")
  public String register(@Valid RegisterForm form, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "register";
    }
    boolean ok = loginService.register(form.getUsername(), form.getPassword());
    if (!ok) {
      model.addAttribute("registerError", "そのユーザー名は既に使われています");
      return "register";
    }
    return "redirect:/login";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }
}
