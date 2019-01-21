package com.miage.altea.controller;

import com.miage.altea.bo.PokemonType;
import com.miage.altea.repository.PokemonTypeRepository;
import com.miage.altea.servlet.Controller;
import com.miage.altea.servlet.RequestMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonTypeControllerTest {

    @InjectMocks
    PokemonTypeController controller;

    @Mock
    PokemonTypeRepository pokemonRepository;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPokemon_shouldRequireAParameter(){
        var exception = assertThrows(IllegalArgumentException.class,
                () -> controller.getPokemon(null));
        assertEquals("parameters should not be empty", exception.getMessage());
    }

    @Test
    void getPokemon_shouldRequireAKnownParameter(){
        var parameters = Map.of("test", new String[]{"25"});
        var exception = assertThrows(IllegalArgumentException.class,
                () -> controller.getPokemon(parameters));
        assertEquals("unknown parameter", exception.getMessage());
    }

    @Test
    void getPokemon_withAnIdParameter_shouldReturnAPokemon(){
        var pikachu = new PokemonType();
        pikachu.setId(25);
        pikachu.setName("pikachu");
        when(pokemonRepository.findPokemonById(25)).thenReturn(pikachu);

        var parameters = Map.of("id", new String[]{"25"});
        var pokemon = controller.getPokemon(parameters);
        assertNotNull(pokemon);
        assertEquals(25, pokemon.getId());
        assertEquals("pikachu", pokemon.getName());

        verify(pokemonRepository).findPokemonById(25);
        verifyNoMoreInteractions(pokemonRepository);
    }

    @Test
    void getPokemon_withANameParameter_shouldReturnAPokemon(){
        var zapdos = new PokemonType();
        zapdos.setId(145);
        zapdos.setName("zapdos");
        when(pokemonRepository.findPokemonByName("zapdos")).thenReturn(zapdos);

        var parameters = Map.of("name", new String[]{"zapdos"});
        var pokemon = controller.getPokemon(parameters);
        assertNotNull(pokemon);
        assertEquals(145, pokemon.getId());
        assertEquals("zapdos", pokemon.getName());

        verify(pokemonRepository).findPokemonByName("zapdos");
        verifyNoMoreInteractions(pokemonRepository);
    }

    @Test
    void pokemonTypeController_shouldBeAnnotated(){
        var controllerAnnotation =
                PokemonTypeController.class.getAnnotation(Controller.class);
        assertNotNull(controllerAnnotation);
    }

    @Test
    void getPokemon_shouldBeAnnotated() throws NoSuchMethodException {
        var getPokemonMethod =
                PokemonTypeController.class.getDeclaredMethod("getPokemon", Map.class);
        var requestMappingAnnotation =
                getPokemonMethod.getAnnotation(RequestMapping.class);

        assertNotNull(requestMappingAnnotation);
        assertEquals("/pokemons", requestMappingAnnotation.uri());
    }

}
