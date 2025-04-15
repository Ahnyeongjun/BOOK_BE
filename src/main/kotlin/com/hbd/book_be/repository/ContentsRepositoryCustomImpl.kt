package com.hbd.book_be.repository

import com.hbd.book_be.domain.Contents
import com.hbd.book_be.domain.QContents.contents
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ContentsRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ContentsRepositoryCustom {
    override fun findAllNonDeletedContents(pageable: Pageable): Page<Contents> {
        val totalCount = queryFactory.select(contents.count())
            .from(contents)
            .where(contents.deletedAt.isNull)
            .fetchOne()

        var query = queryFactory.selectFrom(contents)
            .where(contents.deletedAt.isNull)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        for (order in pageable.sort) {
            val entityPath: PathBuilder<*> = PathBuilder(Contents::class.java, "contents")

            val orderSpecifier = OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                entityPath[order.property] as Expression<Comparable<*>>
            )
            query = query.orderBy(orderSpecifier)
        }

        val result = query.fetch()

        return PageImpl(result, pageable, totalCount ?: 0L)
    }


}