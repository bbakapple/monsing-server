package org.monsing.report

import jakarta.persistence.*
import org.monsing.BaseEntity
import org.monsing.member.Member
import java.net.URL

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

enum class ReportType {
    INSULT,
    SPAM,
    SEXUAL
}

@Entity
class ReportImage(
    @ManyToOne
    @JoinColumn(name = "report_id")
    val report: Report,

    val url: URL
) : BaseEntity() {
}
