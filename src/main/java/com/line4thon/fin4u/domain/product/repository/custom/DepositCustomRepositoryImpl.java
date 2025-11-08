package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Deposit;
import com.line4thon.fin4u.domain.product.web.dto.ProductFilterReq;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.line4thon.fin4u.domain.product.entity.QDeposit;
import com.line4thon.fin4u.domain.product.entity.QBank;

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
        return QBank.bank.bankName.equalsIgnoreCase(bankName);
    }

    // 금리 (우대 금리 기준)
    private BooleanExpression rateBetween(Double minRate, Double maxRate){
        if (minRate == null || maxRate == null)
            return null;

        return QDeposit.deposit.maxInterestRate.between(minRate, maxRate);
    }

    // 기간
    private BooleanExpression termBetween(Integer maxTermMonths) {
        if (maxTermMonths == null)
            return null;
        QDeposit deposit = QDeposit.deposit;

        // DTO에서 받은 기간(TermMonths) 이하의 상품만 검색
        // loe : loe(Less or Equal)
        return deposit.depositTerm.loe(maxTermMonths);
    }

}
