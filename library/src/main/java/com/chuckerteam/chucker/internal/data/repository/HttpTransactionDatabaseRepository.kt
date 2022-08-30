package com.chuckerteam.chucker.internal.data.repository

import androidx.lifecycle.LiveData
import com.chuckerteam.chucker.api.SensitivityCheck
import com.chuckerteam.chucker.internal.data.entity.HttpTransaction
import com.chuckerteam.chucker.internal.data.entity.HttpTransactionTuple
import com.chuckerteam.chucker.internal.data.room.ChuckerDatabase
import com.chuckerteam.chucker.internal.support.distinctUntilChanged
import java.util.Locale

internal class HttpTransactionDatabaseRepository(
    private val database: ChuckerDatabase,
    private val sensitivityCheck: SensitivityCheck?
) : HttpTransactionRepository {

    companion object {
        const val MOCK_HOST = "https://sensitive.com"
        val SECURITY_ERROR_MSG = "This log contains sensitive data. Please do not log it."
            .replace(' ', '+').lowercase(Locale.ROOT)
        val MOCK_PATH = "/sensitive/$SECURITY_ERROR_MSG"
        val MOCK_URL = "$MOCK_HOST$MOCK_PATH"
    }

    private val transactionDao get() = database.transactionDao()

    override fun getFilteredTransactionTuples(code: String, path: String): LiveData<List<HttpTransactionTuple>> {
        val pathQuery = if (path.isNotEmpty()) "%$path%" else "%"
        return transactionDao.getFilteredTuples("$code%", pathQuery)
    }

    override fun getTransaction(transactionId: Long): LiveData<HttpTransaction?> {
        return transactionDao.getById(transactionId)
            .distinctUntilChanged { old, new -> old?.hasTheSameContent(new) != false }
    }

    override fun getSortedTransactionTuples(): LiveData<List<HttpTransactionTuple>> {
        return transactionDao.getSortedTuples()
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }

    override suspend fun insertTransaction(transaction: HttpTransaction) {
        sensitivityCheck?.let {
            if (it.isCheckHostSensitive(transaction.host)) {
                transaction.host = MOCK_HOST
                transaction.url = MOCK_URL
            }
            if (it.isCheckPathSensitive(transaction.url)) {
                transaction.url = MOCK_URL
                transaction.path = MOCK_PATH
            }
            if (it.isCheckResponseBodySensitive(transaction.responseBody)) {
                transaction.responseBody = SECURITY_ERROR_MSG
            }
            if (it.isRequestBodySensitive(transaction.requestBody)) {
                transaction.requestBody = SECURITY_ERROR_MSG
            }
        }

        val id = transactionDao.insert(transaction)
        transaction.id = id ?: 0
    }

    override fun updateTransaction(transaction: HttpTransaction): Int {
        return transactionDao.update(transaction)
    }

    override suspend fun deleteOldTransactions(threshold: Long) {
        transactionDao.deleteBefore(threshold)
    }

    override suspend fun getAllTransactions(): List<HttpTransaction> = transactionDao.getAll()
}
