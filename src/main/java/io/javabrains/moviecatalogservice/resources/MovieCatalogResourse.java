package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResourse {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        //get all rated movie ids;
        UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId,
                UserRating.class);



       return userRating.getUserRating().stream().map(rating -> {
           //for each movie id , call movie info service and get details
             Movie movie=restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);

//             Movie movie = webClientBuilder.build()
//                         .get()
//                         .uri("http://localhost:8082/movies/"+rating.getMovieId())
//                         .retrieve()
//                         .bodyToMono(Movie.class)
//                         .block();

           //put them all together
             return new CatalogItem(movie.getName(),"Desc",rating.getRating());


       }).collect(Collectors.toList());


    }

}
