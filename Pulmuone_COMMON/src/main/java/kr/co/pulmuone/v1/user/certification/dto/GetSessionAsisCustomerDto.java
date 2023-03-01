package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "세션에 저장할 AS-IS회원 정보 DTO ")
public class GetSessionAsisCustomerDto {

  // as-is회원 기존 아이디
  private String asisLoginId;

  //as-is회원 고객번호
  private String asisCustomerNumber;

  // as-is회원 배송지 우편번호
  private String asisReceiverZipCd;

  // as-is회원 배송지 주소1
  private String asisReceiverAddr1;

  // as-is회원 배송지 주소2
  private String asisReceiverAddr2;

  // as-is회원 배송지 건물 관리 번호
  private String asisBuildingCode;

}
