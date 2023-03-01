package kr.co.pulmuone.v1.user.buyer.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mapper.user.buyer.UserDropMapper;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.UserDropRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetUserDropListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.UserDropResultVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200612    	  천혜현           최초작성
 * 1.1    20210115        최윤지           자동메일 템플릿 작성
 * =======================================================================
 * </PRE>
 */

@Service
public class UserBuyerDropService {

    @Autowired
    private UserDropMapper userDropMapper;

    /**
     * 탈퇴회원 리스트조회
     *
     * @param dto
     * @return GetUserDropListResponseDto
     * @throws Exception
     */
    protected GetUserDropListResponseDto getUserDropList(GetUserDropListRequestDto dto) throws Exception {

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetUserDropListResultVo> rows = userDropMapper.getUserDropList(dto); // rows

        return GetUserDropListResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(rows.getResult())
                .build();
    }


    /**
     * 회원 탈퇴 진행
     *
     * @param dto UserDropRequestDto
     * @throws Exception Exception
     */
    protected void progressUserDrop(UserDropRequestDto dto) throws Exception {

        userDropMapper.addUserDrop(dto);
        userDropMapper.putUser(dto.getUrUserId());
        userDropMapper.delBuyer(dto.getUrUserId());
        userDropMapper.delShippingAddr(dto.getUrUserId());
        userDropMapper.delRefundBank(dto.getUrUserId());

    }


	/**
	 * @Desc 회원탈퇴 완료 시 회원탈퇴결과 조회
	 * @param urUserId
	 * @return UserDropResultVo
	 */
    protected UserDropResultVo getUserDropInfo(Long urUserDropId) {
    	return userDropMapper.getUserDropInfo(urUserDropId);
    }


}
