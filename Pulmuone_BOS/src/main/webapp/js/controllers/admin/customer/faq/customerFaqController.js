/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : FAQ 관리
 * @
 * @ 수정일 			수정자 			수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.01 	안치열 			최초생성 @
 ******************************************************************************/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var oldSort;
var workindEditorId;

var newFaqType;
var newFaqTitle;
var newDisplayYn;
var newContent;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'feedback',
			callback : fnUI
		});
	}


	function fnUI(){

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitOptionBox();// Initialize Option Box

		fnDefaultSet();

		fnSearch();


	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnNew, #fnShowImage, #fnRemove').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);

		if($('#companyStandardType').getRadioVal() == "Y"){
			if($('#findKeyword').val() == '' ){
				fnKendoMessage({ message : "검색 조건을 확인해 주세요."});
				return
			}
		}

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}

	function fnClear(){

		$('#searchForm').formClear(true);
		setDefaultDatePicker();

		// 단일조건, 복수조건 검색 제어
		if($("#companyStandardType_1").prop("checked")==true ){
			setDefaultDatePicker();
		}
//		else{
//			$('#createDateStart').val('');
//			$('#createDateEnd').val('');
//			fnDefaultSet();
//			fnSearch();
//		}


	}

	// 기본 설정
	function fnDefaultSet(){
	    $('#searchOneDiv').hide();
	    $('#searchManyDiv').show();
		$('#searchTypeDiv').show();
		$('#searchDateDiv').show();
		$("#companyStandardType_1").prop("checked",true);

		fnInputValidationForNumber("viewSort");

		setDefaultDatePicker();
	};

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})


//		$("button[data-id='fnDateBtn5']").attr("fb-btn-active", true);

//		var today = fnGetToday();

