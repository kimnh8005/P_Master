package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class MealScheduleListBizImpl implements MealScheduleListBiz {

    @Autowired
    MealScheduleListService mealScheduleListService;

    /**
     * @Desc 식단 패턴 리스트 조회
     * @param mealPatternRequestDto
     * @return MealPatternListResponseDto
     */
    @Override
    public ApiResult<?> getMealPatternList(MealPatternRequestDto mealPatternRequestDto) {
        MealPatternListResponseDto result = new MealPatternListResponseDto();

        if (!mealPatternRequestDto.getFindKeyword().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(mealPatternRequestDto.getFindKeyword(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            mealPatternRequestDto.setFindKeywordArray(array);
        }

        Page<MealPatternListVo> rows = mealScheduleListService.getMealPatternList(mealPatternRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);
    }

    /**
     * 식단 패턴 삭제
     * @param mealPatternRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> delMealPattern(MealPatternRequestDto mealPatternRequestDto) throws Exception {
    	int result = mealScheduleListService.delMealPattern(mealPatternRequestDto);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }

    /**
	 * 식단 패턴 수정/상세조회 - 연결상품 조회
	 * @param patternCd
	 * @return
	 */
    @Override
    public ApiResult<?> getMealPatternGoodsList(String patternCd) {
        return ApiResult.success(mealScheduleListService.getMealPatternGoodsList(patternCd));
    }

    /**
	 * 식단 패턴 수정/상세조회 - 패턴정보 조회
	 * @param patternCd
	 * @return
	 */
    @Override
    public ApiResult<?> getMealPatternDetailList(String patternCd) {
        return ApiResult.success(mealScheduleListService.getMealPatternDetailList(patternCd));
    }
    
    /**
	 * 식단 패턴 수정/상세조회 - 기본정보 조회
	 * @param patternCd
	 * @return
	 */
    @Override
    public ApiResult<?> getMealPatternInfo(String patternCd) {
        return ApiResult.success(mealScheduleListService.getMealPatternInfo(patternCd));
    }
    
    /**
     * 식단 패턴 수정/상세조회 - 기본정보 수정
     * @param mealPatternRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> putMealPatternInfo(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        mealPatternRequestDto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
    	int result = mealScheduleListService.putMealPatternInfo(mealPatternRequestDto);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }
    
    /**
     * 식단 패턴 수정/상세조회 - 연결상품 추가확인
     * @param ilGoodsId
     * @return
     */
    @Override
    public ApiResult<?> checkMealPatternGoods(String mallDiv, long ilGoodsId) throws Exception {
         return ApiResult.success(mealScheduleListService.checkMealPatternGoods(mallDiv, ilGoodsId));
    }

    /**
     * 식단정보 패턴 엑셀 다운로드
     */
    @Override
    public ExcelDownloadDto getMealPatternExportExcel(MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception {

        //{다운로드정보}_{식단유형}_{패턴코드}_{패턴명}_{다운로드날짜} 예) 패턴_베이비밀_B10001_이유식중기.xml
        String excelFileName = mealInfoExcelRequestDto.getDownloadType() + "_" + mealInfoExcelRequestDto.getMallDivNm() + "_" + mealInfoExcelRequestDto.getPatternCd() + "_" +
                               mealInfoExcelRequestDto.getPatternNmExcel();// 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
         *
         */

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 300, 400, 600, 300, 600, 200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center","center", "center","center","center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "patternNo", "setNo", "setCd", "setNm", "mealContsCd", "mealNm", "allergyYn"};

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         *
         */
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "패턴순번", "세트순번", "세트코드", "세트명", "식단품목코드", "식단품목명", "알러지식단"
        };

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         *
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        List<MealPatternDetailListVo> voList = null;
        try
        {
            voList = mealScheduleListService.getMealPatternExportExcel(mealInfoExcelRequestDto.getPatternCd());
        }
        catch (Exception e)
        {
            throw e; // 추후 CustomException 으로 변환 예정
        }
        firstWorkSheetDto.setExcelDataList(voList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    /**
     * 식단정보 패턴 엑셀 다운로드
     */
    @Override
    public ExcelDownloadDto getMealScheduleExportExcel(MealInfoExcelRequestDto mealInfoExcelRequestDto) throws Exception {

        //{다운로드정보}_{식단유형}_{패턴코드}_{패턴명}_{다운로드 스케쥴기간} 예) 패턴_잇슬림_S10001_300샐러드_20210801_20211031.xml
        String excelFileName = mealInfoExcelRequestDto.getDownloadType() + "_" + mealInfoExcelRequestDto.getMallDivNm() + "_" + mealInfoExcelRequestDto.getPatternCd() + "_" +
                               mealInfoExcelRequestDto.getPatternNmExcel() + "_" + mealInfoExcelRequestDto.getDownloadDateStart() + "_" +
                               mealInfoExcelRequestDto.getDownloadDateEnd(); // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
         *
         */

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 400, 400, 200, 300, 600, 200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center","center", "center","center","center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "patternCd", "patternNm", "deliveryDateExcelStr", "deliveryWeekCode", "mealContsCd", "mealNm", "allergyYn"};

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         *
         */
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "패턴코드", "패턴명", "날짜", "요일", "식단품목코드", "식단품목명", "알러지식단"
        };

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         *
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        List<MealSchedulelDetailListVo> voList = null;
        try
        {
            voList = mealScheduleListService.getMealScheduleExportExcel(mealInfoExcelRequestDto);
        }
        catch (Exception e)
        {
            throw e; // 추후 CustomException 으로 변환 예정
        }
        firstWorkSheetDto.setExcelDataList(voList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

	/**
     * 새로운 식단 패턴 상세저장 (패턴저장 -> 스케쥴생성)
     * @param mealPatternRequestDto
     * @return
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> addMealPatternDetail(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        mealPatternRequestDto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        int result = mealScheduleListService.addMealPatternDetail(mealPatternRequestDto);

    	if(result > 0) {

    	    MealPatternDetailListResponseDto patternDetlResDto = mealScheduleListService.getMealPatternDetailList(mealPatternRequestDto.getPatternCd());
    	    List<MealPatternDetailListVo> patternDetlList = patternDetlResDto.getRows();

            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date insertStartDt = dateFormat.parse(mealPatternRequestDto.getPatternStartDt());
            Date insertEndDt = dateFormat.parse(mealPatternRequestDto.getPatternEndDt());

            LocalDate startDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(insertStartDt) );
            LocalDate endDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(insertEndDt) );

            String mallDiv = mealPatternRequestDto.getMallDiv();

            //식단패턴 스케쥴생성
            List<MealSchedulelListVo> mealSchedulelList = mealScheduleListService.makeMealScheduleVoList(mallDiv, startDate, endDate, patternDetlList);

            //식단스케쥴 저장
            if(mealSchedulelList != null) {
                mealPatternRequestDto.setMealSchedulelList(mealSchedulelList);
                mealScheduleListService.addMealSchedule(mealPatternRequestDto);
            }
    	    return ApiResult.success();
    	} else {
    	    return ApiResult.fail();
        }
    }

    /**
     * 식단패턴 / 연결상품 등록
     * @param mealPatternRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> addMealPatternInfo(MealPatternRequestDto mealPatternRequestDto) throws Exception {
        //패턴코드 생성
        String newPatternCode = mealScheduleListService.getNewPatternCode(mealPatternRequestDto.getMallDiv());
        mealPatternRequestDto.setPatternCd(newPatternCode);

        mealPatternRequestDto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        int result = mealScheduleListService.addMealPatternInfo(mealPatternRequestDto);

        //식단패턴 상세정보 -> 스케쥴생성
		if(result > 0) {
		     addMealPatternDetail(mealPatternRequestDto);
		     return ApiResult.success(mealPatternRequestDto.getPatternCd());
        } else {
		    return ApiResult.fail();
        }
    }

    /**
	 * 식단 스케쥴 상세조회
	 * @param mealScheduleRequestDto
	 * @return
	 */
    @Override
    public ApiResult<?> getMealScheduleDetailList(MealScheduleRequestDto mealScheduleRequestDto) {

        MealScheduleDetailListResponseDto result = new MealScheduleDetailListResponseDto();

        Page<MealSchedulelDetailListVo> rows = mealScheduleListService.getMealScheduleDetailList(mealScheduleRequestDto);

        result.setTotal(rows.getTotal());
        result.setRows(rows.getResult());

        return ApiResult.success(result);
    }

    /**
     * 식단 패턴 수정 (패턴저장 -> 스케쥴생성)
     * @param mealPatternRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> putMealPatternDetail(MealPatternRequestDto mealPatternRequestDto) throws Exception {

        // 업데이트한 패턴 IL_GOODS_DAILY_MEAL_PATTERN_TEMP (임시) 저장
        mealPatternRequestDto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        mealPatternRequestDto.setModifyId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        int result = mealScheduleListService.addMealPatternDetailTemporay(mealPatternRequestDto);

    	if(result > 0) {
            MealPatternListVo mealPatternListVo = mealScheduleListService.getMealPatternInfo(mealPatternRequestDto.getPatternCd());

            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");

            Date nowStartDt = dateFormat.parse(mealPatternListVo.getPatternStartDt());
            Date nowEndDt = dateFormat.parse(mealPatternListVo.getPatternEndDt());
            Date updateStartDt = dateFormat.parse(mealPatternRequestDto.getPatternStartDt());
            Date updateEndDt = dateFormat.parse(mealPatternRequestDto.getPatternEndDt());

            //패턴기간 시작일 이전 OR 종료일 이후의 스케쥴 IL_GOODS_DAILY_MEAL_SCH_TEMP (임시) 저장
            if(nowStartDt.before(updateStartDt) || nowEndDt.after(updateEndDt)) {
               mealScheduleListService.addMealExistScheduleTemporay(mealPatternRequestDto);
            }

            //패턴상세 저장 (기존 패턴상세 삭제 -> 새로운 패턴상세 등록)
            mealScheduleListService.addMealPatternForUpdate(mealPatternRequestDto);

            //새로 등록한 패턴상세 정보 조회
            List<MealPatternDetailListVo> putPatternDetlList = mealScheduleListService.getMealPatternDetailList(mealPatternRequestDto.getPatternCd()).getRows();

            //식단패턴 스케쥴생성
            LocalDate startDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(updateStartDt) );
            LocalDate endDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(updateEndDt) );

            String mallDiv = mealPatternRequestDto.getMallDiv();

            List<MealSchedulelListVo> mealSchedulelList = mealScheduleListService.makeMealScheduleVoList(mallDiv, startDate, endDate, putPatternDetlList);

            //식단스케쥴 저장 (기존 스케쥴 삭제 -> 새로운 스케쥴 등록)
            if(mealSchedulelList != null) {
                mealPatternRequestDto.setMealSchedulelList(mealSchedulelList);
                mealScheduleListService.addMealScheduleTemporay(mealPatternRequestDto);
            }

    	    return ApiResult.success();
    	} else {
    	    return ApiResult.fail();
        }
    }

    /**
     * 식단 패턴 개별수정 (패턴저장 -> 스케쥴생성)
     * @param mealPatternRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> putMealPatternDetailRow(MealPatternRequestDto mealPatternRequestDto) throws Exception {

        // 업데이트한 패턴 IL_GOODS_DAILY_MEAL_PATTERN_TEMP (임시) 저장
        mealPatternRequestDto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        mealPatternRequestDto.setModifyId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        int result = mealScheduleListService.addMealPatternDetailTemporay(mealPatternRequestDto);

    	if(result > 0) {
            MealPatternListVo mealPatternListVo = mealScheduleListService.getMealPatternInfo(mealPatternRequestDto.getPatternCd());

            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");

            Date nowStartDt = dateFormat.parse(mealPatternListVo.getPatternStartDt());
            Date nowEndDt = dateFormat.parse(mealPatternListVo.getPatternEndDt());

            //패턴상세 저장 (기존 패턴상세 삭제 -> 새로운 패턴상세 등록)
            mealScheduleListService.addMealPatternForUpdate(mealPatternRequestDto);

            //새로 등록한 패턴상세 정보 조회
            List<MealPatternDetailListVo> putPatternDetlList = mealScheduleListService.getMealPatternDetailList(mealPatternRequestDto.getPatternCd()).getRows();

            //식단패턴 스케쥴생성
            LocalDate startDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(nowStartDt) );
            LocalDate endDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(nowEndDt) );

            String mallDiv = mealPatternRequestDto.getMallDiv();

            List<MealSchedulelListVo> mealSchedulelList = mealScheduleListService.makeMealScheduleVoList(mallDiv, startDate, endDate, putPatternDetlList);

            //식단스케쥴 저장 (기존 스케쥴 삭제 -> 새로운 스케쥴 등록)
            if(mealSchedulelList != null) {
                mealPatternRequestDto.setMealSchedulelList(mealSchedulelList);
                mealScheduleListService.addMealScheduleTemporay(mealPatternRequestDto);
            }

    	    return ApiResult.success();
    	} else {
    	    return ApiResult.fail();
        }
    }
    
    /**
     * 식단 스케쥴 개별수정
     * @param mealScheduleRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> putMealSchRow(MealScheduleRequestDto mealScheduleRequestDto) throws Exception {

        // 업데이트한 패턴 IL_GOODS_DAILY_MEAL_PATTERN_TEMP (임시) 저장
        mealScheduleRequestDto.setCreateId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));
        mealScheduleRequestDto.setModifyId(Long.parseLong(SessionUtil.getBosUserVO().getUserId()));

        // 일괄변경 X
        if(mealScheduleRequestDto.getBulkUpdateYn().equals("N")) {
          mealScheduleListService.putMealSchedule(mealScheduleRequestDto);

        // 일괄변경 O
        } else if(mealScheduleRequestDto.getBulkUpdateYn().equals("Y")){
            mealScheduleListService.putMealScheduleBulk(mealScheduleRequestDto);
        }

        return ApiResult.success();

    }

    /**
     * 식단 패턴 업로드 시 식단품목명, 알러지식단 조회
     * @param mealContsCd
     * @return
     */
    @Override
    public ApiResult<?> getMealPatternUploadData(String mealContsCd) throws Exception {
         return ApiResult.success(mealScheduleListService.getMealPatternUploadData(mealContsCd));
    }

    /**
     * 식단 패턴 업로드 시 식단품목명, 알러지식단 조회 리스트
     * @param mealContsCdArray
     * @return
     */
    @Override
    public ApiResult<?> getMealPatternUploadDataList(List<String> mealContsCdArray) throws Exception {
         return ApiResult.success(mealScheduleListService.getMealPatternUploadDataList(mealContsCdArray));
    }

}
