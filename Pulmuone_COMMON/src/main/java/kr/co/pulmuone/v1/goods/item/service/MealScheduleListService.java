package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.item.MealScheduleListMapper;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealScheduleListService {

    @Autowired
    private final MealScheduleListMapper mealScheduleListMapper;

     /**
     * @Desc  식단 패턴 리스트 조회
     * @param mealPatternRequestDto
     * @return MealPatternListResponseDto
     */
	 protected Page<MealPatternListVo> getMealPatternList(MealPatternRequestDto mealPatternRequestDto) {
	 	PageMethod.startPage(mealPatternRequestDto.getPage(), mealPatternRequestDto.getPageSize());
	 	return mealScheduleListMapper.getMealPatternList(mealPatternRequestDto);
	 }

	 /**
     * 식단 패턴 삭제
     * @param mealPatternRequestDto
     * @return
     */
    protected int delMealPattern(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        int result = mealScheduleListMapper.delMealPattern(mealPatternRequestDto.getPatternCd());
        int patternDetl = mealScheduleListMapper.getMealPatternDetailCount(mealPatternRequestDto.getPatternCd());
           
        //식단 연결상품이 있을 경우 > 식단연결상품 삭제
        if(mealPatternRequestDto.getIlGoodsIdInfo() != null) {
            mealScheduleListMapper.delMealPatternGoods(mealPatternRequestDto.getPatternCd());
        }
        
        //식단 패턴 상세정보가 있을 경우 > 식단패턴상세, 식단스케줄 삭제
        if (patternDetl > 0) {
            mealScheduleListMapper.delMealPatternDetail(mealPatternRequestDto.getPatternCd());
            mealScheduleListMapper.delMealSchedule(mealPatternRequestDto.getPatternCd());
        }

        return result;
    }

    /**
     * @Desc  식단 패턴 수정/상세조회 - 연결상품 조회
     * @param patternCd
     * @return MealPatternGoodsListResponseDto
     */
	protected MealPatternGoodsListResponseDto getMealPatternGoodsList(String patternCd) {
		MealPatternGoodsListResponseDto mealPatternGoodsListResponseDto = new MealPatternGoodsListResponseDto();
		List<MealPatternGoodsListVo> mealPatternGoodsListVo = mealScheduleListMapper.getMealPatternGoodsList(patternCd);

		mealPatternGoodsListResponseDto.setRows(mealPatternGoodsListVo);

		return mealPatternGoodsListResponseDto;
	}

	/**
     * @Desc  식단 패턴 수정/상세조회 - 패턴정보 조회
     * @param patternCd
     * @return MealPatternGoodsListResponseDto
     */
	protected MealPatternDetailListResponseDto getMealPatternDetailList(String patternCd) {
		MealPatternDetailListResponseDto mealPatternDetailListResponseDto = new MealPatternDetailListResponseDto();
		List<MealPatternDetailListVo> mealPatternDetailListVo = mealScheduleListMapper.getMealPatternDetailList(patternCd);

		mealPatternDetailListResponseDto.setRows(mealPatternDetailListVo);
		if(CollectionUtils.isNotEmpty(mealPatternDetailListVo)){
            mealPatternDetailListResponseDto.setTotal(mealPatternDetailListVo.size());
        }
		return mealPatternDetailListResponseDto;
	}
	
	/**
     * @Desc  식단 패턴 수정/상세조회 - 기본정보 조회
     * @param patternCd
     * @return MealPatternGoodsListResponseDto
     */
	protected MealPatternListVo getMealPatternInfo(String patternCd) {
		MealPatternListResponseDto mealPatternListResponseDto = new MealPatternListResponseDto();
		MealPatternListVo mealPatternListVo = mealScheduleListMapper.getMealPatternInfo(patternCd);

		return mealPatternListVo;
	}

    /**
     * @Desc  식단 패턴 수정/상세조회 - 기본정보 수정
     * @param mealPatternRequestDto
     */
    protected int putMealPatternInfo(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        int result = mealScheduleListMapper.putMealPatternInfo(mealPatternRequestDto);
        List<MealPatternGoodsListVo> patternGoodsList = mealPatternRequestDto.getPatternGoodsList();

        if(result > 0) {
        	//식단패턴 연결상품 삭제 후 재등록
        	mealScheduleListMapper.delMealPatternGoods(mealPatternRequestDto.getPatternCd());
        	if(patternGoodsList != null && patternGoodsList.size() > 0) {
					mealScheduleListMapper.addMealPatternGoods(patternGoodsList, mealPatternRequestDto.getPatternCd());
				}
		}

        return result;
    }

    /**
     * @Desc  식단 패턴 수정/상세조회 - 연결상품 추가확인
     * @param ilGoodsId
     * @return
     */
    protected ApiResult<?> checkMealPatternGoods(String mallDiv, long ilGoodsId) throws Exception {
        MealPatternGoodsListVo mealPatternGoodsListVo = mealScheduleListMapper.checkMealPatternGoods(mallDiv, ilGoodsId);

        if(mealPatternGoodsListVo != null) {
            // 식단연결 불가 상품일 경우
            if(mealPatternGoodsListVo.getMallDiv() == null) {
                return ApiResult.result(ApiEnums.checkMealPatternGoodsAPIResponseCode.NOT_REGISTRATION);

            } else {
                mealPatternGoodsListVo = mealScheduleListMapper.getMealPatternGoodsData(ilGoodsId);
                return ApiResult.success(mealPatternGoodsListVo);
            }
        } else {
            // 존재하지 않는 상품일 경우
            return ApiResult.result(ApiEnums.checkMealPatternGoodsAPIResponseCode.ALREADY_OR_NOT_EXIST);
        }

    }

   /**
     * @Desc 식단정보 패턴 엑셀 다운로드
     * @throws Exception
     * @return ApiResult<?>
     */
	@UserMaskingRun(system = "MUST_MASKING")
	protected List<MealPatternDetailListVo> getMealPatternExportExcel(String patternCd) throws Exception {
		List<MealPatternDetailListVo> result = mealScheduleListMapper.getMealPatternDetailList(patternCd);

		return result;
	}

	/**
     * @Desc 식단정보 패턴 스케쥴 다운로드
     * @throws Exception
     * @return ApiResult<?>
     */
	@UserMaskingRun(system = "MUST_MASKING")
	protected List<MealSchedulelDetailListVo> getMealScheduleExportExcel(MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception {
		List<MealSchedulelDetailListVo> result = mealScheduleListMapper.getMealScheduleDetailExcelList(mealInfoExcelRequestDto);

		return result;
	}

	/**
     * @Desc  식단 패턴 상세저장 (패턴저장 -> 스케쥴생성)
     * @param mealPatternRequestDto
     */
    protected int addMealPatternDetail(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        return mealScheduleListMapper.addMealPatternDetail(mealPatternRequestDto);
    }

    /**
     * @Desc  식단 패턴 스케쥴생성
     */
    protected List<MealSchedulelListVo> makeMealScheduleVoList(String mallDiv, LocalDate startDate, LocalDate endDate, List<MealPatternDetailListVo> patternDetlList) throws Exception {
        List<MealSchedulelListVo> list = new ArrayList<MealSchedulelListVo>();

        // 패턴별로 그룹핑 map
		Map<Integer, List<MealPatternDetailListVo>> groupByMap = patternDetlList.stream()
				.collect(Collectors.groupingBy(MealPatternDetailListVo::getPatternNo));

		Set<Integer> patternNoSet = groupByMap.keySet();
		Iterator<Integer> patternNoList = patternNoSet.iterator();

		// -day 로 시작
        startDate = startDate.minusDays(1);

		while (startDate.isBefore(endDate)) {
            int patternNo = 0;
			startDate = startDate.plusDays(1);

			int dayNum = 0;
            Date startDateForWeek = java.sql.Date.valueOf(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDateForWeek);
            dayNum = cal.get(Calendar.DAY_OF_WEEK);

            // 식단분류가 잇슬림인경우 주 5일 (토,일 제외) 스케쥴 생성
            boolean isMakeMealSch = GoodsEnums.MallDiv.EATSLIM.getCode().equals(mallDiv) && (dayNum == 7|| dayNum == 1);

			if(!isMakeMealSch){
                patternNo = patternNoList.next();
			    List<MealPatternDetailListVo> targetPatternDetlVoList = groupByMap.get(patternNo);

                for (MealPatternDetailListVo patternDetlVo : targetPatternDetlVoList) {
				list.add(new MealSchedulelListVo(patternDetlVo, startDate));
			    }
            }

			// 포인터 초기화
			if (!patternNoList.hasNext()) {
				patternNoList = patternNoSet.iterator();
			}
		}

		return list;
    }

    /**
     * @Desc  식단 패턴 스케쥴저장
     */
    protected int addMealSchedule(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        return mealScheduleListMapper.addMealSchedule(mealPatternRequestDto);
    }

    /**
     * @Desc  식단 패턴 등록
     */
    protected int addMealPatternInfo(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        //패턴 기본정보 등록
        int result = mealScheduleListMapper.addMealPatternInfo(mealPatternRequestDto);
        List<MealPatternGoodsListVo> patternGoodsList = mealPatternRequestDto.getPatternGoodsList();

        if(result > 0) {
        	//식단패턴 연결상품 등록
            if(patternGoodsList != null && patternGoodsList.size() > 0) {
				mealScheduleListMapper.addMealPatternGoods(patternGoodsList, mealPatternRequestDto.getPatternCd());
			}
		}

        return result;
    }

    /**
     * @Desc  식단 패턴코드 생성
     */
    protected String getNewPatternCode(String mallDiv) throws Exception {
        return mealScheduleListMapper.getNewPatternCode(mallDiv);
    }

    /**
     * @Desc  식단 스케쥴 상세조회
     * @param mealScheduleRequestDto
     * @return MealPatternGoodsListResponseDto
     */
	protected Page<MealSchedulelDetailListVo> getMealScheduleDetailList(MealScheduleRequestDto mealScheduleRequestDto) {

	 	PageMethod.startPage(mealScheduleRequestDto.getPage(), mealScheduleRequestDto.getPageSize());
	 	return mealScheduleListMapper.getMealScheduleDetailList(mealScheduleRequestDto);
	}

	/**
     * @Desc  수정 시 식단 패턴 (임시저장)
     * @param mealPatternRequestDto
     */
    protected int addMealPatternDetailTemporay(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        int patternTmpAdd = mealScheduleListMapper.addMealPatternDetailTemporay(mealPatternRequestDto);


        // 패턴별 상세 수정 (임시)
        if(mealPatternRequestDto.getUpdatePatternDetlIdList() != null) {
            mealScheduleListMapper.putMealPatternDetailTemporay(mealPatternRequestDto);
        }

        return patternTmpAdd;
    }

     /**
     * @Desc  수정 시 식단 패턴 스케쥴 (임시저장)
     */
    protected int addMealScheduleTemporay(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        int schTmpAdd = mealScheduleListMapper.addMealScheduleTemporay(mealPatternRequestDto);

        if(schTmpAdd > 0) {
            //기존의 스케쥴 삭제
            mealScheduleListMapper.delMealSchedule(mealPatternRequestDto.getPatternCd());
            //새로운 스케쥴 등록 (업데이트)
            mealScheduleListMapper.addMealScheduleForUpdate(mealPatternRequestDto.getPatternCd());
            //임시의 스케쥴 삭제
            mealScheduleListMapper.delMealScheduleTemporary(mealPatternRequestDto.getPatternCd());

        }
        return schTmpAdd;
    }

    /**
     * @Desc  패턴기간 시작일 이전 OR 종료일 이후의 스케쥴 임시저장
     * @param mealPatternRequestDto
     */
    protected int addMealExistScheduleTemporay(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        return mealScheduleListMapper.addMealExistScheduleTemporay(mealPatternRequestDto);
    }

    /**
     * @Desc  새로운 패턴 저장 (업데이트)
     * @param mealPatternRequestDto
     */
    protected int addMealPatternForUpdate(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        //식단패턴기본정보 수정
        mealScheduleListMapper.putMealPatternInfo(mealPatternRequestDto);
        //식단패턴상세 삭제
        int patternDel = mealScheduleListMapper.delMealPatternDetail(mealPatternRequestDto.getPatternCd());

        if(patternDel > 0) {
            //새로운 패턴 등록 (업데이트)
            mealScheduleListMapper.addMealPatternForUpdate(mealPatternRequestDto.getPatternCd());

            //업데이트 후 삭제 (임시테이블)
            mealScheduleListMapper.delMealPatternDetailTemporary(mealPatternRequestDto.getPatternCd());
        }
        return patternDel;
    }

     /**
     * @Desc  식단 스케쥴 단일변경
     */
    protected int putMealSchedule(MealScheduleRequestDto mealScheduleRequestDto) throws Exception {
        return mealScheduleListMapper.putMealSchedule(mealScheduleRequestDto);
    }
    
     /**
     * @Desc  식단 스케쥴 일괄변경
     */
    protected int putMealScheduleBulk(MealScheduleRequestDto mealScheduleRequestDto) throws Exception {
        MealPatternRequestDto mealPatternRequestDto = new MealPatternRequestDto();
        mealPatternRequestDto.setPatternCd(mealScheduleRequestDto.getPatternCd());
        mealPatternRequestDto.setMealContsCd(mealScheduleRequestDto.getMealContsCd());
        mealPatternRequestDto.setBulkUpdateYn(mealScheduleRequestDto.getBulkUpdateYn());
        mealPatternRequestDto.setOriginMealContsCd(mealScheduleRequestDto.getOriginMealContsCd());

        int schTmpAdd = mealScheduleListMapper.addMealExistScheduleTemporay(mealPatternRequestDto);

        if(schTmpAdd > 0) {
            //동일 식단품목코드 임시 스케쥴에서 일괄변경
            mealScheduleListMapper.putMealScheduleBulk(mealScheduleRequestDto);
            //기존의 스케쥴 삭제
            mealScheduleListMapper.delMealSchedule(mealPatternRequestDto.getPatternCd());
            //새로운 스케쥴 등록 (업데이트)
            mealScheduleListMapper.addMealScheduleForUpdate(mealPatternRequestDto.getPatternCd());
            //임시의 스케쥴 삭제
            mealScheduleListMapper.delMealScheduleTemporary(mealPatternRequestDto.getPatternCd());

        }
        return schTmpAdd;
    }

    /**
     * @Desc  식단 패턴 업로드 시 식단품목명, 알러지식단 조회
     * @param mealContsCd
     * @return MealPatternListVo
     */
	protected MealSchedulelDetailListVo getMealPatternUploadData(String mealContsCd) throws Exception{
		return mealScheduleListMapper.getMealPatternUploadData(mealContsCd);
	}

	/**
     * @Desc  식단 패턴 업로드 시 식단품목명, 알러지식단 리스트 조회
     * @param mealContsCdArray
     * @return MealPatternListVo
     */
	protected List<MealSchedulelDetailListVo> getMealPatternUploadDataList(List<String> mealContsCdArray) throws Exception{
		return mealScheduleListMapper.getMealPatternUploadDataList(mealContsCdArray);
	}


}