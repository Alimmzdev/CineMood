package com.alimmzdev.cinemood_kmp.core.domain.exception

sealed class BaseException(
    message: String? = null,
    val code: Int? = null,
    cause: Throwable? = null
) : Exception(message, cause)

/** Network-related errors (connection, timeout, etc.) */
class NetworkException(
    message: String? = "Network error occurred",
    code: Int? = null,
    cause: Throwable? = null
) : BaseException(message, code, cause)

/** Server-side API errors (4xx, 5xx) */
class ApiException(
    val httpCode: Int,
    message: String? = "API error",
    cause: Throwable? = null
) : BaseException(message, httpCode, cause)

/** Local database errors */
class DatabaseException(
    message: String? = "Database error",
    cause: Throwable? = null
) : BaseException(message, cause = cause)

/** Business logic violations (e.g., invalid input, condition fails) */
class BusinessException(
    message: String? = "Business rule violation",
    cause: Throwable? = null
) : BaseException(message, cause = cause)

/** Unknown or unexpected errors */
class UnknownException(
    message: String? = "Unknown error",
    cause: Throwable? = null
) : BaseException(message, cause = cause)