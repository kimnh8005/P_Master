package kr.co.pulmuone.v1.comm.mapper.goods.item;

import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import kr.co.pulmuone.v1.goods.item.dto.MealContsDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealContsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MealContsMapper {

    /**
     * 식단컨텐츠 리스트 카운트 조회
     * @param mealContsListRequestDto
     * @return
     */
    long getMealContsListCount(MealContsListRequestDto mealContsListRequestDto);

    /**
     * @Desc
     * @param mealContsListRequestDto
     * @return Page<MealContsVo> : 식단컨텐츠 검색 목록
    */
    List<MealContsVo> getMealContsList(MealContsListRequestDto mealContsListRequestDto);

//    List<MealContsVo> getExportExcelMealContsList(MealContsListRequestDto mealContsListRequestDto);

    /**
     * @Desc 식단 컨텐츠 등록
     * @param mealContsDto
     * @return
     */
    int addMealConts(MealContsDto mealContsDto);

    /** 식단품목코드 중복체크 */
    int isOverlapMealContsCd(String ilGoodsDailyMealContsCd);

    /** 식단품목코드 단건 조회 */
    MealContsDto getMealConts(String ilGoodsDailyMealContsCd);

    /** 식단 컨텐츠 수정 */
    int putMealConts(MealContsDto mealContsDto);

    /** 식단 컨텐츠 삭제 */
    int delMealConts(String ilGoodsDailyMealContsCd);

    /** 식단 컨텐츠 아이콘 등록 */
    int addMealContsIconMapping(MealContsDto mealContsDto);

    /** 식단 컨텐츠 아이콘 삭제 */
    int delMealContsIconMapping(String ilGoodsDailyMealContsCd);

    /** 식단 컨텐츠 아이콘 조회 */
    List<FooditemIconVo> getFooditemIconList(String ilGoodsDailyMealContsCd);
}
