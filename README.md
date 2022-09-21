# kakaobank

<br><br>


API 정의서
------
-- [블로그 검색]
-GET /search/blog

<strong>파라미터<strong>
  값 | 의미 | 기본값
---|:---:|---:
`searchword` | 검색을 원하는 질의어 | `static`
`searchsort` | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순) |`accuracy`
`pageNumber` | 보여질 페이지 넘버 | `1`
`pageSize` | 보여질 컨텐츠 갯수|`10`
