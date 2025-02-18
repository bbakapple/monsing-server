package org.monsing.course

import org.springframework.data.jpa.repository.JpaRepository

interface ClassRoomRepository: JpaRepository<ClassRoom, Long>
