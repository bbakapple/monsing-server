package org.monsing.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PortfolioRepository : JpaRepository<Portfolio, Long>
