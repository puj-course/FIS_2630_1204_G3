import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  interfaz grafica (JavaFX) donde se muestran los
 * destinos recomendados, incluyendo ciudad y pais, respetando el orden
 * de coincidencia generado por el algoritmo 
 **/
public class VistaDestinosFX extends Application {

    // Paleta 
        private static final String INK = "#3B2733";
    private static final String INK_SOFT = "#4F3644";
    private static final String PAPER = "#F7E3D8";
    private static final String PAPER_STUB = "#F3C9D6";
    private static final String AMBER = "#C2477E";
    private static final String TEAL = "#D89AA9";
    private static final String TERRACOTTA = "#A6435F";
    private static final String TEXT_DARK = "#3B2733";
    private static final String TEXT_MUTED = "#8C6F73";
    @Override
    public void start(Stage stage) {
        RecomendadorDestinos recomendador = new RecomendadorDestinos();
        SelectorDestinos selector = new SelectorDestinos();

        List<Ciudad> ciudades = ciudadesDeEjemplo();
        PreferenciasUsuario preferencias = preferenciasDeEjemplo();

        List<ResultadoRecomendacion> resultados = recomendador.recomendarDestinos(ciudades, preferencias);
        SeleccionDestinos seleccion = selector.seleccionarMejoresDestinos(resultados);

        VBox root = construirVista(seleccion);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + INK + "; -fx-background-color: " + INK + ";");

        Scene scene = new Scene(scroll, 520, 640);
        stage.setTitle("WiseTrip - Destinos recomendados");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Construye el contenedor visual,
     * respetando el orden y mostrando el mensaje adecuado segun la cantidad
     * de destinos disponibles.
     */
    private VBox construirVista(SeleccionDestinos seleccion) {
        VBox root = new VBox(16);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + INK + ";");

        Label titulo = new Label("WiseTrip");
        titulo.setFont(Font.font("Cambria", FontWeight.BOLD, 30));
        titulo.setTextFill(Color.web(PAPER));

        Label subtitulo = new Label("Destinos recomendados segun tus preferencias y presupuesto.");
        subtitulo.setTextFill(Color.web("#B7C4CE"));
        subtitulo.setWrapText(true);

        root.getChildren().addAll(titulo, subtitulo);

        List<ResultadoRecomendacion> destinos = seleccion.getDestinos();

        // Mensaje cuando hay menos de 3 destinos o ninguno.
        if (destinos.isEmpty()) {
            root.getChildren().add(construirEstadoVacio(seleccion.getMensaje()));
            return root;
        }

        if (destinos.size() < 3) {
            root.getChildren().add(construirAviso(seleccion.getMensaje()));
        }

        int posicion = 1;
        for (ResultadoRecomendacion resultado : destinos) {
            root.getChildren().add(construirTicket(posicion, resultado));
            posicion++;
        }

