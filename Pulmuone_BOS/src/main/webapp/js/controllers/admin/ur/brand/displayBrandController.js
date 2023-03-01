﻿/**-----------------------------------------------------------------------------
 * description 		 : 브랜드 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.10     신성훈          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var itemArray = [];
$(document).ready(function() {

    fnInitialize();


    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
              PG_ID    : 'displayBrand'
            , callback : fnUI
        });
    }

    function fnUI() {
		fnInitButton();	  // Initialize Button
		fnInitGrid()  ;	  // Initialize Grid
		fnInitOptionBox();// Initialize Option Box
		fnSearch();
	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnNew,#fnSave, #fnClear, #fnClose').kendoButton();
	}

    //--- 검색  -----------------
    function fnSearch() {
        $('#inputForm').formClear(false);
        $('#rootPath').val(fnGetPublicStorageUrl());
        var data = $('#searchForm').formSerialize(true);
        var query = {
                       page         : 1
                     , pageSize     : PAGE_SIZE
                     , filterLength : fnSearchData(data).length
                     , filter :  {
                           filters : fnSearchData(data)
                       }
        };

        aGridDs.query( query );
    }

    //-- 초기화 버튼 -----------------
	function fnClear() {
		$('#searchForm').formClear(true);
		$("span#searchUseYn input:radio").eq(0).click();
	}

    //-- 추가 팝업창
    function fnNew() {
        //aGrid.clearSelection();
        $('#inputForm').formClear(true);

        fnImageButtonClear();
        fnInputFormClear();
        //$("#urSupplierId").data("kendoDropDownList").readonly(false);

        inputFocus();
        fnKendoInputPoup({height:"650px" ,width:"1000px", title:{ nullMsg :'전시 브랜드 등록' } });
    }

    //--- 추가/수정하기 전에 입력값 검증 -----------------
    function fnCheckBeforeSave() {

        var dpBrandName    = $.trim($("#dpBrandName").val()).replace(/\n/g, "");
        var useYn        = $(':radio[name="useYn"]:checked').val();
        var brandPavilionYn        = $(':radio[name="brandPavilionYn"]:checked').val();

        if ( dpBrandName == "") {
            $("#dpBrandName").val("");
            return fnAlertMessage("전시 브랜드명을 입력하세요.", "dpBrandName");
        }

        if ( useYn == "") {
            return fnAlertMessage("사용여부를 선택하세요.", "useYn");
        }

        if ( brandPavilionYn == "") {
            return fnAlertMessage("브랜드관 운영여부를 선택하세요.", "brandPavilionYn");
        }

        if ( fnImageYn("filePcMain"    , "전시 브랜드 Logo(PC) 메인 파일을 선택하세요"             ) == false) { return false; }
        if ( fnImageYn("filePcMainOver"    , "전시 브랜드 Logo(PC) 메인 Over 파일을 선택하세요"        ) == false) { return false; }
        if ( fnImageYn("fileMobileMain", "전시 브랜드 Logo(Mobile) 메인 파일을 선택하세요"         ) == false) { return false; }
        if ( fnImageYn("fileMobileMainOver", "전시 브랜드 Logo(Mobile) 메인 Over 파일을 선택하세요"    ) == false) { return false; }
        if ( fnImageYn("fileTitleBannerPc", "타이틀 배너(PC) 파일을 선택하세요") == false) { return false; }
        if ( fnImageYn("fileTitleBannerMobile", "타이틀 배너(Mobile) 파일을 선택하세요") == false) { return false; }

        return true;
    }

    //-- 저장하기 전에 첨부파일 유무를 확인한다.
    function fnImageYn(fileButtonName, NoFileMessage) {

        if (OPER_TP_CODE == "U") {
            //--수정할 때
            var sliceString = fileButtonName.slice(4);
            var fileSeq     = $("#seq" + sliceString).val();

            if (fileSeq == null || fileSeq == "" || fileSeq == undefined) {
                if ( $("#" + fileButtonName).data("kendoUpload").getFiles().length == 0 ) {
                    fnAlertMessage(NoFileMessage, fileButtonName);
                    return false;
                } else {
                    return true;
                }
            } else {
            	return true;
            }

        } else {
            //--추가할 때
            if ( $("#" + fileButtonName).data("kendoUpload").getFiles().length == 0 ) {
                fnAlertMessage(NoFileMessage, fileButtonName);
                return false;
            } else {
                return true;
            }
        }
    };

	function fnClose() {
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
		fnInputFormClear();
	}
	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {
        aGridDs = fnGetPagingDataSource({
              url      : "/admin/ur/brand/getDisplayBrandList"
            , pageSize : PAGE_SIZE
        });

        aGridOpt = {
              dataSource: aGridDs
            , pageable  : {
                  pageSizes   : [20, 30, 50]
                , buttonCount : 10
              }
            , navigatable : true
            , columns : [
                    {field : 'no'        , title : 'No'         , width : '50px' , attributes : {style : 'text-align:center'}, template: "<span class='row-number'></span>"}
                  , {field : 'dpBrandId' , title : '전시 브랜드 코드' , width : '80px' , attributes : {style : 'text-align:center'}}

                  , {field : 'sImg'      , title : fnGetLangData({key : '2841' , nullMsg :'이미지'})
                                         , template   : kendo.template($("#imgTpl").html())
                                         , width      : '50px'
                                         , attributes : { style:'text-align:center' }}

                  , {field : 'dpBrandName'   , title : '전시 브랜드 명'   , width : '200px' , attributes : {style : 'text-align:left'  }}
                  , {field : 'brandPavilionYn', title : '브랜드관 사용'   , width : '80px' , attributes : {style : 'text-align:center'}}
                  , {field : 'useYn'       , title : '사용여부'   , width : '80px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'createDate'  , title : '등록일자'   , width : '100px'  , attributes : {style : 'text-align:center'}}
                  ,{ field:'', hidden:true}
                  ,   { command: [{ text: '수정',
  									click: function(e) {

  						            e.preventDefault();
  						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
  						            var data = this.dataItem(tr);

  						            fnGetBrand(data.dpBrandId);
  						        }, visible: function() { return fnIsProgramAuth("SAVE") } }]
  					, title: '관리', width: "50px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요

              ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#totalCnt').text(aGridDs._total);
        });

	}

    function fnGetBrand(dpBrandId){
    	var baseRoot = fnGetPublicStorageUrl();
    	fnAjax({
            url     : '/admin/ur/brand/getDisplayBrand'
          , params  : {"dpBrandId" : dpBrandId, baseRoot : baseRoot}
          , success : function (data) {
                          fnBizCallback("select", data);
                      }
          , isAction : 'select'
      });
    }


    //---- 브랜드 Logo 팝업
	function fnShowLogoImage (param) {
        var logoUrl = "";

        if ( OPER_TP_CODE != 'U') {
            return;
        }

        switch (param) {
            case "pcMainLogo" :
            	logoUrl = $("#logoUrlPcMainLogo").val();
                break;

            case "pcMainOverLogo" :
            	logoUrl = $("#logoUrlPcMainOverLogo").val();
                break;

            case "mobileMainLogo" :
            	logoUrl = $("#logoUrlMobileMainLogo").val();
                break;

            case "mobileMainOverLogo" :
            	logoUrl = $("#logoUrlMobileMainOverLogo").val();
                break;

            case "titleBannerPc" :
            	logoUrl = $("#logoUrlTitleBannerPc").val();
                break;

            case "titleBannerMobile" :
            	logoUrl = $("#logoUrlTitleBannerMobile").val();
                break;

            default :
                return;
                break;
        }

        fnKendoPopup({
              id      : 'brandPopup'
            , title   : '브랜드 Logo'
            , src     : '#/brandPopup'
            , width   : '500px'
            , height  : '400px'
            , param   : { "LOGO_URL" : logoUrl }
            , success : function (id, data) {
                            if (data.id) {
                                $('#baseName').val(data.baseName);
                            }
                        }
        });
    }
    //==================================================================================
	//-------------------------------  Grid End  -------------------------------
    //==================================================================================



    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {
        $('#kendoPopup').kendoWindow({
              visible: false
            , modal  : true
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 브랜드 콤보상자
        //-----------------------------------------------------------------------
        fnKendoDropDownList({
              id   : 'brandSearchType'
            , data : [
                        {"CODE":"BRAND_NAME", "NAME":'전시 브랜드명'   }
                      , {"CODE":"BRAND_CODE", "NAME":'전시 브랜드 코드'}
                     ]
            , textField  : "NAME"
            , valueField : "CODE"
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 사용여부 라디오
        //-----------------------------------------------------------------------
        fnTagMkRadio({
              id    : 'searchUseYn'
            , tagId : 'searchUseYn'
            , data  : [
                         {"CODE" : "" , "NAME" : '전체'     }
                       , {"CODE" : "Y", "NAME" : '사용'     }
                       , {"CODE" : "N", "NAME" : '사용안함' }
                      ]
            , chkVal: ""
            , style : {}
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 공급업체 콤보 목록
        //-----------------------------------------------------------------------
        // 공급업체 리스트 조회
        fnTagMkRadio({
            id    : 'searchBrandPavilionYn'
          , tagId : 'searchBrandPavilionYn'
          , data  : [
                       {"CODE" : "" , "NAME" : '전체'     }
                     , {"CODE" : "Y", "NAME" : '사용'     }
                     , {"CODE" : "N", "NAME" : '사용안함' }
                    ]
          , chkVal: ""
          , style : {}
      });


        //-----------------------------------------------------------------------
        //-- Initializing -> 추가/수정 레이어 팝업창 : 사용여부 라디오
        //-----------------------------------------------------------------------
        fnTagMkRadio({
            id    : 'useYn'
          , tagId : 'useYn'
          , data  : [
                       {"CODE" : "Y", "NAME" : '사용'     }
                     , {"CODE" : "N", "NAME" : '사용안함' }
                    ]
          , chkVal: "Y"
          , style : {}
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 추가/수정 레이어 팝업 : 공급업체 콤보 목록
        //-----------------------------------------------------------------------
        fnTagMkRadio({
            id    : 'brandPavilionYn'
          , tagId : 'brandPavilionYn'
          , data  : [
                       {"CODE" : "Y", "NAME" : '사용'     }
                     , {"CODE" : "N", "NAME" : '사용안함' }
                    ]
          , chkVal: "Y"
          , style : {}
        });


	}  // the end of fnInitOptionBox()

    //----------------------------------------------------------------------------------------------------
    //-- 파일을 화면에 추가하는 스크립트
    //----------------------------------------------------------------------------------------------------
    function fnFileUploadCheck(e, previewName, fileShowObject) {
        let f = e.files[0];
        let ext = f.extension.substring(1, f.extension.length);

        if ($.inArray(ext.toLowerCase(), ["png"]) == -1) {
            fnKendoMessage({message : "png 이미지 파일만 첨부가능합니다."});
            e.preventDefault();
        } else {
            if (typeof(window.FileReader) == "undefined") {
                $("#" + previewName).attr("src", e.sender.element.val());
            } else {
                if (f) {
                    var reader = new FileReader();
                    reader.readAsDataURL(f.rawFile);

                    reader.onload = function (ele) {
                        $("#" + previewName).attr("src", ele.target.result);
                    };
                    $("#" + fileShowObject).val(f.name);
                }
            }
        }
    };
    //==================================================================================
	//---------------Initialize Option Box End -----------------------------------------
    //==================================================================================


    // 브랜드 로고 PC(메인) 파일첨부
    fnKendoUpload({
        id : "filePcMain",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "png" ]) == -1 ){
                fnKendoMessage({
                	message : "png 파일만 첨부가능합니다."
                });
                e.preventDefault();
                return false;
          } else {
        	  if(f.size <= 500000){
                if (e.files && e.files[0]) {
                    let reader = new FileReader();

                    reader.onload = function(ele) {
                    	$('.k-upload-sync').addClass("k-upload-empty");
                    	let fileName = $(".cs_bbs_attc_pcMain").prev().find("span.k-file-name")[0].title;
                    	$(".cs_bbs_attc_pcMain").html(fileName);
                    	$('#logoPcMainLogoImage').attr('src', ele.target.result);
                    };


                    reader.readAsDataURL(f.rawFile);
                }
        	  } else {
          		fnKendoMessage({
                      message : "이미지는 500kb까지 첨부할 수 있습니다."
					});
					e.preventDefault();
          	}
            }
        },
        localization : "파일첨부"
    });


    // 브랜도 로고 PC(메인 OVER) 파일첨부
    fnKendoUpload({
        id : "filePcMainOver",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "png"]) == -1 ){
                  fnKendoMessage({
                	  message : "png 파일만 첨부가능합니다."
                  });
                  e.preventDefault();
                  return false;
            } else {
            	if(f.size <= 500000){
	                if (e.files && e.files[0]) {
	                    let reader = new FileReader();

	                    reader.onload = function(ele) {
	                    	$('.k-upload-sync').addClass("k-upload-empty");
	                    	let fileName = $(".cs_bbs_attc_pcMainOver").prev().find("span.k-file-name")[0].title;
	                    	$(".cs_bbs_attc_pcMainOver").html(fileName);
	                    	$('#logoPcMainOverLogoImage').attr('src', ele.target.result);
	                    };

	                    reader.readAsDataURL(f.rawFile);
	                }
            	} else {
	        		fnKendoMessage({
	                    message : "이미지는 500kb까지 첨부할 수 있습니다."
						});
						e.preventDefault();
	        	}
            }
        },
        localization : "파일첨부"
    });


    // 브랜도 로고 Mobile(메인) 파일첨부
    fnKendoUpload({
        id : "fileMobileMain",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "png" ]) == -1 ){
                  fnKendoMessage({
                	  message : "png 파일만 첨부가능합니다."
                  });
                  e.preventDefault();
            } else {
            	if(f.size <= 500000){
	                if (e.files && e.files[0]) {
	                    let reader = new FileReader();

	                    reader.onload = function(ele) {
	                    	$('.k-upload-sync').addClass("k-upload-empty");
	                    	let fileName = $(".cs_bbs_attc_mobileMain").prev().find("span.k-file-name")[0].title;
	                    	$(".cs_bbs_attc_mobileMain").html(fileName);
	                    	$('#logoMobileMainLogoImage').attr('src', ele.target.result);
	                    };

	                    reader.readAsDataURL(f.rawFile);
	                }
	        	} else {
	        		fnKendoMessage({
	                    message : "이미지는 500kb까지 첨부할 수 있습니다."
						});
						e.preventDefault();
	        	}
            }
        },
        localization : "파일첨부"
    });

    // 브랜도 로고 Mobile(메인 Over) 파일첨부
    fnKendoUpload({
        id : "fileMobileMainOver",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "png" ]) == -1 ){
                  fnKendoMessage({
                	  message : "png 파일만 첨부가능합니다."
                  });
                  e.preventDefault();
            } else {
            	if(f.size <= 500000){
	                if (e.files && e.files[0]) {
	                    let reader = new FileReader();

	                    reader.onload = function(ele) {
	                    	$('.k-upload-sync').addClass("k-upload-empty");
	                    	let fileName = $(".cs_bbs_attc_mobileMainOver").prev().find("span.k-file-name")[0].title;
	                    	$(".cs_bbs_attc_mobileMainOver").html(fileName);
	                    	$('#logoMobileMainOverLogoImage').attr('src', ele.target.result);
	                    };

	                    reader.readAsDataURL(f.rawFile);
	                }
	        	} else {
	        		fnKendoMessage({
	                    message : "이미지는 500kb까지 첨부할 수 있습니다."
						});
						e.preventDefault();
	        	}
            }
        },
        localization : "파일첨부"
    });



    // 타이틀 배너(PC) 파일첨부
    fnKendoUpload({
        id : "fileTitleBannerPc",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "jpg", "jpeg" ]) == -1 ){
                  fnKendoMessage({
                	  message : "jpg jpeg 파일만 첨부가능합니다."
                  });
                  e.preventDefault();
            } else {
            	if(f.size <= 500000){
	                if (e.files && e.files[0]) {
	                    let reader = new FileReader();

	                    reader.onload = function(ele) {
	                    	$('.k-upload-sync').addClass("k-upload-empty");
	                    	let fileName = $(".cs_bbs_attc_titleBannerPc").prev().find("span.k-file-name")[0].title;
	                    	$(".cs_bbs_attc_titleBannerPc").html(fileName);
	                    	$('#logoTitleBannerPcImage').attr('src', ele.target.result);
	                    };

	                    reader.readAsDataURL(f.rawFile);
	                }
	        	} else {
	        		fnKendoMessage({
	                    message : "이미지는 500kb까지 첨부할 수 있습니다."
						});
						e.preventDefault();
	        	}
            }
        },
        localization : "파일첨부"
    });


    // 타이틀 배너(Mobile) 파일첨부
    fnKendoUpload({
        id : "fileTitleBannerMobile",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "jpg", "jpeg" ]) == -1 ){
                  fnKendoMessage({
                	  message : "jpg jpeg 파일만 첨부가능합니다."
                  });
                  e.preventDefault();
            } else {
            	if(f.size <= 500000){
	                if (e.files && e.files[0]) {
	                    let reader = new FileReader();

	                    reader.onload = function(ele) {
	                    	$('.k-upload-sync').addClass("k-upload-empty");
	                    	let fileName = $(".cs_bbs_attc_titleBannerMobile").prev().find("span.k-file-name")[0].title;
	                    	$(".cs_bbs_attc_titleBannerMobile").html(fileName);
	                    	$('#logoTitleBannerMobileImage').attr('src', ele.target.result);
	                    };

	                    reader.readAsDataURL(f.rawFile);
	                }
	        	} else {
	        		fnKendoMessage({
	                    message : "이미지는 500kb까지 첨부할 수 있습니다."
						});
						e.preventDefault();
	        	}
            }
        },
        localization : "파일첨부"
    });


    // 업로드 회원등록 버튼 클릭
    /*function fnExcelUploadUser() {
        $("#filePcMainLogo").trigger("click");
    };*/


	function inputFocus(){
		$('#input1').focus();
	};


    function fnSave(){
    	var useYnValue = $('#useYn').getRadioVal();
        if(useYnValue == 'N' && itemArray.includes($('#dpBrandId').val())){
    		fnKendoMessage({
				type : "confirm",
				message : "현재 해당 브랜드로 등록된 상품이 존재하며, 사용안함 처리 시 해당상품 노출이 제한될 수 있습니다. <br> 진행하시겠습니까?",
				ok : function() {
					fnSaveConfirm();
				},
				cancel : function() {
					return;
				}
			});
    	}else{
    		fnSaveConfirm();
    	}
	}

    // 브랜드 저장
    function fnSaveConfirm() {

        let data = $('#inputForm').formSerialize(true);

        if (!fnCheckBeforeSave()) {
            return;
        }

        var cbId = 'insert';

        if ( OPER_TP_CODE == 'U') {
            cbId= 'update';
        }


        var url = '';
        if (cbId == "insert") {
            url = '/admin/ur/brand/addDisplayBrand';
        } else if (cbId == "update") {
            url = '/admin/ur/brand/putDisplayBrand';
        }



        if( data.rtnValid ){
	        fnAjaxSubmit({
	            form : 'inputForm',
	            fileUrl : '/fileUpload',
	            storageType : "public", // 추가
	            domain : "ur", // 추가
	            url : url,
	            params : data,
	            success : function(successData) {
	                fnBizCallback(cbId , successData);
	            },
	            isAction : 'batch'
	        });
        }

    }

    function fnGetPublicStorageUrl() {  // 로컬/개발 등 각 서버 환경에서 public 저장소 접근 url 호출/반환

        var publicStorageUrl;

        $.ajax({
            type : 'GET',
            dataType : 'json',
            url : '/comn/getPublicStorageUrl',
            async : false,
            beforeSend : function(xhr, settings) {
                fnOpenLoading();
            },
            success : function(data, status, xhr) {
                data = data.data;

                fnCloseLoading();
                publicStorageUrl = data['publicStorageUrl'];
            },
            error : function(xhr, status, strError) {
                fnCloseLoading();
                return null;
            }
        });

        return publicStorageUrl;
    }



    //==================================================================================
    //-------------------------------  Common Function start ---------------------------
    //==================================================================================
    function inputFocus() {
        $('#dpBrandName').focus();
    };

    //-------------------------------  콜백합수 -----------------------------
    function fnBizCallback (id, data) {
        switch (id) {
            case 'select':

                //-- 화면 초기화
                //-----------------------------------------------
                fnInputFormClear();
                $('#inputForm').formClear(true);
                fnImageButtonClear();

                if(data.rows.dpIdList != null){
	            	for(var i=0 ; i<data.rows.dpIdList.length; i++){
	            		itemArray.push(data.rows.dpIdList[i].dpBrandId);
	            	}
                }

                //-----------------------------------------------
                //-- 이미지 보여주기 - 시작 --
                //-----------------------------------------------
                if (data.rows.logoUrlPcMainLogo != null && data.rows.logoUrlPcMainLogo != "") {
                	$("#logoPcMainLogoImage").attr("src", data.rows.logoUrlPcMainLogo);
                }

                if (data.rows.logoUrlPcMainOverLogo != null && data.rows.logoUrlPcMainOverLogo != "") {
                	$("#logoPcMainOverLogoImage").attr("src", data.rows.logoUrlPcMainOverLogo);
                }

                if (data.rows.logoUrlMobileMainLogo != null && data.rows.logoUrlMobileMainLogo != "") {
                	$("#logoMobileMainLogoImage").attr("src", data.rows.logoUrlMobileMainLogo);
                }

                if (data.rows.logoUrlMobileMainOverLogo != null && data.rows.logoUrlMobileMainOverLogo != "") {
                	$("#logoMobileMainOverLogoImage").attr("src", data.rows.logoUrlMobileMainOverLogo);
                }

                if (data.rows.logoUrlTitleBannerPc != null && data.rows.logoUrlTitleBannerPc != "") {
                	$("#logoTitleBannerPcImage").attr("src", data.rows.logoUrlTitleBannerPc);
                }

                if (data.rows.logoUrlTitleBannerMobile != null && data.rows.logoUrlTitleBannerMobile != "") {
                	$("#logoTitleBannerMobileImage").attr("src", data.rows.logoUrlTitleBannerMobile);
                }
                //-----------------------------------------------
                //-- 이미지 보여주기 - 끝 --
                //-----------------------------------------------

                //$(".k-upload").css({"display": "none"});
				var pcMain = "";
				pcMain = pcMain + data.rows.originNamePcMain+"\n";
				$(".cs_bbs_attc_pcMain").html(pcMain);

				var pcMainOver = "";
				pcMainOver = pcMainOver + data.rows.originNamePcMainOver+"\n";
				$(".cs_bbs_attc_pcMainOver").html(pcMainOver);

        		var mobileMain = "";
        		mobileMain = mobileMain + data.rows.originNameMobileMain+"\n";
        		$(".cs_bbs_attc_mobileMain").html(mobileMain);

        		var mobileMainOver = "";
        		mobileMainOver = mobileMainOver + data.rows.originNameMobileMainOver+"\n";
        		$(".cs_bbs_attc_mobileMainOver").html(mobileMainOver);

        		var titleBannerPc = "";
        		titleBannerPc = titleBannerPc + data.rows.originNameTitleBannerPc+"\n";
        		$(".cs_bbs_attc_titleBannerPc").html(titleBannerPc);

        		var titleBannerMobile = "";
        		titleBannerMobile = titleBannerMobile + data.rows.originNameTitleBannerMobile+"\n";
        		$(".cs_bbs_attc_titleBannerMobile").html(titleBannerMobile);

        		$('#inputForm').bindingForm(data, "rows", true);

                fnKendoInputPoup({height:"650px" ,width:"1000px",title:{key :"5876",nullMsg :'전시 브랜드 수정' } });
                break;

			case 'insert':
			case 'update':
                if ( id == "insert" ) {
                    aGridDs.page(1);
                }

				fnKendoMessage({
                        message:"저장되었습니다."
                        , ok : function() {
                                      fnSearch();
                                      fnClose();
                               }
				});
			    break;

            case 'delete':
                fnKendoMessage({  message : '삭제되었습니다.'
                                , ok      : function(){
                                                fnSearch();
                                                fnClose();
                                            }
                });
                break;
        }
    }

    //-- Alert 메세지
    function fnAlertMessage(msg, id) {
        fnKendoMessage(
                       {  message : msg
                        , ok      : function focusValue() { $("#" + id).focus(); }
                       }
                      );

        return false;
    };

    //------------------------------------------------------------------
    //-- file 버튼 내부 초기화
    //--- 입력/수정 레이어 팝업을 호출하기 전에 사용한다.
    //------------------------------------------------------------------
    function fnImageButtonClear() {
    	$("#filePcMain").data("kendoUpload").clearAllFiles();
        $("#filePcMainOver").data("kendoUpload").clearAllFiles();

        $("#fileMobileMain").data("kendoUpload").clearAllFiles();
        $("#fileMobileMainOver").data("kendoUpload").clearAllFiles();

        $("#fileTitleBannerPc").data("kendoUpload").clearAllFiles();
        $("#fileTitleBannerMobile").data("kendoUpload").clearAllFiles();

    	 var emptyImage = "/contents/images/noimg.png";
        $("#logoPcMainLogoImage").attr("src", emptyImage);
        $("#logoPcMainOverLogoImage").attr("src", emptyImage);

        $("#logoMobileMainLogoImage").attr("src", emptyImage);
        $("#logoMobileMainOverLogoImage").attr("src", emptyImage);

        $("#logoTitleBannerPcImage").attr("src", emptyImage);
        $("#logoTitleBannerMobileImage").attr("src", emptyImage);

        $("#originNamePcMain").val("");
        $("#originNamePcMainOver").val("");

        $("#originNameMobileMain").val("");
        $("#originNameMobileMainOver").val("");

        $("#originNameTitleBannerPc").val("");
        $("#originNameTitleBannerMobile").val("");

        $(".cs_bbs_attc_pcMain").html("");
        $(".cs_bbs_attc_pcMainOver").html("");
        $(".cs_bbs_attc_mobileMain").html("");
        $(".cs_bbs_attc_mobileMainOver").html("");
        $(".cs_bbs_attc_titleBannerPc").html("");
        $(".cs_bbs_attc_titleBannerMobile").html("");
    }

    //-- 입력/수정 화면 초기화
    function fnInputFormClear() {
        var dpBrandName    = $("#dpBrandName").val("")   ;
        $("span#useYn input:radio").eq(0).click();
        $("span#brandPavilionYn input:radio").eq(0).click();
        fnImageButtonClear();
    }
    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start --------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnNew        = function () { fnNew()   ; };
    $scope.fnSave       = function () { fnSave()  ; };
    $scope.fnClose      = function () { fnClose() ; };

    $scope.fnShowLogoImage = function (param) { fnShowLogoImage(param); };
	$scope.fnPopupButton   = function (param) { fnPopupButton  (param); };

    //-- 입력값 제한
//	fnInputValidationForAlphabetKorean("brandSearchValue");
//    fnInputValidationForAlphabetKorean("brandName");
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

}); // document ready - END
