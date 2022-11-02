## 3Week_Mission

### ⭐️ 3Week 필수과제 ⭐️

-   [x] 관리자 홈
    -   GET /adm/home/main
-   [x] RebateOrderItem 엔티티 설계
-   [x] 정산 데이터 생성폼
    -   GET /adm/rebate/makeData
        -   RebateDataForm : year, month
-   [x] 정산 데이터 생성
    -   POST /adm/rebate/makeData
        -   RebateDataForm : year, month
-   [x] 정산 데이터 리스트
    -   GET /adm/rebate/rebateOrderItemList
        -   RequestParam: year, month
-   [x] 전체 정산
    -   POST /adm/rebate/rebate
        -   RequestParam: ids(rebateOrderItemId)
-   [x] 건별 정산
    -   POST /adm/rebate/rebateOne/{rebateOrderItemId}

### 👍🏻 3Week 추가과제 👍🏻
*표시: 개인적으로 추가한 기능

-   [x] 정산 데이터 Spring Batch 로 생성
-   [x] 정산 데이터 생성 Job 스케줄링
-   [x] *출금 계좌 관리
    -   GET /member/manageWithdrawAccount
-   [x] *출금 계좌 등록폼
    -   GET /member/registerWithdrawAccount
-   [x] *출금 계좌 등록
    -   POST /member/registerWithdrawAccount
        -   WithdrawAccountForm: bankName, bankAccountNo
-   [x] 출금 신청 폼
    -   GET /withdraw/apply
-   [ ] 출금 신청 처리
    -   POST /withdraw/apply
        -   Form: bankName, bankAccountNo, price
-   [ ] 출금 신청 리스트
    -   GET /adm/withdraw/applyList
-   [ ] 출금 처리
    -   POST /adm/withdraw/{withdrawApplyId}

### 🙈 요구사항 및 접근방법 정리 🙈

### 정산

