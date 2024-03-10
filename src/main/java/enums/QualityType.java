package enums;

public enum QualityType {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
    Y("Y"),
    Cb("Cb"),
    Cr("Cr"),
    RGB("RGB"),
    YcBcR("YCbCr");

    String quality;

    QualityType(String quality) {
        this.quality = quality;
    }
}
