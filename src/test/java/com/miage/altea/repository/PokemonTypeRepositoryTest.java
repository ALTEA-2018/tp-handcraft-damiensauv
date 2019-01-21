package com.miage.altea.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTypeRepositoryTest {

    private PokemonTypeRepository repository = new PokemonTypeRepository();

    @Test
    void findPokemonById_with25_shouldReturnPikachu(){
        var pikachu = repository.findPokemonById(25);
        assertNotNull(pikachu);
        assertEquals("pikachu", pikachu.getName());
        assertEquals(25, pikachu.getId());
    }

    @Test
    void findPokemonById_with145_shouldReturnZapdos(){
        var zapdos = repository.findPokemonById(145);
        assertNotNull(zapdos);
        assertEquals("zapdos", zapdos.getName());
        assertEquals(145, zapdos.getId());
    }

    @Test
    void findPokemonByName_withEevee_shouldReturnEevee(){
        var eevee = repository.findPokemonByName("eevee");
        assertNotNull(eevee);
        assertEquals("eevee", eevee.getName());
        assertEquals(133, eevee.getId());
    }

    @Test
    void findPokemonByName_withMewTwo_shouldReturnMewTwo(){
        var mewtwo = repository.findPokemonByName("mewtwo");
        assertNotNull(mewtwo);
        assertEquals("mewtwo", mewtwo.getName());
        assertEquals(150, mewtwo.getId());
    }

    @Test
    void findAllPokemon_shouldReturn151Pokemons(){
        var pokemons = repository.findAllPokemon();
        assertNotNull(pokemons);
        assertEquals(151, pokemons.size());
    }

}
