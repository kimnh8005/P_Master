/**-----------------------------------------------------------------------------
 * description 		 : 공지사항 등록 및 수정 < 거래처 공지/문의 < 거래처 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.02.21		김아란          최초생성
 * @
 * **/
'use strict';

var pageParam = fnGetPageParam();
var dataInfo;
var currPopupYn;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------


	var publicStorageUrl; // Public 저장소 Root Url
	var workindEditorId; // 상품 상세 기본 정보와 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id

	var itemImageUploadMaxLimit = 512000; // 상품 이미지 첨부 가능 최대 용량 ( 단위 : byte )

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'noticeAdd',
			callback : fnUI
		});
	}

	function fnUI(){

		fnDefaultValue();

	   	fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------
	}

	//--------------------------------- Button Start---------------------------------

	function fnDefaultValue(){
		var ID = pageParam.csCompanyBbsId;
		var GUBUN = pageParam.editMode;
		// 수정인 경우
		if(GUBUN == "U"){
			fnAjax({
				url     : 'admin/cs/notice/getNotice',
				params  : {csCompanyBbsId : ID, viewMode : 'N'},
				success :
					function( data ){
						dataInfo = data.rows;
						$('#inputForm').bindingForm( {'rows':data.rows},'rows', true);

						if(data.rows.popupYn == "Y") {
							 $("input:checkbox[name=inputPopupYnCheckBox]").prop("checked", true);
							 $(".popupInfo").show();
						}

						$("#spanCreateId").html(data.rows.createName);
						$("#spanCreateDate").html(data.rows.createDate);
						$("#spanModifyId").html(data.rows.modifyName);
						$("#spanModifyDate").html(data.rows.modifyDate);

						$("#popupDisplayEndDate").data("kendoDatePicker").min($("#popupDisplayStartDate").val());
						$("#popupDisplayStartDate").data("kendoDatePicker").max($("#popupDisplayEndDate").val());

						$("#trCreateInfo").show();
						$("#trModifyInfo").show();

						// 첨부파일 정보
						if(data.rowsFile != null){
							$(".k-upload").css({"display": "none"});
							var fileList = "";
							fileList = fileList + data.rowsFile.physicalAttached+"\n";
							fileList = fileList + "<input type=\"hidden\" value=\""+data.rowsFile.csCompanyBbsAttachId+"\" name=\"csCompanyBbsAttachId\" id=\"csCompanyBbsAttachId\"/>\n";
							fileList = fileList + "<button type=\"button\" id=\"fileDeleteBtn\" class=\"btn-red btn-s\">삭제 </button><br>\n";
							$(".cs_bbs_attc_div").html(fileList);

							$('#fileDeleteBtn').on('click', function(e){
						        e.preventDefault();
						        fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" , ok : function(){
						        	fnFileDelApply(data.rowsFile.csCompanyBbsId);
								}});
						    });
						}

						/* HGRM-2053 - dgyoun : 팝업노출변경에 따른 초기값 설정 */
						// 변경전의 팝업노출여부 Set
						currPopupYn = rows.popupYn;
					},
				isAction : 'batch'
			});
		}else{

		}
	}
	function fnFileDelApply(attcId){
    	//var url  = '/biz/cs/compQna/delCompQnaAttc';
    	var url  = '/admin/cs/notice/delAttc';
    	var cbId = 'delete';
    	var data = {"csCompanyBbsId":attcId};
    	fnAjax({
			url     : url,
			params  : data,
			success :
				function( data ){
    				$(".cs_bbs_attc_div").css({"display": "none"});
			        $(".k-upload").css({"display": "block"});
			},
			isAction : 'delete'
		});
    }
	function fnInitButton(){
		$('#fnSave, #fnBefore, #fnList').kendoButton();
	}
	function fnSave(){
		var url  = '/admin/cs/notice/addNotice';
		var cbId = 'insert';

		if(pageParam.editMode == 'U'){
			url  = '/admin/cs/notice/putNotice';
			cbId= 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		//checkbox 체크안함 => 'N' 값보정
		$("input:checkbox:not(:checked)").each(function(){
			data[$(this).attr("name")] = 'N';
		});

		if( data.rtnValid ){
			fnAjaxSubmit({
				   form    : 'inputForm',
				   fileUrl : '/fileUpload',
				   url     : url,
				   params  : data,
				   storageType : "public",
				   domain : "il",
				   success : function(data){
				      fnBizCallback(cbId, data);
				   },
				   isAction : 'batch'
			});
		}else{

		}
    }
	function fnBefore(){
		var option = new Object();
		if(pageParam.editMode == 'U'){
			option.url = "#/noticePopup";
        	option.menuId = 200 ;
        	option.data = {
        					csCompanyBbsId	:pageParam.csCompanyBbsId,
        					companyBbsType	:pageParam.companyBbsType,
        					useYn			:pageParam.useYn,
        					popupYn			:pageParam.popupYn,
        					conditionType	:pageParam.conditionType,
        					conditionValue	:pageParam.conditionValue,
        					startCreateDate	:pageParam.startCreateDate,
        					endCreateDate	:pageParam.endCreateDate
		        			};
        	$scope.$emit('goPage', option);

		}else{
			fnList();
		}
	}
    function fnList(){
    	var option = new Object();
    	option.url = "#/notice";
    	option.menuId = 179;
    	option.data = {
		    			companyBbsType	:pageParam.companyBbsType,
						useYn			:pageParam.useYn,
						popupYn			:pageParam.popupYn,
						conditionType	:pageParam.conditionType,
						conditionValue	:pageParam.conditionValue,
						startCreateDate	:pageParam.startCreateDate,
						endCreateDate	:pageParam.endCreateDate
	        			};
    	$scope.$emit('goPage', option);
    }
    function fnDetail(data){
		var option = new Object();
    	option.url = "#/noticePopup";
    	option.menuId = 200;
    	option.data = {
    					csCompanyBbsId :data.rows.csCompanyBbsId,
    					companyBbsType	:pageParam.companyBbsType,
						useYn			:pageParam.useYn,
						popupYn			:pageParam.popupYn,
						conditionType	:pageParam.conditionType,
						conditionValue	:pageParam.conditionValue,
						startCreateDate	:pageParam.startCreateDate,
						endCreateDate	:pageParam.endCreateDate
    					};
    	$scope.goPage(option);
	}
	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		fnTagMkRadio({
			id    :  'inputCompanyBbsType',
			tagId : 'companyBbsType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "BOS_BBS_TYPE", "useYn" :"Y"},
			async : false,
			chkVal: "BOS_BBS_TYPE.COMMON",
			style : {}
		});
		fnTagMkRadioYN({id: "inputNotificationYn", tagId : "notificationYn", chkVal: 'Y'});
		fnTagMkRadioYN({id: "inputPopupDisplayTodayYn", tagId : "popupDisplayTodayYn", chkVal: 'Y'});
		fnTagMkRadioYN({id: "inputUseYn", tagId : "useYn", chkVal: 'Y'});

		fnTagMkChkBox({
            id : "inputPopupYnCheckBox",
            data : [ { "CODE" : "Y", "NAME" : "" } ],
            tagId : "inputPopupYnCheckBox",
            change : function(e){
                if( $("input:checkbox[name=inputPopupYnCheckBox]").is(":checked") ){
                    $("#popupYn").val("Y");
                    $(".popupInfo").show();

                    /* HGRM-2053 - dgyoun : 팝업노출변경에 따른 초기값 설정 */
                    // --------------------------------------------------------
                    // 수정전의 팝업노출여부가 N이었을 경우, Y 선택 시 팝업노출기간, 팝업위치, 팝업오늘보기 초기화
                    // --------------------------------------------------------
                    if (currPopupYn != 'Y') {
                      // 변경전 팝업노출여부가 'Y'가 아니라면

                      // 1. 팝업노출기간 초기화
                      fnKendoDatePicker({
                        id    : 'popupDisplayStartDate',
                        format: 'yyyy-MM-dd',
                        defVal: fnGetToday()
                      });

                      $("#popupDisplayStartDate").change(function() {
                        $("#popupDisplayEndDate").data("kendoDatePicker").min($("#popupDisplayStartDate").val());
                      })

                      fnKendoDatePicker({
                        id    : 'popupDisplayEndDate',
                        format: 'yyyy-MM-dd',
                        btnStyle : false,
                        btnStartId : 'popupDisplayStartDate',
                        btnEndId : 'popupDisplayEndDate',
                        defVal: fnGetDayAdd(fnGetToday(),6,'yyyy-MM-dd'),
                        min: $("#popupDisplayStartDate").val()
                      });
                      $("#popupDisplayStartDate").data("kendoDatePicker").max($("#popupDisplayEndDate").val());
                      $("#popupDisplayEndDate").change(function() {
                        $("#popupDisplayStartDate").data("kendoDatePicker").max($("#popupDisplayEndDate").val());
                      })

                      // 2.팝업위치 초기화
                      $('#popupCoordinateY').val('0');
                      $('#popupCoordinateX').val('0');

                      // 3.팝업오늘하루보기 초기화
                      fnTagMkRadioYN({id: "inputPopupDisplayTodayYn", tagId : "popupDisplayTodayYn", chkVal: 'Y'});
                    }

                }else{
                    $("#popupYn").val("N");
                    $(".popupInfo").hide();
                }
            }
        });

		fnKendoDatePicker({
			id    : 'popupDisplayStartDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetToday()
		});

		$("#popupDisplayStartDate").change(function() {
			$("#popupDisplayEndDate").data("kendoDatePicker").min($("#popupDisplayStartDate").val());
		})

		fnKendoDatePicker({
			id    : 'popupDisplayEndDate',
			format: 'yyyy-MM-dd',
			btnStyle : false,
			btnStartId : 'popupDisplayStartDate',
			btnEndId : 'popupDisplayEndDate',
			defVal: fnGetDayAdd(fnGetToday(),6,'yyyy-MM-dd'),
			min: $("#popupDisplayStartDate").val()
		});
		$("#popupDisplayStartDate").data("kendoDatePicker").max($("#popupDisplayEndDate").val());
		$("#popupDisplayEndDate").change(function() {
			$("#popupDisplayStartDate").data("kendoDatePicker").max($("#popupDisplayEndDate").val());
		})

        fnItemDescriptionKendoEditor({ // 상품 상세 기본정보 Editor
            id : 'content',
        });

        fnKendoUpload({ // 상품 상세 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
            id : "uploadImageOfEditor",
            select : function(e) {

                if (e.files && e.files[0]) { // 이미지 파일 선택시

                    // UPLOAD_IMAGE_SIZE : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
                    if (UPLOAD_IMAGE_SIZE < e.files[0].size) { // 용량 체크
                        fnKendoMessage({
                            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(UPLOAD_IMAGE_SIZE / 1048576) + ' MB 입니다.',
                            ok : function(e) {}
                        });
                        return;
                    }

                    let reader = new FileReader();

                    reader.onload = function(ele) {
                        fnUploadImageOfEditor(); // 선택한 이미지 파일 업로드 함수 호출
                    };

                    reader.readAsDataURL(e.files[0].rawFile);
                }
            }
        });

		//fnKendoEditor( {id : 'content'} );

	    fnKendoUpload({
	        id     : 'fileUpload',
	        template: "<div><span>파일명: #=name# &nbsp; (Size: #= kendo.toString(size, 'n0') # Bytes)</p>" +
			            "<strong class='k-upload-status'>" +
			            "<button type='button' class='k-upload-action'></button>" +
			            "<button type='button' class='k-upload-action'></button>" +
			            "</strong>" +
			            "</div>",
	        select : function(e){
	            var f = e.files[0];
	            var ext = f.extension.substring(1, f.extension.length);
	            if($.inArray(ext.toLowerCase(), ['js','html','htm','jsp','exe','cgi']) > -1){
	            	fnKendoMessage({ message : "js, html, htm, jsp, exe, cgi 확장자는 업로드 불가한 확장자입니다."});
	                e.preventDefault();
	                return false;
	            }
	            else{
	            	if (typeof(window.FileReader) == 'undefined'){
	                	//$('#photo').attr('src', e.sender.element.val());
	                }
	                else {
	                    if(f){
	                        var reader = new FileReader();
	                        reader.onload = function (ele) {
	                            //$('#photo').attr('src', ele.target.result);
	                        	$("ul.k-upload-files.k-reset").css({"display":"block"});
	                        	$("#csBbsAttcDelYn").val("");
	                        };
	                        reader.readAsDataURL(f.rawFile);
	                    }
	                }
	            }
	        },
	        localization : '파일첨부'
	    });
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start -------------------------------

	function fnUploadImageOfEditor(opt) { // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가

        var formData = $('#uploadImageOfEditorForm').formSerialize(true);

        fnAjaxSubmit({
            form : "uploadImageOfEditorForm",
            fileUrl : "/fileUpload",
            method : 'GET',
            url : '/comn/getPublicStorageUrl',
            storageType : "public",
            domain : "cs",
            params : formData,
            success : function(result) {

                var uploadResult = result['addFile'][0];
                var serverSubPath = uploadResult['serverSubPath'];
                var physicalFileName = uploadResult['physicalFileName'];
                var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url

                var editor = $('#' + workindEditorId).data("kendoEditor"); // 이미지 첨부할 Editor
                editor.exec('inserthtml', {
                	 value : '<img src="' + imageSrcUrl + '"/>'
                });

            },
            isAction : 'insert'
        });

    }

    function fnItemDescriptionKendoEditor(opt) { // 상품 상세 기본정보 / 주요 정보 Editor

        if  ( $('#' + opt.id).data("kendoEditor") ) { // 기존에 이미 Editor 로 생성되어 있는 경우 초기화

            $('#' + opt.id + 'Td').html('');  // 해당 editor TextArea 를 가지고 있는 td 내의 html 을 강제 초기화

            var textAreaHtml = '<textarea id="' + opt.id + '" ';
            textAreaHtml += 'style="width: 100%; height: 400px" ';
            textAreaHtml += '></textarea>"'

            $('#' + opt.id + 'Td').append(textAreaHtml);  // 새로운 editor TextArea 를 추가

        }

        $('#' + opt.id).kendoEditor({
            tools : [ {
                name : 'viewHtml',
                tooltip : 'HTML 소스보기'
            },
            // { name : 'pdf', tooltip : 'PDF 저장' },  // PDF 저장시 한글 깨짐 현상 있어 주석 처리
            //------------------- 구분선 ----------------------
            {
                name : 'bold',
                tooltip : '진하게'
            }, {
                name : 'italic',
                tooltip : '이탤릭'
            }, {
                name : 'underline',
                tooltip : '밑줄'
            }, {
                name : 'strikethrough',
                tooltip : '취소선'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'foreColor',
                tooltip : '글자색상'
            }, {
                name : 'backColor',
                tooltip : '배경색상'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'justifyLeft',
                tooltip : '왼쪽 정렬'
            }, {
                name : 'justifyCenter',
                tooltip : '가운데 정렬'
            }, {
                name : 'justifyRight',
                tooltip : '오른쪽 정렬'
            }, {
                name : 'justifyFull',
                tooltip : '양쪽 맞춤'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'insertUnorderedList',
                tooltip : '글머리기호'
            }, {
                name : 'insertOrderedList',
                tooltip : '번호매기기'
            }, {
                name : 'indent',
                tooltip : '들여쓰기'
            }, {
                name : 'outdent',
                tooltip : '내어쓰기'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'createLink',
                tooltip : '하이퍼링크 연결'
            }, {
                name : 'unlink',
                tooltip : '하이퍼링크 제거'
            }, {
                name : 'insertImage',
                tooltip : '이미지 URL 첨부'
            }, {
                name : 'file-image',
                tooltip : '이미지 파일 첨부',
                exec : function(e) {
                    e.preventDefault();
                    workindEditorId = opt.id; // 이미지 첨부할 Editor 의 id 를 전역변수에 저장
                    $('#uploadImageOfEditor').trigger('click'); // 파일 input Tag 클릭 이벤트 호출

                },
            },
            //------------------- 구분선 ----------------------
            {
                name : 'subscript',
                tooltip : '아래 첨자'
            }, {
                name : 'superscript',
                tooltip : '위 첨자'
            },
            //------------------- 구분선 ----------------------
            { name : 'tableWizard'    , tooltip : '표 수정' },
            {
                name : 'createTable',
                tooltip : '표 만들기'
            }, {
                name : 'addRowAbove',
                tooltip : '위 행 추가'
            }, {
                name : 'addRowBelow',
                tooltip : '아래 행 추가'
            }, {
                name : 'addColumnLeft',
                tooltip : '왼쪽 열 추가'
            }, {
                name : 'addColumnRight',
                tooltip : '오른쪽 열 추가'
            }, {
                name : 'deleteRow',
                tooltip : '행 삭제'
            }, {
                name : 'deleteColumn',
                tooltip : '열 삭제'
            },
            { name : 'mergeCellsHorizontally'   , tooltip : '수평으로 셀 병합' },
	        { name : 'mergeCellsVertically'   , tooltip : '수직으로 셀 병합' },
	        { name : 'splitCellHorizontally'   , tooltip : '수평으로 셀 분할' },
	        { name : 'splitCellVertically'   , tooltip : '수직으로 셀 분할' },
            //------------------- 구분선 ----------------------
            'formatting', 'fontName', {
                name : 'fontSize',
                items : [ {
                    text : '8px',
                    value : '8px'
                }, {
                    text : '9px',
                    value : '9px'
                }, {
                    text : '10px',
                    value : '10px'
                }, {
                    text : '11px',
                    value : '11px'
                }, {
                    text : '12px',
                    value : '12px'
                }, {
                    text : '13px',
                    value : '13px'
                }, {
                    text : '14px',
                    value : '14px'
                }, {
                    text : '16px',
                    value : '16px'
                }, {
                    text : '18px',
                    value : '18px'
                }, {
                    text : '20px',
                    value : '20px'
                }, {
                    text : '22px',
                    value : '22px'
                }, {
                    text : '24px',
                    value : '24px'
                }, {
                    text : '26px',
                    value : '26px'
                }, {
                    text : '28px',
                    value : '28px'
                }, {
                    text : '36px',
                    value : '36px'
                }, {
                    text : '48px',
                    value : '48px'
                }, {
                    text : '72px',
                    value : '72px'
                }, ]
            }
            //  ,'print'
            ],
            //            pdf : {
            //                title : "상품상세 기본정보",
            //                fileName : "상품상세 기본정보.pdf",
            //                paperSize : 'A4',
            //                margin : {
            //                    bottom : '20 mm',
            //                    left : '20 mm',
            //                    right : '20 mm',
            //                    top : '20 mm'
            //                }
            //            },
            messages : {
                formatting : '포맷',
                formatBlock : '포맷을 선택하세요.',
                fontNameInherit : '폰트',
                fontName : '글자 폰트를 선택하세요.',
                fontSizeInherit : '글자크기',
                fontSize : '글자 크기를 선택하세요.',
                //------------------- 구분선 ----------------------
                print : '출력',
                //------------------- 구분선 ----------------------

                //------------------- 구분선 ----------------------
                imageWebAddress : '웹 주소',
                imageAltText : '대체 문구', //Alternate text
                //------------------- 구분선 ----------------------
                fileWebAddress : '웹 주소',
                fileTitle : '링크 문구',
                //------------------- 구분선 ----------------------
                linkWebAddress : '웹 주소',
                linkText : '선택 문구',
                linkToolTip : '풍선 도움말',
                linkOpenInNewWindow : '새 창에서 열기', //Open link in new window
                //------------------- 구분선 ----------------------
                dialogInsert : '적용',
                dialogUpdate : 'Update',
                dialogCancel : '닫기',
            //-----------------------------
            }
        });

        $('<br/>').insertAfter($('.k-i-create-table').closest('li'));

    }

    function inputFocus(){
    	$('#Title').focus();
    };

     /**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
				if(data.rows =="DUP_DATA"){
                    fnKendoMessage({
                        message : '중복입니다.'
                        , ok : inputFocus
                    });
				} else {
                    fnKendoMessage({
                        message : '저장되었습니다.'
                        , ok : function() { fnDetail(data) }
                    });
				}
				break;
			case 'update':
                fnKendoMessage({
                    message : '수정되었습니다.'
                    , ok : function() { fnDetail(data) }
                });
				break;
			case 'delete':
                fnKendoMessage({
                    message : '삭제되었습니다.'
                    , ok : function() { $('#fnList').trigger('click'); }
                });
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Clear*/
    $scope.fnClear =function(){	fnClear();};
    /** Common New*/
    $scope.fnNew = function( ){	fnNew();};
    /** Common Save*/
    $scope.fnSave = function(){	fnSave();};
    /** Common fnBefore*/
    $scope.fnBefore = function( ){	fnBefore();};
    /** Common List*/
    $scope.fnList = function( ){	fnList();};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

    fnInputValidationForNumber("popupCoordinateX");
    fnInputValidationForNumber("popupCoordinateY");

}); // document ready - END
