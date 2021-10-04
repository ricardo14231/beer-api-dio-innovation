package dio.innovation.beerAPI.exception;

public class BeerAlreadyRegisteredException extends Exception {

    public BeerAlreadyRegisteredException() {
        super(String.format("Beer já registrada!"));
    }
}
