package com.chuckerteam.chucker.internal.data.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.chuckerteam.chucker.internal.data.repository.RepositoryProvider.initialize
import com.chuckerteam.chucker.internal.data.room.ChuckerDatabase
import com.chuckerteam.chucker.internal.toolkit.SensitivityCheck

/**
 * A singleton to hold the [HttpTransactionRepository] instance.
 * Make sure you call [initialize] before accessing the stored instance.
 */
internal object RepositoryProvider {

    private var transactionRepository: HttpTransactionRepository? = null

    fun transaction(): HttpTransactionRepository {
        return checkNotNull(transactionRepository) {
            "You can't access the transaction repository if you don't initialize it!"
        }
    }

    /**
     * Idempotent method. Must be called before accessing the repositories.
     */
    fun initialize(applicationContext: Context, sensitivityCheck: SensitivityCheck?) {
        if (transactionRepository == null) {
            val db = ChuckerDatabase.create(applicationContext)
            transactionRepository = HttpTransactionDatabaseRepository(db, sensitivityCheck)
        }
    }

    /**
     * Cleanup stored singleton objects
     */
    @VisibleForTesting
    fun close() {
        transactionRepository = null
    }
}