//		$("#createDateStart").val(fnGetDayMinus(today, 30));
//		$("#createDateEnd").data("kendoDatePicker").value(today);
	}


	// FAQ 등록 팝업 호출
	function fnNew(){

		$('#inputForm').formClear(true);

		fnKendoInputPoup({height:"auto" ,width:"600px", title:{ nullMsg :'FAQ 등록' } } );

		$('#' + 'content').data("kendoEditor").value('');

//		$("#faqType").data("kendoDropDownList").enable( true );
		$('#fnRemove').hide();
		$("#displayYn_0").prop("checked",true);

		newFaqType = $('#faqType').val();
		newFaqTitle = $('#faqTitle').val();
		newDisplayYn = $('#displayYn').val();
		newContent = $('#content').val();
	}


	function fnConfirm(){

		if( $("#faqType").val() == ''){
			fnKendoMessage({message:'분류를 선택해 주세요.'});
			return;
		}

		if( $("#faqTitle").val() == ''){
			fnKendoMessage({message:'제목은 필수로 입력해야 합니다. 제목을 입력해 주세요.'});
			return;
		}

		fnKendoMessage({message:'입력한 정보로 등록하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });
	}

	// 확인 후 수정사항 저장
	function fnSave(){

		var url = '/admin/customer/faq/addFaqInfo';
		var data = $('#inputForm').formSerialize(true);
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/customer/faq/putFaqInfo';
			cbId= 'update';
		}

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
				});
		}
	}

	function fnClose(){
		if(fnCheckData()){
			fnKendoMessage({message:'입력한 정보를 저장하지 않고 등록을 취소 하시겠습니까?', type : "confirm" ,ok : function(){ fnCancel() } });
		}else{
			fnCancel();
		}
	}

	function fnCheckData(){

		if(newFaqType != $('#faqType').val()){
			return true;
		}
		if(newFaqTitle != $('#faqTitle').val()){
			return true;
		}
		if(newDisplayYn != $('#displayYn').val()){
			return true;
		}
		if(newContent != $('#content').val()){
			return true;
		}
		return false;
	}

	// 팝업창 닫기
	function fnCancel(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}


	function fnRemove(){
		fnKendoMessage({message:'삭제한 내용은 복원할 수 없습니다. 삭제 하시겠습니까?', type : "confirm" ,ok : function(){ fnRemoveConfirm() } });
	}

	// 삭제
	function fnRemoveConfirm(){
		var url = '/admin/customer/faq/deleteFaqInfo';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);

		fnAjax({
			url     : url,
			params  : data,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'batch'
			});
	}



	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetEditPagingDataSource({
			url      : "/admin/customer/faq/getFaqList",
			pageSize : PAGE_SIZE,
			model_id : 'viewSort',
			model_fields : {
				faqTypeName : { editable : false, type: 'string' },
				faqTitle : { editable : false, type: 'string' },
				userName : { editable : false, type: 'string' },
				userId : { editable : false, type: 'string' },
				displayYn : { editable : false, type: 'string' },
				createDate : { editable : false, type: 'string' },
				viewCount : { editable : false, type: 'string' },
				viewSort : { editable : true, type: 'string', validation: {  maxLength : "3"
					, stringValidation: function (input) {
			            if (input.is("[name='viewSort']")) {
			                input.attr("data-stringValidation-msg", '숫자만 등록할 수 있습니다.');
				                if(!validateFormat.typeNotNumber.test(input.val()) == false){
				                	return !validateFormat.typeNotNumber.test(input.val());
				                }else{
				                	if(input.val() != ""){
				                		var aMap = aGrid.dataItem(aGrid.select());
				                		fnSaveSort(aMap, input.val());
				                	}else{
				                		fnSearch();
				                	}
				                }
		            		}
					    return true;
						}
					}

				}

			},

		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,editable:true
			,columns   : [
				 { title : 'No'	, width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'faqTypeName'	,title : '분류',width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'faqTitle'	,title : '제목'	, width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
				,{ field:'userName'			,title : '등록자'		, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'userId'			,title : '등록자 ID'	, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'displayYn'			,title : '노출여부'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'			,title : '등록일자'	, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'viewCount'			,title : '조회'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'viewSort'		,title : '노출순서'		, width:'70px',attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }}
				,{ field:'faqId', hidden:true}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			if(index == 2){
				fnGridClick();
			}
			if(index == 8){
				fnSortGridClick();
			}
		});

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#totalCnt').text(aGridDs._total);
        });

	}

	function fnSaveSort(aMap,input){

		var url  = '/admin/customer/faq/putFaqInfo';
		var cbId= 'update';

		if(oldSort != input){
			fnAjax({
				url     : url,
				params  : {faqId : aMap.faqId, viewSort : input},
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
			});
		}

	}

	// FAQ 관리 상세화면 호출
	function fnGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/customer/faq/getDetailFaq',
			params  : {faqId : aMap.faqId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};

	function fnSortGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		oldSort = aMap.viewSort;
		$('#inputForm').bindingForm( {'row':aMap},'rows', true);
	};


	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});



		// 단일/복수조건 검색 구분
		fnTagMkRadio({
			id    :  'companyStandardType',
			tagId : 'companyStandardType',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "Y"	, "NAME":'단일조건 검색' },
						{ "CODE" : "N"	, "NAME":'복수조건 검색' }
					],
			style : {},
			change : function(e){
				if($('#companyStandardType').getRadioVal() == "Y"){
					$('#searchForm').formClear(true);
					$('#searchOneDiv').show();
					$('#searchManyDiv').hide();
					$('#searchTypeDiv').hide();
					$('#searchDateDiv').hide();
					$("#companyStandardType_0").prop("checked",true);

					$('#createDateStart').val('');
					$('#createDateEnd').val('');

					$('#aGrid').gridClear(true);

				}else if($('#companyStandardType').getRadioVal() == "N"){
					$('#searchForm').formClear(true);
					fnDefaultSet();
					$('#searchOneDiv').hide();
					$('#searchManyDiv').show();
					$('#searchTypeDiv').show();
					$('#searchDateDiv').show();
					$("#companyStandardType_1").prop("checked",true);

					$('#aGrid').gridClear(true);
					fnSearch();
				}
			}
		});


		// 후기단일조건
		fnKendoDropDownList({
			id    : 'searchSelect',
			data  : [{"CODE":"SEARCH_SELECT.CREATE_USER","NAME":'등록자명'}
					,{"CODE":"SEARCH_SELECT.CREATE_ID","NAME":'등록자ID'}
					]
		});


		// 후기복수조건
		fnKendoDropDownList({
			id    : 'searchManySelect',
			data  : [{"CODE":"SEARCH_SELECT.TITLE","NAME":'제목'}
					,{"CODE":"SEARCH_SELECT.CONTENT","NAME":'내용'}
					,{"CODE":"SEARCH_SELECT.TITLE_CONTENT","NAME":'제목+내용'}
					]
		});


		// FAQ 분류 유형
		fnKendoDropDownList({
			id  : 'searchFaqSelect',
			tagId : 'searchFaqSelect',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "FAQ_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "",
			blank : "전체"
		});

		// FAQ 분류 유형(Popup)
		fnKendoDropDownList({
			id  : 'faqType',
			tagId : 'faqType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "FAQ_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "FAQ_TP.MEMBER",
			blank : "선택"
		});

		// FAQ 정보 노출여부
		fnTagMkRadio({
			id : 'displayYn',
			tagId : 'displayYn',
			async : false,
			style : {},
			data  : [
				{ "CODE" : "Y"	, "NAME":'노출' },
				{ "CODE" : "N"	, "NAME":'비노출' }
			]


		});


		// 등록일 시작
        fnKendoDatePicker({
            id: "createDateStart",
            format: "yyyy-MM-dd",
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
//            defVal: fnGetDayMinus(fnGetToday(),30),
            defType : 'oneMonth'
//            change : function(e) {
//                fnStartCalChange("createDateStart", "createDateEnd");
//            }
        });

        // 등록일 종료
        fnKendoDatePicker({
            id: "createDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
//            defVal: fnGetToday(),
            defType : 'oneMonth'
//            change : function(e) {
//                fnEndCalChange("createDateStart", "createDateEnd");
//            }
        });



        fnItemDescriptionKendoEditor({ // FAQ 기본정보 Editor
            id : 'content',
        });

        fnKendoUpload({ // FAQ 상세 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
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
	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------

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

function fnItemDescriptionKendoEditor(opt) { // FAQ 상세 기본정보 / 주요 정보 Editor

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
        ],
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


	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':

				fnKendoInputPoup({height:"auto" ,width:"800px",title:{ nullMsg : 'FAQ 수정'} });

				$('#fnRemove').show();
//				$("#faqType").data("kendoDropDownList").enable( false );
				$('#kendoPopup').scrollTop(0);

				$('#inputForm').bindingForm(data, "row", true);

				break;

			case 'insert':
			case 'update':
				fnKendoMessage({
					message:"등록이 완료되었습니다.",
					ok:function(){
						fnSearch();
						fnCancel();
					}
			});
				break;

			case 'delete':
				fnKendoMessage({
					message:"삭제 되었습니다.",
					ok:function(){
						fnSearch();
						fnCancel();
					}
			});
				break;
		}
	}
	// ------------------------------- Common Function end
	// -------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
	/** Common ExcelDownload */
	$scope.fnNew = function( ){	fnNew();};
	/** Common Confirm */
	$scope.fnConfirm = function(){	 fnConfirm();};
	/** Common Close */
	$scope.fnClose = function( ){  fnClose();};
	/** Common Remove */
	$scope.fnRemove = function( ){  fnRemove();};

	/** Common ShowImage */
	$scope.fnShowImage = function(url){ fnShowImage(url);};


	$("#clear").click(function(){
	      $(".resultingarticles").empty();
	      $("#searchbox").val("");
    });



	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
