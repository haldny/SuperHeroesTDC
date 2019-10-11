package com.haldny

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.salomonbrys.kodein.Kodein
import com.haldny.data.repository.SuperHeroRepository
import com.haldny.domain.model.SuperHero
import com.haldny.recyclerview.RecyclerViewInteraction
import com.haldny.ui.view.MainActivity
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.haldny.ui.view.SuperHeroDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@RunWith(AndroidJUnit4::class)
class MainActivityTest2 : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    var repository: SuperHeroRepository = SuperHeroRepository()

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
    }

    // Unregister your Idling Resource so it can be garbage collected and does not leak any memory
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
    }


    @Test
    fun showsSuperHeroesNameIfThereAreSuperHeroes() {

        startActivity()

        RecyclerViewInteraction.onRecyclerView<SuperHero>(withId(R.id.recycler_view))
            .withItems(repository.getAllSuperHeroes())
            .check { (name), view, exception ->
                matches(hasDescendant(withText(name))).check(
                    view,
                    exception
                )
            }
    }

    @Test
    fun opensSuperHeroDetailActivityOnRecyclerViewItemTapped() {
        val superHeroes = repository.getAllSuperHeroes()
        val superHeroIndex = 0

        startActivity()

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(superHeroIndex, click()))

        val superHeroSelected = superHeroes[superHeroIndex]
        Intents.intended(IntentMatchers.hasComponent(SuperHeroDetailActivity::class.java.canonicalName))
        Intents.intended(IntentMatchers.hasExtra("super_hero_name_key", superHeroSelected.name))
    }

    override val testDependencies = Kodein.Module(allowSilentOverride = true) {}

}