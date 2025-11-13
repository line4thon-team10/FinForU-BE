package com.line4thon.fin4u.domain.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.product.entity.*;
import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.repository.*;
import com.line4thon.fin4u.domain.product.web.dto.CompareSaveReq;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ComparisonIntegrationTest extends IntegrationTestSupport {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired MemberRepository memberRepository;
    @Autowired CardRepository cardRepository;
    @Autowired DepositRepository depositRepository;
    @Autowired InstallmentSavingRepository savingRepository;
    @Autowired ComparisonRepository comparisonRepository;
    @Autowired BankRepository bankRepository;

    Member member;
    Card cardA;
    Deposit depositA;
    InstallmentSaving savingA;
    Bank greenTreeBank;
    Bank sunnyBank;

    private static final String LANG = "en"; // 테스트 기본 언어

    @BeforeEach
    void setUp() {
        greenTreeBank = bankRepository.save(Bank.builder()
                .bankName("GreenTree Bank")
                .build());

        sunnyBank = bankRepository.save(Bank.builder()
                .bankName("Sunny Bank")
                .build());

        member = memberRepository.save(Member.builder()
                .email("tester@gmail.com")
                .password("pw")
                .name("테스터")
                .language(Member.Language.ENGLISH)
                .nationality("KOR")
                .visaType(Member.VisaType.ACADEMIC) // 수정
                .visa_expir(Timestamp.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                .notify(true)
                .desiredProductTypes(Set.of(
                        Member.DesiredProductType.CARD  // 기본값 1개
                ))
                .build());

        cardA = cardRepository.save(Card.builder()
                .nameEn("No-Spend Card")
                .nameZh("无支出卡")
                .nameVi("Thẻ không chi tiêu")
                .descriptionEn("카드 A 설명")
                .descriptionZh("卡 A 说明")
                .descriptionVi("Mô tả thẻ A")
                .cardType(CardType.CHECK)
                .domesticAnnualFee(0)
                .internationalAnnualFee(0)
                .officialWebsite(null)
                .bank(greenTreeBank)
                .build());

        depositA = depositRepository.save(Deposit.builder()
                .nameEn("Regular Deposit")
                .nameZh("普通存款")
                .nameVi("Tiền gửi thường")
                .descriptionEn("예금 A 설명")
                .descriptionZh("存款 A 说明")
                .descriptionVi("Mô tả tiền gửi A")
                .baseInterestRate(2.0)
                .maxInterestRate(3.2)
                .depositTerm(12)
                .isFlexible(false)
                .minDepositAmount(2)
                .officialWebsite(null)
                .bank(sunnyBank)
                .build());

        savingA = savingRepository.save(InstallmentSaving.builder()
                .nameEn("Youth Saving")
                .nameZh("青年储蓄")
                .nameVi("Tiết kiệm thanh niên")
                .descriptionEn("적금 A 설명")
                .descriptionZh("储蓄 A 说明")
                .descriptionVi("Mô tả tiết kiệm A")
                .baseInterestRate(3.0)
                .maxInterestRate(4.1)
                .savingTerm(24)
                .isFlexible(false)
                .maxMonthly(300_000)
                .officialWebsite(null)
                .bank(sunnyBank)
                .build());
    }

    @Test
    @DisplayName("바구니 저장 API 정상 호출")
    void saveProduct_success() throws Exception {
        CompareSaveReq req = new CompareSaveReq(Type.CARD, cardA.getId());

        mockMvc.perform(post("/products/comparison")
                        .with(user(member.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", LANG)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        List<Comparison> stored = comparisonRepository.findByMember(member);
        assertThat(stored).hasSize(1);
        assertThat(stored.get(0).getProductId()).isEqualTo(cardA.getId());
        assertThat(stored.get(0).getType()).isEqualTo(Type.CARD);
    }

    @Test
    @DisplayName("바구니 전체 조회 → 필터 없음 → 바구니 모두 반환")
    void getComparison_noFilter_returnAll() throws Exception {
        comparisonRepository.save(Comparison.of(member, Type.CARD, cardA.getId()));
        comparisonRepository.save(Comparison.of(member, Type.DEPOSIT, depositA.getId()));
        comparisonRepository.save(Comparison.of(member, Type.SAVING, savingA.getId()));

        mockMvc.perform(get("/products/comparison")
                        .with(user(member.getEmail()))
                        .header("Accept-Language", LANG))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cards.length()").value(1))
                .andExpect(jsonPath("$.data.deposits.length()").value(1))
                .andExpect(jsonPath("$.data.savings.length()").value(1));
    }

    @Test
    @DisplayName("바구니 조회 → type=CARD → 카드만 반환")
    void getComparison_filterCardOnly() throws Exception {
        comparisonRepository.save(Comparison.of(member, Type.CARD, cardA.getId()));
        comparisonRepository.save(Comparison.of(member, Type.DEPOSIT, depositA.getId()));
        comparisonRepository.save(Comparison.of(member, Type.SAVING, savingA.getId()));

        mockMvc.perform(get("/products/comparison")
                        .with(user(member.getEmail()))
                        .param("type", "CARD")
                        .header("Accept-Language", LANG))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cards.length()").value(1));
    }
}
