## 4Week_Mission
*표시: 개인적으로 추가한 기능

### ⭐️ 3Week 추가과제 ⭐️
- [x] withdrawApply 엔티티 설계
- [x] MemberExtra 엔티티 설계
    - 회원의 출금 계좌 정보(은행명, 계좌번호) 관리 목적
- [x] 출금 신청
    - POST /withdraw/apply
        - Form: price
- [x] 출금 신청 내역 리스트(사용자 기능)*
    - GET /withdraw/applyList
- [x] 출금 신청 내역 리스트(관리자 기능)
    - GET /adm/withdraw/applyList
- [x] 출금 처리
    - POST /adm/withdraw/{withdrawApplyId}
- [x] 출금 취소(사용자 기능)*
    - POST /withdraw/cancel/{withdrawApplyId}
- [x] 출금 취소(관리자 기능)*
  - POST /adm/withdraw/cancel/{withdrawApplyId}

### ⭐️ 4Week 필수과제 ⭐️
- [x] JWT 로그인 구현(POST /api/v1/member/login)
- [x] 로그인 한 회원의 정보 구현(GET /api/v1/member/me)
    - [x] MemberDto 추가
- [x] 내 도서 리스트 구현(GET /api/v1/myBooks)
    - [x] MyBookDto 추가
    - [x] ProductDto 추가
- [x] 내 도서 상세정보 구현(GET /api/v1/myBooks/{myBookId})
    - [x] MyBookDetailDto 추가
    - [x] ProductDetailDto 추가
    - [x] PostDetailDto 추가
- [x] Srping Doc 으로 API 문서화(/swagger-ui/index.html )
    - [x] SpringDocConfig 추가
    - [x] 관리자 회원만 spring doc 접근가능하도록 SecurityConfig 설정

### 👍🏻 4Week 추가과제 👍🏻
- [x] ERD 완성*
- [ ] 엑세스 토큰 화이트리스트 구현(Member 엔티티에 accessToken 필드 추가)
- [x] 리액트 코드 작동 확인

### 🙈 요구사항 및 접근방법 정리 🙈
### JWT 프로세스
1.  사용자가 `username, password` 를 입력하고 서버로 로그인 요청을 보낸다.
2.  로그인 성공시 서버는 비밀키로 서명을 하고 공개키로 암호화 하여  `Access Token` 을 발급한다.
3. `Authorization Header` 에 `Access Token` 을 담아 클라이언트에게 응답을 보낸다.
4. 클라이언트는 API를 요청할 때 `Authorization Header` 에 `Access Token` 을 담아 요청을 보낸다.
5. 서버에서는 `Access Token` 을 검증하고 사용자를 인증한다.
6. 서버가 요청에 대한 응답을 클라이언트에게 전달한다.
- https://velog.io/@junghyeonsu/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8%EC%9D%84-%EC%B2%98%EB%A6%AC%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95

---
### Spring Security + JWT 로그인 구현
- https://samtao.tistory.com/65

**1. JWT dependency 추가**
- `build.gradle` 파일에 jwt 구현을 위해 필요한 의존성을 추가한다.
```bash
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'  
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'  
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
```
**2. JWT secretKey 관리**
- ``JwtConfig`` 는 JWT AccessToken 발급에 사용되는 비밀키를 싱글톤 빈으로 등록하여 관리한다.
```java
@Configuration  
public class JwtConfig {  
  @Value("${custom.jwt.secretKey}")  
  private String secretKeyPlain; // 비밀키 원문  
  
  // JWT 비밀키 싱글톤 빈 관리  
  @Bean  
  public SecretKey jwtSecretKey() {  
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());  
 return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());  
  }  
}
```
- secretKey(원문)은 ``application.yml`` 파일에서 관리한다.

**3. JWT 토큰(AccessToken) 발급**
- ``JwtProvider`` 은 JWT 토큰을 발급하는 역할을 하고 비밀키를 이용해 토큰을 생성한다.
```java
// JWT Access Token 발급  
public String generateAccessToken(Map<String, Object> claims, int seconds) {  
    long now = new Date().getTime();  
  Date accessTokenExpiresIn = new Date(now + 1000L * seconds);  
  
 return Jwts.builder()  
            .claim("body", Ut.json.toStr(claims))         // Claims 정보 설정  
  .setExpiration(accessTokenExpiresIn)                // accessToken 만료 시간 설정  
  .signWith(getSecretKey(), SignatureAlgorithm.HS512) // HS512, 비밀키로 서명  
  .compact(); // 토큰 생성  
}
```
1. `Jwts.builder()` 를 이용해 `JwtBuilder` 객체를 생성한다.
2.  Headers, Claims, 토큰 용도, 토큰 만료 시간 등을 설정한다.
3.  `HS512(서명 알고리즘), 비밀키` 로 서명한다. 
4.  `compact()` 로 토큰을 생성한다.

