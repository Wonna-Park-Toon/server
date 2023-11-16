![image](https://github.com/Wonna-Park-Toon/server/assets/70051888/d66afaf9-efe7-43e1-9a18-43268f6ccdee)

**데브코스 백엔드 4기 승훈팀: 네이버웹툰 클론코딩 프로젝트**

진행 기간: 2023. 08. 29 ~ 2023. 09. 22

## 팀원 소개

| <img src="https://avatars.githubusercontent.com/u/102007066?v=4"> | <img src="https://avatars.githubusercontent.com/u/70051888?v=4"/> | <img src="https://avatars.githubusercontent.com/u/98159941?v=4"/> | <img src="https://avatars.githubusercontent.com/u/60502370?v=4"> | <img src="https://avatars.githubusercontent.com/u/61923768?v=4"> |
|:-----------------------------------------------------------------:|:-----------------------------------------------------------------:|:-----------------------------------------------------------------:|:----------------------------------------------------------------:|:----------------------------------------------------------------:|
|                  [나영경](https://github.com/na-yk)                  |              [박상민](https://github.com/smart-sangmin)              |                 [원건희](https://github.com/weonest)                 |           [멘토<br>함승훈](https://github.com/seung-hun-h)            |            [서브멘토<br>유도진](https://github.com/dojinyou)            |

## 프로젝트 목표

- 단순 CRUD가 아닌 어떻게 좋은 비즈니스 로직을 구현할 수 있을지 고민하자!
- CS 기반의 의사결정을 하자!

## 팀 규칙

- 매일 코어타임(1시) 팀 스크럼 진행
    - 팀 스크럼 시
        - Notion TO-DO 작성
        - Github issue 작성
        - 특이사항(잘 모르겠는 부분, 공부한 부분 ...) 공유
        - 어떤 것을 했는지 공유
        - 코드 리뷰
- 1일 1PR

## 기술 스택

### 개발 환경

![Java](https://img.shields.io/badge/java17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring_boot-6DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)

![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Redis](https://img.shields.io/badge/redis-DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)
![Spring Data Redis](https://img.shields.io/badge/Spring_Data_Redis-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-1C9AD6?style=for-the-badge&logo=querydsl&logoColor=white)
![amazons3](https://img.shields.io/badge/amazon_s3-FF0000?style=for-the-badge&logo=amazons3&logoColor=white)

![JUnit](https://img.shields.io/badge/junit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white)
![H2](https://img.shields.io/badge/h2-005AF0?style=for-the-badge&logo=h2&logoColor=white)

![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white)
![Spring Rest docs](https://img.shields.io/badge/Spring_Rest_Docs-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

![Github actions](https://img.shields.io/badge/Github_actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white)
![AWS CODEDEPLOY](https://img.shields.io/badge/CODE_DEPLOY-007054?style=for-the-badge&logo=amazonaws&logoColor=white)
![AWS EC2](https://img.shields.io/badge/amazon_ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)

### 협업 툴

![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032.svg?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/Github-181717.svg?style=for-the-badge&logo=Github&logoColor=white)

## 문서

### API 명세서

[Swagger](http://54.180.66.111/swagger-ui/index.html)

## 설계

### ERD

![image](https://github.com/Wonna-Park-Toon/server/assets/70051888/1b6121ce-d228-4923-9f80-0f1d82db533e)

<br>

### CI

![image](https://github.com/Wonna-Park-Toon/server/assets/70051888/89bfe675-a02f-455a-896f-afe3129ad207)

### CD

![image](https://github.com/Wonna-Park-Toon/server/assets/70051888/3ac4057d-1e0f-452b-b04f-437ba2da88ea)

### 프로젝트 구조

```text
└── java
     └── com.wonnapark.wnpserver
         ├── auth
         │   ├── application
         │   ├── config
         │   ├── dto
         │   ├── exception
         │   ├── infrastructure
         │   └── presentation
         ├── episode
         │   ├── application
         │   ├── dto
         │   │   ├── request
         │   │   └── response
         │   ├── infrastructure
         │   └── presentation
         ├── global
         │   ├── auth
         │   ├── common
         │   ├── config
         │   ├── exception
         │   ├── response
         │   └── utils
         ├── media
         ├── oauth
         │   ├── application
         │   ├── config
         │   ├── dto
         │   │   ├── request
         │   │   └── response
         │   ├── infrastructure
         │   │   └── client
         │   └── presentation
         ├── user
         │   ├── application
         │   ├── dto
         │   └── infrastructure
         └── webtoon
             ├── application
             ├── dto
             │   ├── request
             │   └── response
             ├── exception
             ├── infrastructure
             └── presentation
```

## Git 컨벤션

##### ([AngularJS commit conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153))

```text
feat (feature)
fix (bug fix)
docs (documentation)
style (formatting, missing semi colons, …)
refactor
test (when adding missing tests)
chore (maintain)
```
