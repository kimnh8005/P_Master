package kr.co.pulmuone.v1.user.dormancy.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.user.dormancy.UserDormancyMapper;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordRequestDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetBuyerMoveInfoByCiVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserDormantHistoryListResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserDormantListResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.UserDormancyResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200612    	  천혜현           최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class UserDormancyService {

    @Autowired
    private UserDormancyMapper userDormancyMapper;

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    /**
     * 휴면회원 정상전환
     * @param dto
     * @return
     * @throws Exception
     */
    protected void putUserDormant(CommonPutUserDormantRequestDto dto) throws Exception {

        userDormancyMapper.putUserActive(dto);
        userDormancyMapper.addUserBuyerActive(dto);
        userDormancyMapper.putUrUserMoveLog(dto);
        userDormancyMapper.delUrBuyerMove(dto);
        userDormancyMapper.delUrUserMove(dto);

    }

    protected ApiResult<?> putUserActive(String password) throws Exception {
        String ciCd = userCertificationBiz.getSessionUserCertification().getCi();

        // 본인인증 세션 체크 CI 값 체크
        if (StringUtils.isEmpty(ciCd)) {
            return ApiResult.result(UserEnums.Join.NOT_ANY_CERTI);
        }

        GetBuyerMoveInfoByCiVo getBuyerMoveInfoByCiVo = userDormancyMapper.getBuyerMoveInfoByCI(ciCd);

        if (getBuyerMoveInfoByCiVo == null) {
            return ApiResult.result(UserEnums.Join.NO_FIND_USER_MOVE);
        }

        // 비밀번호 변경
        PutPasswordRequestDto putPasswordRequestDto = new PutPasswordRequestDto();
        putPasswordRequestDto.setPassword(password);
        putPasswordRequestDto.setPasswordChangeCd(getBuyerMoveInfoByCiVo.getPasswordChangeCd());
        putPasswordRequestDto.setUrUserId(getBuyerMoveInfoByCiVo.getUrUserId());

        ApiResult<?> result = userCertificationBiz.putPassword(putPasswordRequestDto);
        log.info("putpasswordResponseDto.getCode()===> {}", result.getCode());

        // 성공인 경우에 실행
        if (result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
            // 휴면 회원 해제
            CommonPutUserDormantRequestDto commonPutUserDormantRequestDto = new CommonPutUserDormantRequestDto();

            commonPutUserDormantRequestDto.setUrUserId(getBuyerMoveInfoByCiVo.getUrUserId());
            commonPutUserDormantRequestDto.setUrUserMoveId(getBuyerMoveInfoByCiVo.getUrUserMoveId());

            putUserDormant(commonPutUserDormantRequestDto);
        }

        return result;

    }

    protected GetIsCheckUserMoveResultVo isCheckUserMove(String urUserId) throws Exception {

        return userDormancyMapper.isCheckUserMove(urUserId);
    }

    /**
     * 휴면회원 리스트조회
     * @param getUserDormantListRequestDto
     * @return GetUserDormantListResponseDto
     * @throws Exception
     */
     protected GetUserDormantListResponseDto getUserDormantList(GetUserDormantListRequestDto getUserDormantListRequestDto) throws Exception {
        if(StringUtils.isNotEmpty(getUserDormantListRequestDto.getCondiValue())) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(getUserDormantListRequestDto.getCondiValue(), "\n|,");
            while(st.hasMoreElements()) {
                String object = (String)st.nextElement();
                array.add(object);
            }
            getUserDormantListRequestDto.setCondiValueArray(array);
        }

//        int total = userDormancyMapper.getUserDormantListCount(getUserDormantListRequestDto);
        PageMethod.startPage(getUserDormantListRequestDto.getPage(), getUserDormantListRequestDto.getPageSize());
        Page<GetUserDormantListResultVo> rows = userDormancyMapper.getUserDormantList(getUserDormantListRequestDto);

         return GetUserDormantListResponseDto.builder()
                 .total((int) rows.getTotal())
                 .rows(rows.getResult())
                 .build();
    }

    /**
     * 휴면회원 이력리스트조회
     * @param getUserDormantHistoryListRequestDto
     * @return GetUserDormantHistoryListResponseDto
     * @throws Exception
     */
    protected GetUserDormantHistoryListResponseDto getUserDormantHistoryList(GetUserDormantHistoryListRequestDto getUserDormantHistoryListRequestDto) throws Exception {
        if(StringUtils.isNotEmpty(getUserDormantHistoryListRequestDto.getCondiValue())) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(getUserDormantHistoryListRequestDto.getCondiValue(), "\n|,");
            while(st.hasMoreElements()) {
                String object = (String)st.nextElement();
                array.add(object);
            }
            getUserDormantHistoryListRequestDto.setCondiValueArray(array);
        }

//        int total = userDormancyMapper.getUserDormantHistoryListCount(getUserDormantHistoryListRequestDto);
        PageMethod.startPage(getUserDormantHistoryListRequestDto.getPage(), getUserDormantHistoryListRequestDto.getPageSize());
        Page<GetUserDormantHistoryListResultVo> rows = userDormancyMapper.getUserDormantHistoryList(getUserDormantHistoryListRequestDto);

        return GetUserDormantHistoryListResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(rows.getResult())
                .build();
    }

	/**
	 * @Desc 휴면회원 전환 완료 정보 조회
	 * @param urUserId
	 * @return UserDormancyResultVo
	 */
	public UserDormancyResultVo getUserDormancyInfo(Long urUserId) {
		return userDormancyMapper.getUserDormancyInfo(urUserId);
	}

	/**
	 * @Desc 휴면회원 전환 예정 정보 조회
	 * @param urUserId
	 * @return UserDormancyResultVo
	 */
	public UserDormancyResultVo getUserDormancyExpected(Long urUserId) {
		return userDormancyMapper.getUserDormancyExpected(urUserId);
	}

}
