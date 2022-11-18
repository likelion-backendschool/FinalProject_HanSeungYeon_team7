# MUT Books

  

## 프로젝트 소개

- Mut Books 는 eBook 마켓과 eBook리더로 이루어진 서비스입니다.

  

## 🕰개발 기간

- 22.10.17 ~ 22.11.11(4주) : 1차 프로젝트 개발

- 22.11.14 ~ 22.12.16(5주) : 1차 프로젝트 리팩토링 및 2차 프로젝트 개발(진행중)

  

## 🛠Stack

<div align=center>

<img src="https://img.shields.io/badge/java 17-007396?style=for-the-badge&logo=java&logoColor=white">

<img src="https://img.shields.io/badge/gradle 7.5-02303A?style=for-the-badge&logo=gradle&logoColor=white">

<img src="https://img.shields.io/badge/springboot 2.7.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

<img src="https://img.shields.io/badge/intellij idea-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">

<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB 10.8.3&logoColor=white">

<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">

<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">

<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">

<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">

<img src="https://img.shields.io/badge/fontawesome-339AF0?style=for-the-badge&logo=fontawesome&logoColor=white">

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

</div>

  

## ⚙️개발 환경

- Language: Java 17(JDK 17.0.3)

- Build: Gradle

- IDE: Intellij

- Framework: SrpingBoot 2.7.4

- Database: MariaDB

- ORM: JPA

  

## Git Convention

### Branch
|이름             |설명                          |
|----------------|-------------------------------|
|`main`    |배포용 메인 브랜치           |
|`devbranch`    |배포 전 개발 브랜치           |

### Commit
`#이슈번호 - Type : 내용` 형태로 커밋 메시지 작성
|Type             |설명                          |
|----------------|-------------------------------|
|`Feat`      |기능개발           |
|`Fix`       |버그수정             |
|`Docs`      |문서수정             |
|`Style`     |스타일수정 (들여쓰기, 세미콜론 등)  |
|`Refactor`     |리팩토링  |
|`Test`     |테스트 코드  |
|`Chore`     |빌드, 패키지매니저 수정 (gitignore 등)  |


  

  

## ERD 설계

<img src="https://user-images.githubusercontent.com/48237976/200752261-5dd5f21a-05fb-4d71-85a4-657e81b64aca.png">
  

## 🔍Preview


