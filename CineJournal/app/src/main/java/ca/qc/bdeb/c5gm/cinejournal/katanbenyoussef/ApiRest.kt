package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


data class FilmResults(
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val note: Double,
)

data class ListeFilms(
    val results: List<FilmResults>,
)

data class FilmApi(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: ArrayList<Int>,
    val id: Int,
    val originalLanguage:String,
    val originalTitle:String,
    val overview:String,
    val popularity: Int,
    val posterPath:String,
    val releaseDate:String,
    val title:String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipCode: String,
)

/**
 * Interface qui spécifie des méthodes qui seront générées automatiquement par la librairie Retrofit
 *
 * Très similaire au DAO (Data Access Object) dans Room où vous définissiez vos requêtes
 *
 * Les méthodes peuvent composer
 */
interface ApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.API_KEY_TMDB}"
    ): Response<ListeFilms>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.API_KEY_TMDB}"
    ): Response<ListeFilms>
}

/**
 * Objet d'accès à l'API : essentiellement équivalent au singleton qui donnait accès à la BD dans le cas de Room
 */
object ApiClient {
    private const val BASE_URL: String = "https://api.themoviedb.org/3/"

    private val httpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .build()
    }

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

