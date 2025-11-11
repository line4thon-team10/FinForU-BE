package com.line4thon.fin4u.domain.product.repository.custom;

import com.line4thon.fin4u.domain.product.entity.Card;
import com.line4thon.fin4u.domain.product.entity.QBank;
import com.line4thon.fin4u.domain.product.entity.QCard;
import com.line4thon.fin4u.domain.product.entity.QDeposit;
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
public class CardCustomRepositoryImpl implements CardCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Card> searchProducts(ProductFilterReq filter) {
        QCard card = QCard.card;
        QBank bank = QBank.bank;

        //동적 쿼리 시작
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();

        expression = expression.and(bankEq(filter.bank()));

        return queryFactory.selectFrom(card)
                .join(card.bank, bank).fetchJoin()
                .where(expression)
                .fetch();
    }

    @Override
    public List<Card> searchProducts(ProductFilterReq filter, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        QCard card = QCard.card;
        QBank bank = QBank.bank;

        BooleanExpression expression = Expressions.asBoolean(true).isTrue();
        expression = expression.and(bankEq(filter.bank()));

        // 기존 조건 + 바구니 상품 ID 제한 조건 추가
        expression = expression.and(card.id.in(ids));

        return queryFactory.selectFrom(card)
                .join(card.bank, bank).fetchJoin()
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
}
