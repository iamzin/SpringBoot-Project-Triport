package com.project.triport.scheduler;

import com.project.triport.repository.MemberPromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberPromotionScheduler {

    private final MemberPromotionRepository memberPromotionRepository;

    // ๋งค์ผ ์์ 
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteSentHistory() {
        memberPromotionRepository.deleteByTrilsFiveLikePromo(true);
    }
}
