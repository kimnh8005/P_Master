(function(e){function t(t){for(var s,c,l=t[0],n=t[1],o=t[2],h=0,f=[];h<l.length;h++)c=l[h],Object.prototype.hasOwnProperty.call(a,c)&&a[c]&&f.push(a[c][0]),a[c]=0;for(s in n)Object.prototype.hasOwnProperty.call(n,s)&&(e[s]=n[s]);u&&u(t);while(f.length)f.shift()();return i.push.apply(i,o||[]),r()}function r(){for(var e,t=0;t<i.length;t++){for(var r=i[t],s=!0,l=1;l<r.length;l++){var n=r[l];0!==a[n]&&(s=!1)}s&&(i.splice(t--,1),e=c(c.s=r[0]))}return e}var s={},a={"pc/shop/search/index":0},i=[];function c(t){if(s[t])return s[t].exports;var r=s[t]={i:t,l:!1,exports:{}};return e[t].call(r.exports,r,r.exports,c),r.l=!0,r.exports}c.m=e,c.c=s,c.d=function(e,t,r){c.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:r})},c.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},c.t=function(e,t){if(1&t&&(e=c(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var r=Object.create(null);if(c.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var s in e)c.d(r,s,function(t){return e[t]}.bind(null,s));return r},c.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return c.d(t,"a",t),t},c.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},c.p="/";var l=window["webpackJsonp"]=window["webpackJsonp"]||[],n=l.push.bind(l);l.push=t,l=l.slice();for(var o=0;o<l.length;o++)t(l[o]);var u=n;i.push([180,"chunk-commons"]),r()})({180:function(e,t,r){e.exports=r("3162")},3162:function(e,t,r){"use strict";r.r(t);var s=r("5530"),a=(r("e260"),r("e6cf"),r("cca6"),r("a79d"),function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("main",{staticClass:"fb__shopSearch"},[r("fb-header",{attrs:{pageType:"sub",brand:e.isOrgaHeader?"orga":"",searchWord:e.searchResult.word}}),r("section",{ref:"scrollWrapper",staticClass:"search"},[e.$FB_CODES.FETCHES.WAIT===e.fetches.search?r("div",{staticClass:"fb__fetching"}):e.$FB_CODES.FETCHES.SUCCESS===e.fetches.search?[e.searchResult.word.trim()?r("header",{staticClass:"search__header"},[r("h2",{staticClass:"search__header__title"},[e._v("‘"+e._s(e.searchResult.word)+"’ 검색결과")])]):e._e(),r("nav",{staticClass:"search__nav"},[r("div",{staticClass:"fb__tabs"},e._l(e.search.mallType,(function(t,s){return r("label",{key:s+t.code,staticClass:"fb__tabs__list"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.category,expression:"search.category"}],class:{disabled:!t.count||"0"==t.count},attrs:{type:"radio",name:"searchTab"},domProps:{value:t.code,checked:e._q(e.search.category,t.code)},on:{click:function(t){return t.preventDefault(),e.changeMall.apply(null,arguments)},change:function(r){return e.$set(e.search,"category",t.code)}}}),r("span",{staticClass:"fb__tabs__btn"},[e._v(e._s(t.name)+" "),t.count&&"0"!=t.count?r("span",{staticClass:"fb__tabs__count"},[e._v(e._s(t.count))]):e._e()])])})),0)]),r("div",{staticClass:"product-layout__body"},[e.search.searchComplete&&!e.isDisabledFilter?r("div",{staticClass:"product-layout__body__left"},[r("div",{staticClass:"search__filter"},[r("div",{staticClass:"filter"},[e._l(e.searchRespons,(function(t,s,a){return[t.length?[r("div",{key:a+t,staticClass:"filter__box filter__type__checkbox"},[r("button",{staticClass:"filter__title",class:e.searchResult.filterVisible[s]?"":"filter__title--close",attrs:{type:"button"},on:{click:function(t){return e.filterVisible(s)}}},[e._v(" "+e._s(t[0].title)+" ")]),e.searchResult.filterVisible[s]?r("div",{staticClass:"filter__inner"},e._l(t,(function(t,a){return r("label",{key:a+t.name,staticClass:"fb__checkbox"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.searchResult.filter[s],expression:"searchResult.filter[name]"}],attrs:{type:"checkbox"},domProps:{value:t.code,checked:Array.isArray(e.searchResult.filter[s])?e._i(e.searchResult.filter[s],t.code)>-1:e.searchResult.filter[s]},on:{change:function(r){var a=e.searchResult.filter[s],i=r.target,c=!!i.checked;if(Array.isArray(a)){var l=t.code,n=e._i(a,l);i.checked?n<0&&e.$set(e.searchResult.filter,s,a.concat([l])):n>-1&&e.$set(e.searchResult.filter,s,a.slice(0,n).concat(a.slice(n+1)))}else e.$set(e.searchResult.filter,s,c)}}}),r("span",[e._v(e._s(t.name)+" ")])])})),0):e._e()])]:e._e()]})),r("div",{staticClass:"filter__box"},[r("button",{staticClass:"filter__title",class:e.searchResult.filterVisible["price"]?"":"filter__title--close",attrs:{type:"button"},on:{click:function(t){return e.filterVisible("price")}}},[e._v(" 가격 ")]),e.searchResult.filterVisible["price"]?r("div",{staticClass:"filter__inner"},[r("div",{staticClass:"price"},[r("div",{staticClass:"price__min"},[r("vue-autonumeric",{ref:"priceMin",staticClass:"emptyPrice",attrs:{placeholder:"최소가격",options:e.numericOption},on:{input:e.isEmptyPrice},model:{value:e.price.min,callback:function(t){e.$set(e.price,"min",t)},expression:"price.min"}}),r("span",{staticClass:"unit"},[e._v("원")])],1),r("span",{staticClass:"tilde"},[e._v("~")]),r("div",{staticClass:"price__max"},[r("vue-autonumeric",{ref:"priceMax",staticClass:"emptyPrice",attrs:{placeholder:"최대가격",options:e.numericOption},on:{input:e.isEmptyPrice},model:{value:e.price.max,callback:function(t){e.$set(e.price,"max",t)},expression:"price.max"}}),r("span",{staticClass:"unit"},[e._v("원")])],1),r("div",{staticClass:"price__apply"},[r("button",{attrs:{type:"button"},on:{click:e.priceApply}},[e._v("적용")])])])]):e._e()])],2)])]):e._e(),r("div",{staticClass:"product-layout__body__right"},[e.search.searchComplete?r("div",{staticClass:"search__filterBox"},[e.searchResult.filterIs&&e.searchResult.filterList.length?r("div",{staticClass:"search__filterList"},[r("ul",{staticClass:"fb__filter-list"},e._l(e.searchResult.filterList,(function(t,s){return r("li",{key:t.name+s,staticClass:"fb__filter-list__item"},[e._v(" "+e._s(t.name)+" "),r("button",{staticClass:"fb__filter-list__delete",attrs:{type:"button"},on:{click:function(r){return e.filterDel(t,r)}}},[e._v("삭제")])])})),0),e._e(),r("button",{staticClass:"search__filterList__clear",attrs:{type:"button"},on:{click:e.filterAllDel}},[e._v("초기화")])]):e._e()]):e._e(),e.search.searchComplete?r("div",{staticClass:"search__top"},[r("div",{staticClass:"search__total"},[e._v("총 "),r("span",{class:[e.search.category.replace(".","_")]},[e._v(e._s(e.searchResult.total))]),e._v("개")]),r("div",{staticClass:"search__listType"},[r("label",{staticClass:"fb__checkbox  search__soldOut"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.soldOut,expression:"search.soldOut"}],attrs:{type:"checkbox"},domProps:{checked:Array.isArray(e.search.soldOut)?e._i(e.search.soldOut,null)>-1:e.search.soldOut},on:{change:function(t){var r=e.search.soldOut,s=t.target,a=!!s.checked;if(Array.isArray(r)){var i=null,c=e._i(r,i);s.checked?c<0&&e.$set(e.search,"soldOut",r.concat([i])):c>-1&&e.$set(e.search,"soldOut",r.slice(0,c).concat(r.slice(c+1)))}else e.$set(e.search,"soldOut",a)}}}),r("span",[e._v(" 품절상품 제외 ")])]),r("div",{staticClass:"search__listType__sort"},e._l(e.sortList,(function(t,s){return r("label",{key:"sortList-"+s},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.sorts,expression:"search.sorts"}],attrs:{type:"radio"},domProps:{value:t.value,checked:e._q(e.search.sorts,t.value)},on:{change:function(r){return e.$set(e.search,"sorts",t.value)}}}),r("span",[e._v(e._s(t.name))])])})),0),r("div",{staticClass:"search__listType__btn"},[r("label",{staticClass:"search__listType__list"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.views,expression:"search.views"}],attrs:{type:"radio"},domProps:{value:["wide"],checked:e._q(e.search.views,["wide"])},on:{change:function(t){return e.$set(e.search,"views",["wide"])}}}),r("span",[e._v(" 리스트 ")])]),r("label",{staticClass:"search__listType__photo"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.views,expression:"search.views"}],attrs:{type:"radio"},domProps:{value:["small"],checked:e._q(e.search.views,["small"])},on:{change:function(t){return e.$set(e.search,"views",["small"])}}}),r("span",[e._v(" 포토 ")])])])])]):e._e(),r("div",{staticClass:"search__result"},[e.searchResult.goodsResultList&&e.searchResult.goodsResultList.length?r("div",{staticClass:"search__result__productWrap"},[r("fb-product",{attrs:{viewType:[e.search.views],"product-list":e.searchResult.goodsResultList,"mall-type":e.search.category,PageCd:e.getAdMallKey+"_PC_SerKwd",ContentCd:e.queryString.get("searchword")}}),r("div",{staticClass:"search__result__pagination"},[e.searchResult.total>0?r("fb-pagination",{attrs:{pagination:e.pagination},on:{movePaging:e.movePaging}}):e._e()],1)],1):r("div",{staticClass:"search__result--noItem"},[e.search.searchComplete&&e.searchResult.filterIs?[e._v(" 선택된 필터에 대한 검색결과가 없습니다.​"),r("br"),e._v(" 다른 필터를 선택해 보세요.​ ")]:[r("span",[e._v("'"+e._s(e.searchResult.word)+"' ")]),e._v("에 대한"),r("br"),e._v(" 검색결과가 없습니다. ")]],2)])])])]:e.$FB_CODES.FETCHES.ERROR===e.fetches.search?[r("error-layout",{attrs:{"error-type":"default"}},[[r("div",{staticClass:"fb__btn-wrap margin"},[r("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.reload(t)}}},[e._v("새로고침")]),r("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.goToRoute(e.$APP_CONFIG.PAGE_URL.ROOT)}}},[e._v("홈으로")])])]],2)]:e._e()],2),r("fb-footer"),r("fb-alert",{attrs:{message:e.alert.message,open:e.alert.open,buttonText:e.alert.buttonText,sendData:e.sendData},on:{"close-alert":e.closeAlert}})],1)}),i=[],c=(r("a4d3"),r("e01a"),r("d28b"),r("99af"),r("4de4"),r("7db0"),r("c740"),r("c975"),r("a15b"),r("a434"),r("b0c0"),r("4fad"),r("d3b7"),r("ac1f"),r("25f0"),r("3ca3"),r("5319"),r("841c"),r("1276"),r("498a"),r("ddb0"),r("2b3d"),r("96cf"),r("1da1")),l=r("2909"),n=r("3835"),o=r("ade3"),u=r("53ca"),h=r("dcbe"),f=r("4402"),d=r("9d0b"),p=r("7c17"),g=r("3af9"),m=r("7e74"),v=r("0bf4"),_=r("f18a"),y=r("3540"),b=r("181c"),R={name:"find",mixins:[v["a"],_["a"]],components:{fbHeader:h["a"],fbFooter:f["a"],fbAlert:d["a"],VueAutonumeric:b["a"],fbProduct:p["a"],fbSelectBox:g["a"],fbPagination:m["a"],errorLayout:y["a"]},data:function(){return{fetches:{search:this.$FB_CODES.FETCHES.WAIT},sendData:null,alert:{message:null,open:!1},numericOption:{decimalPlaces:0,maximumValue:9999999,minimumValue:0},filterLayerState:!1,sortList:[{name:"인기순",value:"POPULARITY"},{name:"신상품순",value:"NEW "},{name:"낮은가격순",value:"LOW_PRICE"},{name:"높은가격순",value:"HIGH_PRICE"},{name:"할인율높은순",value:"HIGH_DISCOUNT_RATE"}],search:{category:"MALL_DIV.PULMUONE",soldOut:!1,filterBtn:!1,views:["small"],sorts:"POPULARITY",url:"",load:!1,mallType:null,searchComplete:!1,doMoveTab:!1},searchResult:{total:"0",word:"",saveIs:null==this.getIsSaveKeyword()||this.getIsSaveKeyword(),filter:{},filterIs:!1,filterList:[],filterVisible:[],allFilter:{},goodsResultList:null},price:{min:"",max:""},searchRespons:null,pagination:{id:"goods-pagination",currentPage:1,goodsCount:40,totalCount:0,disabled:!1,updated:!0},isPageLoaded:!1}},created:function(){var e=this,t=this.getQueryString();this.search.url=t.toString(),this.searchResult.word=t.get("searchword"),this.search.category=t.get("mall")?t.get("mall"):"MALL_DIV.PULMUONE",this.getHistory?this.filterInit():this.requestTerms();var r=this.getViewType();r&&(this.search.views=r),this.$nextTick((function(){e.isPageLoaded=!0}))},mounted:function(){},watch:{"search.soldOut":{handler:function(e,t){this.isPageLoaded&&this.requestGoods()}},"searchResult.filter":{handler:function(e,t){var r=this;this.searchResult.filterList=[],this.searchResult.url="";var s=this.getQueryString();if(this.search.load){var a=function(e){if(r.searchResult.filter[e].length>0||s.get("minPrice")||s.get("maxPrice")){if(r.searchResult.filterIs=!0,"object"==Object(u["a"])(r.searchResult.filter[e]))if(Array.isArray(r.searchResult.filter[e]))if(r.searchResult.filter[e].length>0){s.set(e,r.searchResult.filter[e].join("%")),history.replaceState(r.filterData(),null,"?"+s.toString());var t=!0,a=!1,i=void 0;try{for(var c,l=function(){var t=c.value,s=r.searchRespons[e].find((function(e){return e.code==t}));r.searchResult.filterList.push(s)},n=r.searchResult.filter[e][Symbol.iterator]();!(t=(c=n.next()).done);t=!0)l()}catch(h){a=!0,i=h}finally{try{t||null==n.return||n.return()}finally{if(a)throw i}}}else s.delete(e),history.replaceState(r.filterData(),null,"?"+s.toString());else s.get("minPrice")>=0&&s.get("maxPrice")>0&&r.searchResult.filterList.push({name:"".concat(r.toPrice(s.get("minPrice"))," ~ ").concat(r.toPrice(s.get("maxPrice")),"원"),category:"price",value:{minPrice:s.get("minPrice"),maxPrice:s.get("maxPrice")}});else if(r.searchResult.filter[e]){s.set(e,r.searchResult.filter[e]),history.replaceState(r.filterData(),null,"?"+s.toString());var o=r.searchRespons[e].find((function(t){return t.code==r.searchResult.filter[e]}));r.searchResult.filterList.push(o)}}else s.delete(e),history.replaceState(r.filterData(),null,"?"+s.toString()),r.searchResult.filterList.length||(r.searchResult.filterIs=!1)};for(var i in this.searchResult.filter)a(i);this.search.url!==s.toString()&&this.search.load&&(this.search.url=s.toString(),this.requestGoods())}else{var c=function(e){if(r.searchResult.filter[e].length>0||s.get("minPrice")||s.get("maxPrice"))if(r.searchResult.filterIs=!0,"object"==Object(u["a"])(r.searchResult.filter[e]))if(Array.isArray(r.searchResult.filter[e])){var t=!0,a=!1,i=void 0;try{for(var c,l=function(){var t,s=c.value,a=null===(t=r.searchRespons[e])||void 0===t?void 0:t.find((function(e){return e.code==s}));a&&r.searchResult.filterList.push(a)},n=r.searchResult.filter[e][Symbol.iterator]();!(t=(c=n.next()).done);t=!0)l()}catch(h){a=!0,i=h}finally{try{t||null==n.return||n.return()}finally{if(a)throw i}}}else s.get("minPrice")>=0&&s.get("maxPrice")>0&&(r.searchResult.filterList.push({name:"".concat(r.toPrice(s.get("minPrice"))," ~ ").concat(r.toPrice(s.get("maxPrice")),"원"),category:"price",value:{minPrice:s.get("minPrice"),maxPrice:s.get("maxPrice")}}),r.search.url!==s.toString()&&r.search.load&&(r.search.url=s.toString(),r.requestGoods()));else if(r.searchResult.filter[e]){s.set(e,r.searchResult.filter[e]),history.replaceState(r.filterData(),null,"?"+s.toString());var o=r.searchRespons[e].find((function(t){return t.code==r.searchResult.filter[e]}));r.searchResult.filterList.push(o)}};for(var l in this.searchResult.filter)c(l)}},deep:!0},"search.views":{handler:function(e,t){this.setViewType(e)}},"search.sorts":{handler:function(e,t){this.isPageLoaded&&this.requestGoods()}},"searchResult.goodsResultList":{handler:function(e,t){this.pagination.updated||(this.pagination.updated=!0)},deep:!0}},computed:{isDisabledFilter:function(){var e=this.searchResult,t=e.filterIs,r=e.goodsResultList;return!t&&!(null===r||void 0===r?void 0:r.length)},isOrgaHeader:function(){return"MALL_DIV.ORGA"==this.search.category}},methods:{isEmptyPrice:function(e,t){""==t.detail.newRawValue?t.target.classList.add("emptyPrice"):t.target.classList.remove("emptyPrice")},changeMall:function(e){for(var t,r=this.$FB_CODES.MALL_TYPES,s=(t={},Object(o["a"])(t,r.PULMUONE,""),Object(o["a"])(t,r.ORGA,"/orga"),Object(o["a"])(t,r.BABY_MEAL,""),Object(o["a"])(t,r.EATS_SLIM,""),t),a=this.$APP_CONFIG.PAGE_URL.SEARCH,i=0,c=Object.entries(s);i<c.length;i++){var l=Object(n["a"])(c[i],2),u=(l[0],l[1]);a=a.replace(u,"")}a=s[e.target.value]+a;var h="?searchword=".concat(encodeURIComponent(this.searchResult.word),"&mall=").concat(e.target.value);this.goToRoute(a+h)},urlSearch:function(){var e,t=this,r=this.getQueryString(),s=!0,a=!1,i=void 0;try{for(var c,n=this.getQueryString().keys()[Symbol.iterator]();!(s=(c=n.next()).done);s=!0){var o=c.value;switch(o){case"minPrice":this.searchResult.filter.price.min=r.get(o);break;case"maxPrice":this.searchResult.filter.price.max=r.get(o);break;case"searchword":this.searchResult.word=decodeURIComponent(r.get(o));break;case"page":this.pagination.currentPage=r.get(o);break;case"mall":break;default:this.$set(this.searchResult.filter,o,[]),(e=this.searchResult.filter[o]).push.apply(e,Object(l["a"])(r.get(o).split("%")));break}}}catch(u){a=!0,i=u}finally{try{s||null==n.return||n.return()}finally{if(a)throw i}}this.$nextTick((function(){t.search.load=!0}))},filterInit:function(){var e=this,t=null;for(var r in this.searchRespons){if(function(){switch(r){case"category":t="카테고리";break;case"brand":t="브랜드";break;case"certification":t="인증제품";break;case"storage":t="보관방법";break;case"delivery":t="배송";break;case"benefit":t="혜택";break;case"mall":var s=["MALL_DIV.PULMUONE","MALL_DIV.ORGA","MALL_DIV.EATSLIM","MALL_DIV.BABYMEAL"];e.search.mallType=e.searchRespons[r],e.search.mallType.sort((function(e,t){return s.indexOf(e.code)<s.indexOf(t.code)?-1:s.indexOf(e.code)>s.indexOf(t.code)?1:0}));var a=!0,i=!1,c=void 0;try{for(var l,n=e.search.mallType[Symbol.iterator]();!(a=(l=n.next()).done);a=!0){var o=l.value;o.count}}catch(u){i=!0,c=u}finally{try{a||null==n.return||n.return()}finally{if(i)throw c}}delete e.searchRespons.mall;break;default:t="기타"}}(),Array.isArray(this.searchRespons[r])){this.$set(this.searchResult.allFilter,r,[]);var s=!0,a=!1,i=void 0;try{for(var c,l=this.searchRespons[r][Symbol.iterator]();!(s=(c=l.next()).done);s=!0){var n=c.value;this.searchResult.allFilter[r].push(n.code)}}catch(u){a=!0,i=u}finally{try{s||null==l.return||l.return()}finally{if(a)throw i}}}for(var o in this.$set(this.searchResult.filter,r,[]),this.$set(this.searchResult.filterVisible,r,!0),this.$set(this.searchResult.filterVisible,"price",!0),this.searchRespons[r])this.searchRespons[r][o].category=r,this.searchRespons[r][o].title=t}this.$set(this.searchResult.filter,"price",{}),this.$set(this.searchResult.filter.price,"min",""),this.$set(this.searchResult.filter.price,"max",""),this.urlSearch()},getQueryString:function(){return new URLSearchParams(window.location.search)},filterVisible:function(e){this.searchResult.filterVisible[e]=!this.searchResult.filterVisible[e]},filterDel:function(e,t){var r=this.getQueryString();switch(this.search.load=!0,e.category){case"price":this.search.url=r.toString(),this.searchResult.filter[e.category].min="",this.searchResult.filter[e.category].max="",r.delete("minPrice"),r.delete("maxPrice"),history.replaceState(this.filterData(),null,"?"+r.toString());break;default:var s=this.searchResult.filter[e.category].findIndex((function(t){return t==e.code}));this.searchResult.filter[e.category].splice(s,1);break}},filterAllDel:function(e,t){var r=this.getQueryString();for(var s in this.search.load=!0,this.searchResult.filter)switch(s){case"price":this.search.url=r.toString(),this.searchResult.filter[s].min="",this.searchResult.filter[s].max="",r.delete("minPrice"),r.delete("maxPrice"),history.replaceState(this.filterData(),null,"?"+r.toString());break;default:this.searchResult.filter[s]=[];break}},priceFilter:function(e){this.searchResult.filter.price.min+=e.key},priceApply:function(){var e=this;if(""==this.$refs.priceMin.$el.value||""==this.$refs.priceMax.$el.value)return this.alert.message=this.$FB_MESSAGES.SYSTEM.ALERT_237,this.sendData=""==this.$refs.priceMin.$el.value?"priceMin":"priceMax",this.alert.open=!0,!1;if(this.price.min>=this.price.max)return this.alert.message=this.$FB_MESSAGES.SYSTEM.ALERT_224,this.sendData="priceMin",this.alert.open=!0,!1;this.searchResult.filter.price.min=this.price.min,this.searchResult.filter.price.max=this.price.max;var t=this.getQueryString();if(this.searchResult.filterIs=!0,this.searchResult.filter.price.min!=t.get("minPrice")||this.searchResult.filter.price.max!=t.get("maxPrice")){t.set("minPrice",this.searchResult.filter.price.min),t.set("maxPrice",this.searchResult.filter.price.max),history.replaceState(this.filterData(),null,"?"+t.toString());var r=this.searchResult.filterList.findIndex((function(e){return"price"==e.category}));r>=0?this.searchResult.filterList[r].name="".concat(this.toPrice(t.get("minPrice"))," ~ ").concat(this.toPrice(t.get("maxPrice")),"원"):this.searchResult.filterList.push({name:"".concat(this.toPrice(t.get("minPrice"))," ~ ").concat(this.toPrice(t.get("maxPrice")),"원"),category:"price",value:{minPrice:t.get("minPrice"),maxPrice:t.get("maxPrice")}}),this.search.load=!1,this.requestGoods(),this.$nextTick((function(){e.search.load=!0}))}},closeAlert:function(e){var t=e.sendData;this.alert.open=!1,"priceMin"==t?this.$refs.priceMin.$el.focus():"priceMax"==t&&this.$refs.priceMax.$el.focus()},requestTerms:function(){var e=this;return Object(c["a"])(regeneratorRuntime.mark((function t(){var r,s,a,i,c,l,n,o,u,h,f;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,r=e.getQueryString(),t.next=4,e.$store.dispatch("network/request",{method:"post",url:"/goods/search/getSearchGoodsList",data:{keyword:encodeURIComponent(r.get("searchword")),isSaveKeyword:!!decodeURIComponent(r.get("searchword")).trim()&&e.searchResult.saveIs,isFirstSearch:!0,page:r.get("page")?r.get("page"):1,limit:e.pagination.goodsCount,mallId:"MALL_DIV.PULMUONE"==e.search.category?"":e.search.category,excludeSoldOutGoods:!1,lev1CategoryId:r.get("category")?r.get("category"):[],benefitTypeIdList:r.get("benefit")?r.get("benefit").split("%"):[],brandIdList:r.get("brand")?r.get("brand").split("%"):[],deliveryTypeIdList:r.get("delivery")?r.get("delivery").split("%"):[],certificationTypeIdList:r.get("certification")?r.get("certification").split("%"):[],storageMethodIdList:r.get("storage")?r.get("storage").split("%"):[],minimumPrice:r.get("minPrice")?r.get("minPrice"):0,maximumPrice:r.get("maxPrice")?r.get("maxPrice"):9999999,sortCode:"POPULARITY"}});case 4:if(s=t.sent,a=s.data,s.code!=e.$FB_CODES.API.SUCCESS){t.next=47;break}if(e.fetches.search=e.$FB_CODES.FETCHES.SUCCESS,e.searchResult.total=a.count,i={},i.mall=a.filter.mall&&a.filter.mall.length?a.filter.mall:[],i.category=a.filter.category&&a.filter.category.length?a.filter.category:[],i.benefit=a.filter.benefit&&a.filter.benefit.length?a.filter.benefit:[],i.brand=a.filter.brand&&a.filter.brand.length?a.filter.brand:[],i.delivery=a.filter.delivery&&a.filter.delivery.length?a.filter.delivery:[],i.certification=a.filter.certification&&a.filter.certification.length?a.filter.certification:[],i.storage=a.filter.storage&&a.filter.storage.length?a.filter.storage:[],e.searchRespons=i,e.searchResult.goodsResultList=a.document,!e.searchResult.goodsResultList){t.next=42;break}for(c=null,l=!0,n=!1,o=void 0,t.prev=24,u=i.mall[Symbol.iterator]();!(l=(h=u.next()).done);l=!0)f=h.value,c+=f.count;t.next=32;break;case 28:t.prev=28,t.t0=t["catch"](24),n=!0,o=t.t0;case 32:t.prev=32,t.prev=33,l||null==u.return||u.return();case 35:if(t.prev=35,!n){t.next=38;break}throw o;case 38:return t.finish(35);case 39:return t.finish(32);case 40:e.searchResult.total||c?e.search.searchComplete=!0:e.search.searchComplete=!1;case 42:e.pagination.totalCount=e.searchResult.total,e.pagination.currentPage=r.get("page")?r.get("page"):1,e.$nextTick((function(){e.filterInit()})),t.next=48;break;case 47:throw s;case 48:t.next=54;break;case 50:t.prev=50,t.t1=t["catch"](0),e.fetches.search=e.$FB_CODES.FETCHES.ERROR,console.error("requestTerms error...",t.t1.message);case 54:case"end":return t.stop()}}),t,null,[[0,50],[24,28,32,40],[33,,35,39]])})))()},requestGoods:function(e){var t=this;return Object(c["a"])(regeneratorRuntime.mark((function r(){var s,a,i;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.prev=0,s=t.getQueryString(),"undefined"==typeof e&&s.get("page")&&(s.delete("page"),history.replaceState(t.filterData(),null,"?"+s.toString())),r.next=5,t.$store.dispatch("network/request",{method:"post",url:"/goods/search/getSearchGoodsList",data:{keyword:encodeURIComponent(s.get("searchword")),isSaveKeyword:!!decodeURIComponent(s.get("searchword")).trim()&&t.searchResult.saveIs,isFirstSearch:!1,page:s.get("page")?s.get("page"):1,limit:t.pagination.goodsCount,mallId:"MALL_DIV.PULMUONE"==t.search.category?"":t.search.category,excludeSoldOutGoods:t.search.soldOut,lev1CategoryId:t.searchResult.filter.category?t.searchResult.filter.category:[],benefitTypeIdList:t.searchResult.filter.benefit?t.searchResult.filter.benefit:[],brandIdList:t.searchResult.filter.brand?t.searchResult.filter.brand:[],deliveryTypeIdList:t.searchResult.filter.delivery?t.searchResult.filter.delivery:[],certificationTypeIdList:t.searchResult.filter.certification?t.searchResult.filter.certification:[],storageMethodIdList:t.searchResult.filter.storage?t.searchResult.filter.storage:[],minimumPrice:s.get("minPrice")?s.get("minPrice"):0,maximumPrice:s.get("maxPrice")?s.get("maxPrice"):9999999,sortCode:t.search.sorts}});case 5:if(a=r.sent,i=a.data,a.code!=t.$FB_CODES.API.SUCCESS){r.next=15;break}t.fetches.search=t.$FB_CODES.FETCHES.SUCCESS,t.pagination.currentPage=s.get("page")?s.get("page"):1,t.searchResult.total=i.count,i.count?(t.searchResult.goodsResultList=i.document,t.pagination.totalCount=t.searchResult.total,t.search.doMoveTab&&(t.search.searchComplete=!0)):(t.searchResult.goodsResultList=[],t.search.doMoveTab&&(t.search.searchComplete=!1)),t.search.doMoveTab=!1,r.next=16;break;case 15:throw a;case 16:r.next=22;break;case 18:r.prev=18,r.t0=r["catch"](0),t.fetches.search=t.$FB_CODES.FETCHES.ERROR,console.error("requestTerms error...",r.t0.message);case 22:case"end":return r.stop()}}),r,null,[[0,18]])})))()},movePaging:function(e){var t=this.getQueryString();t.get("page")!=e&&(t.set("page",e),history.replaceState(this.filterData(),null,"?"+t.toString()),this.requestGoods(e),this.pagination.currentPage=parseInt(t.get("page")))},pageBack:function(){var e=this.$refs["search-header-form"];e.$el.classList.contains("fb__search--fixed")?e.$el.classList.remove("fb__search--fixed"):history.back()},getViewType:function(){var e=sessionStorage.getItem("viewTypePc");return e?e.split(","):this.search.views},setViewType:function(e){sessionStorage.setItem("viewTypePc",e)},getIsSaveKeyword:function(){return null==sessionStorage.getItem("isSaveKeywordPC")||"true"===sessionStorage.getItem("isSaveKeywordPC")},reload:function(e){this.fetches.search=this.$FB_CODES.FETCHES.WAIT,this.requestTerms()}}},S=R,P=(r("48f5"),r("2877")),C=Object(P["a"])(S,a,i,!1,null,null,null),x=C.exports,w=r("8f7d"),L=r("79a5"),O=r("caf9");w["a"].use(L["a"]),w["a"].use(O["a"],{error:w["b"].IMAGES.NOT_FOUND,loading:w["b"].IMAGES.IMG_LOADING}),w["c"].components=Object(s["a"])({App:x},w["c"].components),w["d"].dispatch("init").then((function(){return new w["a"](w["c"])}))},"48f5":function(e,t,r){"use strict";var s=r("a577"),a=r.n(s);a.a},a577:function(e,t,r){}});