(function(e){function t(t){for(var i,n,o=t[0],l=t[1],c=t[2],u=0,p=[];u<o.length;u++)n=o[u],Object.prototype.hasOwnProperty.call(a,n)&&a[n]&&p.push(a[n][0]),a[n]=0;for(i in l)Object.prototype.hasOwnProperty.call(l,i)&&(e[i]=l[i]);d&&d(t);while(p.length)p.shift()();return r.push.apply(r,c||[]),s()}function s(){for(var e,t=0;t<r.length;t++){for(var s=r[t],i=!0,o=1;o<s.length;o++){var l=s[o];0!==a[l]&&(i=!1)}i&&(r.splice(t--,1),e=n(n.s=s[0]))}return e}var i={},a={"mobile/mypage/shipping/index":0},r=[];function n(t){if(i[t])return i[t].exports;var s=i[t]={i:t,l:!1,exports:{}};return e[t].call(s.exports,s,s.exports,n),s.l=!0,s.exports}n.m=e,n.c=i,n.d=function(e,t,s){n.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:s})},n.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},n.t=function(e,t){if(1&t&&(e=n(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var s=Object.create(null);if(n.r(s),Object.defineProperty(s,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var i in e)n.d(s,i,function(t){return e[t]}.bind(null,i));return s},n.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return n.d(t,"a",t),t},n.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},n.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],l=o.push.bind(o);o.push=t,o=o.slice();for(var c=0;c<o.length;c++)t(o[c]);var d=l;r.push([75,"chunk-commons"]),s()})({"60a4":function(e,t,s){},75:function(e,t,s){e.exports=s("ddb6")},"975c":function(e,t,s){},bf3e:function(e,t,s){"use strict";var i=s("60a4"),a=s.n(i);a.a},ddb6:function(e,t,s){"use strict";s.r(t);var i=s("5530"),a=(s("e260"),s("e6cf"),s("cca6"),s("a79d"),function(){var e=this,t=e.$createElement,s=e._self._c||t;return e.userSession.isLogin?s("main",{staticClass:"fb__mypage "},[s("fb-header",{attrs:{type:"sub",title:e.pageTitle,buttons:["back","home","setting"],actions:["back"]},on:{"handle-back":e.pageBack}}),s("div",{staticClass:"shipping"},[e.loading.list?[e.shippinglayer.open?[e.shippinglayer.open?s("shipping-white",{attrs:{open:e.shippinglayer.open,type:e.shippinglayer.type,datas:e.shippinglayer.data},on:{"close-modal":e.closeChangeDate}}):e._e()]:[s("section",{staticClass:"shipping__basic"},[s("h3",{staticClass:"shipping__title"},[e._v("기본배송지")]),e._l(e.data.basicList,(function(t,i){return s("div",{key:"basicList-"+i,staticClass:"shipping__list"},[s("p",{staticClass:"shipping__info"},[s("span",{staticClass:"shipping__name"},[e._v(e._s(t.receiverName))]),s("button",{attrs:{type:"button"},on:{click:function(s){return e.shippingWhite(t,"push")}}},[e._v("변경")])]),t.delivery?s("ul",{staticClass:"shipping__delivery"},[t.delivery.storeDelivery?s("li",{staticClass:"store"},[e._v(" 매장 배송 ("+e._s(t.delivery.storeName.join(", "))+") ")]):e._e(),t.delivery.dawnDelivery?s("li",{staticClass:"dawn"},[e._v(" 새벽배송 ")]):e._e(),t.delivery.shippingCompDelivery?s("li",{staticClass:"delivery"},[e._v(" 택배배송 ")]):e._e(),t.delivery.dailyDelivery?s("li",{staticClass:"daily"},[e._v(" 일일배송 "),t.delivery.dailyDeliveryType==e.$FB_CODES.CART.DAILY_DELIVERY_TYPES.ONLY_GREEN_JUICE?s("em",[e._v("(녹즙)")]):e._e()]):e._e()]):e._e(),s("p",{staticClass:"shipping__zipcode"},[e._v(" ["+e._s(t.receiverZipCode)+"] "+e._s(t.receiverAddress1)+", "+e._s(t.receiverAddress2)+" ")]),s("p",{staticClass:"shipping__phone"},[e._v(" "+e._s(e._f("phone")(t.receiverMobile))+" ")]),t.shippingComment?s("p",{staticClass:"shipping__request"},[s("span",[e._v("배송 요청사항")]),e._v(" "+e._s(t.shippingComment)+" ")]):e._e(),t.accessInformationType?s("p",{staticClass:"shipping__access"},[s("span",[e._v("배송 출입정보")]),e._v(" "+e._s(t.accessInformationName)+" "),t.accessInformationPassword&&t.accessInformationPassword.length>0?[e._v(" : "+e._s(t.accessInformationPassword)+" ")]:e._e()],2):e._e()])}))],2),s("section",{staticClass:"shipping__content"},[s("header",{staticClass:"shipping__header"},[s("h3",{staticClass:"shipping__title"},[e._v("배송지 목록")]),s("button",{staticClass:"shipping__add",attrs:{type:"button"},on:{click:function(t){return e.shippingWhite({},"add")}}},[e._v("배송지 추가")])]),e.data.contentList.length>0?[s("ul",e._l(e.data.contentList,(function(t,i){return s("li",{key:"contentList-"+i,staticClass:"shipping__box"},[s("div",{staticClass:"shipping__list"},[s("p",{staticClass:"shipping__info"},[s("span",{staticClass:"shipping__name"},[e._v(e._s(t.receiverName))]),"Y"!=t.defaultYn?s("button",{attrs:{type:"button"},on:{click:function(s){return e.del(t.urShippingAddrId)}}},[e._v("삭제")]):e._e()]),t.delivery?s("ul",{staticClass:"shipping__delivery"},[t.delivery.storeDelivery?s("li",{staticClass:"store"},[e._v(" 매장 배송 ("+e._s(t.delivery.storeName.join(", "))+") ")]):e._e(),t.delivery.dawnDelivery?s("li",{staticClass:"dawn"},[e._v(" 새벽배송 ")]):e._e(),t.delivery.shippingCompDelivery?s("li",{staticClass:"delivery"},[e._v(" 택배배송 ")]):e._e(),t.delivery.dailyDelivery?s("li",{staticClass:"daily"},[e._v(" 일일배송 "),t.delivery.dailyDeliveryType==e.$FB_CODES.CART.DAILY_DELIVERY_TYPES.ONLY_GREEN_JUICE?s("em",[e._v("(녹즙)")]):e._e()]):e._e()]):e._e(),s("p",{staticClass:"shipping__zipcode"},[e._v(" ["+e._s(t.receiverZipCode)+"] "+e._s(t.receiverAddress1)+", "+e._s(t.receiverAddress2)+" ")]),s("p",{staticClass:"shipping__phone"},[e._v(" "+e._s(e._f("phone")(t.receiverMobile))+" ")]),t.shippingComment?s("p",{staticClass:"shipping__request"},[s("span",[e._v("배송 요청사항")]),e._v(" "+e._s(t.shippingComment)+" ")]):e._e(),t.accessInformationType?s("p",{staticClass:"shipping__access"},[s("span",[e._v("배송 출입정보")]),e._v(" "+e._s(t.accessInformationName)+" "),t.accessInformationPassword&&t.accessInformationPassword.length>0?[e._v(" : "+e._s(t.accessInformationPassword)+" ")]:e._e()],2):e._e(),s("div",{staticClass:"shipping__btn"},["Y"!=t.defaultYn?s("button",{staticClass:"shipping__btn__basic",attrs:{type:"button"},on:{click:function(s){return e.shippingListBasic(t.urShippingAddrId)}}},[e._v(" 기본 배송지로 설정 ")]):e._e(),s("button",{staticClass:"shipping__btn__change",attrs:{type:"button"},on:{click:function(s){return e.shippingWhite(t,"push")}}},[e._v(" 변경 ")])])])])})),0),e.pagination.goodsCount>e.pagination.totalCount?s("infinite-loading",{ref:"infiniteLoading",on:{infinite:e.infiniteHandler}},[s("div",{attrs:{slot:"no-more"},slot:"no-more"}),s("div",{attrs:{slot:"no-results"},slot:"no-results"}),s("div",{attrs:{slot:"error"},slot:"error"})]):e._e()]:[e._m(0)]],2)]]:"error"==e.loading.list?void 0:e._e(),s("fb-confirm",{attrs:{open:e.confirm.open,title:e.confirm.title,message:e.confirm.message,ok:e.confirm.ok},on:{"close-confirm":e.closeConfirm}}),s("fb-alert",{attrs:{open:e.alert.open,message:e.alert.message},on:{"close-alert":e.closeAlert}})],2),s("fb-footer"),s("fb-dockbar")],1):e._e()}),r=[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"shipping__inner"},[s("div",{staticClass:"shipping__noContent"},[s("div",{staticClass:"noContent"},[s("p",{staticClass:"noContent__title"},[e._v(" 등록된 배송지가 없습니다 ")])])])])}],n=(s("4160"),s("caad"),s("d3b7"),s("ac1f"),s("2532"),s("3ca3"),s("5319"),s("841c"),s("159b"),s("ddb0"),s("2b3d"),s("96cf"),s("1da1")),o=s("8e32"),l=s("629b"),c=s("cbaa"),d=s("face"),u=s("2479"),p=s("5f72"),_=s("0bf4"),v=function(){var e=this,t=e.$createElement,s=e._self._c||t;return e.open&&e.layerOpen?s("section",{staticClass:"shipping__white"},[s("form",{on:{submit:function(t){return t.preventDefault(),e.onSubmit.apply(null,arguments)}}},[s("div",{staticClass:"white"},[s("div",{staticClass:"white__inner"},[s("ul",{staticClass:"white__box"},[s("li",{staticClass:"white__list white__name fb__input-text"},[e._m(0),s("div",{staticClass:"fb__input-text__inner"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.name,expression:"request.name"}],ref:"name",attrs:{type:"text",placeholder:" "},domProps:{value:e.request.name},on:{input:[function(t){t.target.composing||e.$set(e.request,"name",t.target.value)},function(t){return e.maxTextLength(t,"name",20)}]}}),s("button",{staticClass:"fb__input-text__clear",attrs:{type:"button",tabindex:"-1"},on:{click:function(t){e.request.name=""}}},[e._v("지우기")])]),e.validator.name?s("p",{staticClass:"white__validator"},[e._v(e._s(e.validator.name))]):e._e()]),s("li",{staticClass:"white__list"},[e._m(1),s("div",{staticClass:"white__phone fb__input-text"},[s("div",{staticClass:"white__phone__agency"},[s("fb-select-box",{attrs:{classes:"full",rows:e.phonePrefixList},scopedSlots:e._u([{key:"option",fn:function(t){var i=t.row;return[s("label",[s("input",{directives:[{name:"model",rawName:"v-model",value:e.phonePrefix,expression:"phonePrefix"}],staticClass:"blind",attrs:{type:"radio",name:"select-box"},domProps:{value:i.value,checked:e._q(e.phonePrefix,i.value)},on:{change:function(t){e.phonePrefix=i.value}}}),s("span",[e._v(e._s(i.name))])])]}}],null,!1,3919062916)},[e._v(" "+e._s(e.phonePrefix)+" ")])],1),s("div",{staticClass:"white__phone__number fb__input-text__inner"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.phoneNumber,expression:"request.phoneNumber"}],ref:"phone",attrs:{type:"tel",placeholder:"-없이 입력해주세요",maxlength:"8"},domProps:{value:e.request.phoneNumber},on:{input:[function(t){t.target.composing||e.$set(e.request,"phoneNumber",t.target.value)},function(t){return e.phoneInputEvent(t)}]}}),s("button",{staticClass:"fb__input-text__clear",attrs:{type:"button",tabindex:"-1"},on:{click:function(t){e.request.phoneNumber=""}}})])]),e.validator.phone?s("p",{staticClass:"white__validator"},[e._v(e._s(e.validator.phone))]):e._e()]),s("li",{staticClass:"white__list "},[e._m(2),s("div",{staticClass:"zipcode"},[s("div",{staticClass:"zipcode__code"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.zipCode,expression:"request.zipCode"}],class:{blur:!e.request.zipCode},attrs:{type:"text",disabled:""},domProps:{value:e.request.zipCode},on:{input:function(t){t.target.composing||e.$set(e.request,"zipCode",t.target.value)}}}),s("button",{attrs:{type:"button"},on:{click:function(t){return e.findAddress(t)}}},[e._v("주소찾기")])]),s("div",{staticClass:"zipcode__add"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.zipCodeAdd,expression:"request.zipCodeAdd"}],class:{blur:!e.request.zipCodeAdd},attrs:{type:"text",disabled:""},domProps:{value:e.request.zipCodeAdd},on:{input:function(t){t.target.composing||e.$set(e.request,"zipCodeAdd",t.target.value)}}})]),s("div",{staticClass:"zipcode__deep"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.zipCodeDeep,expression:"request.zipCodeDeep"}],ref:"zipcode",attrs:{type:"text"},domProps:{value:e.request.zipCodeDeep},on:{input:[function(t){t.target.composing||e.$set(e.request,"zipCodeDeep",t.target.value)},function(t){return e.maxTextLength(t,"zipCodeDeep",30)}]}})])]),e.validator.zipcode?s("p",{staticClass:"white__validator"},[e._v(e._s(e.validator.zipcode))]):e._e(),e.addressAvailableType?s("div",{staticClass:"zipcode__store"},[s("span",{staticClass:"zipcode__store__title"},[e._v("가능한 배송유형 안내")]),s("ul",{staticClass:"shipping__delivery"},[e.addressAvailableType.storeDelivery?s("li",{staticClass:"store"},[e._v(" 매장 배송 ("+e._s(e.addressAvailableType.storeName.join(", "))+") ")]):e._e(),e.addressAvailableType.dawnDelivery?s("li",{staticClass:"dawn"},[e._v(" 새벽배송 ")]):e._e(),e.addressAvailableType.shippingCompDelivery?s("li",{staticClass:"delivery"},[e._v(" 택배배송 ")]):e._e(),e.addressAvailableType.dailyDelivery?s("li",{staticClass:"daily"},[e._v(" 일일배송 "),e.addressAvailableType.dailyDeliveryType==e.$FB_CODES.CART.DAILY_DELIVERY_TYPES.ONLY_GREEN_JUICE?s("em",[e._v("(녹즙)")]):e._e()]):e._e()])]):e._e()]),s("li",{staticClass:"white__list"},[s("p",{staticClass:"white__title"},[e._v(" 배송 요청사항 ")]),s("div",{staticClass:"request"},[s("div",{staticClass:"request__select"},[s("fb-select-box",{attrs:{classes:"full",rows:e.deliveryMessage},scopedSlots:e._u([{key:"option",fn:function(t){var i=t.row;return[s("label",[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.deliveryRequest,expression:"request.deliveryRequest"}],staticClass:"blind",attrs:{type:"radio",name:"select-box"},domProps:{value:i,checked:e._q(e.request.deliveryRequest,i)},on:{change:function(t){return e.$set(e.request,"deliveryRequest",i)}}}),s("span",[e._v(e._s(i))])])]}}],null,!1,3445901814)},[e._v(" "+e._s(null==e.request.deliveryRequest?e.deliveryMessage[0]:e.request.deliveryRequest)+" ")])],1),"직접입력"==e.request.deliveryRequest?s("div",{staticClass:"request__textarea fb__textarea"},[s("div",{staticClass:"fb__textarea__inner"},[s("textarea",{directives:[{name:"model",rawName:"v-model",value:e.request.deliveryTextarea,expression:"request.deliveryTextarea"}],ref:"deliveryMessage",attrs:{placeholder:"내용을 입력해주세요",targetType:"deliveryMessage"},domProps:{value:e.request.deliveryTextarea},on:{input:[function(t){t.target.composing||e.$set(e.request,"deliveryTextarea",t.target.value)},e.checkComment],keydown:function(t){if(!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;t.preventDefault()}}}),s("span",{staticClass:"fb__textarea__count"},[e._v(e._s(e.deliveryMessageCurrentSize)+"/"+e._s(e.deliveryMessageMaxSize))])])]):e._e(),e.validator.delivery?s("p",{staticClass:"fb__required__error"},[e._v(e._s(e.validator.delivery))]):e._e(),e.validator.deliveryMessage?s("p",{staticClass:"fb__required__error"},[e._v(e._s(e.validator.deliveryMessage))]):e._e()])]),s("li",{staticClass:"white__list"},[e._m(3),e.validator.accessCode?s("p",{staticClass:"fb__required__error"},[e._v(e._s(e.validator.accessCode))]):e._e(),s("div",{staticClass:"access__info"},[s("ul",{staticClass:"access__info__list"},e._l(e.accessList,(function(t,i){return s("li",{key:"accessList-"+i},[s("label",[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.accessCode,expression:"request.accessCode"}],attrs:{type:"radio",name:"accessInfo"},domProps:{value:t.code,checked:e._q(e.request.accessCode,t.code)},on:{change:function(s){return e.$set(e.request,"accessCode",t.code)}}}),s("span",[e._v(" "+e._s(t.name)+" ")])]),["ACCESS_INFORMATION.FRONT_DOOR_PASSWORD","ACCESS_INFORMATION.ETC"].includes(e.request.accessCode)&&e.request.accessCode==t.code?s("div",{staticClass:"access__info__pw fb__input-text"},[s("div",{staticClass:"fb__input-text__inner"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.password,expression:"request.password"}],ref:"accessPw",refInFor:!0,attrs:{type:"text",targetType:"accessPw"},domProps:{value:e.request.password},on:{input:[function(t){t.target.composing||e.$set(e.request,"password",t.target.value)},e.checkComment],keydown:function(t){if(!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter"))return null;t.preventDefault()}}}),s("button",{staticClass:"fb__input-text__clear",attrs:{type:"button",tabindex:"-1"},on:{click:function(t){e.request.password=null}}})]),s("p",{staticClass:"fb__input-text__noti"},[e._v("공동현관 비밀번호는 배송을 위한 출입 목적으로만 사용됩니다.")]),e.validator.access?s("p",{staticClass:"fb__required__error"},[e._v(e._s(e.validator.access))]):e._e()]):e._e()])})),0)])])])]),s("div",{staticClass:"white__set"},["Y"!=e.datas.defaultYn?s("ul",{staticClass:"white__inner"},[s("li",[s("label",[s("input",{directives:[{name:"model",rawName:"v-model",value:e.request.zipCodeBasic,expression:"request.zipCodeBasic"}],attrs:{type:"checkbox"},domProps:{checked:Array.isArray(e.request.zipCodeBasic)?e._i(e.request.zipCodeBasic,null)>-1:e.request.zipCodeBasic},on:{change:function(t){var s=e.request.zipCodeBasic,i=t.target,a=!!i.checked;if(Array.isArray(s)){var r=null,n=e._i(s,r);i.checked?n<0&&e.$set(e.request,"zipCodeBasic",s.concat([r])):n>-1&&e.$set(e.request,"zipCodeBasic",s.slice(0,n).concat(s.slice(n+1)))}else e.$set(e.request,"zipCodeBasic",a)}}}),s("span",[e._v("기본 배송지로 설정")])])])]):e._e()]),s("div",{staticClass:"white__inner"},[s("div",{staticClass:"white__btn"},[s("button",{staticClass:"white__btn__cancel",attrs:{type:"button"},on:{click:e.cancel}},[e._v("취소")]),s("button",{staticClass:"white__btn__add",attrs:{type:"submuit"}},[e._v("저장")])])])])]),s("fb-alert",{attrs:{message:e.alert.message,open:e.alert.open},on:{"close-alert":function(t){return e.closeAlert(t)}}}),s("fb-confirm",{attrs:{open:e.confirm.open,title:e.confirm.title,message:e.confirm.message,ok:e.confirm.ok},on:{"close-confirm":e.closeConfirm}}),s("fb-modal",{attrs:{classes:e.modals.daumPost.classes,width:e.modals.daumPost.width,height:e.modals.daumPost.height,open:e.modals.daumPost.open},on:{"close-modal":function(t){return e.closeModal(t,"daumPost")}},scopedSlots:e._u([{key:"default",fn:function(){return[s("header",[s("h2",[e._v("주소검색")])]),s("div",{ref:"daum-post-api-wrapper",staticClass:"daum-post",class:e.modals.daumPost.open?"open":null})]},proxy:!0}],null,!1,2070964049)})],1):e._e()},h=[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",{staticClass:"white__title"},[e._v(" 받는분 "),s("span",[e._v("(필수) ")])])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",{staticClass:"white__title"},[e._v(" 휴대폰번호 "),s("span",[e._v("(필수) ")])])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",{staticClass:"white__title"},[e._v(" 주소 "),s("span",[e._v("(필수) ")])])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("p",{staticClass:"white__title"},[e._v(" 출입 정보 "),s("span",[e._v("(필수) ")])])}],f=(s("7db0"),s("b0c0"),s("4fad"),s("498a"),s("3835")),m=s("533e"),g=s("4646"),C={name:"shipping-white",mixins:[g["a"]],components:{fbSelectBox:m["a"],fbAlert:d["a"],fbConfirm:p["a"],fbModal:u["a"]},props:{open:{type:Boolean,default:!1},type:{type:String,default:"add"},datas:{type:Object,default:function(){return{}}}},data:function(){return{layerOpen:!0,alert:{message:null,open:!1},deliveryMessage:["배송 요청사항 선택","부재 시 경비실에 맡겨주세요.","부재 시 문 앞에 놓아주세요.","부재 시 휴대폰으로 연락바랍니다.","배송 전 연락바랍니다.","직접입력"],modals:{daumPost:{classes:"daum-post-modal",width:"100%",height:"100%",isCloseButton:!0,open:!1,model:null}},validator:{name:!1,phone:!1,zipcode:!1,delivery:!1,access:!1,accessCode:!1,deliveryMessage:!1},request:{urShippingAddrId:null,name:null,phoneNumber:null,zipCode:null,zipCodeAdd:null,zipCodeDeep:null,deliveryRequest:null,accessCode:null,zipCodeBasic:!1,addShippingAddress:!1,buildingCode:"",password:null,deliveryTextarea:"",accessCodeOrder:""},addressAvailableType:null,phonePrefix:this.$APP_CONFIG.MOBILE_CARRIERS[0].value,phonePrefixList:this.$APP_CONFIG.MOBILE_CARRIERS,accessList:[],deliveryMessageMaxSize:50,deliveryMessageCurrentSize:0,accessEtcMaxSize:30,accessEtcCurrentSize:0,accessPwMaxSize:30,accessPwCurrentSize:0}},created:function(){this.$store.dispatch("script/import",this.$APP_CONFIG.DAUM_POST_API_URL),this.getShippingAddressInfo()},mounted:function(){this.moveTop()},watch:{datas:{handler:function(e,t){if(e&&null!=e.urShippingAddrId){var s,i,a,r,n,o,l=this.splitPhone(e.receiverMobile.replace(/\-/g,""));this.request.urShippingAddrId=e.urShippingAddrId,this.request.name=e.receiverName,this.phonePrefix=l[0],this.request.phoneNumber=l[1]+l[2],this.request.zipCode=e.receiverZipCode,this.request.zipCodeAdd=e.receiverAddress1,this.request.zipCodeDeep=e.receiverAddress2,this.request.buildingCode=e.buildingCode,e.shippingComment?this.request.deliveryRequest=this.deliveryMessage.includes(e.shippingComment)?e.shippingComment:"직접입력":this.request.deliveryRequest=this.deliveryMessage[0],this.request.deliveryTextarea=e.shippingComment,this.deliveryMessageCurrentSize=null!==(s=null===(i=e.shippingComment)||void 0===i?void 0:i.length)&&void 0!==s?s:0,this.request.defaultYn=e.defaultYn,this.request.accessCode=e.accessInformationType,this.request.accessCodeOrder=e.accessInformationName,this.request.password=e.accessInformationPassword,this.accessEtcCurrentSize=null!==(a=null===(r=e.accessInformationPassword)||void 0===r?void 0:r.length)&&void 0!==a?a:0,this.accessPwCurrentSize=null!==(n=null===(o=e.accessInformationPassword)||void 0===o?void 0:o.length)&&void 0!==n?n:0,this.addressAvailableType=e.delivery}},deep:!0,immediate:!0},"request.deliveryRequest":{handler:function(e,t){this.request.deliveryEtc=""}},"request.accessCode":{handler:function(e,t){this.request.password="",this.validator.access=!1,this.validator.accessCode=!1,this.accessEtcCurrentSize=0,this.accessPwCurrentSize=0}}},methods:{close:function(e){this.scrollLock(!1),this.$emit("close-modal",{e:e})},layerClose:function(){this.layerOpen=!1},closeAlert:function(){this.alert.message=!1,this.alert.message=null},phoneInputEvent:function(e){this.request.phoneNumber=e.target.value.replace(/[^\d]/g,"")},maxTextLength:function(e,t){var s=arguments.length>2&&void 0!==arguments[2]?arguments[2]:30;e.target.value=e.target.value.replace("","").replace(/\s\s/g," "),this.request[t]=this.getCuttingValue(e,s)},checkComment:function(e){var t=e.target.value.replace(/^\s*|\n|\r/g,""),s=e.target.getAttribute("targetType");if(t.length>this["".concat(s,"MaxSize")]){var i=t.substr(0,this["".concat(s,"MaxSize")]);if(e.returnValue=!1,e.target.value=i,this["".concat(s,"CurrentSize")]=i.length,"deliveryMessage"==s)this.request.deliveryTextarea=t.substr(0,this["".concat(s,"MaxSize")]),this.validator.deliveryMessage=this.$FB_MESSAGES.SYSTEM.ALERT_222,this.$refs.deliveryMessage.focus();else if(["accessPw","accessEtc"].includes(s)){var a;this.request.password=t.substr(0,this["".concat(s,"MaxSize")]),this.validator.access=this.$FB_MESSAGES.SYSTEM.ALERT_222.replace("50",this["".concat(s,"MaxSize")]),(null===(a=this.$refs[s])||void 0===a?void 0:a[0])?this.$refs[s][0].focus():this.$refs[s].focus()}}else e.target.value=t,this["".concat(s,"CurrentSize")]=t.length,"deliveryMessage"==s?this.validator.deliveryMessage=!1:["accessPw","accessEtc"].includes(s)&&(this.validator.access=!1)},onSubmit:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var s,i,a,r,n,o,l,c;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:for(o in e.validator)e.validator[o]=!1;if(e.request.name){t.next=7;break}return e.validator.name="받는분을 입력해주세요.",e.$refs.name.focus(),t.abrupt("return",!1);case 7:if(!((null===(s=e.request.name)||void 0===s?void 0:s.length)<2||(null===(i=e.request.name)||void 0===i?void 0:i.length)>20)){t.next=13;break}return e.validator.name=e.$FB_MESSAGES.SYSTEM.ALERT_216,e.$refs.name.focus(),t.abrupt("return",!1);case 13:if(null==/[^ㄱ-ㅎ|ㅏ-ㅣ|가-힣|A-Z|a-z|\s]/.exec(e.request.name)){t.next=18;break}e.validator.name=e.$FB_MESSAGES.SYSTEM.ALERT_215,e.$refs.name.focus(),t.next=54;break;case 18:if(e.request.phoneNumber&&8==e.request.phoneNumber.length){t.next=24;break}return e.validator.phone="전화번호를 입력해주세요.",e.$refs.phone.focus(),t.abrupt("return",!1);case 24:if(e.request.zipCode&&e.request.zipCodeAdd){t.next=30;break}return e.validator.zipcode="배송지 주소를 검색해주세요.",e.$refs.zipcode.focus(),t.abrupt("return",!1);case 30:if(null===(a=e.request.zipCodeDeep)||void 0===a?void 0:a.trim()){t.next=36;break}return e.validator.zipcode="배송지 상세 주소를 입력해주세요.",e.$refs.zipcode.focus(),t.abrupt("return",!1);case 36:if("직접입력"!=e.request.deliveryRequest||(null===(r=e.request.deliveryTextarea)||void 0===r?void 0:r.trim())){t.next=42;break}return e.validator.deliveryMessage="배송 요청사항을 입력해주세요.",e.$refs.deliveryMessage.focus(),t.abrupt("return",!1);case 42:if(e.accessList.find((function(t){return t.code==e.request.accessCode}))){t.next=47;break}return e.validator.accessCode="출입 정보를 선택해주세요.",t.abrupt("return",!1);case 47:if(!["ACCESS_INFORMATION.FRONT_DOOR_PASSWORD","ACCESS_INFORMATION.ETC"].includes(e.request.accessCode)||(null===(n=e.request.password)||void 0===n?void 0:n.trim())){t.next=53;break}return e.validator.access="출입 정보를 입력해주세요.",(null===(l=e.$refs)||void 0===l?void 0:l.accessPw)?(e.validator.access="공동현관 비밀번호를 입력해주세요.",e.$refs.accessPw[0].focus()):(null===(c=e.$refs)||void 0===c?void 0:c.accessEtc)&&(e.validator.access="출입 정보를 입력해주세요.",e.$refs.accessEtc[0].focus()),t.abrupt("return",!1);case 53:e.openConfirm({message:e.$FB_MESSAGES.SYSTEM.CONFIRM_21,type:"submit"});case 54:case"end":return t.stop()}}),t)})))()},cancel:function(){var e="add"===this.type?this.$FB_MESSAGES.SYSTEM.CONFIRM_20:this.$FB_MESSAGES.SYSTEM.CONFIRM_24;this.openConfirm({message:e,type:"cancel"})},closeConfirm:function(e){var t=e.value;if(t)switch(this.confirm.type){case"cancel":this.close();break;case"submit":this.addAndModifyShippingAddress();break;default:}this.resetConfirm()},addAndModifyShippingAddress:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var s,i,a;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return s="",i={shippingName:e.request.shippingName,receiverName:e.request.name,receiverMobile:(e.phonePrefix+e.request.phoneNumber).replace(/\-/g,""),receiverTelephone:e.request.receiverTelephone,receiverZipCode:e.request.zipCode,receiverAddress1:e.request.zipCodeAdd,receiverAddress2:e.request.zipCodeDeep,shippingComment:"직접입력"==e.request.deliveryRequest?e.request.deliveryTextarea:e.request.deliveryRequest==e.deliveryMessage[0]?"":e.request.deliveryRequest,accessInformationType:e.request.accessCode,accessInformationName:e.accessList.find((function(t){return t.code==e.request.accessCode})).name,accessInformationPassword:e.request.password,buildingCode:e.request.buildingCode},"add"==e.type?(s="/user/buyer/addShippingAddress",i["defaultYn"]=e.request.zipCodeBasic?"Y":"N"):"push"==e.type&&(s="/user/buyer/putShippingAddress",i["defaultYn"]="Y"==e.request.defaultYn||e.request.zipCodeBasic?"Y":"N",i["urShippingAddrId"]=e.request.urShippingAddrId),t.prev=3,t.next=6,e.$store.dispatch("network/request",{method:"post",url:s,data:i});case 6:a=t.sent,a.code==e.$FB_CODES.API.SUCCESS&&location.reload(),t.next=14;break;case 10:t.prev=10,t.t0=t["catch"](3),e.openAlert(e.$FB_MESSAGES.ERROR.DEFAULT),console.error("addShippingAddress error...",t.t0.message);case 14:case"end":return t.stop()}}),t,null,[[3,10]])})))()},getShippingAddressInfo:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getShippingAddressInfo"});case 2:s=t.sent,s.code==e.$FB_CODES.API.SUCCESS&&(e.accessList=s.data.rows,e.request.accessCode||(e.request.accessCode=e.accessList[0].code));case 4:case"end":return t.stop()}}),t)})))()},getShippingAddressPossibleDeliveryInfo:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function s(){var i,a;return regeneratorRuntime.wrap((function(s){while(1)switch(s.prev=s.next){case 0:return s.prev=0,s.next=3,t.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getShippingAddressPossibleDeliveryInfo",data:e});case 3:if(i=s.sent,a=i.data,t.$FB_CODES.API.SUCCESS!=i.code){s.next=9;break}return s.abrupt("return",a);case 9:throw{code:i.code,message:i.message};case 10:s.next=15;break;case 12:throw s.prev=12,s.t0=s["catch"](0),s.t0;case 15:case"end":return s.stop()}}),s,null,[[0,12]])})))()},findAddress:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function e(){var s,i,a,r,n,o,l;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,t.getDaumPost();case 2:return s=e.sent,e.next=5,t.getShippingAddressPossibleDeliveryInfo({zipCode:s.zonecode,buildingCode:s.buildingCode});case 5:for(i=e.sent,a=!0,r=0,n=Object.entries(i);r<n.length;r++)o=Object(f["a"])(n[r],2),o[0],l=o[1],"boolean"==typeof l&&l&&(a=!1);a?(t.addressAvailableType=null,t.openAlert(t.$FB_MESSAGES.SYSTEM.ALERT_223),t.request.zipCode=null,t.request.zipCodeAdd=null,t.request.zipCodeDeep=null,t.request.buildingCode=null):(t.addressAvailableType=i,t.request.zipCode=s.zonecode,t.request.zipCodeAdd=s.roadAddress,t.request.zipCodeDeep=null,t.request.buildingCode=s.buildingCode);case 9:case"end":return e.stop()}}),e)})))()},closeModal:function(e,t){switch(t){case"daumPost":this.modals[t].model=null,this.modals[t].open=!1;break;default:console.wran("not allowed type...",t);break}},moveTop:function(){window.scrollTo(0,0)}}},b=C,y=(s("bf3e"),s("2877")),S=Object(y["a"])(b,v,h,!1,null,"e4e40e00",null),q=S.exports,w=s("e166"),x=s.n(w),A=(s("2ef0"),{name:"mypage-shipping",mixins:[_["a"]],components:{fbHeader:o["a"],fbFooter:l["a"],fbDockbar:c["a"],fbAlert:d["a"],fbModal:u["a"],shippingWhite:q,InfiniteLoading:x.a,fbConfirm:p["a"]},data:function(){return{pageTitle:"배송지 관리",shippinglayer:{open:!1,type:"add",data:{}},data:{delId:null,basicList:[],contentList:[]},loading:{list:!0},pagination:{id:"shipping-pagination",currentPage:1,goodsCount:20,totalCount:0}}},created:function(){if(this.shippinglayer.open=!1,this.userSession.isLogin){this.getQueryString();this.shippingList()}},mounted:function(){},watch:{},computed:{},methods:{getQueryString:function(){return new URLSearchParams(window.location.search)},pageBack:function(e){this.shippinglayer.open?this.closeChangeDate():history.length>1?history.back():this.goToRoute("/")},infiniteHandler:function(e){this.pagination.currentPage++,this.data.contentList.length<this.pagination.totalCount?this.shippingList(e):e.complete()},closeChangeDate:function(){this.pageTitle="배송지 관리",this.shippinglayer.data={},this.shippinglayer.open=!1},shippingWhite:function(e,t){this.pageTitle="add"===t?"배송지 추가":"배송지 수정",this.shippinglayer.data=e,this.shippinglayer.type=t,this.shippinglayer.open=!0},shippingList:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function s(){var i,a;return regeneratorRuntime.wrap((function(s){while(1)switch(s.prev=s.next){case 0:return s.prev=0,s.next=3,t.$store.dispatch("network/request",{method:"post",url:"/user/buyer/getShippingAddressListFromMyPage",data:{page:t.pagination.currentPage,limit:t.pagination.goodsCount}});case 3:i=s.sent,a=i.data,i.code==t.$FB_CODES.API.SUCCESS&&(a.total>0&&a.rows.length>0?(t.pagination.totalCount=a.total,t.loading.list=!0,a.rows&&a.rows.forEach((function(e){"Y"==e.defaultYn&&(t.data.basicList=[e]),e.receiverMobile.includes("-")&&(e.receiverMobile=e.receiverMobile.replace(/\-/g,"")),t.data.contentList.push(e)})),e&&e.loaded()):e&&e.complete()),s.next=12;break;case 8:s.prev=8,s.t0=s["catch"](0),console.error("shippingList error...",s.t0.message),t.loading.list="error";case 12:case"end":return s.stop()}}),s,null,[[0,8]])})))()},closeConfirm:function(e){var t=this.confirm;if(t){if(e.value)switch(t.type){case"del":this.shippingListDel(t.model);break}this.resetConfirm()}},closeAlert:function(e){var t=this.alert;switch(t.sendData){case"reload":location.reload();break;default:}this.resetAlert()},del:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:"0";this.openConfirm({message:this.$FB_MESSAGES.SYSTEM.CONFIRM_19,type:"del",model:e}),this.data.delId=e},shippingListDel:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function s(){var i;return regeneratorRuntime.wrap((function(s){while(1)switch(s.prev=s.next){case 0:return s.prev=0,s.next=3,t.$store.dispatch("network/request",{method:"post",url:"/user/buyer/delShippingAddress",data:{urShippingAddrId:e}});case 3:i=s.sent,(i.code=t.$FB_CODES.API.SUCCESS)&&location.reload(),s.next=10;break;case 7:s.prev=7,s.t0=s["catch"](0),console.error("shippingListDel error...",s.t0.message);case 10:case"end":return s.stop()}}),s,null,[[0,7]])})))()},shippingListBasic:function(){var e=this,t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:0;return Object(n["a"])(regeneratorRuntime.mark((function s(){var i;return regeneratorRuntime.wrap((function(s){while(1)switch(s.prev=s.next){case 0:return s.prev=0,s.next=3,e.$store.dispatch("network/request",{method:"post",url:"/user/buyer/putShippingAddressSetDefault",data:{urShippingAddrId:t}});case 3:i=s.sent,i.code==e.$FB_CODES.API.SUCCESS&&(e.alert.sendData="reload",e.openAlert(e.$FB_MESSAGES.SYSTEM.ALERT_175)),s.next=10;break;case 7:s.prev=7,s.t0=s["catch"](0),console.error("shippingListBasic error...",s.t0.message);case 10:case"end":return s.stop()}}),s,null,[[0,7]])})))()}}}),E=A,k=(s("eafd"),Object(y["a"])(E,a,r,!1,null,null,null)),z=k.exports,P=s("dd69");P["c"].components=Object(i["a"])({App:z},P["c"].components),P["d"].dispatch("init").then((function(){return new P["a"](P["c"])}))},eafd:function(e,t,s){"use strict";var i=s("975c"),a=s.n(i);a.a}});