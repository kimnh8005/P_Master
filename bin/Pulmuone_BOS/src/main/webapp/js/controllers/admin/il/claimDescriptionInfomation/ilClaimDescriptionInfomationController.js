﻿/**-----------------------------------------------------------------------------
 * description 		 : 배송/반품/취소안내
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.27		박영후          최초생성
 * @
 * **/
'use strict';


var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {

	var workindEditorId; // 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'ilClaimDescriptionInfomation',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		//fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSave,  #fnClear').kendoButton();
		//$('#fnDel').kendoButton({ enable: false });
	}

	function fnSave(){
		var url  = '/admin/goods/claiminfo/putClaimInfo';
		var cbId= 'update';

		var data = $('#inputForm').formSerialize(true);

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
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url      : '/admin/goods/claiminfo/getClaimInfoList'
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
	        ,scrollable: false
	        ,autoBind: true
			,columns   : [
				 { field:'itemTypeName'			,title : '품목유형'		, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsTypeName'		,title : '상품유형'		, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'templateName'			,title : '템플릿명'		, width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'useYnName'			,title : '사용여부'		, width:'30px'	,attributes:{ style:'text-align:center' }}
				,{ command: [{ text: "수정", click: fnGetRow, visible: function() { return fnIsProgramAuth("SAVE")} }]
					, title : '관리', width:'66px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
		    //,change: fnGridRowLick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._data.length);
        });
	}


	function fnGetRow(e) {
		e.preventDefault();

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		fnAjax({
			url     : '/admin/goods/claiminfo/getClaimInfo',
			params  : {ilClaimInfoId : dataItem.ilClaimInfoId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	}


	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Input Mask Start ------------------------------------------------
	function fnInitMaskTextBox() {
		fnInputValidationForNumber("sort");
		fnInputValidationForHangulAlphabetNumber("templateName");
	}
	//---------------Initialize Input Mask End ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		// 사용여부
		fnTagMkRadioYN({
			id: "useYn"
			, tagId : "useYn"
			, chkVal: 'Y'
			, change: function (e) {
				changeDescribeOption(e.target.defaultValue);
			}
		});

		fnDescriptionKendoEditor({ // Editor
            id : 'describe',
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

		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);
				$("#itemTypeName").html(data.rows.itemTypeName);
				$("#goodsTypeName").html(data.rows.goodsTypeName);

				changeDescribeOption(data.rows.useYn);

				fnKendoInputPoup({height:"630px" ,width:"1200px",title:{nullMsg :'배송/반품/취소안내 수정'} });

				break;
			case 'update':
				aGridDs.read();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;

		}
	}


	function fnUploadImageOfEditor(opt) { // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가

        var formData = $('#uploadImageOfEditorForm').formSerialize(true);

        fnAjaxSubmit({
            form : "uploadImageOfEditorForm",
            fileUrl : "/fileUpload",
            method : 'GET',
            url : '/comn/getPublicStorageUrl',
            storageType : "public",
            domain : "il",
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


	function fnDescriptionKendoEditor(opt) { // Editor

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
            //          pdf : {
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
	}

	function changeDescribeOption(useYn) {
		if (useYn == 'Y') {
			$("#describe").prop("required", true);
			$("label[for='describe']").addClass("req-star-front");
		} else {
			$("#describe").prop("required", false);
			$("label[for='describe']").removeClass("req-star-front");
		}
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	//$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	//$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	//$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	//$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
