/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 기획전 등록/수정 - 소스분리 - DataSet
 * @             - 조회 후 활성/비활성 처리
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.03.14    dgyoun        최초 생성
 * @
 ******************************************************************************/


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ Validation Check
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // ==========================================================================
  // # Validation Check - 기본정보
  // ==========================================================================
  function fnCheckValidBasic() {
    //console.log('# fnCheckValidBasic Start [', gbExhibitTp, ']');
    // ------------------------------------------------------------------------
    // 기획전유형 [콤보]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueItemForId('mailTemplateTp'))) {
      fnMessage('', '<font color="#FF1A1A">[DM 구분]</font> 필수 입력입니다.', 'mailTemplateTp', 'kendoDropDownList');
      return false;
    }
    // ------------------------------------------------------------------------
    // 전시여부 [라디오]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueRadioForName('goodsDispYn'))) {
      fnMessage('', '<font color="#FF1A1A">[상품전시여부]</font> 필수 입력입니다.', 'goodsDispYn');
      return false;
    }
    // ------------------------------------------------------------------------
    // 기획전제목 [Text]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueItemForId('title'))) {
      fnMessage('', '<font color="#FF1A1A">[관리자 제목]</font> 필수 입력입니다.', 'title');
      return false;
    }

    if (fnIsEmpty($('#descHtml').val()) == true) {
      fnMessage('', '<font color="#FF1A1A">[DM 콘텐츠]</font> 필수 입력입니다.', 'descHtml', 'kendoEditor');
      //var editor = $("#detlHtmlPc").data("kendoEditor");
      //editor.focus();
      return false;
    }

  // ------------------------------------------------------------------------
    // 진행기간
    // ------------------------------------------------------------------------
    // **********************************************************************
    // 상시진행 : 언체크
    // **********************************************************************
    if (fnIsEmpty(getValueItemForId('startDe'))) {
      fnMessage('', '<font color="#FF1A1A">[발송 예정일]</font> 필수 입력입니다.', 'startDe');
      return false;
    }

    return true;
  }

  // ==========================================================================
  // # Validation Check - 그리드
  // ==========================================================================
  function fnCheckValidGrid(data, dmMailDataObj, groupList) {
    //console.log('# fnCheckValidGrid Start [', gbExhibitTp, ']');
    // ------------------------------------------------------------------------
    // 상품그리드 체크
    // ------------------------------------------------------------------------
    let resultGridCheck = new Object();
    resultGridCheck.result = true;
    resultGridCheck.message = '';

    let resultGroupCheck = new Object();               //  상품그룹 Validataion 결과
    resultGroupCheck.result = true;
    resultGroupCheck.message = '';

    if (resultGridCheck != null && resultGridCheck.result) {
      // Validation Success -> 상품그룹 채크

      let isCheckYn = true;

      // --------------------------------------------------------------------
      // 3. 상품그룹 체크
      // --------------------------------------------------------------------
      // ------------------------------------------------------------------
      // 3.1. 상품그룹List
      // ------------------------------------------------------------------
      // ------------------------------------------------------------------
      // 3.1.1. Validation Check - 상품그룹List
      // ------------------------------------------------------------------


      if (isCheckYn == true) {

        // ----------------------------------------------------------------
        // 상품그룹 그리드 체크
        // ----------------------------------------------------------------
        resultGroupCheck = fnCheckValidGroupInfo(groupList);

        // ----------------------------------------------------------------
        // 3.1.2. Validation Check - Validataion 결과
        // ----------------------------------------------------------------
        if (resultGroupCheck != null && resultGroupCheck.result == true) {
          // Validation Success
        }
        else {
          // --------------------------------------------------------------
          // 상품그룹 체크 오류 처리
          // --------------------------------------------------------------
          if (resultGroupCheck != null && resultGroupCheck.message != null) {
            fnMessage('', resultGroupCheck.message, resultGroupCheck.tagId);
          }
          else {
            fnMessage('', '<font color="#FF1A1A">[입력항목]</font> 체크 오류입니다.', '');
          }
          return false;
        }

      } // End of if (isCheckYn == true) {
    }

    // ==========================================================================
    // # 상품그룹 그리드 체크
    // ==========================================================================
    function fnCheckValidGroupInfo(groupList) {

      let resultCheck = new Object();
      resultCheck.result = true;
      resultCheck.message = '';

      // 그룹 체크
      if (groupList == undefined || groupList == null || groupList == '' || groupList.length <= 0) {
        resultCheck.result = false;
        resultCheck.message = '<font color="#FF1A1A">[상품그룹]</font> 필수 입력입니다.';
        return resultCheck;
      }

      for (let i = 0; i < groupList.length; i++) {
        // 상품그룹명
        if (groupList[i].groupNm == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][상품그룹명]</font> 필수 입력입니다.';
          resultCheck.tagId   = 'groupNm'+groupList[i].groupIdx;
          break;
        }
        // 그룹전시순서
        if (groupList[i].groupSort == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][그룹노출순서]</font> 필수 입력입니다.';
          resultCheck.tagId   = 'groupSort'+groupList[i].groupIdx;
          break;
        }
        // 전시상품목록
        if (groupList[i].groupGoodsList == undefined || groupList[i].groupGoodsList == null || groupList[i].groupGoodsList == '' || groupList[i].groupGoodsList.length <= 0) {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][노출목록]</font> 필수 입력입니다.';
          break;
        }

        var goodsObj;
        var bGoodsListCheck = true;

        for (var j = 0; j < groupList[i].groupGoodsList.length; j++) {

          goodsObj = groupList[i].groupGoodsList[j];

          // 순번
          if (goodsObj.goodsSort == undefined || goodsObj.goodsSort == null) {
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][노출목록('+j+1+')][순번]</font> 필수 입력입니다.';
            bGoodsListCheck = false;
            break;
          }
          // 상품코드
          if (goodsObj.ilGoodsId == undefined || goodsObj.ilGoodsId == null || goodsObj.ilGoodsId == '') {
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][노출목록('+j+1+')][상품코드]</font> 필수 입력입니다.';
            bGoodsListCheck = false;
            break;
          }

        } // End of for (var j = 0; j < groupList[i].groupGoodsList.length; j++)

        // 상품리스트 체크 실패인 경우 모든 loop break;
        if (bGoodsListCheck == false) {
          break;
        }

      } // End of for (var i = 0; i < groupList.length; i++)
      return resultCheck;

    } // End of fnCheckValidGroupInfo(groupList)


    // ----------------------------------------------------------------------
    // 메시지 처리
    // ----------------------------------------------------------------------
    if (resultGridCheck != null && resultGridCheck.result == true) {

    }
    else {
      // --------------------------------------------------------------------
      // 상품그리드 체크 오류 처리
      // --------------------------------------------------------------------
      if (resultGridCheck != null && resultGridCheck.message != null) {
        fnMessage('', resultGridCheck.message, '');
      }
      else {
        fnMessage('', '<font color="#FF1A1A">[입력항목]</font> 체크 오류입니다.', '');
      }
      return false;
    }

    return true;

  }


  // ==========================================================================
  // # 라디오 선택값
  // ==========================================================================
  function getValueRadioForName(name) {
    return $('input[name='+name+']:checked').val();
  }

  // ==========================================================================
  // # 셀렉트박스/Text/날짜/시/분 선택값
  // ==========================================================================
  function getValueItemForId(id) {
    return $('#'+id).val();
  }

  // ==========================================================================
  // # 체크박스 선택값 (N개, 콤마(,)로 연결
  // ==========================================================================
  function getValueCheckBoxForName(name) {
    let checkVal = '';
    $('input[name='+name+']:checked').each(function() {
      let code = $(this).val();
      if (fnIsEmpty(checkVal)) {
        checkVal += code;
      }
      else {
        checkVal += ',' + code;
      }
    });
    //console.log('# checkVal :: ', checkVal);
    return checkVal;
  }




  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 상세조회 DataSet End
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // ==========================================================================
  // # 조회 후 활성/비활성 처리
  // ==========================================================================
  function fnSetScreen(data) {

    let detail = data.detail;
    // ------------------------------------------------------------------------
    // 수정가능여부 - fnBizCallback > select 에서 Set 됨
    // ------------------------------------------------------------------------
    //console.log('# gbEditableMode   :: ', gbEditableMode);
    //let editableMode = fnCheckEditableMode(exhibitTp, approvalStatus, statusSe);

    // ------------------------------------------------------------------------
    // fnInitOptionBox 로딩과 활성/비활성 처리 시간차 문제 우회 처리를 위한 setTimeout
    // ------------------------------------------------------------------------
    setTimeout(function() {
      // ----------------------------------------------------------------------
      // 기본정보
      // ----------------------------------------------------------------------
      fnSetBasicInfo(detail, gbEditableMode);

      // ----------------------------------------------------------------------
      // 상세정보
      // ----------------------------------------------------------------------
      fnSetDetailInfo(detail)
    }, 500);

  }

  // ==========================================================================
  // # 기본정보 활성/비활성 처리
  // ==========================================================================
  function fnSetBasicInfo(detail, editableMode) {
    //console.log('# ==================================================');
    //console.log('# fnSetBasicInfo Start');
    let alwaysYn        = detail.alwaysYn;
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# useYn          :: ', useYn);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);
    //console.log('# exhibitStatus  :: ', exhibitStatus);
    //console.log('# detail         :: ', JSON.stringify(detail));

    // ------------------------------------------------------------------------
    // 활성/비활성 Set
    //  - editableMode : Y(수정가능)/N(수정불가)/P(부분수정)
    //                 : fnCheckEditableMode(exhibitTp, approvalStatus, statusSe) 에서 설정
    // ------------------------------------------------------------------------
      // ----------------------------------------------------------------------
      // 수정불가
      // ----------------------------------------------------------------------
      // 기획전제목
      $('input[name=title]').attr('disabled', true);                              // input text
      // 진행기간
      $('#startDe').data('kendoDatePicker').enable(true);                        // kendoDatePicker
      // 기획전상세(PC)
      $($('#descHtml').data().kendoEditor.body).attr('contenteditable', true); // kendoEditor
  }

  // ==========================================================================
  // # 상세정보 활성/비활성 처리
  // ==========================================================================
  function fnSetDetailInfo(detail) {
    //console.log('# ==================================================');
    //console.log('# fnSetDetailInfo Start');
    let exhibitTp       = detail.exhibitTp;
    let useYn           = detail.useYn;
    let statusSe        = detail.statusSe;
    let approvalStatus  = detail.approvalStatus
    let alwaysYn        = detail.alwaysYn;
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# useYn          :: ', useYn);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);

    // 수정가능여부
    let editableMode = fnCheckEditableMode(exhibitTp, approvalStatus, statusSe);
    //console.log('# editableMode   :: ', editableMode);

    // ------------------------------------------------------------------------
    // 이벤트유형별 호출
    // ------------------------------------------------------------------------
    if (exhibitTp == 'EXHIBIT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반이벤트
      // ----------------------------------------------------------------------

    }
  }

  // ==========================================================================
  // # 골라담기 상세정보 활성/비활성 처리
  // ==========================================================================
  function fnSetDetailSelect(detail, editableMode) {
    //console.log('# ==================================================');
    //console.log('# fnSetDetailSelect Start');
    let exhibitTp       = detail.exhibitTp;
    let useYn           = detail.useYn;
    let statusSe        = detail.statusSe;
    let approvalStatus  = detail.approvalStatus
    let alwaysYn        = detail.alwaysYn;
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# useYn          :: ', useYn);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);
    //console.log('# editableMode   :: ', editableMode);

    // ------------------------------------------------------------------------
    // 활성/비활성 Set
    // ------------------------------------------------------------------------
    if (editableMode == 'Y') {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 수정가능
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 상품별구매가능수량
      $('#goodsBuyLimitCnt').data('kendoDropDownList').enable(true);                  // kendoDropDownList
      // 골라담기기본구매수량
      $('#defaultBuyCnt').data('kendoDropDownList').enable(true);                     // kendoDropDownList
      // 골라담기균일가
      $('input[name=selectPrice]').attr('disabled', false);                           // input text
      // 단가적용 버튼
      $('#btnSetUnitPrice').prop('disabled', false);                                  // button

      // ----------------------------------------------------------------------
      // 대표상품 그리드
      // ----------------------------------------------------------------------
      // 대표상품그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectTargetGoods').removeAttr('disabled', 'disabled');     // button
      // 대표상품그리드-변경버튼(n) -> 그리드 fnInitSelectTargetGoodsGrid > dataBound 에서 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 상품 그리드
      // ----------------------------------------------------------------------
      // 골라담기그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectGoods').removeAttr('disabled', 'disabled');
      // 골라담기그리드-삭제버튼 -> 그리드 fnInitSelectGoodsGrid > dataBound 에서 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 추가상품 그리드
      // ----------------------------------------------------------------------
      // 추가상품그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectAddGoods').removeAttr('disabled', 'disabled');        // button
      // 추가상품그리드-삭제버튼 -> 그리드 fnInitSelectAddGoodsGrid > dataBound 에서 활성/비활성 처리
      // 추가상품그리드-판매가   -> 그리드 fnInitSelectAddGoodsGrid > 생성시 활성/비활성 설정
    }
    else if (editableMode == 'N') {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 수정불가
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 상품별구매가능수량
      $('#goodsBuyLimitCnt').data('kendoDropDownList').enable(false);                 // kendoDropDownList
      // 골라담기기본구매수량
      $('#defaultBuyCnt').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      // 골라담기균일가
      $('input[name=selectPrice]').attr('disabled', true);                            // input text
      // 단가적용 버튼
      $('#btnSetUnitPrice').prop('disabled', true);                                   // button

      // ----------------------------------------------------------------------
      // 대표상품 그리드
      // ----------------------------------------------------------------------
      // 대표상품그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectTargetGoods').attr('disabled', 'disabled');           // button
      // 대표상품그리드-변경버튼(n) -> 그리드 fnInitSelectTargetGoodsGrid > dataBound 에서 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 상품 그리드
      // ----------------------------------------------------------------------
      // 골라담기그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectGoods').attr('disabled', 'disabled');                 // button
      // 골라담기그리드-삭제버튼 -> 그리드 fnInitSelectGoodsGrid > dataBound 에서 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 추가상품 그리드
      // ----------------------------------------------------------------------
      // 추가상품그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectAddGoods').attr('disabled', 'disabled');                // button
      // 추가상품그리드-삭제버튼 -> 그리드 fnInitSelectAddGoodsGrid > dataBound 에서 활성/비활성 처리
      // 추가상품그리드-판매가   -> 그리드 fnInitSelectAddGoodsGrid > 생성시 활성/비활성 설정

      // ----------------------------------------------------------------------
      // 그리드내 활성/비활성 처리 : 그리드 바운드에서 처리
      // ----------------------------------------------------------------------
    }
    else {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 상세 중 상품관련 그리드 이외만 비활성 : editableMode == 'P'
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

      //// 수정불가
      // 상품별구매가능수량
      $('#goodsBuyLimitCnt').data('kendoDropDownList').enable(false);                 // kendoDropDownList
      // 골라담기기본구매수량
      $('#defaultBuyCnt').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      // 골라담기균일가
      $('input[name=selectPrice]').attr('disabled', true);                            // input text
      // 단가적용 버튼
      $('#btnSetUnitPrice').prop('disabled', true);                                   // button

      //// 수정가능
      // 대표상품그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectTargetGoods').prop('disabled', true);                 // button
      // 대표상품그리드-변경버튼(n)
      //$('.btnAddGoodsPopupSelectTargetGoodsEdit').attr('disabled', 'disabled');       // button
      // 골라담기그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectGoods').prop('disabled', false);                      // button
      // 골라담기그리드-삭제버튼
      $('.btnSelectGoodsDel').removeAttr('disabled', 'disabled');                     // button
      // 추가상품그리드-상품추가버튼
      $('#btnAddGoodsPopupSelectAddGoods').prop('disabled', false);                   // button
      // 추가상품그리드-삭제버튼
      $('.btnSelectAddGoodsDel').removeAttr('disabled', 'disabled');                  // button
      // 추가상품그리드-판매가
      $('.salePrice-input').removeAttr('disabled', 'disabled');                       // input text

      // ----------------------------------------------------------------------
      // 그리드내 활성/비활성 처리 : 그리드 바운드에서 처리
      // ----------------------------------------------------------------------
    }

  }

  // ==========================================================================
  // # 증정행사 상세정보 활성/비활성 처리
  // ==========================================================================
  function fnSetDetailGift(detail, editableMode) {
    //console.log('# ==================================================');
    //console.log('# fnSetDetailGift Start [', editableMode, ']');
    let exhibitTp       = detail.exhibitTp;
    let useYn           = detail.useYn;
    let statusSe        = detail.statusSe;
    let approvalStatus  = detail.approvalStatus
    let alwaysYn        = detail.alwaysYn;
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# useYn          :: ', useYn);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);
    //console.log('# editableMode   :: ', editableMode);

    // ------------------------------------------------------------------------
    // 활성/비활성 Set
    // ------------------------------------------------------------------------
    if (editableMode == 'Y') {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 수정가능
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

      // ----------------------------------------------------------------------
      // 상세항목
      // ----------------------------------------------------------------------
      // 증정수량
      $('#giftCnt').removeAttr('disabled', 'disabled');                           // input text
      // 증정조건
      $('#giftTp').data('kendoDropDownList').enable(true);                        // kendoDropDownList
      // 증정조건금액
      $('#overPrice').attr('disabled', 'disabled');                               // input text
      // 증정조건 포함
      $('#giftRangeTp').data('kendoDropDownList').enable(true);                   // kendoDropDownList
      // 증정방식
      $('#giftGiveTp').data('kendoDropDownList').enable(true);                    // kendoDropDownList
      // 증정품배송조건
      $('#giftShippingTp').data('kendoDropDownList').enable(true);                // kendoDropDownList
      // 출고처
      $('#urWarehouseId').data('kendoDropDownList').enable(true);                 // kendoDropDownList

      // ----------------------------------------------------------------------
      // 증정상품 그리드
      // ----------------------------------------------------------------------
      // 증정상품그리드-상품추가버튼
      $('#btnAddGoodsPopupGiftGoods').removeAttr('disabled', 'disabled');         // button
      // 증정상품그리드-삭제버튼 -> 그리드 fnInitGiftGoodsGrid > dataBound 에서 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 적용대상상품 그리드
      // ----------------------------------------------------------------------
      // 적용대상그리드-상품/브랜드
      $('#giftTargetTp').data('kendoDropDownList').enable(true);                  // kendoDropDownList
      // 적용대상그리드-상품추가버튼
      $('#btnAddGoodsPopupGiftTargetGoods').removeAttr('disabled', 'disabled');   // button
      // 적용대상그리드-브랜드콤보
      $('#dpBrandId').data('kendoDropDownList').enable(true);                     // kendoDropDownList
      // 적용대상그리드-브랜드추가버튼
      $('#btnAddGoodsPopupGiftTargetBrand').removeAttr('disabled', 'disabled');   // button
      // 적용대상그리드-선택삭제버튼
      $('#fnBtnTargetGoodsDel').removeAttr('disabled', 'disabled');               // button
      // 적용대상그리드-상품삭제버튼 -> 그리드 fnInitGiftTargetGoodsGrid > dataBound 활성/비활성 처리
      // 적용대상그리드-브랜드삭제버튼 -> 그리드 fnInitGiftTargetBrandGrid > dataBound 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 상품그룹
      // ----------------------------------------------------------------------
      // 상품그룹추가버튼
      $('#btnGroupAddGift').removeAttr('disabled', 'disabled');                   // button
      // 그룹별상품업로드버튼
      $('#btnGroupGoodsUploadGift').removeAttr('disabled', 'disabled');           // button
      // 상품그룹별 기본정보       -> fnSetGroupInfo 에서 활성/비활성 설정
      // 상품그룹 그리드 삭제버튼  -> 그리드 fnInitGroupGoodsGrid > dataBound에서 활성/비활성 처리

    }
    else if (editableMode == 'N') {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 수정불가
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // ----------------------------------------------------------------------
      // 상세항목
      // ----------------------------------------------------------------------
      // 증정수량
      $('#giftCnt').attr('disabled', 'disabled');                                 // input text
      // 증정조건
      $('#giftTp').data('kendoDropDownList').enable(false);                       // kendoDropDownList
      // 증정조건금액
      $('#overPrice').attr('disabled', 'disabled');                               // input text
      // 증정조건 포함
      $('#giftRangeTp').data('kendoDropDownList').enable(false);                  // kendoDropDownList
      // 증정방식
      $('#giftGiveTp').data('kendoDropDownList').enable(false);                   // kendoDropDownList
      // 증정품배송조건
      $('#giftShippingTp').data('kendoDropDownList').enable(false);               // kendoDropDownList
      // 출고처
      $('#urWarehouseId').data('kendoDropDownList').enable(false);                // kendoDropDownList

      // ----------------------------------------------------------------------
      // 증정상품 그리드
      // ----------------------------------------------------------------------
      // 증정상품그리드-상품추가버튼
      $('#btnAddGoodsPopupGiftGoods').attr('disabled', 'disabled');               // button
      // 증정상품그리드-삭제버튼 -> 그리드 fnInitGiftGoodsGrid > dataBound 에서 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 적용대상상품 그리드
      // ----------------------------------------------------------------------
      // 적용대상그리드-상품/브랜드
      $('#giftTargetTp').data('kendoDropDownList').enable(false);                 // kendoDropDownList
      // 적용대상그리드-상품추가버튼
      $('#btnAddGoodsPopupGiftTargetGoods').attr('disabled', 'disabled');         // button
      // 적용대상그리드-브랜드콤보
      $('#dpBrandId').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      // 적용대상그리드-브랜드추가버튼
      $('#btnAddGoodsPopupGiftTargetBrand').attr('disabled', 'disabled');         // button
      // 적용대상그리드-선택삭제버튼
      $('#fnBtnTargetGoodsDel').attr('disabled', 'disabled');                     // button
      // 적용대상그리드-상품삭제버튼 -> 그리드 fnInitGiftTargetGoodsGrid > dataBound 활성/비활성 처리
      // 적용대상그리드-브랜드삭제버튼 -> 그리드 fnInitGiftTargetBrandGrid > dataBound 활성/비활성 처리

      // ----------------------------------------------------------------------
      // 상품그룹
      // ----------------------------------------------------------------------
      // 상품그룹추가버튼
      $('#btnGroupAddGift').attr('disabled', 'disabled');                         // button
      // 그룹별상품업로드버튼
      $('#btnGroupGoodsUploadGift').attr('disabled', 'disabled');                 // button
      // 상품그룹별 기본정보       -> fnSetGroupInfo 에서 활성/비활성 설정
      // 상품그룹 그리드 삭제버튼  -> 그리드 fnInitGroupGoodsGrid > dataBound에서 활성/비활성 처리

    }
    else {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 상세 중 상품관련 그리드 이외만 비활성
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // ----------------------------------------------------------------------
      // 상세항목
      // ----------------------------------------------------------------------
      // 증정수량
      $('#giftCnt').attr('disabled', 'disabled');                                 // input text
      // 증정조건
      $('#giftTp').data('kendoDropDownList').enable(false);                       // kendoDropDownList
      // 증정조건금액
      $('#overPrice').attr('disabled', 'disabled');                               // input text
      // 증정조건 포함
      $('#giftRangeTp').data('kendoDropDownList').enable(false);                  // kendoDropDownList
      // 증정방식
      $('#giftGiveTp').data('kendoDropDownList').enable(false);                   // kendoDropDownList
      // 증정품배송조건
      $('#giftShippingTp').data('kendoDropDownList').enable(false);               // kendoDropDownList
      // 출고처
      $('#urWarehouseId').data('kendoDropDownList').enable(false);                // kendoDropDownList

      // ----------------------------------------------------------------------
      // 증정상품 그리드
      // ----------------------------------------------------------------------
      // 증정상품그리드-상품추가버튼
      $('#btnAddGoodsPopupGiftGoods').attr('disabled', 'disabled');               // button
      // 증정상품그리드-삭제버튼 -> 그리드 fnInitGiftGoodsGrid > dataBound 에서 활성/비활성 처리

    }

  }


  // ==========================================================================
  // # 에디터모드 조회
  // ==========================================================================
  function fnCheckEditableMode(exhibitTp, approvalStatus, statusSe) {
    //console.log('# fnCheckEditableMode Start');
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# approvalStatus :: ', approvalStatus);
    //console.log('# statusSe       :: ', statusSe);

    let editableMode;   // 수정가능여부

    if (exhibitTp == 'EXHIBIT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반이벤트
      // ----------------------------------------------------------------------
      editableMode = 'Y';
    }
    else {
      // ----------------------------------------------------------------------
      // 골라담기/증정행사
      // ----------------------------------------------------------------------
      if (approvalStatus == 'APPR_STAT.NONE' || approvalStatus == 'APPR_STAT.CANCEL' || approvalStatus == 'APPR_STAT.DENIED') {
        // 승인대기(저장)/요청철회/승인반려 : 수정가능
        editableMode = 'Y';
      }
      else if (approvalStatus == 'APPR_STAT.REQUEST' || approvalStatus == 'APPR_STAT.SUB_APPROVED') {
        // 승인요청/승인완료(부)            : 수정불가
        editableMode = 'N';
      }
      else if (approvalStatus == 'APPR_STAT.APPROVED') {
        // 승인완료                         : 진행상태에 따라 설정
        if (statusSe == 'END') {
          // 진행종료
          editableMode = 'N';
        }
        else {
          // 진행예정/진행중
          editableMode = 'P';   // 상품그리드 영역 수정 가능
        }
      }
      else {
        // 미정의 승인상태 : 수정불가
        editableMode = 'N';
      }
    }
    //console.log('# editableMode   :: ', editableMode);
    return editableMode;
  }

  // ==========================================================================
  // 상시진행 이벤트 처리
  // ==========================================================================
  function fnSetExhibitAlwaysYn(alwaysYn) {
    // 해제 -> 기간, 자동종료 활성
    $("#startDe").data("kendoDatePicker").enable(true);
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 기타 공통
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID, option) {
    fnKendoMessage({
        message : fnGetLangData({ key : key, nullMsg : nullMsg})
      , ok      : function() {
                    if (fnIsEmpty(ID) == false) {
                      if (option == 'kendoDropDownList') {
                        // kendoDropDownList 이면
                        $('#'+ID).data('kendoDropDownList').focus();
                      }
                      else if (option == 'kendoEditor') {
                        // kendoEditor 이면
                        $('#'+ID).data('kendoEditor').focus();
                      }
                      else {
                        $('#'+ID).focus();
                      }
                    }
                  }
    });
  }
