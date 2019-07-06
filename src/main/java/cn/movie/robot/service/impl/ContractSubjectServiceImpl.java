package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ContractSubjectRepository;
import cn.movie.robot.model.ContractSubject;
import cn.movie.robot.model.Provider;
import cn.movie.robot.service.IContractSubjectService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Service
public class ContractSubjectServiceImpl implements IContractSubjectService {

  @Resource
  ContractSubjectRepository contractSubjectRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<ContractSubject> contractSubjectPagePage = contractSubjectRepository.findAll(pageable);
    PageBean<ContractSubject> contractSubjectPageBean = new PageBean<>(
        contractSubjectPagePage.getTotalElements(),
        contractSubjectPagePage.getTotalPages(),
        contractSubjectPagePage.getContent()
    );
    return Result.succ(contractSubjectPageBean);
  }

  @Override
  public Result queryNormal() {
    List<ContractSubject> contractSubjects = contractSubjectRepository.queryByState(Constants.COMMON_STATE_NORMAL);
    return Result.succ(contractSubjects);
  }

  @Override
  public Result updateState(Integer contractId, int state) {
    ContractSubject contractSubject = contractSubjectRepository.getOne(contractId);
    if (Objects.isNull(contractSubject)){
      return Result.error("该合同主体不存在");
    }
    contractSubject.setState(state);
    contractSubjectRepository.save(contractSubject);
    return Result.succ();
  }

  @Override
  public Result save(String name) {
    ContractSubject contractSubject = new ContractSubject();
    contractSubject.setName(name);
    contractSubjectRepository.save(contractSubject);
    return Result.succ();
  }

  @Override
  public Result update(Integer contractId, String name) {
    ContractSubject contractSubject = contractSubjectRepository.getOne(contractId);
    if (Objects.isNull(contractSubject)){
      return Result.error("该合同主体不存在");
    }
    contractSubject.setName(name);
    contractSubjectRepository.save(contractSubject);
    return Result.succ();
  }
}
