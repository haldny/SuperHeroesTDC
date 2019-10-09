package com.haldny.ui.presenter

import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.haldny.common.weak
import com.haldny.domain.model.SuperHero
import com.haldny.domain.usecase.GetSuperHeroByName
import kotlinx.coroutines.*

class SuperHeroDetailPresenter(
    view: View,
    private val getSuperHeroByName: GetSuperHeroByName,
    private val  isAsync: Boolean = false
) : LifecycleObserver, CoroutineScope by MainScope() {

    private val view: View? by weak(view)

    private lateinit var name: String

    fun preparePresenter(name: String?) {
        if (name != null) {
            this.name = name
        } else {
            view?.close()
        }
    }

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
        val result: SuperHero

        if (isAsync) {
            result = async { getSuperHeroByName(name) }.await()
        } else {
            result = runBlocking { getSuperHeroByName(name) }
        }

        view?.hideLoading()
        view?.showSuperHero(result)
    }

    interface View {
        fun close()
        fun showLoading()
        fun hideLoading()
        fun showSuperHero(superHero: SuperHero)
    }
}