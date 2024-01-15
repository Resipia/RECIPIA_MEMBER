-- member 테이블에 테스트 데이터 삽입
INSERT INTO member (password,
                    full_name,
                    email,
                    nickname,
                    status,
                    introduction,
                    tel_no,
                    addr1,
                    addr2,
                    create_dttm,
                    update_dttm,
                    role_type)
VALUES ('password1', '홍길동', 'hong1@example.com', 'hong1', 'ACTIVE', '안녕하세요', '01012345678', '서울시 강남구', '123번지',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER'),
       ('password2', '김철수', 'kim2@example.com', 'kim2', 'DORMANT', '반갑습니다', '01023456789', '서울시 서초구', '456번지',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER'),
       ('password3', '이영희', 'lee3@example.com', 'lee3', 'ACTIVE', '잘 부탁드립니다', '01034567890', '부산시 해운대구', '789번지',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN'),
       ('password4', '박지민', 'user@example.com', 'park4', 'ACTIVE', '반가워요', '01045678901', '대구시 중구', '1011번지',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER'),
       ('password5', '정수민', 'jung5@example.com', 'jung5', 'ACTIVE', '안녕하세요!', '01056789012', '인천시 남동구', '1213번지',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER');

-- member_event_record 테이블에 가짜 데이터 삽입

INSERT INTO member_event_record (member_id, sns_topic, event_type, attribute, trace_id, create_dttm, published,
                                 published_at)
VALUES (1, 'topic1', 'eventType1', 'attribute1', 'traceId1', CURRENT_TIMESTAMP, false, NULL),
       (1, 'topic1', 'eventType1', 'attribute2', 'traceId2', CURRENT_TIMESTAMP, false, NULL),
       (1, 'topic1', 'eventType1', 'attribute3', 'traceId3', CURRENT_TIMESTAMP, false, NULL),
       (2, 'topic4', 'eventType4', 'attribute4', 'traceId4', CURRENT_TIMESTAMP, false, NULL),
       (3, 'topic5', 'eventType5', 'attribute5', 'traceId5', CURRENT_TIMESTAMP, false, NULL);


--  jwt 테이블에 가짜 데이터 삽입
INSERT INTO jwt(jwt_id, member_id, refresh_token, expired_dttm)
VALUES (1, 1, 'some-refresh-token', DATEADD(MONTH, 6, CURRENT_DATE()));

-- 멤버 파일 추가
INSERT INTO member_file (member_id, file_order, flpth, object_url, origin_file_nm, strd_file_nm, file_extsn, file_size, del_yn, create_dttm, update_dttm)
VALUES (1, 1, 'path', 'url', 'origin', 'stord', 'jpg', 30, 'N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);