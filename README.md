jar download link : https://drive.google.com/drive/folders/0B9CM2xw7Q7uIQVktQmpwTTZhS0k?resourcekey=0-kUNH3dTiDylTwYcZps52QA&usp=share_link

1. 외부 라이브러리 및 오픈소스 사용 목적
   1. spring batch
      1. 검색 키워드 히스토리 데이터에서 검색 키워드 조회수 데이터로 마이그레이션하기 위해 사용
      2. 검색 키워드 히스토리에서 마이그레이션이 완료된 건은 특정 일자 기준으로 삭제 배치 처리
   2. mapstruct
      1. 객체 매핑을 위해 사용
   3. httpclient
      1. restTemplate 설정을 위해 사용
   4. jackson-databind
      1. 직렬화 역직렬화를 위해 사용
   5. swagger, querydsl
      1. swagger및 querydsl을 위해 사용
      
2. 참고 사항
   1. 블로그 검색 API 호출 시 header에 access-id를 요구하도록 설계
      1. 특정 사용자의 조회수 어뷰징을 방지하기 위해 추가하였으며, 사용자를 식별하기 위한 key값이다.(비회원일 경우 client-ip 기준으로 값을 정하면 될 것 같다.)
   2. 배치를 위한 스케쥴링 설정
      1. 현재는 테스트를 위해 초단위로 설정하였습니다.
      2. 실제 운영 서버일 경우 배치 처리 사이즈 및 스케쥴링 시간 조정이 필요합니다.
   3. app으로 실행되는 모듈은 module-application과 module-batch 2개 입니다. h2 database server로 사용되고 있는 module-application부터 실행합니다.