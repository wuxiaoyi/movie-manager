package cn.movie.robot.vo.resp.search;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
@Data
public class ProjectSearchRespVo {
  private int id;
  private String sid;
  private String name;
  private Integer contractSubjectId;
  private Integer state;
  private List<ProjectSearchParentFeeRespVo> projectDetailList;
}
