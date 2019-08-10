package cn.movie.robot.service.impl;

import cn.movie.robot.dao.*;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.*;
import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

  @Resource
  private CustomerCompanyRepository customerCompanyRepository;

  @Resource
  private ContractSubjectRepository contractSubjectRepository;

  @Override
  public XSSFWorkbook exportProjects(List<Integer> projectIds) {
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

    List<CustomerCompany> companyList = customerCompanyRepository.findAll();
    HashMap<Integer, String> companyNameHash = buildCompanyNameHash(companyList);

    List<ContractSubject> contractSubjectList = contractSubjectRepository.findAll();
    HashMap<Integer, String> contractNameHash = buildContractNameHash(contractSubjectList);

    for (Project project : projectList){
      List<ProjectMember> members = projectMemberList.stream().filter(projectMember -> projectMember.getProjectId().equals(project.getId())).collect(Collectors.toList());
      String projectLeader = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.PROJECT_LEADER.getType());
      String customerManager = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.CUSTOMER_MANAGER.getType());
      String director = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.DIRECTOR.getType());
      String executiveDirector = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.EXECUTIVE_DIRECTOR.getType());
      String copywriting = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.COPYWRITING.getType());
      String producer = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.PRODUCER.getType());
      String postEditing = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.POST_EDITING.getType());
      String compositing = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.COMPOSITING.getType());
      String art = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.ART.getType());
      String music = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.MUSIC.getType());
      String storyBoard = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.STORY_BOARD.getType());

      String[] projectInfo = {
          project.getId().toString(), project.getSid(), project.getName(), ProjectStateEnum.getStateName(project.getState()),
          companyNameHash.get(project.getCompanyId()), companyNameHash.get(project.getChildCompanyId()), contractNameHash.get(project.getContractSubjectId()),
          objectToString(project.getShootingStartAt()), objectToString(project.getShootingDuration()), objectToString(project.getFilmDuration()),
          objectToString(project.getContractAmount()), project.getReturnAmount().toString(), objectToString(project.getBudgetCost()),
          objectToString(project.getRealCost()), objectToString(project.getBudgetCost()), objectToString(project.getShootingBudget()),
          objectToString(project.getBudgetCost()), objectToString(project.getLateStateBudget()), objectToString(project.getLateStateCost()),
          projectLeader, customerManager, director, executiveDirector, copywriting, producer,
          postEditing, compositing, art, music, storyBoard
      };
      projectExcelList.add(projectInfo);
    }

    return ExcelUtils.generateWorkbook(projectExcelList);
  }

  private String buildMemberNames(List<ProjectMember> members, HashMap<Integer, String> staffNameHash, int memberType){
    Stream<ProjectMember> memberStream = members.stream();
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

  private HashMap<Integer, String> buildCompanyNameHash(List<CustomerCompany> companyList){
    HashMap<Integer, String> companyNameHash = new HashMap<>(companyList.size());
    for (CustomerCompany company : companyList){
      companyNameHash.put(company.getId(), company.getName());
    }
    return companyNameHash;
  }

  private HashMap<Integer, String> buildContractNameHash(List<ContractSubject> contractSubjectList){
    HashMap<Integer, String> contractSubjectNameHash = new HashMap<>(contractSubjectList.size());
    for (ContractSubject subject : contractSubjectList){
      contractSubjectNameHash.put(subject.getId(), subject.getName());
    }
    return contractSubjectNameHash;
  }

  private String objectToString(Object object){
    if (Objects.isNull(object)){
      return "";
    }
    return object.toString();
  }
}
