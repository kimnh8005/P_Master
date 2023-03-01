package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.mapper.goods.item.MealContsMapper;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import kr.co.pulmuone.v1.goods.item.dto.MealContsDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealContsVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealContsService {

    @Autowired
    private final MealContsMapper mealContsMapper;

    /**
     * 식단컨텐츠 리스트 카운트 조회
     * @param mealContsListRequestDto
     * @return
     */
    protected long getMealContsListCount(MealContsListRequestDto mealContsListRequestDto) {
        return mealContsMapper.getMealContsListCount(mealContsListRequestDto);
    }

    /**
     * @Desc
     * @param
     * @return
     */
    protected List<MealContsVo> getMealContsList(MealContsListRequestDto mealContsListRequestDto){
        return mealContsMapper.getMealContsList(mealContsListRequestDto);

    }

    /**
     * 식단컨텐츠 리스트 엑셀 다운로드
     *
     * @param
     * @return List<MealContsVo>
     * @throws Exception
     */
//    protected List<MealContsVo> getExportExcelMealContsList(MealContsListRequestDto mealContsListRequestDto) throws Exception
//    {
//        List<MealContsVo> result = new ArrayList<>();
//
//        result = mealContsMapper.getExportExcelMealContsList(mealContsListRequestDto);
//
//        return result;
//    }

    /**
     * 식단 컨텐츠 등록
     */
    protected int addMealConts(MealContsDto mealContsDto){

        if(CollectionUtils.isNotEmpty(mealContsDto.getAddFileList())) {
            String PopupImagePath= mealContsDto.getAddFileList().get(0).getServerSubPath() + mealContsDto.getAddFileList().get(0).getPhysicalFileName();
            mealContsDto.setThumbnailImg(PopupImagePath);
        }

        int result =  mealContsMapper.addMealConts(mealContsDto);

        if(result > 0 && CollectionUtils.isNotEmpty(mealContsDto.getFooditemIconList())){
            // 식단컨텐츠 아이콘 등록
            mealContsMapper.addMealContsIconMapping(mealContsDto);
        }

        return result;
    }

    /**
     * 식단품목코드 중복체크
     */
    protected int isOverlapMealContsCd(String ilGoodsDailyMealContsCd){
        return mealContsMapper.isOverlapMealContsCd(ilGoodsDailyMealContsCd);
    }

    /**
     * 식단품목코드 단건 조회
     */
    protected MealContsDto getMealConts(String ilGoodsDailyMealContsCd){
        MealContsDto mealContsDto = mealContsMapper.getMealConts(ilGoodsDailyMealContsCd);

        // 식단컨텐츠 아이콘 조회
        List<FooditemIconVo> fooditemIconVoList = mealContsMapper.getFooditemIconList(ilGoodsDailyMealContsCd);
        mealContsDto.setFooditemIconList(fooditemIconVoList);

        return mealContsDto;
    }

    /**
     * 식단 컨텐츠 수정
     */
    protected int putMealConts(MealContsDto mealContsDto){

        if(CollectionUtils.isNotEmpty(mealContsDto.getAddFileList())) {
            String PopupImagePath= mealContsDto.getAddFileList().get(0).getServerSubPath() + mealContsDto.getAddFileList().get(0).getPhysicalFileName();
            mealContsDto.setThumbnailImg(PopupImagePath);
        }

        int result = mealContsMapper.putMealConts(mealContsDto);

        if(result > 0){
            // 식단컨텐츠 아이콘 삭제
            mealContsMapper.delMealContsIconMapping(mealContsDto.getIlGoodsDailyMealContsCd());

            if(CollectionUtils.isNotEmpty(mealContsDto.getFooditemIconList())) {
                mealContsDto.setCreateId(mealContsDto.getModifyId());
                // 식단컨텐츠 아이콘 등록
                mealContsMapper.addMealContsIconMapping(mealContsDto);
            }

        }

        return result;
    }

    /**
     * 식단 컨텐츠 삭제
     */
    protected int delMealConts(String ilGoodsDailyMealContsCd){
        // 식단컨텐츠 아이콘 삭제
        mealContsMapper.delMealContsIconMapping(ilGoodsDailyMealContsCd);
        return mealContsMapper.delMealConts(ilGoodsDailyMealContsCd);
    }

    protected List<FooditemIconVo> getFooditemIconList(String ilGoodsDailyMealContsCd){
        return mealContsMapper.getFooditemIconList(ilGoodsDailyMealContsCd);
    }
}
