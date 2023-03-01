package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.UserDropRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetUserDropListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.UserDropResultVo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDropMapper {

    Page<GetUserDropListResultVo> getUserDropList(GetUserDropListRequestDto dto) throws Exception;

    void addUserDrop(UserDropRequestDto dto) throws Exception;

    void putUser(Long urUserId) throws Exception;

    void delBuyer(Long urUserId) throws Exception;

    void delShippingAddr(Long urUserId) throws Exception;

    void delRefundBank(Long urUserId) throws Exception;

	UserDropResultVo getUserDropInfo(Long urUserDropId);

}
