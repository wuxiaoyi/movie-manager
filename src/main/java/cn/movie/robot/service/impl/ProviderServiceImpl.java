package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ProviderRepository;
import cn.movie.robot.model.Provider;
import cn.movie.robot.service.IProviderService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProviderVo;
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
public class ProviderServiceImpl implements IProviderService {

  @Resource
  ProviderRepository providerRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<Provider> providerPage = providerRepository.findAll(pageable);
    PageBean<Provider> providerPageBean = new PageBean<>(
        providerPage.getTotalElements(),
        providerPage.getTotalPages(),
        providerPage.getContent()
    );
    return Result.succ(providerPageBean);
  }

  @Override
  public Result queryNormal() {
    List<Provider> providers = providerRepository.queryByState(Constants.COMMON_STATE_NORMAL);
    return Result.succ(providers);
  }

  @Override
  public Result updateState(Integer providerId, int state) {
    Provider provider = providerRepository.getOne(providerId);
    if (Objects.isNull(provider)){
      return Result.error("该供应商不存在");
    }
    provider.setState(state);
    providerRepository.save(provider);
    return Result.succ();
  }

  @Override
  public Result save(ProviderVo providerVo) {
    Provider provider = new Provider();
    provider.setName(providerVo.getName());
    provider.setBankName(providerVo.getBankName());
    provider.setBankAccount(providerVo.getBankAccount());
    provider.setCellphone(providerVo.getCellphone());
    providerRepository.save(provider);
    return Result.succ();
  }

  @Override
  public Result update(Integer providerId, ProviderVo providerVo) {
    Provider provider = providerRepository.getOne(providerId);
    if (Objects.isNull(provider)){
      return Result.error("该供应商不存在");
    }
    provider.setName(providerVo.getName());
    provider.setBankName(providerVo.getBankName());
    provider.setBankAccount(providerVo.getBankAccount());
    provider.setCellphone(providerVo.getCellphone());
    providerRepository.save(provider);
    return Result.succ();
  }
}
