package com.hbd.book_be.domain

import com.hbd.book_be.domain.core.BaseTimeEntity
import com.hbd.book_be.domain.enums.EventFlag
import com.hbd.book_be.domain.enums.EventLocation
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "event",
    indexes = [
        Index(name = "idx_event_title", columnList = "title"),
        Index(name = "idx_event_type", columnList = "event_type"),
        Index(name = "idx_event_flag", columnList = "event_flag"),
        Index(name = "idx_event_date", columnList = "start_date, end_date"),

    ]
)
class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "owner", nullable = false)
    var owner: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    var creator: User,

    @Column(name = "url", nullable = false)
    var url: String,

    @Column(name = "sender_email")
    var senderEmail: String? = null,

    @Column(name = "sender_message")
    var senderMessage: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    var location: EventLocation,

    @Column(name = "is_posting", nullable = false)
    var isPosting: Boolean,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDateTime,

    @Column(name = "end_date")
    var endDate: LocalDateTime,

    @Column(name = "event_type")
    var eventType: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_flag")
    var eventFlag: EventFlag,

    @Column(name = "memo")
    var memo: String? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tagEventsList: MutableList<TagEvent> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookEventList: MutableList<BookEvent> = mutableListOf()

) : BaseTimeEntity() {

    fun getBookList(): List<Book> {
        return bookEventList.map { it.book }
    }

    fun addBook(book: Book) {
        val addedBook = BookEvent(book = book, event = this)
        bookEventList.add(addedBook)
        bookEventList.forEach {
            it.book.addEvent(this)
        }
    }

    fun getTagList(): List<Tag> {
        return tagEventsList.map { it.tag }
    }

    fun addTag(tag: Tag) {
        val addedTag = TagEvent(tag = tag, event = this)

        tagEventsList.add(addedTag)
    }

}
