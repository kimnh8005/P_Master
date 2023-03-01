package kr.co.pulmuone.v1.user.movereason.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;

import java.util.List;

public interface UserMoveReasonBiz {

    ApiResult<?> getMoveReasonList(MoveReasonRequestDto moveReasonRequestDto);
    ApiResult<?> getMoveReason(MoveReasonRequestDto moveReasonRequestDto);
    ApiResult<?> addMoveReason(MoveReasonRequestDto moveReasonRequestDto) throws Exception;
    ApiResult<?> putMoveReason(MoveReasonRequestDto moveReasonRequestDto) throws Exception;
    List<CodeInfoVo> getMoveReasonCode();
}
