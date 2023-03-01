# Pulmuone_Multi_Module_Project
>Creator: dh.kwon  
>Date: 2020/6/05

## Description
* Pulmuone_Master 프로젝트에서 공통 Dependency를 관리한다.
* Pulmuone_Common 프로젝트는 BOS와 MALL 프로젝트의 공통 영역으로 사용한다.
* 소스트리에서 Pull한 후 STS에서 정상적으로 import되지 않을 경우   
  : pulmuone_Master 폴더 하위의 .settings 폴더가 필요(정상 동작하는 환경의PC에서 가져와야 함)

## 전체 프로젝트 COMMON 공통 영역(package)의 정의
* api
* comm.vo
* exception
* util

## 개별 프로젝트 BOS, MALL 단독 영역(package)의 정의
* config
* interceptor
* common.base

## Mapper 관련 경로
### - COMMON
1.interface : kr.co.pulmuone.common.persistence.mapper  
2.xml file : src/main/resources/persistence/mapper

### - BOS
1.interface : kr.co.pulmuone.bos.persistence.bos.mapper  
2.xml file : src/main/resources/persistence/bos/mapper

### - MALL
1.interface : kr.co.pulmuone.mall.persistence.mall.mapper  
2.xml file : src/main/resources/persistence/mall/mapper