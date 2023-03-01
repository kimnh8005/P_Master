package kr.co.pulmuone.v1.comm.mapper.goods.fooditem;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FooditemIconListMapper {

    /**
     * 식단품목아이콘 목록 조회
     * @param fooditemIconListRequestDto
     * @return
     */
    Page<FooditemIconVo> getFooditemIconList(FooditemIconListRequestDto fooditemIconListRequestDto);

    /**
     * 식단품목아이콘 상세 조회
     * @param ilFooditemIconId
     * @return
     */
    FooditemIconVo getFooditemIcon(@Param("ilFooditemIconId") long ilFooditemIconId);

    /**
     * 식단품목아이콘 추가
     * @param fooditemIconRequestDto
     * @return
     */
    int addFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto);

    /**
     * 식단품목아이콘 수정
     * @param fooditemIconRequestDto
     * @return
     */
    int putFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto);

    /**
     * 식단품목아이콘 삭제
     * @param fooditemIconRequestDto
     * @return
     */
    int delFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto);
}
