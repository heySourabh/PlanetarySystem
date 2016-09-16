package main;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PlanetarySystem extends Application {

    boolean pause = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group planetGroup = createPlanetarySystem();
        Scene scene = new Scene(planetGroup, 1300, 1050, Color.TRANSPARENT);
        primaryStage.setScene(scene);

        primaryStage.initStyle(StageStyle.TRANSPARENT);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(planetGroup);
            }
        };
        timer.start();

        scene.setOnKeyTyped(event -> {
            if (event.getCharacter().equals("" + (char) 27)) { // Escape key
                System.exit(0);
            }
            if (event.getCharacter().equals(" ")) {
                pause = !pause;
            }
        });

        //primaryStage.setOnCloseRequest(we -> we.consume());
        primaryStage.getIcons().add(
                //new Image(getClass().getResource("/images/icon_16x16.png").toString())
                new Image(getClass().getResource("/images/icon_32x32.png").toString())
                //new Image(getClass().getResource("/images/icon_64x64.png").toString()),
                //new Image(getClass().getResource("/images/icon_128x128.png").toString())
        );
        primaryStage.setTitle("Planetary System");
        primaryStage.show();
    }

    private static Tooltip objectNameTip;

    private Group createPlanetarySystem() {
        Group planetSystem = new Group();
        // Create sun
        ImageView sun = new ImageView(new Image(getClass().getResourceAsStream("/images/sun.png")));
        sun.setOnMouseClicked(e -> {
            setObjectNameTip(sun, "Sun", e);
        });
        setRotationTransition(sun, 25, false);
        sun.setScaleX(0.15);
        sun.setScaleY(0.15);
        planetSystem.getChildren().add(sun);

        // Create mercury
        Planet mercury = new Planet("Mercury",
                new Image(getClass().getResourceAsStream("/images/mercury.png")),
                80, 59, 88);
        planetSystem.getChildren().add(mercury);

        // Create venus
        Planet venus = new Planet("Venus",
                new Image(getClass().getResourceAsStream("/images/venus.png")),
                160, 243, 225);
        planetSystem.getChildren().add(venus);

        // Create earth
        Planet earth = new Planet("Earth",
                new Image(getClass().getResourceAsStream("/images/earth.png")),
                240, 1, 365);
        planetSystem.getChildren().add(earth);

        // Create mars
        Planet mars = new Planet("Mars",
                new Image(getClass().getResourceAsStream("/images/mars.png")),
                320, 1, 687);
        planetSystem.getChildren().add(mars);

        // Create jupiter
        Planet jupiter = new Planet("Jupiter",
                new Image(getClass().getResourceAsStream("/images/jupiter.png")),
                400, 0.4, 4332);
        planetSystem.getChildren().add(jupiter);

        // Create saturn
        Planet saturn = new Planet("Saturn",
                new Image(getClass().getResourceAsStream("/images/saturn.png")),
                520, 0.45, 10759);
        planetSystem.getChildren().add(saturn);

        planetSystem.getChildren().stream()
                .filter(p -> p instanceof Planet)
                .map(p -> (Planet) p)
                .forEach(planet -> {
                    planet.setOnMouseClicked(e
                            -> setObjectNameTip(planet, planet.getName(), e));
                    planet.setScaleX(0.15);
                    planet.setScaleY(0.15);
                    boolean clockwise = planet.getName().equals("Venus") ? true : false;
                    setRotationTransition(planet, planet.getRotationSecs(), clockwise);
                });
        planetSystem.setTranslateX(200);
        planetSystem.setTranslateY(80);
        return planetSystem;
    }

    private void setObjectNameTip(Node object, String name, MouseEvent e) {
        if (objectNameTip != null) {
            objectNameTip.hide();
        }
        objectNameTip = new Tooltip(name);
        objectNameTip.setAutoHide(true);
        objectNameTip.show(object, e.getScreenX(), e.getScreenY());
    }

    private void setRotationTransition(Node object, double secsForRotation, boolean clockwise) {
        RotateTransition rotation
                = new RotateTransition(Duration.seconds(secsForRotation), object);
        rotation.setFromAngle(0);
        rotation.setToAngle(clockwise ? 360 : -360);
        rotation.setInterpolator(Interpolator.LINEAR);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.play();
    }

    private void update(Group group) {
        ImageView sun = (ImageView) group.getChildren().stream()
                .filter(ob -> !(ob instanceof Planet))
                .findAny().get();
        group.getChildren().stream()
                .filter(p -> p instanceof Planet)
                .map(p -> (Planet) p)
                .forEach(planet -> {
                    double incr = pause ? 0.0 : 2.0 * Math.PI / planet.getRevolutionSecs() * 1.0 / 60.0;
                    planet.currentAngleRadians -= incr;
                    planet.currentAngleRadians %= 2.0 * Math.PI;
                    planet.setTranslateX(sun.getImage().getWidth() / 2
                            - planet.getImage().getWidth() / 2
                            + planet.getRadiusOfRev() * Math.cos(planet.currentAngleRadians));
                    planet.setTranslateY(sun.getImage().getHeight() / 2
                            - planet.getImage().getHeight() / 2
                            + planet.getRadiusOfRev() * Math.sin(planet.currentAngleRadians));
                });
    }
}
