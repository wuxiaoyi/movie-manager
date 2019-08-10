package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.FeeCategoryRepository;
import cn.movie.robot.model.FeeCategory;
import cn.movie.robot.model.Provider;
import cn.movie.robot.service.IFeeCategoryService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.FeeCategoryVo;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Service
public class FeeCategoryServiceImpl implements IFeeCategoryService {

  @Resource
  FeeCategoryRepository feeCategoryRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<FeeCategory> feeCategoryPage = feeCategoryRepository.findAll(pageable);
    PageBean<FeeCategory> feeCategoryPageBean = new PageBean<>(
        feeCategoryPage.getTotalElements(),
        feeCategoryPage.getTotalPages(),
        feeCategoryPage.getContent()
    );
    return Result.succ(feeCategoryPageBean);
  }

  @Override
  public Result queryAll() {
    return Result.succ(feeCategoryRepository.findAll());
  }

  @Override
  public Result updateState(Integer id, int state) {
    FeeCategory feeCategory = feeCategoryRepository.getOne(id);
    if (Objects.isNull(feeCategory)){
      return Result.error("该费用项不存在");
    }
    feeCategory.setState(state);
    feeCategoryRepository.save(feeCategory);
    return Result.succ();
  }

  @Override
  public Result save(FeeCategoryVo feeCategoryVo) {
    if (Objects.nonNull(findExistFeeCategory(feeCategoryVo))){
      return Result.error("费用项名称重复");
    }
    FeeCategory newFee = new FeeCategory();
    newFee.setStage(feeCategoryVo.getStage());
    newFee.setCategoryType(feeCategoryVo.getCategoryType());
    newFee.setName(feeCategoryVo.getName());
    newFee.setParentCategoryId(newFee.getParentCategoryId());
    feeCategoryRepository.save(newFee);
    return Result.succ();
  }

  @Override
  public Result update(Integer id, FeeCategoryVo feeCategoryVo) {
    FeeCategory existFeeCategory = findExistFeeCategory(feeCategoryVo);
    if (Objects.nonNull(existFeeCategory) && existFeeCategory.getId().equals(id)){
      return Result.error("费用项名称重复");
    }
    FeeCategory feeCategory = feeCategoryRepository.getOne(id);
    feeCategory.setStage(feeCategoryVo.getStage());
    feeCategory.setCategoryType(feeCategoryVo.getCategoryType());
    feeCategory.setName(feeCategoryVo.getName());
    feeCategory.setParentCategoryId(feeCategory.getParentCategoryId());
    feeCategoryRepository.save(feeCategory);
    return Result.succ();
  }

  private FeeCategory findExistFeeCategory(FeeCategoryVo feeCategoryVo){
    FeeCategory feeCategory;
    if (feeCategoryVo.getCategoryType() == Constants.FEE_CATEGORY_TYPE_CHILD){
      feeCategory = feeCategoryRepository.findByCategoryTypeAndParentCategoryIdAndName(
          feeCategoryVo.getCategoryType(),
          feeCategoryVo.getParentCategoryId(),
          feeCategoryVo.getName()
      );
    }else {
      feeCategory = feeCategoryRepository.findByCategoryTypeAndName(
          feeCategoryVo.getCategoryType(),
          feeCategoryVo.getName()
      );
    }
    return feeCategory;
  }
}
