package kr.co.pulmuone.v1.system.log.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.system.log.IllegalDetectLogMapper;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogResponseDto;
import kr.co.pulmuone.v1.system.log.dto.vo.IllegalDetectLogResultVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IllegalDetectLogService {

    @Autowired
    private final IllegalDetectLogMapper mapper;

    /**
     * @Desc 부정거래 탐지 목록
     * @param illegalDetectLogRequestDto
     * @return
     * @return IllegalDetectLogResponseDto
     */
    protected IllegalDetectLogResponseDto getIllegalDetectLogList(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {
        IllegalDetectLogResponseDto result = new IllegalDetectLogResponseDto();

        PageMethod.startPage(illegalDetectLogRequestDto.getPage(), illegalDetectLogRequestDto.getPageSize());
        Page<IllegalDetectLogResultVo> rows = mapper.getIllegalDetectLogList(illegalDetectLogRequestDto);

        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());

        return result;
    }


    /**
     * @Desc 부정거래 탐지 상세조회
     * @param illegalDetectLogRequestDto
     * @return IllegalDetectLogResultVo
     */
    protected IllegalDetectLogResultVo getIllegalDetectLogDetail(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception{
        return mapper.getIllegalDetectLogDetail(illegalDetectLogRequestDto.getStIllegalLogId());
    }



    /**
     * @Desc 부정거래 탐지 내용 수정
     * @param illegalDetectLogRequestDto
     * @throws Exception
     * @return int
     */
    protected int putIllegalDetectDetailInfo(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception{
        return mapper.putIllegalDetectDetailInfo(illegalDetectLogRequestDto);
    }




    /**
     * @Desc 부정거래 탐지 리스트 엑셀 다운로드 목록 조회
     * @param illegalDetectLogRequestDto : 부정거래 탐지 검색 조건 request dto
     * @return List<IllegalDetectLogResultVo> : 부정거래 탐지 엑셀 다운로드 목록
     */
    public List<IllegalDetectLogResultVo> illegalDetectListExportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        List<IllegalDetectLogResultVo> itemList = mapper.illegalDetectListExportExcel(illegalDetectLogRequestDto);

        return itemList;
    }


    /**
     * @Desc 부정거래 탐지 회원ID 엑셀 다운로드 목록 조회
     * @param illegalDetectLogRequestDto : 부정거래 탐지 검색 조건 request dto
     * @return List<IllegalDetectLogResultVo> : 부정거래 탐지 엑셀 다운로드 목록
     */
    public List<IllegalDetectLogResultVo> illegalDetectUserIdxportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        List<IllegalDetectLogResultVo> itemList = mapper.illegalDetectUserIdxportExcel(illegalDetectLogRequestDto);

        return itemList;
    }

    /**
     * @Desc 부정거래 탐지 주문번호 엑셀 다운로드 목록 조회
     * @param illegalDetectLogRequestDto : 부정거래 탐지 검색 조건 request dto
     * @return List<IllegalDetectLogResultVo> : 부정거래 탐지 엑셀 다운로드 목록
     */
    public List<IllegalDetectLogResultVo> illegalDetectOrderExportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        List<IllegalDetectLogResultVo> itemList = mapper.illegalDetectOrderExportExcel(illegalDetectLogRequestDto);

        return itemList;
    }

}
