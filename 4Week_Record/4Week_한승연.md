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
    <details>
    <summary>ERD 설계</summary>
    <div markdown="1">
    <img src="https://user-images.githubusercontent.com/48237976/200752261-5dd5f21a-05fb-4d71-85a4-657e81b64aca.png">
    </div>
    </details>

- [x] 엑세스 토큰 화이트리스트 구현(Member 엔티티에 accessToken 필드 추가)
- [x] 리액트 코드 작동 확인
    <details>
    <summary>로그인 성공 메인화면</summary>
    <div markdown="1">
    <img width="838" alt="스크린샷 2022-11-08 오후 10 56 03" src="https://user-images.githubusercontent.com/48237976/200752923-e89ec956-70ea-42d5-a65c-c13327f71f83.png">
    </div>
    </details>
    
    <details>
    <summary>회원 정보</summary>
    <div markdown="1">
    <img width="833" alt="스크린샷 2022-11-08 오후 10 57 45" src="https://user-images.githubusercontent.com/48237976/200753680-7a222e01-0703-48ff-a952-6ab28f51885b.png">
    </div>
    </details>
    
    <details>
    <summary>내 도서 리스트</summary>
    <div markdown="1">
    <img width="823" alt="스크린샷 2022-11-08 오후 10 58 35" src="https://user-images.githubusercontent.com/48237976/200753788-fb58146a-db15-4bdb-beec-38d18fa1fe93.png">
    </div>
    </details>
    
    <details>
    <summary>내 도서 상세정보</summary>
    <div markdown="1">
    <img width="830" alt="스크린샷 2022-11-08 오후 10 58 54" src="https://user-images.githubusercontent.com/48237976/200753722-8266c0b8-9318-4674-b029-aad982aeeab7.png">
    </div>
    </details>
        

