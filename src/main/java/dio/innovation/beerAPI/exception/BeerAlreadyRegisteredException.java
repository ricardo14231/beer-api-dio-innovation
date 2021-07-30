package dio.innovation.beerAPI.exception;

import java.util.NoSuchElementException;

public class BeerAlreadyRegisteredException extends Exception {

    public BeerAlreadyRegisteredException() {
        super(String.format("Beer já registrada!"));
    }
}
