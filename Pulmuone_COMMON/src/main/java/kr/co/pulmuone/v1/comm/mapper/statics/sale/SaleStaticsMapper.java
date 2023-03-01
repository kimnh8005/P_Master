package kr.co.pulmuone.v1.comm.mapper.statics.sale;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsRequestDto;
import kr.co.pulmuone.v1.statics.sale.dto.vo.SaleStaticsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleStaticsMapper {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 판매현황 통계
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  List<SaleStaticsVo> selectSaleStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품별 판매현황 통계
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  List<SaleStaticsVo> selectSaleGoodsStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전체건수
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  int selectTotalCount () throws BaseException;

  int confirmPackage (String packageId) throws BaseException;

  List<SaleStaticsVo> getPakageGoodsInfo (String packageId) throws BaseException;
}
