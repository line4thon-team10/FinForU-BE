package com.line4thon.fin4u.domain.product;

import com.line4thon.fin4u.domain.product.entity.Bank;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.repository.BankRepository;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.service.Comparison.ComparisonService;
import com.line4thon.fin4u.domain.product.web.dto.CompareRes;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
class CompareServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ComparisonService comparisonService;

    @Autowired
    private InstallmentSavingRepository savingRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private DepositRepository depositRepo;

    @Autowired
    private BankRepository bankRepo;



    @Test
    void 카드_비교_통합_테스트() {
        //given: 실제 DB에 은행 + 카드 2개 저장
        Bank bank = bankRepo.save(
                Bank.builder()
                        .bankName("국민은행")
                        .build()
        );

        Card c1 = cardRepo.save(
                Card.builder()
                        .name("카드A")
                        .description("설명A")
                        .cardType(CardType.CHECK)
                        .domesticAnnualFee(15000)
                        .internationalAnnualFee(20000)
                        .bank(bank)
                        .build()
        );

        Card c2 = cardRepo.save(
                Card.builder()
                        .name("카드B")
                        .description("설명B")
                        .cardType(CardType.CREDIT)
                        .domesticAnnualFee(30000)
                        .internationalAnnualFee(10000)
                        .bank(bank)
                        .build()
        );

        List<Long> ids = List.of(c1.getId(), c2.getId());

        // when
        CompareRes result = comparisonService.compare(ids, Type.CARD);

        // then
        assertThat(result.cards()).hasSize(2);
        assertThat(result.highlights().lowestDomesticId()).isEqualTo(c1.getId());
        assertThat(result.highlights().lowestInternationalId()).isEqualTo(c2.getId());
    }

    @Test
    void 적금_비교_통합_테스트(){
        //given: 실제 DB에 은행 + 적금 2개 저장
        Bank bank = bankRepo.save(
                Bank.builder()
                        .bankName("국민은행")
                        .build()
        );


        InstallmentSaving s1 = savingRepo.save(
                InstallmentSaving.builder()
                        .name("적금A")
                        .description("설명A")
                        .baseInterestRate(2.0)
                        .maxInterestRate(3.5)
                        .savingTerm(12)
                        .isFlexible(false)
                        .maxMonthly(300000)
                        .officialWebsite("http://a.com")
                        .bank(bank)
                        .build()
        );

        InstallmentSaving s2 = savingRepo.save(
                InstallmentSaving.builder()
                        .name("적금B")
                        .description("설명B")
                        .baseInterestRate(2.5)
                        .maxInterestRate(4.0)
                        .savingTerm(12)
                        .isFlexible(false)
                        .maxMonthly(500000)
                        .officialWebsite("http://b.com")
                        .bank(bank)
                        .build()
        );

        List<Long> ids = List.of(s1.getId(), s2.getId());

        //when
        CompareRes result = comparisonService.compare(ids, Type.SAVING);

        //then
        assertThat(result.savings()).hasSize(2);
        assertThat(result.highlights().bestBaseRateId()).isEqualTo(s2.getId());
        assertThat(result.highlights().bestMaxRateId()).isEqualTo(s2.getId());
        assertThat(result.highlights().lowestMaxMonthlyId()).isEqualTo(s1.getId());
    }

    @Test
    void 예금_비교_통합_테스트() {
        // given: 실제 DB에 은행 + 예금 2개 저장
        Bank bank = bankRepo.save(
                Bank.builder()
                        .bankName("국민은행")
                        .build()
        );


        Deposit d1 = depositRepo.save(
                Deposit.builder()
                        .name("예금A")
                        .description("설명A")
                        .baseInterestRate(2.0)      // 기본금리 낮음
                        .maxInterestRate(3.2)       // 최대금리 낮음
                        .depositTerm(12)
                        .isFlexible(false)
                        .minDepositAmount(1000000)  // 최소 예치금 높음 → 불리
                        .bank(bank)
                        .build()
        );

        Deposit d2 = depositRepo.save(
                Deposit.builder()
                        .name("예금B")
                        .description("설명B")
                        .baseInterestRate(3.5)      // 기본금리 높음 → Best
                        .maxInterestRate(4.1)       // 최대금리 높음 → Best
                        .depositTerm(12)
                        .isFlexible(true)
                        .minDepositAmount(500000)   // 최소 예치금 낮음 → Best
                        .bank(bank)
                        .build()
        );
        List<Long> ids = List.of(d1.getId(), d2.getId());

        //when
        CompareRes result = comparisonService.compare(ids, Type.DEPOSIT);

        //then
        assertThat(result.deposits()).hasSize(2);

        CompareRes.Highlights h = result.highlights();
        assertThat(h.bestBaseRateId()).isEqualTo(d2.getId());
        assertThat(h.bestMaxRateId()).isEqualTo(d2.getId());
        assertThat(h.lowestMinDepositId()).isEqualTo(d2.getId());
    }

}
