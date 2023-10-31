package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.logging.Logger

/**
 * Ensemble de data class pour représenter les données fournies par l'API
 */

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
    /**
     * URL de base pour toutes les requêtes faites à l'API
     */
    private const val BASE_URL: String = "https://api.themoviedb.org/3/"

    /** __by lazy__ est un construit de Kotlin qui permet d'initialiser une variable
     * au moment de l'utiliser pour la première fois
     *
     * Ça fait essentiellement ce qu'un singleton ferait avec :
     *     if(INSTANCE == null)
     *         INSTANCE = new Bidule()
     *
     * Mais en plus gracieux, en utilisant une fonctionnalité du langage plutôt qu'on codant
     * cette logique à la main à chaque fois
     */
    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
        // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .header("Authorization",
                        "Bearer ${BuildConfig.API_KEY_TMDB}")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        /**
         *  Le patron Builder est utilisé ici
         *  Voir: https://refactoring.guru/design-patterns/builder
         *
         * Un Builder est un objet de configuration dont les méthodes sont typiquement chaînables
         *
         * C'est une bonne alternative à définir un constructeur avec 2178643 arguments,
         * qui sont tous possiblement optionnels
         */
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
