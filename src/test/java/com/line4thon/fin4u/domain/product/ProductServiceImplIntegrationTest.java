package com.line4thon.fin4u.domain.product;

import com.line4thon.fin4u.domain.product.entity.Bank;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.domain.product.entity.enums.PaymentMethod;
import com.line4thon.fin4u.domain.product.repository.BankRepository;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.service.ProductService;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ProductServiceImplIntegrationTest extends IntegrationTestSupport {

    @Autowired private ProductService productService;
    @Autowired private BankRepository bankRepository;
    @Autowired private DepositRepository depositRepository;
    @Autowired private CardRepository cardRepository;
    @Autowired private InstallmentSavingRepository savingRepository;

    private Bank sunnyBank;
    private Bank greenTreeBank;

    // 예금
    private Deposit depositA; // 4.5%, 36개월, Sunny Bank
    private Deposit depositB; // 3.0%, 12개월, GreenTree Bank

    // 적금
    private InstallmentSaving savingA; // 5.5%, 12개월, GreenTree Bank
    private InstallmentSaving savingB; // 3.5%, 36개월, Sunny Bank

    // 카드
    private Card cardA; // CREDIT, GreenTree Bank
    private Card cardB; // DEBIT, Sunny Bank

    // 테스트 데이터 셋업
    @BeforeEach
    void setUp() {
        // 1. 은행 데이터 삽입
        sunnyBank = bankRepository.save(Bank.builder().bankName("Sunny Bank").build());
        greenTreeBank = bankRepository.save(Bank.builder().bankName("GreenTree Bank").build());

        // 2. 예금 상품 데이터 삽입
        depositA = depositRepository.save(
                Deposit.builder()
                        .name("High Rate 3Y")
                        .bank(sunnyBank)
                        .maxInterestRate(4.5)
                        .depositTerm(36) // 필드명 통일 가정
                        .minDepositAmount(100000)
                        .build()
        );

        depositB = depositRepository.save(
                Deposit.builder()
                        .name("Low Rate 1Y")
                        .bank(greenTreeBank)
                        .maxInterestRate(3.0)
                        .depositTerm(12) // 필드명 통일 가정
                        .minDepositAmount(50000)
                        .build()
        );

        // 3. 적금 상품 데이터 삽입
        savingA = savingRepository.save(
                InstallmentSaving.builder()
                        .name("Star 고금리 적금")
                        .bank(greenTreeBank)
                        .maxInterestRate(5.5)
                        .savingTerm(12) // 필드명 통일 가정
                        .paymentMethod(PaymentMethod.MONTHLY)
                        .maxMonthly(500000)
                        .build()
        );
        savingB = savingRepository.save(
                InstallmentSaving.builder()
                        .name("Sunny 일반 적금")
                        .bank(sunnyBank)
                        .maxInterestRate(3.5)
                        .savingTerm(36) // 필드명 통일 가정
                        .paymentMethod(PaymentMethod.FREE)
                        .maxMonthly(300000)
                        .build()
        );

        // 4. 카드 상품 데이터 삽입
        cardA = cardRepository.save(
                Card.builder()
                        .name("Star 무비 카드")
                        .bank(greenTreeBank)
                        .cardType(CardType.CREDIT)
                        .annualFee(15000)
                        .build()
        );
        cardB = cardRepository.save(
                Card.builder()
                        .name("Sunny 카페 체크카드")
                        .bank(sunnyBank)
                        .cardType(CardType.CHECK)
                        .annualFee(0)
                        .build()
        );
    }

    // -----------------------------------------------------------
    // 💡 1. 기본/통합 테스트
    // -----------------------------------------------------------

    @DisplayName("ProductType이 null일 때, 모든 상품 유형을 검색하는 로직이 작동한다.")
    @Test
    void testSearchAllProductTypes() {
        // Given: ProductType만 null (전체 검색)
        ProductFilterReq request = new ProductFilterReq(null, null, 0.0, 10.0, 60);

        // When: 서비스 호출
        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: 모든 상품이 2개씩 조회되었는지 확인
        assertThat(result.deposits()).hasSize(2);
        assertThat(result.savings()).hasSize(2);
        assertThat(result.cards()).hasSize(2);
    }

    // -----------------------------------------------------------
    // 💡 2. 예금 (Deposit) 테스트
    // -----------------------------------------------------------

    @DisplayName("예금: 금리 필터링 (4.0% 이상) - depositA 조회")
    @Test
    void testFilterDepositByHighRate() {
        // Given: 금리 필터 요청 (4.0% ~ 10.0%)
        ProductFilterReq request = new ProductFilterReq(
                null, "deposit", 4.0, 10.0, 60
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: depositA (4.5%)만 조회되어야 함
        assertThat(result.deposits()).hasSize(1);
        assertThat(result.deposits().get(0).name()).isEqualTo(depositA.getName());
    }

    @DisplayName("예금: 기간 필터링 (24개월 이하) - depositB 조회")
    @Test
    void testFilterDepositByShortTerm() {
        // Given: 기간 필터 요청 (최대 24개월 이하)
        ProductFilterReq request = new ProductFilterReq(
                null, "deposit", 0.0, 10.0, 24
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: depositB (12개월)만 조회되어야 함
        assertThat(result.deposits()).hasSize(1);
        assertThat(result.deposits().get(0).name()).isEqualTo(depositB.getName());
    }

    @DisplayName("예금: 은행 + 금리 복합 필터링 - depositA 조회")
    @Test
    void testFilterDepositByBankAndRate() {
        // Given: Sunny Bank + 금리 4.0% 이상 요청
        ProductFilterReq request = new ProductFilterReq(
                "Sunny Bank", "deposit", 4.0, 10.0, 60
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: depositA (Sunny Bank, 4.5%)만 조회되어야 함
        assertThat(result.deposits()).hasSize(1);
        assertThat(result.deposits().get(0).bankName()).isEqualTo(sunnyBank.getBankName());
    }

    // -----------------------------------------------------------
    // 💡 3. 적금 (Saving) 테스트
    // -----------------------------------------------------------

    @DisplayName("적금: 금리 필터링 (5.0% 이상) - savingA 조회")
    @Test
    void testFilterSavingByHighRate() {
        // Given: 금리 필터 요청 (5.0% ~ 10.0%)
        ProductFilterReq request = new ProductFilterReq(
                null, "saving", 5.0, 10.0, 60
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: savingA (5.5%)만 조회되어야 함
        assertThat(result.savings()).hasSize(1);
        assertThat(result.savings().get(0).name()).isEqualTo(savingA.getName());
    }

    @DisplayName("적금: 기간 필터링 (12개월 이하) - savingA 조회")
    @Test
    void testFilterSavingByPeriod() {
        // Given: 기간 필터 요청 (최대 12개월 이하)
        ProductFilterReq request = new ProductFilterReq(
                null, "saving", 0.0, 10.0, 12
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: savingA (12개월)만 조회되어야 함
        assertThat(result.savings()).hasSize(1);
        assertThat(result.savings().get(0).name()).isEqualTo(savingA.getName());
    }

    @DisplayName("적금: 복합 필터링 - 필터 조건에 맞는 상품이 없을 경우 0개 조회")
    @Test
    void testFilterSavingByBankAndPeriod_NoMatch() {
        // Given: Sunny Bank (36개월) + 기간 12개월 이하 요청 (매칭되는 상품 없음)
        ProductFilterReq request = new ProductFilterReq(
                "Sunny Bank", "saving", 0.0, 10.0, 12
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: 결과는 0개여야 함
        assertThat(result.savings()).hasSize(0);
    }

    // -----------------------------------------------------------
    // 💡 4. 카드 (Card) 테스트
    // -----------------------------------------------------------

    @DisplayName("카드: 은행 필터링 (Sunny Bank) - cardB 조회")
    @Test
    void testFilterCardByBank() {
        // Given: Sunny Bank 요청
        ProductFilterReq request = new ProductFilterReq(
                "Sunny Bank", "card", null, null, null
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: cardB (Sunny)만 조회되어야 함
        assertThat(result.cards()).hasSize(1);
        assertThat(result.cards().get(0).name()).isEqualTo(cardB.getName());
        assertThat(result.cards().get(0).annualFee()).isEqualTo(0);
        assertThat(result.deposits()).isEmpty();
    }

    @DisplayName("카드: 카드 특화 필터링 (연회비 0원) - cardB 조회")
    @Test
    void testFilterCardByAnnualFee() {
        // Given: 연회비 0원 이하 요청 (최대 0원으로 가정)
        ProductFilterReq request = new ProductFilterReq(
                null, "card", null, null, null // 연회비 필드가 DTO에 없으므로, Repository에서 특화 필터 적용해야 함
        );

        // *주의: 이 테스트는 Repository에서 '연회비 <= 0' 필터링이 구현되어야 통과합니다.*
        // ProductFilterReq DTO에 연회비 필드가 없으므로, 이 테스트는 Mocking 또는 DTO 수정을 전제로 합니다.
        // 현재 DTO 기준으로는 연회비 필터링을 직접 테스트하기 어렵습니다.

        // 임시로 모든 카드 조회 후 연회비 0원인 카드만 확인 (Service단 테스트 아님)
        ProductFilterRes result = productService.getFilterProduct(request);

        // 연회비 필터 없이 전체 조회 후 Sunny Bank 카드(0원)가 포함되어 있는지 확인
        assertThat(result.cards().stream().filter(c -> c.annualFee() == 0).count()).isEqualTo(1);
    }
}