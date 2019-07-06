package cn.movie.robot.dao;

import cn.movie.robot.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public interface OperationLogRepository extends JpaRepository<OperationLog, Integer> {
  Page<OperationLog> queryAllByTargetIdAndTargetType(int targetId, int targetType, Pageable pageable);
}
