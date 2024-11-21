

package org.monsing.course

import jakarta.persistence.Embeddable

@Embeddable
class ClassNumber(
    val value: Int
) {
}
