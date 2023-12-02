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
    val vote_average: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
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

data class User(
    /**
     * Dans le cas où on veut donner un nom plus clair à notre classe
     * que le nom qui est disponible dans l'API, on peut utiliser
     *
     * @SerializedName("nomDansLAPI")
     */
    @SerializedName("id") val identifiantUnique: Int,
    @SerializedName("wat") val name: Int,
    val userName: String,
    val email: String,
    val address: Address?,
    val phone: String,
    val website: String,
)

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipCode: String,
)

data class Comment(
    val postId: Int = 0,
    val id: Int = 0,
    val name: String,
    val email: String,
    val body: String
)

data class Post(
    val userId: Int = 0,
    val id: Int,
    val title: String,
    val body: String
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

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    /**
     * Méthode avec un paramètre à injecter dans l'URL, par exemple :
     *
     * num = 17
     * => https://jsonplaceholder.typicode.com/posts/17
     */
    @GET("posts/{num}")
    suspend fun getPostById(@Path("num") num: Int): Response<Post>

    /**
     * Méthode avec un paramètre à injecter dans la Query String, par exemple :
     *
     * postId = 17
     * => https://jsonplaceholder.typicode.com/comments?postId=17
     */
    @GET("comments")
    suspend fun getCommentsByPost(@Query("postId") postId: Int): Response<List<Comment>>

    /**
     * On peut également définir des méthodes @POST() pour envoyer des données à l'API si celui-ci
     * le supporte
     *
     * D'autres méthodes HTTP sont également possibles, à vous de fouiller dans la doc
     */

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") postId: Int): Response<List<FilmApi>>
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

