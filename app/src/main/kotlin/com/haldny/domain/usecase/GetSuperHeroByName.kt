package com.haldny.domain.usecase

import com.haldny.data.repository.SuperHeroRepository
import com.haldny.domain.model.SuperHero

class GetSuperHeroByName(private val superHeroesRepository: SuperHeroRepository) {

    operator fun invoke(name: String): SuperHero = superHeroesRepository.getByName(name)
}