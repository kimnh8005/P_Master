package kr.co.pulmuone.v1.customer.qna.service;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;
import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import kr.co.pulmuone.v1.api.ecs.service.EcsBiz;
import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums.EnumSiteType;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.ImageUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaOrderInfoByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaInfoByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaListByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaListByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaByUserAttcVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaOrderInfoByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaResultVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaListByGoodsVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaInfoByUserVo;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsBannedWordBiz;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerQnaBizImpl implements CustomerQnaBiz {

    @Autowired
    private CustomerQnaService customerQnaService;

    @Autowired
    private EcsBiz ecsBiz;

    @Autowired
    private ComnBizImpl comnBizImpl;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    @Autowired
    private PolicyBbsBannedWordBiz policyBbsBannedWordBiz;


    /**
     * 1:1문의 현황 조회
     *
     * @param dto QnaInfoByUserRequestDto
     * @return QnaInfoByUserVo
     * @throws Exception exception
     */
    @Override
    public QnaInfoByUserVo getQnaInfoByUser(QnaInfoByUserRequestDto dto) throws Exception {
        return customerQnaService.getQnaInfoByUser(dto);
    }

    /**
     * 1:1문의 목록 조회
     *
     * @param dto QnaListByUserRequestDto
     * @return QnaListByUserResponseDto
     * @throws Exception exception
     */
    @Override
    public QnaListByUserResponseDto getQnaListByUser(QnaListByUserRequestDto dto) throws Exception {
        return customerQnaService.getQnaListByUser(dto);
    }

    /**
     * 상품문의 목록 조회
     *
     * @param dto ProductQnaListByUserRequestDto
     * @return ProductQnaListByUserResponseDto
     * @throws Exception exception
     */
    @Override
    public ProductQnaListByUserResponseDto getProductQnaListByUser(ProductQnaListByUserRequestDto dto) throws Exception {
        return customerQnaService.getProductQnaListByUser(dto);
    }

    @Override
    public void putQnaAnswerUserCheckYn(Long csQnaId) throws Exception {
        customerQnaService.putQnaAnswerUserCheckYn(csQnaId);
    }

    /**
     * 상품문의 비공개 처리 - 고객
     *
     * @param csQnaId  Long
     * @param urUserId Long
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @Override
    public ApiResult<?> putProductQnaSetSecretByUser(Long csQnaId, Long urUserId) throws Exception {
        ProductQnaVo productQnaVo = customerQnaService.getProductQnaByGoods(csQnaId);
        if (productQnaVo == null) {
            return ApiResult.result(QnaEnums.PutQnaValidation.NOT_QNA);
        }
        if (!urUserId.equals(productQnaVo.getUrUserId())) {
            return ApiResult.result(QnaEnums.PutQnaValidation.USER_CECHK_FAIL);
        }

        customerQnaService.putProductQnaSetSecretByUser(csQnaId);
        return ApiResult.success();
    }

    /**
     * 상품상세 - 상품문의 목록 조회
     *
     * @param dto ProductQnaListByGoodsRequestDto
     * @return ProductQnaListByGoodsResponseDto
     * @throws Exception exception
     */
    @Override
    public ProductQnaListByGoodsResponseDto getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception {
        return customerQnaService.getProductQnaListByGoods(dto);
    }


    /**
     * 상품상세 - 상품 문의 작성
     *
     * @param dto ProductQnaRequestDto
     * @throws Exception exception
     */
    @Override
    public ApiResult<?> addProductQna(ProductQnaRequestDto dto) throws Exception {
        //금칙어 - 마스킹 하여 저장
        dto.setTitle(policyBbsBannedWordBiz.filterSpamWord(dto.getTitle(), BaseEnums.EnumSiteType.MALL));
        dto.setQuestion(policyBbsBannedWordBiz.filterSpamWord(dto.getQuestion(), BaseEnums.EnumSiteType.MALL));

        //ECS 분류값 조회
        CsEcsCodeVo csEcsCode = ecsBiz.getEcsCode(dto.getProductType(), dto.getQuestion(), dto.getGoodsName());
        dto.setHdBcode(csEcsCode.getHdBcode());
        dto.setHdScode(csEcsCode.getHdScode());
        dto.setClaimGubun(csEcsCode.getClaimGubun());

        //상품 문의 등록
        ProductQnaListByGoodsVo result = customerQnaService.addProductQna(dto);

        //ECS연동 문의 등록 파라미터 세팅
        String userMobile = dto.getUserMobile().replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
        String customerPhonearea = userMobile.split("-")[0];
        String customerPhonefirst = userMobile.split("-")[1];
        String customerPhonesecond = userMobile.split("-")[2];

        String boardSeq = ecsBiz.getEcsBoardSeq(QnaEnums.QnaType.PRODUCT.getCode(), dto.getUrUserId(), dto.getCsQnaId(), dto.getIlGoodsId());
        String counselDesc = StringUtil.htmlSingToText("제목:" + dto.getTitle() + "\n상품명:" + result.getGoodsName() + "[ERP CD:" + dto.getIlGoodsId() + "]\n내용:" + dto.getQuestion());

        QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
                .receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
                .boardDiv("상품Q&A")
                .boardSeq(boardSeq)
                .customerNum(String.valueOf(dto.getUrUserId()))
                .customerName(dto.getUserName())
                .customerPhonearea(customerPhonearea)
                .customerPhonefirst(customerPhonefirst)
                .customerPhonesecond(customerPhonesecond)
                .customerEmail(dto.getUserEmail())
                .hdBcode(csEcsCode.getHdBcode())
                .hdScode(csEcsCode.getHdScode())
                .claimGubun(csEcsCode.getClaimGubun())
                .counselDesc(counselDesc)
                .secCode(Constants.SEC_CODE)
                .build();

        //ECS 연동 문의 등록
        try {
            ecsBiz.addQnaToEcs(qnaToEcsVo);
        } catch (Exception e) {
            return ApiResult.result(QnaEnums.AddQnaValidation.INTERFACE_ERROR);
        }

        return ApiResult.success(result);
    }

    /**
     * 상품상세 - 상품문의 상세 조회
     *
     * @param csQnaId Long
     * @return ProductQnaVo
     * @throws Exception exception
     */
    @Override
    public ProductQnaVo getProductQnaByGoods(Long csQnaId) throws Exception {
        return customerQnaService.getProductQnaByGoods(csQnaId);
    }

    /**
     * 상품상세 - 상품 문의 수정
     *
     * @param vo ProductQnaVo
     * @throws Exception exception
     */
    @Override
    public ApiResult<?> putProductQna(ProductQnaVo vo) throws Exception {
        ProductQnaVo productQnaVo = customerQnaService.getProductQnaByGoods(vo.getCsQnaId());
        if (productQnaVo == null) {
            return ApiResult.result(QnaEnums.PutQnaValidation.NOT_QNA);
        }
        if (!vo.getUrUserId().equals(productQnaVo.getUrUserId())) {
            return ApiResult.result(QnaEnums.PutQnaValidation.USER_CECHK_FAIL);
        }
        if (!productQnaVo.getStatus().equals(QnaEnums.QnaStatus.RECEPTION.getCode())) {

            if (productQnaVo.getStatus().equals(QnaEnums.QnaStatus.ANSWER_COMPLETED.getCode())) {
                return ApiResult.result(QnaEnums.PutQnaValidation.ANSWER_COMPLETED);
            }
            return ApiResult.result(QnaEnums.PutQnaValidation.ANSWER_CHECKING);
        }

        //금칙어 - 마스킹 하여 저장
        vo.setTitle(policyBbsBannedWordBiz.filterSpamWord(vo.getTitle(), BaseEnums.EnumSiteType.MALL));
        vo.setQuestion(policyBbsBannedWordBiz.filterSpamWord(vo.getQuestion(), BaseEnums.EnumSiteType.MALL));

        //ECS 분류값 조회
        CsEcsCodeVo csEcsCode = ecsBiz.getEcsCode(vo.getProductType(), vo.getQuestion(), vo.getGoodsName());
        vo.setHdBcode(csEcsCode.getHdBcode());
        vo.setHdScode(csEcsCode.getHdScode());
        vo.setClaimGubun(csEcsCode.getClaimGubun());

        //상품 문의 수정
        customerQnaService.putProductQna(vo);

        //ECS연동 문의 수정 파라미터 세팅
        String boardSeq = ecsBiz.getEcsBoardSeq(QnaEnums.QnaType.PRODUCT.getCode(), productQnaVo.getUrUserId(), productQnaVo.getCsQnaId(), productQnaVo.getIlGoodsId());
        String counselDesc = StringUtil.htmlSingToText("제목:" + vo.getTitle() + "\n상품명:" + productQnaVo.getGoodsName() + "[ERP CD:" + productQnaVo.getIlGoodsId() + "]\n내용:" + vo.getQuestion());

        QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
                .receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
                .boardSeq(boardSeq)
                .hdBcode(csEcsCode.getHdBcode())
                .hdScode(csEcsCode.getHdScode())
                .claimGubun(csEcsCode.getClaimGubun())
                .counselDesc(counselDesc)
                .build();

        // ECS 연동 문의 수정
        try {
            ecsBiz.putQnaToEcs(qnaToEcsVo);
        } catch (Exception e) {
            return ApiResult.result(QnaEnums.AddQnaValidation.INTERFACE_ERROR);
        }

        return ApiResult.success();

    }

    /**
     * @param onetooneQnaByUserRequestDto
     * @throws Exception
     * @Desc 1:1 문의 등록
     */
    @Override
    public ApiResult<?> addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception {
        //금칙어 - 마스킹 하여 저장
        onetooneQnaByUserRequestDto.setTitle(policyBbsBannedWordBiz.filterSpamWord(onetooneQnaByUserRequestDto.getTitle(), BaseEnums.EnumSiteType.MALL));
        onetooneQnaByUserRequestDto.setQuestion(policyBbsBannedWordBiz.filterSpamWord(onetooneQnaByUserRequestDto.getQuestion(), BaseEnums.EnumSiteType.MALL));

        //ECS 분류값 조회
        CsEcsCodeVo csEcsCode = ecsBiz.getEcsCode(onetooneQnaByUserRequestDto.getOnetooneType(), onetooneQnaByUserRequestDto.getQuestion(), null);
        if (csEcsCode == null) return ApiResult.result(QnaEnums.AddQnaValidation.INTERFACE_ERROR);
        onetooneQnaByUserRequestDto.setHdBcode(csEcsCode.getHdBcode());
        onetooneQnaByUserRequestDto.setHdScode(csEcsCode.getHdScode());
        onetooneQnaByUserRequestDto.setClaimGubun(csEcsCode.getClaimGubun());

        //1:1문의 등록
        int addQnaByUserInt = customerQnaService.addQnaByUser(onetooneQnaByUserRequestDto);

        if (addQnaByUserInt > 0 && onetooneQnaByUserRequestDto.getImage() != null) {
            addQnaImage(onetooneQnaByUserRequestDto); // 문의 이미지 등록
        }

        //ECS연동 문의 등록 파라미터 세팅
        String userMobile = onetooneQnaByUserRequestDto.getUserMobile().replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
        String customerPhonearea = userMobile.split("-")[0];
        String customerPhonefirst = userMobile.split("-")[1];
        String customerPhonesecond = userMobile.split("-")[2];

        String boardSeq = ecsBiz.getEcsBoardSeq(QnaEnums.QnaType.ONETOONE.getCode(), onetooneQnaByUserRequestDto.getUrUserId(), onetooneQnaByUserRequestDto.getCsQnaId(), null);
        String counselDesc = StringUtil.htmlSingToText("제목:" + onetooneQnaByUserRequestDto.getTitle() + "\n내용:" + onetooneQnaByUserRequestDto.getQuestion());

        QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
                .receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
                .boardDiv("1:1문의")
                .boardSeq(boardSeq)
                .customerNum(String.valueOf(onetooneQnaByUserRequestDto.getUrUserId()))
                .customerName(onetooneQnaByUserRequestDto.getUserName())
                .customerPhonearea(customerPhonearea)
                .customerPhonefirst(customerPhonefirst)
                .customerPhonesecond(customerPhonesecond)
                .customerEmail(onetooneQnaByUserRequestDto.getUserEmail())
                .hdBcode(csEcsCode.getHdBcode())
                .hdScode(csEcsCode.getHdScode())
                .claimGubun(csEcsCode.getClaimGubun())
                .counselDesc(counselDesc)
                .secCode(Constants.SEC_CODE)
                .build();

        //ECS 연동 문의 등록
        try {
            ecsBiz.addQnaToEcs(qnaToEcsVo);
        } catch (Exception e) {
            return ApiResult.result(QnaEnums.AddQnaValidation.INTERFACE_ERROR);
        }

        //1:1문의 완료 자동메일/SMS 발송
        OnetooneQnaResultVo onetooneQnaResultVo = getOnetooneQnaAddInfo(String.valueOf(onetooneQnaByUserRequestDto.getUrUserId()));
        getOnetooneQnaAddCompleted(onetooneQnaResultVo);
        return ApiResult.success(addQnaByUserInt);
    }

    /**
     * @param urUserId
     * @Desc 1:1문의 완료 시 정보조회
     */
    @Override
    public OnetooneQnaResultVo getOnetooneQnaAddInfo(String urUserId) {

        return customerQnaService.getOnetooneQnaAddInfo(urUserId);
    }

    /**
     * @param onetooneQnaResultVo
     * @Desc 1:1문의 접수완료 이메일/SMS 전송
     */
    @Override
    public void getOnetooneQnaAddCompleted(OnetooneQnaResultVo onetooneQnaResultVo) {

        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ONETOONE_QNA_ADD_COMPLETED.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

        //이메일 발송
        if ("Y".equals(getEmailSendResultVo.getMailSendYn())) {
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getOnetooneQnaAddEmailTmplt?urUserId=" + onetooneQnaResultVo.getUrUserId();
            String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                    .reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(String.valueOf(onetooneQnaResultVo.getUrUserId()))
                    .mail(onetooneQnaResultVo.getMail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 발송
        if ("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, onetooneQnaResultVo);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId(String.valueOf(onetooneQnaResultVo.getUrUserId()))
                    .mobile(onetooneQnaResultVo.getMobile())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

        }
    }

    /**
     * @param onetooneQnaByUserRequestDto
     * @Desc 1:1 문의 이미지 등록
     */
    @Override
    public void addQnaImage(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) {

        List<OnetooneQnaByUserAttcVo> onetooneImageUploadResultList = onetooneQnaByUserRequestDto.getImage();
        String serverSubPath = onetooneQnaByUserRequestDto.getServerSubPath().replaceAll("\\\\", "/");
        if (onetooneImageUploadResultList == null)
            return;
        for (OnetooneQnaByUserAttcVo onetooneQnaByUserAttcVo : onetooneImageUploadResultList) {
            onetooneQnaByUserAttcVo.setCsQnaId(onetooneQnaByUserRequestDto.getCsQnaId());
            onetooneQnaByUserAttcVo.setImageName(onetooneQnaByUserAttcVo.getImageName()); // 물리적 파일명

            //가로 세로 길이에 따른 리사이징
            String filePath = onetooneQnaByUserRequestDto.getPublicRootStoragePath() + serverSubPath; // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
            String originalFileName = onetooneQnaByUserAttcVo.getImageName(); // 원본 파일명
            double ratio;

            //이미지 가로 세로 사이즈 확인
            Image img = new ImageIcon(filePath + originalFileName).getImage();
            int imgWidth = img.getWidth(null); //이미지 가로길이
            int imgHeight = img.getHeight(null); //이미지 세로길이

            int width = 1300; //리사이징할 가로 길이
            int height = 1000; //리사이징할 세로 길이

            // imgOriginNm, imgNm, imgPath set
            if (imgWidth < width && imgHeight < height) { // 리사이징 해당 X
                onetooneQnaByUserAttcVo.setImagePath(serverSubPath + onetooneQnaByUserAttcVo.getImageName());

            } else {
                String imageNamePrefix = "resize_";

                // 리사이징 해당 O
                if (imgWidth > width && imgHeight < height) { // 이미지 가로길이만 1300px 초과 시
                    ratio = (double) width / imgWidth;
                    height = (int) (imgHeight * ratio);

                } else if (imgHeight > height && imgWidth < width) { // 이미지 세로길이만 1000px 초과 시
                    ratio = (double) height / imgHeight;
                    width = (int) (imgWidth * ratio);
                }
                // 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
                ImageUtil.createResizedImage(filePath, originalFileName, imageNamePrefix, width, height);

                onetooneQnaByUserAttcVo.setImagePath(serverSubPath + imageNamePrefix + onetooneQnaByUserAttcVo.getImageName());
            }

            // thumnailOriginNm, thumbnailNm, thumbnailPath set '180X180 리사이징'
            onetooneQnaByUserAttcVo.setThumbnailOriginalName(onetooneQnaByUserAttcVo.getImageOriginalName());
            onetooneQnaByUserAttcVo.setThumbnailName(onetooneQnaByUserAttcVo.getImageName());

            ImageUtil.createResizedImage(filePath, originalFileName, "180X180_", 180, 180);
            onetooneQnaByUserAttcVo.setThumbnailPath(serverSubPath + "180X180_" + onetooneQnaByUserAttcVo.getThumbnailName());

            customerQnaService.addQnaImage(onetooneQnaByUserAttcVo);
        }

    }

    /**
     * @param onetooneQnaByUserRequestDto
     * @return void
     * @throws Exception
     * @Desc 1:1 문의 수정
     */
    @Override
    public ApiResult<?> putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception {
        //금칙어 - 마스킹 하여 저장
        onetooneQnaByUserRequestDto.setTitle(policyBbsBannedWordBiz.filterSpamWord(onetooneQnaByUserRequestDto.getTitle(), BaseEnums.EnumSiteType.MALL));
        onetooneQnaByUserRequestDto.setQuestion(policyBbsBannedWordBiz.filterSpamWord(onetooneQnaByUserRequestDto.getQuestion(), BaseEnums.EnumSiteType.MALL));

        //ECS 분류값 조회
        CsEcsCodeVo csEcsCode = ecsBiz.getEcsCode(onetooneQnaByUserRequestDto.getOnetooneType(), onetooneQnaByUserRequestDto.getQuestion(), null);
        onetooneQnaByUserRequestDto.setHdBcode(csEcsCode.getHdBcode());
        onetooneQnaByUserRequestDto.setHdScode(csEcsCode.getHdScode());
        onetooneQnaByUserRequestDto.setClaimGubun(csEcsCode.getClaimGubun());

        //1:1문의 수정
        int putQnaByUserInt = customerQnaService.putQnaByUser(onetooneQnaByUserRequestDto);

        if (putQnaByUserInt > 0) {
            putQnaImage(onetooneQnaByUserRequestDto);
        }

        //ECS연동 문의 수정 파라미터 세팅
        String boardSeq = ecsBiz.getEcsBoardSeq(QnaEnums.QnaType.ONETOONE.getCode(), onetooneQnaByUserRequestDto.getUrUserId(), onetooneQnaByUserRequestDto.getCsQnaId(), null);
        String counselDesc = StringUtil.htmlSingToText("제목:" + onetooneQnaByUserRequestDto.getTitle() + "\n내용:" + onetooneQnaByUserRequestDto.getQuestion());

        QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
                .receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
                .boardSeq(boardSeq)
                .counselDesc(counselDesc)
                .hdBcode(csEcsCode.getHdBcode())
                .hdScode(csEcsCode.getHdScode())
                .claimGubun(csEcsCode.getClaimGubun())
                .build();

        // ECS 연동 문의 수정
        try {
            ecsBiz.putQnaToEcs(qnaToEcsVo);
        } catch (Exception e) {
            return ApiResult.result(QnaEnums.AddQnaValidation.INTERFACE_ERROR);
        }

        return ApiResult.success(putQnaByUserInt);
    }

    /**
     * @param onetooneQnaByUserRequestDto
     * @Desc 1:1 문의 이미지 수정
     */
    @Override
    public void putQnaImage(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) {

        // 해당 상품코드로 등록된 기존 상품 이미지 목록 조회
        List<OnetooneQnaByUserAttcVo> oldQnaImageList = customerQnaService.qnaImageList(onetooneQnaByUserRequestDto.getCsQnaId());

        // 삭제 대상 상품 이미지 파일들을 일괄 삭제 ( 리사이징된 파일 포함 )
        // 이미지 파일 삭제 후 oldGoodsImageList 내에서 관련 Vo 도 삭제
        removeQnaImage(oldQnaImageList, false, onetooneQnaByUserRequestDto.getPublicRootStoragePath());
        //원본 / 리사이징 품목 이미지 모두 삭제 후 CS_QNA_ATTC 테이블에서 해당 품목 이미지 기존 정보 삭제
        customerQnaService.delQnaImage(onetooneQnaByUserRequestDto.getCsQnaId());

        addQnaImage(onetooneQnaByUserRequestDto);

    }

    /**
     * @param oldQnaImageList
     * @param onetooneImageNameListToDelete
     * @param isAllDeleted
     * @param publicRootStoragePath
     * @Desc 1:1 문의 이미지 삭제 (저장소)
     */
    @Override
    public void removeQnaImage(List<OnetooneQnaByUserAttcVo> oldQnaImageList, boolean isAllDeleted, String publicRootStoragePath) {
        //해당 상품의 삭제 대상 상품 이미지 원본 / 리사이징 파일들을 물리적으로 삭제
        for (int i = 0; i < oldQnaImageList.size(); i++) { // 기존 상품 이미지 목록 반복문 시작

            // 모두 삭제 flag 값 false / 삭제 대상 이미지 파일명 목록에 포함되지 않은 이미지인 경우 continue / 다음 반복문 실행
            if (!isAllDeleted) {
                continue;
            }

            OnetooneQnaByUserAttcVo onetooneQnaByUserAttcVo = oldQnaImageList.get(i);

            String serverSubPath = null;    // 해당 리사이징 이미지 파일의 상대 경로
            String fullFilePath = null;        // 삭제할 파일의 전체 경로 : 물리적 파일명 포함

            serverSubPath = onetooneQnaByUserAttcVo.getImagePath();

            try {
                fullFilePath = publicRootStoragePath + UriUtils.decode(serverSubPath, "UTF-8"); // 리사이징 이미지의 하위 경로 : URI decoding
                Files.delete(FileSystems.getDefault().getPath(fullFilePath));
            } catch (IOException e) {
                // 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
            }

            // 해당 상품 이미지의 원본 파일 삭제
            fullFilePath = publicRootStoragePath + // 상품 상세 이미지가 저장되는 public 저장소의 최상위 저장 디렉토리 경로
                    UriUtils.decode(onetooneQnaByUserAttcVo.getImagePath(), "UTF-8");// 원본 상품 이미지의 하위 경로 : URI decoding

            try {
                Files.delete(FileSystems.getDefault().getPath(fullFilePath));
            } catch (IOException e) {
                // 로직 없음 : 해당 이미지가 존재하지 않는 경우 무시함
            }

            oldQnaImageList.remove(i); // 해당 상품 이미지 파일 모두 삭제 후 관련 Vo 삭제

        } // 기존 문의 이미지 목록 반복문 끝
    }

    /**
     * @param csQnaId
     * @param urUserId
     * @throws Exception
     * @Desc 1:1 문의 상세 조회
     */
    @Override
    public OnetooneQnaByUserResponseDto getQnaDetailByUser(Long csQnaId, Long urUserId) throws Exception {

        OnetooneQnaByUserResponseDto onetooneQnaByUserResponseDto = new OnetooneQnaByUserResponseDto();

        //이미지 조회
        List<OnetooneQnaByUserAttcVo> list = customerQnaService.qnaImageList(csQnaId);

        if (list != null) {
            onetooneQnaByUserResponseDto.setImage(list);
        }

        // 이미지 제외 조회
        OnetooneQnaByUserVo onetooneQnaByUserVo = customerQnaService.getQnaDetailByUser(csQnaId, urUserId);
        onetooneQnaByUserResponseDto.setCsQnaId(onetooneQnaByUserVo.getCsQnaId());
        onetooneQnaByUserResponseDto.setTitle(onetooneQnaByUserVo.getTitle());
        onetooneQnaByUserResponseDto.setQuestion(onetooneQnaByUserVo.getQuestion());
        onetooneQnaByUserResponseDto.setOnetooneType(onetooneQnaByUserVo.getOnetooneType());
        onetooneQnaByUserResponseDto.setOdOrderId(onetooneQnaByUserVo.getOdOrderId());
        onetooneQnaByUserResponseDto.setOdOrderDetlId(onetooneQnaByUserVo.getOdOrderDetlId());
        onetooneQnaByUserResponseDto.setGoodsName(onetooneQnaByUserVo.getGoodsName());
        onetooneQnaByUserResponseDto.setImagePath(onetooneQnaByUserVo.getImagePath());
        onetooneQnaByUserResponseDto.setImageOriginalName(onetooneQnaByUserVo.getImageOriginalName());
        onetooneQnaByUserResponseDto.setAnswerMailYn(onetooneQnaByUserVo.getAnswerMailYn());
        onetooneQnaByUserResponseDto.setAnswerSmsYn(onetooneQnaByUserVo.getAnswerSmsYn());
        onetooneQnaByUserResponseDto.setStatus(onetooneQnaByUserVo.getStatus());

        return onetooneQnaByUserResponseDto;
    }

    /**
     * @param searchPeriod
     * @param urUserId
     * @throws Exception
     * @Desc 1:1 문의 주문조회 팝업조회 (기간 내 조회 / 상세 조회)
     */
    @Override
    public OnetooneQnaOrderInfoByUserResponseDto getOrderInfoPopupByQna(String searchPeriod, Long urUserId) throws Exception {
        OnetooneQnaOrderInfoByUserResponseDto onetooneQnaOrderInfoByUserResponseDto = new OnetooneQnaOrderInfoByUserResponseDto();

        //주문조회 팝업조회 -> 조회기간 검색
        List<OnetooneQnaOrderInfoByUserVo> order = customerQnaService.getOrderInfoPopupByQna(searchPeriod, urUserId);
        for (OnetooneQnaOrderInfoByUserVo vo : order) {
            vo.setOrderDetail(customerQnaService.getOrderDetlInfoPopupByQna(vo.getOdOrderId()));
        }
        onetooneQnaOrderInfoByUserResponseDto.setOrder(order);

        return onetooneQnaOrderInfoByUserResponseDto;
    }

    /**
     * 통합몰문의 관리 목록조회
     *
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception {

        QnaBosResponseDto result = new QnaBosResponseDto();

        if (!qnaBosRequestDto.getFindKeyword().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(qnaBosRequestDto.getFindKeyword(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            qnaBosRequestDto.setFindKeywordArray(array);
        }

        qnaBosRequestDto.setQnaTypeList(customerQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaType(), Constants.ARRAY_SEPARATORS)); // 검색어

        qnaBosRequestDto.setQnaStatusList(customerQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaStatus(), Constants.ARRAY_SEPARATORS)); // 검색어


        Page<QnaBosListVo> voList = customerQnaService.getQnaList(qnaBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }


    /**
     * @param FeedbackBosRequestDto : 후기관리 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     * @Desc 통합몰문의 리스트 엑셀 다운로드 목록 조회
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ExcelDownloadDto qnaExportExcel(QnaBosRequestDto qnaBosRequestDto) {

        String excelFileName = "통합몰문의관리 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                70, 200, 200, 400, 400, 400, 150, 200, 150, 200, 150, 200, 200, 200, 150, 150,
                200, 200, 200, 200, 200 ,200, 200, 200 ,200, 200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "left", "left", "left", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "rowNumber", "qnaTypeName", "qnaDivisionName", "qnaTitle", "qnaQuestion",  "goodsName", "userName", "userId", "dropUserYn", "qnaStatusName", "delayYn", "createDate", "firstAnswerDt", "secondAnswerDt", "firstAnswerUserNm", "secondAnswerUserNm",
                "firstAnswer", "secondAnswer", "userMobile", "userMail", "answerSmsYn", "answerMailYn", "odid", "hdBcodeNm", "hdScodeNm", "claimGubunNm"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "No", "문의유형", "문의분류", "문의제목", "문의내용", "상품명", "회원명", "회원ID", "탈퇴여부", "처리상태", "답변지연여부", "등록일자", "1차 답변 처리날짜", "2차 답변 처리날짜", "1차 답변 담당자", "2차 답변 담당자",
                "답변여부(1차)", "답변여부(2차)", "휴대폰번호", "EMAIL", "답변방법(SMS)", "답변방법(MAIL)", "주문번호", "eCS대분류", "eCS중분류", "eCS소분류"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼


        if (!qnaBosRequestDto.getFindKeyword().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(qnaBosRequestDto.getFindKeyword(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            qnaBosRequestDto.setFindKeywordArray(array);
        }

        qnaBosRequestDto.setQnaStatusList(customerQnaService.getSearchKeyToSearchKeyList(qnaBosRequestDto.getQnaStatus(), Constants.ARRAY_SEPARATORS)); // 검색어

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<QnaBosListVo> itemList = customerQnaService.qnaExportExcel(qnaBosRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }

    /**
     * 통합몰문의 관리 상세조회
     *
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception {
        QnaBosDetailResponseDto result = new QnaBosDetailResponseDto();
        QnaBosDetailVo vo = new QnaBosDetailVo();
        vo = customerQnaService.getQnaDetail(qnaBosRequestDto);

        UserVo userVo = qnaBosRequestDto.getUserVo();
        String answer = "";
        /*
        answer = CustomerEnums.AnswerTempType.ANSWER.getMessage() + " " + vo.getOrganizationNm() + " "
        		+ qnaBosRequestDto.getUserVo().getLoginName() + CustomerEnums.AnswerTempType.ANSWER_CMT.getMessage();
        */
        answer = CustomerEnums.AnswerTempType.ANSWER.getMessage();
        if (vo.getFirstContent() == null) {
            vo.setFirstContent(answer);
        }

        if (vo.getSecondContent() == null) {
            vo.setSecondContent(answer);
        }
        result.setRow(vo);

        return ApiResult.success(result);
    }


    /**
     * 답변진행 상태변경
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> putQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception {
        return customerQnaService.putQnaAnswerStatus(qnaBosRequestDto);
    }


    /**
     * 문의 답변정보 수정
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> putQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception {

        QnaBosDetailVo vo = new QnaBosDetailVo();

        // 답변대기 상태
        if (qnaBosRequestDto.getStatus().equals(QnaEnums.QnaStatus.ANSWER_CHECKING.getCode()) && !qnaBosRequestDto.getFirstContent().isEmpty()) {

            qnaBosRequestDto.setStatus(QnaEnums.QnaStatus.ANSWER_COMPLETED_1ST.getCode());  //1차답변 완료 상태변경

            vo = customerQnaService.getQnaDetail(qnaBosRequestDto);
            if (vo.getStatus().equals(QnaEnums.QnaStatus.ANSWER_COMPLETED_1ST.getCode())) {    //1차답변 상태 확인: 이미 처리된 경우
                return ApiResult.result(QnaEnums.PutQnaValidation.ALREADY_CHECK);
            }

            if (!qnaBosRequestDto.getFirstContent().isEmpty()) {
                qnaBosRequestDto.setContent(policyBbsBannedWordBiz.filterSpamWord(qnaBosRequestDto.getFirstContent(), EnumSiteType.BOS));
                customerQnaService.addQnaAnswer(qnaBosRequestDto);
            }
        } else if (qnaBosRequestDto.getStatus().equals(QnaEnums.QnaStatus.ANSWER_COMPLETED_1ST.getCode()) && !qnaBosRequestDto.getSecondContent().isEmpty()) {

            qnaBosRequestDto.setStatus(QnaEnums.QnaStatus.ANSWER_COMPLETED_2ND.getCode());    //2차답변 완료 상태변경

            vo = customerQnaService.getQnaDetail(qnaBosRequestDto);
            if (vo.getStatus().equals(QnaEnums.QnaStatus.ANSWER_COMPLETED_2ND.getCode())) {    //2차답변 상태 확인: 이미 처리된 경우
                return ApiResult.result(QnaEnums.PutQnaValidation.ALREADY_CHECK);
            }

            if (!qnaBosRequestDto.getSecondContent().isEmpty()) {
                qnaBosRequestDto.setContent(policyBbsBannedWordBiz.filterSpamWord(qnaBosRequestDto.getSecondContent(), EnumSiteType.BOS));
                customerQnaService.addQnaAnswer(qnaBosRequestDto);
            }
        }
        // 공개여부, eCS분류, 처리상태 수정
        ApiResult<?> result = customerQnaService.putQnaInfo(qnaBosRequestDto);

        // 문의 답변 등록 후 자동메일/SMS 발송
        QnaBosDetailVo qnaBosDetailResultVo = getQnaAnswerInfo(qnaBosRequestDto.getCsQnaId());

        // ECS 답변 업데이트
        ecsBiz.putQnaAnswerToEcs(qnaBosRequestDto, qnaBosDetailResultVo);

        //1:1문의
        if (qnaBosDetailResultVo.getQnaType().equals(QnaEnums.QnaType.ONETOONE.getCode())) {
            //메일/SMS답변여부 확인
            if (("Y".equals(qnaBosDetailResultVo.getAnswerMailYn()) || "Y".equals(qnaBosDetailResultVo.getAnswerSmsYn()))) {
                getOnetooneQnaAnsweredCompleted(qnaBosDetailResultVo);
            }
        }
        //상품문의
        else if (qnaBosDetailResultVo.getQnaType().equals(QnaEnums.QnaType.PRODUCT.getCode())) {
            //메일/SMS답변여부 확인
            if (("Y".equals(qnaBosDetailResultVo.getAnswerMailYn()) || "Y".equals(qnaBosDetailResultVo.getAnswerSmsYn()))) {
                getProductQnaAnsweredCompleted(qnaBosDetailResultVo);
            }
        }
        return result;
    }


    /**
     * @param csQnaId
     * @return QnaBosDetailVo
     * @Desc 문의 답변 등록 후 정보 조회
     */
    @Override
    public QnaBosDetailVo getQnaAnswerInfo(String csQnaId) {
        return customerQnaService.getQnaAnswerInfo(csQnaId);
    }

    /**
     * @param qnaBosDetailResultVo
     * @Desc 1:1문의 답변완료 이메일/SMS 전송
     */
    @Override
    public void getOnetooneQnaAnsweredCompleted(QnaBosDetailVo qnaBosDetailResultVo) {
        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ONETOONE_QNA_ANSWER_COMPLETED.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

        //메일 답변여부 - 메일 발송
        if ("Y".equals(qnaBosDetailResultVo.getAnswerMailYn()) && ("Y".equals(getEmailSendResultVo.getMailSendYn()))) {
            //이메일 발송
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getOnetooneQnaAnswerEmailTmplt?csQnaId=" + qnaBosDetailResultVo.getCsQnaId();
            String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                    .reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(qnaBosDetailResultVo.getUrUserId())
                    .mail(qnaBosDetailResultVo.getUserEmail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 답변여부 - SMS 발송
        if ("Y".equals(qnaBosDetailResultVo.getAnswerSmsYn()) && ("Y".equals(getEmailSendResultVo.getSmsSendYn()))) {
            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, qnaBosDetailResultVo);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId(qnaBosDetailResultVo.getUrUserId())
                    .mobile(qnaBosDetailResultVo.getUserMobile())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
        }
    }

    /**
     * @param qnaBosDetailResultVo
     * @Desc 상품문의 답변완료 이메일/SMS 전송
     */
    @Override
    public void getProductQnaAnsweredCompleted(QnaBosDetailVo qnaBosDetailResultVo) {
        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.PRODUCT_QNA_ANSWER_COMPLETED.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

        //메일 답변여부 - 메일 발송
        if ("Y".equals(qnaBosDetailResultVo.getAnswerMailYn()) && ("Y".equals(getEmailSendResultVo.getMailSendYn()))) {
            //이메일 발송
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getProductQnaAnswerEmailTmplt?csQnaId=" + qnaBosDetailResultVo.getCsQnaId();
            String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                    .reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(qnaBosDetailResultVo.getUrUserId())
                    .mail(qnaBosDetailResultVo.getUserEmail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 답변여부 - SMS 발송
        if ("Y".equals(qnaBosDetailResultVo.getAnswerSmsYn()) && ("Y".equals(getEmailSendResultVo.getSmsSendYn()))) {
            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, qnaBosDetailResultVo);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId(qnaBosDetailResultVo.getUrUserId())
                    .mobile(qnaBosDetailResultVo.getUserMobile())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
        }
    }

    /**
     * @param String
     * @return ApiResult
     * @throws Exception
     * @Desc 1:1문의 상세 첨부파일 이미지
     */
    @Override
    public ApiResult<?> getImageList(String csQnaId) throws Exception {
        QnaBosDetailResponseDto result = new QnaBosDetailResponseDto();

        List<QnaBosDetailVo> imageList = customerQnaService.getImageList(csQnaId);
        result.setRows(imageList);

        return ApiResult.success(result);
    }

}
