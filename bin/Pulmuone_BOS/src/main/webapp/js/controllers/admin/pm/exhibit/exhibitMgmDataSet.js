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
    if (fnIsEmpty(getValueItemForId('exhibitTp')) == true) {
      fnMessage('', '<font color="#FF1A1A">[기획전 분류]</font> 필수 입력입니다.', 'exhibitTp', 'kendoDropDownList');
      return false;
    }
    // ------------------------------------------------------------------------
    // 몰구분 [라디오]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueRadioForName('mallDiv')) == true) {
      fnMessage('', '<font color="#FF1A1A">[몰구분]</font> 필수 입력입니다.', 'mallDiv');
      return false;
    }
    // ------------------------------------------------------------------------
    // 사용여부 [라디오]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueRadioForName('useYn')) == true) {
      fnMessage('', '<font color="#FF1A1A">[사용여부]</font> 필수 입력입니다.', 'useYn');
      return false;
    }
    // ------------------------------------------------------------------------
    // 전시여부 [라디오]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueRadioForName('dispYn')) == true) {
      fnMessage('', '<font color="#FF1A1A">[전시여부]</font> 필수 입력입니다.', 'dispYn');
      return false;
    }
    // ------------------------------------------------------------------------
    // 기획전전시여부 [라디오]
    // ------------------------------------------------------------------------
    if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      if (fnIsEmpty(getValueRadioForName('dispYn')) == true) {
        fnMessage('', '<font color="#FF1A1A">[기획전 전시여부]</font> 필수 입력입니다.', 'dispYn');
        return false;
      }
    }
    // ------------------------------------------------------------------------
    // 기획전제목 [Text]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueItemForId('title')) == true) {
      fnMessage('', '<font color="#FF1A1A">[기획전 제목]</font> 필수 입력입니다.', 'title');
      return false;
    }
    // ------------------------------------------------------------------------
    // 노출범위(디바이스) [체크]
    // ------------------------------------------------------------------------
    if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // **********************************************************************
      // 증정행사
      // **********************************************************************
      if (getValueRadioForName('exhibitDispYn') == 'Y') {
        if (fnIsEmpty(getValueCheckBoxForName('goodsDisplayType')) == true) {
          fnMessage('', '<font color="#FF1A1A">[노출범위(디바이스)]</font> 필수 입력입니다.', 'goodsDisplayType_0');
          return false;
        }
      }
    }
    else {
      // **********************************************************************
      // 일반/골라담기
      // **********************************************************************
      if (fnIsEmpty(getValueCheckBoxForName('goodsDisplayType')) == true) {
        fnMessage('', '<font color="#FF1A1A">[노출범위(디바이스)]</font> 필수 입력입니다.', 'goodsDisplayType');
        return false;
      }
    }
    // ------------------------------------------------------------------------
    // 비회원 접근권한 [라디오]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueRadioForName('dispNonmemberYn')) == true) {
      fnMessage('', '<font color="#FF1A1A">[비회원 접근권한]</font> 필수 입력입니다.', 'dispNonmemberYn');
      return false;
    }
    // ------------------------------------------------------------------------
    // 임직원 전용여부 [라디오]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueRadioForName('evEmployeeTp')) == true) {
      fnMessage('', '<font color="#FF1A1A">[임직원 전용여부]</font> 필수 입력입니다.', 'evEmployeeTp');
      return false;
    }
    // ------------------------------------------------------------------------
    // 접근권한(회원등급레벨) [콤보][콤보]
    // ------------------------------------------------------------------------
    if(gbUserGroupMaps.size == 0 && $('#userMaster').data('kendoDropDownList').value() != '') {
      fnMessage('', '[접근권한] 회원등급 전체이거나, 하나의 등급이상 추가한 상태이어야만 저장 됩니다.', 'userMaster', 'kendoDropDownList');
      return false;
    }
    // ------------------------------------------------------------------------
    // 진행기간
    // ------------------------------------------------------------------------
    if($('input:checkbox[name=alwaysYn]').is(':checked') == false) {
      // **********************************************************************
      // 상시진행 : 언체크
      // **********************************************************************
      if (fnIsEmpty(getValueItemForId('startDe')) == true) {
        fnMessage('', '<font color="#FF1A1A">[시작일자]</font> 필수 입력입니다.', 'startDe');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('startHour')) == true) {
        fnMessage('', '<font color="#FF1A1A">[시작시]</font> 필수 입력입니다.', 'startHour');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('startMin')) == true) {
        fnMessage('', '<font color="#FF1A1A">[시작분]</font> 필수 입력입니다.', 'startMin');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('endDe')) == true) {
        fnMessage('', '<font color="#FF1A1A">[종료일자]</font> 필수 입력입니다.', 'endDe');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('endHour')) == true) {
        fnMessage('', '<font color="#FF1A1A">[종료시]</font> 필수 입력입니다.', 'endHour');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('endMin')) == true) {
        fnMessage('', '<font color="#FF1A1A">[종료분]</font> 필수 입력입니다.', 'endMin');
        return false;
      }
    }

    let isCheckYn = true;
    // console.log('# gbExhibitTp :: ', gbExhibitTp);
    if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // --------------------------------------------------------------------
      // 증정행사
      // --------------------------------------------------------------------
      let exhibitDispYn = $('input[name=exhibitDispYn]:checked').val();

      if (exhibitDispYn == 'Y') {
        isCheckYn = true;
      }
      else {
        isCheckYn = false;
      }
    }
    else {
      // --------------------------------------------------------------------
      // 일반/골라담기
      // --------------------------------------------------------------------
      isCheckYn = true;
    }

    if (isCheckYn == true) {
      // ----------------------------------------------------------------------
      // 배너이미지 [Img]
      // ----------------------------------------------------------------------
      if (jQuery('#imgBannerView').attr("src") == null || jQuery('#imgBannerView').attr("src") == '') {
        gbBnrImgPath        = '';
        gbBnrImgOriginNm    = '';
      }
      if (fnIsEmpty(gbBnrImgPath) == true) {
        fnMessage('', '<font color="#FF1A1A">[배너 이미지]</font> 필수 입력입니다.', 'timeOverCloseYn_0'); // 제일 가까운 항목으로 포커스 이동
        return false;
      }
      // ----------------------------------------------------------------------
      // 기획전상세(PC) [Edit]
      // ----------------------------------------------------------------------
      if ($('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').is(':checked') == true ||
          $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_PC"]').is(':checked') == true) {
        // 노출범위 : 전체/PC

        if (fnIsEmpty($('#detlHtmlPc').val()) == true) {
          fnMessage('', '<font color="#FF1A1A">[기획전 상세(PC)]</font> 필수 입력입니다.', 'detlHtmlPc', 'kendoEditor');
          //var editor = $("#detlHtmlPc").data("kendoEditor");
          //editor.focus();
          return false;
        }
      }
      // ----------------------------------------------------------------------
      // 기획전상세(Mobile) [Edit]
      // ----------------------------------------------------------------------
      if ($('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').is(':checked') == true ||
          $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_MOBILE"]').is(':checked') == true ||
          $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.APP"]').is(':checked') == true) {
            // 노출범위 : 전체/Mweb/APP
        if (fnIsEmpty($('#detlHtmlMo').val()) == true) {
          fnMessage('', '<font color="#FF1A1A">[기획전 상세(Mobile)]</font> 필수 입력입니다.', 'detlHtmlMo', 'kendoEditor');
          //var editor = $("#detlHtmlMo").data("kendoEditor");
          //editor.focus();
          return false;
        }
      }
    }

    if (gbExhibitTp == 'EXHIBIT_TP.NORMAL') {
      // **********************************************************************
      // 일반기획전
      // **********************************************************************
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      // **********************************************************************
      // 골라담기
      // **********************************************************************
      // ----------------------------------------------------------------------
      // 골라담기기본구매수량 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('defaultBuyCnt')) == true) {
        fnMessage('', '<font color="#FF1A1A">[골라담기 기본 구매 수량]</font> 필수 입력입니다.', 'defaultBuyCnt', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 골라담기균일가 [Text]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('selectPrice')) == true) {
        fnMessage('', '<font color="#FF1A1A">[골라담기 균일가]</font> 필수 입력입니다.', 'selectPrice');
        return false;
      }

    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // **********************************************************************
      // 증정행사
      // **********************************************************************
      // ----------------------------------------------------------------------
      // 증정수량 [Text]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('giftCnt')) == true) {
        fnMessage('', '<font color="#FF1A1A">[증정 수량]</font> 필수 입력입니다.', 'giftCnt');
        return false;
      }
      // ----------------------------------------------------------------------
      // 증정조건 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('giftTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[증정 조건]</font> 필수 입력입니다.', 'giftTp', 'kendoDropDownList');
        return false;
      }

      if ($('#giftTp').val() == 'GIFT_TP.CART') {
        // ********************************************************************
        // 증정조건이 장바구니
        // ********************************************************************
        // --------------------------------------------------------------------
        // 제한금액 [Text]
        // --------------------------------------------------------------------
        if (fnIsEmpty(getValueItemForId('overPrice')) == true) {
          fnMessage('', '<font color="#FF1A1A">[증정 조건 제한금액]</font> 필수 입력입니다.', 'overPrice');
          return false;
        }
        // --------------------------------------------------------------------
        // 증정범위유형 [콤보]
        // --------------------------------------------------------------------
        if (fnIsEmpty(getValueItemForId('giftRangeTp')) == true) {
          fnMessage('', '<font color="#FF1A1A">[증정 조건 증정범위유형]</font> 필수 입력입니다.', 'giftRangeTp', 'kendoDropDownList');
          return false;
        }
      }

      // ----------------------------------------------------------------------
      // 증정방식 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('giftGiveTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[증정 방식]</font> 필수 입력입니다.', 'giftGiveTp', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 증정품배송조건 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('giftShippingTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[증정품 배송 조건]</font> 필수 입력입니다.', 'giftShippingTp', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 출고처 [콤보]
      // ----------------------------------------------------------------------
      if ($('#giftShippingTp').val() == 'GIFT_SHIPPING_TP.COMBINED') {
        // 증정품배송조건이 합배송    : 출고처 필수
        if (fnIsEmpty(getValueItemForId('urWarehouseId')) == true) {
          fnMessage('', '<font color="#FF1A1A">[출고처]</font> 필수 입력입니다.', 'urWarehouseId', 'kendoDropDownList');
          return false;
        }
      }
    }

    return true;
  }

  // ==========================================================================
  // # Validation Check - 그리드
  // ==========================================================================
  function fnCheckValidGrid(data, exhibitDataObj, groupList) {
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

    if (gbExhibitTp == 'EXHIBIT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반기획전
      // ----------------------------------------------------------------------
      //resultGridCheck = new Object();
      //resultGridCheck.result = true;
      //resultGridCheck.message = '';
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      // ----------------------------------------------------------------------
      // 골라담기
      // ----------------------------------------------------------------------
      // 대표상품
      resultGridCheck = fnCheckValidGoodsGrid(exhibitDataObj.exhibitSelectData.exhibitSelectTargetGoodsList, 'selectTargetGoodsGrid');

      // 골라담기 상품 그리드
      if (resultGridCheck != null && resultGridCheck.result == true) {
        resultGridCheck = fnCheckValidGoodsGrid(exhibitDataObj.exhibitSelectData.exhibitSelectGoodsList, 'selectGoodsGrid');
      }
      // 추가 상품 그리드
      if (resultGridCheck != null && resultGridCheck.result == true) {

        if (exhibitDataObj.exhibitSelectData.exhibitSelectAddGoodsList != undefined &&
            exhibitDataObj.exhibitSelectData.exhibitSelectAddGoodsList != null &&
            exhibitDataObj.exhibitSelectData.exhibitSelectAddGoodsList.length > 0) {
          // 추가상품을 선택했을 경우만 체크
          resultGridCheck = fnCheckValidGoodsGrid(exhibitDataObj.exhibitSelectData.exhibitSelectAddGoodsList, 'selectAddGoodsGrid');
        }
      }
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // --------------------------------------------------------------------
      // 증정행사
      // --------------------------------------------------------------------
      if (exhibitDataObj.exhibitGiftData.exhibitGiftGoodsList == undefined || exhibitDataObj.exhibitGiftData.exhibitGiftGoodsList == null || exhibitDataObj.exhibitGiftData.exhibitGiftGoodsList.length <= 0) {
        resultGridCheck.result = false;
        resultGridCheck.message = '<font color="#FF1A1A">[증정상품]</font> 필수 입력입니다.';
      }

      if (resultGridCheck != null && resultGridCheck.result == true) {

        // 증정상품 그리드
        resultGridCheck = fnCheckValidGoodsGrid(exhibitDataObj.exhibitGiftData.exhibitGiftGoodsList, 'giftGoodsGrid');

        if (resultGridCheck != null && resultGridCheck.result == true) {

          if (data.giftTargetTp == 'GIFT_TARGET_TP.GOODS') {
            // ----------------------------------------------------------------
            // 증정대상 : 상품
            // ----------------------------------------------------------------
            if (exhibitDataObj.exhibitGiftData.exhibitGiftTargetGoodsList == undefined || exhibitDataObj.exhibitGiftData.exhibitGiftTargetGoodsList == null || exhibitDataObj.exhibitGiftData.exhibitGiftTargetGoodsList.length <= 0) {
              resultGridCheck.result = false;
              resultGridCheck.message = '<font color="#FF1A1A">[적용대상 상품]</font> 필수 입력입니다.';
            }
            if (resultGridCheck != null && resultGridCheck.result == true) {
              resultGridCheck = fnCheckValidGoodsGrid(exhibitDataObj.exhibitGiftData.exhibitGiftTargetGoodsList, 'giftTargetGoodsGrid');
            }
          }
          else {
            // ----------------------------------------------------------------
            // 증정대상 : 브랜드
            // ----------------------------------------------------------------
            if (exhibitDataObj.exhibitGiftData.exhibitGiftTargetBrandList == undefined || exhibitDataObj.exhibitGiftData.exhibitGiftTargetBrandList == null || exhibitDataObj.exhibitGiftData.exhibitGiftTargetBrandList.length <= 0) {
              resultGridCheck.result = false;
              resultGridCheck.message = '<font color="#FF1A1A">[적용대상 브랜드]</font> 필수 입력입니다.';
            }
            if (resultGridCheck != null && resultGridCheck.result == true) {
              resultGridCheck = fnCheckValidGoodsGrid(exhibitDataObj.exhibitGiftData.exhibitGiftTargetBrandList, 'giftTargetBrandGrid');
            }
          }
        }
      }
    }

    if (resultGridCheck != null && resultGridCheck.result == true) {
      // Validation Success -> 상품그룹 채크

      let isCheckYn = true;

      // --------------------------------------------------------------------
      // 3. 상품그룹 체크
      // --------------------------------------------------------------------
      if (gbExhibitTp == 'EXHIBIT_TP.NORMAL' || gbExhibitTp == 'EXHIBIT_TP.GIFT') {
        // ------------------------------------------------------------------
        // 3.1. 상품그룹List
        // ------------------------------------------------------------------
        // ------------------------------------------------------------------
        // 3.1.1. Validation Check - 상품그룹List
        // ------------------------------------------------------------------

        if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
          // ----------------------------------------------------------------
          // 증정행사
          // ----------------------------------------------------------------
          let exhibitDispYn = $('input[name=exhibitDispYn]:checked').val();

          if (exhibitDispYn == 'Y') {
            isCheckYn = true;
          }
          else {
            isCheckYn = false;
          }
        }

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
    }

    // ==========================================================================
    // # 상품그룹 그리드 체크
    // ==========================================================================
    function fnCheckValidGroupInfo(groupList) {

      var resultCheck = new Object();
      resultCheck.result = true;
      resultCheck.message = '';

      // 그룹 체크
      if (groupList == undefined || groupList == null || groupList == '' || groupList.length <= 0) {
        resultCheck.result = false;
        resultCheck.message = '<font color="#FF1A1A">[상품그룹]</font> 필수 입력입니다.';
        return resultCheck;
      }

      for (var i = 0; i < groupList.length; i++) {
        // 상품그룹명
        if (groupList[i].groupNm == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][상품그룹명]</font> 필수 입력입니다.';
          resultCheck.tagId   = 'groupNm'+groupList[i].groupIdx;
          break;
        }
        // 그룹사용여부
        if (groupList[i].groupUseYn == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][그룹 사용 여부]</font> 필수 입력입니다.';
          break;
        }
        // 상품 그룹명 배경 설정
        if (groupList[i].exhibitImgTp == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][상품 그룹명 배경 설정]</font> 필수 입력입니다.';
          break;
        }
        // 상품 그룹명 배경 색상
        if (groupList[i].exhibitImgTp == 'EXHIBIT_IMG_TP.BG') {
          if (groupList[i].bgCd == '') {
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][상품 그룹명 배경 색상]</font> 필수 입력입니다.';
            resultCheck.tagId   = 'bgCd'+groupList[i].groupIdx;
            break;
          }
        }
        // 전시상품수
        if (groupList[i].dispCnt == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][전시상품수]</font> 필수 입력입니다.';
          resultCheck.tagId   = 'dispCnt'+groupList[i].groupIdx;
          break;
        }
        // 그룹전시순서
        if (groupList[i].groupSort == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][그룹전시순서]</font> 필수 입력입니다.';
          resultCheck.tagId   = 'groupSort'+groupList[i].groupIdx;
          break;
        }

        // 전시상품목록
        if (groupList[i].groupGoodsList == undefined || groupList[i].groupGoodsList == null || groupList[i].groupGoodsList == '' || groupList[i].groupGoodsList.length <= 0) {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][전시상품목록]</font> 필수 입력입니다.';
          break;
        }

        var goodsObj;
        var bGoodsListCheck = true;

        for (var j = 0; j < groupList[i].groupGoodsList.length; j++) {

          goodsObj = groupList[i].groupGoodsList[j];

          // 순번
          if (goodsObj.goodsSort == undefined || goodsObj.goodsSort == null) {
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][전시상품목록('+j+1+')][순번]</font> 필수 입력입니다.';
            bGoodsListCheck = false;
            break;
          }
          // 상품코드
          if (goodsObj.ilGoodsId == undefined || goodsObj.ilGoodsId == null || goodsObj.ilGoodsId == '') {
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][전시상품목록('+j+1+')][상품코드]</font> 필수 입력입니다.';
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
  // # 상품 그리드 체크
  // ==========================================================================
  function fnCheckValidGoodsGrid(goodsList, gridId) {
    //console.log('# fnCheckValidGoodsGrid [', gridId, ']');
    var resultCheck = new Object();
    resultCheck.result = true;
    resultCheck.message = '';

    var goodsObj;
    var gridNm   = '';

    // 체크실행여부
    var bCheckGoodsSort               = false;    // 상품순번
    var bCheckIlGoodsId               = false;    // 상품코드
    var bCheckSelectDisplayYn         = false;    // 골라담기 노출여부
    var bCheckSalePrice               = false;    // 판매가
    var bCheckGiftCnt                 = false;    // 증정행사 증정기본수량
    var bCheckRepGoodsYn              = false;    // 증정행사 대표상품 대표상품여부
    var bCheckBrandId                 = false;    // 증정행사 브랜드ID

    var selectDisplayYnEmptyCnt = 0;      // 골라담기 노출여부 공백 개수
    var giftRepGoodsYnCnt     = 0;        // 증정행사 대표상품 개수

    if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      // ----------------------------------------------------------------------
      // 골라담기
      // ----------------------------------------------------------------------
      if (gridId == 'selectTargetGoodsGrid') {
        // 대표상품
        gridNm                = '대표상품';
        bCheckIlGoodsId       = true;         // 상품코드 체크
        bCheckSelectDisplayYn = true;         // 노출여부 체크
      }
      else if (gridId == 'selectGoodsGrid') {
        // 상품
        gridNm                = '골라담기 상품';
        bCheckGoodsSort       = true;         // 순번 체크
        bCheckIlGoodsId       = true;         // 상품코드 체크
        bCheckSelectDisplayYn = true;         // 노출여부 체크
      }
      else if (gridId == 'selectAddGoodsGrid') {
        // 추가상품
        gridNm                = '추가 상품';
        bCheckIlGoodsId       = true;         // 상품코드 체크
        bCheckSelectDisplayYn = true;         // 노출여부 체크
        bCheckSalePrice       = true;         // 판매가 체크
      }
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // ----------------------------------------------------------------------
      // 증정행사
      // ----------------------------------------------------------------------
      if (gridId == 'giftGoodsGrid') {
        // 증정상품
        gridNm            = '증정상품';
        bCheckGoodsSort   = true;             // 지급순위 체크
        bCheckIlGoodsId   = true;             // 상품코드 체크
        bCheckGiftCnt     = true;             // 증정기본수량 체크
        bCheckRepGoodsYn  = true;             // 대표상품여부 체크
      }
      else if (gridId == 'giftTargetGoodsGrid') {
        // 적용대상상품
        gridNm = '적용대상상품';
        bCheckIlGoodsId = true;               // 상품코드 체크
      }
      else if (gridId == 'giftTargetBrandGrid') {
        // 적용대상브랜드
        gridNm = '적용대상브랜드';
        bCheckBrandId   = true;               // 브랜드ID 체크
      }
    }

    // ------------------------------------------------------------------------
    // 그리드 크기 체크
    // ------------------------------------------------------------------------
    if (goodsList == undefined || goodsList == null || goodsList == '' || goodsList.length <= 0) {
      resultCheck.result = false;
      resultCheck.message = '<font color="#FF1A1A">['+gridNm+']</font> 필수 입력입니다.';
      return resultCheck;
    }

    //console.log('# gbExhibitTp      :: ', gbExhibitTp);
    //console.log('# gridId           :: ', gridId);
    //console.log('# giftGiveTp       :: ', $('#giftGiveTp').val());
    //console.log('# goodsList.length :: ', goodsList.length);
    if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // 증정행사인 경우
      if (gridId == 'giftGoodsGrid') {
        // 증정상품인 경우
        if ($('#giftGiveTp').val() == 'GIFT_GIVE_TP.FIXED') {
          // 증정방식이 지정인 경우
          if (goodsList.length > 1) {
            resultCheck.result = false;
            resultCheck.message = '증정방식이 지정일경우 1개의 증정상품만 등록하실 수 있습니다.';
            return resultCheck
          }
        }
      }
    }

    // ------------------------------------------------------------------------
    // 그리드 항목 체크
    // ------------------------------------------------------------------------
    for (var i = 0; i < goodsList.length; i++) {

      goodsObj = goodsList[i];

      // ----------------------------------------------------------------------
      // 상품순번
      // ----------------------------------------------------------------------
      if (bCheckGoodsSort == true) {
        if (goodsObj.goodsSort == undefined || goodsObj.goodsSort == null) {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][순번]</font> 필수 입력입니다.';
          //resultCheck.message = '<font color="#FF1A1A">['+gridNm+'('+Number(i+1)+' Row)][순번]</font> 필수 입력입니다.';
          break;
        }
      }
      // ----------------------------------------------------------------------
      // 상품코드
      // ----------------------------------------------------------------------
      if (bCheckIlGoodsId == true) {
        if (goodsObj.ilGoodsId == undefined || goodsObj.ilGoodsId == null || goodsObj.ilGoodsId == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][상품코드]</font> 필수 입력입니다.';
          //resultCheck.message = '<font color="#FF1A1A">['+gridNm+'('+Number(i+1)+' Row)][상품코드]</font> 필수 입력입니다.';
          break;
        }
      }
      // ----------------------------------------------------------------------
      // 증정기본수량
      // ----------------------------------------------------------------------
      if (bCheckGiftCnt == true) {
        if (goodsObj.giftCnt == undefined || goodsObj.giftCnt == null || goodsObj.giftCnt == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][증정기본수량]</font> 필수 입력입니다.';
          //resultCheck.message = '<font color="#FF1A1A">['+gridNm+'('+Number(i+1)+' Row)][증정기본수량]</font> 필수 입력입니다.';
          break;
        }
      }
      // ----------------------------------------------------------------------
      // 브랜드ID
      // ----------------------------------------------------------------------
      if (bCheckBrandId == true) {
        if (goodsObj.brandId == undefined || goodsObj.brandId == null || goodsObj.brandId == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][브랜드ID]</font> 필수 입력입니다.';
          //resultCheck.message = '<font color="#FF1A1A">['+gridNm+'('+Number(i+1)+' Row)][브랜드ID]</font> 필수 입력입니다.';
          break;
        }
      }
      // ----------------------------------------------------------------------
      // 판매가
      // ----------------------------------------------------------------------
      if (bCheckSalePrice == true) {
        if (goodsObj.salePrice == undefined || goodsObj.salePrice == null || goodsObj.salePrice == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][판매가]</font> 필수 입력입니다.';
          //resultCheck.message = '<font color="#FF1A1A">['+gridNm+'('+Number(i+1)+' Row)][판매가]</font> 필수 입력입니다.';
          break;
        }

        //console.log('# 판매가 정상가 비교[', i, ']  :: ', goodsObj.salePrice, ' || ', goodsObj.recommendedPrice);
        //console.log('# goodsObj[', i, ']            :: ', JSON.stringify(goodsObj));
        if (Number(goodsObj.salePrice) > Number(goodsObj.recommendedPrice)) {
          resultCheck.result = false;
          resultCheck.message = '추가상품의 판매가를 확인해주세요 (정상가 이하 등록가능)';
          //resultCheck.message = '<font color="#FF1A1A">['+gridNm+'('+Number(i+1)+' Row)][판매가]</font>를 확인해주세요 (정상가 이하 등록가능)';
          break;
        }

      }
      // ----------------------------------------------------------------------
      // 골라담기 노출여부 공백 개수
      // ----------------------------------------------------------------------
      if (bCheckSelectDisplayYn == true) {
        if (goodsObj != undefined && goodsObj != null) {
          if (goodsObj.displayYnNm == undefined || goodsObj.displayYnNm == null || goodsObj.displayYnNm == '') {
            selectDisplayYnEmptyCnt++;
          }
        }
      }
      // ----------------------------------------------------------------------
      // 증정행사 대표상품여부 수 체크
      // ----------------------------------------------------------------------
      if (bCheckRepGoodsYn == true) {
        if (goodsObj != undefined && goodsObj != null && goodsObj.repGoodsYn == 'Y') {
          giftRepGoodsYnCnt++;
        }
      }
    } // End of for (var i = 0; i < goodsList.length; i++)

    // ------------------------------------------------------------------------
    // 결과 체크
    // ------------------------------------------------------------------------
    if (resultCheck.result == true) {
      // 선행 체크 오류가 없는 경우

      if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
        // --------------------------------------------------------------------
        // 골라담기
        // --------------------------------------------------------------------

        // --------------------------------------------------------------------
        // 골라담기 노출여부 공백수 결과 체크
        // --------------------------------------------------------------------
        if (resultCheck.result == true) {
          // 체크 결과가 True 이라면
          if (selectDisplayYnEmptyCnt > 0) {
            // 대표상품이 존재하지 않는다면
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][단가적용]</font> 필수 입력입니다.';
          }
        }

        // --------------------------------------------------------------------
        //
        // --------------------------------------------------------------------
      }
      else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
        // --------------------------------------------------------------------
        // 증정행사
        // --------------------------------------------------------------------

        // --------------------------------------------------------------------
        // 증정행사 대표상품여부 결과 체크
        // --------------------------------------------------------------------
        if (gridId == 'giftGoodsGrid') {
          // 증정상품
          if (giftRepGoodsYnCnt <= 0) {
            // 대표상품이 존재하지 않는다면
            resultCheck.result = false;
            resultCheck.message = '<font color="#FF1A1A">['+gridNm+'][대표상품 선택]</font> 필수 입력입니다.';
          }
        }
      }
    }

    return resultCheck;
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

    let isSaveAbleYn  = false;
    let isDelAbleYnYn = false;
    let detail = data.detail;


    let exhibitTp       = detail.exhibitTp;
    let useYn           = detail.useYn;
    let statusSe        = detail.statusSe;
    let approvalStatus  = detail.approvalStatus
    //let exhibitStatus   = detail.exhibitStatus
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# useYn          :: ', useYn);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);
    //console.log('# exhibitStatus  :: ', exhibitStatus);
    //console.log('# data.detail    :: ', JSON.stringify(data.detail));

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

    // ------------------------------------------------------------------------
    // 하단 저장/삭제 버튼
    // ------------------------------------------------------------------------
    if (detail.exhibitTp == 'EXHIBIT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반기획전
      // ----------------------------------------------------------------------
      // ----------------------------------------------------------------------
      // 삭제버튼
      // ----------------------------------------------------------------------
      if (detail.statusSe != undefined && detail.statusSe != null && detail.statusSe == 'BEF') {
        // 진행상태 : 진행예정인
        $('#btnDel').attr('disabled', false);     // 삭제버튼 활성
      }
      else {
        // 진행상태 : 진행중/진행완료
        $('#btnDel').attr('disabled', true);     // 삭제버튼 비활성
      }

    }
    else {
      // ----------------------------------------------------------------------
      // 골라담기/증정행사
      // ----------------------------------------------------------------------
      // ----------------------------------------------------------------------
      // 저장버튼
      // ----------------------------------------------------------------------
      if ( detail.approvalStatus == 'APPR_STAT.NONE' || detail.approvalStatus == 'APPR_STAT.CANCEL' || detail.approvalStatus == 'APPR_STAT.DENIED' ||
          (detail.approvalStatus == 'APPR_STAT.APPROVED' && (detail.statusSe == 'BEF' || detail.statusSe == 'ING' || detail.statusSe == 'END')) ){
        // 승인상태:승인대기/요청철회/승인반려/(승인완료 중 진행예정/진행중) : 수정가능
        $('#fnBtnSave').attr('disabled', false);    // 저장버튼 활성
        isSaveAbleYn = true;

      }
      else {
        $('#fnBtnSave').attr('disabled', true);     // 저장버튼 비활성
        isSaveAbleYn = false;
      }
      // ----------------------------------------------------------------------
      // 삭제버튼
      // ----------------------------------------------------------------------
      if (detail.approvalStatus == 'APPR_STAT.NONE' || detail.approvalStatus == 'APPR_STAT.CANCEL' || detail.approvalStatus == 'APPR_STAT.DENIED' ) {
        // 승인상태 : 승인대기/요청철회/승인반려 : 삭제 가능
        // 진행상태가 진행예정인 경우
        $('#btnDel').attr('disabled', false);     // 삭제버튼 활성
        isDelAbleYnYn = true;
      }
      else {
        // 승인상태 : 승인요청/승인완료(부)/승인완료
        $('#btnDel').attr('disabled', true);     // 삭제버튼 비활성
        isDelAbleYnYn = false;
      }
    }

  }

  // ==========================================================================
  // # 기본정보 활성/비활성 처리
  // ==========================================================================
  function fnSetBasicInfo(detail, editableMode) {
    //console.log('# ==================================================');
    //console.log('# fnSetBasicInfo Start');
    let exhibitTp       = detail.exhibitTp;
    let useYn           = detail.useYn;
    let statusSe        = detail.statusSe;
    let approvalStatus  = detail.approvalStatus
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
    if (editableMode == 'Y' || editableMode == 'P') {
      // ----------------------------------------------------------------------
      // 수정가능
      // ----------------------------------------------------------------------
      // 몰구분
      $('input[name=mallDiv]').attr('disabled', false);                           // radio
      // 사용여부
      $('input[name=useYn]').attr('disabled', false);                             // radio
      // 전시여부
      $('input[name=dispYn]').attr('disabled', false);                            // radio
      // 기획전전시여부
      $('input[name=exhibitDispYn]').attr('disabled', false);                     // radio
      // 기획전제목
      $('input[name=title]').attr('disabled', false);                             // input text
      // 기획전설명
      $('#description').attr('disabled', false);                                  // textarea
      // 노출범위설정(디바이스)
      $('input[name=goodsDisplayType]').attr('disabled', false);                  // checkbox
      // 비회원접근권한
      $('input[name=dispNonmemberYn]').attr('disabled', false);                   // radio
      // 임직원전용여부
      $('input[name=evEmployeeTp]').attr('disabled', false);                      // radio
      // 접근권한(회원등급레벨)-그룹
      $('#userMaster').data('kendoDropDownList').enable(true);                    // kendoDropDownList
      // 접근권한(회원등급레벨)-등급
      $('#userGroup').data('kendoDropDownList').enable(true);                     // kendoDropDownList
      // 접근권한(회원등급레벨)-추가 버튼
      $('#btnAddUserGroup').prop('disabled', false);                              // button
      // 접근권한(회원등급레벨)-삭제 버튼
      $('.js__remove__userGroupBtn').prop('disabled', false);                     // button
      // 상시진행 체크박스
      $('input[name=alwaysYn]').attr('disabled', false);                          // checkbox
      // 진행기간
      fnSetExhibitAlwaysYn(alwaysYn);
      //$('#startDe').data('kendoDatePicker').enable(true);                         // kendoDatePicker
      //$('#startHour').data('kendoDropDownList').enable(true);                     // kendoDropDownList
      //$('#startMin').data('kendoDropDownList').enable(true);                      // kendoDropDownList
      //$('#endDe').data('kendoDatePicker').enable(true);                           // kendoDatePicker
      //$('#endHour').data('kendoDropDownList').enable(true);                       // kendoDropDownList
      //$('#endMin').data('kendoDropDownList').enable(true);                        // kendoDropDownList
      // 기간후종료 체크박스
      $('input[name=timeOverCloseYn]').attr('disabled', false);                   // checkbox
      $('input[name=timeOverCloseYn]').prop("checked",true);                      // checkbox
      // 배너이미지 버튼
      $('#imgBanner').prop('disabled', false);                                    // button
      $('#imgBannerDel').prop('disabled', false);                                 // button
      // 기획전상세(PC)
      $($('#detlHtmlPc').data().kendoEditor.body).attr('contenteditable', true);  // kendoEditor
      // 기획전상세(Mobile)
      $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', true);  // kendoEditor

    }
    else {
      // ----------------------------------------------------------------------
      // 수정불가
      // ----------------------------------------------------------------------
      // 몰구분
      $('input[name=mallDiv]').attr('disabled', true);                            // radio
      // 사용여부
      $('input[name=useYn]').attr('disabled', false);                              // radio
      // 전시여부
      $('input[name=dispYn]').attr('disabled', true);                             // radio
      // 기획전전시여부
      $('input[name=exhibitDispYn]').attr('disabled', true);                      // radio
      // 기획전제목
      $('input[name=title]').attr('disabled', true);                              // input text
      // 기획전설명
      $('#description').attr('disabled', true);                                   // textarea
      // 노출범위설정(디바이스)
      $('input[name=goodsDisplayType]').attr('disabled', true);                   // checkbox
      // 비회원접근권한
      $('input[name=dispNonmemberYn]').attr('disabled', true);                    // radio
      // 임직원전용여부
      $('input[name=evEmployeeTp]').attr('disabled', true);                       // radio
      // 접근권한(회원등급레벨)-그룹
      $('#userMaster').data('kendoDropDownList').enable(false);                   // kendoDropDownList
      // 접근권한(회원등급레벨)-등급
      $('#userGroup').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      // 접근권한(회원등급레벨)-추가 버튼
      $('#btnAddUserGroup').prop('disabled', true);                               // button
      // 접근권한(회원등급레벨)-삭제 버튼
      $('.js__remove__userGroupBtn').prop('disabled', true);                      // button
      // 상시진행 체크박스
      $('input[name=alwaysYn]').attr('disabled', true);                           // checkbox
      // 진행기간
      $('#startDe').data('kendoDatePicker').enable(false);                        // kendoDatePicker
      $('#startHour').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      $('#startMin').data('kendoDropDownList').enable(false);                     // kendoDropDownList
      $('#endDe').data('kendoDatePicker').enable(false);                          // kendoDatePicker
      $('#endHour').data('kendoDropDownList').enable(false);                      // kendoDropDownList
      $('#endMin').data('kendoDropDownList').enable(false);                       // kendoDropDownList
      // 기간후종료 체크박스
      $('input[name=timeOverCloseYn]').attr('disabled', true);                    // checkbox
      $('input[name=timeOverCloseYn]').prop("checked",true);                      // checkbox
      // 배너이미지 버튼
      $('#imgBanner').prop('disabled', true);                                     // button
      $('#imgBannerDel').prop('disabled', true);                                  // button
      // 기획전상세(PC)
      $($('#detlHtmlPc').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
      // 기획전상세(Mobile)
      $($('#detlHtmlMo').data().kendoEditor.body).attr('contenteditable', false); // kendoEditor
    }
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
    else if (exhibitTp == 'EXHIBIT_TP.SELECT') {
      // ----------------------------------------------------------------------
      // 골라담기
      // ----------------------------------------------------------------------
      fnSetDetailSelect(detail, editableMode);
    }
    else if (exhibitTp == 'EXHIBIT_TP.GIFT') {
    // ----------------------------------------------------------------------
    // 증정행사
    // ----------------------------------------------------------------------
      fnSetDetailGift(detail, editableMode);
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
    if(alwaysYn == true || alwaysYn == 'Y') {
      // 체크 -> 기간, 자동종료 비활성
      $("#startDe").data("kendoDatePicker").enable(false);
      $("#startHour").data("kendoDropDownList").enable(false);
      $("#startMin").data("kendoDropDownList").enable(false);
      $("#endDe").data("kendoDatePicker").enable(false);
      $("#endHour").data("kendoDropDownList").enable(false);
      $("#endMin").data("kendoDropDownList").enable(false);
      //$('#timeOverCloseYn_0').attr('disabled', true);
      $('input[name="timeOverCloseYn"]').attr('disabled', true);
    }
    else {
      // 해제 -> 기간, 자동종료 활성
      $("#startDe").data("kendoDatePicker").enable(true);
      $("#startHour").data("kendoDropDownList").enable(true);
      $("#startMin").data("kendoDropDownList").enable(true);
      $("#endDe").data("kendoDatePicker").enable(true);
      $("#endHour").data("kendoDropDownList").enable(true);
      $("#endMin").data("kendoDropDownList").enable(true);
      $('#timeOverCloseYn').attr('disabled', false);
      $('input[name="timeOverCloseYn"]').attr('disabled', false);
    }
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