        return root;
    }

    private HBox construirTicket(int posicion, ResultadoRecomendacion resultado) {
        Ciudad ciudad = resultado.getCiudad();

        
        VBox sello = new VBox(2);
        sello.setAlignment(Pos.CENTER);
        sello.setPadding(new Insets(6, 10, 6, 10));
        sello.setStyle("-fx-background-color: " + TERRACOTTA + "; -fx-background-radius: 6;");
        Label labelGate = new Label("GATE");
        labelGate.setFont(Font.font("Consolas", FontWeight.SEMI_BOLD, 9));
        labelGate.setTextFill(Color.web(PAPER));
        Label numGate = new Label(String.format("%02d", posicion));
        numGate.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        numGate.setTextFill(Color.web(PAPER));
        sello.getChildren().addAll(labelGate, numGate);

        // ciudad y pais 
        VBox info = new VBox(2);
        info.setAlignment(Pos.CENTER_LEFT);
        Label ciudadLabel = new Label(ciudad.getNombre());
        ciudadLabel.setFont(Font.font("Cambria", FontWeight.BOLD, 20));
        ciudadLabel.setTextFill(Color.web(TEXT_DARK));
        Label paisLabel = new Label(ciudad.getPais().toUpperCase());
        paisLabel.setFont(Font.font("Consolas", FontWeight.NORMAL, 12));
        paisLabel.setTextFill(Color.web(TEXT_MUTED));
        info.getChildren().addAll(ciudadLabel, paisLabel);

        HBox infoBox = new HBox(14, sello, info);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(14, 16, 14, 16));
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // 
        Region perforado = new Region();
        perforado.setPrefWidth(2);
        perforado.setStyle("-fx-background-color: transparent; -fx-border-color: transparent " +
                PAPER_STUB + " transparent transparent; -fx-border-width: 0 2 0 0; -fx-border-style: dashed;");

        // puntaje
        VBox stub = new VBox(4);
        stub.setAlignment(Pos.CENTER);
        stub.setPadding(new Insets(14, 18, 14, 18));
        stub.setStyle("-fx-background-color: " + PAPER_STUB + ";");
        Label labelPuntaje = new Label("COINCIDENCIA");
        labelPuntaje.setFont(Font.font("Consolas", FontWeight.SEMI_BOLD, 9));
        labelPuntaje.setTextFill(Color.web(TEXT_MUTED));
        Label valorPuntaje = new Label(String.format("%.0f%%", resultado.getPuntajeTotal() * 100));
        valorPuntaje.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        valorPuntaje.setTextFill(Color.web(AMBER));
        stub.getChildren().addAll(labelPuntaje, valorPuntaje);

        HBox ticket = new HBox(infoBox, stub);
        ticket.setAlignment(Pos.CENTER_LEFT);
        ticket.setStyle("-fx-background-color: " + PAPER + "; -fx-background-radius: 10; " +
                "-fx-border-color: " + PAPER_STUB + "; -fx-border-radius: 10;");
        ticket.setEffect(new javafx.scene.effect.DropShadow(6, Color.rgb(0, 0, 0, 0.25)));

        return ticket;
    }

    private Label construirAviso(String mensaje) {
        Label aviso = new Label(mensaje);
        aviso.setWrapText(true);
        aviso.setTextFill(Color.web(AMBER));
        aviso.setPadding(new Insets(10, 14, 10, 14));
        aviso.setStyle("-fx-background-color: " + INK_SOFT + "; -fx-background-radius: 6; " +
                "-fx-border-color: " + AMBER + "; -fx-border-width: 0 0 0 3;");
        return aviso;
    }

    private VBox construirEstadoVacio(String mensaje) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setStyle("-fx-background-color: " + INK_SOFT + "; -fx-background-radius: 10; " +
                "-fx-border-color: " + TEAL + "; -fx-border-style: dashed; -fx-border-radius: 10;");

        Label titulo = new Label("SIN VUELOS DISPONIBLES");
        titulo.setFont(Font.font("Consolas", FontWeight.SEMI_BOLD, 11));
        titulo.setTextFill(Color.web(AMBER));

        Label texto = new Label(mensaje);
        texto.setWrapText(true);
        texto.setTextFill(Color.web(PAPER));
        texto.setAlignment(Pos.CENTER);

        box.getChildren().addAll(titulo, texto);
        return box;
    }

    // Datos de ejemplo, solo para poder ejecutar y ver la pantalla 

    private List<Ciudad> ciudadesDeEjemplo() {
        List<Ciudad> ciudades = new ArrayList<>();
        ciudades.add(new Ciudad(1, "Cartagena", "Colombia", 1_500_000, mapaAtributos(true, true, true, false)));
        ciudades.add(new Ciudad(2, "San Andres", "Colombia", 2_200_000, mapaAtributos(true, false, false, true)));
        ciudades.add(new Ciudad(3, "Bogota", "Colombia", 900_000, mapaAtributos(false, true, true, false)));
        ciudades.add(new Ciudad(4, "Medellin", "Colombia", 1_100_000, mapaAtributos(false, true, true, true)));
        ciudades.add(new Ciudad(5, "Santa Marta", "Colombia", 1_300_000, mapaAtributos(true, true, false, true)));
        ciudades.add(new Ciudad(6, "Cali", "Colombia", 1_000_000, mapaAtributos(false, true, false, false)));
        return ciudades;
    }

    private PreferenciasUsuario preferenciasDeEjemplo() {
        Map<String, Boolean> atributosUsuario = new HashMap<>();
        atributosUsuario.put("playa", true);
        atributosUsuario.put("vidaNocturna", true);
        atributosUsuario.put("cultura", false);
        atributosUsuario.put("naturaleza", false);
        return new PreferenciasUsuario(1_200_000, atributosUsuario);
    }

    private Map<String, Boolean> mapaAtributos(boolean playa, boolean vidaNocturna,
                                                boolean cultura, boolean naturaleza) {
        Map<String, Boolean> atributos = new HashMap<>();
        atributos.put("playa", playa);
        atributos.put("vidaNocturna", vidaNocturna);
        atributos.put("cultura", cultura);
        atributos.put("naturaleza", naturaleza);
        return atributos;
    }

    public static void main(String[] args) {
        launch(args);
    }
}