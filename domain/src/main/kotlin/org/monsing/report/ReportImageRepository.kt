package org.monsing.report

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportImageRepository : JpaRepository<ReportImage, Long> {
}
