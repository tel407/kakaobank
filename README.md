# kakaobank

<br><br>

OUTPUT 경로
------


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
  
  예시 ) [Dribbble link]: http://localhost:8080/search/blog?searchword=카카오과제&searchsort=recency
  <br><br><br>
### 과제 2. [인기 검색어 목록]
 URL -GET /rank/keyword
  
  예시 ) [Dribbble link]: http://localhost:8080/rank/keyword

