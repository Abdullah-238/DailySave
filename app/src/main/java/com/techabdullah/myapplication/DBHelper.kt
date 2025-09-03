import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class WalletDto
    (
    var walletID: Int? = null,
    var amount: Double?,
    var currency: String?,
    var date: String?,       // ISO 8601 string (e.g., "2025-09-03")
    var note: String?,
    var type: String?,
    var dateShort: String
)

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                $ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $AMOUNT_COL REAL,
                $CURRENCY_COL TEXT,
                $DATE_COL TEXT,
                $NOTE_COL TEXT,
                $TYPE_COL TEXT,
                $DATE_SHORT_COL TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addWallet(wallet: WalletDto)
    {
        val values = ContentValues().apply{
            put(AMOUNT_COL, wallet.amount)
            put(CURRENCY_COL, wallet.currency)
            put(DATE_COL, wallet.date)
            put(NOTE_COL, wallet.note)
            put(TYPE_COL, wallet.type)
            put(DATE_SHORT_COL, wallet.dateShort)
        }
        writableDatabase.use { it.insert(TABLE_NAME, null, values) }
    }

    fun getAllWallets(): List<WalletDto>
    {
        val wallets = mutableListOf<WalletDto>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(ID_COL)
            val amountIndex = cursor.getColumnIndexOrThrow(AMOUNT_COL)
            val currencyIndex = cursor.getColumnIndexOrThrow(CURRENCY_COL)
            val dateIndex = cursor.getColumnIndexOrThrow(DATE_COL)
            val noteIndex = cursor.getColumnIndexOrThrow(NOTE_COL)
            val typeIndex = cursor.getColumnIndexOrThrow(TYPE_COL)
            val dateShortIndex = cursor.getColumnIndexOrThrow(DATE_SHORT_COL)

            while (cursor.moveToNext()) {
                wallets.add(
                    WalletDto(
                        walletID = cursor.getInt(idIndex),
                        amount = if (!cursor.isNull(amountIndex)) cursor.getDouble(amountIndex) else null,
                        currency = cursor.getString(currencyIndex),
                        date = cursor.getString(dateIndex),
                        note = cursor.getString(noteIndex),
                        type = cursor.getString(typeIndex),
                        dateShort = cursor.getString(dateShortIndex) ?: ""
                    )
                )
            }
        }
        return wallets
    }

    fun calculateTotal(): Double {
        readableDatabase.rawQuery("SELECT SUM($AMOUNT_COL) FROM $TABLE_NAME", null).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        }


    }

    fun calculateTotalPriceUSD(): Double {
        readableDatabase.rawQuery(
            "SELECT SUM($AMOUNT_COL) FROM $TABLE_NAME WHERE $CURRENCY_COL = ?",
            arrayOf("USD")
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        }
    }

    fun calculateTotalPriceRS(): Double {
        // Include all variations of RS you might store
        readableDatabase.rawQuery(
            "SELECT SUM($AMOUNT_COL) FROM $TABLE_NAME WHERE $CURRENCY_COL IN (?, ?)",
            arrayOf("RS", "ريال")
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        }
    }

    fun getWalletsByType(type: String): List<WalletDto> {
        val wallets = mutableListOf<WalletDto>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE $TYPE_COL = ?", arrayOf(type)).use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(ID_COL)
            val amountIndex = cursor.getColumnIndexOrThrow(AMOUNT_COL)
            val currencyIndex = cursor.getColumnIndexOrThrow(CURRENCY_COL)
            val dateIndex = cursor.getColumnIndexOrThrow(DATE_COL)
            val noteIndex = cursor.getColumnIndexOrThrow(NOTE_COL)
            val typeIndex = cursor.getColumnIndexOrThrow(TYPE_COL)
            val dateShortIndex = cursor.getColumnIndexOrThrow(DATE_SHORT_COL)

            while (cursor.moveToNext()) {
                wallets.add(
                    WalletDto(
                        walletID = cursor.getInt(idIndex),
                        amount = if (!cursor.isNull(amountIndex)) cursor.getDouble(amountIndex) else null,
                        currency = cursor.getString(currencyIndex),
                        date = cursor.getString(dateIndex),
                        note = cursor.getString(noteIndex),
                        type = cursor.getString(typeIndex),
                        dateShort = cursor.getString(dateShortIndex) ?: ""
                    )
                )
            }
        }
        return wallets
    }

    fun getWalletsByDate(date: String): List<WalletDto> {
        val wallets = mutableListOf<WalletDto>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE $DATE_COL = ?", arrayOf(date)).use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(ID_COL)
            val amountIndex = cursor.getColumnIndexOrThrow(AMOUNT_COL)
            val currencyIndex = cursor.getColumnIndexOrThrow(CURRENCY_COL)
            val dateIndex = cursor.getColumnIndexOrThrow(DATE_COL)
            val noteIndex = cursor.getColumnIndexOrThrow(NOTE_COL)
            val typeIndex = cursor.getColumnIndexOrThrow(TYPE_COL)
            val dateShortIndex = cursor.getColumnIndexOrThrow(DATE_SHORT_COL)

            while (cursor.moveToNext()) {
                wallets.add(
                    WalletDto(
                        walletID = cursor.getInt(idIndex),
                        amount = if (!cursor.isNull(amountIndex)) cursor.getDouble(amountIndex) else null,
                        currency = cursor.getString(currencyIndex),
                        date = cursor.getString(dateIndex),
                        note = cursor.getString(noteIndex),
                        type = cursor.getString(typeIndex),
                        dateShort = cursor.getString(dateShortIndex) ?: ""
                    )
                )
            }
        }
        return wallets
    }

    fun getWalletsByDateAndType(date: String, type: String): List<WalletDto> {
        val wallets = mutableListOf<WalletDto>()
        readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $DATE_COL = ? AND $TYPE_COL = ?",
            arrayOf(date, type)
        ).use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(ID_COL)
            val amountIndex = cursor.getColumnIndexOrThrow(AMOUNT_COL)
            val currencyIndex = cursor.getColumnIndexOrThrow(CURRENCY_COL)
            val dateIndex = cursor.getColumnIndexOrThrow(DATE_COL)
            val noteIndex = cursor.getColumnIndexOrThrow(NOTE_COL)
            val typeIndex = cursor.getColumnIndexOrThrow(TYPE_COL)
            val dateShortIndex = cursor.getColumnIndexOrThrow(DATE_SHORT_COL)

            while (cursor.moveToNext()) {
                wallets.add(
                    WalletDto(
                        walletID = cursor.getInt(idIndex),
                        amount = if (!cursor.isNull(amountIndex)) cursor.getDouble(amountIndex) else null,
                        currency = cursor.getString(currencyIndex),
                        date = cursor.getString(dateIndex),
                        note = cursor.getString(noteIndex),
                        type = cursor.getString(typeIndex),
                        dateShort = cursor.getString(dateShortIndex) ?: ""
                    )
                )
            }
        }
        return wallets
    }


    companion object {
        private const val DATABASE_NAME = "wallet_database"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "wallet_table"
        const val ID_COL = "wallet_id"
        const val AMOUNT_COL = "amount"
        const val CURRENCY_COL = "currency"
        const val DATE_COL = "date"
        const val NOTE_COL = "note"
        const val TYPE_COL = "type"
        const val DATE_SHORT_COL = "date_short"
    }
}
