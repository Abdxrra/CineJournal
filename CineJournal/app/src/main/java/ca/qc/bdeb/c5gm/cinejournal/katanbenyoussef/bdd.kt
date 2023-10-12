package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Context
import androidx.room.*

@Entity
data class Film(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    val titre: String,
    val description: String,
    val annee: Int,
    val rating: Double,
    val imageUri: String
)

@Dao
interface ClientDao {
    @Query("SELECT * FROM Film")
    suspend fun getAll(): List<Film>

    @Query(
        "SELECT * FROM Film WHERE titre LIKE :titre LIMIT 1"
    )
    suspend fun findByTitre(titre: String): Film

    @Insert
    suspend fun insertAll(vararg films: Film)

    @Update
    suspend fun updateAll(vararg films: Film)

    @Update
    suspend fun updateOne(vararg film: Film)

    @Delete
    suspend fun delete(film: Film)
}

@Database(entities = [Film::class], version = 1)
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