-- member 테이블에 테스트 데이터 삽입
INSERT INTO member (email, password, full_name, nickname, status, introduction, tel_no, addr1, addr2, protection_yn,
                    collection_yn, role_type, create_dttm, update_dttm)
VALUES ('test1@example.com', '$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte', 'Full Name 1', 'Nickname1', 'ACTIVE', 'Introduction 1', '010-1234-5678',
        'Address 1-1', 'Address 1-2', 'Y', 'Y', 'MEMBER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('test2@example.com', '$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte', 'Full Name 2', 'Nickname2', 'ACTIVE', 'Introduction 2', '010-2345-6789',
        'Address 2-1', 'Address 2-2', 'Y', 'Y', 'MEMBER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('test3@example.com', '$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte', 'Full Name 3', 'Nickname3', 'ACTIVE', 'Introduction 3', '010-3456-7890',
        'Address 3-1', 'Address 3-2', 'Y', 'Y', 'MEMBER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- member_event_record 테이블에 가짜 데이터 삽입

INSERT INTO member_event_record (member_id, sns_topic, event_type, attribute, trace_id, create_dttm, published,
                                 published_at)
VALUES (1, 'topic1', 'eventType1', 'attribute1', 'traceId1', CURRENT_TIMESTAMP, false, NULL),
       (1, 'topic1', 'eventType1', 'attribute2', 'traceId2', CURRENT_TIMESTAMP, false, NULL),
       (1, 'topic1', 'eventType1', 'attribute3', 'traceId3', CURRENT_TIMESTAMP, false, NULL),
       (2, 'topic4', 'eventType4', 'attribute4', 'traceId4', CURRENT_TIMESTAMP, false, NULL),
       (3, 'topic5', 'eventType5', 'attribute5', 'traceId5', CURRENT_TIMESTAMP, false, NULL);
