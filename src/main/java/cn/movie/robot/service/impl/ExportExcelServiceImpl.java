package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.*;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.*;
import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.utils.ExcelUtils;
import cn.movie.robot.vo.resp.search.ProjectSearchChildFeeRespVo;
import cn.movie.robot.vo.resp.search.ProjectSearchParentFeeRespVo;
import cn.movie.robot.vo.resp.search.ProjectSearchRespVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
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
  public XSSFWorkbook exportProjects(List<ProjectSearchRespVo> projectSearchRespVos) {
    List<String[]> projectExcelList = new ArrayList<>();
    String[] title = {
        "项目id", "项目编号", "项目名称", "项目执行状态", "合同签署主体",
        "一级费用名称", "预算金额", "实际金额",
        "二级费用名称", "预算金额", "实际金额", "供应商"
    };
    projectExcelList.add(title);

    HashMap<Integer, String> contractNameHash = buildContractNameHash();
    HashMap<Integer, String> feeCategoryNameHash = buildFeeCategoryNameHash();
    HashMap<Integer, String> providerNameHash = buildProviderNameHash();

    for (ProjectSearchRespVo projectVo : projectSearchRespVos){
      for (ProjectSearchParentFeeRespVo parentFeeRespVo : projectVo.getProjectDetailList()){
        if (!CollectionUtils.isEmpty(parentFeeRespVo.getChildFeeList())){
          for (ProjectSearchChildFeeRespVo childFeeRespVo : parentFeeRespVo.getChildFeeList()){
            String[] info = {
                objectToString(projectVo.getId()), projectVo.getSid(),
                projectVo.getName(), ProjectStateEnum.getStateName(projectVo.getState()),
                contractNameHash.get(projectVo.getContractSubjectId()),
                feeCategoryNameHash.get(parentFeeRespVo.getCategoryId()),
                objectToString(parentFeeRespVo.getBudgetAmount()),
                objectToString(parentFeeRespVo.getRealAmount()),
                feeCategoryNameHash.get(childFeeRespVo.getCategoryId()),
                objectToString(childFeeRespVo.getBudgetAmount()),
                objectToString(childFeeRespVo.getRealAmount()),
                providerNameHash.get(childFeeRespVo.getProviderId())
            };
            projectExcelList.add(info);
          }
        }else {
          String[] info = {
              objectToString(projectVo.getId()), projectVo.getSid(),
              projectVo.getName(), ProjectStateEnum.getStateName(projectVo.getState()),
              contractNameHash.get(projectVo.getContractSubjectId()),
              feeCategoryNameHash.get(parentFeeRespVo.getCategoryId()),
              objectToString(parentFeeRespVo.getBudgetAmount()),
              objectToString(parentFeeRespVo.getRealAmount()),
              "", "", "", ""
          };
          projectExcelList.add(info);
        }
      }
    }

    return ExcelUtils.generateWorkbook(projectExcelList);
  }

  @Override
  public XSSFWorkbook exportDetail(Integer projectId) {
    List<String[]> projectExcelList = new ArrayList<>();

    Project project = projectRepository.getOne(projectId);
    List<ProjectMember> projectMembers = projectMemberRepository.queryByProjectId(projectId);
    List<ProjectDetail> projectDetails = projectDetailRepository.queryByProjectId(projectId);


    HashMap<Integer, String> staffNameHash = buildStaffNameHash();
    HashMap<Integer, String> companyNameHash = buildCompanyNameHash();
    HashMap<Integer, String> contractNameHash = buildContractNameHash();

    /**
     * 基本信息
     */
    projectExcelList.add(new String[]{"项目基本信息", ""});
    projectExcelList.add(new String[]{"合同签署主体", contractNameHash.get(project.getContractSubjectId())});
    projectExcelList.add(new String[]{"项目编号", project.getSid()});
    projectExcelList.add(new String[]{"项目名称", project.getName()});
    projectExcelList.add(new String[]{"客户公司-一级", companyNameHash.get(project.getCompanyId())});
    projectExcelList.add(new String[]{"客户公司-二级", companyNameHash.get(project.getChildCompanyId())});
    projectExcelList.add(new String[]{"项目执行状态", ProjectStateEnum.getStateName(project.getState())});
    projectExcelList.add(new String[]{"成片时长", filmDuration(project.getFilmDuration())});
    projectExcelList.add(new String[]{"拍摄周期", shootingDuration(project.getShootingDuration())});
    projectExcelList.add(new String[]{"拍摄日期", objectToString(project.getShootingStartAt())});

    for (ProjectMemberTypeEnum memberTypeEnum : ProjectMemberTypeEnum.values()){
      projectExcelList.add(new String[]{
          memberTypeEnum.getName(),
          buildMemberNames(projectMembers, staffNameHash, memberTypeEnum.getType())
      });
    }

    projectExcelList.add(new String[]{"项目合同金额", objectToString(project.getContractAmount())});
    projectExcelList.add(new String[]{"项目回款金额", objectToString(project.getReturnAmount())});
    projectExcelList.add(new String[]{"项目预算金额", objectToString(project.getBudgetCost())});
    projectExcelList.add(new String[]{"项目实际总成本", objectToString(project.getRealCost())});
    projectExcelList.add(new String[]{"项目预算总成本", objectToString(project.getBudgetCost())});
    projectExcelList.add(new String[]{"项目拍摄预算", objectToString(project.getShootingBudget())});
    projectExcelList.add(new String[]{"项目拍摄成本", objectToString(project.getShootingCost())});
    projectExcelList.add(new String[]{"项目后期预算", objectToString(project.getLateStateBudget())});
    projectExcelList.add(new String[]{"项目后期成本", objectToString(project.getLateStateCost())});

    projectExcelList.add(new String[]{""});

    /**
     * 拍摄费用
     */
    projectExcelList.add(new String[]{
        "项目明细-拍摄费用"
    });

    List<ProjectDetail> shootingDetail = projectDetails.stream()
        .filter(projectDetail -> projectDetail.getStage().equals(Constants.PROJECT_DETAIL_STATG_SHOOTING)).collect(Collectors.toList());

    genFeeInfo(projectExcelList, shootingDetail);

    projectExcelList.add(new String[]{""});

    projectExcelList.add(new String[]{
        "项目明细-后期费用"
    });
    /**
     * 后期费用
     */
    List<ProjectDetail> lateStateDetail = projectDetails.stream()
        .filter(projectDetail -> projectDetail.getStage().equals(Constants.PROJECT_DETAIL_STATG_LAST_STATE)).collect(Collectors.toList());

    genFeeInfo(projectExcelList, lateStateDetail);

    return ExcelUtils.generateWorkbook(projectExcelList);
  }

  private String buildMemberNames(List<ProjectMember> members, HashMap<Integer, String> staffNameHash, int memberType){
    Stream<ProjectMember> memberStream = members.stream();
    return memberStream.filter(projectMember -> projectMember.getMemberType().equals(memberType))
        .map(projectMember -> staffNameHash.get(projectMember.getStaffId()))
        .collect(Collectors.joining(","));
  }

  private HashMap<Integer, String> buildStaffNameHash(){
    List<Staff> staffList = staffRepository.findAll();
    HashMap<Integer, String> staffNameHash = new HashMap<>(staffList.size());
    for (Staff staff : staffList){
      staffNameHash.put(staff.getId(), staff.getName());
    }
    return staffNameHash;
  }

  private HashMap<Integer, String> buildCompanyNameHash(){
    List<CustomerCompany> companyList = customerCompanyRepository.findAll();
    HashMap<Integer, String> companyNameHash = new HashMap<>(companyList.size());
    for (CustomerCompany company : companyList){
      companyNameHash.put(company.getId(), company.getName());
    }
    return companyNameHash;
  }

  private HashMap<Integer, String> buildContractNameHash(){
    List<ContractSubject> contractSubjectList = contractSubjectRepository.findAll();
    HashMap<Integer, String> contractSubjectNameHash = new HashMap<>(contractSubjectList.size());
    for (ContractSubject subject : contractSubjectList){
      contractSubjectNameHash.put(subject.getId(), subject.getName());
    }
    return contractSubjectNameHash;
  }

  private HashMap<Integer, String> buildFeeCategoryNameHash(){
    List<FeeCategory> feeCategories = feeCategoryRepository.findAll();
    HashMap<Integer, String> feeCategoryNameHash = new HashMap<>(feeCategories.size());
    for (FeeCategory feeCategory : feeCategories){
      feeCategoryNameHash.put(feeCategory.getId(), feeCategory.getName());
    }
    return feeCategoryNameHash;
  }

  private HashMap<Integer, String> buildProviderNameHash(){
    List<Provider> providers = providerRepository.findAll();
    HashMap<Integer, String> providerNameHash = new HashMap<>(providers.size());
    for (Provider provider : providers){
      providerNameHash.put(provider.getId(), provider.getName());
    }
    return providerNameHash;
  }

  private void genFeeInfo(List<String[]> projectExcelList, List<ProjectDetail> detailList){
    HashMap<Integer, String> feeCategoryNameHash = buildFeeCategoryNameHash();
    HashMap<Integer, String> providerNameHash = buildProviderNameHash();

    projectExcelList.add(new String[]{
        "一级费用项", "二级费用项", "预算金额", "实际金额", "供应商"
    });

    Map<Integer, List<ProjectDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(ProjectDetail::getFeeCategoryId));

    for (Map.Entry<Integer, List<ProjectDetail>> entry : detailMap.entrySet()){
      String parentCategoryName = feeCategoryNameHash.get(entry.getKey());

      List<ProjectDetail> projectDetailList = entry.getValue();
      for (ProjectDetail projectDetail : projectDetailList){
        if (Objects.isNull(projectDetail.getFeeChildCategoryId())){
          continue;
        }

        String providerName = "";
        if (Objects.nonNull(projectDetail.getProviderId())){
          providerName = providerNameHash.get(projectDetail.getProviderId());
        }

        projectExcelList.add(new String[]{
            parentCategoryName, feeCategoryNameHash.get(projectDetail.getFeeChildCategoryId()),
            objectToString(projectDetail.getBudgetAmount()), objectToString(projectDetail.getRealAmount()),
            providerName
        });
      }

      ProjectDetail parentDetail = projectDetailList.stream()
          .filter(projectDetail -> Objects.isNull(projectDetail.getFeeChildCategoryId())).findFirst().orElse(null);

      projectExcelList.add(new String[]{
          parentCategoryName + "总费用", "", objectToString(parentDetail.getBudgetAmount()), objectToString(parentDetail.getRealAmount()), ""
      });

      projectExcelList.add(new String[]{""});
    }
  }

  private String filmDuration(Integer duration){
    if (Objects.isNull(duration)){
      return "";
    }
    int hour = duration / 60;
    int minute = duration % 60;
    String result = "";
    if (hour != 0){
      result += hour + "小时";
    }
    if (minute != 0){
      result += minute + "分钟";
    }
    return result;
  }

  private String shootingDuration(Integer duration){
    if (Objects.isNull(duration)){
      return "";
    }
    return duration.toString() + "天";
  }

  private String objectToString(Object object){
    if (Objects.isNull(object)){
      return "";
    }
    return object.toString();
  }