**4. JWT 토큰(AccessToken) 검증**
- ``JwtProvider`` 은 JWT 토큰을 검증하는 역할을 하고 공개키를 이용해 토큰을 검증한다.
```java
// JWT Access Token 검증  
public boolean verify(String accessToken) {  
    try {  
        Jwts.parserBuilder()  
                .setSigningKey(getSecretKey())  // 비밀키  
  .build()  
                .parseClaimsJws(accessToken); // 파싱 및 검증(실패시 에러)  
  } catch (ExpiredJwtException e) {  
        // 토큰이 만료되었을 경우  
  return false;  
  }  
    catch (Exception e) {  
        // 그 외 에러  
  return false;  
  }  
    return true;  
}
```
1. ``Jwts.parserBuilder()`` 를 이용해 ``JwtParserBuilder`` 객체를 생성한다.
2. 서명 검증을 위한 비밀키를 지정한다.
3. `parseClaimsJws()` 로 파싱 및 서명 검증을 한다. (예외처리를 위해 try-catch)

**5. JWT 토큰(AccessToken) 으로부터 Claim 정보 가져오기**
- ``JwtProvider`` 은 JWT 토큰으로부터 Claim 정보를 가져오는 역할을 한다.
```java
// accessToken 으로부터 Claim 정보 얻기  
public Map<String, Object> getClaims(String accessToken) {  
    String body = Jwts.parserBuilder()  
            .setSigningKey(getSecretKey())  
            .build()  
            .parseClaimsJws(accessToken)  
            .getBody()  
            .get("body", String.class);  
  
 return Ut.json.toMap(body);  
}
```
1~3. 위와 동일
4. `getBody()` 로 claim 정보를 가져온다.

**6. REST API 요청 Security 설정**
- REST API 요청이 들어왔을 때는 JWT 방식으로 인증을 수행해야하므로 `ApiSecurityConfig` 에 관련 설정을 추가한다.
```java
  @Bean  
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {  
        http  
                ...
                .authorizeRequests(  
                        authorizeRequests -> authorizeRequests  
                                // 로그인 요청 외 모든 요청은 로그인 필수  
  .antMatchers("/api/*/member/login").permitAll()  
                                .anyRequest()  
                                .authenticated() // 최소자격 : 로그인  
  )  
                .sessionManagement(sessionManagement -> sessionManagement  
                        .sessionCreationPolicy(STATELESS)  
                )  
                .addFilterBefore(  
                        jwtAuthorizationFilter,  
  UsernamePasswordAuthenticationFilter.class  
  )  
                ...  
 return http.build();  
  }
```

- `/api/*/member/login` 요청 외 모든  `/api/**` 요청은 인증된 사용자여야 한다.
- 지정된 필터보다 먼저 실행되도록 `jwtAuthorizationFilter` (커스텀 필터) 를 추가한다.

**7. jwtAuthorizationFilter 추가**
- REST API 요청이 Controller 에 도달하기 이전에 앞 단(Filter 혹은 Interceptor)에서 인증/인가를 수행한다.
```java
@Slf4j  
@Component  
@RequiredArgsConstructor  
public class JwtAuthorizationFilter extends OncePerRequestFilter {  
    private final JwtProvider jwtProvider;  
 private final MemberService memberService;  
  
  @Override  
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {  
        String barerToken = request.getHeader("Authorization");  
  // 토큰 유효성 검증  
  if(barerToken != null) {  
            String token = barerToken.substring("Barer ".length());  
  // 토큰이 유효하면 회원 정보 얻어서 강제 로그인 처리  
  if(jwtProvider.verify(token)) {  
                Map<String, Object> claims = jwtProvider.getClaims(token);  
  String username = (String) claims.get("username");  
  Member member = memberService.findByUsername(username);  
  
 if(member != null) {  
                    forceAuthentication(member);  
  }  
            }  
        }  
        filterChain.doFilter(request, response);  
  }  
  
    // 강제 로그인 처리  
  private void forceAuthentication(Member member) {  
        MemberContext memberContext = new MemberContext(member);  
  
  UsernamePasswordAuthenticationToken authentication =  
                UsernamePasswordAuthenticationToken.authenticated(  
                        memberContext,  
 null,  member.getAuthorities()  
                );  
  
  // 이후 컨트롤러 단에서 MemberContext 객체 사용O  
  SecurityContext context = SecurityContextHolder.createEmptyContext();  
  context.setAuthentication(authentication);  
  SecurityContextHolder.setContext(context);  
  }  
}
```
1. 요청 헤더의 `Access Token` 을 검증한다.
2. 토큰으로부터 `claim(회원 정보)` 를 얻어 DB에서 Member 객체 조회한다.  
3. 해당 회원 강제 로그인 처리한다.(`MemberContext` 세션 등록)
---
### Spring Doc API 문서화
1. spring doc dependency 추가(build.gradle)
```bash
implementation 'org.springdoc:springdoc-openapi-ui:1.6.11'
```
2. SpringDocConfig 추가

---
<strong>궁금했던 점</strong>
- api 패키지 위치 어디에 해야하는가?
- AuthLevel 같은 enum 클래스는 어느 패키지의 하위에 생성해야는가?

<strong>Refactoring</strong>
-   토스 카드 결제 환불 처리 구현
-   관리자 회원으로 로그인하면 관리자 회원 메인페이지로 리다이렉트하기
-   장바구니 단건 삭제에서는 productId 를 넘기고 선택 삭제에서는 cartItemId 를 넘기는 부분을 모두 cartItemId를 넘기도록 통일
-   금액 콤마 표시(프론트)