### 🙈 요구사항 및 접근방법 정리 🙈
### JWT 프로세스
<img src="https://velog.velcdn.com/images%2Fjunghyeonsu%2Fpost%2Faf0fc689-e01a-484e-9519-267cba590864%2F%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202021-09-14%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%209.02.17.png">
1.  사용자가 `username, password` 를 입력하고 서버로 로그인 요청을 보낸다.
2.  로그인 성공시 서버는 비밀키로 서명을 하고 공개키로 암호화 하여  `Access Token` 을 발급한다.
3. `응답 Header` 에 `Access Token` 을 담아 클라이언트에게 보낸다.
4. 클라이언트는 API를 요청할 때 `Authorization Header` 에 `Access Token` 을 담아 요청을 보낸다.
5. 서버에서는 `Access Token` 을 검증하고 사용자를 인증한다.
6. 서버가 요청에 대한 응답을 클라이언트에게 전달한다.
- [JWT, 정확하게 무엇이고 왜 쓰이는 걸까?](https://velog.io/@junghyeonsu/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8%EC%9D%84-%EC%B2%98%EB%A6%AC%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95)

---
### Spring Security + JWT 로그인 구현
- [Java에서 JJWT(Java JSON Web Token)를 이용한 JWT(JSON Web Token) 사용방법](https://samtao.tistory.com/65)

**1. JWT dependency 추가**
- `build.gradle` 파일에 jwt 구현을 위해 필요한 의존성을 추가한다.
```bash
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'  
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'  
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
```
**2. JWT secretKey 관리**
- ``JwtConfig`` 는 JWT AccessToken 발급에 사용되는 `secretKey` 를 싱글톤 빈으로 등록하여 관리한다.
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
- `JwtProvider` 은 JWT 토큰을 발급하는 역할을 하고 `secretKey` 를 이용해 토큰을 생성한다.
```java
private final SecretKey jwtSecretKey; // 비밀키  
private long ACCESS_TOKEN_VALIDATION_SECOND = 60 * 60 * 24 * 365 * 100L; // accessToken 유효시간(100년)  
  
private SecretKey getSecretKey() {  
    return jwtSecretKey;  
}  
  
// JWT Access Token 발급  
public String generateAccessToken(Map<String, Object> claims) {  
    long now = new Date().getTime();  
  Date accessTokenExpiresIn = new Date(now + 1000L * ACCESS_TOKEN_VALIDATION_SECOND);  
  
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
- `JwtAuthorizationFilter` 에서 API 요청이 들어왔을 때 토큰을 검증하고 난 후, Claim 에서 얻은 username 으로 member 객체를 조회해 로그인 처리(세션값 강제 수정)을 하는데 사용된다.
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
- [Spring Security, JWT, 인증, 인가](https://hipopatamus.tistory.com/72)
- REST API 요청이 들어왔을 때는 JWT 방식으로 인증을 수행해야하므로 `ApiSecurityConfig` 에 관련 설정을 추가한다. 
```java
    @Bean  
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {  
        http  
                .antMatcher("/api/**")  
 // jwt 사용 기본 설정  
  .httpBasic().disable()  
                .formLogin().disable()     
                .csrf().disable()  
                .sessionManagement(sessionManagement -> sessionManagement  
                        .sessionCreationPolicy(STATELESS)         
                )  
                // cors 허용 설정  
  .cors(cors -> cors    
                        .configurationSource(corsConfigurationSource())  
                )  
                .authorizeRequests(  
                        authorizeRequests -> authorizeRequests  
                                // 로그인 요청 외 모든 요청은 로그인 필수  
  .antMatchers("/api/*/member/login").permitAll()  
                                .anyRequest()  
                                .authenticated() // 최소자격 : 로그인  
  )  
                // 필터 설정  
  .addFilterBefore(             
                        jwtAuthorizationFilter,  
  UsernamePasswordAuthenticationFilter.class  
  )  
                .logout().disable();  
  
 return http.build();  
  }  
  
    // cors 허용 정책 설정  
  @Bean  
  public CorsConfigurationSource corsConfigurationSource() {  
        CorsConfiguration corsConfiguration = new CorsConfiguration();  
  
  corsConfiguration.addAllowedOrigin("*"); // 모든 URL 허용  
  corsConfiguration.addAllowedHeader("*"); // 모든 Header 허용  
  corsConfiguration.addAllowedMethod("*"); // 모든 HTTP METHOD 허용  
  
  UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();  
  urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);  
 return urlBasedCorsConfigurationSource;  
  }
```
- jwt 방식 로그인을 위해 `formLogin.disable()` 설정을 해주어야 한다.
- 타도메인에서 API 호출을 하기 위해 `cors 허용` 설정을 해주어야 한다.
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
        String bearerToken = request.getHeader("Authorization");  
  // 1. 1차 체크(정보가 변조되지 않았는지 검증)  
  if(bearerToken != null) {  
            // accessToken 에서 회원 정보 가져오려면 Authorization 에서 Bearer 제거 필요  
  String token = bearerToken.split("  ")[1];  
  // 토큰이 유효하면 회원 정보 얻어서 강제 로그인 처리  
  if(jwtProvider.verify(token)) {  
                Map<String, Object> claims = jwtProvider.getClaims(token);  
  String username = (String) claims.get("username");  
  Member member = memberService.findByUsername(username);  
  
  // 2. 2차 체크(해당 엑세스 토큰이 화이트 리스트에 포함되는지 검증)  
  if (memberService.verifyWithWhiteList(member, token)) {  
                    // 강제 로그인 처리  
  forceAuthentication(member);  
  }  
            }  
        }  
        filterChain.doFilter(request, response);  
  }
```
1. 요청 헤더의 `Access Token` 을 검증한다.
2. 토큰으로부터 `claim(회원 정보)` 를 얻어 DB에서 Member 객체 조회한다.  
3. 해당 `Access Token` 이 화이트 리스트에 포함되는지 검증한다.
- 최초에 발급된 accessToken 을 DB(Member 테이블)에 기록해두고 요청 헤더의 accessToken 과 일치하는지 비교한다.
4. 해당 회원 강제 로그인 처리한다.(`MemberContext` 세션 등록)
---
### Spring Doc API 문서화
1. spring doc dependency 추가(build.gradle)
```bash
implementation 'org.springdoc:springdoc-openapi-ui:1.6.11'
```
2. SpringDocConfig 추가
```java
@Configuration  
public class SpringDocConfig {  
    @Bean  
  public OpenAPI springShopOpenAPI() {  
        return new OpenAPI()  
                .info(new Info().title("SpringShop API")  
                        .description("Spring shop sample application")  
                        .version("v0.0.1")  
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))  
                .externalDocs(new ExternalDocumentation()  
                        .description("SpringShop Wiki Documentation")  
                        .url("https://springshop.wiki.github.org/docs"));  
  }  
}
```
3. SecurityConfig 설정 추가
```java
.authorizeRequests(  
        authorizeRequests -> authorizeRequests  
                // spring doc 관리자 회원만 허용  
  .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")  
                .hasAuthority("ADMIN")  
                .anyRequest()  
                .permitAll()  
)
```
- 관리자 회원만 접근하도록 설정한다.
---
<strong>궁금했던 점</strong>
- AuthLevel 같은 enum 클래스는 어느 패키지에 위치해야하는가? (현재는 entity 패키지 내부에 entity 클래스와 같은 layer 에 위치함)

<strong>Refactoring</strong>
-   토스 카드 결제 환불 처리 구현
-   관리자 회원으로 로그인하면 관리자 회원 메인페이지로 리다이렉트하기
-   장바구니 단건 삭제에서는 productId 를 넘기고 선택 삭제에서는 cartItemId 를 넘기는 부분을 모두 cartItemId를 넘기도록 통일
-   금액 콤마 표시(프론트)
