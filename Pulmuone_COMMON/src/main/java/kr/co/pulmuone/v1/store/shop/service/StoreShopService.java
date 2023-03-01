package kr.co.pulmuone.v1.store.shop.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.mapper.store.shop.StoreShopMapper;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.dto.ShopListResponseDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.PickUpShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreShopService {

    private final StoreShopMapper storeShopMapper;

    /**
     * 시 / 도 리스트 조회
     *
     * @return List<CodeInfoVo>
     */
    protected List<CodeInfoVo> getAreaTypeList() {
        List<CodeInfoVo> responseList = new ArrayList<>();
        StoreEnums.StoreAreaType[] areaTypeArray = StoreEnums.StoreAreaType.values();

        for (StoreEnums.StoreAreaType item : areaTypeArray) {
            CodeInfoVo vo = new CodeInfoVo();
            vo.setCode(item.getCode());
            vo.setName(item.getMessage());
            responseList.add(vo);
        }

        return responseList;
    }

    /**
     * 매장 리스트 조회
     *
     * @param dto ShopListRequestDto
     * @return ShopListResponseDto
     */
    protected ShopListResponseDto getShopList(ShopListRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<ShopListVo> result = storeShopMapper.getShopList(dto);

        return ShopListResponseDto.builder()
                .total((int) result.getTotal())
                .list(result.getResult())
                .build();
    }

    /**
     * 매장 조회
     *
     * @param urStoreId Long
     * @return ShopVo
     */
    protected ShopVo getShop(String urStoreId) {
        String deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode();
        return storeShopMapper.getShop(urStoreId, deviceType);
    }

    /**
     * 픽업 가능 매장
     *
     * @return List<PickUpShopListVo>
     */
    protected List<PickUpShopListVo> getPickUpShopList() {
        return storeShopMapper.getPickUpShopList();
    }

}