package helium314.keyboard.latin.cloud

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object TranslateService {

    /**
     * Translates [text] from [sourceLang] to [targetLang] using Google's public translation endpoint.
     * @param sourceLang Language code (e.g., "auto", "en", "bn")
     * @param targetLang Language code (e.g., "bn", "en", "es")
     */
    suspend fun translate(text: String, sourceLang: String = "auto", targetLang: String = "bn"): Result<String> = withContext(Dispatchers.IO) {
        if (text.isBlank()) return@withContext Result.success("")

        try {
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val urlString = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=$sourceLang&tl=$targetLang&dt=t&q=$encodedText"
            val url = URL(urlString)
            
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val translatedText = parseTranslationResponse(response)
                Result.success(translatedText)
            } else {
                Result.failure(Exception("HTTP Error ${connection.responseCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseTranslationResponse(jsonResponse: String): String {
        val result = StringBuilder()
        val jsonArray = JSONArray(jsonResponse)
        val sentences = jsonArray.optJSONArray(0) ?: return ""

        for (i in 0 until sentences.length()) {
            val sentence = sentences.optJSONArray(i)
            if (sentence != null) {
                result.append(sentence.optString(0, ""))
            }
        }
        return result.toString()
    }
}

