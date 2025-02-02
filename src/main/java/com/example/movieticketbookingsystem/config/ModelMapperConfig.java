package com.example.movieticketbookingsystem.config;

import com.example.movieticketbookingsystem.model.Movie;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();

//        // Skip the 'id' field while mapping
//        modelMapper.addMappings(new PropertyMap<Movie, Movie>() {
//            @Override
//            protected void configure() {
//                skip(destination.getId());  // Skip mapping the 'id' field
//            }
//        });
//
//        return modelMapper;
    }
}
