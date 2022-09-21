# kakaobank

<br><br>

OUTPUT 다운 경로
------
- 파일명 : kakaobank.jar<br><br>
- 경로 : [github]/output/kakaobank.jar<br><br>
[git 다운링크]:  https://github.com/tel407/kakaobank/blob/23ef6a0b963f34c761aba2d0a5eea60d031ddf62/output/kakaobank.jar<br><br>
[google 다운링크]: https://drive.google.com/file/d/1dKXsYy2Nw-bNOeiZ8aXT3AGziJ69w_jw/view?usp=sharing<br><br>



API 정의서
------
### 과제 1. [ 블로그 검색]
 URL -GET <strong>/search/blog<strong>

<strong>파라미터<strong>
  값 | 의미 | 기본값
---|:---:|---:
`searchword` | 검색을 원하는 질의어 | ``
`searchsort` | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순) |`accuracy`
`pageNumber` | 보여질 페이지 넘버 | `1`
`pageSize` | 보여질 컨텐츠 갯수|`10`
  
  예시 ) [예시링크]: http://localhost:8080/search/blog?word=카카오과제&sort=recency&page=2&cnt=15
  <br><br><br>
### 과제 2. [인기 검색어 목록]
 URL -GET /rank/keyword
  
  예시 ) [예시링크]: http://localhost:8080/rank/keyword

<br><br><br><br>


과제 추가 설명
------
### 모듈 설명
- app-code : (어플리케이션)
- module-api : (request & respons 처리만 담당하는 Module)
- module-data : (비지니스 로직 및 domain 처리 담당 하는 Module)
- module-common : (Utile , Constant 등 모든 모듈이 공통을 가져야하는 공통 Module)

###  추가한 dependencies
-implementation 'com.github.shin285:KOMORAN:3.3.4' (검색어의 키워드를 뽑기위해 형태소 분석 을 위한 라이브러리 추가)
