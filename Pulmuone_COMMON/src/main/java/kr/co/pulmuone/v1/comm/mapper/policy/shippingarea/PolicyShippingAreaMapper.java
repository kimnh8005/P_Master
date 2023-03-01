package kr.co.pulmuone.v1.comm.mapper.policy.shippingarea;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadListRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadInfoVo;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadFailVo;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadSuccVo;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PolicyShippingAreaMapper {

    // 도서산간/배송불가 권역 관리 엑셀 업로드
    void addShippingAreaExcelInfo(ShippingAreaExcelUploadInfoVo vo);

    // 도서산간/배송불가 권역 관리 엑셀 수정 - 삭제
    void putShippingAreaExcelInfo(ShippingAreaExcelUploadInfoVo vo);

    // 도서산간/배송불가 엑셀 등록
    void addShippingAreaInfo(@Param("psShippingAreaExcelInfoId") Long psShippingAreaExcelInfoId, @Param("voList") List<ShippingAreaExcelUploadSuccVo> voList);

    // 도서산간/배송불가 엑셀업로드 실패 등록
    void addShippingAreaExcelFail(@Param("psShippingAreaExcelInfoId") Long psShippingAreaExcelInfoId, @Param("voList") List<ShippingAreaExcelUploadFailVo> voList);

    // 엑셀 업로드 내역 조회
    ShippingAreaExcelUploadInfoVo getUploadShippingAreaExcelInfo(ShippingAreaExcelUploadDto dto);

    // 엑셀 적용 내역 조회
    Page<ShippingAreaExcelUploadInfoVo> getShippingAreaExcelInfoList(ShippingAreaExcelUploadListRequestDto dto);

    // 도서산간/배송불가 권역별 우편번호 조회
    Page<ShippingAreaExcelUploadInfoVo> getShippingAreaInfoList(ShippingAreaExcelUploadListRequestDto dto);

    // 도서산간/배송불가 엑셀 적용 내역 다운로드
    List<ShippingAreaExcelUploadInfoVo> getShippingAreaInfoExcelDownload(ShippingAreaExcelUploadFailRequestDto dto);

    // 업데이트 실패내역 조회
    List<ShippingAreaExcelUploadFailVo> getShippingAreaUpdateFailExcelDownload(ShippingAreaExcelUploadFailRequestDto dto);

    // 삭제 이전 키 조회
    Long getShippingAreaExcelInfoId(ShippingAreaExcelUploadFailRequestDto dto);

    // 도서산간/배송불가 삭제
    int delShippingAreaInfo(ShippingAreaExcelUploadFailRequestDto dto);

    // 도서산간/배송불가 업로드 내역 삭제
    int delShippingAreaExcelInfo(ShippingAreaExcelUploadFailRequestDto dto);

    // 도서산간/배송불가 업로드 실패 내역 삭제
    int delShippingAreaExcelFail(ShippingAreaExcelUploadFailRequestDto dto);

    // 등록된 키워드
    int getShippingAreaKeyword(@Param("keyword") String keyword);

    // 등록된 우편번호 조회
    int getShippingAreaZipCd(@Param("zipCd") String zipCd, @Param("undeliverableTp") String undeliverableTp);
}
