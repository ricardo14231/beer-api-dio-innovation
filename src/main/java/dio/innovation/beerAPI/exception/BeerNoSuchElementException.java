package dio.innovation.beerAPI.exception;

import java.util.NoSuchElementException;

public class BeerNoSuchElementException extends NoSuchElementException {

    public BeerNoSuchElementException(Long id) {
        super(String.format("Beer com ID: %o não encontrada!", id));
    }
}
