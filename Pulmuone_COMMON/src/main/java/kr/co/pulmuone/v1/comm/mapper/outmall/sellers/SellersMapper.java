package kr.co.pulmuone.v1.comm.mapper.outmall.sellers;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersCodeListDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersExcelDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersListRequestDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersRequestDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersListVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SellersMapper {


	Page<SellersListVo> getSellersList(SellersListRequestDto sellersListRequestDto);

    List<SellersExcelDto> getSellersExcelList(SellersListRequestDto sellersListRequestDto);

    List<SellersCodeListDto> getSellersGroupList(SellersListRequestDto sellersListRequestDto);

    SellersVo getSellers(SellersRequestDto sellersRequestDto);

    long addSellers(SellersRequestDto sellersRequestDto);

    long putSellers(SellersRequestDto sellersRequestDto);

	int getApplyOmBasicFeeListCount(long omSellersId);

	List<OmBasicFeeListDto> getApplyOmBasicFeeList(long omSellersId);

    long putErpInterfaceStatusChg(SellersRequestDto sellersRequestDto);
}
