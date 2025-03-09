package org.monsing.util

inline fun <T> T?.toNonNull(
    supplier: () -> Throwable = {
        IllegalArgumentException("NonNull Type 변경에 실패헀습니다.")
    },
): T {
    return this ?: throw supplier.invoke()
}
