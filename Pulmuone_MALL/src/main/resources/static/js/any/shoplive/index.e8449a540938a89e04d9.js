(function(e){function t(t){for(var r,a,u=t[0],c=t[1],l=t[2],p=0,f=[];p<u.length;p++)a=u[p],Object.prototype.hasOwnProperty.call(o,a)&&o[a]&&f.push(o[a][0]),o[a]=0;for(r in c)Object.prototype.hasOwnProperty.call(c,r)&&(e[r]=c[r]);s&&s(t);while(f.length)f.shift()();return i.push.apply(i,l||[]),n()}function n(){for(var e,t=0;t<i.length;t++){for(var n=i[t],r=!0,u=1;u<n.length;u++){var c=n[u];0!==o[c]&&(r=!1)}r&&(i.splice(t--,1),e=a(a.s=n[0]))}return e}var r={},o={"any/shoplive/index":0},i=[];function a(t){if(r[t])return r[t].exports;var n=r[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,a),n.l=!0,n.exports}a.m=e,a.c=r,a.d=function(e,t,n){a.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},a.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},a.t=function(e,t){if(1&t&&(e=a(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(a.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)a.d(n,r,function(t){return e[t]}.bind(null,r));return n},a.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return a.d(t,"a",t),t},a.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},a.p="/";var u=window["webpackJsonp"]=window["webpackJsonp"]||[],c=u.push.bind(u);u.push=t,u=u.slice();for(var l=0;l<u.length;l++)t(u[l]);var s=c;i.push([6,"chunk-commons"]),n()})({"592f":function(e,t,n){"use strict";var r=n("6c82"),o=n.n(r);o.a},6:function(e,t,n){e.exports=n("6570")},6570:function(e,t,n){"use strict";n.r(t);var r=n("5530"),o=(n("e260"),n("e6cf"),n("cca6"),n("a79d"),function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"shopLiveArea",attrs:{id:"shopLiveArea"}})}),i=[],a=(n("96cf"),n("1da1")),u=n("66b2"),c={name:"shop-live",components:{},data:function(){return{requests:{detail:{evShopliveId:null}},liveLoaded:null}},created:function(){var e=this.queryString.get("shoplive");this.requests.detail.evShopliveId=e,null!=this.liveLoaded&&this.liveLoaded||(function(e,t,n,r,o,i,a,u){e["ShoplivePlayer"]=o,e[o]=e[o]||function(){(e[o].q=e[o].q||[]).push(arguments)},i=t.createElement(n),a=t.getElementsByTagName(n)[0],i.async=1,i.src=r,a.parentNode.insertBefore(i,a)}(window,document,"script","https://static.shoplive.cloud/live.js","mplayer"),this.liveLoaded=!0)},mounted:function(){this.shopLiveLoading()},methods:{shopLiveLoading:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){var n,r,o,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return n={REQUEST_LOGIN:function(){alert("로그인이 필요합니다")},ERROR:function(e){console.log(e.code),console.log(e.msg)},DOWNLOAD_COUPON:function(e){alert(e.coupon+"쿠폰 다운로드 성공!")},CLOSE_PLAYER:function(e){window.self.close()}},r={messageCallback:n,ui:{viewerCount:!0,likeCount:!0,shareButton:!1,pipButton:!1,backButton:!0,optionButton:!0,liveIndicator:!1},hideControls:!1},t.prev=2,o=e.requests.detail,t.next=6,u["a"].getShopliveInfo(o);case 6:i=t.sent,setTimeout((function(){mplayer("init","xe9XZmy2pEIuNRirpdVC",i.campaignKey,i.jwtAuthId,r),mplayer("run","shopLiveArea")}),500),t.next=14;break;case 10:t.prev=10,t.t0=t["catch"](2),console.error("shoplive error...",t.t0),alert(t.t0.message);case 14:case"end":return t.stop()}}),t,null,[[2,10]])})))()}}},l=c,s=(n("592f"),n("2877")),p=Object(s["a"])(l,o,i,!1,null,null,null),f=p.exports,d=n("633d");d["c"].components=Object(r["a"])({App:f},d["c"].components),d["d"].dispatch("init").then((function(){return new d["a"](d["c"])}))},"6c82":function(e,t,n){}});