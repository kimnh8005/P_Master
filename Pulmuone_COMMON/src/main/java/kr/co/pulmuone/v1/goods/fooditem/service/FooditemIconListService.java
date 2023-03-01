package kr.co.pulmuone.v1.goods.fooditem.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.goods.etc.GoodsCertificationMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.fooditem.FooditemIconListMapper;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconListRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.FooditemIconRequestDto;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * 식단품목 아이콘 리스트 Service
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class FooditemIconListService {

    @Autowired
    private final FooditemIconListMapper fooditemIconListMapper;

    /**
     * 식단품목아이콘 목록 조회
     * @param fooditemIconListRequestDto
     * @return
     */
    protected Page<FooditemIconVo> getFooditemIconList(FooditemIconListRequestDto fooditemIconListRequestDto) {
        PageMethod.startPage(fooditemIconListRequestDto.getPage(), fooditemIconListRequestDto.getPageSize());
        return fooditemIconListMapper.getFooditemIconList(fooditemIconListRequestDto);
    }

    /**
     * 식단품목아이콘 상세 조회
     * @param ilFooditemIconId
     * @return
     */
    protected FooditemIconVo getFooditemIcon(long ilFooditemIconId) {
        return fooditemIconListMapper.getFooditemIcon(ilFooditemIconId);
    }

    /**
     * 식단품목아이콘 추가
     * @param fooditemIconRequestDto
     * @return
     */
    protected int addFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto) {
        return fooditemIconListMapper.addFooditemIcon(fooditemIconRequestDto);
    }

    /**
     * 식단품목아이콘 수정
     * @param fooditemIconRequestDto
     * @return
     */
    protected int putFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto){
        return fooditemIconListMapper.putFooditemIcon(fooditemIconRequestDto);
    }

    /**
     * 식단품목아이콘 삭제
     * @param fooditemIconRequestDto
     * @return
     */
    protected int delFooditemIcon(FooditemIconRequestDto fooditemIconRequestDto){
        return fooditemIconListMapper.delFooditemIcon(fooditemIconRequestDto);
    }
}
