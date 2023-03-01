package kr.co.pulmuone.v1.outmall.sellers.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.outmall.sellers.SellersMapper;
import kr.co.pulmuone.v1.outmall.sellers.dto.*;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersListVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellersService {

    private final SellersMapper sellersMapper;

    public SellersListResponseDto getSellersList(SellersListRequestDto sellersListRequestDto) {
        PageMethod.startPage(sellersListRequestDto.getPage(), sellersListRequestDto.getPageSize());
        Page<SellersListVo> rows = sellersMapper.getSellersList(sellersListRequestDto); // rows

        // 외부몰별 공급업체 수 조회
        for(SellersListVo vo : rows) {
        	int cnt = sellersMapper.getApplyOmBasicFeeListCount(vo.getOmSellersId());
        	vo.setSellersSupplierCount(cnt);
        }

        return SellersListResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
    }


    public List<SellersExcelDto> getSellersExcelList(SellersListRequestDto sellersListRequestDto){
        return sellersMapper.getSellersExcelList(sellersListRequestDto);
    }

    public SellersCodeListResponseDto getSellersGroupList(SellersListRequestDto sellersListRequestDto) {
        List<SellersCodeListDto> rows = sellersMapper.getSellersGroupList(sellersListRequestDto); // rows
        return SellersCodeListResponseDto.builder().total(rows.size()).rows(rows).build();
    }

    public SellersVo getSellers(SellersRequestDto sellersRequestDto) {
        return sellersMapper.getSellers(sellersRequestDto);
    }

    public long addSellers(SellersRequestDto sellersRequestDto) {
        return sellersMapper.addSellers(sellersRequestDto);
    }

    public long putSellers(SellersRequestDto sellersRequestDto) {
        return sellersMapper.putSellers(sellersRequestDto);
    }

    public List<OmBasicFeeListDto> getApplyOmBasicFeeList(long omSellersId) {
        return sellersMapper.getApplyOmBasicFeeList(omSellersId);
    }

    public long putErpInterfaceStatusChg(SellersRequestDto sellersRequestDto) {
        return sellersMapper.putErpInterfaceStatusChg(sellersRequestDto);
    }
}
