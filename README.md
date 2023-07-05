1. 지원자 정보 : 2126-000096_조하명_서버 개발자
2. 프로젝트 설명 :
3. 빌드 결과물 Download link : https://drive.google.com/drive/folders/0B9CM2xw7Q7uIQVktQmpwTTZhS0k?resourcekey=0-kUNH3dTiDylTwYcZps52QA&usp=share_link
   1. app으로 실행되는 모듈은 module-application과 module-batch 2개 입니다. h2 database server로 사용되고 있는 module-application이 로드가 완료되면 module-batch를 실행합니다.
4. swagger url : http://localhost:7071/swagger-ui/index.html

5. 외부 라이브러리 및 오픈소스 사용 목적
   1. spring batch
      1. 동시성에서 이슈에서 벗어나기 위한 목적
      2. 검색 키워드 히스토리 데이터에서 인기 검색 순위 데이터로 변경하는 배치 작업을 위해 사용
      3. 배치로 조회된 데이터는 특정일 기준으로 삭제되도록 함
   2. mapstruct
      1. 객체 매핑을 위해 사용
   3. httpclient
      1. restTemplate 설정을 위해 사용
   4. jackson-databind
      1. 직렬화 역직렬화를 위해 사용
   5. swagger, querydsl
      1. swagger및 querydsl을 위해 사용
   6. resilience4j
      1. external api 호출 시 circuitbreaker를 위해 사용
      
6. 참고 사항 
   1. 동시성을 제어하기 위해 여러 고민을 했지만, Lock방식이 아닌 배치로 처리하는 것을 고민했습니다.
      1. 조회 요청 시 키워드 저장 로직은 비동기로 처리하고 비관적 락을 적용하게 되면 빠른 응답과 데이터의 정확도를 높일 수 있겠지만, 
         동시에 많은 트래픽이 들어오게 되면 순간적으로 쓰레드양이 많아져 위험성이 커질 수 있다고 판단하였습니다.
   2. 서킷브레이커에 대한 설정 값은 실제 블로그 API들의 스펙에 따라 유동적으로 변경될 수 있음
   3. 배치에 대한 설정은 트래픽양에 따라 유동적으로 변경될 수 있음
   4. app으로 실행되는 모듈은 module-application과 module-batch 2개 입니다. h2 database server로 사용되고 있는 module-application부터 실행하고 실행이 완료되면 module-batch를 실행합니다.
