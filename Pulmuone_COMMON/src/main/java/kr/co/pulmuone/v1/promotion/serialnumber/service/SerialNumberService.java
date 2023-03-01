package kr.co.pulmuone.v1.promotion.serialnumber.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.enums.SerialEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.promotion.serialnumber.SerialNumberMapper;
import kr.co.pulmuone.v1.comm.util.google.Recaptcha;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponValidationByUserResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.dto.CommonCheckAddPointValidationByUserResponseDto;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.AddSerialNumberResponseDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListResponseDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestSaveDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.SerialNumberTypeResponseDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.CommonGetSerialNumberInfoVo;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.GetSerialNumberListResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SerialNumberService {

    private final SerialNumberMapper serialNumberMapper;
    private final PromotionPointBiz promotionPointBiz;
    private final PromotionCouponBiz promotionCouponBiz;
    private final UserBuyerBiz userBuyerBiz;

    private final PointBiz pointBiz;

    @Autowired
    Recaptcha googleRecaptcha;



    /**
     * 카테고리 정보
     *
     * @param getSerialNumberListRequestDto
     * @return GetCategoryResultDto
     * @throws Exception
     */
    protected Page<GetSerialNumberListResultVo> getSerialNumberList(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {
        PageMethod.startPage(getSerialNumberListRequestDto.getPage(), getSerialNumberListRequestDto.getPageSize());
        return serialNumberMapper.getSerialNumberList(getSerialNumberListRequestDto);
    }

    protected GetSerialNumberListResponseDto putSerialNumberCancel(PutSerialNumberCancelRequestDto putSerialNumberCancelRequestDto) throws Exception {
        GetSerialNumberListResponseDto result = new GetSerialNumberListResponseDto();

        List<PutSerialNumberCancelRequestSaveDto> updateRequestDtoList = putSerialNumberCancelRequestDto.getUpdateRequestDtoList();
        if (!updateRequestDtoList.isEmpty()) {
            serialNumberMapper.putSerialNumberCancel(updateRequestDtoList);
        }
//    	int total = serialNumberMapper.getSerialNumberListCount(getSerialNumberListRequestDto);
//    	result.setRows(rows);
//    	result.setTotal(total);

        return result;
    }

    /**
     * Captcha 인증 진행
     *
     * @param captcha String
     * @return boolean
     * @throws Exception exception
     */
    protected boolean checkCaptcha(String captcha) throws Exception {
        if (captcha == null || captcha.equals("")) return false;

        // API 통신 처리
        return googleRecaptcha.siteVerify(captcha);
    }

    /**
     * 시리얼 정보 판별
     *
     * @param serialNumber String
     * @return ApiResult<?>
     * @throws Exception exception
     */
    protected ApiResult<?> addPromotionByUser(String serialNumber, Long urUserId) throws Exception {
        SerialNumberTypeResponseDto serialNumberAddType = getSerialNumberAddType(serialNumber);
        AddSerialNumberResponseDto responseDto = new AddSerialNumberResponseDto();

        int failCount = userBuyerBiz.getPromotionRecaptchaFailCount();
        failCount++;
        responseDto.setRecaptchaFailCnt(failCount);

        if (serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.NONE)) {           // Error return(등록가능한 이용권 없음)
            responseDto.setSerialType(SerialEnums.SerialNumberAddType.NONE.getCode());
            userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
            return ApiResult.result(responseDto, SerialEnums.AddPromotion.NOT_FIND_SERIAL_NUMBER);
        } else if (serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.USE)) {     // Error return(사용한 이용권)
            responseDto.setSerialType(SerialEnums.SerialNumberAddType.USE.getCode());
            userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
            return ApiResult.result(responseDto, SerialEnums.AddPromotion.USE_SERIAL_NUMBER);
        } else if (serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.POINT) ||
                serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.FIX_POINT)) {   // 적립금 등록 처리
            responseDto.setSerialType(SerialEnums.SerialNumberAddType.POINT.getCode());
            CommonCheckAddPointValidationByUserResponseDto pointResponseDto = promotionPointBiz.checkPointValidationByUser(urUserId, serialNumberAddType.getPointOrCouponId());
            if (pointResponseDto.getValidationEnum().equals(PointEnums.AddPointValidation.PASS_VALIDATION)) {        // 적립금 정상처리
                //이용권 - 적립금 등록
                ApiResult<?> pointResult = pointBiz.depositPointsBySerialNumber(urUserId, serialNumber);
                if (BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum())) {
                    responseDto.setRecaptchaFailCnt(0);
                    userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
                    return ApiResult.result(responseDto, SerialEnums.AddPromotion.SUCCESS_ADD_POINT);
                } else if (PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())) {
                    responseDto.setRecaptchaFailCnt(0);
                    Object object = pointResult.getData();
                    if(object.getClass().isAssignableFrom(Integer.class)){
                        int temp = (Integer)pointResult.getData();
                        responseDto.setPointPartialDeposit(temp);
                    }else if (object.getClass().isAssignableFrom(Long.class)){
                        Long temp = (Long) pointResult.getData();
                        responseDto.setPointPartialDeposit(Math.toIntExact(temp));
                    }
                    //개별난수번호 사용처리
                    if (serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.POINT)) {
                        putSerialNumberSetUse(serialNumberAddType.getPmSerialNumberId(), urUserId);
                    }
                    userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
                    return ApiResult.result(responseDto, SerialEnums.AddPromotion.SUCCESS_ADD_POINT_PARTIAL);
                } else if (PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.equals(pointResult.getMessageEnum())) {
                    userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
                    return ApiResult.result(responseDto, SerialEnums.AddPromotion.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
                } else {
                    userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
                    return ApiResult.result(responseDto, SerialEnums.AddPromotion.NOT_FIND_SERIAL_NUMBER);
                }
            } else {                                                                                // Error return(적립금 기타 오류)
                userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
                return ApiResult.result(responseDto, pointResponseDto.getValidationEnum());
            }
        } else if (serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.COUPON) ||
                serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.FIX_COUPON)) {
            responseDto.setSerialType(SerialEnums.SerialNumberAddType.COUPON.getCode());
            CouponValidationByUserResponseDto couponResponseDto = promotionCouponBiz.checkCouponValidationByUser(urUserId, serialNumberAddType.getPointOrCouponId());
            if (couponResponseDto.getValidationEnum().equals(CouponEnums.AddCouponValidation.PASS_VALIDATION)) {        // 쿠폰 정상처리
                //이용권 - 쿠폰 등록
                promotionCouponBiz.addCouponWithoutValidation(couponResponseDto.getData(), urUserId, serialNumberAddType.getPmSerialNumberId());
                //개별난수번호 사용처리
                if (serialNumberAddType.getSerialType().equals(SerialEnums.SerialNumberAddType.COUPON)) {
                    putSerialNumberSetUse(serialNumberAddType.getPmSerialNumberId(), urUserId);
                }
                responseDto.setRecaptchaFailCnt(0);
                userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
                return ApiResult.result(responseDto, SerialEnums.AddPromotion.SUCCESS_ADD_COUPON);
            } else {
                userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());// Error return(쿠폰 기타 오류)
                return ApiResult.result(responseDto, couponResponseDto.getValidationEnum());
            }
        }

        userBuyerBiz.putPromotionRecaptchaFailCount(responseDto.getRecaptchaFailCnt());
        return ApiResult.result(responseDto, SerialEnums.AddPromotion.NOT_FIND_SERIAL_NUMBER);
    }

    /**
     * 이용권 등록 정보 판별
     *
     * @param serialNumber String
     * @return CommonGetSerialNumberTypeResponseDto
     * @throws Exception exception
     */
    private SerialNumberTypeResponseDto getSerialNumberAddType(String serialNumber) throws Exception {
        SerialEnums.SerialNumberAddType serialType = SerialEnums.SerialNumberAddType.NONE;
        Long id = 0L;
        CommonGetSerialNumberInfoVo vo = serialNumberMapper.getSerialNumberInfo(serialNumber);
        if (vo.getUseSerial() > 0) {
            serialType = SerialEnums.SerialNumberAddType.USE;
            id = vo.getPointSerial();
        } else if (!Objects.isNull(vo.getPointSerial())) {
            serialType = SerialEnums.SerialNumberAddType.POINT;
            id = vo.getPointSerial();
        } else if (!Objects.isNull(vo.getCouponSerial())) {
            serialType = SerialEnums.SerialNumberAddType.COUPON;
            id = vo.getCouponSerial();
        } else if (!Objects.isNull(vo.getPointFixSerial())) {
            serialType = SerialEnums.SerialNumberAddType.FIX_POINT;
            id = vo.getPointFixSerial();
        } else if (!Objects.isNull(vo.getCouponFixSerial())) {
            serialType = SerialEnums.SerialNumberAddType.FIX_COUPON;
            id = vo.getCouponFixSerial();
        }

        return SerialNumberTypeResponseDto.builder()
                .serialType(serialType)
                .pointOrCouponId(id)
                .pmSerialNumberId(vo.getPmSerialNumberId())
                .build();
    }

    /**
     * 개별난수번호 사용 처리
     *
     * @param pmSerialNumberId Long
     * @throws Exception exception
     */
    private void putSerialNumberSetUse(Long pmSerialNumberId, Long urUserId) throws Exception {
        serialNumberMapper.putSerialNumberSetUse(pmSerialNumberId, urUserId);
    }


    /**
     * 시리얼번호로 사용 처리
     * @param serialNumber
     * @throws Exception
     */
    protected int redeemSerialNumber(Long urUserId, String serialNumber) {
        return serialNumberMapper.redeemSerialNumber(urUserId, serialNumber);
    }




    /**
	 * 이용권 내역 엑셀 다운로드 목록 조회
	 *
	 * @param getBuyerListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
    @UserMaskingRun(system = "BOS")
	protected ExcelDownloadDto serialNumberListExportExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {
		String excelFileName = "이용권내역 엑셀다운로드";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = { 100, 300, 300, 400, 150, 150, 150, 300 };

		String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center" };

		String[] propertyListOfFirstWorksheet = { "rowNumber", "serialNumber", "createDate", "issuePeriod", "statusName", "loginId", "userName", "useDate" };

		String[] firstHeaderListOfFirstWorksheet = { "No", "이용권 번호", "등록일자", "등록가능기간", "사용여부", "사용 ID", "사용 회원명", "사용일시" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder().workSheetName(excelSheetName).propertyList(propertyListOfFirstWorksheet).widthList(widthListOfFirstWorksheet).alignList(alignListOfFirstWorksheet).build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		List<GetSerialNumberListResultVo>  getSerialNumberListResultVo = null;
		try
		{
			getSerialNumberListResultVo = serialNumberListExcel(getSerialNumberListRequestDto);
			log.info("getSerialNumberListResultVo {}", getSerialNumberListResultVo);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			throw e; // 추후 CustomException 으로 변환 예정
		}
		firstWorkSheetDto.setExcelDataList(getSerialNumberListResultVo);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}



	   /**
		 * 이용권 내역 엑셀 다운로드 목록 조회
		 *
		 * @param getBuyerListRequestDto
		 * @return ExcelDownloadDto
		 * @throws Exception
		 */
		protected ExcelDownloadDto ticketNumberExportExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {

			SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd");
			Date time = new Date();
			String downloadTime = dateFormat.format(time);

			String excelFileName = getSerialNumberListRequestDto.getDisplayName() + " " + downloadTime;
			String excelSheetName = "sheet";
			/* 화면값보다 20더 하면맞다. */
			Integer[] widthListOfFirstWorksheet = { 300};

			String[] alignListOfFirstWorksheet = { "center" };

			String[] propertyListOfFirstWorksheet = { "serialNumber"};

			String[] firstHeaderListOfFirstWorksheet = { "이용권 번호" };

			ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder().workSheetName(excelSheetName).propertyList(propertyListOfFirstWorksheet).widthList(widthListOfFirstWorksheet).alignList(alignListOfFirstWorksheet).build();

			firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

			List<GetSerialNumberListResultVo>  getSerialNumberListResultVo = null;
			try
			{
				getSerialNumberListResultVo = serialNumberListExcel(getSerialNumberListRequestDto);
				log.info("getSerialNumberListResultVo {}", getSerialNumberListResultVo);
			}
			catch (Exception e)
			{
				log.error(e.getMessage());
				throw e; // 추후 CustomException 으로 변환 예정
			}
			firstWorkSheetDto.setExcelDataList(getSerialNumberListResultVo);

			ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

			excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

			return excelDownloadDto;
		}


	/**
	 * 이용권 내역 리스트조회 엑셀다운로드
	 *
	 * @param getBuyerListRequestDto
	 * @return GetSerialNumberListResultVo
	 * @throws Exception
	 */
	@UserMaskingRun(system = "BOS")
	protected List<GetSerialNumberListResultVo> serialNumberListExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception
	{
		 if (StringUtils.isNotEmpty(getSerialNumberListRequestDto.getInputSearchValue())) {
	            getSerialNumberListRequestDto.setSearchValueList(Stream.of(getSerialNumberListRequestDto.getInputSearchValue().split("\n|,"))
	                    .map(String::trim)
	                    .filter(StringUtils::isNotEmpty)
	                    .collect(Collectors.toList()));
	        }

		 List<GetSerialNumberListResultVo> result = serialNumberMapper.serialNumberListExcel(getSerialNumberListRequestDto);

		// 화면과 동일하게 역순으로 no 지정
        for (int i = result.size() - 1; i >= 0; i--) {
        	result.get(i).setRowNumber(String.valueOf(result.size() - i));
        }

		return result;
	}





}
