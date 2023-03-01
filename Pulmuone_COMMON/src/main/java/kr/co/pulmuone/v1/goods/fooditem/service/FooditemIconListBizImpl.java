package kr.co.pulmuone.v1.goods.fooditem.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.goods.etc.service.GoodsCertificationBiz;
import kr.co.pulmuone.v1.goods.etc.service.GoodsCertificationService;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListResponseDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconResponseDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <PRE>
 * 식단품목 아이콘 리스트 BizImpl
 * </PRE>
 */
@Service
public class FooditemIconListBizImpl implements FooditemIconListBiz {

    @Autowired
    FooditemIconListService fooditemIconListService;

    /**
     * 식단품목아이콘 목록 조회
     * @param fooditemIconListRequestDto
     * @return
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public FooditemIconListResponseDto getFooditemIconList(FooditemIconListRequestDto fooditemIconListRequestDto){
        FooditemIconListResponseDto fooditemIconListResponseDto = new FooditemIconListResponseDto();

        Page<FooditemIconVo> certificationList = fooditemIconListService.getFooditemIconList(fooditemIconListRequestDto);

        fooditemIconListResponseDto.setTotal(certificationList.getTotal());
        fooditemIconListResponseDto.setRows(certificationList.getResult());

        return fooditemIconListResponseDto;
    }

    /**
     * 식단품목아이콘 상세 조회
     * @param ilFooditemIconId
     * @return
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public FooditemIconResponseDto getFooditemIcon(long ilFooditemIconId){
        FooditemIconResponseDto fooditemIconResponseDto = new FooditemIconResponseDto();

        FooditemIconVo fooditemIconVo = fooditemIconListService.getFooditemIcon(ilFooditemIconId);
        fooditemIconResponseDto.setFooditemIconVo(fooditemIconVo);

        return fooditemIconResponseDto;
    }

    /**
     * 식단품목아이콘 추가
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception{

        if( CollectionUtils.isNotEmpty(fooditemIconRequestDto.getAddFileList()) ) {
            for(FileVo fileVo : fooditemIconRequestDto.getAddFileList()) {
                fooditemIconRequestDto.setImagePath(fileVo.getServerSubPath());
                fooditemIconRequestDto.setImageName(fileVo.getPhysicalFileName());
                fooditemIconRequestDto.setImageOriginName(fileVo.getOriginalFileName());
            }
        }

        fooditemIconListService.addFooditemIcon(fooditemIconRequestDto);

        return ApiResult.success();
    }

    /**
     * 식단품목아이콘 수정
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception{

        if( CollectionUtils.isNotEmpty(fooditemIconRequestDto.getAddFileList()) ) {
            for(FileVo fileVo : fooditemIconRequestDto.getAddFileList()) {
                fooditemIconRequestDto.setImagePath(fileVo.getServerSubPath());
                fooditemIconRequestDto.setImageName(fileVo.getPhysicalFileName());
                fooditemIconRequestDto.setImageOriginName(fileVo.getOriginalFileName());
            }
        }

        fooditemIconListService.putFooditemIcon(fooditemIconRequestDto);

        return ApiResult.success();
    }

    /**
     * 식단품목아이콘 삭제
     * @param fooditemIconRequestDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) throws Exception{

        fooditemIconListService.delFooditemIcon(fooditemIconRequestDto);

        return ApiResult.success();
    }

    /**
     * 식단품목아이콘 목록 드롭다운 리스트
     * @param fooditemIconListRequestDto
     * @return
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public FooditemIconListResponseDto getFooditemIconDropDownList(FooditemIconListRequestDto fooditemIconListRequestDto){
        FooditemIconListResponseDto fooditemIconListResponseDto = new FooditemIconListResponseDto();

        List<FooditemIconVo> certificationList = fooditemIconListService.getFooditemIconList(fooditemIconListRequestDto);
        fooditemIconListResponseDto.setRows(certificationList);

        return fooditemIconListResponseDto;
    }
}
