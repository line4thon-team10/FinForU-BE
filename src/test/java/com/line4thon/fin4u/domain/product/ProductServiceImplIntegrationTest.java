package com.line4thon.fin4u.domain.product;

import com.line4thon.fin4u.domain.product.entity.Bank;
import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.enums.CardType;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
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

    private final String EN = "en";
    private final String ZH = "zh";

    private Bank sunnyBank;
    private Bank greenTreeBank;

    private Deposit depositA; // 4.5%, 36m, Sunny Bank
    private Deposit depositB; // 3.0%, 12m, GreenTree Bank

    private InstallmentSaving savingA; // 5.5%, 12m, GreenTree Bank
    private InstallmentSaving savingB; // 3.5%, 36m, Sunny Bank

    private Card cardA; // CREDIT, GreenTree Bank
    private Card cardB; // CHECK, Sunny Bank

    @BeforeEach
    void setUp() {
        // Bank
        sunnyBank = bankRepository.save(Bank.builder().bankName("Sunny Bank").build());
        greenTreeBank = bankRepository.save(Bank.builder().bankName("GreenTree Bank").build());

        // Deposit A (Sunny)
        depositA = depositRepository.save(
                Deposit.builder()
                        .nameEn("High Rate 3Y")
                        .nameZh("高利率 3年")
                        .nameVi("Lãi cao 3 năm")
                        .descriptionEn("Deposit A description")
                        .descriptionZh("存款A说明")
                        .descriptionVi("Mô tả tiền gửi A")
                        .bank(sunnyBank)
                        .baseInterestRate(4.0)
                        .maxInterestRate(4.5)
                        .depositTerm(36)
                        .isFlexible(false)
                        .minDepositAmount(100_000)
                        .officialWebsite(null)
                        .build()
        );

        // Deposit B (GreenTree)
        depositB = depositRepository.save(
                Deposit.builder()
                        .nameEn("Low Rate 1Y")
                        .nameZh("低利率 1年")
                        .nameVi("Lãi thấp 1 năm")
                        .descriptionEn("Deposit B description")
                        .descriptionZh("存款B说明")
                        .descriptionVi("Mô tả tiền gửi B")
                        .bank(greenTreeBank)
                        .baseInterestRate(0.0) // 기본금리가 비어있던 예를 보완
                        .maxInterestRate(3.0)
                        .depositTerm(12)
                        .isFlexible(false)
                        .minDepositAmount(50_000)
                        .officialWebsite(null)
                        .build()
        );

        // Saving A (GreenTree)
        savingA = savingRepository.save(
                InstallmentSaving.builder()
                        .nameEn("Star High-rate Saving")
                        .nameZh("明星高利率储蓄")
                        .nameVi("Tiết kiệm lãi cao Star")
                        .descriptionEn("Saving A description")
                        .descriptionZh("储蓄A说明")
                        .descriptionVi("Mô tả tiết kiệm A")
                        .bank(greenTreeBank)
                        .baseInterestRate(5.0)
                        .maxInterestRate(5.5)
                        .savingTerm(12)
                        .isFlexible(true)
                        .maxMonthly(500_000)
                        .officialWebsite(null)
                        .build()
        );

        // Saving B (Sunny)
        savingB = savingRepository.save(
                InstallmentSaving.builder()
                        .nameEn("Sunny Regular Saving")
                        .nameZh("阳光普通储蓄")
                        .nameVi("Tiết kiệm thường Sunny")
                        .descriptionEn("Saving B description")
                        .descriptionZh("储蓄B说明")
                        .descriptionVi("Mô tả tiết kiệm B")
                        .bank(sunnyBank)
                        .baseInterestRate(0.0)
                        .maxInterestRate(3.5)
                        .savingTerm(36)
                        .isFlexible(false)
                        .maxMonthly(300_000)
                        .officialWebsite(null)
                        .build()
        );

        // Card A (GreenTree)
        cardA = cardRepository.save(
                Card.builder()
                        .nameEn("Star Movie Card")
                        .nameZh("明星电影卡")
                        .nameVi("Thẻ phim Star")
                        .descriptionEn("Card A description")
                        .descriptionZh("卡A说明")
                        .descriptionVi("Mô tả thẻ A")
                        .bank(greenTreeBank)
                        .cardType(CardType.CREDIT)
                        .domesticAnnualFee(15_000)
                        .internationalAnnualFee(20_000)
                        .officialWebsite(null)
                        .build()
        );

        // Card B (Sunny)
        cardB = cardRepository.save(
                Card.builder()
                        .nameEn("Sunny Cafe Check Card")
                        .nameZh("阳光咖啡借记卡")
                        .nameVi("Thẻ ghi nợ Sunny Cafe")
                        .descriptionEn("Card B description")
                        .descriptionZh("卡B说明")
                        .descriptionVi("Mô tả thẻ B")
                        .bank(sunnyBank)
                        .cardType(CardType.CHECK)
                        .domesticAnnualFee(0)
                        .internationalAnnualFee(0)
                        .officialWebsite(null)
                        .build()
        );
    }

    // -----------------------------------------------------------
    // 1) 전체 검색
    // -----------------------------------------------------------
    @DisplayName("ProductType이 null이면 모든 상품 유형을 검색한다.")
    @Test
    void searchAllProductTypes() {
        ProductFilterReq req = new ProductFilterReq(null, null, 0.0, 10.0, 60);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.deposits()).hasSize(2);
        assertThat(res.savings()).hasSize(2);
        assertThat(res.cards()).hasSize(2);
    }

    // -----------------------------------------------------------
    // 2) 예금 필터
    // -----------------------------------------------------------
    @DisplayName("예금: 금리 필터(4.0% 이상) → depositA 조회")
    @Test
    void filterDepositByHighRate() {
        ProductFilterReq req = new ProductFilterReq(null, Type.DEPOSIT, 4.0, 10.0, 60);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.deposits()).hasSize(1);
        assertThat(res.deposits().get(0).name()).isEqualTo(depositA.getNameByLang(EN));
    }

    @DisplayName("예금: 기간 필터(24개월 이하) → depositB 조회")
    @Test
    void filterDepositByShortTerm() {
        ProductFilterReq req = new ProductFilterReq(null, Type.DEPOSIT, 0.0, 10.0, 24);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.deposits()).hasSize(1);
        assertThat(res.deposits().get(0).name()).isEqualTo(depositB.getNameByLang(EN));
    }

    @DisplayName("예금: 은행+금리 복합 필터 → depositA 조회")
    @Test
    void filterDepositByBankAndRate() {
        ProductFilterReq req = new ProductFilterReq("Sunny Bank", Type.DEPOSIT, 4.0, 10.0, 60);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.deposits()).hasSize(1);
        assertThat(res.deposits().get(0).bankName()).isEqualTo("Sunny Bank");
    }

    // -----------------------------------------------------------
    // 3) 적금 필터
    // -----------------------------------------------------------
    @DisplayName("적금: 금리 필터(5.0% 이상) → savingA 조회")
    @Test
    void filterSavingByHighRate() {
        ProductFilterReq req = new ProductFilterReq(null, Type.SAVING, 5.0, 10.0, 60);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.savings()).hasSize(1);
        assertThat(res.savings().get(0).name()).isEqualTo(savingA.getNameByLang(EN));
    }

    @DisplayName("적금: 기간 필터(12개월 이하) → savingA 조회")
    @Test
    void filterSavingByPeriod() {
        ProductFilterReq req = new ProductFilterReq(null, Type.SAVING, 0.0, 10.0, 12);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.savings()).hasSize(1);
        assertThat(res.savings().get(0).name()).isEqualTo(savingA.getNameByLang(EN));
    }

    @DisplayName("적금: 은행+기간 복합 필터 매칭 없음 → 0개")
    @Test
    void filterSavingNoMatch() {
        ProductFilterReq req = new ProductFilterReq("Sunny Bank", Type.SAVING, 0.0, 10.0, 12);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.savings()).isEmpty();
    }

    // -----------------------------------------------------------
    // 4) 카드 필터
    // -----------------------------------------------------------
    @DisplayName("카드: 은행 필터(Sunny Bank) → cardB 조회")
    @Test
    void filterCardByBank() {
        ProductFilterReq req = new ProductFilterReq("Sunny Bank", Type.CARD, null, null, null);

        ProductFilterRes res = productService.getFilterProduct(req, EN);

        assertThat(res.cards()).hasSize(1);
        assertThat(res.cards().get(0).name()).isEqualTo(cardB.getNameByLang(EN));
        assertThat(res.deposits()).isEmpty();
    }

    // -----------------------------------------------------------
    // 5) 상세 조회
    // -----------------------------------------------------------
    @DisplayName("상세 조회: 카드")
    @Test
    void getProductDetail_Card() {
        Long id = cardA.getId();

        ProductDetailRes res = productService.getProductDetail(Type.CARD, id, EN);

        assertThat(res.cardDetail()).isNotNull();
        assertThat(res.cardDetail().id()).isEqualTo(id);
        assertThat(res.cardDetail().name()).isEqualTo(cardA.getNameByLang(EN));
        assertThat(res.cardDetail().bank()).isEqualTo(greenTreeBank.getBankName());
        assertThat(res.cardDetail().internationalAnnualFee()).isEqualTo(20_000);
        assertThat(res.depositDetail()).isNull();
        assertThat(res.savingDetail()).isNull();
    }

    @DisplayName("상세 조회: 예금")
    @Test
    void getProductDetail_Deposit() {
        Long id = depositA.getId();

        ProductDetailRes res = productService.getProductDetail(Type.DEPOSIT, id, EN);

        assertThat(res.depositDetail()).isNotNull();
        assertThat(res.depositDetail().id()).isEqualTo(id);
        assertThat(res.depositDetail().name()).isEqualTo(depositA.getNameByLang(EN));
        assertThat(res.depositDetail().maxRate()).isEqualTo(4.5);
        assertThat(res.depositDetail().termMonths()).isEqualTo(36);
        assertThat(res.cardDetail()).isNull();
        assertThat(res.savingDetail()).isNull();
    }

    @DisplayName("상세 조회: 적금")
    @Test
    void getProductDetail_Saving() {
        Long id = savingA.getId();

        ProductDetailRes res = productService.getProductDetail(Type.SAVING, id, EN);

        assertThat(res.savingDetail()).isNotNull();
        assertThat(res.savingDetail().id()).isEqualTo(id);
        assertThat(res.savingDetail().name()).isEqualTo(savingA.getNameByLang(EN));
        assertThat(res.savingDetail().maxRate()).isEqualTo(5.5);
        assertThat(res.savingDetail().termMonths()).isEqualTo(12);
        assertThat(res.savingDetail().isFlexible()).isTrue();
        assertThat(res.cardDetail()).isNull();
        assertThat(res.depositDetail()).isNull();
    }

    // -----------------------------------------------------------
    // 6) 예외
    // -----------------------------------------------------------
    @DisplayName("상세 조회 예외: 적금 ID 미존재 시 NotFoundSavingException")
    @Test
    void getProductDetail_NotFoundSaving() {
        assertThatThrownBy(() -> productService.getProductDetail(Type.SAVING, 9999L, EN))
                .isInstanceOf(NotFoundSavingException.class);
    }

    // -----------------------------------------------------------
    // 7) 다국어 매핑(중국어) 검증 샘플
    // -----------------------------------------------------------
    @DisplayName("ZH 언어 요청 시 다국어 이름 매핑이 동작한다(예금 단건)")
    @Test
    void i18n_ZH_NameMapping_ForDeposit() {
        ProductFilterReq req = new ProductFilterReq(null, Type.DEPOSIT, 0.0, 10.0, 60);

        ProductFilterRes res = productService.getFilterProduct(req, ZH);

        // 두 건 모두 조회되며, 이름은 ZH 필드에서 가져온다.
        assertThat(res.deposits()).hasSize(2);
        // depositA
        assertThat(res.deposits().stream().anyMatch(d -> d.name().equals(depositA.getNameByLang(ZH)))).isTrue();
        // depositB
        assertThat(res.deposits().stream().anyMatch(d -> d.name().equals(depositB.getNameByLang(ZH)))).isTrue();
    }
}
