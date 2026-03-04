package sample.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.common.dao.entity.LoginUser;
import sample.common.dao.mapper.LoginMapper;

@Service
public class LoginService {
  private final LoginMapper loginMapper;
  private final PasswordService passwordService;

  public LoginService(LoginMapper loginMapper, PasswordService passwordService) {
    this.loginMapper = loginMapper;
    this.passwordService = passwordService;
  }

  public LoginUser findByUsername(String username) {
    return loginMapper.findByUsername(username);
  }

  public boolean authenticate(String username, String password) {
    LoginUser user = loginMapper.findByUsername(username);
    if (user == null) {
      return false;
    }
    String hash = passwordService.hash(password);
    return hash.equals(user.getPassword());
  }

  @Transactional
  public boolean register(String username, String password) {
    if (loginMapper.findByUsername(username) != null) {
      return false;
    }
    LoginUser user = new LoginUser();
    user.setUsername(username);
    user.setPassword(passwordService.hash(password));
    loginMapper.insert(user);
    return true;
  }
}
