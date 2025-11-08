package com.line4thon.fin4u.domain.product;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.member.repository.MemberRepository;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Comparison;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.ComparisonRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.web.dto.ComparisonSaveReq;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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

    Member member;
    Card cardA;
    Deposit depositA;
    InstallmentSaving savingA;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
                .email("tester@gmail.com")
                .password("pw")
                .name("테스터")
                .build());

        cardA = cardRepository.save(Card.builder()
                .name("무지출카드")
                .domesticAnnualFee(0)
                .build());
        depositA = depositRepository.save(Deposit.builder()
                .name("일반예금")
                .maxInterestRate(3.2)
                .depositTerm(12)
                .build());
        savingA = savingRepository.save(InstallmentSaving.builder()
                .name("청년적금")
                .maxInterestRate(4.1)
                .savingTerm(24)
                .build());
    }

    @Test
    @DisplayName("바구니 저장 API 정상 호출")
    void saveProduct_success() throws Exception {
        ComparisonSaveReq req = new ComparisonSaveReq(Type.CARD, cardA.getId(), null);

        mockMvc.perform(post("/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
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

        mockMvc.perform(get("/comparison"))
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

        mockMvc.perform(get("/comparison")
                        .param("type", "CARD")) // QueryParam 로 전달
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cards.length()").value(1))
                .andExpect(jsonPath("$.data.deposits.length()").value(0))
                .andExpect(jsonPath("$.data.savings.length()").value(0));
    }
}
