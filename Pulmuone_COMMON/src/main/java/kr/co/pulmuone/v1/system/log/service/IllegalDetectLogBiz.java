package kr.co.pulmuone.v1.system.log.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogRequestDto;

public interface IllegalDetectLogBiz {

    /** 부정거래 탐지 목록 */
    ApiResult<?> getIllegalDetectLogList(IllegalDetectLogRequestDto illegalDetectLogRequestDto);

    /** 부정거래 탐지 상태변경 */
    ApiResult<?> putCompleteRequest(IllegalDetectLogRequestDto requestDto) throws Exception;

    /** 부정거래 탐지 상세조회 */
    ApiResult<?> getIllegalDetectLogDetail(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception;

    /** 부정거래 탐지 내용수정 */
    ApiResult<?> putIllegalDetectDetailInfo(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception;

    /** 부정거래 탐지 목록조회 엑셀 다운로드 */
    ExcelDownloadDto illegalDetectListExportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto);

    /** 부정거래 탐지 회원ID 엑셀 다운로드 */
    ExcelDownloadDto illegalDetectUserIdxportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto);

    /** 부정거래 탐지 주문번호 엑셀 다운로드 */
    ExcelDownloadDto illegalDetectOrderExportExcel(IllegalDetectLogRequestDto illegalDetectLogRequestDto);

}
