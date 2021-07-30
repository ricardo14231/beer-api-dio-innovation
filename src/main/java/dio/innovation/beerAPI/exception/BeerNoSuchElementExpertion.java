package dio.innovation.beerAPI.exception;

import java.util.NoSuchElementException;

public class BeerNoSuchElementExpertion extends NoSuchElementException {

    public BeerNoSuchElementExpertion(Long id) {
        super(String.format("Beer com ID: %o não encontrada!", id));
    }
}
