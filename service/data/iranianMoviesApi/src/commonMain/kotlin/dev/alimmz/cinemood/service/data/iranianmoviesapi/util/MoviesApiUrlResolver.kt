package dev.alimmz.cinemood.service.data.iranianmoviesapi.util

internal object MoviesApiUrlResolver {
    private const val BASE_URL: String = "https://moviesapi.ir"
    fun resolveImageUrl(path: String): String {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path
        }
        return "$BASE_URL/images/$path"
    }
}
