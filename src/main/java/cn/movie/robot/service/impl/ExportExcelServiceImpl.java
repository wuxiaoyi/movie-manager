package cn.movie.robot.service.impl;

import cn.movie.robot.dao.*;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.model.Staff;
import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.service.IProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
@Service
public class ExportExcelServiceImpl implements IExportExcelService {

  @Resource
  private ProjectRepository projectRepository;

  @Resource
  private ProjectMemberRepository projectMemberRepository;

  @Resource
  private ProjectDetailRepository projectDetailRepository;

  @Resource
  private StaffRepository staffRepository;

  @Resource
  private ProviderRepository providerRepository;

  @Resource
  private FeeCategoryRepository feeCategoryRepository;

  @Override
  public InputStream exportProjects(List<Integer> projectIds) {
    List<String[]> projectExcelList = new ArrayList<>();
    String[] title = {
        "项目id", "项目编号", "项目名称", "项目执行状态",
        "所属公司-一级", "所属公司-二级", "合同签署主体",
        "拍摄日期", "拍摄周期", "成片时长",
        "项目合同金额", "项目回款金额", "项目预算金额",
        "项目实际总成本", "项目预算总成本", "项目拍摄预算",
        "项目拍摄成本", "项目后期预算", "项目后期成本",
        "项目负责人", "客户对接人", "导演", "执行导演", "文案", "制片",
        "后期剪辑", "后期合成", "美术", "音乐", "分镜"
    };
    projectExcelList.add(title);

    List<Project> projectList = projectRepository.queryByIdIn(projectIds);
    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectIdIn(projectIds);
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdInAndFeeCategoryIdIsNull(projectIds);
    List<Staff> staffList = staffRepository.findAll();
    HashMap<Integer, String> staffNameHash = buildStaffNameHash(staffList);

    for (Project project : projectList){
      List<ProjectMember> members = projectMemberList.stream().filter(projectMember -> projectMember.getProjectId().equals(project.getId())).collect(Collectors.toList());
      Stream<ProjectMember> memberStream = members.stream();
      String projectLeader = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.PROJECT_LEADER.getType());
      String customerManager = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.CUSTOMER_MANAGER.getType());
      String director = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.DIRECTOR.getType());
      String executiveDirector = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.EXECUTIVE_DIRECTOR.getType());
      String copywriting = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.COPYWRITING.getType());
      String producer = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.PRODUCER.getType());
      String postEditing = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.POST_EDITING.getType());
      String compositing = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.COMPOSITING.getType());
      String art = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.ART.getType());
      String music = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.MUSIC.getType());
      String storyBoard = buildMemberNames(memberStream, staffNameHash, ProjectMemberTypeEnum.STORY_BOARD.getType());

      String[] projectInfo = {
          project.getId().toString(), project.getSid(), project.getName(), ProjectStateEnum.getStateName(project.getState()),
          "", "", "",
          project.getShootingStartAt().toString(), project.getShootingDuration().toString(), project.getFilmDuration().toString(),
          project.getContractAmount().toString(), project.getReturnAmount().toString(), project.getBudgetCost().toString(),
          project.getRealCost().toString(), project.getBudgetCost().toString(), project.getShootingBudget().toString(),
          project.getBudgetCost().toString(), project.getLateStateBudget().toString(), project.getLateStateCost().toString(),
          projectLeader, customerManager, director, executiveDirector, copywriting, producer,
          postEditing, compositing, art, music, storyBoard
      };
      projectExcelList.add(projectInfo);
    }


    return null;
  }

  private String buildMemberNames(Stream<ProjectMember> memberStream, HashMap<Integer, String> staffNameHash, int memberType){
    return memberStream.filter(projectMember -> projectMember.getMemberType().equals(memberType))
        .map(projectMember -> staffNameHash.get(projectMember.getStaffId()))
        .collect(Collectors.joining(","));
  }

  private HashMap<Integer, String> buildStaffNameHash(List<Staff> staffList){
    HashMap<Integer, String> staffNameHash = new HashMap<>(staffList.size());
    for (Staff staff : staffList){
      staffNameHash.put(staff.getId(), staff.getName());
    }
    return staffNameHash;
  }
}
