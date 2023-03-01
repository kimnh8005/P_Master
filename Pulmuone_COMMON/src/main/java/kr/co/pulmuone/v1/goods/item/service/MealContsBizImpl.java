package kr.co.pulmuone.v1.goods.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import kr.co.pulmuone.v1.goods.item.dto.MealContsDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealContsVo;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderResponseDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
public class MealContsBizImpl implements MealContsBiz{

    @Autowired
    MealContsService mealContsService;

    private int addRowNum = 3;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;


    /**
     * @Desc
     * @param
     * @return
     */
    @Override
    public ApiResult<?> getMealContsList(MealContsListRequestDto mealContsListRequestDto) {

        if (!mealContsListRequestDto.getSearchIlGoodsDailyMealContsCd().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(mealContsListRequestDto.getSearchIlGoodsDailyMealContsCd(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            mealContsListRequestDto.setIlGoodsDailyMealContsCdList(array);
        }

        long totalCnt = mealContsService.getMealContsListCount(mealContsListRequestDto);

        List<MealContsVo> mealContsList = mealContsService.getMealContsList(mealContsListRequestDto);

        return ApiResult.success(
                MealContsListResponseDto.builder()
                        .rows(mealContsList)
                        .total(totalCnt)
                        .build()
        );

    }


    /**
     * 식단컨텐츠 리스트 엑셀  다운로드
     */
    @Override
    public ExcelDownloadDto getExportExcelMealContsList(MealContsListRequestDto mealContsListRequestDto) throws Exception {


        String excelFileName   = "식단컨텐츠" + "_" + DateUtil.getCurrentDate();
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름


        /*
         * 컬럼별 width 목록 : 단위 pixel
         */
        Integer[] widthListOfFirstWorksheet =  {200, 300, 300, 250, 250, 250};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         */
        String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {"mallDivNm", "ilGoodsDailyMealContsCd", "ilGoodsDailyMealNm", "allergyYn", "createDt", "modifyDt"};

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         */
        String[] firstHeaderListOfFirstWorksheet = {"식단분류", "식단품목 코드", "식단명", "알러지 식단여부", "등록일", "수정일"};

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        if(mealContsListRequestDto.getSearchIlGoodsDailyMealContsCd() != null && !mealContsListRequestDto.getSearchIlGoodsDailyMealContsCd().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(mealContsListRequestDto.getSearchIlGoodsDailyMealContsCd(), "\n|,");
            while(st.hasMoreElements()) {
                String object = (String)st.nextElement();
                array.add(object);
            }
            mealContsListRequestDto.setIlGoodsDailyMealContsCdList(array);
        }

        List<MealContsVo> notIssueList = null;
        try
        {
            notIssueList = mealContsService.getMealContsList(mealContsListRequestDto);
        }
        catch (Exception e)
        {
            throw e; // 추후 CustomException 으로 변환 예정
        }
        firstWorkSheetDto.setExcelDataList(notIssueList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    /**
     * 식단 컨텐츠 일괄 업로드
     */
    @Override
    public ApiResult<?> mealContsExcelUpload(MultipartFile file, String createId) throws Exception {
        if (ExcelUploadUtil.isFile(file) != true)
            return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

        // Return 값 설정
        OutMallOrderResponseDto responseDto = new OutMallOrderResponseDto();

        // Excel Import 정보 -> Dto 변환
        Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
        if (uploadSheet == null) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

        // Excel 데이터 Mapping
        List<MealContsDto> excelList = setExcelData(uploadSheet);

        if(CollectionUtils.isEmpty(excelList)) {
            return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
        }

        // 원본 데이터 JSON 변환
        List<MealContsDto> resultList = new ArrayList<>();
        for(MealContsDto x : excelList){
            resultList.add(OBJECT_MAPPER.convertValue(x, MealContsDto.class));
        }

        // 항목별 검증 진행
        excelList = getMealContsExcelUploadValidator(excelList);

        //업로드 현황 설정
        List<MealContsDto> tmpSuccessVoList = new ArrayList<>();
        List<MealContsDto> failVoList = new ArrayList<>();
        for (MealContsDto dto : excelList) {
            if (dto.isSuccess()) {
                tmpSuccessVoList.add(dto);
            }else{
                failVoList.add(dto);
            }
        }

        if (CollectionUtils.isNotEmpty(failVoList)) {
            List<String> failMessageList = new ArrayList<>();
            failVoList.stream()
                    .forEach(item -> {
                        String[] arr = item.getFailMessage().split(Constants.ARRAY_SEPARATORS);
                        for(String str:arr){
                            if (!"".equals(str.trim())) {
                                failMessageList.add(str);
                            }
                        }
                    });

            responseDto.setFailMessage(
                    failMessageList.stream()
                            .distinct()
                            .collect(Collectors.joining("<br/>"))
            );
            responseDto.setFailCount(failVoList.size());
        }

        if(CollectionUtils.isNotEmpty(tmpSuccessVoList)){
            for(MealContsDto successDto : tmpSuccessVoList){
                successDto.setCreateId(createId);
                mealContsService.addMealConts(successDto);
            }
        }

         return ApiResult.success(responseDto);
    }

    /**
     * @Desc 식단 컨텐츠 등록
     * @param mealContsDto
     * @return
     */
    @Override
    public ApiResult<?> addMealConts(MealContsDto mealContsDto) {
        
        // 식단품목코드 중복체크
        int overlapMealContsCdCnt = mealContsService.isOverlapMealContsCd(mealContsDto.getIlGoodsDailyMealContsCd());
        if(overlapMealContsCdCnt > 0){
            return ApiResult.result(GoodsEnums.MealContsCode.OVERLAP_MEAL_CONTS_CD);
        }else{
            return ApiResult.success(mealContsService.addMealConts(mealContsDto));
        }
    }

    /**
     * @Desc 식단 컨텐츠 단건 조회
     * @param ilGoodsDailyMealContsCd
     * @return
     */
    @Override
    public ApiResult<?> getMealConts(String ilGoodsDailyMealContsCd) {
        return ApiResult.success(mealContsService.getMealConts(ilGoodsDailyMealContsCd));
    }

    /**
     * @Desc 식단 컨텐츠 수정
     * @param mealContsDto
     * @return
     */
    @Override
    public ApiResult<?> putMealConts(MealContsDto mealContsDto) {
        return ApiResult.success(mealContsService.putMealConts(mealContsDto));
    }

    /**
     * @Desc 식단 컨텐츠 삭제
     * @param ilGoodsDailyMealContsCd
     * @return
     */
    @Override
    public ApiResult<?> delMealConts(String ilGoodsDailyMealContsCd) {
        return ApiResult.success(mealContsService.delMealConts(ilGoodsDailyMealContsCd));
    }


    public List<MealContsDto> setExcelData(Sheet sheet) {
        List<MealContsDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if(row != null){
                Iterator cellItr = row.cellIterator();

                while ( cellItr.hasNext() ) {
                    XSSFCell cell = (XSSFCell) cellItr.next();
                }

                // 첫번째, 두번째, 네번째, 다섯번째가 Null이 아닌경우만 데이터 생성
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                        || org.apache.commons.lang3.StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
                        || org.apache.commons.lang3.StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(3)))
                        || org.apache.commons.lang3.StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(4)))
                ) {
                    excelList.add(MealContsDto.builder()
                            .ilGoodsDailyMealContsCd(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealContsCd.getColNum())))
                            .ilGoodsDailyMealNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealNm.getColNum())))
                            .allergyYn(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyYn.getColNum())))
                            .mallDiv(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.mallDiv.getColNum())))
                            .thumbnailImg(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.thumbnailImg.getColNum())))
                            .recommendedAge(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.recommendedAge.getColNum())))
                            .totalCapacity(StringUtil.nvlInt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.totalCapacity.getColNum()))))
                            .calorie(StringUtil.nvlInt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.calorie.getColNum()))))
                            .eatsslimIndex(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.eatsslimIndex.getColNum())))
                            .description(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.description.getColNum())))
                            .allergyEgg(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyEgg.getColNum())))
                            .allergyMilk(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyMilk.getColNum())))
                            .allergyShrimp(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyShrimp.getColNum())))
                            .allergyMackerel(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyMackerel.getColNum())))
                            .allergySquid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergySquid.getColNum())))
                            .allergyCrab(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyCrab.getColNum())))
                            .allergyShellfish(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyShellfish.getColNum())))
                            .allergyPork(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyPork.getColNum())))
                            .allergyBeef(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyBeef.getColNum())))
                            .allergyChicken(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyChicken.getColNum())))
                            .allergyBuckwheat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyBuckwheat.getColNum())))
                            .allergyWheat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyWheat.getColNum())))
                            .allergySoybean(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergySoybean.getColNum())))
                            .allergyPeanut(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyPeanut.getColNum())))
                            .allergyWalnut(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyWalnut.getColNum())))
                            .allergyPinenut(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyPinenut.getColNum())))
                            .allergySulfite(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergySulfite.getColNum())))
                            .allergyPeach(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyPeach.getColNum())))
                            .allergyTomato(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.allergyTomato.getColNum())))
                            .nutritionTotalCarbohydrate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionTotalCarbohydrate.getColNum())))
                            .nutritionTotalCarbohydrateRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionTotalCarbohydrateRate.getColNum())))
                            .nutritionFiber(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionFiber.getColNum())))
                            .nutritionFiberRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionFiberRate.getColNum())))
                            .nutritionSugars(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionSugars.getColNum())))
                            .nutritionSugarsRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionSugarsRate.getColNum())))
                            .nutritionTotalFat(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionTotalFat.getColNum())))
                            .nutritionTotalFatRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionTotalFatRate.getColNum())))
                            .nutritionSaturatedFat(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionSaturatedFat.getColNum())))
                            .nutritionSaturatedFatRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionSaturatedFatRate.getColNum())))
                            .nutritionTransFat(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionTransFat.getColNum())))
                            .nutritionTransFatRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionTransFatRate.getColNum())))
                            .nutritionProtein(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionProtein.getColNum())))
                            .nutritionProteinRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionProteinRate.getColNum())))
                            .nutritionCholesterol(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionCholesterol.getColNum())))
                            .nutritionCholesterolRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionCholesterolRate.getColNum())))
                            .nutritionSodium(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionSodium.getColNum())))
                            .nutritionSodiumRate(ExcelUploadUtil.cellValueForNumericNotFormatting(row.getCell(ExcelUploadEnums.MealContsUploadCols.nutritionSodiumRate.getColNum())))
                            .specGoodsName(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specGoodsName.getColNum())))
                            .specGoodsType(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specGoodsType.getColNum())))
                            .specProducerLocation(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specProducerLocation.getColNum())))
                            .specManufacturingDate(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specManufacturingDate.getColNum())))
                            .specExpirationDate(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specExpirationDate.getColNum())))
                            .specStorageMethod(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specStorageMethod.getColNum())))
                            .specOriginalMaterial(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.MealContsUploadCols.specOriginalMaterial.getColNum())))
                            .build());
                }
            }
        }
        return excelList;
    }

    public List<MealContsDto> getMealContsExcelUploadValidator(List<MealContsDto> excelList) throws Exception {
        for (MealContsDto dto : excelList) {
            MessageCommEnum responseCommEnum = mealContsExcelUploadValidate(dto);

            dto.setSuccess(true);
            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                dto.setSuccess(false);
            }
        }
        return excelList;
    }

    public MessageCommEnum mealContsExcelUploadValidate(MealContsDto dto) {

        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        
        // 필수 값 확인
        if (StringUtil.isEmpty(dto.getIlGoodsDailyMealContsCd())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealContsCd.getCodeName()));
            //returnStatus = BaseEnums.Default.FAIL;
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getIlGoodsDailyMealNm())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealNm.getCodeName()));
            //returnStatus = BaseEnums.Default.FAIL;
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getMallDiv())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.MealContsUploadCols.mallDiv.getCodeName()));
            //returnStatus = BaseEnums.Default.FAIL;
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getThumbnailImg())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.MealContsUploadCols.thumbnailImg.getCodeName()));
            //returnStatus = BaseEnums.Default.FAIL;
            return BaseEnums.Default.FAIL;
        }

        // 바이트 확인 & 데이터타입 확인
        // 식단컨텐츠 코드
        if (StringUtil.getByteLength(dto.getIlGoodsDailyMealContsCd()) > ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealContsCd.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isNumeric(dto.getIlGoodsDailyMealContsCd())) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealContsCd.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        // 중복확인
        int overlapMealContsCdCnt = mealContsService.isOverlapMealContsCd(dto.getIlGoodsDailyMealContsCd());
        if(overlapMealContsCdCnt > 0){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.OVERLAP_MEAL_CONTS_CD.getMessage(), dto.getIlGoodsDailyMealContsCd()));
            return BaseEnums.Default.FAIL;
        }

        // 식단품목명
        if (StringUtil.getByteLength(dto.getIlGoodsDailyMealNm()) > ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.ilGoodsDailyMealNm.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        // 알러지 식단여부
        if(StringUtil.isNotEmpty(dto.getAllergyYn())){
            if(!"Y".equals(dto.getAllergyYn())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyYn.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
        }
        // 식단분류
        if(StringUtils.isNotEmpty(dto.getMallDiv())){
            if("1".equals(dto.getMallDiv())){
                dto.setMallDiv("MALL_DIV.BABYMEAL");
            }else if("2".equals(dto.getMallDiv())){
                dto.setMallDiv("MALL_DIV.EATSLIM");
            }else{
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.mallDiv.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
        }
        // 섬네일이미지url
        if (StringUtil.getByteLength(dto.getThumbnailImg()) > ExcelUploadValidateEnums.ByteLength.BYTE_255.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.thumbnailImg.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_255.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        // 연령
        if(StringUtils.isNotEmpty(dto.getRecommendedAge())){
            if (StringUtil.getByteLength(dto.getRecommendedAge()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.recommendedAge.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
        }
        // 1회 제공량
        if(StringUtil.isNotEmpty(dto.getTotalCapacity())){
            if (StringUtil.getByteLength(String.valueOf(dto.getTotalCapacity())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.totalCapacity.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
        }
        //칼로리정보
        if(StringUtil.isNotEmpty(dto.getCalorie())){
            if (StringUtil.getByteLength(String.valueOf(dto.getCalorie())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.calorie.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
        }
        // 잇슬림지수
        if(StringUtils.isNotEmpty(dto.getEatsslimIndex())){
            if (StringUtil.getByteLength(dto.getEatsslimIndex()) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.eatsslimIndex.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            if(!NumberUtils.isCreatable(dto.getEatsslimIndex())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.eatsslimIndex.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
        }
        // 상품설명
        if(StringUtils.isNotEmpty(dto.getDescription())){
            if (StringUtil.getByteLength(dto.getDescription()) > ExcelUploadValidateEnums.ByteLength.BYTE_2000.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.description.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_2000.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
        }
        // 알러지 유발성분 목록
        if(StringUtils.isNotEmpty(dto.getAllergyEgg()) && !"Y".equals(dto.getAllergyEgg())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyEgg.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyMilk()) && !"Y".equals(dto.getAllergyMilk())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyMilk.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergySoybean()) && !"Y".equals(dto.getAllergySoybean())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergySoybean.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyBuckwheat()) && !"Y".equals(dto.getAllergyBuckwheat())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyBuckwheat.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyWheat()) && !"Y".equals(dto.getAllergyWheat())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyWheat.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyTomato()) && !"Y".equals(dto.getAllergyTomato())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyTomato.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyPinenut()) && !"Y".equals(dto.getAllergyPinenut())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyPinenut.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyWalnut()) && !"Y".equals(dto.getAllergyWalnut())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyWalnut.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyPeanut()) && !"Y".equals(dto.getAllergyPeanut())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyPeanut.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyPork()) && !"Y".equals(dto.getAllergyPork())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyPork.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyBeef()) && !"Y".equals(dto.getAllergyBeef())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyBeef.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyChicken()) && !"Y".equals(dto.getAllergyChicken())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyChicken.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyCrab()) && !"Y".equals(dto.getAllergyCrab())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyCrab.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyMackerel()) && !"Y".equals(dto.getAllergyMackerel())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyMackerel.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergySquid()) && !"Y".equals(dto.getAllergySquid())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergySquid.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyShellfish()) && !"Y".equals(dto.getAllergyShellfish())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyShellfish.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyShrimp()) && !"Y".equals(dto.getAllergyShrimp())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyShrimp.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergySulfite()) && !"Y".equals(dto.getAllergySulfite())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergySulfite.getCodeName()));
            return BaseEnums.Default.FAIL;
        }
        if(StringUtils.isNotEmpty(dto.getAllergyPeach()) && !"Y".equals(dto.getAllergyPeach())){
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_YN.getMessage(), ExcelUploadEnums.MealContsUploadCols.allergyPeach.getCodeName()));
            return BaseEnums.Default.FAIL;
        }

        // 기본영양정보
        if(StringUtils.isNotEmpty(dto.getNutritionTotalCarbohydrate())){
            if(!NumberUtils.isCreatable(dto.getNutritionTotalCarbohydrate())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalCarbohydrate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionTotalCarbohydrate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalCarbohydrate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double totalCarbohydrate = Double.valueOf(dto.getNutritionTotalCarbohydrate());
            if(totalCarbohydrate == Math.floor(totalCarbohydrate) && ! Double.isInfinite(totalCarbohydrate)){
                dto.setNutritionTotalCarbohydrate(String.format("%.0f", totalCarbohydrate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionTotalCarbohydrateRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionTotalCarbohydrateRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalCarbohydrateRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionTotalCarbohydrateRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalCarbohydrateRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double totalCarbohydrateRate = Double.valueOf(dto.getNutritionTotalCarbohydrateRate());
            if(totalCarbohydrateRate == Math.floor(totalCarbohydrateRate) && ! Double.isInfinite(totalCarbohydrateRate)){
                dto.setNutritionTotalCarbohydrateRate(String.format("%.0f", totalCarbohydrateRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionFiber())){
            if(!NumberUtils.isCreatable(dto.getNutritionFiber())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionFiber.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionFiber())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionFiber.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double fiber = Double.valueOf(dto.getNutritionFiber());
            if(fiber == Math.floor(fiber) && ! Double.isInfinite(fiber)){
                dto.setNutritionFiber(String.format("%.0f", fiber));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionFiberRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionFiberRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionFiberRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionFiberRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionFiberRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double fiberRate = Double.valueOf(dto.getNutritionFiberRate());
            if(fiberRate == Math.floor(fiberRate) && ! Double.isInfinite(fiberRate)){
                dto.setNutritionFiberRate(String.format("%.0f", fiberRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionSugars())){
            if(!NumberUtils.isCreatable(dto.getNutritionSugars())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSugars.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionSugars())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSugars.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double sugars = Double.valueOf(dto.getNutritionSugars());
            if(sugars == Math.floor(sugars) && ! Double.isInfinite(sugars)){
                dto.setNutritionSugars(String.format("%.0f", sugars));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionSugarsRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionSugarsRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSugarsRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionSugarsRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSugarsRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double sugarsRate = Double.valueOf(dto.getNutritionSugarsRate());
            if(sugarsRate == Math.floor(sugarsRate) && ! Double.isInfinite(sugarsRate)){
                dto.setNutritionSugarsRate(String.format("%.0f", sugarsRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionTotalFat())){
            if(!NumberUtils.isCreatable(dto.getNutritionTotalFat())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalFat.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionTotalFat())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalFat.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double totalFat = Double.valueOf(dto.getNutritionTotalFat());
            if(totalFat == Math.floor(totalFat) && ! Double.isInfinite(totalFat)){
                dto.setNutritionTotalFat(String.format("%.0f", totalFat));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionTotalFatRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionTotalFatRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalFatRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionTotalFatRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTotalFatRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double totalFatRate = Double.valueOf(dto.getNutritionTotalFatRate());
            if(totalFatRate == Math.floor(totalFatRate) && ! Double.isInfinite(totalFatRate)){
                dto.setNutritionTotalFatRate(String.format("%.0f", totalFatRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionSaturatedFat())){
            if(!NumberUtils.isCreatable(dto.getNutritionSaturatedFat())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSaturatedFat.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionSaturatedFat())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSaturatedFat.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double saturatedFat = Double.valueOf(dto.getNutritionSaturatedFat());
            if(saturatedFat == Math.floor(saturatedFat) && ! Double.isInfinite(saturatedFat)){
                dto.setNutritionSaturatedFat(String.format("%.0f", saturatedFat));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionSaturatedFatRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionSaturatedFatRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSaturatedFatRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionSaturatedFatRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSaturatedFatRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double saturatedFatRate = Double.valueOf(dto.getNutritionSaturatedFatRate());
            if(saturatedFatRate == Math.floor(saturatedFatRate) && ! Double.isInfinite(saturatedFatRate)){
                dto.setNutritionSaturatedFatRate(String.format("%.0f", saturatedFatRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionTransFat())){
            if(!NumberUtils.isCreatable(dto.getNutritionTransFat())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTransFat.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionTransFat())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTransFat.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double transFat = Double.valueOf(dto.getNutritionTransFat());
            if(transFat == Math.floor(transFat) && ! Double.isInfinite(transFat)){
                dto.setNutritionTransFat(String.format("%.0f", transFat));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionTransFatRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionTransFatRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTransFatRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionTransFatRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionTransFatRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double transFatRate = Double.valueOf(dto.getNutritionTransFatRate());
            if(transFatRate == Math.floor(transFatRate) && ! Double.isInfinite(transFatRate)){
                dto.setNutritionTransFatRate(String.format("%.0f", transFatRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionProtein())){
            if(!NumberUtils.isCreatable(dto.getNutritionProtein())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionProtein.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionProtein())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionProtein.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double protein = Double.valueOf(dto.getNutritionProtein());
            if(protein == Math.floor(protein) && ! Double.isInfinite(protein)){
                dto.setNutritionProtein(String.format("%.0f", protein));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionProteinRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionProteinRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionProteinRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionProteinRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionProteinRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double proteinRate = Double.valueOf(dto.getNutritionProteinRate());
            if(proteinRate == Math.floor(proteinRate) && ! Double.isInfinite(proteinRate)){
                dto.setNutritionProteinRate(String.format("%.0f", proteinRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionCholesterol())){
            if(!NumberUtils.isCreatable(dto.getNutritionCholesterol())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionCholesterol.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionCholesterol())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionCholesterol.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double cholesterol = Double.valueOf(dto.getNutritionCholesterol());
            if(cholesterol == Math.floor(cholesterol) && ! Double.isInfinite(cholesterol)){
                dto.setNutritionCholesterol(String.format("%.0f", cholesterol));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionCholesterolRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionCholesterolRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionCholesterolRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionCholesterolRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionCholesterolRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double cholesterolRate = Double.valueOf(dto.getNutritionCholesterolRate());
            if(cholesterolRate == Math.floor(cholesterolRate) && ! Double.isInfinite(cholesterolRate)){
                dto.setNutritionCholesterolRate(String.format("%.0f", cholesterolRate));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionSodium())){
            if(!NumberUtils.isCreatable(dto.getNutritionSodium())){
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSodium.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionSodium())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSodium.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double sodieum = Double.valueOf(dto.getNutritionSodium());
            if(sodieum == Math.floor(sodieum) && ! Double.isInfinite(sodieum)){
                dto.setNutritionSodium(String.format("%.0f", sodieum));
            }
        }
        if(StringUtils.isNotEmpty(dto.getNutritionSodiumRate())){
            if (!NumberUtils.isCreatable(dto.getNutritionSodiumRate())) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSodiumRate.getCodeName()));
                return BaseEnums.Default.FAIL;
            }
            if (StringUtil.getByteLength(String.valueOf(dto.getNutritionSodiumRate())) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.nutritionSodiumRate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
                return BaseEnums.Default.FAIL;
            }
            Double sodiumRate = Double.valueOf(dto.getNutritionSodiumRate());
            if(sodiumRate == Math.floor(sodiumRate) && ! Double.isInfinite(sodiumRate)){
                dto.setNutritionSodiumRate(String.format("%.0f", sodiumRate));
            }
        }

        // 상품제공고시
        if (StringUtil.getByteLength(dto.getSpecGoodsName()) > ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specGoodsName.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getSpecGoodsType()) > ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specGoodsType.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getSpecProducerLocation()) > ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specProducerLocation.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getSpecManufacturingDate()) > ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specManufacturingDate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getSpecExpirationDate()) > ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specExpirationDate.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getSpecStorageMethod()) > ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specStorageMethod.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_100.getByteLength()));
            return BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getSpecOriginalMaterial()) > ExcelUploadValidateEnums.ByteLength.BYTE_1000.getByteLength()) {
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.mealContsValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.MealContsUploadCols.specOriginalMaterial.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_1000.getByteLength()));
            return BaseEnums.Default.FAIL;
        }

        return returnStatus;
    }

    @Override
    public List<FooditemIconVo> getFooditemIconList(String ilGoodsDailyMealContsCd){
        return mealContsService.getFooditemIconList(ilGoodsDailyMealContsCd);
    }
}
