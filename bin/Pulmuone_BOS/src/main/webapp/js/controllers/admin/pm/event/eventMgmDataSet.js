/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트 등록/수정 - 소스분리 - DataSet
 * @             - 저장/수정/상세조회 DataSet
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.17    dgyoun        최초 생성
 * @
 ******************************************************************************/

var gbValidData = new Object();   // 반환데이터 (중복로직 회피용)


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록/수정 DataSet Start
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // ==========================================================================
  // # Validataion Check
  // ==========================================================================
  function fnCheckValidatiaon() {
    //console.log('# fnCheckValidatiaon Start [', gbEventTp, ']');

    // 체크 반환데이터 초기화
    gbValidData = new Object();

    // ------------------------------------------------------------------------
    // 필수체크 - 공통
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // 이벤트 분류(유형)
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueItemForId('eventTp')) == true) {
      fnMessage('', '<font color="#FF1A1A">[이벤트 분류]</font> 필수 입력입니다.', 'eventTp', 'kendoDropDownList');
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
    // 이벤트제목 [Text]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueItemForId('title')) == true) {
      fnMessage('', '<font color="#FF1A1A">[이벤트 제목]</font> 필수 입력입니다.', 'title');
      return false;
    }
    // ------------------------------------------------------------------------
    // 노출범위(디바이스) [체크]
    // ------------------------------------------------------------------------
    if (fnIsEmpty(getValueCheckBoxForName('goodsDisplayType')) == true) {
      fnMessage('', '<font color="#FF1A1A">[노출범위(디바이스)]</font> 필수 입력입니다.', 'goodsDisplayType_0');
      return false;
    }
    // ------------------------------------------------------------------------
    // 임직원 전용여부 [라디오]
    // ------------------------------------------------------------------------
    if (gbEventTp != 'EVENT_TP.NORMAL') {
      if (fnIsEmpty(getValueRadioForName('evEmployeeTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[임직원 전용여부]</font> 필수 입력입니다.', 'evEmployeeTp');
        return false;
      }
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


    // 당첨자선정기간 / 후기작성기간
    if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      // ----------------------------------------------------------------------
      // 당첨자선정기간
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('selectStartDe')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자선정 시작일자]</font> 필수 입력입니다.', 'selectStartDe');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('selectStartHour')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자선정 시작시]</font> 필수 입력입니다.', 'selectStartHour');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('selectStartMin')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자선정 시작분]</font> 필수 입력입니다.', 'selectStartMin');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('selectEndDe')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자선정 종료일자]</font> 필수 입력입니다.', 'selectEndDe');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('selectEndHour')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자선정 종료시]</font> 필수 입력입니다.', 'selectEndHour');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('selectEndMin')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자선정 종료분]</font> 필수 입력입니다.', 'selectEndMin');
        return false;
      }
      // ----------------------------------------------------------------------
      // 후기작성기간
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('feedbackStartDe')) == true) {
        fnMessage('', '<font color="#FF1A1A">[후기작성 시작일자]</font> 필수 입력입니다.', 'feedbackStartDe');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('feedbackStartHour')) == true) {
        fnMessage('', '<font color="#FF1A1A">[후기작성 시작시]</font> 필수 입력입니다.', 'feedbackStartHour');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('feedbackStartMin')) == true) {
        fnMessage('', '<font color="#FF1A1A">[후기작성 시작분]</font> 필수 입력입니다.', 'feedbackStartMin');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('feedbackEndDe')) == true) {
        fnMessage('', '<font color="#FF1A1A">[후기작성 종료일자]</font> 필수 입력입니다.', 'feedbackEndDe');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('feedbackEndHour')) == true) {
        fnMessage('', '<font color="#FF1A1A">[후기작성 종료시]</font> 필수 입력입니다.', 'feedbackEndHour');
        return false;
      }
      if (fnIsEmpty(getValueItemForId('feedbackEndMin')) == true) {
        fnMessage('', '<font color="#FF1A1A">[후기작성 종료분]</font> 필수 입력입니다.', 'feedbackEndMin');
        return false;
      }
    }

    // ------------------------------------------------------------------------
    // 배너이미지(전체/PC) [Img]
    // ------------------------------------------------------------------------
    if ($('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').is(':checked') == true ||
        $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_PC"]').is(':checked') == true) {
      if (fnIsEmpty(gbImgBnrPcPath) == true) {
        fnMessage('', '<font color="#FF1A1A">[배너 이미지(PC)]</font> 필수 입력입니다.', 'timeOverCloseYn_0'); // 제일 가까운 항목으로 포커스 이동
        return false;
      }
    }
    // ------------------------------------------------------------------------
    // 배너이미지(전체/MWeb/App) [Img]
    // ------------------------------------------------------------------------
    if ($('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').is(':checked') == true ||
        $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_MOBILE"]').is(':checked') == true ||
        $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.APP"]').is(':checked') == true) {
      if (fnIsEmpty(gbImgBnrMobilePath) == true) {
        fnMessage('', '<font color="#FF1A1A">[배너 이미지(Mobile)]</font> 필수 입력입니다.', 'timeOverCloseYn_0'); // 제일 가까운 항목으로 포커스 이동
        return false;
      }
    }
    // ------------------------------------------------------------------------
    // 이벤트상세(PC) [Edit]
    // ------------------------------------------------------------------------
    if ($('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').is(':checked') == true ||
        $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_PC"]').is(':checked') == true) {
      if ($('#detlHtmlPc').val() == '') {
        fnMessage('', '<font color="#FF1A1A">[이벤트 상세(PC)]</font> 필수 입력입니다.', 'detlHtmlPc', 'kendoEditor');
        return false;
      }
    }
    // ------------------------------------------------------------------------
    // 이벤트상세(Mobile) [Edit]
    // ------------------------------------------------------------------------
    if ($('input:checkbox[name="goodsDisplayType"]:input[value="ALL"]').is(':checked') == true ||
        $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.WEB_MOBILE"]').is(':checked') == true ||
        $('input:checkbox[name="goodsDisplayType"]:input[value="GOODS_DISPLAY_TYPE.APP"]').is(':checked') == true) {
      if ($('#detlHtmlMo').val() == '') {
        fnMessage('', '<font color="#FF1A1A">[이벤트 상세(Mobile)]</font> 필수 입력입니다.', 'detlHtmlMo', 'kendoEditor');
        return false;
      }
    }

    // ------------------------------------------------------------------------
    // 이벤트유형별 Validation
    // ------------------------------------------------------------------------
    if (gbEventTp == 'EVENT_TP.NORMAL') {
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 일반이벤트
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

      // ----------------------------------------------------------------------
      // 댓글허용여부 [라디오]
      // ----------------------------------------------------------------------
//      if (fnIsEmpty(getValueRadioForName('commentYn')) == true) {
//        fnMessage('', '<font color="#FF1A1A">[댓글 허용여부]</font> 필수 입력입니다.', 'commentYn');
//        return false;
//      }
      var commentYnVal = $('input[name=commentYn]:checked').val();

      var normalEventTpVal = $('input[name=normalEventTp]:checked').val();

//      if (commentYnVal == 'Y') {
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 댓글허용인 경우만 필수 체크
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (normalEventTpVal != 'NORMAL_EVENT_TP.NONE') {
        // --------------------------------------------------------------------
        // 임직원참여여부 [라디오]
        // --------------------------------------------------------------------
        if (fnIsEmpty(getValueRadioForName('employeeJoinYn')) == true) {
          fnMessage('', '<font color="#FF1A1A">[임직원 참여여부]</font> 필수 입력입니다.', 'employeeJoinYn');
          return false;
        }
        // --------------------------------------------------------------------
        // 참여횟수설정(ID당) [콤보]
        // --------------------------------------------------------------------
        if (fnIsEmpty(getValueItemForId('eventJoinTp')) == true) {
          fnMessage('', '<font color="#FF1A1A">[참여횟수 설정 (ID당)]</font> 필수 입력입니다.', 'eventJoinTp', 'kendoDropDownList');
          return false;
        }
        // --------------------------------------------------------------------
        // 당첨자설정 [라디오]
        // --------------------------------------------------------------------
        if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
          fnMessage('', '<font color="#FF1A1A">[당첨자 설정]</font> 필수 입력입니다.', 'eventDrawTp');
          return false;
        }

        if($('input[name=eventDrawTp]:checked').val() == 'EVENT_DRAW_TP.AUTO'){
            if(Number($('#expectJoinUserCnt').val()) > 1000000){
                fnMessage('', '  예상참여자수가 1,000,000명을 초과할 수 없습니다.  ', '');
                return false;
            }

        }

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 참여방법 : 댓글응모
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (normalEventTpVal == 'COMMENT') {
            // --------------------------------------------------------------------
            // 댓글분류설정 [라디오]
            // --------------------------------------------------------------------
            if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
              fnMessage('', '<font color="#FF1A1A">[댓글 분류설정]</font> 필수 입력입니다.', 'commentCodeYn');
              return false;
            }

            // --------------------------------------------------------------------
            // 댓글분류 [Text]
            // --------------------------------------------------------------------
            var commentCodeYn = $('input[name=commentCodeYn]:checked').val();

            if (commentCodeYn == 'Y'){
              if (gbCommentCodeMaps.size == 0) {
                fnMessage('', '<font color="#FF1A1A">[댓글 분류]</font> 필수 입력입니다.', 'inputValue');
                return false;
              }
            }
        }
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 당첨혜택 영역
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        var eventBenefitTp = $('input[name=eventBenefitTp]:checked').val();

        if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
          // ------------------------------------------------------------------
          // 당첨혜택 - 쿠폰 [그리드]
          // ------------------------------------------------------------------
          var targetGrid    = $('#benefitCouponGrid').data('kendoGrid');
          var targetGridDs  = targetGrid.dataSource;
          var targetGridArr = targetGridDs.data();

          if (targetGridArr == undefined || targetGridArr == null || targetGridArr == '' || targetGridArr.length <= 0) {
            fnMessage('', '<font color="#FF1A1A">[당첨자혜택(쿠폰)]</font> 필수 입력입니다.', '');
            return false;
          }

          if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
            //console.log('# targetGridArr.length :: ', targetGridArr.length);
            for (var i = 0; i < targetGridArr.length; i++) {
              var couponCnt   = $('#benefitCouponGrid tbody input[name=couponCount]')[i].value;
              if (couponCnt == undefined || couponCnt == null || couponCnt == '' || Number(couponCnt) < 1) {
                fnMessage('', '<font color="#FF1A1A">[계정당 지급 수량]</font> 필수 입력입니다.', '');
                return false;
              }
              var couponTotalCnt   = $('#benefitCouponGrid tbody input[name=couponTotalCount]')[i].value;
              if (couponTotalCnt == undefined || couponTotalCnt == null || couponTotalCnt == '') {
                fnMessage('', '<font color="#FF1A1A">[총 당첨 수량]</font>을 확인 해 주세요.', '');
                return false;
              }
              if (Number(couponTotalCnt) < 1) {
                fnMessage('', '<font color="#FF1A1A">[총 당첨 수량]</font> 1이상만 입력 가능합니다.', '');
                return false;
              }
            } // End of for (var i = 0; i < targetGridArr.length; i++)
          } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)

        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
          // ------------------------------------------------------------------
          // 당첨혜택 - 적립금 [그리드]
          // ------------------------------------------------------------------
          var targetGrid    = $('#benefitPointGrid').data('kendoGrid');
          var targetGridDs  = targetGrid.dataSource;
          var targetGridArr = targetGridDs.data();

          if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
            fnMessage('', '<font color="#FF1A1A">[당첨자혜택(적립금)]</font> 필수 입력입니다.', '');
            return false;
          }
          if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length > 1) {
            fnMessage('', '<font color="#FF1A1A">[당첨자혜택(적립금)]</font> 1건만 입력가능합니다.', '');
            return false;
          }
          var totalWinCnt   = $('#benefitPointGrid tbody input[name=totalWinCount]').val();
          if (totalWinCnt == undefined || totalWinCnt == null || totalWinCnt == '') {
            fnMessage('', '<font color="#FF1A1A">[총 당첨 수량]</font>을 확인 해 주세요.', '');
            return false;
          }
          if (Number(totalWinCnt) < 1) {
            fnMessage('', '<font color="#FF1A1A">[총 당첨 수량]</font> 1이상만 입력 가능합니다.', '');
            return false;
          }
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
          // ------------------------------------------------------------------
          // 당첨혜택 - 경품 [Text]
          // ------------------------------------------------------------------
          let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
          if (eventDrawTp == 'EVENT_DRAW_TP.AUTO') {
            // 당첨자설정 : 즉시(EVENT_DRAW_TP.AUTO)
            if (fnIsEmpty(getValueItemForId('benefitNmGift')) == true) {
              fnMessage('', '<font color="#FF1A1A">[경품명]</font> 필수 입력입니다.', 'benefitNmGift');
              return false;
            }
          }
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
          // ------------------------------------------------------------------
          // 당첨혜택 - 응모 [Text]
          // ------------------------------------------------------------------
          let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
          if (eventDrawTp == 'EVENT_DRAW_TP.AUTO') {
            // 당첨자설정 : 즉시(EVENT_DRAW_TP.AUTO)
            if (fnIsEmpty(getValueItemForId('benefitNmEnter')) == true) {
              fnMessage('', '<font color="#FF1A1A">[자동응모문구]</font> 필수 입력입니다.', 'benefitNmEnter');
              return false;
            }
          }
        }
      }
    }
    else if (gbEventTp == 'EVENT_TP.SURVEY') {
      // ----------------------------------------------------------------------
      // 설문이벤트
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 임직원참여여부 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('employeeJoinYn')) == true) {
        fnMessage('', '<font color="#FF1A1A">[임직원 참여여부]</font> 필수 입력입니다.', 'employeeJoinYn');
        return false;
      }
      // ----------------------------------------------------------------------
      // 참여횟수설정(ID당) [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('eventJoinTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[참여횟수 설정 (ID당)]</font> 필수 입력입니다.', 'eventJoinTp', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 당첨자설정 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자 설정]</font> 필수 입력입니다.', 'eventDrawTp');
        return false;
      }

      if($('input[name=eventDrawTp]:checked').val() == 'EVENT_DRAW_TP.AUTO'){
          if(Number($('#expectJoinUserCnt').val()) > 1000000){
              fnMessage('', '  예상참여자수가 1,000,000명을 초과할 수 없습니다.  ', '');
              return false;
          }
      }

      // ----------------------------------------------------------------------
      // 댓글분류설정 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[댓글 분류설정]</font> 필수 입력입니다.', 'commentCodeYn');
        return false;
      }
      // ----------------------------------------------------------------------
      // 댓글분류 [Text]
      // ----------------------------------------------------------------------
      var commentCodeYn = $('input[name=commentCodeYn]:checked').val();

      if (commentCodeYn == 'Y'){
        var tmpList = Array.from(gbCommentCodeMaps.values());
        if (fnIsEmpty(getValueItemForId('inputValue')) == true) {
          fnMessage('', '<font color="#FF1A1A">[댓글 분류]</font> 필수 입력입니다.', 'inputValue');
          return false;
        }
      }
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 당첨혜택 영역
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      var eventBenefitTp = $('input[name=eventBenefitTp]:checked').val();

      if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
        // --------------------------------------------------------------------
        // 당첨혜택 - 쿠폰 [그리드]
        // --------------------------------------------------------------------
        var targetGrid    = $('#benefitCouponGrid').data('kendoGrid');
        var targetGridDs  = targetGrid.dataSource;
        var targetGridArr = targetGridDs.data();

        if (targetGridArr == undefined || targetGridArr == null || targetGridArr == '' || targetGridArr.length <= 0) {
          fnMessage('', '<font color="#FF1A1A">[당첨자혜택(쿠폰)]</font> 필수 입력입니다.', '');
          return false;
        }

        if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
          //console.log('# targetGridArr.length :: ', targetGridArr.length);
          for (var i = 0; i < targetGridArr.length; i++) {
            var couponCnt   = $('#benefitCouponGrid tbody input[name=couponCount]')[i].value;
            if (couponCnt == undefined || couponCnt == null || couponCnt == '' || Number(couponCnt) < 1) {
              fnMessage('', '<font color="#FF1A1A">[계정당 지급 수량]</font> 필수 입력입니다.', '');
              return false;
            }


          } // End of for (var i = 0; i < targetGridArr.length; i++)
        } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)

      }
      else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
        // --------------------------------------------------------------------
        // 당첨혜택 - 적립금 [그리드]
        // --------------------------------------------------------------------
        var targetGrid    = $('#benefitPointGrid').data('kendoGrid');
        var targetGridDs  = targetGrid.dataSource;
        var targetGridArr = targetGridDs.data();

        if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
          fnMessage('', '<font color="#FF1A1A">[당첨자혜택(적립금)]</font> 필수 입력입니다.', '');
          return false;
        }
        if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length > 1) {
          fnMessage('', '<font color="#FF1A1A">[당첨자혜택(적립금)]</font> 1건만 입력가능합니다.', '');
          return false;
        }
      }
      else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
        // --------------------------------------------------------------------
        // 당첨혜택 - 경품 [Text]
        // --------------------------------------------------------------------
        let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
        if (eventDrawTp == 'EVENT_DRAW_TP.AUTO') {
          // 당첨자설정 : 즉시(EVENT_DRAW_TP.AUTO)
          if (fnIsEmpty(getValueItemForId('benefitNmGift')) == true) {
            fnMessage('', '<font color="#FF1A1A">[경품명]</font> 필수 입력입니다.', 'benefitNmGift');
            return false;
          }
        }
      }
      else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
        // --------------------------------------------------------------------
        // 당첨혜택 - 응모 [Text]
        // --------------------------------------------------------------------
        let eventDrawTp = $('input[name=eventDrawTp]:checked').val(); // 당첨자설정(관리자추첨/즉시당첨 등)
        if (eventDrawTp == 'EVENT_DRAW_TP.AUTO') {
          // 당첨자설정 : 즉시(EVENT_DRAW_TP.AUTO)
          if (fnIsEmpty(getValueItemForId('benefitNmEnter')) == true) {
            fnMessage('', '<font color="#FF1A1A">[자동응모문구]</font> 필수 입력입니다.', 'benefitNmEnter');
            return false;
          }
        }
      }

      // **********************************************************************
      // 설문 영역 Validataion
      // **********************************************************************
      // console.log('# 설문이벤트 fnCheckValidatiaon 설문영역 Start');
      let eventSurveyQuestionList = new Array();  // EventSurveyVo         설문리스트
      let eventSurveyItemList     = new Array();  // EventSurveyItemVo     설문아이템리스트
      let eventSurveyItemAttcList = new Array();  // EventSurveyItemAttcVo 설문첨부파일리스트리스트 (현재 설문아이템리스트와 1:1 이지만 List로 처리함)
      let eventSurveyQuestionData;
      let eventSurveyItemData;
      let eventSurveyItemAttcData;
      var surveyIdxArr = new Array();

      // ----------------------------------------------------------------------
      // 당첨혜택영역 스탬프 idx 조회
      // ----------------------------------------------------------------------
      $('tbody#surveyTbody tr').each(function() {
        var surveyTrId = $(this).attr('id');

        if (fnIsEmpty(surveyTrId) == false) {
          //console.log('# surveyTrId :: ', surveyTrId);
          let surveyTrIdArr = surveyTrId.split('surveyTmplTr_');
          let surveyIdx = surveyTrIdArr[1];
          //console.log('# surveyIdx :: ', surveyIdx);
          surveyIdxArr.push(surveyIdx);
        }
      });

      let surveyIdxCnt = surveyIdxArr.length;

      if (gbSurveySubCurrentRowIndexMap == undefined || gbSurveySubCurrentRowIndexMap == null || gbSurveySubCurrentRowIndexMap == '' || gbSurveySubCurrentRowIndexMap.size < 0) {
        //console.log('# gbSurveySubCurrentRowIndexMap.size :: ', gbSurveySubCurrentRowIndexMap.size);
        fnMessage('', '설문의 보기 정보를 확인하세요.', '');
        return false;
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 설문영역 값 체크 : N개
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      for (var i = 0; i < surveyIdxCnt; i++ ) {

        eventSurveyQuestionData = new Object(); // 설문
        eventSurveyItemList     = new Array();  // 아이템리스트
        eventSurveyItemAttcList = new Array();  // 파일리스트

        let indexKey = surveyIdxArr[i];
        //console.log('# indexKey :: ', indexKey);

        // --------------------------------------------------------------------
        // 설문제목
        // --------------------------------------------------------------------
        let title = $('#title'+indexKey).val();
        if (title == undefined || title == null || title == '') {
          fnMessage('', '설문'+ (i+1) +'의 설문제목을 확인하세요.', 'title'+indexKey);
          return false;
        }
        // @@@@@ 설문제목
        eventSurveyQuestionData.title = title;

        // @@@@@ 설문유형 (단일선택/복수선택)
        eventSurveyQuestionData.eventSurveyTp = $('input[name=eventSurveyTp'+indexKey+']:checked').val();

        // @@@@@ 순서
        eventSurveyQuestionData.sort = i+1;

        // --------------------------------------------------------------------
        // 설문아이템
        // --------------------------------------------------------------------
        let subIndexKeyMax = 0;

        if (gbSurveySubCurrentRowIndexMap == undefined || gbSurveySubCurrentRowIndexMap == null || gbSurveySubCurrentRowIndexMap.size <= 0) {
          subIndexKeyMax = gbSurveySubCurrentRowIndexMap.get(indexKey+'');
          //console.log('# subIndexKeyMax :: ', subIndexKeyMax);
          fnMessage('', '설문'+ (i+1) +'의 보기 정보를 확인하세요.', '');
          return false;
        }

        // --------------------------------------------------------------------
        // 설문별 아이템 영역 값 체크 : N개
        // --------------------------------------------------------------------
        const $surveyItemArr = $('input[id^="item'+indexKey+'_"]');
        //console.log('# $surveyItemArr[', indexKey, '] :: ', $surveyItemArr.length);
        let itemIdx = 0;
        let errMsg = '';

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 보기 수만큼 loop Start
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        $surveyItemArr.each(function(){

          eventSurveyItemData = new Object(); // 단위 보기

          let itemId = $(this).attr('id');
          let itemIdArr = itemId.split('item');
          let indexArr  = itemIdArr[1].split('_');;
          let subIndexKey = indexArr[1];
          //console.log('# ##### subIndexKey :: ', subIndexKey);

          // ------------------------------------------------------------------
          // 보기
          // ------------------------------------------------------------------
          let item = $('#'+itemId).val();
          //console.log('# item :: ', item);

          // ------------------------------------------------------------------
          // 직접입력
          // ------------------------------------------------------------------
          let directInputYn = '';
          if($('input:checkbox[name=directInputYn'+indexKey+'_'+subIndexKey+']').is(':checked') == true) {
            directInputYn = 'Y';
          }
          else {
            directInputYn = 'N';
          }

          // 설문 보기 입력 필수체크 해제
          if ((item == undefined || item == null || item == '') && directInputYn == 'N') {
            //console.log('# 설문'+ (i+1) +'의 보기를 확인하세요.');
            //fnMessage('', '설문'+ (i+1) +'의 보기를 확인하세요.', '');
            //errMsg = '설문'+ (i+1) +'의 보기를 확인하세요.';
            errMsg = '설문보기 정보는 필수 입력입니다';
            return false;
          }
          // @@@@@ 보기
          eventSurveyItemData.item = item;

          // @@@@@ 순서
          eventSurveyItemData.sort = itemIdx+1;


          // @@@@@ 직접입력
          eventSurveyItemData.directInputYn = directInputYn;

          // ------------------------------------------------------------------
          // 파일정보 Set
          // ------------------------------------------------------------------
          eventSurveyItemAttcList = new Array();
          eventSurveyItemAttcData = new Object();

          let imgPath     = '';
          let imgOriginNm = '';
          if (gbImgSurveyMap != undefined && gbImgSurveyMap != null && gbImgSurveyMap.size > 0) {
            //console.log('# 파일정보 :: [', indexKey, '][', subIndexKey, ']');
            if (gbImgSurveyMap.get(indexKey+'') != undefined && gbImgSurveyMap.get(indexKey+'') != null && gbImgSurveyMap.get(indexKey+'').size > 0) {
              let surveyMap = gbImgSurveyMap.get(indexKey+'');
              if (surveyMap.get(subIndexKey+'') != undefined && surveyMap.get(subIndexKey+'') != null && surveyMap.get(subIndexKey+'').size > 0) {
                let imgMap = surveyMap.get(subIndexKey+'');
                imgPath     = imgMap.get('Path');
                imgOriginNm = imgMap.get('OriginNm');
                //console.log('# 파일정보 Path     :: [', indexKey, '][', subIndexKey, '] :: ', imgPath);
                //console.log('# 파일정보 OriginNm :: [', indexKey, '][', subIndexKey, '] :: ', imgOriginNm);
              }
            }
          }
          //console.log('# imgPath     :: ', imgPath);
          //console.log('# imgOriginNm :: ', imgOriginNm);
          if (imgPath != undefined && imgPath != null && imgPath != '') {
            // @@@@@ 파일정보
            eventSurveyItemAttcData.imgPath = imgPath;
            eventSurveyItemAttcData.imgOriginNm = imgOriginNm;
            // @@@@@ 파일리스트 Add
            eventSurveyItemAttcList.push(eventSurveyItemAttcData);
            // @@@@@ 아이템데이터 <- 파일리스트 Set
            eventSurveyItemData.eventSurveyItemAttcList = eventSurveyItemAttcList;
          }

          // @@@@@ 아이템리스트 Add
          eventSurveyItemList.push(eventSurveyItemData);

          // 아이템 idx
          itemIdx++;
        });
        //console.log('# errMsg :: ', errMsg);
        if (errMsg != null && errMsg != '') {
          fnMessage('', errMsg, '');
          return false;
        }

        // @@@@@ 설문데이터 <- 아이템리스트 Set
        eventSurveyQuestionData.eventSurveyItemList = eventSurveyItemList;

        // @@@@@ 설문리스트 Add
        eventSurveyQuestionList.push(eventSurveyQuestionData);

      } // End of for (var i = 0; i < surveyIdxCnt; i++ )
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

      // @@@@@ 반환데이터
      gbValidData.eventSurveyQuestionList = eventSurveyQuestionList;
      //console.log('# gbValidData.eventSurveyQuestionList :: ', JSON.stringify(gbValidData.eventSurveyQuestionList));
    }
    else if (gbEventTp == 'EVENT_TP.ATTEND' || gbEventTp == 'EVENT_TP.MISSION' || gbEventTp == 'EVENT_TP.PURCHASE') {
      // ----------------------------------------------------------------------
      // 스탬프(출석)/스탬프(미션)/스탬프(구매)
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 임직원참여여부 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('employeeJoinYn')) == true) {
        fnMessage('', '<font color="#FF1A1A">[임직원 참여여부]</font> 필수 입력입니다.', 'employeeJoinYn');
        return false;
      }
      // ----------------------------------------------------------------------
      // 참여횟수설정(ID당) [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('eventJoinTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[참여횟수 설정(ID당)]</font> 필수 입력입니다.', 'eventJoinTp', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 당첨자설정 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자 설정]</font> 필수 입력입니다.', 'eventDrawTp');
        return false;
      }

      // ----------------------------------------------------------------------
      // 스탬프 기본 이미지 [Img]
      // ----------------------------------------------------------------------
      if (gbImgStampDefaultPath == undefined || gbImgStampDefaultPath == 'undefined' || gbImgStampDefaultPath == null || gbImgStampDefaultPath == 'null' || gbImgStampDefaultPath == '') {
        fnMessage('', '<font color="#FF1A1A">[스탬프 기본 이미지]</font> 필수 입력입니다.', 'eventDrawTp_1');      // 가장 가까운 포커스
        return false;
      }
      // ----------------------------------------------------------------------
      // 스탬프 체크 이미지 [Img]
      // ----------------------------------------------------------------------
      if (gbImgStampCheckPath == undefined || gbImgStampCheckPath == 'undefined' || gbImgStampCheckPath == null || gbImgStampCheckPath == 'null' || gbImgStampCheckPath == '') {
        fnMessage('', '<font color="#FF1A1A">[스탬프 체크 이미지]</font> 필수 입력입니다.', 'eventDrawTp_1');      // 가장 가까운 포커스
        return false;
      }
      // ----------------------------------------------------------------------
      // 스탬프 배경 이미지 [Img]
      // ----------------------------------------------------------------------
      if (gbImgStampBgPath == undefined || gbImgStampBgPath == 'undefined' || gbImgStampBgPath == null || gbImgStampBgPath == 'null' || gbImgStampBgPath == '') {
        fnMessage('', '<font color="#FF1A1A">[스탬프 배경 이미지]</font> 필수 입력입니다.', 'eventDrawTp_1');      // 가장 가까운 포커스
        return false;
      }
      // ----------------------------------------------------------------------
      // 스탬프노출개수 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('stampCnt1')) == true) {
        fnMessage('', '<font color="#FF1A1A">[스탬프 열구성]</font> 필수 입력입니다.', 'stampCnt1', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 스탬프등록개수 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('stampCnt2')) == true) {
        fnMessage('', '<font color="#FF1A1A">[스탬프 총개수]</font> 필수 입력입니다.', 'stampCnt2', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 스탬프지급조건금액 [Text]
      // ----------------------------------------------------------------------
      if (gbEventTp == 'EVENT_TP.PURCHASE') {
        if (fnIsEmpty(getValueItemForId('orderPrice')) == true) {
          fnMessage('', '<font color="#FF1A1A">[스탬프 지급 조건]</font> 필수 입력입니다.', 'orderPrice');
          return false;
        }
      }

      // ----------------------------------------------------------------------
      // 스탬프(출석)/스탬프(미션)/스탬프(구매)
      // ----------------------------------------------------------------------
      var eventStampDetlList = new Array();
      var eventStampDetlData;

      // **********************************************************************
      // 스탬프 당첨혜택영역 Validataion
      // **********************************************************************
      var gbImgStampDefaultMapSize = 0;   // 스탬프혜택 - 스탬프 기본 이미지 개수
      var gbImgStampCheckMapSize   = 0;   // 스탬프혜택 - 스탬프 체크 이미지 개수
      var gbImgStampIconMapSize    = 0;   // 스탬프혜택 - 스탬프 아이콘 이미지 개수
      var createStampCnt           = 0;   // 설정한 스탬프 개수 (stampCnt2)

      // 당첨혜택영역 스탬프 설정 개수
      createStampCnt = $('#stampCnt2').val();
      //console.log('# createStampCnt          :: ', createStampCnt);

      if (createStampCnt == undefined || createStampCnt == null || createStampCnt <= 0) {
        fnMessage('', '스탬프 개수를 확인하세요.', '');
        return false;
      }

      // ----------------------------------------------------------------------
      // 당첨혜택영역 스탬프 idx 조회
      // ----------------------------------------------------------------------
      var stampIdxArr = new Array();
      $('tbody#stampBenefitCommonTbody tr').each(function() {
        var stampTrId = $(this).attr('id');
        if (fnIsEmpty(stampTrId) == false) {
          //console.log('# stampTrId :: ', stampTrId);
          let stampTrIdArr = stampTrId.split('stampTmplTr_');
          let stampIdx = stampTrIdArr[1];
          //console.log('# stampIdx :: ', stampIdx);
          stampIdxArr.push(stampIdx);
        }
      });
      //console.log('# stampIdxArr :: ', stampIdxArr);

      let stampIdxCnt = stampIdxArr.length;

      if (stampIdxCnt == undefined || stampIdxCnt == null || stampIdxCnt == '' || stampIdxCnt <= 0) {
        fnMessage('', '스탬프 설정수를 확인하세요.', '');
        return false;
      }
      if (stampIdxCnt != createStampCnt) {
        fnMessage('', '스탬프 개수와 스탬프 설정수를 확인하세요.', '');
        return false;
      }

      // ----------------------------------------------------------------------
      // 당첨혜택영역 스탬프 기본 이미지 체크
      // ----------------------------------------------------------------------
      // 혜택영역 스탬프 기본 이미지 개수 Set
      //if (gbImgStampDefaultMap  == undefined || gbImgStampDefaultMap == null) {
      //  fnMessage('', '당첨혜택 스탬프 기본 이미지를 등록을 확인하세요.', '');
      //  return false;
      //}
      // 당첨혜택영역 스탬프 기본 이미지 등록 수 체크
      gbImgStampDefaultMapSize = gbImgStampDefaultMap.size;
      //console.log('# gbImgStampDefaultMapSize :: ', gbImgStampDefaultMapSize);
      //
      //console.log('# gbImgStampDefaultMapSize :: ', gbImgStampDefaultMapSize);
      //if (gbImgStampDefaultMapSize == undefined || gbImgStampDefaultMapSize == null || gbImgStampDefaultMapSize <= 0) {
      //  fnMessage('', '당첨혜택 스탬프 기본 이미지를 확인하세요.', '');
      //  return false;
      //}
      //// 당첨혜택영역 스탬프 기본 이미지 개수 체크
      ////console.log('# ***** createStampCnt          :: ', createStampCnt);
      ////console.log('# ***** gbImgStampDefaultMapSize :: ', gbImgStampDefaultMapSize);
      //if (createStampCnt != gbImgStampDefaultMapSize) {
      //  fnMessage('', '당첨혜택 스탬프 기본 이미지 수를 확인하세요.', '');
      //  return false;
      //}

      // ----------------------------------------------------------------------
      // 당첨혜택영역 스탬프 체크 이미지 체크
      // ----------------------------------------------------------------------
      // 당첨혜택영역 스탬프 체크 이미지 개수 Set
      //if (gbImgStampCheckMap  == undefined || gbImgStampCheckMap == null) {
      //  fnMessage('', '당첨혜택 스탬프 체크 이미지를 등록을 확인하세요.', '');
      //  return false;
      //}
      // 당첨혜택영역 스탬프 체크 이미지 등록 수 체크
      gbImgStampCheckMapSize = gbImgStampCheckMap.size;
      //console.log('# gbImgStampCheckMapSize :: ', gbImgStampCheckMapSize);
      ////console.log('# gbImgStampCheckMapSize :: ', gbImgStampCheckMapSize);
      //
      //if (gbImgStampCheckMapSize == undefined || gbImgStampCheckMapSize == null || gbImgStampCheckMapSize <= 0) {
      //  fnMessage('', '당첨혜택 스탬프 체크 이미지를 확인하세요.', '');
      //  return false;
      //}
      //// 당첨혜택영역 스탬프 체크 이미지 개수 체크
      //if (createStampCnt != gbImgStampCheckMapSize) {
      //  fnMessage('', '당첨혜택 스탬프 체크 이미지 수를 확인하세요.', '');
      //  return false;
      //}

      // ----------------------------------------------------------------------
      // 당첨혜택영역 스탬프 아이콘 이미지 체크
      // ----------------------------------------------------------------------
      if (gbEventTp == 'EVENT_TP.MISSION') {
      //  // 스탬프(미션)
      //
      //  // 당첨혜택영역 스탬프 아이콘 이미지 개수 Set
      //  if (gbImgStampIconMap  == undefined || gbImgStampIconMap == null) {
      //    fnMessage('', '당첨혜택 스탬프 아이콘 이미지를 등록을 확인하세요.', '');
      //    return false;
      //  }
        // 당첨혜택영역 스탬프 아이콘 이미지 등록 수 체크
        gbImgStampIconMapSize = gbImgStampIconMap.size;
      //console.log('# gbImgStampIconMapSize :: ', gbImgStampIconMapSize);
      //  //console.log('# gbImgStampIconMapSize :: ', gbImgStampIconMapSize);
      //
      //  if (gbImgStampIconMapSize == undefined || gbImgStampIconMapSize == null || gbImgStampIconMapSize <= 0) {
      //    fnMessage('', '당첨혜택 스탬프 아이콘 이미지를 확인하세요.', '');
      //    return false;
      //  }
      //  // 당첨혜택영역 스탬프 아이콘 이미지 개수 체크
      //  if (createStampCnt != gbImgStampIconMapSize) {
      //    fnMessage('', '당첨혜택 스탬프 아이콘 이미지 수를 확인하세요.', '');
      //    return false;
      //  }
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 당첨혜택영역 설정 값 체크 : N개
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      for (var i = 0; i < stampIdxCnt; i++ ) {

        // 등록데이트 Object
        eventStampDetlData = new Object();

        let indexKey      = stampIdxArr[i]+'';
        //console.log('# indexKey[', i, '] :: ', indexKey);

        // --------------------------------------------------------------------
        // 스탬프 번호
        // --------------------------------------------------------------------
        if (gbEventTp == 'EVENT_TP.ATTEND') {
          // 이벤트유형 : 출석
          var stampIdx = $('#stampIdx'+indexKey).val();
          if (stampIdx == undefined || stampIdx == null || stampIdx == '') {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 번호를 확인하세요.', '');
            return false;
          }
          // @@@@@ 스탬프번호
          eventStampDetlData.stampCnt = stampIdx;
        }
        else {
          // 이벤트유형 : 미션/구매
          // @@@@@ 스탬프번호
          eventStampDetlData.stampCnt = i+1;
        }

        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 기본 이미지 파일정보 체크
        // --------------------------------------------------------------------
        var defaultValue  = gbImgStampDefaultMap.get(indexKey+'');
        //console.log('# defaultValue[', i, '] :: ', defaultValue);
        //if (defaultValue == undefined || defaultValue == null) {
        //  fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 기본 이미지 파일정보를 확인하세요.', '');
        //  return false;
        //}
        if (fnIsEmpty(defaultValue) == false) {
          //console.log('# defaultValue.length :: ', defaultValue.length);
          for (let [imgKey, imgValue] of defaultValue) {
            //console.log('# 당첨혜택 스탬프 기본 이미지 맵 :: [', imgKey, ' :: ', imgValue, ']');
            if (imgKey == undefined || imgKey == null || imgKey == '') {
              fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 기본 이미지 파일정보를 확인하세요.', '');
              return false;
            }
            // @@@@@ 기본이미지경로
            if (imgKey == 'Path') {
              eventStampDetlData.defaultPath = imgValue;
            }
            // @@@@@ 기본이미지파일명
            if (imgKey == 'OriginNm') {
              eventStampDetlData.defaultOriginNm = imgValue;
            }
          }
        }
        //console.log('# eventStampDetlData.defaultPath[', i, '] :: ', eventStampDetlData.defaultPath);
        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 체크 이미지 파일정보 체크
        // --------------------------------------------------------------------
        let checktValue  = gbImgStampCheckMap.get(indexKey+'');
        //console.log('# checktValue :: ', checktValue);

        //if (checktValue == undefined || checktValue == null) {
        //  fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 체크 이미지 파일정보를 확인하세요.', '');
        //  return false;
        //}
        if (fnIsEmpty(checktValue) == false) {
          //console.log('# checktValue.length :: ', checktValue.length);
          for (let [imgKey, imgValue] of checktValue) {
            //console.log('# 당첨혜택 스탬프 체크 이미지 맵 :: [', imgKey, ' :: ', imgValue, ']');
            if (imgKey == undefined || imgKey == null || imgKey == '') {
              fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 체크 이미지 파일정보를 확인하세요.', '');
              return false;
            }
            // @@@@@ 체크이미지경로
            if (imgKey == 'Path') {
              eventStampDetlData.checkPath = imgValue;
            }
            // @@@@@ 체크이미지파일명
            if (imgKey == 'OriginNm') {
              eventStampDetlData.checkOriginNm = imgValue;
            }
          }
        }

        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 아이콘 이미지 파일정보 체크
        // --------------------------------------------------------------------
        if (gbEventTp == 'EVENT_TP.MISSION') {
          // 스탬프(미션)
          let bgValue  = gbImgStampIconMap.get(indexKey+'');

          //if (bgValue == undefined || bgValue == null) {
          //  fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 아이콘 이미지 파일정보를 확인하세요.', '');
          //  return false;
          //}
          if (fnIsEmpty(bgValue) == false) {
            //console.log('# bgValue.length :: ', bgValue.length);
            for (let [imgKey, imgValue] of bgValue) {
              //console.log('# 당첨혜택 스탬프 아이콘 이미지 맵 :: [', imgKey, ' :: ', imgValue, ']');
              if (imgKey == undefined || imgKey == null || imgKey == '') {
                fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 아이콘 이미지 파일정보를 확인하세요.', '');
                return false;
              }
              // @@@@@ 아이콘이미지경로
              if (imgKey == 'Path') {
                eventStampDetlData.iconPath = imgValue;
              }
              // @@@@@ 아이콘이미지파일명
              if (imgKey == 'OriginNm') {
                eventStampDetlData.iconOriginNm = imgValue;
              }
            }
          }
        }

        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 당첨혜택 체크 : 쿠폰/적립금/경품/응모
        // --------------------------------------------------------------------
        let eventBenefitTp = $('input[name=eventBenefitTp'+indexKey+']:checked').val();
        //console.log('# [', indexKey, '] eventBenefitTp :: ', eventBenefitTp);
        // @@@@@ 당첨혜택유형
        eventStampDetlData.eventBenefitTp = eventBenefitTp;

        if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
          // ------------------------------------------------------------------
          // 쿠폰 [그리드]
          // ------------------------------------------------------------------
          var targetGrid    = $('#benefitCouponDynamicGrid'+indexKey).data('kendoGrid');
          var targetGridDs  = targetGrid.dataSource;
          var targetGridArr = targetGridDs.data();

          if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 쿠폰을 확인하세요.', '');
            return false;
          }
          // @@@@@ 혜택PK(쿠폰PK)
          var eventCouponList = new Array();
          for (var c = 0; c < targetGridArr.length; c++) {
            var couponCnt   = $('#benefitCouponDynamicGrid'+indexKey+' tbody input[name=couponCount]')[c].value;
            if (couponCnt == undefined || couponCnt == null || couponCnt == '' || Number(couponCnt) < 1) {
              fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 쿠폰수량을 확인하세요.', '');
              return false;
              break;
            }
            var couponObj = new Object();
            couponObj.pmCouponId  = targetGridArr[c].pmCouponId;
            couponObj.couponCnt   = couponCnt;
            couponObj.evEventCouponId   = targetGridArr[c].evEventCouponId;
            eventCouponList.push(couponObj);
          } // End of for (var i = 0; i < targetGridArr.length; i++)

          eventStampDetlData.eventCouponList = eventCouponList;
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
          // ------------------------------------------------------------------
          // 적립금 [그리드]
          // ------------------------------------------------------------------
          var targetGrid    = $('#benefitPointDynamicGrid'+indexKey).data('kendoGrid');
          var targetGridDs  = targetGrid.dataSource;
          var targetGridArr = targetGridDs.data();

          if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 적립금을 확인하세요.', '');
            return false;
          }
          // @@@@@ 혜택PK(적립금PK)
          eventStampDetlData.benefitId = targetGridArr[0].pmPointId;
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
          // ------------------------------------------------------------------
          // 경품
          // ------------------------------------------------------------------
          let val = $('#benefitNmGiftStamp'+indexKey).val();
          if (val == undefined || val == null || val == '') {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 경품내용을 확인하세요.', 'benefitNmGiftStamp'+indexKey);
            return false;
          }
          // @@@@@ 경품
          eventStampDetlData.benefitNm = val;
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
          // ------------------------------------------------------------------
          // 응모
          // ------------------------------------------------------------------
          let val = $('#benefitNmEnterStamp'+indexKey).val();
          if (val == undefined || val == null || val == '') {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 응모내용을 확인하세요.', 'benefitNmEnterStamp'+indexKey);
            return false;
          }
          // @@@@@ 응모
          eventStampDetlData.benefitNm = val;
        }

        // --------------------------------------------------------------------
        // 당첨혜택영역 스탬프 노출 URL
        // --------------------------------------------------------------------
        if (gbEventTp == 'EVENT_TP.MISSION') {
          // 스탬프(미션)
          // stampUrl
          let val = $('#stampUrl'+indexKey).val();
          //console.log('# stampUrl.val[', indexKey, '] :: ', val);
          if (val == undefined || val == null || val == '') {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 스탬프의 스탬프 노출 URL을 확인하세요.', 'stampUrl'+indexKey);
            return false;
          }
          // @@@@@ 응모
          eventStampDetlData.stampUrl = val;
        }

        // @@@@@ 스탬프이벤트상세리스트() 추
        eventStampDetlList.push(eventStampDetlData);

      } // End of for (var i = 0; i < stampIdxCnt; i++ )

      // @@@@@ 반환데이터
      gbValidData.eventStampDetlList = eventStampDetlList;
      //console.log('# gbValidData.eventStampDetlList :: ', JSON.stringify(gbValidData.eventStampDetlList));

    }
    else if (gbEventTp == 'EVENT_TP.ROULETTE') {
      // ----------------------------------------------------------------------
      // 룰렛이벤트
      // ----------------------------------------------------------------------
      //console.log('# fnCheckValidatiaon Roulette Start');

      // ----------------------------------------------------------------------
      // 임직원참여여부 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('employeeJoinYn')) == true) {
        fnMessage('', '<font color="#FF1A1A">[임직원 참여여부]</font> 필수 입력입니다.', 'employeeJoinYn');
        return false;
      }
      // ----------------------------------------------------------------------
      // 참여횟수설정(ID당) [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('eventJoinTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[참여횟수 설정(ID당)]</font> 필수 입력입니다.', 'eventJoinTp', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 당첨자설정 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자 설정]</font> 필수 입력입니다.', 'eventDrawTp');
        return false;
      }
      // ----------------------------------------------------------------------
      // 시작 버튼 이미지 [Img]
      // ----------------------------------------------------------------------
      if (gbImgRouletteStartBtnPath == undefined || gbImgRouletteStartBtnPath == 'undefined' || gbImgRouletteStartBtnPath == null || gbImgRouletteStartBtnPath == 'null' || gbImgRouletteStartBtnPath == '') {
        fnMessage('', '<font color="#FF1A1A">[룰렛 시작 버튼 이미지]</font> 필수 입력입니다.', 'eventDrawTp_1');    // 가장 가까운 포커스
        return false;
      }
      // ----------------------------------------------------------------------
      // 화살표 이미지 [Img]
      // ----------------------------------------------------------------------
      if (gbImgRouletteArrowPath == undefined || gbImgRouletteArrowPath == 'undefined' || gbImgRouletteArrowPath == null || gbImgRouletteArrowPath == 'null' || gbImgRouletteArrowPath == '') {
        fnMessage('', '<font color="#FF1A1A">[룰렛 화살표 이미지]</font> 필수 입력입니다.', 'eventDrawTp_1');      // 가장 가까운 포커스
        return false;
      }
      // ----------------------------------------------------------------------
      // 룰렛 개수 설정 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('rouletteCnt')) == true) {
        fnMessage('', '<font color="#FF1A1A">[룰렛 개수 설정]</font> 필수 입력입니다.', 'rouletteCnt', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 이벤트 제한고객 룰렛강제 지정 [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('exceptionUserRouletteCnt')) == true) {
        fnMessage('', '<font color="#FF1A1A">[이벤트 제한고객 룰렛강제 지정]</font> 필수 입력입니다.', 'exceptionUserRouletteCnt', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 룰렛 이미지
      // ----------------------------------------------------------------------
      if (gbImgRoulettePath == undefined || gbImgRoulettePath == 'undefined' || gbImgRoulettePath == null || gbImgRoulettePath == 'null' || gbImgRoulettePath == '') {
        fnMessage('', '<font color="#FF1A1A">[룰렛 이미지]</font> 필수 입력입니다.', 'rouletteCnt');            // 가장 가까운 포커스
        return false;
      }


      var eventRouletteItemList = new Array();
      var eventRouletteItemData;

      // **********************************************************************
      // 룰렛 당첨혜낵 설정 Validation
      // **********************************************************************
      var cresateRouletteCnt          = 0;   // 설정한 룰렛 개수 (rouletteCnt)

      cresateRouletteCnt = $('#rouletteCnt').val();
      //console.log('# cresateRouletteCnt  :: ', cresateRouletteCnt);

      var totalAwardRt = 0;
      var totalAwardMaxCnt = 0;
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 당첨혜택영역 설정 값 체크 : N개
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      for (var i = 0; i < cresateRouletteCnt; i++ ) {

        // 등록데이트 Object
        eventRouletteItemData = new Object();

        let indexKey      = i+1;
        //console.log('# indexKey[', i, '] :: ', indexKey);

        // --------------------------------------------------------------------
        // 당첨혜택영역 룰렛 당첨혜택 체크 : 쿠폰/적립금/경품/응모
        // --------------------------------------------------------------------
        let eventBenefitTp = $('input[name=eventBenefitTp'+indexKey+']:checked').val();
        //console.log('# [', indexKey, '] eventBenefitTp :: ', eventBenefitTp);
        // @@@@@ 당첨혜택유형
        eventRouletteItemData.eventBenefitTp = eventBenefitTp;

        if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
          // ------------------------------------------------------------------
          // 쿠폰
          // ------------------------------------------------------------------
          var targetGrid    = $('#benefitCouponDynamicGrid'+indexKey).data('kendoGrid');
          var targetGridDs  = targetGrid.dataSource;
          var targetGridArr = targetGridDs.data();

          if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 쿠폰을 확인하세요.', '');
            return false;
          }
          // 쿠폰PK
          var eventCouponList = new Array();
          for (var c = 0; c < targetGridArr.length; c++) {
            var couponCnt   = $('#benefitCouponDynamicGrid'+indexKey+' tbody input[name=couponCount]')[c].value;
            if (couponCnt == undefined || couponCnt == null || couponCnt == '' || Number(couponCnt) < 1) {
              fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 쿠폰수량을 확인하세요.', '');
              return false;
              break;
            }
            var couponObj = new Object();
            couponObj.pmCouponId  = targetGridArr[c].pmCouponId;
            couponObj.couponCnt   = couponCnt;
            couponObj.evEventCouponId   = targetGridArr[c].evEventCouponId;
            eventCouponList.push(couponObj);
          } // End of for (var i = 0; i < targetGridArr.length; i++)
          // @@@@@ 혜택PK(쿠폰PK)
          eventRouletteItemData.eventCouponList = eventCouponList;
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
          // ------------------------------------------------------------------
          // 적립금
          // ------------------------------------------------------------------
          var targetGrid    = $('#benefitPointDynamicGrid'+indexKey).data('kendoGrid');
          var targetGridDs  = targetGrid.dataSource;
          var targetGridArr = targetGridDs.data();

          if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length <= 0) {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 적립금을 확인하세요.', '');
            return false;
          }
          //console.log('# [', indexKey, ']targetGridArr[0] :: ', JSON.stringify(targetGridArr[0]));
          //console.log('# [', indexKey, ']targetGridArr[0].pmPointId :: ', targetGridArr[0].pmPointId);
          // @@@@@ 혜택PK(적립금PK)
          eventRouletteItemData.benefitId = targetGridArr[0].pmPointId;
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
          // ------------------------------------------------------------------
          // 경품
          // ------------------------------------------------------------------
          let val = $('#benefitNmGiftRoulette'+indexKey).val();
          if (val == undefined || val == null || val == '') {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 경품내용을 확인하세요.', 'benefitNmGiftRoulette'+indexKey);
            return false;
          }
          // @@@@@ 경품
          eventRouletteItemData.benefitNm = val;
        }
        else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
          // ------------------------------------------------------------------
          // 응모
          // ------------------------------------------------------------------
          let val = $('#benefitNmEnterRoulette'+indexKey).val();
          if (val == undefined || val == null || val == '') {
            fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 응모내용을 확인하세요.', 'benefitNmEnterRoulette'+indexKey);
            return false;
          }
          // @@@@@ 응모
          eventRouletteItemData.benefitNm = val;
        }

        // --------------------------------------------------------------------
        // 룰렛 당첨 확률
        // --------------------------------------------------------------------
        let val = $('#awardRt'+indexKey).val();
        //console.log('# awardRt', indexKey, ' :: ', val);
        if (val == undefined || val == null || val == '') {
          fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 당첨확률을 확인하세요.', 'awardRt'+indexKey);
          return false;
        }
        // @@@@@ 당첨확률
        eventRouletteItemData.awardRt = val;
        totalAwardRt = (Number(totalAwardRt) + Number(val)).toFixed(3);
        // --------------------------------------------------------------------
        // 룰렛 당첨 인원
        // --------------------------------------------------------------------
        val = $('#awardMaxCnt'+indexKey).val();
        //console.log('# awardMaxCnt', indexKey, ' :: ', val);
        if (val == undefined || val == null || val == '') {
          fnMessage('', '당첨혜택 '+ (i+1) +'번째 룰렛의 당첨인원을 확인하세요.', 'awardMaxCnt'+indexKey);
          return false;
        }
        // @@@@@ 응모
        eventRouletteItemData.awardMaxCnt = val;
        totalAwardMaxCnt = Number(totalAwardMaxCnt) + Number(val);
        eventRouletteItemData.evEventRouletteItemId = $('#evEventRouletteItemId'+indexKey).val();

        // @@@@@ 룰렛프이벤트상세리스트() 추가
        eventRouletteItemList.push(eventRouletteItemData);

      } // End of for (var i = 0; i < cresateRouletteCnt; i++ )


      //룰렛 이벤트 확률
      if(totalAwardRt != 100){
          fnMessage('', ' 룰렛당첨 확률의 합계가 100이 되도록 각 경품별 룰렛당첨 확률을 조정 해 주세요.', '');
          return false;
      }

      if(Number($('#expectJoinUserCntRoulette').val()) > 1000000) {
         fnMessage('', '  예상참여자수가 1,000,000명을 초과할 수 없습니다.  ', '');
         return false;
      }

      if(totalAwardMaxCnt != Number($('#expectJoinUserCntRoulette').val())){
        fnMessage('', '  각 경품의 당첨인원의 합계는 예상참여자수와 동일해야 합니다. 경품 별 당첨인원을 조정 해 주세요.  ', '');
        return false;
      }
      // @@@@@ 반환데이터
      gbValidData.eventRouletteItemList = eventRouletteItemList;
      //console.log('# gbValidData.eventRouletteItemList :: ', JSON.stringify(gbValidData.eventRouletteItemList));
    }
    else if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
      // ----------------------------------------------------------------------
      // 체험단이벤트
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 당첨혜택 - 쿠폰 콤보 [그리드]
      // ----------------------------------------------------------------------
      var targetGrid    = $('#experienceGrid').data('kendoGrid');
      var targetGridDs  = targetGrid.dataSource;
      var targetGridArr = targetGridDs.data();

      if (targetGridArr == undefined || targetGridArr == null || targetGridArr == '' || targetGridArr.length <= 0) {
        fnMessage('', '<font color="#FF1A1A">[체험상품등록]</font> 필수 입력입니다.', '');
        return false;
      }

      if (targetGridArr.length > 1) {
        fnMessage('', '<font color="#FF1A1A">체험상품</font>은 1건만 선택 가능합니다.', '');
        return false;
      }
      // ----------------------------------------------------------------------
      // 임직원참여여부 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('employeeJoinYn')) == true) {
        fnMessage('', '<font color="#FF1A1A">[임직원 참여여부]</font> 필수 입력입니다.', 'employeeJoinYn');
        return false;
      }
      // ----------------------------------------------------------------------
      // 참여횟수설정(ID당) [콤보]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('eventJoinTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[참여횟수 설정(ID당)]</font> 필수 입력입니다.', 'eventJoinTp', 'kendoDropDownList');
        return false;
      }
      // ----------------------------------------------------------------------
      // 당첨자설정 [라디오]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueRadioForName('eventDrawTp')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자 설정]</font> 필수 입력입니다.', 'eventDrawTp');
        return false;
      }
      // ----------------------------------------------------------------------
      // 당첨자수 [Text]
      // ----------------------------------------------------------------------
      var eventDrawTpVal = $('input[name=eventDrawTp]:checked').val();
      if (eventDrawTpVal == 'EVENT_DRAW_TP.FIRST_COME') {
        if (fnIsEmpty(getValueItemForId('firstComeCnt')) == true) {
          fnMessage('', '<font color="#FF1A1A">[당첨자설정][당첨자 수]</font> 필수 입력입니다.', 'firstComeCnt');
          return false;
        }
      }
      // ----------------------------------------------------------------------
      // 댓글분류설정 [라디오]
      // ----------------------------------------------------------------------
      let eventDrawTp = $('input[name=eventDrawTp]:checked').val();

      if (eventDrawTp != 'EVENT_DRAW_TP.FIRST_COME') {
        // 당첨자설정 : 선착순이 아닌경우만 필수 체크
        var commentCodeYn = $('input[name=commentCodeYn]:checked').val();

        if (commentCodeYn == 'Y'){
          var tmpList = Array.from(gbCommentCodeMaps.values());
          if (tmpList == undefined || tmpList == null || tmpList == '' || tmpList.length <= 0) {
            fnMessage('', '<font color="#FF1A1A">[댓글분류설정]</font> 필수 입력입니다.', 'commentCodeYn');
            return false;
          }
        }
      }
      // ----------------------------------------------------------------------
      // 당첨자혜택(쿠폰 콤보) [히든]
      // ----------------------------------------------------------------------
      if (fnIsEmpty(getValueItemForId('pmCouponId')) == true) {
        fnMessage('', '<font color="#FF1A1A">[당첨자 혜택]</font> 필수 입력입니다.', 'pmCouponCombo', 'kendoDropDownList');
        return false;
      }
      //

    }

    return true;
  }

  // ==========================================================================
  // # Validation Check - 그리드
  // ==========================================================================
  function fnCheckValidGrid(data, eventDataObj, groupList) {
    //console.log('# fnCheckValidGrid Start [', gbEventTp, ']');
    // ------------------------------------------------------------------------
    // 상품그리드 체크
    // ------------------------------------------------------------------------
    let resultGridCheck = new Object();
    resultGridCheck.result = true;
    resultGridCheck.message = '';

    let resultGroupCheck = new Object();               //  상품그룹 Validataion 결과
    resultGroupCheck.result = true;
    resultGroupCheck.message = '';

    if (resultGridCheck != null && resultGridCheck.result == true) {
      // Validation Success -> 상품그룹 채크

      let isCheckYn = true;

      // --------------------------------------------------------------------
      // 3. 상품그룹 체크
      // --------------------------------------------------------------------
      if (gbEventTp == 'EVENT_TP.NORMAL' ||
          gbEventTp == 'EVENT_TP.ATTEND' ||
          gbEventTp == 'EVENT_TP.MISSION' ||
          gbEventTp == 'EVENT_TP.PURCHASE' ||
          gbEventTp == 'EVENT_TP.ROULETTE' ||
          gbEventTp == 'EVENT_TP.SURVEY') {

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
        if (groupList[i].eventImgTp == '') {
          resultCheck.result = false;
          resultCheck.message = '<font color="#FF1A1A">[상품그룹'+(i+1)+'][상품 그룹명 배경 설정]</font> 필수 입력입니다.';
          break;
        }
        // 상품 그룹명 배경 색상
        if (groupList[i].eventImgTp == 'EVENT_IMG_TP.BG') {
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

    // ------------------------------------------------------------------------
    // 그리드 크기 체크
    // ------------------------------------------------------------------------
    if (goodsList == undefined || goodsList == null || goodsList == '' || goodsList.length <= 0) {
      resultCheck.result = false;
      resultCheck.message = '<font color="#FF1A1A">['+gridNm+']</font> 필수 입력입니다.';
      return resultCheck;
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

      if (gbEventTp == 'EVENT_TP.SELECT') {
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
      else if (gbEventTp == 'EVENT_TP.GIFT') {
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
  // # 일반이벤트 데이터 Set
  // ==========================================================================
  function fnParamDataSetNormal(paramData, data) {
    //console.log('# fnParamDataSetNormal Start');

    // ------------------------------------------------------------------------
    // @@@@@ 이벤트정보 - 상세정보
    // ------------------------------------------------------------------------
    // EV_EVENT
    paramData.eventInfo.employeeJoinYn   = data.employeeJoinYn;     // 임직원참여여부
    paramData.eventInfo.eventJoinTp      = data.eventJoinTp;        // 참여횟수설정(ID당)
    paramData.eventInfo.eventDrawTp      = data.eventDrawTp;        // 담청자설정
    paramData.eventInfo.winnerNotice     = data.winnerNotice;       // 당첨자공지사항(url입력)
    paramData.eventInfo.winnerInfor      = data.winnerInfor;        // 당첨자안내
    paramData.eventInfo.checkNewUserYn   = data.checkNewUserYn == undefined || data.checkNewUserYn == null || data.checkNewUserYn == '' || data.checkNewUserYn == 'N' ? 'N' : 'Y';     // 신규회원확인유무
    // TODO 어떤 영역인지 확인해볼것 paramData.eventInfo.winnerInforDt    = data.winnerInforDt;      // 당첨자안내일자

    var eventNormalData = new Object();
    eventNormalData.commentYn           = data.commentYn;              // 댓글허용여부
    eventNormalData.normalEventTp       = data.normalEventTp;          // 참여방법
    eventNormalData.commentCodeYn       = data.commentCodeYn;          // 댓글분류설정여부
    eventNormalData.eventBenefitTp      = data.eventBenefitTp;         // 당첨자혜택유형
    eventNormalData.awardRt             = data.awardRt;                // 당첨확률
    eventNormalData.expectJoinUserCnt   = data.expectJoinUserCnt;      // 예상참여자수
    eventNormalData.joinCondition       = data.joinCondition;          // 참여조건
    eventNormalData.orderCnt            = data.orderCnt;               // 최소주문건수
    eventNormalData.orderPrice          = data.normalOrderPrice;       // 최수구매금액
    eventNormalData.goodsDeliveryTp     = data.goodsDeliveryTp;        // 참여조건 주문고객 주문배송유형

    // ------------------------------------------------------------------------
    // 댓글허용여부에 따른 처리
    // ------------------------------------------------------------------------
//    if (eventNormalData.commentYn == 'Y') {

    // ------------------------------------------------------------------------
    // 참여방법 여부에 따른 처리
    // ------------------------------------------------------------------------
    if (eventNormalData.normalEventTp != 'NORMAL_EVENT_TP.NONE') {
      // ----------------------------------------------------------------------
      // 참여방법 : NORMAL_EVENT_TP.BUTTON, NORMAL_EVENT_TP.COMMENT
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 댓글분류설정
      // ----------------------------------------------------------------------
      // @@@@@ 초기화
      paramData.eventCommentCodeList = null;

      // 참여방법 : 댓글응모
      if(eventNormalData.normalEventTp == 'NORMAL_EVENT_TP.COMMENT'){
          if($('input[name=commentCodeYn]:checked').val() == 'Y'){
            var commentCodeList = Array.from(gbCommentCodeMaps.values());
            var eventCommentCodeList = new Array();

            if (commentCodeList != undefined && commentCodeList != null && commentCodeList.length > 0) {
              for (var i = 0; i < commentCodeList.length; i++) {
                var commentCodeObj = new Object();
                commentCodeObj.commentValue = commentCodeList[i];
                eventCommentCodeList.push(commentCodeObj);
              }
            }
            // @@@@@
            paramData.eventCommentCodeList = eventCommentCodeList;
          }
      }

      // 적용범위 그리드, coverageType
      var insertData;
      insertData = kendo.stringify(ctgryGrid._data);
      paramData.insertData = insertData;
      paramData.eventInfo.coverageType = $('#goodsCoverageType').data('kendoDropDownList').value();
      if(insertData == undefined || insertData == '[]'){
        paramData.eventInfo.coverageType = "APPLYCOVERAGE.ALL";
        paramData.coverageType = "APPLYCOVERAGE.ALL";
      } else {
        paramData.eventInfo.coverageType = ctgryGrid._data[0].coverageType;
        paramData.coverageType = ctgryGrid._data[0].coverageType;
      }

      // ----------------------------------------------------------------------
      // 당첨혜택
      // ----------------------------------------------------------------------
      var eventCouponList = new Array();
      var eventBenefitTp = $('input[name=eventBenefitTp]:checked').val();
      //console.log('# eventBenefitTp :: ', eventBenefitTp);

      // 당첨혜택 초기화
      paramData.eventCouponList = null;
      paramData.eventInfo.pmPointId = '';
      paramData.eventInfo.benefitNm = '';

      if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
        // --------------------------------------------------------------------
        // 당첨혜택 - 쿠폰리스트
        // --------------------------------------------------------------------
        var targetGrid    = $('#benefitCouponGrid').data('kendoGrid');
        var targetGridDs  = targetGrid.dataSource;
        var targetGridArr = targetGridDs.data();

        if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
          //console.log('# targetGridArr.length :: ', targetGridArr.length);
          for (var i = 0; i < targetGridArr.length; i++) {
            var couponObj = new Object();
            couponObj.pmCouponId  = targetGridArr[i].pmCouponId;
            couponObj.couponCnt   = $('#benefitCouponGrid tbody input[name=couponCount]')[i].value;
            couponObj.couponTotalCnt   = $('#benefitCouponGrid tbody input[name=couponTotalCount]')[i].value;
            if($('#benefitCouponGrid tbody input[name=evEventCouponId]')[i] != undefined ){
                couponObj.evEventCouponId   = $('#benefitCouponGrid tbody input[name=evEventCouponId]')[i].value;
            }
            eventCouponList.push(couponObj);
          } // End of for (var i = 0; i < targetGridArr.length; i++)
        } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)

        // @@@@@
        paramData.eventCouponList = eventCouponList;
      }
      else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
        // --------------------------------------------------------------------
        // 당첨혜택 - 적립금
        // --------------------------------------------------------------------
        var targetGrid    = $('#benefitPointGrid').data('kendoGrid');
        var targetGridDs  = targetGrid.dataSource;
        var targetGridArr = targetGridDs.data();
        if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
          //console.log('# targetGridArr.length :: ', targetGridArr.length);
          for (var i = 0; i < targetGridArr.length; i++) {

            // @@@@@
            eventNormalData.pmPointId = targetGridArr[i].pmPointId;
            //eventNormalData.totalWinCnt = targetGridArr[i].totalWinCount;
            eventNormalData.totalWinCnt = data.totalWinCount;
          } // End of for (var i = 0; i < targetGridArr.length; i++)
        } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)

      }
      else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
        // @@@@@
        eventNormalData.benefitNm = data.benefitNmGift;
        eventNormalData.totalWinCnt = data.totalWinCntGift;
      }
      else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
        // @@@@@
        eventNormalData.benefitNm = data.benefitNmEnter;
      }
    } // End of if (eventNormalData.commentYn == 'Y')
    else {
      // ----------------------------------------------------------------------
      // 댓글허용여부 : N
      // ----------------------------------------------------------------------
      paramData.eventInfo.employeeJoinYn  = 'N';                      // 임직원참여여부
      paramData.eventInfo.eventJoinTp     = '';                       // 참여횟수설정(ID당)
      paramData.eventInfo.eventDrawTp     = 'EVENT_DRAW_TP.ADMIN';    // 담청자설정          (Notnull이어서 관리자추첨으로 Set)
      eventNormalData.commentCodeYn       = 'N';                      // 댓글분류설정여부
      eventNormalData.eventBenefitTp      = 'EVENT_BENEFIT_TP.NONE';  // 당첨자혜택유형      (Notnull이어서 혜택없음으로 Set)
    }

    // ------------------------------------------------------------------------
    // 그룹 데이터 Set
    // ------------------------------------------------------------------------
    paramData.groupList = fnSetGroupData();

    // @@@@@ 일반이벤트정보
    paramData.eventNormalInfo = eventNormalData;

    return paramData;

  } //  End of function fnParamDataSetNormal(paramData, data)

  // ==========================================================================
  // # 설문이벤트 데이터 Set
  // ==========================================================================
  function fnParamDataSetSurvey(paramData, data) {
    //console.log('# fnParamDataSetSurvey Start');

    // ------------------------------------------------------------------------
    // @@@@@ 이벤트정보 - 상세정보
    // ------------------------------------------------------------------------
    // EV_EVENT
    paramData.eventInfo.employeeJoinYn   = data.employeeJoinYn;             // 임직원참여여부
    paramData.eventInfo.eventJoinTp      = data.eventJoinTp;                // 참여횟수설정(ID당)
    paramData.eventInfo.eventDrawTp      = data.eventDrawTp;                // 담청자설정
    paramData.eventInfo.winnerNotice     = data.winnerNotice;       // 당첨자공지사항(url입력)
    paramData.eventInfo.winnerInfor      = data.winnerInfor;                // 당첨자안내

    var eventSurveyData = new Object();
    eventSurveyData.btnColorCd       = data.btnColorCd;                     // 참여버튼 BG color code
    eventSurveyData.eventBenefitTp   = data.eventBenefitTp;                 // 당첨자혜택유형

    // ------------------------------------------------------------------------
    // 당첨혜택
    // ------------------------------------------------------------------------
    var eventCouponList = new Array();
    var eventBenefitTp = $('input[name=eventBenefitTp]:checked').val();
    //console.log('# eventBenefitTp :: ', eventBenefitTp);

    // 당첨혜택 초기화
    paramData.eventCouponList = null;
    paramData.eventInfo.pmPointId = '';
    paramData.eventInfo.benefitNm = '';

    if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
      // --------------------------------------------------------------------
      // 당첨혜택 - 쿠폰리스트
      // --------------------------------------------------------------------
      var targetGrid    = $('#benefitCouponGrid').data('kendoGrid');
      var targetGridDs  = targetGrid.dataSource;
      var targetGridArr = targetGridDs.data();

      if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
        //console.log('# targetGridArr.length :: ', targetGridArr.length);
        for (var i = 0; i < targetGridArr.length; i++) {
          var couponObj = new Object();
          couponObj.pmCouponId  = targetGridArr[i].pmCouponId;
          couponObj.couponCnt   = $('#benefitCouponGrid tbody input[name=couponCount]')[i].value;
          couponObj.couponTotalCnt   = $('#benefitCouponGrid tbody input[name=couponTotalCount]')[i].value;
          if($('#benefitCouponGrid tbody input[name=evEventCouponId]')[i] != undefined ){
              couponObj.evEventCouponId   = $('#benefitCouponGrid tbody input[name=evEventCouponId]')[i].value;
          }
          eventCouponList.push(couponObj);
        } // End of for (var i = 0; i < targetGridArr.length; i++)
      } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)

      // @@@@@
      paramData.eventCouponList = eventCouponList;
    }
    else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
      // --------------------------------------------------------------------
      // 당첨혜택 - 적립금
      // --------------------------------------------------------------------
      var targetGrid    = $('#benefitPointGrid').data('kendoGrid');
      var targetGridDs  = targetGrid.dataSource;
      var targetGridArr = targetGridDs.data();

      if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
        //console.log('# targetGridArr.length :: ', targetGridArr.length);
        for (var i = 0; i < targetGridArr.length; i++) {

          // @@@@@
          eventSurveyData.pmPointId = targetGridArr[i].pmPointId;
        } // End of for (var i = 0; i < targetGridArr.length; i++)
      } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)
    }
    else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
      // @@@@@
      eventSurveyData.benefitNm = data.benefitNmGift;
    }
    else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
      // @@@@@
      eventSurveyData.benefitNm = data.benefitNmEnter;
    }

    // @@@@@ 설문이벤트정보
    paramData.eventSurveyInfo = eventSurveyData;

    // ------------------------------------------------------------------------
    // 그룹 데이터 Set
    // ------------------------------------------------------------------------
    paramData.groupList = fnSetGroupData();

    return paramData;

  } //  End of function fnParamDataSetNormal(paramData, data)


  // ==========================================================================
  // # 스탬프이벤트 데이터 Set
  // ==========================================================================
  function fnParamDataSetStamp(paramData, data) {
    //console.log('# fnParamDataSetStamp Start');

    // ------------------------------------------------------------------------
    // @@@@@ 이벤트정보 - 상세정보
    // ------------------------------------------------------------------------
    // EV_EVENT
    paramData.eventInfo.employeeJoinYn   = data.employeeJoinYn;             // 임직원참여여부
    paramData.eventInfo.eventJoinTp      = data.eventJoinTp;                // 참여횟수설정(ID당)
    paramData.eventInfo.eventDrawTp      = data.eventDrawTp;                // 담청자설정
    paramData.eventInfo.winnerNotice     = data.winnerNotice;       // 당첨자공지사항(url입력)
    paramData.eventInfo.winnerInfor      = data.winnerInfor;                // 당첨자안내

    var eventStampData = new Object();
    eventStampData.btnColorCd       = data.btnColorCd;              // 참여버튼 BG color code
    eventStampData.defaultPath      = gbImgStampDefaultPath;        // 스탬프기본이미지경로
    eventStampData.defaultOriginNm  = gbImgStampDefaultOriginNm;    // 스탬프기본이미지파일명
    eventStampData.checkPath        = gbImgStampCheckPath;          // 스탬프체크이미지경로
    eventStampData.checkOriginNm    = gbImgStampCheckOriginNm;      // 스탬프체크이미지파일명
    eventStampData.bgPath           = gbImgStampBgPath;             // 스탬프배경이미지경로
    eventStampData.bgOriginNm       = gbImgStampBgOriginNm;         // 스탬프배경이미지파일명
    eventStampData.stampCnt1        = data.stampCnt1;               // 스탬프노출개수
    eventStampData.stampCnt2        = data.stampCnt2;               // 스탬프설정개수

    if (gbEventTp == 'EVENT_TP.PURCHASE') {
      eventStampData.orderPrice        = data.orderPrice            // 스탬프지급조건-금액
      eventStampData.eventStampOrderTp = data.eventStampOrderTp     // 스탬프지급조건-주문상태
    }

    // @@@@@ 스탬프이벤트정보
    paramData.eventStampInfo = eventStampData;

    // ------------------------------------------------------------------------
    // 그룹 데이터 Set
    // ------------------------------------------------------------------------
    paramData.groupList = fnSetGroupData();

    return paramData;

  } //  End of function fnParamDataSetNormal(paramData, data)

  // ==========================================================================
  // # 룰렛이벤트 데이터 Set
  // ==========================================================================
  function fnParamDataSetRoulette(paramData, data) {
    //console.log('# fnParamDataSetRoulette Start');
    var groupList         = new Array();  // 그룹리스트

    // ------------------------------------------------------------------------
    // @@@@@ 이벤트정보 - 상세정보
    // ------------------------------------------------------------------------
    // EV_EVENT
    paramData.eventInfo.employeeJoinYn   = data.employeeJoinYn;             // 임직원참여여부
    paramData.eventInfo.eventJoinTp      = data.eventJoinTp;                // 참여횟수설정(ID당)
    paramData.eventInfo.eventDrawTp      = data.eventDrawTp;                // 담청자설정
    paramData.eventInfo.winnerNotice     = data.winnerNotice;               // 당첨자공지사항(url입력)
    paramData.eventInfo.winnerInfor      = data.winnerInfor;                // 당첨자안내

    var eventRouletteData = new Object();
    eventRouletteData.startBtnPath              = gbImgRouletteStartBtnPath;        // 룰렛시박버튼이미지경로
    eventRouletteData.startBtnOriginNm          = gbImgRouletteStartBtnOriginNm;    // 룰렛시박버튼이미지파일명
    eventRouletteData.arrowPath                 = gbImgRouletteArrowPath;           // 룰렛화살표이미지경로
    eventRouletteData.arrowOriginNm             = gbImgRouletteArrowOriginNm;       // 룰렛화살표이미지파일명
    eventRouletteData.bgPath                    = gbImgRouletteBgPath;              // 룰렛배경이미지경로
    eventRouletteData.bgOriginNm                = gbImgRouletteBgOriginNm;          // 룰렛배경이미지파일명
    eventRouletteData.roulettePath              = gbImgRoulettePath;                // 룰렛이미지경로
    eventRouletteData.rouletteOriginNm          = gbImgRouletteOriginNm;            // 룰렛이미지파일명
    eventRouletteData.rouletteCnt               = data.rouletteCnt;                 // 룰렛개수
    eventRouletteData.exceptionUserRouletteCnt  = data.exceptionUserRouletteCnt;    // 이벤트제한고객룰렛설정
    //eventRouletteData.winnerNotice              = data.winnerNotice;                // 당첨자공지사항(URL입력) -> 이벤트 기본정보로 변경됨
    eventRouletteData.expectJoinUserCntRoulette         = data.expectJoinUserCntRoulette;    // 예상참여자수

    // @@@@@ 룰렛이벤트정보
    paramData.eventRouletteInfo = eventRouletteData;

    // ------------------------------------------------------------------------
    // 그룹 데이터 Set
    // ------------------------------------------------------------------------
    paramData.groupList = fnSetGroupData();

    return paramData;

  } //  End of function fnParamDataSetNormal(paramData, data)

  // ==========================================================================
  // # 체험단이벤트 데이터 Set
  // ==========================================================================
  function fnParamDataSetExperience(paramData, data) {
    //console.log('# fnParamDataSetExperience Start');

    // ------------------------------------------------------------------------
    // @@@@@ 이벤트정보 - 상세정보
    // ------------------------------------------------------------------------
    // EV_EVENT
    paramData.eventInfo.employeeJoinYn   = data.employeeJoinYn;             // 임직원참여여부
    paramData.eventInfo.eventJoinTp      = data.eventJoinTp;                // 참여횟수설정(ID당)
    paramData.eventInfo.eventDrawTp      = data.eventDrawTp;                // 담청자설정
    paramData.eventInfo.winnerNotice     = data.winnerNotice;               // 당첨자공지사항(url입력)
    paramData.eventInfo.winnerInfor      = data.winnerInfor;                // 당첨자안내
    paramData.eventInfo.timeOverCloseYn  = data.experienceTimeOverCloseYn == 'Y' ? 'Y' : 'N';  // 자동종료여부 (체험단인 경우 experienceTimeOverCloseYn 값으로 덮어 씀)
    // TODO 어떤 영역인지 확인해볼것 paramData.eventInfo.winnerInforDt    = data.winnerInforDt;      // 당첨자안내일자

    var eventExperienceData = new Object();
    eventExperienceData.btnColorCd      = data.btnColorCd;          // 참여버튼 BG color code
    eventExperienceData.commentCodeYn   = data.commentCodeYn;       // 댓글분류설정여부
    eventExperienceData.pmCouponId      = data.pmCouponId;          // 당첨자혜택 (쿠폰 콤보)
    //eventExperienceData.winnerNotice    = data.winnerNotice;        // 당첨자 공지사항 (url 입력) -> 이벤트 기본정보로 변경됨
    eventExperienceData.selectStartDt   = data.selectStartDe   + '' + data.selectStartHour   + '' + data.selectStartMin   + '00'; // 당첨자선정기간 시작일시
    eventExperienceData.selectEndDt     = data.selectEndDe     + '' + data.selectEndHour     + '' + data.selectEndMin     + '59'; // 당첨자선정기간 종료일시
    eventExperienceData.feedbackStartDt = data.feedbackStartDe + '' + data.feedbackStartHour + '' + data.feedbackStartMin + '00'; // 후기작성기간 시작일시
    eventExperienceData.feedbackEndDt   = data.feedbackEndDe   + '' + data.feedbackEndHour   + '' + data.feedbackEndMin   + '59'; // 후기작성기간 종료일시
    eventExperienceData.recruitCloseYn  = 'N';                      // 모집종료여부(기본값 N)

    if (paramData.eventInfo.eventDrawTp == 'EVENT_DRAW_TP.FIRST_COME') {
      // 당첨자설정유형 : 선착순당첨인 경우
      eventExperienceData.firstComeCnt    = data.firstComeCnt;      // 선착순당첨인원
    }

    // ------------------------------------------------------------------------
    // 체험상품
    // ------------------------------------------------------------------------
    var targetGrid    = $('#experienceGrid').data('kendoGrid');
    var targetGridDs  = targetGrid.dataSource;
    var targetGridArr = targetGridDs.data();

    if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0) {
      for (var i = 0; i < targetGridArr.length; i++) {
        eventExperienceData.ilGoodsId = targetGridArr[i].ilGoodsId;
        // 한건이므로 break 처리
        break;
      } // End of for (var i = 0; i < targetGridArr.length; i++)
    } // End of if (targetGridArr != undefined && targetGridArr != null && targetGridArr.length > 0)

    // ------------------------------------------------------------------------
    // 댓글허용여부에 따른 처리
    // ------------------------------------------------------------------------
    commentCodeYn_0
    let eventDrawTp = $('input[name=eventDrawTp]:checked').val();

    if (eventDrawTp == 'EVENT_DRAW_TP.FIRST_COME') {
      // 당첨자설정 : 선착순
      // @@@@@
      eventExperienceData.commentCodeYn == 'N';
      // @@@@@ 초기화
      paramData.eventCommentCodeList = null;
    }
    else {
      // 당첨자설정 : 선착순 이외
      if (eventExperienceData.commentCodeYn == 'Y') {
        // ----------------------------------------------------------------------
        // 댓글허용여부 : Y
        // ----------------------------------------------------------------------

        // ----------------------------------------------------------------------
        // 댓글분류설정
        // ----------------------------------------------------------------------
        // @@@@@ 초기화
        paramData.eventCommentCodeList = null;

        if($('input[name=commentCodeYn]:checked').val() == 'Y'){
          var commentCodeList = Array.from(gbCommentCodeMaps.values());
          var eventCommentCodeList = new Array();

          if (commentCodeList != undefined && commentCodeList != null && commentCodeList.length > 0) {
            for (var i = 0; i < commentCodeList.length; i++) {
              var commentCodeObj = new Object();
              commentCodeObj.commentValue = commentCodeList[i];
              eventCommentCodeList.push(commentCodeObj);
            }
          }
          // @@@@@
          paramData.eventCommentCodeList = eventCommentCodeList;
        }
      }

    }

    // @@@@@ 체험단이벤트정보
    paramData.eventExperienceInfo = eventExperienceData;

    return paramData;

  } //  End of function fnParamDataSetNormal(paramData, data)

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록/수정 DataSet End
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  // ==========================================================================
  // # 조회데이터Set - 일반이벤트
  // ==========================================================================
  function fnSetSearchDataNormal(data) {
    //console.log('# fnSetSearchDataNormal Start');
    var eventInfo       = data.eventInfo;
    var eventCouponList = data.eventCouponList;
    var eventDetlInfo   = data.eventNormalInfo;

    // ------------------------------------------------------------------------
    // 이벤트기본정보/이벤트상세정보
    // ------------------------------------------------------------------------
    if (eventDetlInfo != undefined && eventDetlInfo != null && eventDetlInfo != '') {
      eventInfo.commentYn           = eventDetlInfo.commentYn;              // 댓글허용여부
      eventInfo.normalEventTp       = eventDetlInfo.normalEventTp;          // 참여방법
      eventInfo.awardRt             = eventDetlInfo.awardRt;                // 당첨확률
      eventInfo.expectJoinUserCnt   = eventDetlInfo.expectJoinUserCnt;      // 예상참여자수
      eventInfo.joinCondition       = eventDetlInfo.joinCondition;          // 참여조건
      eventInfo.orderCnt            = eventDetlInfo.orderCnt;               // 최소구매건수
      eventInfo.normalOrderPrice    = eventDetlInfo.orderPrice;             // 최소구매가격
      eventInfo.goodsDeliveryTp     = eventDetlInfo.goodsDeliveryTp;        // 참여조건 주문고객 주문배송유형




      // ----------------------------------------------------------------------
      // 댓글허용여부에 따른 노출 처리
      // ----------------------------------------------------------------------
//      if (eventInfo.commentYn == 'Y') {
      if (eventInfo.normalEventTp != 'NORMAL_EVENT_TP.NONE') {
        // 댓글 허용

        // --------------------------------------------------------------------
        // 상세정보-노출
        // --------------------------------------------------------------------
        fnDisplayScreen(eventInfo.eventTp);

        // --------------------------------------------------------------------
        // 상세정보 Set
        // --------------------------------------------------------------------
        eventInfo.commentCodeYn   = eventDetlInfo.commentCodeYn;  // 댓글분류설정
        eventInfo.eventBenefitTp  = eventDetlInfo.eventBenefitTp; // 당첨자혜택유형
        eventInfo.pmPointId       = eventDetlInfo.pmPointId;      // 적립금
        eventInfo.totalWinCnt     = eventDetlInfo.totalWinCnt;      // 적립금
        // 임직원참여여부 : eventInfo에 있음
        // 참여횟수       : eventInfo에 있음
        // 당첨자설졍유형 : eventInfo에 있음

        // --------------------------------------------------------------------
        // 당첨자 설정(참여방법 응모버튼 시) : 즉시당첨 정보 노출
        // --------------------------------------------------------------------
        if(eventInfo.eventDrawTp == 'EVENT_DRAW_TP.AUTO' && eventInfo.normalEventTp == 'NORMAL_EVENT_TP.BUTTON'){
            $('#immediatleyWinDiv').show();
        }else{
            $('#immediatleyWinDiv').hide();
        }

        // --------------------------------------------------------------------
        // 참여조건 설정 : 즉시당첨 정보 노출
        // --------------------------------------------------------------------
        if(eventDetlInfo.joinCondition == 'JOIN_CONDITION.ORDER'){
            $('#joinConditionDiv').show();
        }else{
            $('#joinConditionDiv').hide();
        }

        // --------------------------------------------------------------------
        // 댓글분류설정
        // --------------------------------------------------------------------
        if (eventInfo.commentCodeYn == 'Y') {
          // 분류영역 노출
          $('#commentCodeYnTr').show();
          $('#commentCodeValueDiv').show();
          $('#commentCodeValueListDiv').show();

          // 댓글분류 초기화 : 수정 후 재조회 시 중복방지
          gbCommentCodeMaps.clear();
          $('.js__remove__commentTpList').each(function(){
            $(this).remove();
          });

          var eventCommentCodeList = data.eventCommentCodeList;

          if (eventCommentCodeList != undefined && eventCommentCodeList != null && eventCommentCodeList.length > 0) {
            for (var i = 0; i < eventCommentCodeList.length; i++) {
              fnEventAddCommentCode(eventCommentCodeList[i].commentValue);
            }
          }
        }

        // --------------------------------------------------------------------
        // 당첨자혜택 노출
        // --------------------------------------------------------------------
        eventInfo = fnTemplateSetBenefit (eventInfo.eventBenefitTp, eventInfo, eventDetlInfo, eventCouponList, 'N', null);

      }
      else {
        // 댓글 미허용

        // --------------------------------------------------------------------
        // 상세정보-숨김
        // --------------------------------------------------------------------
        fnDisplayScreen(eventInfo.eventTp+'.NO');
      }
    }

    // ------------------------------------------------------------------------
    // 댓글분류설정 리스트
    // ------------------------------------------------------------------------
    if (eventInfo.commentCodeYn == 'Y') {
      // TODO 템플릿에 넣을 것
    }

    // ------------------------------------------------------------------------
    // 당첨자설정에 따른 경품/응모 노출 처리
    // ------------------------------------------------------------------------
    fnBenefitItemGiftEnterView(eventInfo.eventDrawTp);
    //console.log('# statusSe :: ', eventInfo.statusSe);
    // ------------------------------------------------------------------------
    // 상세정보 비활성처리 - 진행예정이 아니면 비활성
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'BEF') {
      // 진행예정 : 비활성 없음
    }
    else if (eventInfo.statusSe == 'ING' || eventInfo.statusSe == 'END') {
      // 진행중/종료 : 부분 비활성

      // 댓글허용여부
//      $('input[name=commentYn]').attr('disabled', true);

      // 참여방법
      $('input[name=normalEventTp]').attr('disabled', true);
      // 참여조건
      $('input[name=joinCondition]').attr('disabled', true);
      // 적용범위
      $('#goodsCoverageType').data('kendoDropDownList').enable(false);
      $('#goodsIncludeYn').data('kendoDropDownList').enable(false);
      $('#fnAddCoverageGoods').hide();

      // 당첨확률
      $('input[name=awardRt]').attr('disabled', true);
      // 예상참여자수
      $('input[name=expectJoinUserCnt]').attr('disabled', true);
      // 최소 주문건수 결제정보
      $('#normalEventStampOrderTp').data('kendoDropDownList').enable(false);    // kendoDropDownList
      // 참여조건 주문고객 주문배송유형
      $('#goodsDeliveryTp').data('kendoDropDownList').enable(false);    // kendoDropDownList
      // 최소구매건수
      $('input[name=orderCnt]').attr('disabled', true);
      // 최수구매금액
      $('input[name=normalOrderPrice]').attr('disabled', true);
      // 임직원참여여부
      $('input[name=employeeJoinYn]').attr('disabled', true);
      // 참여횟수 콤보
      $('#eventJoinTp').data('kendoDropDownList').enable(false);    // kendoDropDownList
      // 당첨자 설정
      $('input[name=eventDrawTp]').attr('disabled', true);
      // 댓글분류설정
      $('input[name=commentCodeYn]').attr('disabled', true);
      // 분류값 입력
      $('input[name=inputValue]').attr('disabled', true);
      // 분류추가 버튼
      $('#btnAddCommentCode').prop('disabled', true);
      //$('#btnAddCommentCode').prop('disabled', 'disabled');
      // 분류삭제 버튼
      $('.js__remove__commentTpBtn').prop('disabled', true);
      // 당첨자혜택
      $('input[name=eventBenefitTp]').attr('disabled', true);
      // 쿠폰조회 버튼
      $('#btnCouponSearch').prop('disabled', true);
      // 쿠폰그리드 지급수량 (전체)
      $("input[name=couponCount]").attr('disabled', true);          // input text
      // 쿠폰그리드 총 당첨 수량 (전체)
      $("input[name=couponTotalCount]").attr('disabled', true);          // input text
      // 적립금 총 당첨 수량
      $("input[name=totalWinCount]").attr('disabled', true);          // input text
      // 당첨자 혜택 경품 :  총 당첨 수량
      $("input[name=totalWinCntGift]").attr('disabled', true);          // input text
      // 쿠폰그리드 삭제 버튼
      $('.btnCouponDel').prop('disabled', true);                    // button
      // 적립금 콤보
      $('#pmPointId').data('kendoDropDownList').enable(false);      // kendoDropDownList
      // 적립금 추가 버튼
      $('#btnPointAdd').prop('disabled', true);
      // 적립금그리드 삭제 버튼
      $('.btnPointDel').prop('disabled', true);                     // button
      // 경품명 입력
      $('input[name=benefitNmGift]').attr('disabled', true);
      // 응모명 입력
      $('input[name=benefitNmEnter]').attr('disabled', true);
      // 당첨자공지사항(url입력)
      //$('#winnerNotice').attr('disabled', 'disabled');              // textarea
      // 당첨자안내
      $('#winnerInfor').attr('disabled', 'disabled');               // textarea
      // 자동종료여부
      if(eventInfo.timeOverCloseYn == 'N'){
        $("input[name=timeOverCloseYn]:checkbox").prop("checked", false);
      }else{
        $("input[name=timeOverCloseYn]:checkbox").prop("checked", true);
      }
      // 신규가입자 한정 여부
      $("input[name=checkNewUserYn]").attr('disabled', true);
    }
    return eventInfo;

  } // End of function fnSetSearchDataNormal(data)


  // ==========================================================================
  // # 조회데이터Set - 설문이벤트
  // ==========================================================================
  function fnSetSearchDataSurvey(data) {
    //console.log('# fnSetSearchDataSurvey Start');

    var eventInfo               = data.eventInfo;
    var eventCouponList         = data.eventCouponList;
    var eventSurveyInfo         = data.eventSurveyInfo;
    var eventSurveyQuestionList = data.eventSurveyQuestionList;

    // ------------------------------------------------------------------------
    // 이벤트기본정보/이벤트상세정보
    // ------------------------------------------------------------------------
    if (eventSurveyInfo != undefined && eventSurveyInfo != null && eventSurveyInfo != '') {

      // ----------------------------------------------------------------------
      // 상세정보-노출
      // ----------------------------------------------------------------------
      fnDisplayScreen(eventInfo.eventTp);

      // ----------------------------------------------------------------------
      // 상세정보 Set
      // ----------------------------------------------------------------------
      eventInfo.btnColorCd      = eventSurveyInfo.btnColorCd;       // 참여버튼 BG color code
      eventInfo.eventBenefitTp  = eventSurveyInfo.eventBenefitTp;   // 이벤트당첨유형(쿠폰/적립금/경품/응모)

      // --------------------------------------------------------------------
      // 당첨자혜택 노출
      // --------------------------------------------------------------------
      eventInfo = fnTemplateSetBenefit (eventSurveyInfo.eventBenefitTp, eventInfo, eventSurveyInfo, eventCouponList, 'N', null);

      // ----------------------------------------------------------------------
      // 당첨혜택영역
      // ----------------------------------------------------------------------
      //console.log('# eventStampDetlList.length :: ', eventStampDetlList.length);
      if (eventSurveyQuestionList != undefined && eventSurveyQuestionList != null && eventSurveyQuestionList.length > 0) {
        let surveyCount = eventSurveyQuestionList.length;
        //console.log('# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>');
        //console.log('# >>>>> surveyCount :: ', surveyCount);
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 설문 영역 for 처리
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        $('#surveyDiv tbody').empty();
        for (var i = 0; i < surveyCount; i++) {
          let index = i+1;
          //console.log('# ======================================');
          //console.log('# 설문 [', index, '] Start');

          // 아이템 개수
          let eventSurveyItemList = eventSurveyQuestionList[i].eventSurveyItemList;

          let itemCnt = eventSurveyItemList.length;

          // ------------------------------------------------------------------
          // 템플릿 생성
          // ------------------------------------------------------------------
          fnTmplAddSurveySearch(itemCnt);

          // ------------------------------------------------------------------
          // 설문 정보 Set
          // ------------------------------------------------------------------
          // 설문제목
          $('#title'+index).val(eventSurveyQuestionList[i].title);

          // 설문유형
          //$('input:radio[name="eventSurveyTp'+index+'"][value="'+eventSurveyQuestionList[i].eventSurveyTp+'"]').prop("checked", true);

          // ------------------------------------------------------------------------
          // 설문유형 라디오 생성
          // ------------------------------------------------------------------------
          const eventSurveyTp = 'eventSurveyTp' + gbSurveyCurrentRowIndex;
          fnMakeEventSurveyTp(eventSurveyTp, eventSurveyQuestionList[i].eventSurveyTp);

          // ------------------------------------------------------------------
          // 보기 정보 Set
          // ------------------------------------------------------------------
          if (eventSurveyItemList != undefined && eventSurveyItemList != null && eventSurveyItemList.length > 0) {
            let path      = '';
            let originNm  = '';
            let viewId    = '';

            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // 보기 영역 for 처리
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            for (let j = 0; j < eventSurveyItemList.length; j++) {
              let subIndex = j+1;
              //console.log('# --------------------------------------');
              //console.log('# 보기 [', index, '][', subIndex, '] Start');
              // --------------------------------------------------------------
              // 보기 정보
              // --------------------------------------------------------------
              // 보기내용
              $('#item'+index+'_'+subIndex).val(eventSurveyItemList[j].item);
              // 직접입력여부
              if (eventSurveyItemList[j].directInputYn == 'Y') {
                $('input:checkbox[name="directInputYn'+index+'_'+subIndex+'"]').prop('checked', true);
              }
              else {
                $('input:checkbox[name="directInputYn'+index+'_'+subIndex+'"]').prop('checked', false);
              }

              // --------------------------------------------------------------
              // 파일 정보
              // --------------------------------------------------------------
              let eventSurveyItemAttcList = eventSurveyItemList[j].eventSurveyItemAttcList;

              if (eventSurveyItemAttcList != undefined && eventSurveyItemAttcList != null && eventSurveyItemAttcList.length > 0) {
                //console.log('# 이미지 존재 [', index, '][', subIndex, '] :: ', JSON.stringify(eventSurveyItemAttcList));
                for (let k = 0; k < eventSurveyItemAttcList.length; k++) {
                  path      = eventSurveyItemAttcList[k].imgPath;
                  originNm  = eventSurveyItemAttcList[k].imgOriginNm;
                  //console.log('# [', index, '][', subIndex, '] path     :: ', path);
                  //console.log('# [', index, '][', subIndex, '] originNm :: ', originNm);
                  viewId    = 'imgSurveyView'+index+'_'+subIndex;
                  if (path != undefined && path != null && path != '') {
                    // --------------------------------------------------------
                    // 전역변수에 Set
                    // --------------------------------------------------------
                    var imgMap = new Map();
                    imgMap.set('Path'     , path);
                    imgMap.set('OriginNm' , originNm);
                    if (gbImgSurveyMap.get(index+'') == undefined || gbImgSurveyMap.get(index+'') == null) {
                      // ------------------------------------------------------
                      // 설문이미지그룹 인덱스 미존재
                      // ------------------------------------------------------
                      // subIndex
                      let imgSurveyMap = new Map();
                      imgSurveyMap.set(subIndex+'', imgMap);
                      // index
                      gbImgSurveyMap.set(index+'', imgSurveyMap);
                    }
                    else {
                      // ------------------------------------------------------
                      // 설문인덱스그룹 존재
                      // ------------------------------------------------------
                      gbImgSurveyMap.get(index+'').set(subIndex+'', imgMap);
                      //let imgSurveyMap = gbImgSurveyMap.get(index+'').get();
                      //imgSurveyMap.set(subIndex+'', imgMap);
                    }

                    // --------------------------------------------------------
                    // 이미지 노출
                    // --------------------------------------------------------
                    //console.log('# 이미지노출[', index, '][', subIndex, '] :: [', path, '][', originNm, '][', viewId, ']');
                    fnSetImgView(path, originNm, viewId);
                  }
                }
              } // End of if (eventSurveyItemAttcList != undefined && eventSurveyItemAttcList != null && eventSurveyItemAttcList.length > 0)

            } // End of for (let j = 0; j < eventSurveyItemList.length; j++)

          } // End of if (eventSurveyItemList != undefined && eventSurveyItemList != null && eventSurveyItemList.length > 0)



        } // End of for (var i = 0; i < surveyCount; i++)

        //console.log('# gbImgStampDefaultMap.size(3) :: ', gbImgStampDefaultMap.size);
        //console.log('# gbImgStampCheckMap.size(3)   :: ', gbImgStampCheckMap.size);
        //console.log('# gbImgStampIconMap.size(3)    :: ', gbImgStampIconMap.size);

        //console.log('# gbImgStampDefaultMap :: ', JSON.stringify(Array.from(gbImgStampDefaultMap)));
        //if (gbImgStampDefaultMap.size > 0) {
        //  for (var k = 0; k < gbImgStampDefaultMap.size; k++) {
        //    console.log('# gbImgStampDefaultMap[', k+1, '] :: ', JSON.stringify(Array.from(gbImgStampDefaultMap.get((k+1)+''))));
        //  }
        //}
        //console.log('# gbImgStampDefaultMap(3) :: ', JSON.stringify(Array.from(gbImgStampDefaultMap.entries())));
      }

    } // End of if (eventStampInfo != undefined && eventStampInfo != null && eventStampInfo != '')

    // ------------------------------------------------------------------------
    // 당첨자설정에 따른 경품/응모 노출 처리
    // ------------------------------------------------------------------------
    fnBenefitItemGiftEnterView(eventInfo.eventDrawTp);
    //console.log('# statusSe :: ', eventInfo.statusSe);
    // ------------------------------------------------------------------------
    // 상세정보 비활성처리 - 진행예정이 아니면 비활성
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'BEF') {
      // 진행예정 : 비활성 없음
    }
    else if (eventInfo.statusSe == 'ING' || eventInfo.statusSe == 'END') {
      // 진행중/종료 : 부분 비활성

      // 임직원참여여부
      $('input[name=employeeJoinYn]').attr('disabled', true);
      // 참여횟수 콤보
      $('#eventJoinTp').data('kendoDropDownList').enable(false);    // kendoDropDownList
      // 당첨자 설정
      $('input[name=eventDrawTp]').attr('disabled', true);
      // 참여버튼 BG color code
      $('input[name=btnColorCd]').attr('disabled', true);

      // --------------------------------------------------------------------
      // 당첨자혜택 영역
      // --------------------------------------------------------------------
      // 당첨자혜택
      $('input[name=eventBenefitTp]').attr('disabled', true);
      // 쿠폰조회 버튼
      $('#btnCouponSearch').prop('disabled', true);
      // 쿠폰그리드 지급수량 (전체)
      $("input[name=couponCount]").attr('disabled', true);          // input text
      // 쿠폰그리드 삭제 버튼
      $('.btnCouponDel').prop('disabled', true);                    // button
      // 적립금 콤보
      $('#pmPointId').data('kendoDropDownList').enable(false);      // kendoDropDownList
      // 적립금 추가 버튼
      $('#btnPointAdd').prop('disabled', true);
      //$('.btnPointAdd').prop('disabled', true);                             // button
      // 적립금그리드 삭제 버튼
      $('.btnPointDel').prop('disabled', true);                     // button
      // 경품명 입력
      $('input[name=benefitNmGift]').attr('disabled', true);
      // 응모명 입력
      $('input[name=benefitNmEnter]').attr('disabled', true);
      // 당첨자공지사항(url입력)
      //$('#winnerNotice').attr('disabled', 'disabled');              // textarea
      // 당첨자안내
      $('#winnerInfor').attr('disabled', 'disabled');               // textarea
      // 설문 추가 버튼
      $('#fnBtnAddSurvey').prop('disabled', true);                  // button

      // 자동종료여부
      if(eventInfo.timeOverCloseYn == 'N'){
        $("input[name=timeOverCloseYn]:checkbox").prop("checked", false);
      }else{
        $("input[name=timeOverCloseYn]:checkbox").prop("checked", true);
      }

      // ----------------------------------------------------------------------
      // 시간차로 인한 활성/비활성 미적용될 경우 Timeout 적용할 것
      // ----------------------------------------------------------------------
      //setTimeout(function() {}, 1000);

      // ----------------------------------------------------------------------
      // 설문 영역
      // ----------------------------------------------------------------------
      if (eventSurveyQuestionList != undefined && eventSurveyQuestionList != null && eventSurveyQuestionList.length) {
        //console.log('# ##### eventSurveyQuestionList.length :: ', eventSurveyQuestionList.length);
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 설문 영역 for 처리
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        for (var i = 0; i < eventSurveyQuestionList.length; i++) {
          let index = i+1;

          // 아이템 개수
          let eventSurveyItemList = eventSurveyQuestionList[i].eventSurveyItemList;

          // 설문제목
          $('input[name=title'+index+']').attr('disabled', true);
          // 설문유형
          $('input[name=eventSurveyTp'+index+']').attr('disabled', true);

          // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          // 보기 영역 for 처리
          // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          for (let j = 0; j < eventSurveyItemList.length; j++) {
            let subIndex = j+1;

            //console.log('##### [', index, '][', subIndex, ']');
            // 파일선택 버튼
            $('#imgSurvey'+index+'_'+subIndex).attr('disabled', true);                // 버튼
            // 보기입력
            $('input[name=item'+index+'_'+subIndex+']').attr('disabled', true);       // input text
            // 추가 버튼
            $('#btnAddItem'+index+'_'+subIndex).attr('disabled', true);               // 버튼
            // 삭제 버튼
            $('#btnRemoveItem'+index+'_'+subIndex).attr('disabled', true);            // 버튼
            // 직접입력
            $('#directInputYn'+index+'_'+subIndex).attr('disabled', true);            // 체크박스
            // 설문삭제 버튼
            $('.js__remove__survey').attr('disabled', true);            // 버튼

          } // End of for (let j = 0; j < eventSurveyItemList.length; j++)

        } // End of for (var i = 0; i < eventStampDetlList.length; i++)

        // --------------------------------------------------------------------
        // 설문영역의 이벤트설문유형(단일선택/복수선택) 비활성화 처리 : 즉시 비활성화 안되는 문제 조치
        // --------------------------------------------------------------------
        setTimeout(function() {
          let $eventSurveyTp = $('input[name^=eventSurveyTp]');
          //console.log('# $eventSurveyTp.length :: ', $eventSurveyTp.length);
          $eventSurveyTp.each(function(){
            //console.log('# id :: ', $(this).attr('id'));
            $(this).attr('disabled', true);
          });
        }, 1000);   // 모두 처리 안될 경우 시간 늘릴 것


      }

    }

    return eventInfo;

  } // End of function fnSetSearchDataStmap(data)

  // ==========================================================================
  // # 조회데이터Set - 스탬프(출석)/스탬프(미션)/스탬프(구매)
  // ==========================================================================

  // ==========================================================================
  // # 조회데이터Set - 스탬프이벤트
  // ==========================================================================
  function fnSetSearchDataStamp(data) {
    //console.log('# fnSetSearchDataStamp Start');
    //console.log('# data.eventInfo :: ', JSON.stringify(data.eventInfo));
    var eventInfo           = data.eventInfo;
    var eventStampInfo      = data.eventStampInfo;
    var eventStampDetlList  = data.eventStampDetlList;

    // ------------------------------------------------------------------------
    // 이벤트기본정보/이벤트상세정보
    // ------------------------------------------------------------------------
    if (eventStampInfo != undefined && eventStampInfo != null && eventStampInfo != '') {

      // ----------------------------------------------------------------------
      // 상세정보-노출
      // ----------------------------------------------------------------------
      fnDisplayScreen(eventInfo.eventTp);

      // ----------------------------------------------------------------------
      // 상세정보 Set
      // ----------------------------------------------------------------------
      eventInfo.btnColorCd = eventStampInfo.btnColorCd;   // 참여버튼 BG color code
      eventInfo.stampCnt1  = eventStampInfo.stampCnt1;    // 스탬프 개수설정 : 노출개수
      eventInfo.stampCnt2  = eventStampInfo.stampCnt2;    // 스탬프 개수설정 : 설정개수

      if (gbEventTp == 'EVENT_TP.PURCHASE') {
        // 이벤트유형 : 스탬프(구매)
        eventInfo.orderPrice        = eventStampInfo.orderPrice    // 스탬프지급조건-금액
        $('#eventStampOrderTp').data('kendoDropDownList').value(eventStampInfo.eventStampOrderTp);// 스탬프지급조건-주문상태

      }

      // ----------------------------------------------------------------------
      // 스탬프 기본 이미지
      // ----------------------------------------------------------------------
      let path      = eventStampInfo.defaultPath;
      let originNm  = eventStampInfo.defaultOriginNm;
      let viewId    = 'imgStampDefaultView';
      // 전역변수에 Set
      gbImgStampDefaultPath      = path;
      gbImgStampDefaultOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      // ----------------------------------------------------------------------
      // 스탬프 체크 이미지
      // ----------------------------------------------------------------------
      path      = eventStampInfo.checkPath;
      originNm  = eventStampInfo.checkOriginNm;
      viewId    = 'imgStampCheckView';
      // 전역변수에 Set
      gbImgStampCheckPath      = path;
      gbImgStampCheckOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      // ----------------------------------------------------------------------
      // 스탬프 배경 이미지
      // ----------------------------------------------------------------------
      path      = eventStampInfo.bgPath;
      originNm  = eventStampInfo.bgOriginNm;
      viewId    = 'imgStampBgView';
      // 전역변수에 Set
      gbImgStampBgPath      = path;
      gbImgStampBgOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      //console.log('# gbImgStampDefaultMap.size :: ', gbImgStampDefaultMap.size);
      //console.log('# gbImgStampCheckMap.size   :: ', gbImgStampCheckMap.size);
      //console.log('# gbImgStampIconMap.size    :: ', gbImgStampIconMap.size);
      gbImgStampDefaultMap  = new Map();
      gbImgStampCheckMap    = new Map();
      gbImgStampIconMap     = new Map();
      //console.log('# gbImgStampDefaultMap.size(2) :: ', gbImgStampDefaultMap.size);
      //console.log('# gbImgStampCheckMap.size(2)   :: ', gbImgStampCheckMap.size);
      //console.log('# gbImgStampIconMap.size(2)    :: ', gbImgStampIconMap.size);

      // ----------------------------------------------------------------------
      // 당첨혜택영역
      // ----------------------------------------------------------------------
      //console.log('# eventStampDetlList.length :: ', eventStampDetlList.length);
      if (eventStampDetlList != undefined && eventStampDetlList != null && eventStampDetlList.length > 0) {

        // --------------------------------------------------------------------
        // 템플릿 생성
        // --------------------------------------------------------------------
        let benefitCnt = eventStampDetlList.length;
        //console.log('# >>>>> benefitCnt :: ', benefitCnt);

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 당첨자 혜택 영역 for 처리
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        for (var i = 0; i < benefitCnt; i++) {

          let index = i+1;
          fnTmplAddStamp(gbEventTp, benefitCnt);

          // ------------------------------------------------------------------
          // 스탬프번호
          // ------------------------------------------------------------------
          if (gbEventTp == 'EVENT_TP.ATTEND') {
            // 이벤트유형 : 스탬프(출석)
            $('#stampIdx'+index).data('kendoDropDownList').value(eventStampDetlList[i].stampCnt);
            // 이벤트혜택유형 (쿠폰/적립금/경품/응모/제공안함)
            $('input:radio[name="eventBenefitTp'+index+'"]:input[value="'+eventStampDetlList[i].eventBenefitTp+'"]').prop("checked", true);
          }
          // ------------------------------------------------------------------
          // 스탬프 기본 이미지
          // ------------------------------------------------------------------
          path      = eventStampDetlList[i].defaultPath;
          originNm  = eventStampDetlList[i].defaultOriginNm;
          viewId    = 'imgStampDefaultView'+index;

          if (path != undefined && path != null && path != '') {
            // 전역변수에 Set
            var imgDefaultMap = new Map();
            imgDefaultMap.set('Path'     , path);
            imgDefaultMap.set('OriginNm' , originNm);
            gbImgStampDefaultMap.set(index+'', imgDefaultMap);
            // 이미지 노출
            if (path != undefined && path != null && path != '') {
              fnSetImgView(path, originNm, viewId);
            }
          }

          // ------------------------------------------------------------------
          // 스탬프 체크 이미지
          // ------------------------------------------------------------------
          path      = eventStampDetlList[i].checkPath;
          originNm  = eventStampDetlList[i].checkOriginNm;
          viewId    = 'imgStampCheckView'+index;

          if (path != undefined && path != null && path != '') {
            // 전역변수에 Set
            var imgCheckMap = new Map();
            imgCheckMap.set('Path'     , path);
            imgCheckMap.set('OriginNm' , originNm);
            gbImgStampCheckMap.set(index+'', imgCheckMap);
            // 이미지 노출
            if (path != undefined && path != null && path != '') {
              fnSetImgView(path, originNm, viewId);
            }
          }

          // ------------------------------------------------------------------
          // 스탬프 아이콘 이미지
          // ------------------------------------------------------------------
          if (gbEventTp == 'EVENT_TP.MISSION') {
            // 스탬프유형 : 미션
            path      = eventStampDetlList[i].iconPath;
            originNm  = eventStampDetlList[i].iconOriginNm;
            viewId    = 'imgStampIconView'+index;

            if (path != undefined && path != null && path != '') {
              // 전역변수에 Set
              var imgIconMap = new Map();
              imgIconMap.set('Path'     , path);
              imgIconMap.set('OriginNm' , originNm);
              gbImgStampIconMap.set(index+'', imgIconMap);
              // 이미지 노출
              if (path != undefined && path != null && path != '') {
                fnSetImgView(path, originNm, viewId);
              }
            }
          }

          // ------------------------------------------------------------------
          // 당첨혜택
          // ------------------------------------------------------------------
          eventInfo = fnTemplateSetBenefit (eventStampDetlList[i].eventBenefitTp, eventInfo, eventStampDetlList[i], eventStampDetlList[i].eventCouponList, 'Y', index);

        } // End of for (var i = 0; i < benefitCnt; i++)

        //console.log('# gbImgStampDefaultMap.size(3) :: ', gbImgStampDefaultMap.size);
        //console.log('# gbImgStampCheckMap.size(3)   :: ', gbImgStampCheckMap.size);
        //console.log('# gbImgStampIconMap.size(3)    :: ', gbImgStampIconMap.size);

        //console.log('# gbImgStampDefaultMap :: ', JSON.stringify(Array.from(gbImgStampDefaultMap)));
        //if (gbImgStampDefaultMap.size > 0) {
        //  for (var k = 0; k < gbImgStampDefaultMap.size; k++) {
        //    console.log('# gbImgStampDefaultMap[', k+1, '] :: ', JSON.stringify(Array.from(gbImgStampDefaultMap.get((k+1)+''))));
        //  }
        //}
        //console.log('# gbImgStampDefaultMap(3) :: ', JSON.stringify(Array.from(gbImgStampDefaultMap.entries())));
      }

    } // End of if (eventStampInfo != undefined && eventStampInfo != null && eventStampInfo != '')

    //console.log('# statusSe :: ', eventInfo.statusSe);
    // ------------------------------------------------------------------------
    // 상세정보 비활성처리 - 진행예정이 아니면 비활성
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'BEF') {
      // 진행예정 : 비활성 없음
    }
    else if (eventInfo.statusSe == 'ING' || eventInfo.statusSe == 'END') {
      // 진행중/종료 : 부분 비활성

      // 임직원참여여부
      $('input[name=employeeJoinYn]').attr('disabled', true);
      // 참여횟수 콤보
      $('#eventJoinTp').data('kendoDropDownList').enable(false);    // kendoDropDownList
      // 당첨자 설정
      $('input[name=eventDrawTp]').attr('disabled', true);
      // 참여버튼 BG color code
      $('input[name=btnColorCd]').attr('disabled', true);

      // 스탬프 기본 이미지 파일선택/삭제 버튼
      $('#imgStampDefault').attr('disabled', true);                 // 버튼
      $('#imgStampDefaultDel').attr('disabled', true);              // 버튼
      // 스탬프 체크 이미지 파일선택/삭제 버튼
      $('#btnImgStampCheck').attr('disabled', true);                // 버튼
      $('#imgStampCheckDel').attr('disabled', true);             // 버튼
      // 스탬프 배경 이미지 파일선택/삭제 버튼
      $('#btnImgStampBg').attr('disabled', true);                   // 버튼
      $('#btnImgStampDel').attr('disabled', true);                  // 버튼

      // 스탬프 노출개수 설정
      $('#stampCnt1').data('kendoDropDownList').enable(false);      // kendoDropDownList
      // 스탬프 설정개수 설정
      $('#stampCnt2').data('kendoDropDownList').enable(false);      // kendoDropDownList
      // 스탬프 지급조건
      if (gbEventTp == 'EVENT_TP.PURCHASE') {
        // 스탬프지급조건 - 금액
        $("input[name=orderPrice]").attr('disabled', true);               // input text
        // 스탬프지급조건 - 주문상태
        $('#eventStampOrderTp').data('kendoDropDownList').enable(false);  // kendoDropDownList
      }
      // 당첨자공지사항(url입력)
      //$('#winnerNotice').attr('disabled', 'disabled');                    // textarea
      // 당첨자안내
      $('#winnerInfor').attr('disabled', 'disabled');                     // textarea
      // 자동종료여부
      if(eventInfo.timeOverCloseYn == 'N'){
          $("input[name=timeOverCloseYn]:checkbox").prop("checked", false);
      }else{
          $("input[name=timeOverCloseYn]:checkbox").prop("checked", true);
      }

      // ----------------------------------------------------------------------
      // 당첨혜택 스탬프리스트
      // ----------------------------------------------------------------------
      if (eventStampDetlList != undefined && eventStampDetlList != null && eventStampDetlList.length) {

        for (var i = 0; i < eventStampDetlList.length; i++) {

          let index = i+1;

          // 스탬프번호
          if (gbEventTp == 'EVENT_TP.ATTEND') {
            // 이벤유형 : 스탬프(출석)
            $('#stampIdx'+index).data('kendoDropDownList').enable(false);       // kendoDropDownList
          }

          // 스탬프 기본 이미지 파일선택/삭제 버튼
          $('#imgStampDefault'+index).attr('disabled', true);                 // 버튼
          $('#imgStampDefaultDel'+index).attr('disabled', true);              // 버튼
          // 스탬프 체크 이미지 파일선택/삭제 버튼
          $('#imgStampCheck'+index).attr('disabled', true);                   // 버튼
          $('#imgStampCheckDel'+index).attr('disabled', true);                // 버튼
          // 스탬프 아이콘 이미지 파일선택/삭제 버튼
          $('#imgStampIcon'+index).attr('disabled', true);                    // 버튼
          $('#imgStampIconDel'+index).attr('disabled', true);                 // 버튼

          // 스탬프 당첨자혜택
          $('input[name=eventBenefitTp'+index+']').attr('disabled', true);
          // 쿠폰조회 버튼
          $('#btnCouponSearch'+index).prop('disabled', true);                 // button
          //적립금 콤보
          $('#pmPointId'+index).data('kendoDropDownList').enable(false);      // kendoDropDownList
          // 적립금 추가 버튼
          $('#btnPointAdd'+index).prop('disabled', true);
          // 경품명 입력
          $('input[name=benefitNmGiftStamp'+index+']').attr('disabled', true);
          // 응모명 입력
          $('input[name=benefitNmEnterStamp'+index+']').attr('disabled', true);
          // 스탬프노출URL
          if (gbEventTp == 'EVENT_TP.MISSION') {
            $('#stampUrl'+index).attr('disabled', 'disabled');                // textarea
          }

        } // End of for (var i = 0; i < eventStampDetlList.length; i++)

        // 쿠폰그리드 지급수량 (전체)
        $("input[name=couponCount]").attr('disabled', true);                  // input text
        // 쿠폰그리드 삭제 버튼
        $('.btnCouponDel').prop('disabled', true);                            // button
        // 적립금 추가 버튼
        $('.btnPointAdd').prop('disabled', true);                             // button
        // 적립금그리드 삭제 버튼
        $('.btnPointDel').prop('disabled', true);                             // button

        // 당첨혜택영역 삭제 버튼
        $('.js__remove__stampAttend').prop('disabled', true);                             // button

      }

    }
    //console.log('# >>>>> gbImgStampDefaultMap :: ', JSON.stringify(gbImgStampDefaultMap));

    return eventInfo;

  } // End of function fnSetSearchDataStmap(data)



  // ==========================================================================
  // # 조회데이터Set - 룰렛이벤트
  // ==========================================================================
  function fnSetSearchDataRoulette(data) {
    //console.log('# fnSetSearchDataRoulette Start');
    var eventInfo             = data.eventInfo;
    var eventRouletteInfo     = data.eventRouletteInfo;
    var eventRouletteItemList = data.eventRouletteItemList;

    // ------------------------------------------------------------------------
    // 이벤트기본정보/이벤트상세정보
    // ------------------------------------------------------------------------
    if (eventRouletteInfo != undefined && eventRouletteInfo != null && eventRouletteInfo != '') {

      // ----------------------------------------------------------------------
      // 상세정보-노출
      // ----------------------------------------------------------------------
      fnDisplayScreen(eventInfo.eventTp);

      // ----------------------------------------------------------------------
      // 상세정보 Set
      // ----------------------------------------------------------------------
      eventInfo.rouletteCnt               = eventRouletteInfo.rouletteCnt;                // 룰렛 개수 설정
      eventInfo.exceptionUserRouletteCnt  = eventRouletteInfo.exceptionUserRouletteCnt;   // 이벤트 제한고객 룰렛설정
      //eventInfo.winnerNotice            = eventRouletteInfo.winnerNotice;               // 당첨자 공지사항
      eventInfo.expectJoinUserCntRoulette = eventRouletteInfo.expectJoinUserCnt;          // 예상참여자수
      // 이벤트 제한고객 룰렛설정
      fnSetExceptionUserRouletteCnt(eventInfo.rouletteCnt);

      // ----------------------------------------------------------------------
      // 시작 버튼 이미지
      // ----------------------------------------------------------------------
      let path      = eventRouletteInfo.startBtnPath;
      let originNm  = eventRouletteInfo.startBtnOriginNm;
      let viewId    = 'imgRouletteStartBtnView';
      // 전역변수에 Set
      gbImgRouletteStartBtnPath      = path;
      gbImgRouletteStartBtnOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      // ----------------------------------------------------------------------
      // 화살표 이미지
      // ----------------------------------------------------------------------
      path      = eventRouletteInfo.arrowPath;
      originNm  = eventRouletteInfo.arrowOriginNm;
      viewId    = 'imgRouletteArrowView';
      // 전역변수에 Set
      gbImgRouletteArrowPath      = path;
      gbImgRouletteArrowOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      // ----------------------------------------------------------------------
      // 룰렛 배경 이미지
      // ----------------------------------------------------------------------
      path      = eventRouletteInfo.bgPath;
      originNm  = eventRouletteInfo.bgOriginNm;
      viewId    = 'imgRouletteBgView';
      // 전역변수에 Set
      gbImgRouletteBgPath      = path;
      gbImgRouletteBgOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      // ----------------------------------------------------------------------
      // 룰렛 이미지
      // ----------------------------------------------------------------------
      path      = eventRouletteInfo.roulettePath;
      originNm  = eventRouletteInfo.rouletteOriginNm;
      viewId    = 'imgRouletteView';
      // 전역변수에 Set
      gbImgRoulettePath      = path;
      gbImgRouletteOriginNm  = originNm;
      // 이미지 노출
      if (path != undefined && path != null && path != '') {
        fnSetImgView(path, originNm, viewId);
      }

      // ----------------------------------------------------------------------
      // 당첨혜택영역
      // ----------------------------------------------------------------------
      if (eventRouletteItemList != undefined && eventRouletteItemList != null && eventRouletteItemList.length > 0) {

        // --------------------------------------------------------------------
        // 템플릿 생성
        // --------------------------------------------------------------------
        let benefitCnt = eventRouletteItemList.length;
        //console.log('# >>>>> benefitCnt :: ', benefitCnt);

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 당첨자 혜택 영역 for 처리
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        for (var i = 0; i < benefitCnt; i++) {

          let index = i+1;
          fnTmplAddRoulette(gbEventTp, benefitCnt);

          // ------------------------------------------------------------------
          // 당첨혜택
          // ------------------------------------------------------------------
          eventInfo = fnTemplateSetBenefit (eventRouletteItemList[i].eventBenefitTp, eventInfo, eventRouletteItemList[i], eventRouletteItemList[i].eventCouponList, 'Y', index);

          // ------------------------------------------------------------------
          // 룰렛당첨활률/인원
          // ------------------------------------------------------------------
          $('#awardRt'+index).val(eventRouletteItemList[i].awardRt);
          $('#awardMaxCnt'+index).val(eventRouletteItemList[i].awardMaxCnt);
          $('#evEventRouletteItemId'+index).val(eventRouletteItemList[i].evEventRouletteItemId);

        } // End of for (var i = 0; i < benefitCnt; i++)

      }

    } // End of if (eventStampInfo != undefined && eventStampInfo != null && eventStampInfo != '')

    //console.log('# statusSe :: ', eventInfo.statusSe);

    // ------------------------------------------------------------------------
    // 상세정보 비활성처리 - 진행예정이 아니면 비활성
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'BEF') {
      // 진행예정 : 비활성 없음
    }
    else if (eventInfo.statusSe == 'ING' || eventInfo.statusSe == 'END') {
      // 진행중/종료 : 부분 비활성

      // 임직원참여여부
      $('input[name=employeeJoinYn]').attr('disabled', true);                       // radio
      // 참여횟수 콤보
      $('#eventJoinTp').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      // 당첨자 설정
      $('input[name=eventDrawTp]').attr('disabled', true);                          // radio

      //// 시작 버튼 이미지 파일선택/삭제 버튼
      //$('#imgRouletteStartBtn').attr('disabled', true);                           // 버튼
      //$('#imgStampDefaultDel').attr('disabled', true);                            // 버튼
      //// 화살표 이미지 파일선택/삭제 버튼
      //$('#imgRouletteArrow').attr('disabled', true);                              // 버튼
      //$('#imgRouletteArrowDel').attr('disabled', true);                           // 버튼
      //// 배경 이미지 파일선택/삭제 버튼
      //$('#imgRouletteBg').attr('disabled', true);                                 // 버튼
      //$('#imgRouletteBgDel').attr('disabled', true);                              // 버튼
      //// 룰렛 이미지 파일선택/삭제 버튼
      //$('#imgRoulette').attr('disabled', true);                                   // 버튼
      //$('#imgRouletteDel').attr('disabled', true);                                // 버튼

      // 룰렛 설정 개수
      $('#rouletteCnt').data('kendoDropDownList').enable(false);                    // kendoDropDownList
      // 이벤트 제한고객 룰렛설정 개수
      $('#exceptionUserRouletteCnt').data('kendoDropDownList').enable(false);       // kendoDropDownList
      //// 당첨자 공지사항
      //$("input[name=winnerNotice]").attr('disabled', true);                       // input text
      // 당첨자공지사항(url입력)
      //$('#winnerNotice').attr('disabled', 'disabled');                              // textarea
      // 당첨자 안내
      $('#winnerInfor').attr('disabled', 'disabled');                               // textarea
      // 예상참여자수
      $('input[name=expectJoinUserCntRoulette]').attr('disabled', true);
      // 자동종료여부
      if(eventInfo.timeOverCloseYn == 'N'){
          $("input[name=timeOverCloseYn]:checkbox").prop("checked", false);
      }else{
          $("input[name=timeOverCloseYn]:checkbox").prop("checked", true);
      }

      // ----------------------------------------------------------------------
      // 당첨혜택 스탬프리스트
      // ----------------------------------------------------------------------
      if (eventRouletteItemList != undefined && eventRouletteItemList != null && eventRouletteItemList.length) {

        for (var i = 0; i < eventRouletteItemList.length; i++) {

          let index = i+1;

          // 스탬프 당첨자혜택
          $('input[name=eventBenefitTp'+index+']').attr('disabled', true);
          // 쿠폰조회 버튼
          $('#btnCouponSearch'+index).prop('disabled', true);                       // button
          //적립금 콤보
          $('#pmPointId'+index).data('kendoDropDownList').enable(false);            // kendoDropDownList
          // 적립금 추가 버튼
          $('#btnPointAdd'+index).prop('disabled', true);
          // 경품명 입력
          $('input[name=benefitNmGiftRoulette'+index+']').attr('disabled', true);   // input text
          // 응모명 입력
          $('input[name=benefitNmEnterRoulette'+index+']').attr('disabled', true);  // input text
          // 룰렛 당첨 활률
          $('input[name=awardRt'+index+']').attr('disabled', true);                 // input text
          // 룰렛 당첨 인원
          $('input[name=awardMaxCnt'+index+']').attr('disabled', true);             // input text
        } // End of for (var i = 0; i < eventStampDetlList.length; i++)

        // 쿠폰그리드 지급수량 (전체)
        $('input[name=couponCount]').attr('disabled', true);                        // input text
        // 쿠폰그리드 삭제 버튼
        $('.btnCouponDel').prop('disabled', true);                                  // button
        // 적립금 추가 버튼
        $('.btnPointAdd').prop('disabled', true);                                   // button
        // 적립금그리드 삭제 버튼
        $('.btnPointDel').prop('disabled', true);                                   // button

      }
    }

    return eventInfo;

  } // End of function fnSetSearchDataStmap(data)

  // ==========================================================================
  // # 조회데이터Set - 체험단이벤트
  // ==========================================================================
  function fnSetSearchDataExperience(data) {
    //console.log('# fnSetSearchDataExperience Start');

    var eventInfo           = data.eventInfo;
    var eventExperienceInfo = data.eventExperienceInfo;

    // ------------------------------------------------------------------------
    // 이벤트기본정보/이벤트상세정보
    // ------------------------------------------------------------------------
    if (eventExperienceInfo != undefined && eventExperienceInfo != null && eventExperienceInfo != '') {

      // ----------------------------------------------------------------------
      // 상세정보-노출
      // ----------------------------------------------------------------------
      // TODO 화면 로딩 시 이벤트유형이 있으면 앞부분에서 처리되므로 생략 검토
      //fnDisplayScreen(eventInfo.eventTp);

      // ----------------------------------------------------------------------
      // 당첨자선정기간
      // ----------------------------------------------------------------------
      // 당첨자선정기간-시작
      var selectStartDtStr = '';
      var selectStartDe    = '';
      var selectStartHour;
      var selectStartMin;
      //selectStartDtStr  = (((eventInfo.selectStartDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
      selectStartDtStr  = ((eventExperienceInfo.selectStartDt).replace(/:/gi, '')).replace(/ /gi, '');

      if (selectStartDtStr != null && selectStartDtStr != '') {
        if (selectStartDtStr.length >= 10) {
          selectStartDe = selectStartDtStr.substring(0, 10);
          $('#selectStartDe').val(selectStartDe);
          // ------------------------------------------------------------------
          // 날짜 선후비교를 위한 kendoDatePicker 값 Set
          // ------------------------------------------------------------------
          const $datePicker = $('#selectStartDe').data('kendoDatePicker');
          if( $datePicker ) {
            $datePicker.value(selectStartDe);
          }
        }
        if (selectStartDtStr.length >= 12) {
          selectStartHour = Number(selectStartDtStr.substring(10, 12));
          $('#selectStartHour').data('kendoDropDownList').select(selectStartHour);
        }
        if (selectStartDtStr.length >= 14) {
          selectStartMin = Number(selectStartDtStr.substring(12, 14));
          $('#selectStartMin').data('kendoDropDownList').select(selectStartMin);
        }
      }
      // 당첨자선정기간-종료
      var selectEndDtStr = '';
      var selectEndDe    = '';
      var selectEndHour;
      var selectEndMin;
      //selectEndDtStr  = (((eventInfo.endDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
      selectEndDtStr  = ((eventExperienceInfo.selectEndDt).replace(/:/gi, '')).replace(/ /gi, '');
      if (selectEndDtStr != null && selectEndDtStr != '') {
        if (selectEndDtStr.length >= 10) {
          selectEndDe = selectEndDtStr.substring(0, 10);
          $('#selectEndDe').val(selectEndDe);
          // ------------------------------------------------------------------
          // 날짜 선후비교를 위한 kendoDatePicker 값 Set
          // ------------------------------------------------------------------
          const $datePicker = $('#selectEndDe').data('kendoDatePicker');
          if( $datePicker ) {
            $datePicker.value(selectEndDe);
          }
        }
        if (selectEndDtStr.length >= 12) {
          selectEndHour = Number(selectEndDtStr.substring(10, 12));
          $('#selectEndHour').data('kendoDropDownList').select(selectEndHour);
        }
        if (selectEndDtStr.length >= 14) {
          selectEndMin = Number(selectEndDtStr.substring(12, 14));
          $('#selectEndMin').data('kendoDropDownList').select(selectEndMin);
        }
      }
      // ----------------------------------------------------------------------
      // 후기작성기간
      // ----------------------------------------------------------------------
      // 후기작성기간-시작
      var feedbackStartDtStr = '';
      var feedbackStartDe    = '';
      var feedbackStartHour;
      var feedbackStartMin;
      //feedbackStartDtStr  = (((eventInfo.feedbackStartDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
      feedbackStartDtStr  = ((eventExperienceInfo.feedbackStartDt).replace(/:/gi, '')).replace(/ /gi, '');
      if (feedbackStartDtStr != null && feedbackStartDtStr != '') {
        if (feedbackStartDtStr.length >= 10) {
          feedbackStartDe = feedbackStartDtStr.substring(0, 10);
          $('#feedbackStartDe').val(feedbackStartDe);
          // ------------------------------------------------------------------
          // 날짜 선후비교를 위한 kendoDatePicker 값 Set
          // ------------------------------------------------------------------
          const $datePicker = $('#feedbackStartDe').data('kendoDatePicker');
          if( $datePicker ) {
            $datePicker.value(feedbackStartDe);
          }
        }
        if (feedbackStartDtStr.length >= 12) {
          feedbackStartHour = Number(feedbackStartDtStr.substring(10, 12));
          $('#feedbackStartHour').data('kendoDropDownList').select(feedbackStartHour);
        }
        if (feedbackStartDtStr.length >= 14) {
          feedbackStartMin = Number(feedbackStartDtStr.substring(12, 14));
          $('#feedbackStartMin').data('kendoDropDownList').select(feedbackStartMin);
        }
      }
      // 후기작성기간-종료
      var feedbackEndDtStr = '';
      var feedbackEndDe    = '';
      var feedbackEndHour;
      var feedbackEndMin;
      //feedbackEndDtStr  = (((eventInfo.endDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
      feedbackEndDtStr  = ((eventExperienceInfo.feedbackEndDt).replace(/:/gi, '')).replace(/ /gi, '');
      if (feedbackEndDtStr != null && feedbackEndDtStr != '') {
        if (feedbackEndDtStr.length >= 10) {
          feedbackEndDe = feedbackEndDtStr.substring(0, 10);
          $('#feedbackEndDe').val(feedbackEndDe);
          // ------------------------------------------------------------------
          // 날짜 선후비교를 위한 kendoDatePicker 값 Set
          // ------------------------------------------------------------------
          const $datePicker = $('#feedbackEndDe').data('kendoDatePicker');
          if( $datePicker ) {
            $datePicker.value(feedbackEndDe);
          }
        }
        if (feedbackEndDtStr.length >= 12) {
          feedbackEndHour = Number(feedbackEndDtStr.substring(10, 12));
          $('#feedbackEndHour').data('kendoDropDownList').select(feedbackEndHour);
        }
        if (feedbackEndDtStr.length >= 14) {
          feedbackEndMin = Number(feedbackEndDtStr.substring(12, 14));
          $('#feedbackEndMin').data('kendoDropDownList').select(feedbackEndMin);
        }
      }

      // ----------------------------------------------------------------------
      // 체험단이벤트 - 기간종료후자동종료
      // ----------------------------------------------------------------------
      eventInfo.experienceTimeOverCloseYn = eventInfo.timeOverCloseYn;

      // ----------------------------------------------------------------------
      // 체험상픔
      // ----------------------------------------------------------------------
      // 그리드 초기화 : 수정 후 재조회 시 중복방지
      fnInitExperienceGrid();

      var targetGrid    = $('#experienceGrid').data('kendoGrid');
      var targetGridDs  = targetGrid.dataSource;
      var targetGridArr = targetGridDs.data();

      var gridObj = new Object();
      gridObj.goodsTpNm             = eventExperienceInfo.goodsTpNm;          // 상품유형명
      gridObj.ilGoodsId             = eventExperienceInfo.ilGoodsId;          // 상품PK
      gridObj.goodsNm               = eventExperienceInfo.goodsNm;            // 상품명
      gridObj.goodsImagePath        = eventExperienceInfo.goodsImagePath;     // 상품이미지경로
      gridObj.warehouseNm           = eventExperienceInfo.warehouseNm;        // 출고처명
      gridObj.standardPrice         = eventExperienceInfo.standardPrice;      // 원가
      gridObj.recommendedPrice      = eventExperienceInfo.recommendedPrice;   // 정상가
      // 1건만 가능하므로 0 Set
      targetGridDs.insert(0, gridObj);

      // ----------------------------------------------------------------------
      // 당첨자수 (선착순인경우만 노출)
      // ----------------------------------------------------------------------
      if (eventInfo.eventDrawTp == 'EVENT_DRAW_TP.FIRST_COME') {
        // 선착순인 경우
        $('#firstWinnerTpDiv').show();
        eventInfo.firstComeCnt = eventExperienceInfo.firstComeCnt;
        $('#immediatleyWinDiv').hide();
      }
      else {
        $('#firstWinnerTpDiv').hide();
        eventInfo.firstComeCnt = '';
        if(eventInfo.eventDrawTp == 'EVENT_DRAW_TP.AUTO'){
            $('#immediatleyWinDiv').show();
        }
      }

      // ----------------------------------------------------------------------
      // 참여버튼 BG Color code
      // ----------------------------------------------------------------------
      eventInfo.btnColorCd = eventExperienceInfo.btnColorCd;

      // ----------------------------------------------------------------------
      // 댓글분류설정
      // ----------------------------------------------------------------------
      // 댓글분류설정 TR영역 노출제어
      fnEventCommentCodeYnTr(eventInfo.eventTp, eventInfo.eventDrawTp);

      if (eventInfo.eventDrawTp != 'EVENT_DRAW_TP.FIRST_COME') {
        // 당첨자설정 : 선착순이 아닌 경우

        // 댓글분류 초기화 : 수정 후 재조회 시 중복방지
        gbCommentCodeMaps.clear();
        $('.js__remove__commentTpList').each(function(){
          $(this).remove();
        });

        eventInfo.commentCodeYn = eventExperienceInfo.commentCodeYn;;

        if (eventInfo.commentCodeYn == 'Y') {
          // 댓글분류설정여부 : Y
          $('#commentCodeValueDiv').show();
          $('#commentCodeValueListDiv').show();

          var eventCommentCodeList = data.eventCommentCodeList;

          if (eventCommentCodeList != undefined && eventCommentCodeList != null && eventCommentCodeList.length > 0) {
            for (var i = 0; i < eventCommentCodeList.length; i++) {
              fnEventAddCommentCode(eventCommentCodeList[i].commentValue);
            }
          }
        }
      }
      else {
        // 당첨자설정 : 선착순인 경우
        // 댓글분류 초기화 : 수정 후 재조회 시 중복방지
        gbCommentCodeMaps.clear();
        $('.js__remove__commentTpList').each(function(){
          $(this).remove();
        });

      }

      // ----------------------------------------------------------------------
      // 당첨자혜택 (쿠폰 콤보 하단의 쿠폰정보)
      // ----------------------------------------------------------------------
      eventInfo.pmCouponId = eventExperienceInfo.pmCouponId;
      if (fnIsEmpty(eventExperienceInfo.bosCouponNm) == false) {
        eventInfo.pmCouponNm = eventExperienceInfo.bosCouponNm + '(' + eventExperienceInfo.displayCouponNm + ')';
      }
      else {
        eventInfo.pmCouponNm = '';
      }
      $('#pmCouponId').val(eventInfo.pmCouponId);
      $('#pmCouponNm').text(eventInfo.pmCouponNm);

      // ----------------------------------------------------------------------
      // 당첨자 공지사항(url 입력)
      // ----------------------------------------------------------------------
      //eventInfo.winnerNotice = eventExperienceInfo.winnerNotice;

      // ----------------------------------------------------------------------
      // 당첨자 안내
      // ----------------------------------------------------------------------

    }

    //console.log('# statusSe :: ', eventInfo.statusSe);
    // ------------------------------------------------------------------------
    // 상세정보 비활성처리 - 진행예정이 아니면 비활성
    // ------------------------------------------------------------------------
    if (eventInfo.statusSe == 'BEF') {
      // 진행예정 : 비활성 없음
    }
    else if (eventInfo.statusSe == 'ING' || eventInfo.statusSe == 'END') {
      // 진행중/종료 : 부분 비활성

      // 진행중 인 경우 비활성
      if (eventInfo.statusSe == 'ING') {
        // --------------------------------------------------------------------
        // 당첨자선정기간
        // --------------------------------------------------------------------
        $('#selectStartDe').data('kendoDatePicker').enable(false);        // kendoDatePicker
        $('#selectStartHour').data('kendoDropDownList').enable(false);    // kendoDropDownList
        $('#selectStartMin').data('kendoDropDownList').enable(false);     // kendoDropDownList
        $('#selectEndDe').data('kendoDatePicker').enable(false);          // kendoDatePicker
        $('#selectEndHour').data('kendoDropDownList').enable(false);      // kendoDropDownList
        $('#selectEndMin').data('kendoDropDownList').enable(false);       // kendoDropDownList
        // --------------------------------------------------------------------
        // 후기작성기간
        // --------------------------------------------------------------------
        $('#feedbackStartDe').data('kendoDatePicker').enable(false);      // kendoDatePicker
        $('#feedbackStartHour').data('kendoDropDownList').enable(false);  // kendoDropDownList
        $('#feedbackStartMin').data('kendoDropDownList').enable(false);   // kendoDropDownList
        $('#feedbackEndDe').data('kendoDatePicker').enable(false);        // kendoDatePicker
        $('#feedbackEndHour').data('kendoDropDownList').enable(false);    // kendoDropDownList
        $('#feedbackEndMin').data('kendoDropDownList').enable(false);     // kendoDropDownList
      }

      // 상품등록 버튼
      $('#btnExperienceGoodsAdd').prop('disabled', true);                 // 버튼
      // 상품삭제
      $('button[name=btnExperienceGoodsDel]').prop('disabled', true);     // 버튼

      // 임직원참여여부
      $('input[name=employeeJoinYn]').attr('disabled', true);
      // 참여횟수 콤보
      $('#eventJoinTp').data('kendoDropDownList').enable(false);          // kendoDropDownList
      // 당첨자 설정
      $('input[name=eventDrawTp]').attr('disabled', true);
      // 당첨인원수
      $('input[name=firstComeCnt]').attr('disabled', true);
      // 참여버튼 BG color code
      $('input[name=btnColorCd]').attr('disabled', true);                 // input text
      // 댓글분류설정
      $('input[name=commentCodeYn]').attr('disabled', true);
      // 분류값 입력
      $('input[name=inputValue]').attr('disabled', true);
      // 분류추가 버튼
      $('#btnAddCommentCode').prop('disabled', true);                     // 버튼
      //$('#btnAddCommentCode').prop('disabled', 'disabled');
      // 분류삭제 버튼
      $('.js__remove__commentTpBtn').prop('disabled', true);
      // 당첨자혜택(쿠폰 콤보)
      $('#pmCouponCombo').data('kendoDropDownList').enable(false);        // kendoDropDownList
      // 쿠폰추가 버튼
      $('#btnCouponAdd').prop('disabled', true);                          // 버튼
      //// 당첨자 공지사항(url 입력)
      //$('input[name=winnerNotice]').attr('disabled', true);             // input text
      // 당첨자공지사항(url입력)
      //$('#winnerNotice').attr('disabled', 'disabled');                    // textarea
      // 당첨자안내
      $('#winnerInfor').attr('disabled', 'disabled');                     // textarea
      // 자동종료여부
      if(eventInfo.experienceTimeOverCloseYn == 'N'){
          $("input[name=experienceTimeOverCloseYn]:checkbox").prop("checked", false);
      }else{
          $("input[name=experienceTimeOverCloseYn]:checkbox").prop("checked", true);
      }
    }
    return eventInfo;

  } // End of function fnSetSearchDataExperience(data)


  // ==========================================================================
  // # 당첨자 혜택 영역 Set
  // ==========================================================================
  function fnTemplateSetBenefit (eventBenefitTp, eventInfo, eventDetlInfo, eventCouponList, dynamicYn, index) {
    //console.log('# fnTemplateSetBenefit Start [', eventBenefitTp, ']');
    //console.log('# =========================================================');
    //console.log('# fnTemplateSetBenefit eventBenefitTp  :: ', eventBenefitTp);
    //console.log('# fnTemplateSetBenefit dynamicYn       :: ', dynamicYn);
    //console.log('# fnTemplateSetBenefit index           :: ', index);
    //console.log('# fnTemplateSetBenefit eventInfo       :: ', JSON.stringify(eventInfo));
    //console.log('# fnTemplateSetBenefit eventDetlInfo   :: ', JSON.stringify(eventDetlInfo));
    //console.log('# =========================================================');
    //console.log('# eventDetlInfo', JSON.stringify(eventDetlInfo));
    //var eventCouponList = detlInfo.eventCouponList;

    var targetCouponGridId    = '';
    var targetPointGridId     = '';
    var targetBenefitNmGift   = '';
    var targetBenefitNmEnter  = '';
    var targetEventBenefitTp  = '';
    var targetStampUrl        = '';
    var gbIsPopupHiddenYn     = false;

    // ------------------------------------------------------------------------
    // 그리드 초기화
    // ------------------------------------------------------------------------
    if (dynamicYn == 'Y') {
      // 동적생성
      // ----------------------------------------------------------------------
      // 항목ID
      // ----------------------------------------------------------------------
      targetCouponGridId    = 'benefitCouponDynamicGrid'+index;
      targetPointGridId     = 'benefitPointDynamicGrid'+index;
      if (gbEventTp == 'EVENT_TP.ATTEND' || gbEventTp == 'EVENT_TP.MISSION' || gbEventTp == 'EVENT_TP.PURCHASE') {
        targetBenefitNmGift   = 'benefitNmGiftStamp'+index;
        targetBenefitNmEnter  = 'benefitNmEnterStamp'+index;
      }
      else if (gbEventTp == 'EVENT_TP.ROULETTE') {
        targetBenefitNmGift   = 'benefitNmGiftRoulette'+index;
        targetBenefitNmEnter  = 'benefitNmEnterRoulette'+index;
      }
      else {
        targetBenefitNmGift   = 'benefitNmGift'+index;
        targetBenefitNmEnter  = 'benefitNmEnter'+index;
      }
      targetEventBenefitTp  = 'eventBenefitTp'+index;
      targetStampUrl        = 'stampUrl'+index;
    }
    else {
      // 단건
      // ----------------------------------------------------------------------
      targetCouponGridId    = 'benefitCouponGrid';
      targetPointGridId     = 'benefitPointGrid';
      targetBenefitNmGift   = 'benefitNmGift';
      targetBenefitNmEnter  = 'benefitNmEnter';
      targetEventBenefitTp  = 'eventBenefitTp';
      targetStampUrl        = 'stampUrl';
      // ----------------------------------------------------------------------
      // 그리드 초기화 : 수정 후 재조회 시 중복방지
      // ----------------------------------------------------------------------
      // 쿠폰
      fnInitBenefitCouponGrid();
      // 적립금
      fnInitBenefitPointGrid();
    }

    // ------------------------------------------------------------------------
    // 당첨혜택유형 라디오 Set
    // ------------------------------------------------------------------------
    $('input:radio[name="'+targetEventBenefitTp+'"]:input[value="'+eventBenefitTp+'"]').prop("checked", true);

    if (eventBenefitTp == 'EVENT_BENEFIT_TP.COUPON') {
      // ----------------------------------------------------------------------
      // 당첨자혜택-쿠폰
      // ----------------------------------------------------------------------
      if (eventCouponList != undefined && eventCouponList != null && eventCouponList.length > 0) {
        if(eventInfo.eventTp != 'EVENT_TP.NORMAL'){
            gbIsPopupHiddenYn = true;
        }

        // 그리드 초기화
        fnInitBenefitCouponDynamicGrid(targetCouponGridId, index, gbEvEventId, gbIsPopupHiddenYn);

        var targetGrid    = $('#'+targetCouponGridId).data('kendoGrid');
        var targetGridDs  = targetGrid.dataSource;
        var targetGridArr = targetGridDs.data();

        for (var i = 0; i < eventCouponList.length; i++) {
          var gridObj = new Object();
          gridObj.idx                   = i;                                          // idx
          gridObj.pmCouponId            = eventCouponList[i].pmCouponId;              // 전시쿠폰명
          gridObj.evEventCouponId       = eventCouponList[i].evEventCouponId;              // 전시쿠폰명
          gridObj.displayCouponNm       = eventCouponList[i].displayCouponNm;         // 전시쿠폰명
          gridObj.bosCouponNm           = eventCouponList[i].bosCouponName;           // BOS쿠폰명
          gridObj.discountTpNm          = eventCouponList[i].discountTpNm;            // 할인방식명
          gridObj.discountDetl          = eventCouponList[i].discountValue;           // 할인상세
          gridObj.validityConts         = eventCouponList[i].validityConts;           // 유효기간
          gridObj.couponCnt             = eventCouponList[i].couponCnt+'';            // 지급수량(기본값:1)
          gridObj.couponTotalCnt        = eventCouponList[i].couponTotalCnt+'';       // 총 당첨수량
          gridObj.urErpOrganizationNm   = eventCouponList[i].urErpOrganizationNm;     // 분담조직
          gridObj.issueDt               = eventCouponList[i].issueDt;                 // 발급기간
          gridObj.validityTp            = eventCouponList[i].validityTp;              // 유효기간 설정타입
          targetGridDs.insert(i, gridObj);
          //targetGridDs.at(i).set("couponCnt", eventCouponList[i].couponCnt);
        }
        //for (var i = 0; i < eventCouponList.length; i++) {
        //  $('#benefitCouponGrid input[name="couponCount"]')[i].value = eventCouponList[i].couponCount+'';
        //}
      }
      // 쿠폰 이외 초기화
      $('#'+targetBenefitNmGift).val('');
      $('#'+targetBenefitNmEnter).val('');
    }
    else if (eventBenefitTp == 'EVENT_BENEFIT_TP.POINT') {
      // ----------------------------------------------------------------------
      // 당첨자혜택-적립금
      // ----------------------------------------------------------------------
      // 그리드 초기화
      //fnInitBenefitPointDynamicGrid(targetPointGridId, index, gbEvEventId);

      var targetGrid    = $('#'+targetPointGridId).data('kendoGrid');
      var targetGridDs  = targetGrid.dataSource;
      var targetGridArr = targetGridDs.data();

      var gridObj = new Object();
      if (gbEventTp == 'EVENT_TP.ROULETTE') {
        gridObj.pmPointId             = eventDetlInfo.benefitId;          // 적립금PK
      }
      else {
        gridObj.pmPointId             = eventDetlInfo.pmPointId;          // 적립금PK
      }
      if(eventDetlInfo.validityTp == "VALIDITY_TYPE.PERIOD") {
        gridObj.displayPointNm        = eventDetlInfo.pointNm;
      } else {
        gridObj.displayPointNm        = eventDetlInfo.pointNm +"("+eventDetlInfo.issueDate+")";
      }
      // gridObj.displayPointNm        = eventDetlInfo.pointNm;              // 적립금명
      gridObj.discountTpNm          = eventDetlInfo.pointTpNm;            // 적립구분명
      gridObj.discountDetl          = eventDetlInfo.issueVal;             // 적립금
      gridObj.erpOrganizationCode   = eventDetlInfo.urErpOrganizationCd;  // 분담조직코드
      gridObj.urErpOrganizationNm   = eventDetlInfo.urErpOrganizationNm;  // 분담조직명
      gridObj.totalWinCnt           = eventDetlInfo.totalWinCnt;          // 총 당첨 수량
      gridObj.issueDt               = eventDetlInfo.issueDt;              // 발급기간
      gridObj.validityTp            = eventDetlInfo.validityTp;           // 유효기간 설정타입
      // 1건만 가능하므로 0 Set
      targetGridDs.insert(0, gridObj);

      targetGrid    = $('#'+targetPointGridId).data('kendoGrid');
      targetGridDs  = targetGrid.dataSource;
      targetGridArr = targetGridDs.data();

      // 적립금 이외 초기화
      $('#'+targetBenefitNmGift).val('');
      $('#'+targetBenefitNmEnter).val('');
    }
    else if (eventBenefitTp == 'EVENT_BENEFIT_TP.GIFT') {
      // ----------------------------------------------------------------------
      // 당첨자혜택-경품
      // ----------------------------------------------------------------------
      if (dynamicYn == 'Y') {
        // 동적
        // 응모
        $('#'+targetBenefitNmGift).val(eventDetlInfo.benefitNm);
        // 경품
        $('#'+targetBenefitNmEnter).val('');
      }
      else {
        // 단건
        eventInfo.benefitNmGift       = eventDetlInfo.benefitNm;
        eventInfo.totalWinCntGift     = eventDetlInfo.totalWinCnt;
      }
    }
    else if (eventBenefitTp == 'EVENT_BENEFIT_TP.ENTER') {
      // ----------------------------------------------------------------------
      // 당첨자혜택-응모
      // ----------------------------------------------------------------------
      if (dynamicYn == 'Y') {
        // 동적
        // 응모
        $('#'+targetBenefitNmGift).val('');
        // 경품
        $('#'+targetBenefitNmEnter).val(eventDetlInfo.benefitNm);
      }
      else {
        // 단건
        eventInfo.benefitNmEnter       = eventDetlInfo.benefitNm;
      }
    }

    // 스탬프 노출 URL
    if (gbEventTp == 'EVENT_TP.MISSION') {
      $('#'+targetStampUrl).val(eventDetlInfo.stampUrl);
    }
    else {
      $('#'+targetStampUrl).val('');
    }

    // 혜택유형별 노출 처리
    fnChangeEventBenefitTp(targetEventBenefitTp, eventBenefitTp);

    return eventInfo;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 상세조회 DataSet End
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

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

  // ==========================================================================
  // 그룹 데이터 Set - 일반기획전/증정행사 용
  // ==========================================================================
  function fnSetGroupData() {

    var groupList         = new Array();  // 그룹리스트
    var groupData;                        // 그룹정보
    var goodsList;                        // 그룹별 상품리스트
    var groupGridArr;                     // 그룹별 상품그리드
    var orderNo           = 1;

    // ------------------------------------------------------------------------
    // 그룹별 Loop
    // ------------------------------------------------------------------------
    $(".groupTable").each(function(){

      // 그룹별 초기화
      groupData = new Object();
      goodsList = new Array();

      // 그룹정보
      var self = $(this);
      var idx = self.data("groupId");

      // ----------------------------------------------------------------------
      // 필수체크
      // ----------------------------------------------------------------------
      // TODO
      // $('#inputForm input[name=eventTp]'        ).prop("required", true);
      // 그룹 row별 그룹사용여부
      // 그룹 row별 상품그룹명 배경 설정
      // 그룹 row별 상품그룹명 배경 설정 = 배경컬러 -> 컬러코드 필수
      // 그룹 row별 전시 상품 수
      // 그룹 row별 그룹 전시 순서
      // 그룹 row별 그리드 1개 이상 존재

      // ----------------------------------------------------------------------
      // 그룹별 기본정보
      // ----------------------------------------------------------------------
      groupData.groupIdx      = idx;
      groupData.groupNm       = $('#groupNm'+idx).val();
      groupData.textColor     = $('#textColor'+idx).val();
      groupData.groupUseYn    = $('input[name=groupUseYn'+idx+']:checked').val();
      groupData.eventImgTp  = $('input[name=eventImgTp'+idx+']:checked').val();
      groupData.bgCd          = $('#bgCd'+idx).val();
      groupData.groupDesc     = $('#groupDesc'+idx).val();
      groupData.dispCnt       = $('#dispCnt'+idx).val();
      groupData.groupSort     = $('#groupSort'+idx).val();

      // ----------------------------------------------------------------------
      // 그룹별 그룹상품정보
      // ----------------------------------------------------------------------
      groupGridArr = $('#groupGoodsGrid'+idx).data('kendoGrid').dataSource.data();

      if (groupGridArr != undefined && groupGridArr != null && groupGridArr.length > 0) {

        //var goodsData;

        for (var i = 0; i < groupGridArr.length; i++) {

          var goodsData = new Object();
          goodsData.ilGoodsId = groupGridArr[i].ilGoodsId;
          goodsData.goodsSort = groupGridArr[i].goodsSort;

          goodsList.push(goodsData);

        } // End of for (var i = 0; i < goodsGridArr.length; i++)
        groupData.groupGoodsList            = goodsList;
        groupData.groupGoodsListJsonString  = JSON.stringify(goodsList);
        //groupData.groupGoodsListJsonString = goodsList;

      } // End of if (goodsGridArr != undefined && goodsGridArr != null && goodsGridArr.length > 0)

      // 상품그룹 add
      groupList.push(groupData);

      orderNo++;
    }); // End of $(".groupTable").each(function(){

    // ------------------------------------------------------------------------
    // 반환 : 기획전데이터 + 골라담기
    // ------------------------------------------------------------------------
    return groupList;
  }


