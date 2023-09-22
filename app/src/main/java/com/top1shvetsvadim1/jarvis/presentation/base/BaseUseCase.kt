package com.top1shvetsvadim1.jarvis.presentation.base

interface BaseUseCase

interface UseCase<ARG, RETURN> : BaseUseCase {
    operator fun invoke(params: ARG): RETURN
}

interface UseCaseNoParams<RETURN> : BaseUseCase {
    operator fun invoke(): RETURN
}

interface SuspendUseCase<ARG, RETURN>: BaseUseCase {
    suspend operator fun invoke(params: ARG): RETURN
}

interface SuspendUseCaseNoParams<RETURN>: BaseUseCase {
    suspend operator fun invoke(): RETURN
}