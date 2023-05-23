package fr.epf.projet.cinefil5.model


import com.google.gson.annotations.SerializedName

data class KeywordResult(
    val page: Int,
    val results: List<KeywordDetailsResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class KeywordDetailsResult(
    val id: Int,
    val name: String
)