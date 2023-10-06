package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Context
import androidx.room.*

@Entity
data class Client(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    val prenom: String?,
    val nom: String?,
    val telephone: String?
)

@Dao
interface ClientDao {
    @Query("SELECT * FROM Client")
    suspend fun getAll(): List<Client>

    @Query("SELECT * FROM Client WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Client>

    @Query(
        "SELECT * FROM Client WHERE prenom LIKE :prenom AND " +
                "nom LIKE :nom LIMIT 1"
    )
    suspend fun findByName(prenom: String, nom: String): Client

    @Insert
    suspend fun insertAll(vararg clients: Client)

    @Update
    suspend fun updateAll(vararg clients: Client)

    @Delete
    suspend fun delete(client: Client)
}

@Database(entities = [Client::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao

    companion object {
        // Singleton empêche plusieurs instances de la base
        // d'être ouvertes en même temps
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            // si INSTANCE n'est pas null, on le retourne,
            // sinon, on crée la base de données
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sqlite_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}