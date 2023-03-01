package kr.co.pulmuone.v1.comm.mapper.user.login;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeWelcomeLoginMapper {

    String getWelcomeLoginToken(String employeeNumber);
    int addWelcomeLoginToken(String employeeNumber, String urWcidCd);

}