## 🛠Features
### 회원
-   회원가입
    -   가입 완료시 축하메일 발송
        -   Gmail SMTP 메일 발송
    -   가입 완료시 자동 로그인 처리 ([참고](https://coding-nyan.tistory.com/122))
-   로그인/로그아웃
    -   Spring Security 사용
    -   로그인: `/member/login`
    -   로그아웃: `/member/logout`
-   내 프로필 조회
    -   회원 기본정보 조회(아이디, 이메일, 작가명)
-   회원 기본정보 수정
    -   이메일, 작가명 수정 가능
    -   수정 완료 후, 세션값(MemberContext) 강제 수정
-   비밀번호 변경
    -   현재 비밀번호, 새 비밀번호, 새 비밀번호 확인 입력폼 유효성 검증
-   아이디 찾기
    -   `이메일` 로 아이디 찾기
    -   결과 페이지에서 아이디 확인 가능
-   비밀번호 찾기
    -   `아이디 + 이메일` 로 비밀번호 찾기
    -   해당 계정의 이메일로 임시 비밀번호 발급 -> 발급된 임시 비밀번호로 계정 비밀번호 수정 처리(DB 반영)
        -   임시 비밀번호: UUID 10자리 랜덤

### 글
-   글 작성
    -   Toast UI Editor 적용하여 내용 입력
    -   마크다운 원문, 렌더링 결과(HTML) 모두 DB에 저장([참고](http://forward.nhnent.com/hands-on-labs/toastui.editor-ext/05.html))
-   글 리스트 조회
    -   번호, 제목, 해시태그, 작성자, 작성날짜, 수정날짜 표시
    -   QueryDSL 이용
        1.  내 글 모두 조회
            -   메뉴바 "내 글" 클릭 -> 1번 페이지로 이동
        2.  해시태그(키워드)와 관련된 내 글 모두 조회
            -   1번 페이지의 게시글 "해시태그" 클릭 -> 2번 페이지로 이동
-   글 상세조회
    -   제목, 내용, 해시태그 표시
    -   Toast UI Viewer 적용하여 내용 출력
-   글 수정
    -   제목, 내용, 해시태그 수정 가능
    -   Toast UI Editor 적용하여 내용 입력
    -   수정 완료시 현재시간으로 수정날짜 변경
-   글 삭제
    -   글 삭제시 관련 해시태그도 자동 삭제 처리
   
### 도서(상품)
-   도서 등록/수정/삭제 기능은 작가 회원만 이용O(수정/삭제는 본인만 가능)
-   도서 리스트/상세조회 기능은 모두 이용O

----------

-   도서 등록
    -   글 해시태그를 선택하면 내 글 중 해당 해시태그가 붙은 모든 글을 자동으로 1개의 상품으로 등록
    -   [폼] 상품명, 설명, 권장 판매가, 글 해시태그 키워드(id), 도서 해시태그 키워드
        -   설명 부분에 ToastUI Editor 적용X
        -   글 해시태그 키워드는 1개만 선택 가능
        -   여러 개의 도서 해시태그 키워드 등록 가능
        -   해시태그 키워드 & 관련 게시글 개수 표시
            -   QueryDSL 이용
            -   다른 엔티티의 여러 컬럼 값을 select 하기 때문에, List<PostKeyword>로 반환 불가 → PostKeywordDto를 정의해 List<PostKeywordDto> 반환([참고](https://wildeveloperetrain.tistory.com/94))
    -   등록 완료시 도서 상세 페이지로 리다이렉트
-   도서 수정
    -   글 키워드 수정 X
    -   상품명, 가격, 설명, 도서 해시태그 수정 O
    -   수정 완료시 도서 상세페이지로 리다이렉트
-   도서 상세조회
    -   [UI] 상품 랜덤 이미지, 상품명, 작가명, 등록일자, 판매가, 설명, 도서 해시태그 키워드 표시
        -   랜덤 이미지 표시 Unsplash 사용([참고](https://wallel.com/unsplash-%EB%9E%9C%EB%8D%A4-%EC%9D%B4%EB%AF%B8%EC%A7%80-url-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0/))
        -   설명 부분에 ToastUI Editor Viwer 적용X
    -   [UI] 작가 본인에게만 수정, 삭제 버튼 표시
    -   미리보기 구현X
-   도서 리스트
    -   [UI] 모든 상품 정보(상품 랜덤 이미지, 상품명, 등록 일자, 작가명, 판매가, 해시태그) 최신순 리스팅
        -   랜덤 이미지 표시 Unsplash 사용
    -   상품명 클릭시 해당 상품 상세조회 페이지로 이동
    -   장바구니 버튼 표시
-   도서 삭제
    -   삭제 버튼 클릭시 confirm 창으로 삭제 여부를 한 번 더 체크
    -   삭제 완료시 상품 리스트로 리다이렉트 

### 장바구니
-   장바구니 기능은 로그인한 회원만 이용O

----------

-   품목 리스트
    -   메뉴바의 장바구니 메뉴를 클릭해 장바구니 페이지로 이동
    -   [UI] 상품 정보(상품 랜덤 이미지, 상품명, 작가명, 등록일자, 가격) 표시
        -   랜덤 이미지 표시 Unsplash 사용
        -   장바구니에 담긴 상품이 없을 때는 상품 없음 안내 문구 표시
    -   [UI] 전체 선택 체크박스, 품목 개별 체크박스, 품목 삭제 개별 버튼, 주문하기 버튼 표시
        -   Javascript 로 전체 선택 체크박스와 개별 체크박스 연동
    -   이미지 클릭시 해당 상품 상세조회 페이지로 이동
-   품목 추가
    -   도서 목록 페이지의 각 상품의 장바구니 버튼을 클릭해 장바구니에 품목 추가
    -   회원은 같은 상품 1개만 장바구니에 추가 가능(계속 담아도 최대 수량 = 1개)
    -   품목 추가 완료시 품목 리스트 페이지로 리다이렉트
-   품목 삭제
    -   품목 삭제 버튼 클릭시 confirm 으로 확인 후 삭제
    -   품목 삭제 완료시 품목 리스트 페이지로 리다이렉트
  
### 주문/결제
-   주문/결제 기능은 로그인한 회원만 이용O
-   주문 리스트/상세조회/취소/결제/환불 처리는 주문자 본인 것만 가능
----------
-   주문 생성
    -   장바구니 페이지에서 주문할 품목을 선택 → 주문하기 버튼을 클릭 → 주문 생성(단일 주문 미지원)
        -   선택한 cartItemId 들을 String 으로 만들어 주문 생성 폼 발송
        -   ‘,’ 기준으로 분리한 CartItemIds String[] → List<String> 으로 변환([참고](https://velog.io/@jwkim/java-arraylist-array-type-conversion))
-   주문 리스트
    -   회원 본인의 주문 내역 최신순 리스팅
    -   [UI] 주문일자, 주문 상태, 주문 품목들 정보(상품 랜덤 이미지, 상품명, 가격) 표시
-   주문 상세
    -   [UI] 주문 상품 정보(상품 랜덤 이미지, 상품명, 가격), 주문 정보(회원 id, 총 상품수, 총 상품금액), 결제 정보(총 상품금액, 캐시 사용금액, pg 결제 금액), 환불 규정 표시
    -   접근 방식 2가지
        1.  장바구니 페이지에서 주문하기 버튼 클릭
        2.  주문내역 페이지에서 상세보기 버튼 클릭
    -   주문 완료 상태
        -   [UI] 보유 예치금, 사용할 예치금 입력폼 표시
        -   [UI] 주문 취소 버튼, 결제 하기 버튼 표시
    -   결제 완료 상태
        -   [UI] 환불요청 버튼 표시
        -   [UI] 표시
    -   취소 완료/환불 완료 상태
        -   정보 외에 아무것도 표시X
-   주문 취소
    -   주문 완료 상태일 때만 주문 취소 요청 가능
    -   해당 주문 건에 포함된 주문 품목 전체 취소 방식 지원
    -   취소 완료시 주문 내역 페이지로 리다이렉트
-   결제 처리
    -   주문 상세페이지에서 결제 버튼 클릭시 결제 요청(주문 생성후 미결제상태일 때만 요청 가능, 취소/환불 시 요청 불가)
    -   Toss Payments 연동([참고](https://docs.tosspayments.com/guides/windows/card))
    -   3가지 결제 방식 지원(1과 2, 3 방식으로 처리)
        -   예치금 전액 결제(PG 결제 skip)
        -   Toss Payments 카드 전액 결제(예치금 충전 기록 보존)
        -   예치금 + Toss Payments 카드 결제(혼합 방식)
-   환불 처리
    -   주문 전체 환불 방식 지원(부분 환불 미지원)
    -   환불 규정: 결제 완료 후 10분 이내 & 도서 열람 하지 않았을 경우(1개라도 환불 규정에 어긋나면 환불 불가)
    -   3가지 환불 방식 지원(1과 2,3 방식으로 처리)
        1.  예치금 전액 환불
        2.  Toss Payments 카드 전액 환불(예치금 환불 기록 보존)
        3.  예치금 환불 + Toss Payments 카드 환불(혼합 방식)
    -   환불 완료시 MyBook 에서 구매 상품 삭제
   
 ### 관리자
 -   username 이 admin 인 회원 1명을 관리자(authLevel 7)로 지정
----------
-   관리자 홈
    -   관리자 권한을 가진 회원만 관리자 페이지 접속 가능
 
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
                -   ‣
            -   [Spring-boot Scheduler](https://velog.io/@rivernine/Spring-boot-Scheduler#11-enable-scheduling)
            -   [cron 표현식](https://itworldyo.tistory.com/40)
        
        [Spring Batch 개념 정리](https://www.notion.so/Spring-Batch-9fe550049da34243a8d93f34b495a79c)
        
        -   매달 15일 오전 4시 0분 0초에 저번 달(1일~말일)의 정산 데이터를 생성하는 배치 실행
            -   Spring Batch
                1.  spring-batch 의존성 추가(build.gradle), batch 설정 추가(application.yml)
                2.  앱에 `@EnableBatchProcessing` 추가
                3.  저번 달(1일~말일)의 정산 데이터를 생성 Job, Step 추가
                    -   Chunk size 100 으로 지정([참고](https://jojoldu.tistory.com/331))
            -   Scheduler
                -   Spring-boot Framework Scheduler(내장 스케쥴러)의 `@EnableScheduling`, `@Scheduled` 를 이용
                    -   장애가 발생하여 배치가 15일 오전 4시에 실행되지 못할 경우를 고려하여 매일 오전 4시에 배치가 실행되도록 함(장애가 발생하면 해당 JobParameter 로 배치 다시 실행)
                        -   `@Scheduled(cron = "0 0 4 15 * *")` : 매달 15일 오전 4시 0분 0초 실행
                        -   `@Scheduled(cron = "0 0 4 * * *")` : 매일 오전 4시 0분 0초 실행
                    -   JobParameters 에 year, month 값을 담아 JobLauncher로 Job 실행
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
-   신청 완료/출금 완료/취소 완료 3가지 상태

----------

-   MemberExtra 엔티티 추가
    -   은행명, 계좌번호 컬럼
    -   대상 테이블(member_extra)에 외래 키가 있는 경우 일대일 양방향([참고](https://blog.advenoh.pe.kr/database/JPA-%EC%9D%BC%EB%8C%80%EC%9D%BC-One-To-One-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84/))
        -   memberExtra 엔티티에 @OneToOne 설정
        -   member 엔티티에 @OneToOne(mappedBy=”member”) 설정
-   출금 계좌 관리
    -   [UI] 출금 계좌 정보(은행명, 계좌 번호), 출금 계좌 등록 버튼 표시
    -   내 프로필 > 출금계좌 관리 접속
-   출금 계좌 등록
    -   출금 계좌 정보(은행명, 계좌 번호) 입력 후, 등록
    -   등록 완료시 출금 계좌 관리 페이지로 리다이렉트
-   출금 신청폼
    -   [UI] 출금 계좌 정보(은행명, 계좌번호), 캐시 잔액 표시
    -   Form : price
        -   전액 checkbox 선택 시 자동 값 입력, 최소/최대 입력값 제한
    -   내 프로필 > 출금신청 or 내 프로필 > 출금계좌 관리 > 출금 신청 접속
    -   출금 계좌 정보가 존재하지 않으면, 출금계좌 관리 페이지로 리다이렉트
-   출금 신청(사용자 기능)
    -   작가 회원이 정산받은 금액 or 본인이 충전한 금액
    -   최소 출금 신청 금액 = 1,000원
    -   최대 출금 금액 = 본인이 보유한 예치금
-   출금 신청 내역 리스트
    
    -   [UI] 은행명, 계좌번호, 금액, 신청 일시, 출금 일시, 취소 일시, 처리 상태
    
    1.  사용자 기능
        -   본인의 출금 신청 내역을 최신순으로 조회
    2.  관리자 기능
        -   모든 회원의 출금 신청 내역을 최신순으로 조회
        -   *신청 완료 상태 건을 맨 위로 리스팅
-   출금 처리(관리자 기능)
    -   관리자 출금 신청 내역 페이지에서 건별로 출금 처리 요청
    -   출금 수수료 없다고 가정
-   출금 신청 취소
    
    -   출금 취소 사유: 사용자 요청/관리자 요청(2가지)
        -   enum, converter 관리
    
    1.  사용자 기능
        -   본인의 출금 신청 건만 취소 가능
    2.  관리자 기능
        -   모든 회원의 출금 신청 건에 대해 취소 가능
