package dio.innovation.beerAPI.utils;

public class StringBeerDTOBuilder {

    public static String createBeerBuilder() {
        return "{\n" +
                "\t\"id\":1,\n" +
                "\t\"name\":\"Brahma\",\n" +
                "\t\"brand\":\"Ambev\",\n" +
                "\t\"max\":50,\n" +
                "\t\"quantity\":20,\n" +
                "\t\"type\":\"ALE\",\n" +
                "\t\"consumptionTemperature\": 5,\n" +
                "\t\"alcoholicStrenght\" : 60,\n" +
                "\t\"description\" : \"description...\"\n" +
                "}";
    }

    public static String createBeerNoNameBuilder() {
        return "{\n" +
                "\t\"id\":1,\n" +
                "\t\"brand\":\"Ambev\",\n" +
                "\t\"max\":50,\n" +
                "\t\"quantity\":20,\n" +
                "\t\"type\":\"ALE\",\n" +
                "\t\"consumptionTemperature\": 5,\n" +
                "\t\"alcoholicStrenght\" : 60,\n" +
                "\t\"description\" : \"description...\"\n" +
                "}";
    }

    public static String updateBeerBuilder() {
        return "{\n" +
                "\t\"id\":1,\n" +
                "\t\"name\":\"Skol\",\n" +
                "\t\"brand\":\"Ambev\",\n" +
                "\t\"max\":50,\n" +
                "\t\"quantity\":20,\n" +
                "\t\"type\":\"ALE\",\n" +
                "\t\"consumptionTemperature\": 5,\n" +
                "\t\"alcoholicStrenght\" : 60,\n" +
                "\t\"description\" : \"description...\"\n" +
                "}";
    }

}
