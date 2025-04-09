package com.hbd.book_be.domain

import com.hbd.book_be.domain.core.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "book",
    indexes = [
        Index(name = "idx_book_title", columnList = "title"),
        Index(name = "idx_book_published_date", columnList = "published_date"),
    ]
)
class Book(
    @Id
    @Column(name = "isbn", nullable = false, updatable = false)
    var isbn: String,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "summary", nullable = false)
    var summary: String,

    @Column(name = "published_date", nullable = false)
    var publishedDate: LocalDateTime,

    @Column(name = "detail_url")
    var detailUrl: String? = null,

    @Column(name = "translator")
    var translator: String? = null,

    @Column(name = "price")
    var price: Int? = null,

    @Column(name = "title_image")
    var titleImage: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    var author: Author,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    var publisher: Publisher,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookContentsList: MutableList<BookContents> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookEventList: MutableList<BookEvent> = mutableListOf(),
) : BaseTimeEntity() {

    fun getContentsList(): List<Contents> {
        return this.bookContentsList.map { it.contents }
    }

    fun addContents(contents: Contents) {
        val addedBookContents = BookContents(book = this, contents = contents)

        this.bookContentsList.add(addedBookContents)
        contents.bookContentsList.add(addedBookContents)
    }

    fun getEventList(): List<Event> {
        return this.bookEventList.map { it.event }
    }

    fun addEvent(event: Event) {
        val addedBookEvent = BookEvent(book = this, event = event)
        this.bookEventList.add(addedBookEvent)
        event.bookEventList.add(addedBookEvent)
    }
}

