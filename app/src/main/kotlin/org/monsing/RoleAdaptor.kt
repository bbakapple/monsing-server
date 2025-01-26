package org.monsing

import org.monsing.auth.jwt.Role
import org.monsing.record.RecordRoleHandler
import org.springframework.stereotype.Component

@Component
class RoleAdaptor(
    private val recordRoleHandlers: List<RecordRoleHandler>
) {

    fun <T> handleRecord(role: Role, function: (RecordRoleHandler) -> T): T {
        return recordRoleHandlers.firstOrNull { it.canHandle(role) }?.let {
            function(it)
        } ?: throw IllegalArgumentException("Unsupported role: $role")
    }
}
