package org.monsing.report

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import java.net.URL

@Entity
class ReportImage(
    @ManyToOne
    @JoinColumn(name = "report_id")
    val report: Report,

    val url: URL
) : BaseEntity()
