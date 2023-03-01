package kr.co.pulmuone.v1.user.brand.dto.vo;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DisplayBrandResultVo")
public class DisplayBrandResultVo {

	/*------------------------------------------------------
    ---- 브랜드 정보
    ------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 코드")
    private String dpBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "전시 브랜드 이름")
    private String dpBrandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드관운영여부")
    private String brandPavilionYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "등록일자")
    private String createDate;



    /*----------------------------------------------------------------
    -- SEQ
    ----------------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainLogo Seq")
    private String seqPcMain;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainOverLogo Seq")
    private String seqPcMainOver;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainLogo Seq")
    private String seqMobileMain;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainOverLogo Seq")
    private String seqMobileMainOver;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 PC Seq")
    private String seqTitleBannerPc;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 Mobile Seq")
    private String seqTitleBannerMobile;

    /*----------------------------------------------------------------
    -- 물리적인 파일명
    ----------------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainLogo 물리파일명")
    private String physicalFileNamePcMain;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainOverLogo 물리파일명")
    private String physicalFileNamePcMainOver;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainLogo 물리파일명")
    private String physicalFileNameMobileMain;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainOverLogo 물리파일명")
    private String physicalFileNameMobileMainOver;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 PC 물리파일명")
    private String physicalFileNameTitleBannerPc;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 Mobile 물리파일명")
    private String physicalFileNameTitleBannerMobile;


    /*----------------------------------------------------------------
    -- 물리적인 파일 원본 이름
    ----------------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainLogo 파일명")
    private String originNamePcMain;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainOverLogo 파일명")
    private String originNamePcMainOver;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainLogo 파일명")
    private String originNameMobileMain;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainOverLogo 파일명")
    private String originNameMobileMainOver;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 PC 파일명")
    private String originNameTitleBannerPc;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 Mobile 파일명")
    private String originNameTitleBannerMobile;



    /*----------------------------------------------------------------
    -- 물리적인 파일 중간 경로
    ----------------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "물리적인 파일 중간 경로")
    private String subPath;


    /*----------------------------------------------------------------
    -- image url
    ----------------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainLogo 이미지 URL")
    private String logoUrlPcMainLogo;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 PC MainOverLogo 이미지 URL")
    private String logoUrlPcMainOverLogo;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainLogo 이미지 URL")
    private String logoUrlMobileMainLogo;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 Mobile MainOverLogo 이미지 URL")
    private String logoUrlMobileMainOverLogo;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 PC 이미지 URL")
    private String logoUrlTitleBannerPc;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "타이틀 배너 Mobile 이미지 URL")
    private String logoUrlTitleBannerMobile;

    /*------------------------------------------------------
    ---- 브랜드 Logo 정보 - 종료
    ------------------------------------------------------*/

    @ApiModelProperty(value = "전시브랜드 ID 리스트")
	private	List<DisplayBrandResultVo> dpIdList;

}
