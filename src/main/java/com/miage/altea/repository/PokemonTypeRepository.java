package com.miage.altea.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.bo.PokemonType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PokemonTypeRepository {

    private List<PokemonType> pokemons;

    public PokemonTypeRepository() {
        try {
            var pokemonsStream = this.getClass().getResourceAsStream("/pokemons.json");

            var objectMapper = new ObjectMapper();
            var pokemonsArray = objectMapper.readValue(pokemonsStream, PokemonType[].class);
            this.pokemons = Arrays.asList(pokemonsArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonType findPokemonById(int id) {
        System.out.println("Loading Pokemon information for Pokemon id " + id);


        return this.pokemons.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public PokemonType findPokemonByName(String name) {
        System.out.println("Loading Pokemon information for Pokemon name " + name);

        return this.pokemons.stream().filter(x -> name.equals(x.getName())).findFirst().orElse(null);
    }

    public List<PokemonType> findAllPokemon() {
        return this.pokemons;
    }
}
