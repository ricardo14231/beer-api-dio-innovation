package dio.innovation.beerAPI.exception;

public class BeerQuantityException extends Exception {

    public BeerQuantityException() {
        super(String.format("Quantidade de cerveja excedeu o estoque máximo!"));
    }
}
