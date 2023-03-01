package kr.co.pulmuone.v1.comm.mapper.user.movereason;

import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;
import kr.co.pulmuone.v1.user.movereason.dto.vo.MoveReasonResultVo;

import java.util.List;

@Mapper
public interface UserMoveReasonMapper {
    /**
     * @Desc 회원탈퇴 사유 설정 목록 조회
     * @param moveReasonRequestDto
     * @return Page<MoveReasonResultVo>
     */
    Page<MoveReasonResultVo> getMoveReasonList(MoveReasonRequestDto moveReasonRequestDto);

    /**
     * @Desc 회원탈퇴 사유 설정 상세조회
     * @param moveReasonRequestDto
     * @return MoveReasonResultVo
     */
    MoveReasonResultVo getMoveReason(MoveReasonRequestDto moveReasonRequestDto);

    /**
     * @Desc 회원탈퇴 사유 설정 중복 체크
     * @param moveReasonRequestDto
     * @return int
     */
    int hasMoveReasonDuplicate(MoveReasonRequestDto moveReasonRequestDto);

    /**
     * @Desc 회원탈퇴 사유 설정 등록
     * @param moveReasonRequestDto
     * @return int
     */
    int addMoveReason(MoveReasonRequestDto moveReasonRequestDto);

    /**
     * @Desc 회원탈퇴 사유 설정 수정
     * @param moveReasonRequestDto
     * @return int
     */
    int putMoveReason(MoveReasonRequestDto moveReasonRequestDto);

    List<CodeInfoVo> getMoveReasonCode();

}
