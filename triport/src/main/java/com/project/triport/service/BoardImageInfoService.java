package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.responseDto.results.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardImageInfoService {

    private final BoardImageInfoRepository boardImageInfoRepository;
    private final S3ImageService s3ImageService;

    // Repository에서 tempId와 ImageUrl로 찾은 칼럼들의 shouldBeDeleted 값을 false로 바꾼다.
    // Repository에서 tempId로 찾은 칼럼들 가운데 shouldBeDeleted 값이 true인 칼럼을 삭제하고, S3에서도 삭제한다.
    @Transactional
    public void CompareAndDeleteImage(List<ImageResponseDto> imageUrlList, String tempId) throws IOException {
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByTempId(tempId);

        // Repository에서 tempId와 ImageUrl로 찾은 칼럼들의 shouldBeDeleted 값을 false로 바꾼다. O(n^2)
        for (BoardImageInfo boardImageInfo : boardImageInfoList) { //[1,2,3,4,5]
            for (ImageResponseDto imageResponseDto : imageUrlList) { //[3,4]
                String filename = imageResponseDto.getImageFilePath().split("https://" + S3ImageService.CLOUD_FRONT_DOMAIN_NAME + "/")[1];
                if (boardImageInfo.getFilePath().equals(filename)) {
                    boardImageInfo.updateShouldBeDelete(false); // shouldBeDeleted 값을 false로 바꿈
                    break;
                }
            }
        }

        // Repository에서 tempId로 찾은 칼럼들 가운데 shouldBeDeleted 값이 true인 칼럼을 삭제하고, S3에서도 삭제한다.
        List<BoardImageInfo> shouldBeDeletedBoardImageInfoList = boardImageInfoRepository.findByTempIdEqualsAndShouldBeDeletedEquals(tempId, true);
        for (BoardImageInfo boardImageInfo : shouldBeDeletedBoardImageInfoList) {
            s3ImageService.deleteImg(boardImageInfo.getFilePath()); // S3에서 삭제
            boardImageInfoRepository.delete(boardImageInfo); // 테이블에서 삭제
        }
//        boardImageInfoRepository.deleteByTempIdEqualsAndShouldBeDeletedEquals(tempId, true); // 테이블에서 칼럼 삭제
    }

    public void deleteImageFromS3(Board board) throws IOException {
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByBoard(board);
        for (BoardImageInfo boardImageInfo : boardImageInfoList) {
            s3ImageService.deleteImg(boardImageInfo.getFilePath());
        }
    }
}
