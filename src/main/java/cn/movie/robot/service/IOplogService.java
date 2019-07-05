package cn.movie.robot.service;

import cn.movie.robot.vo.oplog.ProjectBaseInfoOplog;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public interface IOplogService {
  void saveBaseInfoOplog(ProjectBaseInfoOplog newOplog, ProjectBaseInfoOplog oldOplog);

  ProjectBaseInfoOplog buildBaseInfoOplog(int projectId);

}
