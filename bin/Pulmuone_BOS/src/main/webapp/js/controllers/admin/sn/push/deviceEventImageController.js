/**-----------------------------------------------------------------------------
 * system 			 : APP 설치 단말기 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.26		jg          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'deviceEventImage',
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();
		fnInitOptionBox();
		fnInitGrid();
		fnSetGrid();
		fnViewInit();

	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$('#fnSave, #fnImageUpload').kendoButton();
	};


	function fnImageUpload(){
        $("#imageFile").trigger("click");
    }

    function fnViewInit(){
    	$('#iosView').show();
		$('#androidView').hide();
    }

    function fnGridCnage(){
    	var grid = $('#aGrid').data("kendoGrid");
    	var count = grid.dataSource.total();
    }

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------

	// 그리드 초기화
	function fnInitGrid(){

//		aGridDs = fnGetDataSource({
//			url		: '/admin/ur/userDevice/getDeviceEventImage'
//		});

		aGridDs = new kendo.data.DataSource();

		aGridOpt = {
			dataSource: aGridDs
			,columns   : [
				 { field:'deviceTypeName',title : '플랫폼 유형' , width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'useYn'		,title : '사용여부'	 , width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'envVal', hidden:true}
				,{ field:'imageUrl', hidden:true}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();



		$(aGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);

			fnGridClick(e);

		});
		//------------------------------- 왼쪽그리드 E -------------------------------
	};


	function fnGridClick(e){
		var dataItem = aGrid.dataItem($(e.target).closest('tr'));

		if(dataItem.envVal == 'N'){
			$('#useYn_0').prop("checked",true);
		}else{
			$('#useYn_1').prop("checked",true);
		}

		if(dataItem.deviceTypeName == 'iOS'){
			$('#iosView').show();
			$('#androidView').hide();
		}else{
			$('#iosView').hide();
			$('#androidView').show();
		}
		$('#deviceType').val(dataItem.deviceTypeName);
		$("#basicImage").attr("src", dataItem.imageUrl);

	}
	//-------------------------------  Grid End  -------------------------------

	//-------------------------------  Common Function start -------------------------------

	function fnInitOptionBox(){

        fnTagMkRadio({
            id    : 'useYn'
          , tagId : 'useYn'
          , data  : [
        	  		   {"CODE" : "N", "NAME" : '사용안함' }
                     , {"CODE" : "Y", "NAME" : '사용'    }
                    ]
          , chkVal: ""
          , style : {}
      });
	};

	// 콜백
	function fnBizCallback( id, data ){

		  switch (id) {
			case 'insert':
				fnKendoMessage({
                      message:"저장되었습니다."
                      , ok : function() {
                    	  location.reload();
                             }
				});
			    break;
      }
	};


    fnKendoUpload({
        id : "imageFile",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length);

            if ($.inArray(ext.toLowerCase(), [ "jpg", "jpeg", "png", "gif" ]) == -1 ){
                fnKendoMessage({
                	message : "Jpg jpeg png gif 파일만 첨부가능합니다."
                });
                e.preventDefault();
                return false;
          } else {
        	  if(f.size <= 10485760){
                if (e.files && e.files[0]) {
                    let reader = new FileReader();

                    reader.onload = function(ele) {
                    	$('#basicImage').attr('src', ele.target.result);
                    };
                    reader.readAsDataURL(f.rawFile);
                }
        	  } else {
          		fnKendoMessage({
                      message : "이미지는 10MB까지 첨부할 수 있습니다."
					});
					e.preventDefault();
          	}
            }
        },
        localization : "파일첨부"
    });


    function fnSave(){

		 $('#rootPath').val(fnGetPublicStorageUrl());

		 if($('#deviceType').val() == ''){
			 $('#deviceType').val('iOS');
		 }

    	 let data = $('#inputForm').formSerialize(true);

         var cbId = 'insert';

         var url = '';
         url = '/admin/ur/userDevice/setDeviceEventImage';

         let imageFile = $('#basicImage').attr("src");

//         if(imageFile.length < 1 || !imageFile.includes("http")){
         if(imageFile.length < 1 ){
        	 fnKendoMessage({message:'이벤트 이미지를 등록해주세요.'});
			return;
         }

         if( data.rtnValid ){
 	        fnAjaxSubmit({
 	            form : 'inputForm',
 	            fileUrl : '/fileUpload',
 	            storageType : "public", // 추가
 	            domain : "sn", // 추가
 	            url : url,
 	            params : data,
 	            success : function(successData) {
 	                fnBizCallback(cbId , successData);
 	            },
 	            isAction : 'batch'
 	        });
         }

    };

    function fnSetGrid(){

    	fnAjax({
			url     : '/admin/ur/userDevice/getDeviceEventImage',
			params  : '',
			success :
				function( data ){
					let imageFile;
	            	for(var i=0 ; i<data.rows.length; i++){

	            		var obj = new Object();

	            		obj["deviceTypeName"] = data.rows[i].deviceTypeName;
	            		obj["useYn"] = data.rows[i].useYn;
	            		obj["envVal"] = data.rows[i].envVal;

	            		imageFile = data.rows[i].imageUrl;
	            		if(imageFile.length < 1 || !imageFile.includes("http")){
	            			imageFile = "/contents/images/noimg.png";
	            		}
	            		if(i==0){
	            			$("#basicImage").attr("src", imageFile);
	            		}

	            		obj["imageUrl"] = imageFile;
	            		aGridDs.add(obj);
	            	}


	            	if(data.rows[0].envVal == 'N'){
	            		$('#useYn_0').prop("checked",true);
	            	}else{
	            		$('#useYn_1').prop("checked",true);
	            	}

				}
		});
    }



	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    $scope.fnSave = function() { fnSave(); }; /* Common Save */
	$scope.fnImageUpload =function() { fnImageUpload(); }; /* Common Clear */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END