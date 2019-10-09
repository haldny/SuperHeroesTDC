package com.haldny

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.haldny.data.repository.SuperHeroRepository
import com.haldny.domain.model.SuperHero
import com.haldny.matchers.ToolbarMatcher.onToolbarWithTitle
import com.haldny.ui.view.SuperHeroDetailActivity
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class SuperHeroDetailActivityTests :
    AcceptanceTest<SuperHeroDetailActivity>(SuperHeroDetailActivity::class.java) {

    @Mock
    lateinit var repository: SuperHeroRepository

    @Test
    fun showsSuperHeroNameAsToolbarTitle() {
        val superHero = givenThereIsASuperHero()

        startActivity(superHero)

        onToolbarWithTitle(superHero.name).check(matches(isDisplayed()))
    }

    @Test
    fun hidesProgressBarOnSuperHeroLoaded() {
        val superHero = givenThereIsASuperHero()

        startActivity(superHero)

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showsSuperHeroName() {
        val superHero = givenThereIsASuperHero()

        startActivity(superHero)
        scrollToView(R.id.tv_super_hero_name)

        onView(allOf(withId(R.id.tv_super_hero_name), withText(superHero.name))).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun showsSuperHeroDescription() {
        val superHero = givenThereIsASuperHero()

        startActivity(superHero)
        scrollToView(R.id.tv_super_hero_description)

        onView(withText(superHero.description)).check(matches(isDisplayed()))
    }

    @Test
    fun doesNotShowAvengersBadgeIfSuperHeroIsNotPartOfTheAvengersTeam() {
        val superHero = givenThereIsASuperHero(isAvenger = false)

        startActivity(superHero)

        onView(withId(R.id.iv_avengers_badge)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showsAvengersBadgeIfSuperHeroIsPartOfTheAvengersTeam() {
        val superHero = givenAnAvenger()

        startActivity(superHero)

        onView(withId(R.id.iv_avengers_badge)).check(matches(isDisplayed()))
    }

    private fun givenAnAvenger(): SuperHero = givenThereIsASuperHero(isAvenger = true)

    private fun givenThereIsASuperHero(isAvenger: Boolean = false): SuperHero {
        val superHeroName = "SuperHero"
        val superHeroPhoto = "https://i.annihil.us/u/prod/marvel/i/mg/c/60/55b6a28ef24fa.jpg"
        val superHeroDescription = "Super Hero Description"
        val superHero = SuperHero(superHeroName, superHeroPhoto, isAvenger, superHeroDescription)
        whenever(repository.getByName(superHeroName)).thenReturn(superHero)
        return superHero
    }

    private fun startActivity(superHero: SuperHero): SuperHeroDetailActivity {
        val args = Bundle()
        args.putString("super_hero_name_key", superHero.name)
        return startActivity(args)
    }

    private fun scrollToView(viewId: Int) {
        onView(withId(viewId)).perform(scrollTo())
    }

    override val testDependencies = Kodein.Module(allowSilentOverride = true) {
        bind<SuperHeroRepository>() with instance(repository)
    }
}