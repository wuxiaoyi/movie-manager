package cn.movie.robot.dao;

import cn.movie.robot.model.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public interface OperationLogRepository extends JpaRepository<OperationLog, Integer> {
}
