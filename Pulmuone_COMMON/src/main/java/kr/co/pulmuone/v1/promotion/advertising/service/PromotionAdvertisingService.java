package kr.co.pulmuone.v1.promotion.advertising.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.enums.AdvertisingEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.promotion.advertising.PromotionAdvertisingMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.advertising.dto.*;
import kr.co.pulmuone.v1.promotion.advertising.dto.vo.AdvertisingExternalVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20210503   	 이원호            최초작성
 * =======================================================================
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionAdvertisingService {
    private final PromotionAdvertisingMapper promotionAdvertisingMapper;

    @Value("${resource.server.url.mall}")
    private String mallUrl;

    /**
     * 외부광고 코드관리 목록 조회
     *
     * @param dto AdvertisingExternalListRequestDto
     * @return AdvertisingExternalListResponseDto
     */
    protected AdvertisingExternalListResponseDto getAdvertisingExternalList(AdvertisingExternalListRequestDto dto) throws Exception {
        // 요청값 설정
        if (!StringUtil.isEmpty(dto.getMedium())) {
            List<String> filterStr = Stream.of(dto.getMedium().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setMedium(filterStr.get(1));
        }
        if (!StringUtil.isEmpty(dto.getCampaign())) {
            List<String> filterStr = Stream.of(dto.getCampaign().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setMedium(filterStr.get(1));
            dto.setCampaign(filterStr.get(2));
        }

        // 검색조건에서 삭제처리.. 외부몰기획서 v1.5
//        if (!StringUtil.isEmpty(dto.getContent()) && !StringUtil.isEmpty(dto.getTerm())) {
//            List<String> filterStr_C = Stream.of(dto.getContent().split(Constants.ARRAY_SEPARATORS))
//                    .map(String::trim)
//                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
//                    .collect(Collectors.toList());
//            List<String> filterStr_T = Stream.of(dto.getTerm().split(Constants.ARRAY_SEPARATORS))
//                    .map(String::trim)
//                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
//                    .collect(Collectors.toList());
//            dto.setMedium(filterStr_C.get(1));
//            dto.setCampaign(filterStr_C.get(2));
//            dto.setContent(filterStr_C.get(3));
//            dto.setTerm(filterStr_T.get(3));
//        }
//
//        if (!StringUtil.isEmpty(dto.getContent()) && StringUtil.isEmpty(dto.getTerm())) {
//            List<String> filterStr = Stream.of(dto.getContent().split(Constants.ARRAY_SEPARATORS))
//                    .map(String::trim)
//                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
//                    .collect(Collectors.toList());
//            dto.setMedium(filterStr.get(1));
//            dto.setCampaign(filterStr.get(2));
//            dto.setContent(filterStr.get(3));
//        }
//
//        if (StringUtil.isEmpty(dto.getContent()) && !StringUtil.isEmpty(dto.getTerm())) {
//            List<String> filterStr = Stream.of(dto.getTerm().split(Constants.ARRAY_SEPARATORS))
//                    .map(String::trim)
//                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
//                    .collect(Collectors.toList());
//            dto.setMedium(filterStr.get(1));
//            dto.setCampaign(filterStr.get(2));
//            dto.setTerm(filterStr.get(3));
//        }

        dto.setUseYnList(Stream.of(dto.getUseYnFilter().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        dto.setCreateStartDate(DateUtil.convertFormatNew(dto.getCreateStartDate(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setCreateEndDate(DateUtil.convertFormatNew(dto.getCreateEndDate(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setModifyStartDate(DateUtil.convertFormatNew(dto.getModifyStartDate(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setModifyEndDate(DateUtil.convertFormatNew(dto.getModifyEndDate(), "yyyyMMdd", "yyyy-MM-dd"));

        // 조회
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<AdvertisingExternalVo> result = promotionAdvertisingMapper.getAdvertisingExternalList(dto);

        // 결과값 설정
        List<AdvertisingExternalVo> responseList = result.getResult();
        for (AdvertisingExternalVo vo : responseList) {
            if ("Y".equals(vo.getUseYn())) {
                vo.setUseYnName("예");
            } else {
                vo.setUseYnName("아니오");
            }
            if (StringUtil.isEmpty(vo.getModifyLoginId())) {
                vo.setUserInfo(vo.getCreateName() + "/" + vo.getCreateLoginId());
            } else {
                vo.setUserInfo(vo.getModifyName() + "/" + vo.getModifyLoginId());
            }

            if (StringUtil.isEmpty(vo.getModifyDateTime())) {
                vo.setModifyDateTime("-");
            }

            vo.setAdvertisingUrl(mallUrl + PromotionConstants.AD_EXTERNAL_URL + vo.getPmAdExternalCd());
        }

        return AdvertisingExternalListResponseDto.builder()
                .total(result.getTotal())
                .rows(result.getResult())
                .build();
    }

    /**
     * 외부광고 코드관리 조회
     *
     * @param pmAdExternalCd String
     * @return AdvertisingExternalVo
     */
    protected AdvertisingExternalResponseDto getAdvertisingExternal(AdvertisingExternalRequestDto dto) throws Exception {
        List<AdvertisingExternalVo> responseList = promotionAdvertisingMapper.getAdvertisingExternal(dto);
        for (AdvertisingExternalVo vo : responseList) {
            vo.setAdvertisingUrl(mallUrl + PromotionConstants.AD_EXTERNAL_URL + vo.getPmAdExternalCd());
        }

        return AdvertisingExternalResponseDto.builder()
                .advertisingExternalList(responseList)
                .build();
    }

    /**
     * 외부광고 코드관리 다건 등록
     *
     * @param dtoList List<AddAdvertisingExternalRequestDto>
     */
    protected void addAdvertisingExternalList(List<AddAdvertisingExternalRequestDto> dtoList) throws Exception {
        // 기존 내역 조회
        List<String> pmAdExternalCdList = dtoList.stream()
                .map(AddAdvertisingExternalRequestDto::getPmAdExternalCd)
                .collect(Collectors.toList());
        AdvertisingExternalRequestDto requestDto = new AdvertisingExternalRequestDto();
        requestDto.setPmAdExternalCdList(pmAdExternalCdList);
        List<AdvertisingExternalVo> responseList = promotionAdvertisingMapper.getAdvertisingExternal(requestDto);

        // 추가, 수정 구분
        Set<String> setAd = responseList.stream()
                .map(AdvertisingExternalVo::getPmAdExternalCd)
                .collect(Collectors.toSet());

        List<AddAdvertisingExternalRequestDto> insertList = new ArrayList<>();
        List<AddAdvertisingExternalRequestDto> updateList = new ArrayList<>();
        for (AddAdvertisingExternalRequestDto dto : dtoList) {
            if (setAd.contains(dto.getPmAdExternalCd())) {
                updateList.add(dto);
            } else {
                insertList.add(dto);
            }
        }

        if (insertList.size() > 0) {
            promotionAdvertisingMapper.addAdvertisingExternal(insertList);  //추가
        }
        if (updateList.size() > 0) {
            promotionAdvertisingMapper.putAdvertisingExternal(updateList);  //수정
        }
    }

    /**
     * 외부광고 코드관리 단건 등록
     *
     * @param dto AddAdvertisingExternalRequestDto
     */
    protected ApiResult<?> addAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        bindRequestDto(dto);

        MessageCommEnum validationResult = validationDto(dto);
        if (!BaseEnums.Default.SUCCESS.equals(validationResult)) {
            return ApiResult.result(validationResult);
        }

        promotionAdvertisingMapper.addAdvertisingExternal(Collections.singletonList(dto));
        return ApiResult.success();
    }

    private MessageCommEnum validationDto(AddAdvertisingExternalRequestDto dto) throws Exception {
        //validation
        if (!StringUtil.isEmpty(dto.getNewSource())) {
            dto.setSearchType(AdvertisingEnums.SearchType.SOURCE.getCode());
            int cnt = promotionAdvertisingMapper.getAdExternalTypeCount(dto);
            if (cnt > 0) {
                return AdvertisingEnums.AddValidation.EXIST_SOURCE;
            }
        }
        if (!StringUtil.isEmpty(dto.getNewMedium())) {
            dto.setSearchType(AdvertisingEnums.SearchType.MEDIUM.getCode());
            int cnt = promotionAdvertisingMapper.getAdExternalTypeCount(dto);
            if (cnt > 0) {
                return AdvertisingEnums.AddValidation.EXIST_MEDIUM;
            }
        }
        if (!StringUtil.isEmpty(dto.getNewCampaign())) {
            dto.setSearchType(AdvertisingEnums.SearchType.CAMPAIGN.getCode());
            int cnt = promotionAdvertisingMapper.getAdExternalTypeCount(dto);
            if (cnt > 0) {
                return AdvertisingEnums.AddValidation.EXIST_CAMPAIGN;
            }
        }
//        if (!StringUtil.isEmpty(dto.getNewContent())) {
//            dto.setSearchType(AdvertisingEnums.SearchType.CONTENT.getCode());
//            int cnt = promotionAdvertisingMapper.getAdExternalTypeCount(dto);
//            if (cnt > 0) {
//                return AdvertisingEnums.AddValidation.EXIST_CONTENT;
//            }
//        }
//        if (!StringUtil.isEmpty(dto.getNewTerm())) {
//            dto.setSearchType(AdvertisingEnums.SearchType.TERM.getCode());
//            int cnt = promotionAdvertisingMapper.getAdExternalTypeCount(dto);
//            if (cnt > 0) {
//                return AdvertisingEnums.AddValidation.EXIST_TERM;
//            }
//        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 외부광고 코드관리 단건 수정
     *
     * @param dto AddAdvertisingExternalRequestDto
     */
    protected void putAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        promotionAdvertisingMapper.putAdvertisingExternal(Collections.singletonList(dto));
    }

    private void bindRequestDto(AddAdvertisingExternalRequestDto dto) {
        //분류 값 재계산 - DropdownBox 선택 케이스
        if (!StringUtil.isEmpty(dto.getMedium())) {
            List<String> filterStr = Stream.of(dto.getMedium().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setMedium(filterStr.get(1));
        }
        if (!StringUtil.isEmpty(dto.getCampaign())) {
            List<String> filterStr = Stream.of(dto.getCampaign().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            dto.setMedium(filterStr.get(1));
            dto.setCampaign(filterStr.get(2));
        }

        //분류 값 재계산 - 직접입력 케이스
        if (StringUtil.isEmpty(dto.getSource())) {
            dto.setSource(dto.getNewSource());
            dto.setMedium(dto.getNewMedium());
            dto.setCampaign(dto.getNewCampaign());
        } else if (StringUtil.isEmpty(dto.getMedium())) {
            dto.setMedium(dto.getNewMedium());
            dto.setCampaign(dto.getNewCampaign());
        } else if (StringUtil.isEmpty(dto.getCampaign())) {
            dto.setCampaign(dto.getNewCampaign());
        }
        dto.setContent(dto.getNewContent());
        dto.setTerm(dto.getNewTerm());
    }

    /**
     * 외부광고 코드관리 단건 수정
     *
     * @param dto AddAdvertisingExternalRequestDto
     * @return GetCodeListResponseDto
     */
    protected GetCodeListResponseDto getAdvertisingType(AdvertisingTypeRequestDto dto) throws Exception {
        GetCodeListResponseDto result = new GetCodeListResponseDto();

        if (AdvertisingEnums.SearchType.CAMPAIGN.getCode().equals(dto.getSearchType())) {
            List<String> filterStr = Stream.of(dto.getMedium().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            if (filterStr.size() < 2) return result;
            dto.setSource(filterStr.get(0));
            dto.setMedium(filterStr.get(1));
        } else if (
                AdvertisingEnums.SearchType.CONTENT.getCode().equals(dto.getSearchType())
                || AdvertisingEnums.SearchType.TERM.getCode().equals(dto.getSearchType())) {
            List<String> filterStr = Stream.of(dto.getCampaign().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList());
            if (filterStr.size() < 3) return result;
            dto.setSource(filterStr.get(0));
            dto.setMedium(filterStr.get(1));
            dto.setCampaign(filterStr.get(2));
        }

        result.setRows(promotionAdvertisingMapper.getAdvertisingType(dto));
        return result;
    }

    /**
     * 외부광고 코드 - 코드 값 중복여부
     *
     * @param pmAdExternalCd String
     * @return boolean
     */
    protected boolean isExistPmAdExternalCd(String pmAdExternalCd) throws Exception {
        return promotionAdvertisingMapper.getAdExternalCdCount(pmAdExternalCd) > 0;
    }

}