//  @Override
//  public XSSFWorkbook exportProjects(List<Integer> projectIds) {
//    List<String[]> projectExcelList = new ArrayList<>();
//    String[] title = {
//        "项目id", "项目编号", "项目名称", "项目执行状态",
//        "所属公司-一级", "所属公司-二级", "合同签署主体",
//        "拍摄日期", "拍摄周期", "成片时长",
//        "项目合同金额", "项目回款金额", "项目预算金额",
//        "项目实际总成本", "项目预算总成本", "项目拍摄预算",
//        "项目拍摄成本", "项目后期预算", "项目后期成本",
//        "项目负责人", "客户对接人", "导演", "执行导演", "文案", "制片",
//        "后期剪辑", "后期合成", "美术", "音乐", "分镜"
//    };
//    projectExcelList.add(title);
//
//    List<Project> projectList = projectRepository.queryByIdIn(projectIds);
//    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectIdIn(projectIds);
//    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdInAndFeeCategoryIdIsNull(projectIds);
//
//    HashMap<Integer, String> staffNameHash = buildStaffNameHash();
//    HashMap<Integer, String> companyNameHash = buildCompanyNameHash();
//    HashMap<Integer, String> contractNameHash = buildContractNameHash();
//
//    for (Project project : projectList){
//      List<ProjectMember> members = projectMemberList.stream().filter(projectMember -> projectMember.getProjectId().equals(project.getId())).collect(Collectors.toList());
//      String projectLeader = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.PROJECT_LEADER.getType());
//      String customerManager = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.CUSTOMER_MANAGER.getType());
//      String director = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.DIRECTOR.getType());
//      String executiveDirector = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.EXECUTIVE_DIRECTOR.getType());
//      String copywriting = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.COPYWRITING.getType());
//      String producer = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.PRODUCER.getType());
//      String postEditing = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.POST_EDITING.getType());
//      String compositing = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.COMPOSITING.getType());
//      String art = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.ART.getType());
//      String music = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.MUSIC.getType());
//      String storyBoard = buildMemberNames(members, staffNameHash, ProjectMemberTypeEnum.STORY_BOARD.getType());
//
//      String[] projectInfo = {
//          project.getId().toString(), project.getSid(), project.getName(), ProjectStateEnum.getStateName(project.getState()),
//          companyNameHash.get(project.getCompanyId()), companyNameHash.get(project.getChildCompanyId()), contractNameHash.get(project.getContractSubjectId()),
//          objectToString(project.getShootingStartAt()), objectToString(project.getShootingDuration()), objectToString(project.getFilmDuration()),
//          objectToString(project.getContractAmount()), project.getReturnAmount().toString(), objectToString(project.getBudgetCost()),
//          objectToString(project.getRealCost()), objectToString(project.getBudgetCost()), objectToString(project.getShootingBudget()),
//          objectToString(project.getBudgetCost()), objectToString(project.getLateStateBudget()), objectToString(project.getLateStateCost()),
//          projectLeader, customerManager, director, executiveDirector, copywriting, producer,
//          postEditing, compositing, art, music, storyBoard
//      };
//      projectExcelList.add(projectInfo);
//    }
//
//    return ExcelUtils.generateWorkbook(projectExcelList);
//  }
}
