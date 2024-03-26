package com.vstudio.pixabay.core.domain.usecase

import com.vstudio.pixabay.core.domain.repository.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRefreshImagesCauseUseCase @Inject constructor(
    private val networkMonitor: NetworkMonitor,
) {
    operator fun invoke(): Flow<Boolean> = networkMonitor.isOnline
}