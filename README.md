# 💘 파이낙사 온리전 :: 사랑의 진도

**사랑의 진도**는 아마추어 창작 교류 행사 운영을 위한 웹 애플리케이션입니다. 행사 소개, 공지 확인, 부스 등록 및 관리, 문의/답변 기능 등, 행사 주최자와 참가자 모두를 위한 기능을 제공합니다.

🔗 [배포 주소](https://phainaxa.com/)

---

## 📌 프로젝트 개요
- **개발 형태**: Spring Boot 기반 웹 애플리케이션 (MVC 구조)
- **개발 인원**: 1인 (외주 단독 개발)
- **핵심 목표**  
  - 행사 소개 / 공지 / 부스 / 문의 기능 통합
  - 주최자(관리자)와 참가자(사용자) 모두 편리하게 사용할 수 있는 플랫폼 구축
  - 모바일 환경 최적화 및 안정적 운영 지원
  
---

## 🛠️ 기술 스택
| 분야 | 사용 기술 |
|------|-----------|
| Front-end | HTML, CSS, JavaScript, Thymeleaf |
| Back-end | Java (Spring Boot) |
| Database | MySQL |
| ORM | MyBatis |
| 라이브러리 | Lombok, Spring Security, Quill.js |
| Tools | IntelliJ IDEA, VS Code, FileZilla |
| 배포 | 가비아 컨테이너 호스팅 |

---

## 🧩 주요 기능
- 🏠 **메인 페이지**: 슬라이드 배너, 주요 공지 바로가기  
- 📖 **어바웃 페이지**: Quill 에디터 기반 행사 소개 작성/수정  
- 🗂️ **게시판 시스템**: 인포/이벤트/부스 게시판, 권한별 접근 제어, 댓글·공지 고정 기능  
- 🎪 **부스 정보 페이지**: 키워드 기반 필터링 기능 제공  
- ❓ **Q&A 페이지**: 회원/비회원 작성, 비밀글 지원, 로그인 사용자는 자신의 질문 확인 가능  
- 🧑‍💼 **관리자 페이지**: 회원/공지/배너/게시판/부스/Q&A 관리, 회원 등급 조정 및 비밀번호 초기화  
- 🔐 **보안**: Spring Security 기반 로그인 및 세션 관리  
- 📱 **모바일 최적화**: 관리자 제외 모든 페이지 모바일 전용 레이아웃 적용  

---

## ⚙️ 기술적 구현
- **Spring MVC 아키텍처 적용**  
  - DB ↔ MyBatis Mapper(XML) ↔ Mapper Interface ↔ Service ↔ Controller ↔ View/REST API ↔ 클라이언트  
  - 게시판, Q&A 등은 REST API와 Thymeleaf 기반 View를 병행  

- **보안**  
  - Spring Security 기반 로그인/세션 관리  
  - BCrypt 비밀번호 암호화 저장  
  - 게시판/부스/Q&A 권한 분리 및 접근 제어 로직 구현  

- **데이터 관리**  
  - MyBatis 매퍼 XML 기반 SQL 관리  
  - DTO/Mapper Interface를 통한 데이터 매핑  
  - 키워드 기반 검색 기능 구현 (부스 필터링)  

- **에디터 및 입력 처리**  
  - Quill 에디터를 사용한 행사 소개 및 공지 작성  
  - 입력값 유효성 검사 및 XSS 방어 로직 적용  

- **배포 환경**  
  - 가비아 컨테이너 호스팅에 배포  
  - FileZilla를 통한 정기 업데이트 관리  

---

## 📁 디렉터리 구조 (요약)
<pre>
  plaintext loveprogress/ 
  ├── src/ 
  │ └── main/ 
  │ ├── java/ # 백엔드 로직 
  │ └── resources/ 
  │ ├── templates/ # Thymeleaf 템플릿 
  │ └── mapper/ # MyBatis XML 
  └── build.gradle
</pre>

_보안상 공개가 어려운 파일은 Git에 포함돼 있지 않습니다._
_사이트 기획과 디자인은 클라이언트 측에서 제공했습니다._
