# 스위치원 과제

### 실행환경
- JDK 17
- SpringBoot 3.2.5
- h2 database

### api 구현 여부
- ```/api/payment/balance/{userId}```
   - User에 해당 유저의 잔액을 가지고있는 wallet을 가지고있는 형태
   - userId 조회시, 해당 아이디의 잔액을 조회하여 응답
- ```/api/payment/estimate```
  - 금액의 수수료와 전체 금액을 계산에서 예상 결제 금액을 보여주는 API
  - 결제 예상 결과를 조회시, DB에 준비상태로 저장하여, 이후 승인 요청시 결제 검증 데이터로 사용하였습니다.
- ```/api/payment/approval```
  - 결제 승인을 요청 하는 API
  - estimate API 송신시 만들어놓은 데이터를 기반으로 해당 API와 금액정보나, 상점아이디 호출 userId 정보가 다르면 에러를 노출함
  - 기존 wallet에 있는 잔액과 결제금액+수수료의 금액을 비교하여, 추가 충전이 필요한 경우 필요한 금액만큼 추가 충전을 진행하고, 결제금액을 wallet에서 차감하도록함
  - 최종 결제 내용은 Payment에 저장하고, 금액 충전 내용은 PaymentDetail에 저장

### docs
- http://localhost:8080/swagger-ui/index.html
