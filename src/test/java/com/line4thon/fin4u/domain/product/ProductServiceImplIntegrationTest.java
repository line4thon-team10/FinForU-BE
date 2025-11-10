package com.line4thon.fin4u.domain.product;


import com.line4thon.fin4u.domain.product.entity.Bank;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.domain.product.exception.InvalidProductTypeException;
import com.line4thon.fin4u.domain.product.exception.NotFoundSavingException;
import com.line4thon.fin4u.domain.product.repository.BankRepository;
import com.line4thon.fin4u.domain.product.repository.CardRepository;
import com.line4thon.fin4u.domain.product.repository.DepositRepository;
import com.line4thon.fin4u.domain.product.repository.InstallmentSavingRepository;
import com.line4thon.fin4u.domain.product.service.Product.ProductService;
import com.line4thon.fin4u.domain.product.web.dto.ProductDetailRes;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterRes;
import com.line4thon.fin4u.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                        .description("예금 A 설명")
                        .baseInterestRate(4.0)
                        .maxInterestRate(4.5)
                        .depositTerm(36)
                        .isFlexible(false)
                        .minDepositAmount(100000)
                        .build()
        );

        depositB = depositRepository.save(
                Deposit.builder()
                        .name("Low Rate 1Y")
                        .description("예금 b 설명")
                        .bank(greenTreeBank)
                        .maxInterestRate(3.0)
                        .depositTerm(12)
                        .minDepositAmount(50000)
                        .build()
        );

        // 3. 적금 상품 데이터 삽입
        savingA = savingRepository.save(
                InstallmentSaving.builder()
                        .name("Star 고금리 적금")
                        .bank(greenTreeBank)
                        .description("적금 A 설명")
                        .baseInterestRate(5.0)
                        .maxInterestRate(5.5)
                        .savingTerm(12)
                        .isFlexible(true)
                        .maxMonthly(500000)
                        .build()
        );
        savingB = savingRepository.save(
                InstallmentSaving.builder()
                        .name("Sunny 일반 적금")
                        .bank(sunnyBank)
                        .description("적금 B 설명")
                        .maxInterestRate(3.5)
                        .savingTerm(36)
                        .maxMonthly(300000)
                        .build()
        );

        // 4. 카드 상품 데이터 삽입
        cardA = cardRepository.save(
                Card.builder()
                        .name("Star 무비 카드")
                        .bank(greenTreeBank)
                        .cardType(CardType.CREDIT)
                        .description("카드 A 설명")
                        .domesticAnnualFee(15000)
                        .internationalAnnualFee(20000)
                        .build()
        );
        cardB = cardRepository.save(
                Card.builder()
                        .name("Sunny 카페 체크카드")
                        .bank(sunnyBank)
                        .cardType(CardType.CHECK)
                        .description("카드 B 설명")
                        .domesticAnnualFee(0)
                        .build()
        );
    }

    // -----------------------------------------------------------
    // 1. 기본/통합 테스트 (기존 코드)
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
    // 2. 예금 (Deposit) 테스트 (기존 코드)
    // -----------------------------------------------------------

    @DisplayName("예금: 금리 필터링 (4.0% 이상) - depositA 조회")
    @Test
    void testFilterDepositByHighRate() {
        // Given: 금리 필터 요청 (4.0% ~ 10.0%)
        ProductFilterReq request = new ProductFilterReq(
                null, Type.DEPOSIT, 4.0, 10.0, 60
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
                null, Type.DEPOSIT, 0.0, 10.0, 24
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
                "Sunny Bank", Type.DEPOSIT, 4.0, 10.0, 60
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: depositA (Sunny Bank, 4.5%)만 조회되어야 함
        assertThat(result.deposits()).hasSize(1);
        assertThat(result.deposits().get(0).bankName()).isEqualTo(sunnyBank.getBankName());
    }

    // -----------------------------------------------------------
    // 3. 적금 (Saving) 테스트 (기존 코드)
    // -----------------------------------------------------------

    @DisplayName("적금: 금리 필터링 (5.0% 이상) - savingA 조회")
    @Test
    void testFilterSavingByHighRate() {
        // Given: 금리 필터 요청 (5.0% ~ 10.0%)
        ProductFilterReq request = new ProductFilterReq(
                null, Type.SAVING, 5.0, 10.0, 60
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
                null, Type.SAVING, 0.0, 10.0, 12
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
                "Sunny Bank", Type.SAVING, 0.0, 10.0, 12
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: 결과는 0개여야 함
        assertThat(result.savings()).hasSize(0);
    }

    // -----------------------------------------------------------
    // 4. 카드 (Card) 테스트 (기존 코드)
    // -----------------------------------------------------------

    @DisplayName("카드: 은행 필터링 (Sunny Bank) - cardB 조회")
    @Test
    void testFilterCardByBank() {
        // Given: Sunny Bank 요청
        ProductFilterReq request = new ProductFilterReq(
                "Sunny Bank", Type.CARD, null, null, null
        );

        ProductFilterRes result = productService.getFilterProduct(request);

        // Then: cardB (Sunny)만 조회되어야 함
        assertThat(result.cards()).hasSize(1);
        assertThat(result.cards().get(0).name()).isEqualTo(cardB.getName());
        assertThat(result.deposits()).isEmpty();
    }


    // -----------------------------------------------------------
    // 5. Product 상세 조회 (getProductDetail) 테스트
    // -----------------------------------------------------------

    @DisplayName("상세 조회: 카드 상품 ID로 상세 정보를 성공적으로 조회한다.")
    @Test
    void getProductDetail_Card_Success() {
        // Given
        Long cardId = cardA.getId();

        // When
        ProductDetailRes result = productService.getProductDetail(Type.CARD, cardId);

        // Then
        assertThat(result).isNotNull();
        // 카드 상세 정보만 채워져 있어야 함
        assertThat(result.cardDetail()).isNotNull();
        assertThat(result.depositDetail()).isNull();
        assertThat(result.savingDetail()).isNull();

        // 카드 정보 검증
        assertThat(result.cardDetail().id()).isEqualTo(cardId);
        assertThat(result.cardDetail().name()).isEqualTo("Star 무비 카드");
        assertThat(result.cardDetail().bank()).isEqualTo(greenTreeBank.getBankName());
        assertThat(result.cardDetail().internationalAnnualFee()).isEqualTo(20000);

    }

    @DisplayName("상세 조회: 예금 상품 ID로 상세 정보를 성공적으로 조회한다.")
    @Test
    void getProductDetail_Deposit_Success() {
        // Given
        Long depositId = depositA.getId();

        // When
        ProductDetailRes result = productService.getProductDetail(Type.DEPOSIT, depositId);

        // Then
        assertThat(result).isNotNull();
        // 예금 상세 정보만 채워져 있어야 함
        assertThat(result.depositDetail()).isNotNull();
        assertThat(result.cardDetail()).isNull();
        assertThat(result.savingDetail()).isNull();

        // 예금 정보 검증
        assertThat(result.depositDetail().id()).isEqualTo(depositId);
        assertThat(result.depositDetail().name()).isEqualTo("High Rate 3Y");
        assertThat(result.depositDetail().maxRate()).isEqualTo(4.5);
        assertThat(result.depositDetail().termMonths()).isEqualTo(36);
    }

    @DisplayName("상세 조회: 적금 상품 ID로 상세 정보를 성공적으로 조회한다.")
    @Test
    void getProductDetail_Saving_Success() {
        // Given
        Long savingId = savingA.getId();

        // When
        ProductDetailRes result = productService.getProductDetail(Type.SAVING, savingId);

        // Then
        assertThat(result).isNotNull();
        // 적금 상세 정보만 채워져 있어야 함
        assertThat(result.savingDetail()).isNotNull();
        assertThat(result.cardDetail()).isNull();
        assertThat(result.depositDetail()).isNull();

        // 적금 정보 검증
        assertThat(result.savingDetail().id()).isEqualTo(savingId);
        assertThat(result.savingDetail().name()).isEqualTo("Star 고금리 적금");
        assertThat(result.savingDetail().maxRate()).isEqualTo(5.5);
        assertThat(result.savingDetail().termMonths()).isEqualTo(12);
        assertThat(result.savingDetail().isFlexible()).isTrue();

    }

    // -----------------------------------------------------------
    // 6. Product 상세 조회 예외 테스트
    // -----------------------------------------------------------

    @DisplayName("상세 조회 예외: 존재하지 않는 적금 상품 ID 요청 시 NotFoundSavingException이 발생한다.")
    @Test
    void getProductDetail_NotFoundSaving_ThrowsException() {
        // Given
        Long nonExistentId = 9999L;

        // When & Then
        assertThatThrownBy(() -> productService.getProductDetail(Type.SAVING, nonExistentId))
                .isInstanceOf(NotFoundSavingException.class)
        // 에러 메시지 검증 (NotFoundSavingException에 메시지가 있다면 검증)
        // .hasMessageContaining("해당 적금 상품을 찾을 수 없습니다.");
        ;
    }
}