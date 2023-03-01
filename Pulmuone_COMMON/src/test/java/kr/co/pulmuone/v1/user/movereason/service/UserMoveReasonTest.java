package kr.co.pulmuone.v1.user.movereason.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;
import kr.co.pulmuone.v1.user.movereason.dto.vo.MoveReasonResultVo;


class UserMoveReasonTest extends CommonServiceTestBaseForJunit5{

		@Autowired
	    private UserMoveReasonService userMoveReasonService;

	    @BeforeEach
	    void setUp() {
	        preLogin();
	    }

	    @Test
	    void 회원탈퇴사유_목록_조회() {

	        MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
	        List<MoveReasonResultVo> moveReasonList = userMoveReasonService.getMoveReasonList(moveReasonRequestDto);

	        assertTrue(CollectionUtils.isNotEmpty(moveReasonList));
	    }

	    @Test
	    void 회원탈퇴_사유_설정_상세조회_성공() throws Exception {

	        MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
	        moveReasonRequestDto.setUrMoveReasonId("10");
	        MoveReasonResultVo moveReasonResultVo = userMoveReasonService.getMoveReason(moveReasonRequestDto);

	        assertEquals("10", moveReasonResultVo.getUrMoveReasonId());;
	    }

	    @Test
        void 회원탈퇴_사유_설정_상세조회_실패() throws Exception {

            MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
            moveReasonRequestDto.setUrMoveReasonId("9999999999");
            MoveReasonResultVo moveReasonResultVo = userMoveReasonService.getMoveReason(moveReasonRequestDto);

            assertFalse(moveReasonResultVo != null);
        }


	    @Test
	    void 회원탈퇴사유_설정_등록() {

	        MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
	        moveReasonRequestDto.setMoveType("UR_MOVE_TYPE.TYPE_1");
            moveReasonRequestDto.setReasonName("테스트");
            moveReasonRequestDto.setUseYn("Y");

            int count = userMoveReasonService.addMoveReason(moveReasonRequestDto);

            assertTrue(count > 0);
	    }

	    @Test
        void 회원탈퇴사유_설정_수정() {

            MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
            moveReasonRequestDto.setReasonName("테스트");
            moveReasonRequestDto.setUseYn("Y");
            moveReasonRequestDto.setUrMoveReasonId("10");

            int count = userMoveReasonService.putMoveReason(moveReasonRequestDto);

            assertTrue(count > 0);
        }


	    @Test
	    void 회원탈퇴사유_설정_중복_체크() {

	        MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
	        moveReasonRequestDto.setUrMoveReasonId("10");
	        moveReasonRequestDto.setReasonName("탈퇴");

	        int count = userMoveReasonService.hasMoveReasonDuplicate(moveReasonRequestDto);

	        assertTrue(count > 0);
	    };

	}
