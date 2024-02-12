# RECIPIA_MEMBER
- MSA 프로젝트 레시피아 서비스의 멤버 서버 프로젝트
- 배포 URL (원스토어): https://m.onestore.co.kr/mobilepoc/apps/appsDetail.omp?prodId=0000774118
- 소개 페이지 (노션): https://upbeat-willow-06b.notion.site/7ece9e5f602a43f583d7f4cf101e7d69?pvs=4


![레시피아_인트로](https://github.com/Resipia/RECIPIA_MEMBER/assets/79524077/23b05f73-9ae3-4751-be58-44c4aa38b2bb)

## 목차
1. [프로젝트 소개](#-프로젝트-소개)
2. [개발 기간](#-개발-기간)
3. [개발 환경](#-개발-환경)
4. [주요 기능](#-주요-기능)
5. [인프라](#-인프라)
    - [MSA 인프라 구성](#msa-인프라-구성)
    - [ECS 구성](#ecs-구성)
    - [CI/CD 설계](#cicd-설계)
6. [ERD](#-erd)
7. [아키텍처](#-아키텍처)
    - [헥사고날 아키텍처 도입](#헥사고날-아키텍처-도입)
    - [MSA 환경에서 DB 정합성 보장 과정](#msa-환경에서-db-정합성-보장-과정)
    - [ZERO-PAYLOAD 정책](#zero-payload-정책)
    - [미발행된 SNS 메시지 재발행](#미발행된-sns-메시지-재발행)
8. [개발 전략](#-개발-전략)
9. [주요 기능 상세 설명](#-주요-기능-상세-설명)
10. [테스트 코드](#-테스트-코드)


## 🔸 프로젝트 소개
- 레시피아는 레시피를 이미지와 함께 작성하고 공유할 수 있는 SNS입니다.
- 검색을 통해 찾고싶은 레시피 이름을 검색하여 구경할 수 있습니다.
- 다양한 회원을 팔로우하며 마음에 드는 레시피에 댓글을 작성할 수 있고, 좋아요, 북마크를 할 수 있습니다.
- 멤버 서버는 그중 회원 관련 프로세스를 담당하고 있습니다.

## 🔸 개발 기간
- 2023.05 ~ 2023.02 (베타버전 출시)
- 계속해서 추가개발을 진행중입니다.

## 🔸 개발 환경
- Java 17
- Spring Boot 3.1.2, Spring Security6
- IDE: Intellij, Datagrip
- Database: RDS(PostgreSQL), Redis
- ORM: JPA
- 협업 툴: Jira, Confluence, Notion, Slack


## 🔸 주요 기능
#### 김이준, 최진안 (페어)
1. 로그인 (Spring Security, JWT)
2. Access Token 재발행
3. 회원가입 (휴대폰 번호/이메일/닉네임 중복체크, S3 이미지 저장, SNS)

#### 김이준
1. 로그아웃
2. 이메일 찾기
3. 비밀번호 재설정 (Java Mail Sender로 메일 전송)
4. 회원 탈퇴(SNS)
5. 마이페이지 조회/수정(SNS)
6. 팔로우 요청(SNS)/팔로우 취소
7. 팔로우/팔로잉 목록 조회
8. 회원 신고
9. 비밀번호 수정
10. 문의사항 등록/조회



## 🔸 인프라
### MSA 인프라 구성
- 프로젝트는 MSA로 구성되어있으며 총 세개(멤버, 레시피, 지프킨)의 서비스로 이루어져있습니다.
- 그 중 멤버서버 ECS 클러스터에 MEMBER 서버를 구축하였습니다.
- 외부 데이터베이스는 RDS(PostgreSQL), Redis, S3를 사용합니다.
- 이벤트 드리븐, 메시지 드리븐으로는 SNS, SQS를 사용합니다.

![스크린샷 2024-02-10 오후 3 49 57](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/d9f049f7-dc00-480a-8bec-c7d8284e48ef)

### ECS 구성
- ECS 인프라는 다음과 같이 ECR에 저장된 스프링부트 이미지를 받아서 컨테이너로 동작시킵니다.
- 하나의 ECS에는 하나의 서비스, 하나의 태스크 정의로 실행됩니다.
- SpringBoot에는 Zipkin서버로 로그를 전송하도록 설계하였습니다.

![image (1)](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/a4f16700-3bde-4f42-bab1-ea0bd86f6048)

### CI/CD 설계
- CI/CD는 AWS의 CodePipeline으로 구축하였습니다.
- main 브랜치에 merge 발생 시 AWS CodePipeline이 자동으로 활성화됩니다. 이 과정은 GitHub 웹훅을 통해 이루어지며, GitHub의 변경 사항을 감지하여 트리거합니다.
- CodeBuild를 사용해 Docker 이미지를 생성한 후 ECR에 저장합니다. 이 단계에서는 소스 코드를 Docker 이미지로 빌드하고, 생성된 이미지를 ECR에 안전하게 업로드합니다.
- CodePipeline이 ECR에 저장된 Docker 이미지를 감지하고, ECS에 롤링 업데이트 방식을 사용하여 무중단 배포를 진행합니다. 롤링 업데이트를 통해 새 버전의 애플리케이션을 점진적으로 배포하면서 서비스 중단 없이 업데이트를 완료할 수 있습니다.

![image](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/cb4b7c3b-0807-44df-9be3-0652725bc8fd)

## 🔸 ERD
#### AQuery를 사용하여 멤버 서버의 ERD를 설계하였습니다.

![image](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/0bd1a36c-862f-4400-9a82-3f9f41f7c21f)



## 🔸 아키텍처
### 헥사고날 아키텍처 도입

![image (5)](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/ed15fcb5-cb71-4023-850a-0cd4c372c4af)


### MSA 환경에서 DB 정합성 보장 과정
- 유저가 닉네임을 변경하면 멤버 서버에서는 닉네임 변경사항을 멤버 DB에 반영하고 Spring Event를 발행합니다. (이벤트 리스너는 2개를 선언)
- 스프링 이벤트 리스너중 1개가 동작하여 멤버 DB의 Outbox 테이블에 이벤트 발행 여부를 기록하고 DB 커밋을 합니다.(트랜잭션 커밋완료)
- 트랜잭션 커밋이 완료되면 AFTER_COMMIT을 적어준 또다른 스프링 이벤트 리스너가 동작하여 닉네임 변경 토픽으로 SNS 메시지를 발행합니다.
- SNS 메시지가 발행되면 2개의 SQS리스너가 동시에 동작하게 됩니다.
  - 레시피 서버에서 닉네임 변경 토픽을 리스닝하고있던 SQS가 실행됩니다.
  - 멤버 서버에서 닉네임 변경 토픽을 리스닝하고 있던 SQS가 실행됩니다. 이때 Outbox 테이블에 이벤트 발행 여부(published 컬럼)를 true로 업데이트합니다.
- 레시피 서버의 SQS 리스너가 동작할때 FeignClient를 사용하여 멤버 서버에 가장 최신의 유저 닉네임 정보를 요청합니다.
- 멤버 서버로부터 받아온 가장 최신의 유저 닉네임 정보를 레시피DB에 반영합니다.

<img width="1024" alt="spring-event" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/1d620490-b118-472c-b63f-3e8417a299a9">

### ZERO-PAYLOAD 정책
- 데이터 전송시 메시지 내부에는 memberId만을 포함하도록 합니다.
- 분산추적을 위한 traceId는 SNS의 messageAttributes를 사용하여 전송합니다.

<img width="1024" alt="zero-payload" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/0255f90c-a803-430e-bb06-b96d777dacb7">


### 미발행된 SNS 메시지 재발행
- Spring Batch를 사용하여 5분마다 미발행된 메시지를 재발행 합니다.
- Outbox 테이블에 저장된 발행여부(published)가 false것을 조회하여 배치가 동작합니다.

<img width="1024" alt="batch-event" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/7f452133-23c7-4f99-96b0-a3ceda4faf06">


## 🔸 개발 전략
#### 1. 스프링 시큐리티와 JWT를 통한 인증 및 인가

<img width="1024" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/21a7d578-cf14-4334-98d5-a163115b2a35">


#### 2. 에러 중앙 처리
- 이 프로젝트의 에러 처리 전략은 @RestControllerAdvice를 활용하여 모든 예외를 중앙에서 처리합니다. 
- 이를 통해, 커스텀으로 정의된 ErrorCode 클래스를 사용하여 에러 코드 및 메시지 관리를 효율적으로 수행합니다.

![image (6)](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/0396b5cf-9f07-4efc-80e0-4934cd0b912a)



## 🔸 주요 기능 상세 설명

#### 1️. 로그인

<img width="1024" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/a9539525-2a94-4a19-af84-96e9b929ca1e">

#### 2. Access Token, Refresh Token 발행

<img width="1024" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/e75cf16f-0b9f-4407-81d4-9ab71e4a9374">

#### 3. Access Token 재발행

<img width="1024" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/078e73d5-71a3-4ef4-9a43-e4e34c2bfb04">

#### 4. 회원가입

![image](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/0318b2ee-c2cd-4624-bbf1-60a369426644)


#### 5. 임시 비밀번호 발급

![image](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/72295aa9-a6d6-483b-8253-56bc17cf0022)



## 🔸 테스트 코드
- 단위/통합 테스트 진행 (171개)
- Junit5, mockito, S3Mock, BddMockito 사용
- 외부 DB (Redis)를 사용한 테스트도 진행했습니다.

<img width="1024" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/5565f559-f9b8-44b7-9a74-113f64655261">
