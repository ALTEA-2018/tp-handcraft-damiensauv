package com.miage.altea.controller;

import com.miage.altea.bo.PokemonType;
import com.miage.altea.repository.PokemonTypeRepository;
import com.miage.altea.servlet.Controller;
import com.miage.altea.servlet.RequestMapping;

import java.util.Map;

@Controller
public class PokemonTypeController {
    private PokemonTypeRepository repository = new PokemonTypeRepository();

    @RequestMapping(uri = "/pokemon")
    public PokemonType getPokemon(Map<String, String[]> parameters) {

        if (parameters == null)
            throw new IllegalArgumentException("parameters should not be empty");

        this.checkParams(parameters);

        Map.Entry<String, String[]> p = parameters.entrySet().iterator().next();
        String key = p.getKey();
        String[] value = p.getValue();
        if ("id".equals(key)) {
            return this.repository.findPokemonById(Integer.parseInt(value[0]));
        } else if ("name".equals(key)) {
            return this.repository.findPokemonByName(value[0]);
        }

        return null;
    }

    private void checkParams(Map<String, String[]> parameters) {
        for (Map.Entry<String, String[]> p : parameters.entrySet()) {
            String key = p.getKey();
            if (!key.equals("id") && !key.equals("name"))
                throw new IllegalArgumentException("unknown parameter");
        }
    }


}
