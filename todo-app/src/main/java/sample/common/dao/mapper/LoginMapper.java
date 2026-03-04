package sample.common.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sample.common.dao.entity.LoginUser;

@Mapper
public interface LoginMapper {
  LoginUser findByUsername(@Param("username") String username);

  int insert(LoginUser user);
}
