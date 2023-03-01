package kr.co.pulmuone.v1.user.buyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserCIBizImpl implements DeleteUserCIBiz {

    @Autowired
    private DeleteUserCIService deleteUserCIService;

    @Override
    public int resetUserCi(String urUserId) throws Exception {
        return deleteUserCIService.resetUserCi(urUserId);
    }
}
