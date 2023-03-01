package kr.co.pulmuone.v1.user.movereason.service;

import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonResponseDto;
import kr.co.pulmuone.v1.user.movereason.dto.vo.MoveReasonResultVo;

import java.util.List;


/**
* <PRE>
* Forbiz Korea
* Class의 기능과 역할을 상세히 기술한다.
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 20.                최윤지          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class UserMoveReasonBizImpl  implements UserMoveReasonBiz {

    @Autowired
    UserMoveReasonService userMoveReasonService;

    /**
     * @Desc 회원탈퇴 사유 설정 목록 조회
     * @param moveReasonRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getMoveReasonList(MoveReasonRequestDto moveReasonRequestDto){
        MoveReasonResponseDto moveReasonResponseDto = new MoveReasonResponseDto();

        Page<MoveReasonResultVo> moveReasonResultList = userMoveReasonService.getMoveReasonList(moveReasonRequestDto);

        moveReasonResponseDto.setTotal(moveReasonResultList.getTotal());
        moveReasonResponseDto.setRows(moveReasonResultList.getResult());

        return ApiResult.success(moveReasonResponseDto);
    }

    /**
     * @Desc 회원탈퇴 사유 설정 상세조회
     * @param moveReasonRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getMoveReason(MoveReasonRequestDto moveReasonRequestDto){
        MoveReasonResponseDto moveReasonResponseDto = new MoveReasonResponseDto();
        moveReasonResponseDto.setMoveReasonResultVo(userMoveReasonService.getMoveReason(moveReasonRequestDto));

        return ApiResult.success(moveReasonResponseDto);

    }

    /**
     * @Desc 회원탈퇴 사유 설정 등록
     * @param moveReasonRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addMoveReason(MoveReasonRequestDto moveReasonRequestDto) throws Exception{
        //중복체크
        int count = userMoveReasonService.hasMoveReasonDuplicate(moveReasonRequestDto);
        if(count > 0) {
            return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        }

        moveReasonRequestDto.setMoveType(UserEnums.StcomnCodeUrMoveType.TYPE_1.getCode());
        userMoveReasonService.addMoveReason(moveReasonRequestDto);

        return ApiResult.success();
    }

    /**
     * @Desc 회원탈퇴 사유 설정 수정
     * @param moveReasonRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putMoveReason(MoveReasonRequestDto moveReasonRequestDto) throws Exception{

        //중복체크
        int count = userMoveReasonService.hasMoveReasonDuplicate(moveReasonRequestDto);
        if(count > 0) {
             return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        }
        userMoveReasonService.putMoveReason(moveReasonRequestDto);

        return ApiResult.success();
    }

    @Override
    public List<CodeInfoVo> getMoveReasonCode() {
        return userMoveReasonService.getMoveReasonCode();
    }

}
