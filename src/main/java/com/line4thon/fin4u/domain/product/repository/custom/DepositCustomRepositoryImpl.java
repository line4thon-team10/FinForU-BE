package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.entity.QInstallmentSaving;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.line4thon.fin4u.domain.product.entity.QDeposit;
import com.line4thon.fin4u.domain.product.entity.QBank;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepositCustomRepositoryImpl implements DepositCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Deposit> searchProducts(ProductFilterReq filter) {
        QDeposit deposit = QDeposit.deposit;
        QBank bank = QBank.bank;

        //동적 쿼리 시작
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        //각 필터링
        expression = expression.and(bankEq(filter.bank()));
        expression = expression.and(rateBetween(filter.minRate(), filter.maxRate()));
        expression = expression.and(termBetween(filter.termMonths()));

        return queryFactory.selectFrom(deposit)
                .join(deposit.bank, bank).fetchJoin()
                .where(expression)
                .fetch();
    }

    @Override
    public List<Deposit> searchProducts(ProductFilterReq filter, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        QDeposit deposit = QDeposit.deposit;
        QBank bank = QBank.bank;

        //동적 쿼리 시작
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        //각 필터링
        expression = expression.and(bankEq(filter.bank()));
        expression = expression.and(rateBetween(filter.minRate(), filter.maxRate()));
        expression = expression.and(termBetween(filter.termMonths()));

        expression = expression.and(deposit.id.in(ids));

        return queryFactory.selectFrom(deposit)
                .join(deposit.bank, bank).fetchJoin()
                .where(expression)
                .fetch();
    }

    // SQL 쿼리의 WHERE 절에 들어갈 조건을 동적으로 생성하는 메서드
    // 예시
    /*
        WHERE
        (bank.bankName = 'Sunny Bank')
        AND (deposit.maxInterestRate BETWEEN 3.0 AND 5.0)
        AND (deposit.depositTerm <= 36)
     */

    // 은행
    private BooleanExpression bankEq(String bankName){
        if(bankName == null)
            return null;
        return QBank.bank.bankName.lower().eq(bankName.toLowerCase());
    }

    // 금리 (우대 금리 기준)
    // 금리 (우대 금리 기준)
    private BooleanExpression rateBetween(Double minRate, Double maxRate){
        //조건이 없을 경우 항상 참
        BooleanExpression expr = Expressions.asBoolean(true).isTrue();

        // loe : 작거나 같을때
        if (maxRate != null) {
            expr = expr.and(QDeposit.deposit.maxInterestRate.loe(maxRate));
        }

        // goe : 크거나 같을때
        if (minRate != null) {
            expr = expr.and(QDeposit.deposit.maxInterestRate.goe(minRate));
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
                    QDeposit.deposit.depositTerm.goe(36)
                            .or(QDeposit.deposit.isFlexible.isTrue()));
        }

        // 1년이하 / 3년 이하 + 유연한 기간
        return expr.and(
                QDeposit.deposit.depositTerm.loe(maxTermMonths)
                        .or(QDeposit.deposit.isFlexible.isTrue()));
    }

}
