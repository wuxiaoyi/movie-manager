package cn.movie.robot.service;

/**
 * @author Wuxiaoyi
 * @date 2019/9/11
 */
public interface ITaskService {
  void refreshProjectAmount(Integer projectId);
  void fixProjectCreator();
}
