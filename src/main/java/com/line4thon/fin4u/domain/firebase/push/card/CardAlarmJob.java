package com.line4thon.fin4u.domain.firebase.push.card;

import com.line4thon.fin4u.domain.firebase.push.PushService;
import com.line4thon.fin4u.domain.firebase.push.PushType;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.CardType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CardAlarmJob {

    private final PushService push;

    @Value("${fcm.timezone:Asia/Seoul}")
    private String tz;

    @PersistenceContext
    private EntityManager em;

    // 카드는 09:10에 체크
    @Scheduled(cron = "0 10 9 * * *", zone = "Asia/Seoul")
    public void runDailyForCard() {
        LocalDate today = LocalDate.now(ZoneId.of(tz));
        sendCardDueToday(today);
    }

    /**
     * 오늘은 카드 결제일입니다.
     * - cardType = CREDIT (필요하면 CHECK도 포함 가능)
     * - paymentDate = 오늘 일(dayOfMonth)
     */
    private void sendCardDueToday(LocalDate today) {
        int payDay = today.getDayOfMonth();

        var result = em.createQuery("""
                select c.id, w.member.memberId
                from WalletCard c
                join c.wallet w
                where c.cardType = :creditType
                  and c.paymentDate = :payDay
                """, Object[].class)
                .setParameter("creditType", CardType.CREDIT)
                .setParameter("payDay", payDay)
                .getResultList();

        for (Object[] row : result) {
            Long cardId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.CARD_DUE_TODAY,
                    Map.of(
                            "cardId", cardId.toString(),   // ★  wallet 카드 id
                            "body", "Today is your card payment due date",
                            "lang", "EN"
                    )
            );
        }
    }
}
