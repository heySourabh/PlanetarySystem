package main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet extends ImageView {

    private Image img;
    private double radiusOfRev;
    private double rotationSecs;
    private double revolutionSecs;
    private String name;
    
    double currentAngleRadians = 0.0;

    public double getRadiusOfRev() {
        return radiusOfRev;
    }

    public double getRotationSecs() {
        return rotationSecs;
    }

    public double getRevolutionSecs() {
        return revolutionSecs;
    }

    public String getName() {
        return name;
    }

    public Planet(String name, Image img, double radiusOfRev, double rotationSecs, double revolutionSecs) {
        super(img);
        this.name = name;
        this.img = img;
        this.radiusOfRev = radiusOfRev;
        this.rotationSecs = rotationSecs;
        this.revolutionSecs = revolutionSecs;
        currentAngleRadians = Math.random() * 2 * Math.PI;
    }
}
