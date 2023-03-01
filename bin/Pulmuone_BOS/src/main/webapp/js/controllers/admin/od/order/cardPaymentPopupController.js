/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문 상세 > 신용카드 비인증 결제
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.18		김승우          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var pageParam = fnGetPageParam();

$(document).ready(function() {
  // importScript("/js/service/od/claim/claimComm.js");
  importScript("/js/service/od/odComm.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "cardPaymentPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		
		fnTranslate();	// 다국어 변환
    fnInitButton();


    // 입력 값 체크
    bindValidation();

    // 이벤트 바인딩
    bindEvents();
    // window.console.clear();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){};

  //---------------Initialize Option Box End ------------------------------------------------
  
  //--------------- Events Start ------------------------------------------------

  // 옵션 초기화
  function bindEvents(){
    $(".cardNum input").on("keyup",  onKeyupCardNum);
    $(".cardEx input").on("keyup",  onKeyupCardEx);
    $(".extraInfo select").on("change", onChangeExtraInfo);
    $(".cardPw input").on("keyup", onKeyupCardPw);
  };

  // 카드번호 입력 이벤트
  function onKeyupCardNum(e) {
    var MAX_LENGTH = 4;
    var self = $(this);
    var _value = self.val();
    var LENGTH = _value.trim().length;

    if( LENGTH > MAX_LENGTH ) {
      self.val(_value.slice(0, MAX_LENGTH));
    } else if( LENGTH === MAX_LENGTH ) {
      var $nextEl = self.parent().next();

      if( $nextEl.length ) {
        $nextEl.children().focus();
      } else {
        $(".cardEx input").eq(0).focus();
      }
    }
  }

  // 카드유효기간 입력 이벤트
  function onKeyupCardEx(e) {
    var MAX_LENGTH = 2;
    var self = $(this);
    var _value = self.val();
    var LENGTH = _value.trim().length;

    if( LENGTH > MAX_LENGTH ) {
      self.val(_value.slice(0, MAX_LENGTH));
    } else if( LENGTH === MAX_LENGTH ) {
        $("#exMonth").focus();
    }
  }

  function onKeyupCardPw(e) {
    var MAX_LENGTH = 2;
    var self = $(this);
    var _value = self.val();
    var LENGTH = _value.trim().length;

    if( LENGTH > MAX_LENGTH ) {
      self.val(_value.slice(0, MAX_LENGTH));
    }
  }

  // FIXME
  function onChangeExtraInfo(e) {
    var self = $(this);
    var _value = self.val();
    var $notice = $(".form__notice--birth");

    if( !_value ) {
      $notice.addClass("hidden");
      return;
    }

    if ( _value === "birth" ) {
      $notice.removeClass("hidden");
    } else {
      $notice.addClass("hidden");
    }

    $("#extraInfo").focus();
  }
  

  //--------------- Events End ------------------------------------------------


	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

    function fnSearch() {}
    
    function fnClear() {}

    function fnSave() {
      var data = $("#inputForm").formSerialize(true);

      console.log(data);
    }


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
  $scope.fnClear =function(){	fnClear(); };
  
  $scope.fnSave =function(){	fnSave(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

    function bindValidation() {
      $(".cardNum input, .cardEx input, .cardPw input, .extraInfo input").each(function() {
        var self = $(this);
        // 숫자만
        fnInputValidationForNumber(self.attr("id"));
      })
    }

  // fnInputValidationForAlphabetNumberLineBreakComma("code");
  
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
