/**-----------------------------------------------------------------------------
 * description 		 : 외부 광고 코드 등록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.07	    이원호          최초생성
 * @
 */
'use strict';

var gPopupType;
var gPmAdExternalCd;
var gExistCd;

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	gPopupType = parent.POP_PARAM["parameter"].popupType;
	gPmAdExternalCd = parent.POP_PARAM["parameter"].pmAdExternalCd;

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'pmAdvertisingPopup',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnPopSave, #fnClose').kendoButton();
	}

	// 초기화
	function fnClear(){
		$('#inputForm').formClear(true);
	}

    // 조회
    function fnSearch(){
        if(gPopupType == "ADD"){
            return;
        }

        fnAjax({
            url     : "/admin/promotion/advertising/getAdvertisingExternal",
            params  : {pmAdExternalCd : gPmAdExternalCd},
            success :
                function( data ){
                    if(data.advertisingExternalList.length == 0) return;
                    let returnData = data.advertisingExternalList[0];
                    $('#pmAdExternalCdDivAdd').hide();
                    $('#pmAdExternalCdDivModify').show();
                    $('#sourceDivAdd').hide();
                    $('#sourceDivModify').show();
                    $('#mediumDivAdd').hide();
                    $('#mediumDivModify').show();
                    $('#campaignDivAdd').hide();
                    $('#campaignDivModify').show();
                    $('#urlTrModify').show();

                    $('#advertisingName').attr('disabled', true);
                    $('#pmAdExternalCd').attr('disabled', true);
                    $('#newContent').attr('disabled', true);
                    $('#newTerm').attr('disabled', true);
                    $('#advertisingUrl').attr('disabled', true);

                    $('#advertisingName').val(returnData.advertisingName);
                    $('#viewPmAdExternalCd').val(returnData.pmAdExternalCd);
                    $('#viewSource').val(returnData.source);
                    $('#viewMedium').val(returnData.medium);
                    $('#viewCampaign').val(returnData.campaign);
                    $('#newContent').val(returnData.content);
                    $('#newTerm').val(returnData.term);
                    $('#useYn').val(returnData.useYn);
                    $('#redirectUrl').val(returnData.redirectUrl);
                    $('#advertisingUrl').val(returnData.advertisingUrl);
                },
                isAction : 'batch'
        });
    }

	// 저장
	function fnSave(){
        if(gPopupType == "ADD"){

            var url  = '/admin/promotion/advertising/addAdvertisingExternal';
            var cbId = 'insert';
            var params = $('#inputForm').formSerialize(true);

            if(params.advertisingName === ""){
                fnKendoMessage({message : '광고명은 필수 입력 값입니다. 광고명을 입력해주세요.'});
                return;
            }

            if(gExistCd == false){
                fnKendoMessage({message:'광고 ID 중복체크를 해주세요.'});
                return;
            }

            if(params.source === "" && params.newSource === ""){
                fnKendoMessage({message : '매체(source)는 필수 입력 값입니다. 매체(source)를 입력해주세요.'});
                return;
            }
            if(params.medium === "" && params.newMedium === ""){
                fnKendoMessage({message : '구좌(medium)는 필수 입력 값입니다. 구좌(medium)를 입력해주세요.'});
                return;
            }
            if(params.campaign === "" && params.newCampaign === ""){
                fnKendoMessage({message : '캠페인(campaign)는 필수 입력 값입니다. 캠페인(campaign)를 입력해주세요.'});
                return;
            }
            if(params.redirectUrl === ""){
                fnKendoMessage({message : 'Redirect URL 은 필수 입력 값입니다. Redirect URL 을 입력해주세요.'});
                return;
            }
            // 필수값 아님으로 변경(HGRM-8783 최용호)
            // if(params.newContent === ""){
            //     fnKendoMessage({message : '{세분류}는 필수 입력 값입니다. {세분류}를 입력해주세요.'});
            //     return;
            // }

            if( params.rtnValid ){
                fnAjax({
                    url     : url,
                    params  : params,
                    success :
                        function( data ){
                            fnBizCallback(cbId, data);
                        },
                        isAction : 'batch'
                });
            }
        } else {
            var url  = '/admin/promotion/advertising/putAdvertisingExternal';
            var cbId = 'insert';
            var params = { pmAdExternalCd : gPmAdExternalCd,
                useYn : $('#useYn').getRadioVal(),
                redirectUrl : $('#redirectUrl').val()
            };

            fnAjax({
                url     : url,
                params  : params,
                success :
                    function( data ){
                        fnBizCallback(cbId, data);
                    },
                    isAction : 'batch'
            });
        }
	}

    function fnClose(){
		parent.POP_PARAM = 'SAVE';
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

    function fnUrlCopy(){
        // 복사할 텍스트를 변수에 할당해줍니다.
        var text = document.getElementById("advertisingUrl").value;

        const tempElem = document.createElement('textarea');
        tempElem.value = text;
        document.body.appendChild(tempElem);

        tempElem.select();
        document.execCommand('copy');
        document.body.removeChild(tempElem);

        fnKendoMessage({message : 'URL이 복사 되었습니다'});
    }

	// 중복체크
    function fnExistCheck(){
        fnAjax({
            url		:	"/admin/promotion/advertising/isExistPmAdExternalCd",
            params 	:	{ 'pmAdExternalCd' : $('#pmAdExternalCd').val() },
            success	:
                function( data ){
                    if(data === true){
                        gExistCd = false;
                        fnKendoMessage({message:'동일한 이름으로 등록된 광고 ID가 존재합니다. <br>다시 입력해 주세요.'});
                        return;
                    }else{
                        gExistCd = true;
                        fnKendoMessage({message:'등록 가능한 광고 ID 입니다.'});
                        return;
                    }
                },
            isAction : 'select'
        });
    }

    function fnPmExternalCdInit(){
        gExistCd = false;

        if( $('#pmAdExternalCd').val().length > 0 ){
            $('#fnExistCheck').attr('disabled', false);
        }else{
            $('#fnExistCheck').attr('disabled', true);
        }

    }

    function fnUIExternalCd(type){
        if(type == "SOURCE"){
            $('#newSource').val("");
            $('#newMedium').val("");
            $('#newCampaign').val("");
            $('#newContent').val("");
            if($('#source').val() == ""){
                $('#newSource').show();
                $('#newMedium').show();
                $('#newCampaign').show();
                $('#newContent').show();
            } else {
                $('#newSource').hide();
            }
        } else if(type == "MEDIUM"){
           $('#newMedium').val("");
           $('#newCampaign').val("");
           $('#newContent').val("");
           if($('#medium').val() == ""){
               $('#newMedium').show();
               $('#newCampaign').show();
               $('#newContent').show();
           } else {
               $('#newMedium').hide();
           }
       } else if(type == "CAMPAIGN"){
          $('#newCampaign').val("");
          $('#newContent').val("");
          if($('#campaign').val() == ""){
              $('#newCampaign').show();
              $('#newContent').show();
          } else {
              $('#newCampaign').hide();
          }
       }

    }

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	function fnInitOptionBox(){
	    gExistCd = false;

        if(gPopupType == "ADD"){
            // 분류값 선택 - 매체
            fnKendoDropDownList({
                id : "source",
                url : "/admin/promotion/advertising/getAdvertisingType",
                tagId : "source",
                params : { "searchType" : "SOURCE" },
                valueField: 'CODE',
                textField: 'NAME',
                blank : "직접 입력",
                async : false
            });

            // 분류값 선택 - 구좌
            fnKendoDropDownList({
                id : "medium",
                url : "/admin/promotion/advertising/getAdvertisingType",
                tagId : "medium",
                params : { "searchType" : "MEDIUM" },
                chkVal: "",
                blank : "직접 입력",
                async : false,
                cscdId : "source",
                cscdField : "source"
            });

            // 분류값 선택 - 캠페인
            fnKendoDropDownList({
                id : "campaign",
                url : "/admin/promotion/advertising/getAdvertisingType",
                tagId : "campaign",
                params : { "searchType" : "CAMPAIGN"},
                chkVal: "",
                blank : "직접 입력",
                async : false,
                cscdId : "medium",
                cscdField : "medium"
            });

            $('#source').on('change', function(e) {
                fnUIExternalCd("SOURCE");
            });
            $('#medium').on('change', function(e) {
                fnUIExternalCd("MEDIUM");
            });
            $('#campaign').on('change', function(e) {
                fnUIExternalCd("CAMPAIGN");
            });
        }

        // 사용여부 라디오버튼
        fnTagMkRadio({
            id    :  'useYn',
            tagId : 'useYn',
            data  : [	{ "CODE" : "Y" , "NAME":"사용"}
                    , 	{ "CODE" : "N" , "NAME":"미사용"}],
            chkVal: 'Y',
            style : {},
            async : false
        });
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
                fnKendoMessage({
                    message : '저장되었습니다.',
                    ok      : function(){ fnClose();}
                });
            break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Close*/
	$scope.fnClose =function(){	 fnClose();	};

	/** Common Save*/
	$scope.fnSave = function(){
         fnKendoMessage({
             message:'저장 하시겠습니까?',
             type : "confirm" ,
             ok : function(){
                 fnSave();
             }
         });
	};

    $scope.fnPmExternalCdInit = function(){ fnPmExternalCdInit();};

    $scope.fnExistCheck = function(){ fnExistCheck();};

    $scope.fnUrlCopy = function(){ fnUrlCopy();};

    fnInputValidationForAlphabetHangulNumberSpace("advertisingName") //광고명 입력제한 - 한글, 영문, 숫자, 공백
	fnInputValidationForAlphabetNumber("pmAdExternalCd");       //광고ID 입력제한 - 영문, 숫자

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END