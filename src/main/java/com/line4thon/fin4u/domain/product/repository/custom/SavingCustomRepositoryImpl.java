package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.QBank;
import com.line4thon.fin4u.domain.product.entity.QInstallmentSaving;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SavingCustomRepositoryImpl implements SavingCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<InstallmentSaving> searchProducts(ProductFilterReq filter) {
        QInstallmentSaving saving = QInstallmentSaving.installmentSaving;
        QBank bank = QBank.bank;

        //동적 쿼리 시작
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        //각 필터링
        expression = expression.and(bankEq(filter.bank()));
        expression = expression.and(rateBetween(filter.minRate(), filter.maxRate()));
        expression = expression.and(termBetween(filter.termMonths()));

        return queryFactory.selectFrom(saving)
                .join(saving.bank, bank).fetchJoin()
                .where(expression)
                .fetch();
    }

    // SQL 쿼리의 WHERE 절에 들어갈 조건을 동적으로 생성하는 메서드

    // 은행
    private BooleanExpression bankEq(String bankName){
        if(bankName == null)
            return null;
        return QBank.bank.bankName.equalsIgnoreCase(bankName);
    }

    // 금리 (우대 금리 기준)
    private BooleanExpression rateBetween(Double minRate, Double maxRate){
        if (minRate == null || maxRate == null)
            return null;

        return QInstallmentSaving.installmentSaving.maxInterestRate.between(minRate, maxRate);
    }

    // 기간
    private BooleanExpression termBetween(Integer maxTermMonths) {
        if (maxTermMonths == null)
            return null;
        QInstallmentSaving saving = QInstallmentSaving.installmentSaving;

        // DTO에서 받은 기간(TermMonths) 이하의 상품만 검색
        // loe : loe(Less or Equal)
        return saving.savingTerm.loe(maxTermMonths);
    }
}
