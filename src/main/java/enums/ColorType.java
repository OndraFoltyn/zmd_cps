package enums;

public enum ColorType {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue");

    String color;

    ColorType(String color) {
        this.color = color;
    }
}