## 항해99 실전 프로젝트 (Triport)

<p align='center'>
  <img src="./triport/src/main/resources/gitwiki/login_logo.png" width="300px" />
</p>

### 🔗 라이브
https://triport.kr/

### 🏠 소개
Triport 서비스는 여행이라는 테마 아래 영상 <b>릴스 서비스 (=Trils)</b> & <b>블로그 서비스 (=Trilog)</b>를 제공하고 있는 여행 매니아들만의 서비스입니다.

### ⏲️ 개발기간
2021년 04월 23일 ~ 2021년 05월 28일

### 🧙 맴버구성
- 💻 Backend
    - 박은진, 채진욱, 손윤환
- 💄 Frontend(https://github.com/rayrayj92/triport)
    - 정찬엽, 김병훈, 박민경
- 😎 Design
    - 안지수
    
### 📝 공통 문서
- **ERD(Entity Relationship Diagram)** - <a href="" >상세보기 - WIKI 이동</a>  
- **API(Application Programming Interface)** - <a href="" >상세보기 - WIKI 이동</a>

### ⚙️ 개발 환경
- **Server** : AWS EC2(Ubuntu 20.04 LTS)  
- **Framework** : Springboot  
- **Database** : Mysql (AWS RDS)  
- **ETC** : AWS S3, AWS Cloudfront, AWS LoadBalancer, AWS ROUTE 53, AWS IAM  

### 📌 주요 기능
#### Security - <a href="" >상세보기 - WIKI 이동</a>
- SSL
- CORS 
- CSRF
#### 로그인/회원가입 - <a href="" >상세보기 - WIKI 이동</a>
- JWT 기반 일반 로그인
- 카카오 소셜 로그인
- 이메일기반 비밀번호 찾기
#### Trils (영상 릴스) - <a href="https://github.com/iamzin/SpringBoot-Project-Triport/wiki/%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C(Trils_%EC%98%81%EC%83%81)" >상세보기 - WIKI 이동</a>
- 게시물, 좋아요, 해쉬태그 CRUD
- 스트리밍 서비스 제공
#### Trilog (블로그) - <a href="https://github.com/iamzin/SpringBoot-Project-Triport/wiki/%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C(Trillog_%EB%B8%94%EB%A1%9C%EA%B7%B8)" >상세보기 - WIKI 이동</a>
- 게시물, 좋아요, 댓글, 대댓글
- Toast UI Editor (WSYSWYG) 에디터 활용
- Kakao Map 키워드 검색 및 위치 설정
- 블로그 메인 페이지 무한 스크롤
