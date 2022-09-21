OUTPUT 다운 경로
------------
- `파일명` : BlogServiceApp.jar<br><br>
- `경로` : [github]/output/BlogServiceApp.jar<br><br>
- `[git 다운]`: https://github.com/tel407/kakaobank/blob/8dc8bc3227e042d03eff807889ffd056dfb59b16/output/BlogServiceApp.jar<br><br>
- `[google 다운]`: https://drive.google.com/file/d/1CDAQwYCrnFWoGbgrObvxOXPJiSFQf2a1/view?usp=sharing<br><br>



API 정의서
------------
### `과제 1` 블로그 검색 

 `URL` <strong>-GET -- /search/blog<strong>
<br><br><br>
<strong>파라미터<strong>

   값 | 의미 | 기본값
---|:---:|---:
`word` | 검색을 원하는 질의어 | ``
`sort` | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순) |`accuracy`
`page` | 보여질 페이지 넘버 | `1`
`cnt` | 보여질 컨텐츠 갯수|`10`
 
  예시 ) [예시링크]: <br>
 http://localhost:8080/search/blog?word=카카오&sort=recency&page=2&cnt=5 <br>
 http://localhost:8080/search/blog?word=나이키파이썬&sort=recency&page=2&cnt=15<br>
 http://localhost:8080/search/blog?word=전화승
  <br><br><br>
### `과제 2` [인기 검색어 목록]
 
 <br>
 
 `URL`  <strong>-GET /rank/keyword <strong>
 
 <br>
  예시 ) [예시링크] :
 
 http://localhost:8080/rank/keyword

<br><br><br><br>


과제 추가 설명
------
### 모듈 설명
- app-code : (어플리케이션)
- module-api : (request & respons 처리만 담당하는 Module)
- module-data : (비지니스 로직 및 domain 처리 담당 하는 Module)
- module-common : (Utile 등 공통으로 가져야하는 공통 Module)

###  추가한 dependencies
-implementation 'com.github.shin285:KOMORAN:3.3.4' (검색어의 키워드를 뽑기위해 형태소 분석 을 위한 라이브러리 추가)
