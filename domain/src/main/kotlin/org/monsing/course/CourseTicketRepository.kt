package org.monsing.course

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseTicketRepository : JpaRepository<CourseTicket, Long> {
}
