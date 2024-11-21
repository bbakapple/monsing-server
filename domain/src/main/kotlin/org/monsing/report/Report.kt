package org.monsing.report

import jakarta.persistence.*
import org.monsing.BaseEntity
import org.monsing.member.Member

@Entity
class Report(
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    val reporter: Member,

    @ManyToOne
    @JoinColumn(name = "reported_id")
    val reported: Member,

    @Enumerated(EnumType.STRING)
    val reportType: ReportType,

    val detail: String
) : BaseEntity() {
}




