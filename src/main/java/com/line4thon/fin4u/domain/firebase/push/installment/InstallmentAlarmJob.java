package com.line4thon.fin4u.domain.firebase.push.installment;

import com.line4thon.fin4u.domain.firebase.push.PushService;
import com.line4thon.fin4u.domain.firebase.push.PushType;
import com.line4thon.fin4u.domain.wallet.entity.enumulate.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Configuration
@EnableScheduling
class SchedulingConfig {}

@Service
@RequiredArgsConstructor
public class InstallmentAlarmJob {

    private final PushService push;

    @Value("${fcm.timezone:Asia/Seoul}")
    private String tz;

    @PersistenceContext
    private EntityManager em;

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void runDaily() {
        LocalDate today = LocalDate.now(ZoneId.of(tz));

        sendDueToday(today);       // 오늘 납부일
        sendMaturedToday(today);   // 오늘 만기
        sendMaturingSoon(today);   // 7일 이내 만기
    }

    /**
     * 오늘은 적금 납부일입니다.
     * - savingType = INSTALLMENT
     * - paymentDate = 오늘 일(dayOfMonth)
     * - startDate <= today <= endDate
     */
    private void sendDueToday(LocalDate today) {
        int payDay = today.getDayOfMonth();

        var result = em.createQuery("""
                select s.id, w.member.memberId
                from SavingAccount s
                join s.wallet w
                where s.savingType = :installmentType
                  and s.paymentDate = :payDay
                  and s.startDate <= :today
                  and s.endDate   >= :today
                """, Object[].class)
                .setParameter("installmentType", Account.INSTALLMENT)
                .setParameter("payDay", payDay)
                .setParameter("today", today)
                .getResultList();

        for (Object[] row : result) {
            Long savingId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.INSTALLMENT_DUE_TODAY,
                    Map.of(
                            "savingId", savingId.toString(),
                            "body", "Today is your installment savings payment due date.",
                            "lang", "EN"
                    )
            );
        }
    }

    /**
     * 적금 만기 (오늘)
     */
    private void sendMaturedToday(LocalDate today) {
        var result = em.createQuery("""
                select s.id, w.member.memberId
                from SavingAccount s
                join s.wallet w
                where s.savingType = :installmentType
                  and s.endDate = :today
                """, Object[].class)
                .setParameter("installmentType", Account.INSTALLMENT)
                .setParameter("today", today)
                .getResultList();

        for (Object[] row : result) {
            Long savingId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.INSTALLMENT_MATURED,
                    Map.of(
                            "savingId", savingId.toString(),
                            "body", "Your installment savings have matured.",
                            "lang", "EN"
                    )
            );
        }
    }

    /**
     * 적금 만기 7일 이내
     */
    private void sendMaturingSoon(LocalDate today) {
        LocalDate soon = today.plusDays(7);

        var result = em.createQuery("""
                select s.id, w.member.memberId
                from SavingAccount s
                join s.wallet w
                where s.savingType = :installmentType
                  and s.endDate > :today
                  and s.endDate <= :soon
                """, Object[].class)
                .setParameter("installmentType", Account.INSTALLMENT)
                .setParameter("today", today)
                .setParameter("soon", soon)
                .getResultList();

        for (Object[] row : result) {
            Long savingId = (Long) row[0];
            Long memberId = (Long) row[1];

            push.sendToMember(
                    memberId,
                    PushType.INSTALLMENT_MATURING_SOON,
                    Map.of(
                            "savingId", savingId.toString(),
                            "body", "Your installment savings mature soon. Consider a new plan!",
                            "lang", "EN"
                    )
            );
        }
    }
}
