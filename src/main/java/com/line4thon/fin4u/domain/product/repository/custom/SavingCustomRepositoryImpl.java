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
        return QBank.bank.bankName.lower().eq(bankName.toLowerCase());
    }

    // 금리 (우대 금리 기준)
    private BooleanExpression rateBetween(Double minRate, Double maxRate){
        //조건이 없을 경우 항상 참
        BooleanExpression expr = Expressions.asBoolean(true).isTrue();

        // loe : 작거나 같을때
        if (maxRate != null) {
            expr = expr.and(QInstallmentSaving.installmentSaving.maxInterestRate.loe(maxRate));
        }

        // goe : 크거나 같을때
        if (minRate != null) {
            expr = expr.and(QInstallmentSaving.installmentSaving.maxInterestRate.goe(minRate));
        }

        return expr;
    }

    // 기간 (구체적 기간 + 유연한 기간 조건이 2가지)
    private BooleanExpression termBetween(Integer maxTermMonths) {
        //조건이 없을 경우 항상 참
        BooleanExpression expr = Expressions.asBoolean(true).isTrue();

        if (maxTermMonths == null)
            return expr;

        // 3년 이상일때 + 유연한 기간
        if (maxTermMonths == -1) {
            return expr.and(
                    QInstallmentSaving.installmentSaving.savingTerm.goe(36)
                            .or(QInstallmentSaving.installmentSaving.isFlexible.isTrue()));
        }

        // 1년이하 / 3년 이하 + 유연한 기간
        return expr.and(
                QInstallmentSaving.installmentSaving.savingTerm.loe(maxTermMonths)
                        .or(QInstallmentSaving.installmentSaving.isFlexible.isTrue()));
    }
}
