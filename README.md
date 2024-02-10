# RECIPIA_MEMBER
- MSA 프로젝트 레시피아 서비스의 멤버 서버 프로젝트
- 배포 URL (원스토어): https://m.onestore.co.kr/mobilepoc/apps/appsDetail.omp?prodId=0000774118
- 소개 페이지 (노션): https://upbeat-willow-06b.notion.site/7ece9e5f602a43f583d7f4cf101e7d69?pvs=4


![레시피아_인트로](https://github.com/Resipia/RECIPIA_MEMBER/assets/79524077/23b05f73-9ae3-4751-be58-44c4aa38b2bb)


## 목차
- [프로젝트 소개](#-프로젝트-소개)
- [개발 기간](#-개발-기간)
- [개발 환경](#-개발-환경)
- [인프라](#-인프라)
- [아키텍처, 기술 스택](#-아키텍처-기술-스택)
- [개발 전략](#-개발-전략)
  - [테스트 코드](#1-테스트-코드)
  - [에러 중앙 처리](#2-에러-중앙-처리)
- [주요 기능](#-주요-기능)
  - [김이준, 최진안 (페어)](#김이준-최진안-페어)
  - [김이준](#김이준)
- [기능 설명](#-기능-설명)


## 🔸 프로젝트 소개
- 레시피아는 레시피를 이미지와 함께 작성하고 공유할 수 있는 SNS입니다.
- 검색을 통해 찾고싶은 레시피 이름을 검색하여 구경할 수 있습니다.
- 다양한 회원을 팔로우하며 마음에 드는 레시피에 댓글을 작성할 수 있고, 좋아요, 북마크를 할 수 있습니다.
- 멤버 서버는 그중 회원 관련 프로세스를 담당하고 있습니다.

## 🔸 개발 기간
- 2023.05 ~ 현재 진행 중

## 🔸 개발 환경
- Java 17
- Spring Boot 3.1.2
- IDE: Intellij, Datagrip
- Database: RDS(PostgreSQL)
- ORM: JPA

## 🔸 인프라
- 프로젝트는 MSA로 구성되어있으며 ECS 클러스터에 MEMBER 서버를 구축하였습니다.
- 외부 서비스는 RDS(PostgreSQL), S3, SNS, SQS, Redis를 사용합니다.

![스크린샷 2024-02-10 오후 3 49 57](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/d9f049f7-dc00-480a-8bec-c7d8284e48ef)


- ECS 인프라는 다음과 같이 ECR에 저장된 스프링부트 이미지를 받아서 컨테이너로 동작시킵니다.
- SpringBoot에는 Zipkin서버로 로그를 전송하도록 설계하였습니다.

![image (1)](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/a4f16700-3bde-4f42-bab1-ea0bd86f6048)


- CI/CD는 AWS의 CodePipeline으로 구축하였습니다.
- GitHub와 CodeBuild를 연결했고 CodeDeploy에서는 ECR에 접근해서 빌드된 이미지를 사용해서 배포하도록 했습니다.
- 이렇게 설계하여 만약 main 브랜치에 merge가 발생하면 Github 훅이 동작하여 CodePipeline이 동작합니다.

![image](https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/cb4b7c3b-0807-44df-9be3-0652725bc8fd)



## 🔸 아키텍처, 기술 스택
- 이벤트 드리븐, 메세지 드리븐 아키텍처 (AWS SNS,SQS)
<img width="1009" alt="spring-event" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/c51540ec-ab46-4be5-b493-3ef0823af92c">

- Feign Client (zero-payload)
<img width="364" alt="zero-payload" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/cbff50e7-9136-4b70-9b1e-676ebb5b8c4f">

- 미발행된 이벤트 처리 (transactional outbox pattern)
<img width="1034" alt="batch-event" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/b17da5e9-4953-4764-b8af-36d7c8e9945d">

- 로그인 (JWT)
<img width="883" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/e75cf16f-0b9f-4407-81d4-9ab71e4a9374">

- Access Token 재발행
<img width="883" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/078e73d5-71a3-4ef4-9a43-e4e34c2bfb04">

## 🔸 개발 전략
#### 1. 테스트 코드
171개의 테스트 케이스 성공

<img width="1297" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/5565f559-f9b8-44b7-9a74-113f64655261">

#### 2. 에러 중앙 처리
이 프로젝트의 에러 처리 전략은 @RestControllerAdvice를 활용하여 모든 예외를 중앙에서 처리합니다. 


이를 통해, 커스텀으로 정의된 ErrorCode 클래스를 사용하여 에러 코드 및 메시지 관리를 효율적으로 수행합니다.

<img width="641" alt="image" src="https://github.com/Resipia/RECIPIA_MEMBER/assets/74906042/08987059-a918-459b-81bb-c4713232df3a">



## 🔸 주요 기능
#### 김이준, 최진안 (페어)
1. 로그인 (Spring Security, JWT)
2. 회원가입 (휴대폰 번호/이메일/닉네임 중복체크, S3 이미지 저장, SNS)

#### 김이준
1. 로그아웃
2. 이메일 찾기
3. 비밀번호 재설정 (Java Mail Sender로 메일 전송)
4. Access Token 재발행
5. 회원 탈퇴 (SNS)
6. 마이페이지 조회/수정 (SNS)
7. 팔로우 요청/팔로우 취소
8. 팔로우/팔로잉 목록 조회
9. 회원 신고
10. 비밀번호 수정
11. 문의사항 등록/조회


## 🔸 기능 설명


