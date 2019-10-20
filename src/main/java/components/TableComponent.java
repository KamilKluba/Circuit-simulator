package components;


import javafx.scene.image.ImageView;

public class TableComponent {
    private String name;
    private Integer inputsNumber;
    private ImageView imageView;

    public TableComponent(String name, Integer inputsNumber, ImageView imageView) {
        this.name = name;
        this.inputsNumber = inputsNumber;
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInputsNumber() {
        return inputsNumber;
    }

    public void setInputsNumber(Integer inputsNumber) {
        this.inputsNumber = inputsNumber;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
