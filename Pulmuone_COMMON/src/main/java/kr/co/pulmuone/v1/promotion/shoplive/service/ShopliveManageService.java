package kr.co.pulmuone.v1.promotion.shoplive.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.promotion.shoplive.ShopliveManageMapper;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopliveManageService {

    private final ShopliveManageMapper shopliveManageMapper;

    public List<ShopliveInfoVo> selectShopliveList(ShopliveRequestDto shopliveRequestDto) throws Exception {
        return shopliveManageMapper.selectShopliveList(shopliveRequestDto);
    }

    public ShopliveInfoVo selectShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        return shopliveManageMapper.selectShopliveInfo(shopliveRequestDto);
    }

    public void addShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        shopliveManageMapper.addShopliveInfo(shopliveRequestDto);
    }

    public void putShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        shopliveManageMapper.putShopliveInfo(shopliveRequestDto);
    }

    public void delShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        shopliveManageMapper.delShopliveInfo(shopliveRequestDto);
    }
}
