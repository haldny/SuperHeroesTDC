package com.haldny.domain.usecase

import com.haldny.data.repository.SuperHeroRepository
import com.haldny.domain.model.SuperHero

class GetSuperHeroes(private val superHeroesRepository: SuperHeroRepository) {

    operator fun invoke(): List<SuperHero> = superHeroesRepository.getAllSuperHeroes()
}