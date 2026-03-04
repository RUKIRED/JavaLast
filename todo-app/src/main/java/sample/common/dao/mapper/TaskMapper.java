package sample.common.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sample.common.dao.entity.Task;

@Mapper
public interface TaskMapper {
  List<Task> findByUsername(@Param("username") String username,
      @Param("limit") int limit,
      @Param("offset") int offset);

  int countByUsername(@Param("username") String username);

  Task findById(@Param("id") Long id);

  int insert(Task task);

  int update(Task task);

  int delete(@Param("id") Long id);
}
