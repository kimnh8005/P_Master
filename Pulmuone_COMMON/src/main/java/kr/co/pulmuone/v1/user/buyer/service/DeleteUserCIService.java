package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.mapper.user.buyer.DeleteUserCIMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserCIService {

    @Autowired
    private DeleteUserCIMapper deleteUserCIMapper;

    protected int resetUserCi(String urUserId) throws Exception {

        return deleteUserCIMapper.resetUserCi(urUserId);
    }
}
