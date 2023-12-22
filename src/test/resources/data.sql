-- member 테이블에 테스트 데이터 삽입
INSERT INTO public.member (password,
                           full_name,
                           email,
                           nickname,
                           status,
                           introduction,
                           tel_no,
                           addr1,
                           addr2,
                           collection_consent_yn,
                           marketing_consent_yn,
                           privacy_policy_consent_yn,
                           cookie_consent_yn,
                           create_dttm,
                           update_dttm,
                           role_type)
VALUES ('password1', '홍길동', 'hong1@example.com', 'hong1', 'ACTIVE', '안녕하세요', '010-1234-5678', '서울시 강남구', '123번지', 'Y', 'Y',
        'Y', 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER'),
       ('password2', '김철수', 'kim2@example.com', 'kim2', 'DORMANT', '반갑습니다', '010-2345-6789', '서울시 서초구', '456번지', 'Y', 'N',
        'Y', 'N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER'),
       ('password3', '이영희', 'lee3@example.com', 'lee3', 'ACTIVE', '잘 부탁드립니다', '010-3456-7890', '부산시 해운대구', '789번지', 'N',
        'Y', 'N', 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN'),
       ('password4', '박지민', 'park4@example.com', 'park4', 'DEACTIVATED', '반가워요', '010-4567-8901', '대구시 중구', '1011번지', 'Y', 'Y',
        'Y', 'N', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER'),
       ('password5', '정수민', 'jung5@example.com', 'jung5', 'ACTIVE', '안녕하세요!', '010-5678-9012', '인천시 남동구', '1213번지', 'N',
        'N', 'Y', 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER');

-- member_event_record 테이블에 가짜 데이터 삽입

INSERT INTO member_event_record (member_id, sns_topic, event_type, attribute, trace_id, create_dttm, published,
                                 published_at)
VALUES (1, 'topic1', 'eventType1', 'attribute1', 'traceId1', CURRENT_TIMESTAMP, false, NULL),
       (1, 'topic1', 'eventType1', 'attribute2', 'traceId2', CURRENT_TIMESTAMP, false, NULL),
       (1, 'topic1', 'eventType1', 'attribute3', 'traceId3', CURRENT_TIMESTAMP, false, NULL),
       (2, 'topic4', 'eventType4', 'attribute4', 'traceId4', CURRENT_TIMESTAMP, false, NULL),
       (3, 'topic5', 'eventType5', 'attribute5', 'traceId5', CURRENT_TIMESTAMP, false, NULL);
