package cn.movie.robot.service;

import cn.movie.robot.model.OperationLog;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.oplog.ProjectBaseInfoOplog;
import cn.movie.robot.vo.oplog.ProjectLastStateDetailOplog;
import cn.movie.robot.vo.oplog.ProjectShootingDetailOplog;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public interface IOplogService {
  void saveBaseInfoOplog(ProjectBaseInfoOplog newOplog, ProjectBaseInfoOplog oldOplog);

  ProjectBaseInfoOplog buildBaseInfoOplog(int projectId);

  void saveShootingOplog(ProjectShootingDetailOplog newOplog, ProjectShootingDetailOplog oldOplog);

  ProjectShootingDetailOplog buildShootingOplog(int projectId);

  void saveLastStateOplog(ProjectLastStateDetailOplog newOplog, ProjectLastStateDetailOplog oldOplod);

  ProjectLastStateDetailOplog buildLastStateOplog(int projectId);

  Result list(Pageable pageable, int targetId, int targetType);
}
