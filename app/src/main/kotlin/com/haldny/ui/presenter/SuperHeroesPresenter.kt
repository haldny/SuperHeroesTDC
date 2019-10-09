package com.haldny.ui.presenter

import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.haldny.common.async
import com.haldny.common.weak
import com.haldny.domain.model.SuperHero
import com.haldny.domain.usecase.GetSuperHeroes
import kotlinx.coroutines.*

class SuperHeroesPresenter(
    view: View,
    private val getSuperHeroes: GetSuperHeroes,
    private val  isAsync: Boolean = false
) : LifecycleObserver, CoroutineScope by MainScope() {

    private val view: View? by weak(view)

    @OnLifecycleEvent(ON_RESUME)
    fun update() {
        view?.showLoading()
        refreshSuperHeroes()
    }

    @OnLifecycleEvent(ON_DESTROY)
    fun destroy() {
        cancel()
    }

    private fun refreshSuperHeroes() = launch {
        val result: List<SuperHero>

        if (isAsync) {
            result = async { getSuperHeroes() }.await()
        } else {
            result = runBlocking { getSuperHeroes() }
        }

        view?.hideLoading()
        when {
            result.isEmpty() -> view?.showEmptyCase()
            else -> view?.showSuperHeroes(result)
        }
    }

    fun onSuperHeroClicked(superHero: SuperHero) = view?.openDetail(superHero.name)

    interface View {
        fun hideLoading()
        fun showSuperHeroes(superHeroes: List<SuperHero>)
        fun showLoading()
        fun showEmptyCase()
        fun openDetail(name: String)
    }
}