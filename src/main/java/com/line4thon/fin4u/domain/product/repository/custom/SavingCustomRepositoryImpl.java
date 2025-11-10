package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.InstallmentSaving;
import com.line4thon.fin4u.domain.product.entity.QBank;
import com.line4thon.fin4u.domain.product.entity.QDeposit;
import com.line4thon.fin4u.domain.product.entity.QInstallmentSaving;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
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

    @Override
    public List<InstallmentSaving> searchProducts(ProductFilterReq filter, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        QInstallmentSaving saving = QInstallmentSaving.installmentSaving;
        QBank bank = QBank.bank;

        //동적 쿼리 시작
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        //각 필터링
        expression = expression.and(bankEq(filter.bank()));
        expression = expression.and(rateBetween(filter.minRate(), filter.maxRate()));
        expression = expression.and(termBetween(filter.termMonths()));

        expression = expression.and(saving.id.in(ids));

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
        if (minRate == null && maxRate == null)
            return null;

        // loe : 작거나 같을때
        if (minRate == null)
            return QDeposit.deposit.maxInterestRate.loe(maxRate);

        // goe : 크거나 같을때
        if (maxRate == null)
            return QDeposit.deposit.maxInterestRate.goe(minRate);

        return QDeposit.deposit.maxInterestRate.between(minRate, maxRate);
    }

    // 기간 (구체적 기간 + 유연한 기간 조건이 2가지)
    private BooleanExpression termBetween(Integer maxTermMonths) {
        if (maxTermMonths == null)
            return null;

        // 3년 이상일때
        if (maxTermMonths == -1) {
            return QDeposit.deposit.depositTerm.goe(36)
                    .or(QDeposit.deposit.isFlexible.isTrue());
        }

        // 1년이하 / 3년 이하 + 유연한 기간
        return QDeposit.deposit.depositTerm.loe(maxTermMonths)
                .or(QDeposit.deposit.isFlexible.isTrue());
    }
}