-   정산 데이터 생성폼
    -   select box 년, 월 선택 후 폼 전송([참고](https://jh91.tistory.com/entry/JS-select-box%EC%97%90-%EC%83%9D%EB%85%84%EC%9B%94%EC%9D%BC-%EC%83%9D%EC%84%B1%ED%95%98%EB%8A%94-%ED%95%A8%EC%88%98))
    -   정산데이터 생성, 조회 2가지 폼 전송 처리
-   정산 데이터 생성
    
    -   관리자가 월 단위로 정산 데이터 생성
    -   정산 데이터 생성 완료 후, 정산 데이터 리스트 페이지로 리다이렉트
    
    1.  select box로 년/월을 선택 후 정산 데이터 수동 생성
        
        1.  현재 날짜가 선택한 년/월의 정산 데이터를 생성가능한 날짜인지 검증
            -   2022년 9월 정산 데이터는 2022년 10월 15일 새벽 4시 이후 생성 가능
            -   LocalDateTime → String 으로 출력([참고](https://krksap.tistory.com/1158))
        2.  정산 데이터를 생성할 날짜 범위 구하기
            -   Calendar 를 이용해 해당 년, 월의 말일 구하기([참고](https://dpdpwl.tistory.com/111))
            -   LocalDateTime 하루의 시작, 종료시각 구하기([참고](https://mamoruoppa.tistory.com/60))
        3.  해당 날짜 범위의 주문 품목(OrderItem) 데이터 조회
        4.  주문 품목 데이터(OrderItem) -> 정산 데이터(RebateOrderItem) 변환
        5.  정산 데이터 생성 및 DB에 저장
    2.  Spring Batch + Scheduler 를 이용한 정산 데이터 자동 생성
        
        -   참고
            -   [Spring batch+Scheduler 구현 예제](https://dalgun.dev/blog/2019-10-30/spring-batch)
                -   [https://github.com/dalgun/play](https://github.com/dalgun/play)
            -   [Spring-boot Scheduler](https://velog.io/@rivernine/Spring-boot-Scheduler#11-enable-scheduling)
            -   [cron 표현식](https://itworldyo.tistory.com/40)
        
        [Spring Batch 개념 정리](https://www.notion.so/Spring-Batch-24ae6daa30d149d39da1886c70ffd2cc)
        
        -   매달 15일 오전 4시 0분 0초에 저번 달(1일~말일)의 정산 데이터를 생성하는 배치 실행
            -   Spring Batch
                1.  spring-batch 의존성 추가(build.gradle), batch 설정 추가(application.yml)
                2.  앱에 `@EnableBatchProcessing` 추가
                3.  저번 달(1일~말일)의 정산 데이터를 생성 Job, Step 추가
            -   Scheduler
                -   Spring-boot Framework Scheduler(내장 스케쥴러)의 `@EnableScheduling`, `@Scheduled` 를 이용
                    -   `@Scheduled(cron = "0 0 4 15 * *")` : 매달 15일 오전 4시 0분 0초 실행
                    -   JobParameters 에 (”createDate”, 현재일시 String 값)을 담아 JobLauncher로 Job 실행
                        -   JobParameter 에서는 날짜 → String 으로 변환하여 사용([참고](https://jojoldu.tistory.com/490))
-   정산 데이터 리스트
    -   [UI] 정산번호, 품목번호, 상품명, 결제일시, 결제금액, PG 수수료, 도매가, 환불일시, 환불금액, 판매자, 예상정산금액, 정산 예치금 내역번호, 비고 표시
    -   정산 상태에 따른 정산 완료/환불 완료 표시, 건별 정산 신청 버튼 표시
    -   2가지 방식
        -   전체 정산 데이터 리스트 조회
        -   year, month 에 해당하는 월별 정산 데이터 리스트 조회
-   정산 처리
    
    -   전액 환불건, 정산 완료건은 정산 불가
    -   예상 정산 금액 = 도매가 - PG 수수료
        -   PG 수수료는 0원으로 가정
        -   판매자 : MUT Books = 5 : 5 로 정산 → 도매가 = 판매가 * 0.5
    -   관리자는 각 월의 15일에 지난달 정산액을 작가(회원)들에게 송금하여 정산 처리
        -   판매자에게 예치금으로 정산금액 지급
    -   정산 완료 후, 전체 정산 데이터 리스트 페이지로 리다이렉트
    
    1.  전체 정산(선택 방식)
        -   여러 개의 정산 건을 선택하여 한번에 정산 처리
    2.  건별 정산(단건 방식)

### 출금

-   로그인한 회원만 출금 기능 사용 가능

----------

-   출금 계좌 관리
    -   [UI] 출금 계좌 정보(은행명, 계좌 번호), 출금 계좌 등록 버튼 표시
-   출금 계좌 등록
    -   출금 계좌 정보(은행명, 계좌 번호) 입력 후, 등록
    -   등록 완료시 출금 계좌 관리 페이지로 리다이렉트
-   출금 신청폼
    -   [UI] 출금 계좌 정보(은행명, 계좌번호), 캐시 잔액 표시
    -   Form : price
        -   전액 checkbox 선택 시 자동 값 입력, 최소/최대 입력값 제한
    -   *출금 계좌 등록이 안된 회원은 출금계좌 등록 페이지로 리다이렉트
-   출금 신청(사용자 기능)
    -   본인이 보유한 예치금 한도 내에서 출금신청 가능
        -   작가 회원이 정산받은 금액 or 본인이 충전한 금액
-   출금 처리(관리자 기능)
    -   관리자 회원만 관리자페이지에서 출금신청목록 조회 가능
    -   해당 내역에서 건별로 출금 처리
    -   출금 수수료 없다고 가정

### ❗️ 특이사항 ❗️

<strong>아쉬웠던 점</strong>
정산이라는 개념 자체가 어려워서 수업 시간에 했던 프로젝트의 강사님 코드를 참고해 정산 로직 흐름을 이해하고 왜 이런식으로 코드를 짰는지 분석하는데 시간을 많이 쏟았던 것 같다. 이전 프로젝트에서 배치를 사용하여 정산 데이터를 생성하는 것을 해봤었지만 그것을 이 프로젝트에 어떻게 적용해야할지 조금 어려웠던 것 같다. 배치는 수업 외에 한 번도 써본적이 없었고 스케줄링은 아예 처음 써보는 기능이라 어떤 식으로 구현을 해야할지 막막했었다. 개념 자체도 제대로 정립이 안된 상태에서 개발을 하려고 하니 어려움이 많이 있어 간단하게라도 여러 자료를 찾아보며 배치 용어를 배우고 예제 코드를 적용해보며 어떤 식으로 사용하는지 학습했다. 아직 리팩토링해야 할 부분도 많고 테스트 코드를 하나도 짜지 못해 정말 아쉽다.

<strong>궁금했던 점</strong>

-   현재는 임시로 출금 계좌 정보를 Member 테이블에 저장해두었는데, 출금 계좌를 관리하는 테이블을 따로 만들어서 관리해야하는가?
-   정산 데이터 생성은 자동 생성(배치), 수동 생성(관리자 요청) 2가지 방식이 있는것인가?
-   AuthLevel 같은 enum 클래스는 어느 패키지의 하위에 생성해야는가?

<strong>Refactoring</strong>

-   출금 계좌 등록하지 않은 상태에서는 출금 신청을 하지 못하도록 막기
-   토스 카드 결제 환불 처리 구현
-   관리자 회원으로 로그인하면 관리자 회원 메인페이지로 리다이렉트하기
-   장바구니 단건 삭제에서는 productId 를 넘기고 선택 삭제에서는 cartItemId 를 넘기는 부분을 모두 cartItemId를 넘기도록 통일
-   금액 콤마 표시(프론트)
