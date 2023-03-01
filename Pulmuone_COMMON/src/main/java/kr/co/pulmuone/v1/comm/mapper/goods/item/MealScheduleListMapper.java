package kr.co.pulmuone.v1.comm.mapper.goods.item;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.item.dto.MealInfoExcelRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealPatternRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealScheduleRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternGoodsListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelDetailListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MealScheduleListMapper {

    /**
     * @Desc 식단 패턴 리스트 조회
     */
    Page<MealPatternListVo> getMealPatternList(MealPatternRequestDto mealPatternRequestDto);

    /**
     * @Desc 식단 패턴삭제
     */
    int delMealPattern(String patternCd);

    /**
     * @Desc 식단 패턴 연결상품 삭제
     */
    int delMealPatternGoods(String patternCd);

    /**
     * @Desc 식단 패턴 상세정보 삭제
     */
    int delMealPatternDetail(String patternCd);

    /**
     * @Desc 식단 패턴 > 스케줄 삭제
     */
    int delMealSchedule(String patternCd);

    /**
     * @Desc 식단 패턴 상세정보 수
     */
    int getMealPatternDetailCount(String patternCd);

    /**
     * @Desc 식단 패턴 수정/상세조회 - 연결상품 조회
     */
    List<MealPatternGoodsListVo> getMealPatternGoodsList(String patternCd);

    /**
     * @Desc 식단 패턴 수정/상세조회 - 패턴정보 조회
     */
    List<MealPatternDetailListVo> getMealPatternDetailList(String patternCd);

    /**
     * @Desc 식단 패턴 수정/상세조회 - 기본정보 조회
     */
    MealPatternListVo getMealPatternInfo(String patternCd);

    /**
     * @Desc 식단 패턴 수정/상세조회 - 기본정보 수정
     */
    int putMealPatternInfo(MealPatternRequestDto mealPatternRequestDto);
    
    /**
     * @Desc 식단 패턴 연결상품 등록
     */
    int addMealPatternGoods(@Param("patternGoodsList") List<MealPatternGoodsListVo> patternGoodsList, @Param("patternCd") String patternCd);

    /**
     * @Desc 식단 패턴 수정/상세조회 - 연결상품 추가확인
     */
    MealPatternGoodsListVo checkMealPatternGoods(@Param("mallDiv") String mallDiv, @Param("ilGoodsId") long ilGoodsId);

    /**
     * @Desc 식단 패턴 수정/상세조회 - 연결상품 추가 데이터 GET
     */
    MealPatternGoodsListVo getMealPatternGoodsData(long ilGoodsId);

    /**
     * @Desc 식단 패턴 상세저장
     */
    int addMealPatternDetail(MealPatternRequestDto mealPatternRequestDto);

    /**
     * @Desc 식단 스케쥴 저장
     */
    int addMealSchedule(MealPatternRequestDto mealPatternRequestDto);
    
    /**
     * @Desc 식단 패턴 등록
     */
    int addMealPatternInfo(MealPatternRequestDto mealPatternRequestDto);

    /**
     * @Desc 패턴코드 생성 (베이비밀 : BM1000* / 잇슬림 : IS2000*)
     */
    String getNewPatternCode(String mallDiv);

    /**
     * @Desc 식단 스케쥴 상세조회
     */
    Page<MealSchedulelDetailListVo> getMealScheduleDetailList(MealScheduleRequestDto mealScheduleRequestDto);
    
    /**
     * @Desc 수정 시 식단 패턴 (임시저장)
     */
    int addMealPatternDetailTemporay(MealPatternRequestDto mealPatternRequestDto);

    /**
     * @Desc 식단 패턴 별 상세 수정
     */
    int putMealPatternDetailTemporay(MealPatternRequestDto mealPatternRequestDto);

     /**
     * @Desc 수정 시 식단 스케쥴 (임시저장)
     */
    int addMealScheduleTemporay(MealPatternRequestDto mealPatternRequestDto);

     /**
     * @Desc 패턴기간 시작일 이전 OR 종료일 이후의 스케쥴 임시저장
     */
    int addMealExistScheduleTemporay(MealPatternRequestDto mealPatternRequestDto);

    /**
     * @Desc 새로운 패턴 저장 (업데이트)
     */
    int addMealPatternForUpdate(String patternCd);

    /**
     * @Desc 새로운 스케쥴 저장 (업데이트)
     */
    int addMealScheduleForUpdate(String patternCd);

    /**
     * @Desc 식단 패턴 (임시) 삭제
     */
    int delMealPatternDetailTemporary(String patternCd);
    
    /**
     * @Desc 식단 스케쥴 (임시) 삭제
     */
    int delMealScheduleTemporary(String patternCd);

    /**
     * @Desc 식단 스케쥴 단일변경
     */
    int putMealSchedule(MealScheduleRequestDto mealScheduleRequestDto);
    
    /**
     * @Desc 식단 스케쥴 일괄변경
     */
    int putMealScheduleBulk(MealScheduleRequestDto mealScheduleRequestDto);

    /**
     * @Desc 식단 스케쥴 상세조회 (엑셀)
     */
    List<MealSchedulelDetailListVo> getMealScheduleDetailExcelList(MealInfoExcelRequestDto mealInfoExcelRequestDto);

    /**
     * @Desc 식단 패턴 업로드 시 식단품목명, 알러지식단 조회
     */
    MealSchedulelDetailListVo getMealPatternUploadData(String mealContsCd);

    /**
     * @Desc 식단 패턴 업로드 시 식단품목명, 알러지식단 조회 리스트
     */
    List<MealSchedulelDetailListVo> getMealPatternUploadDataList(List<String> mealContsCdArray);
}
