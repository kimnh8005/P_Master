package kr.co.pulmuone.mall.customer.qna.service;

import kr.co.pulmuone.mall.comm.constant.MallDomainPrefixEnum;
import kr.co.pulmuone.mall.comm.constant.MallStorageInfoEnum;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.service.CustomerQnaBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    2020. 11. 30     최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class CustomerQnaMallServiceImpl implements CustomerQnaMallService {

    @Autowired
    public CustomerQnaBiz customerQnaBiz;
    @Autowired
    public UserBuyerBiz userBuyerBiz;

    /**
     * 1:1문의화면 정보 - 유형
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> getQnaInfoByCustomer() throws Exception {

        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return ApiResult.success(userBuyerBiz.getCommonCode("QNA_ONETOONE_TP"));
    }

    /**
     * 1:1 문의 등록
     *
     * @param onetooneQnaByUserRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception {

        String publicRootStoragePath = MallStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
        String serverSubPath = MallStorageInfoEnum.getFullSubStoragePath(StorageType.PUBLIC.name(), MallDomainPrefixEnum.FB.name());
        onetooneQnaByUserRequestDto.setServerSubPath(serverSubPath);
        onetooneQnaByUserRequestDto.setPublicRootStoragePath(publicRootStoragePath);

        //세션 정보 불러오기
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        onetooneQnaByUserRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        // ECS 연동 위한 회원 정보 set
        onetooneQnaByUserRequestDto.setUserName(buyerVo.getUserName());
        onetooneQnaByUserRequestDto.setUserMobile(buyerVo.getUserMobile());
        onetooneQnaByUserRequestDto.setUserEmail(buyerVo.getUserEmail());

        return customerQnaBiz.addQnaByUser(onetooneQnaByUserRequestDto);
    }

    /**
     * 1:1 문의 수정
     *
     * @param onetooneQnaByUserRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @Override
    public ApiResult<?> putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception {

        String publicRootStoragePath = MallStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
        String serverSubPath = MallStorageInfoEnum.getFullSubStoragePath(StorageType.PUBLIC.name(), MallDomainPrefixEnum.FB.name());
        onetooneQnaByUserRequestDto.setServerSubPath(serverSubPath);
        onetooneQnaByUserRequestDto.setPublicRootStoragePath(publicRootStoragePath);

        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        onetooneQnaByUserRequestDto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        // 1:1문의 status 조회
        OnetooneQnaByUserResponseDto qnaDetailDto = customerQnaBiz.getQnaDetailByUser(onetooneQnaByUserRequestDto.getCsQnaId(), Long.valueOf(buyerVo.getUrUserId()));

        if (!qnaDetailDto.getStatus().equals(QnaEnums.QnaStatus.RECEPTION.getCode())) {
            if (qnaDetailDto.getStatus().equals(QnaEnums.QnaStatus.ANSWER_COMPLETED.getCode())) {
                return ApiResult.result(QnaEnums.PutQnaValidation.ANSWER_COMPLETED);
            }
            return ApiResult.result(QnaEnums.PutQnaValidation.ANSWER_CHECKING);
        }

        return customerQnaBiz.putQnaByUser(onetooneQnaByUserRequestDto);
    }

    /**
     * 1:1 문의 상세 조회
     *
     * @param csQnaId
     * @param urUserId
     * @throws Exception
     */
    @Override
    public ApiResult<?> getQnaDetailByUser(Long csQnaId) throws Exception {

        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        Long urUserId = Long.valueOf(buyerVo.getUrUserId());

        return ApiResult.success(customerQnaBiz.getQnaDetailByUser(csQnaId, urUserId));
    }


    /**
     * 1:1 문의 주문조회 팝업조회
     *
     * @param searchPeriod
     * @throws Exception
     */
    @Override
    public ApiResult<?> getOrderInfoPopupByQna(String searchPeriod) throws Exception {

        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        Long urUserId = Long.valueOf(buyerVo.getUrUserId());

        return ApiResult.success(customerQnaBiz.getOrderInfoPopupByQna(searchPeriod, urUserId));

    }
}
