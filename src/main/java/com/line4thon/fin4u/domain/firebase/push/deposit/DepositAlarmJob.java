package com.line4thon.fin4u.domain.firebase.push.deposit;

import com.line4thon.fin4u.domain.firebase.push.PushService;
import com.line4thon.fin4u.domain.firebase.push.PushType;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.Account;
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
public class DepositAlarmJob {

    private final PushService push;

    @Value("${fcm.timezone:Asia/Seoul}")
    private String tz;

    @PersistenceContext
    private EntityManager em;

    // 예금 알람: 매일 09:05
    @Scheduled(cron = "0 5 9 * * *", zone = "Asia/Seoul")
    public void runDailyForDeposit() {
        LocalDate today = LocalDate.now(ZoneId.of(tz));

        sendDepositDueToday(today);       // 오늘 예금 납부일
        sendDepositMaturedToday(today);   // 오늘 예금 만기일
        sendDepositMaturingSoon(today);   // 7일 이내 예금 만기
    }

    /**
     * 오늘 예금 납부일
     * - savingType = DEPOSIT
     * - paymentDate = 오늘 일(dayOfMonth)
     * - startDate <= today <= endDate
     */
    private void sendDepositDueToday(LocalDate today) {
        int payDay = today.getDayOfMonth();

        var result = em.createQuery("""
                select s.id, w.member.memberId
                from SavingAccount s
                join s.wallet w
                where s.savingType = :depositType
                  and s.paymentDate = :payDay
                  and s.startDate <= :today
                  and s.endDate   >= :today
                """, Object[].class)
                .setParameter("depositType", Account.DEPOSIT)
                .setParameter("payDay", payDay)
                .setParameter("today", today)
                .getResultList();

        for (Object[] row : result) {
            Long savingId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.DEPOSIT_DUE_TODAY,
                    Map.of(
                            "depositId", savingId.toString(),
                            "body", "Today is your deposit payment due date.",
                            "lang", "EN"
                    )
            );
        }
    }

    /**
     * 오늘 만기된 예금 → "예금 만기" 푸시
     */
    private void sendDepositMaturedToday(LocalDate today) {
        var result = em.createQuery("""
                select s.id, w.member.memberId
                from SavingAccount s
                join s.wallet w
                where s.savingType = :depositType
                  and s.endDate = :today
                """, Object[].class)
                .setParameter("depositType", Account.DEPOSIT)
                .setParameter("today", today)
                .getResultList();

        for (Object[] row : result) {
            Long savingId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.DEPOSIT_MATURED,
                    Map.of(
                            "depositId", savingId.toString(),
                            "body", "Your deposit have matured",
                            "lang", "EN"
                    )
            );
        }
    }

    /**
     * 앞으로 7일 이내 만기될 예금 → "예금 만기 임박" 푸시
     */
    private void sendDepositMaturingSoon(LocalDate today) {
        LocalDate soon = today.plusDays(7);

        var result = em.createQuery("""
                select s.id, w.member.memberId
                from SavingAccount s
                join s.wallet w
                where s.savingType = :depositType
                  and s.endDate > :today
                  and s.endDate <= :soon
                """, Object[].class)
                .setParameter("depositType", Account.DEPOSIT)
                .setParameter("today", today)
                .setParameter("soon", soon)
                .getResultList();

        for (Object[] row : result) {
            Long savingId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.DEPOSIT_MATURING_SOON,
                    Map.of(
                            "depositId", savingId.toString(),
                            "body", "Your deposit matures soon Explore new options!",
                            "lang", "EN"
                    )
            );
        }
    }
}
