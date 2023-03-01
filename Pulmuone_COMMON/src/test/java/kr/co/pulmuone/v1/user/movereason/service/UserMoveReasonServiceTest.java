package kr.co.pulmuone.v1.user.movereason.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;
import kr.co.pulmuone.v1.user.movereason.dto.vo.MoveReasonResultVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
class UserMoveReasonServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserMoveReasonService moveReasonService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 회원탈퇴사유설정_목록조회_성공() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	Page<MoveReasonResultVo> moveReasonResultVo = moveReasonService.getMoveReasonList(moveReasonRequestDto);

    	moveReasonResultVo.stream().forEach(
                i -> log.info(" moveReasonResultVo : {}",  i)
        );

        assertTrue(CollectionUtils.isNotEmpty(moveReasonResultVo));
    }


    @Test
    void 회원탈퇴사유설정_목록조회_실패() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	Page<MoveReasonResultVo> moveReasonResultVo = moveReasonService.getMoveReasonList(moveReasonRequestDto);

    	moveReasonResultVo.stream().forEach(
                i -> log.info(" moveReasonResultVo : {}",  i)
        );


    	assertFalse(CollectionUtils.isEmpty(moveReasonResultVo));
    }

    @Test
    void 회원탈퇴사유설정_상세조회_성공() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setUrMoveReasonId("236");

    	MoveReasonResultVo moveReasonResultVo = moveReasonService.getMoveReason(moveReasonRequestDto);

        log.info(" 회원탈퇴사유설정_상세조회_성공 moveReasonResultVo : {}",  moveReasonResultVo);

        assertEquals("236", moveReasonRequestDto.getUrMoveReasonId());
    }


    @Test
    void 회원탈퇴사유설정_상세조회_실패() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setUrMoveReasonId("99999999999");

    	MoveReasonResultVo moveReasonResultVo = moveReasonService.getMoveReason(moveReasonRequestDto);

        log.info(" 회원탈퇴사유설정_상세조회_성공 moveReasonResultVo : {}",  moveReasonResultVo);

        assertFalse(moveReasonResultVo != null);
    }



    @Test
    void 회원탈퇴사유_중복체크_성공() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setReasonName("동해물과백두산이마르고닳도록");

    	int count = moveReasonService.hasMoveReasonDuplicate(moveReasonRequestDto);

        log.info("회원탈퇴사유_중복체크_성공 count : "+  count);

        assertTrue(count == 0);
    }



    @Test
    void 회원탈퇴사유_중복체크_실패() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setReasonName("탈퇴사유가 꼭 필요한가요");

    	int count = moveReasonService.hasMoveReasonDuplicate(moveReasonRequestDto);

        log.info("회원탈퇴사유_중복체크_실패 count : "+  count);

        assertFalse(count == 0);
    }



    @Test
    void 회원탈퇴사유_등록_성공() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setMoveType("UR_MOVE_TYPE.TYPE_1");
    	moveReasonRequestDto.setReasonName("동해물과백두산이마르고닳도록");
    	moveReasonRequestDto.setUseYn("N");

    	int result = moveReasonService.addMoveReason(moveReasonRequestDto);

        log.info(" 회원탈퇴사유_등록_성공 result : "+  result);

        assertTrue(result > 0);
    }



    @Test
    void 회원탈퇴사유_등록_실패() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setMoveType("UR_MOVE_TYPE.TYPE_1");
    	moveReasonRequestDto.setReasonName("동해물과백두산이마르고닳도록");

        // when, then
        assertThrows(Exception.class, () -> {
        	moveReasonService.addMoveReason(moveReasonRequestDto);
        });
    }


    @Test
    void 회원탈퇴사유_수정처리_성공() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setReasonName("동해물과백두산이마르고닳도록");
    	moveReasonRequestDto.setUseYn("N");
    	moveReasonRequestDto.setUrMoveReasonId("225");

    	int result = moveReasonService.putMoveReason(moveReasonRequestDto);

        log.info(" 재고기한관리_수정처리_성공 result : "+  result);

        assertTrue(result > 0);
    }



    @Test
    void 회원탈퇴사유_수정처리_실패() throws Exception {

    	MoveReasonRequestDto moveReasonRequestDto = new MoveReasonRequestDto();
    	moveReasonRequestDto.setReasonName("동해물과백두산이마르고닳도록");
    	moveReasonRequestDto.setUrMoveReasonId("225");

        // when, then
        assertThrows(Exception.class, () -> {
        	moveReasonService.putMoveReason(moveReasonRequestDto);
        });
    }

    @Test
    void getMoveReasonCode_조회_성공() {
        //given, when
        List<CodeInfoVo> result = moveReasonService.getMoveReasonCode();

        //then
        assertTrue(result.size() > 0);
    }

}