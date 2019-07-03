package cn.movie.robot.service;

import cn.movie.robot.vo.resp.test.ProjectDetailVo;
import cn.movie.robot.vo.resp.test.ProjectVo;
import cn.movie.robot.vo.resp.test.StaffVo;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/17
 */
public class TestService {

  public static void main(String[] args){
    String key = "aaaaa";
    Integer timestamp = (int) (System.currentTimeMillis() / 1000);
    key = key + timestamp.toString();
    System.out.println(key);
//    StaffVo staffVo1 = genStaff(1, "1号员工");
//    StaffVo staffVo2 = genStaff(2, "6号员工");
//    List<StaffVo> staffVoList = new ArrayList<>();
//    staffVoList.add(staffVo1);
//    staffVoList.add(staffVo2);
//
//    ProjectDetailVo projectDetailVo1 = genProjectDetail(1, "1号费用", 100);
//    ProjectDetailVo projectDetailVo2 = genProjectDetail(2, "1号费用", 200);
//    List<ProjectDetailVo> projectDetailVoList = new ArrayList<>();
//    projectDetailVoList.add(projectDetailVo1);
//    projectDetailVoList.add(projectDetailVo2);
//
//    ProjectVo projectVo = new ProjectVo();
//    projectVo.setId(1);
//    projectVo.setProjectDetailVos(projectDetailVoList);
//    projectVo.setStaffVos(staffVoList);
//
//
//    StaffVo staffVo3 = genStaff(1, "1号员工");
//    StaffVo staffVo4 = genStaff(3, "6号员工");
//    List<StaffVo> staffVoList2 = new ArrayList<>();
//    staffVoList2.add(staffVo3);
//    staffVoList2.add(staffVo4);
//
//    ProjectDetailVo projectDetailVo3 = genProjectDetail(1, "1号费用", 500);
//    ProjectDetailVo projectDetailVo4 = genProjectDetail(2, "1号费用", 200);
//    List<ProjectDetailVo> projectDetailVoList2 = new ArrayList<>();
//    projectDetailVoList2.add(projectDetailVo3);
//    projectDetailVoList2.add(projectDetailVo4);
//
//    ProjectVo projectVo2 = new ProjectVo();
//    projectVo2.setId(1);
//    projectVo2.setProjectDetailVos(projectDetailVoList2);
//    projectVo2.setStaffVos(staffVoList2);
//
//    Javers javers = JaversBuilder.javers()
//        .build();
//
//    Diff diff = javers.compare(projectVo, projectVo2);
//    System.out.println(diff);

  }

  private static StaffVo genStaff(int id, String name){
    StaffVo staffVo = new StaffVo();
    staffVo.setId(id);
    staffVo.setName(name);
    return staffVo;
  }

  private static ProjectDetailVo genProjectDetail(int id, String name, int money){
    ProjectDetailVo projectDetailVo = new ProjectDetailVo();
    projectDetailVo.setId(id);
    projectDetailVo.setName(name);
    projectDetailVo.setMoney(money);
    return projectDetailVo;
  }
}
