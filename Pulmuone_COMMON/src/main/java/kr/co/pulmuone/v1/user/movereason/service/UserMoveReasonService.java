package kr.co.pulmuone.v1.user.movereason.service;

import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.movereason.UserMoveReasonMapper;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;
import kr.co.pulmuone.v1.user.movereason.dto.vo.MoveReasonResultVo;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * dto, vo import 하기
* <PRE>
* Forbiz Korea
* 회원 탈퇴사유 설정
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 07. 17.               박영후          최초작성
*  1.0    2020. 10. 19.               최윤지          NEW 변경
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class UserMoveReasonService {

    @Autowired
    private final UserMoveReasonMapper userMoveReasonMapper;

    /**
     * @Desc 회원탈퇴 사유 설정 목록 조회
     * @param moveReasonRequestDto
     * @return Page<MoveReasonResultVo>
     */
    protected Page<MoveReasonResultVo> getMoveReasonList(MoveReasonRequestDto moveReasonRequestDto){
        PageMethod.startPage(moveReasonRequestDto.getPage(), moveReasonRequestDto.getPageSize());
        return userMoveReasonMapper.getMoveReasonList(moveReasonRequestDto);
    }

    /**
     * @Desc 회원탈퇴 사유 설정 상세조회
     * @param moveReasonRequestDto
     * @return MoveReasonResultVo
     */
    protected MoveReasonResultVo getMoveReason(MoveReasonRequestDto moveReasonRequestDto) {
        return userMoveReasonMapper.getMoveReason(moveReasonRequestDto);
    }

    /**
     * @Desc 회원탈퇴 사유 설정 중복 체크
     * @param moveReasonRequestDto
     * @return int
     */
    protected int hasMoveReasonDuplicate(MoveReasonRequestDto moveReasonRequestDto) {
        return userMoveReasonMapper.hasMoveReasonDuplicate(moveReasonRequestDto);
    }

    /**
     * @Desc 회원탈퇴 사유 설정 등록
     * @param moveReasonRequestDto
     * @return int
     */
    protected int addMoveReason(MoveReasonRequestDto moveReasonRequestDto) {
        return userMoveReasonMapper.addMoveReason(moveReasonRequestDto);
    }

    /**
     * @Desc 회원탈퇴 사유 설정 수정
     * @param moveReasonRequestDto
     * @return int
     */
    protected int putMoveReason(MoveReasonRequestDto moveReasonRequestDto) {
        return userMoveReasonMapper.putMoveReason(moveReasonRequestDto);
    }

    /**
     * @Desc 회원탈퇴 사유 조회
     * @return List<CodeInfoVo>
     */
    protected List<CodeInfoVo> getMoveReasonCode() {
        return userMoveReasonMapper.getMoveReasonCode();
    }

}
