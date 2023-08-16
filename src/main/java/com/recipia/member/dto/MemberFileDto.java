package com.recipia.member.dto;

import com.recipia.member.domain.MemberFile;

import java.time.LocalDateTime;

public record MemberFileDto(
        Long id,
        String filePath,
        String originFileName,
        String storedFileName,
        String fileExtension,
        Integer fileSize,
        String delYn,
        LocalDateTime createDateTime,
        LocalDateTime updateDateTime
) {

    public static MemberFileDto of(Long id, String filePath, String originFileName, String storedFileName, String fileExtension, Integer fileSize, String delYn, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        return new MemberFileDto(id, filePath, originFileName, storedFileName, fileExtension, fileSize, delYn, createDateTime, updateDateTime);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static MemberFileDto fromEntity(MemberFile entity) {
        return of(
                entity.getId(),
                entity.getFilePath(),
                entity.getOriginFileName(),
                entity.getStoredFileName(),
                entity.getFileExtension(),
                entity.getFileSize(),
                entity.getDelYn(),
                entity.getCreateDateTime(),
                entity.getUpdateDateTime()
        );
    }
}