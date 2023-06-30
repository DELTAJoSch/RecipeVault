package at.ac.tuwien.sepm.groupphase.backend.engines.implementations;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto.AmountDtoBuilder;
import at.ac.tuwien.sepm.groupphase.backend.engines.OcrEngine;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * This implementation of the OcrEngine uses Tess4J.
 */
@Component
public class DefaultOcrEngine implements OcrEngine {

    private URI textToJsonUrl;
    private Tesseract tesseract;
    private String apiKey;
    private String modelUuid;
    private Map<String, AmountUnit> unitTranslationTable;

    public DefaultOcrEngine() {
        tesseract = new Tesseract();

        apiKey = "KEY_HERE";
        modelUuid = "b899fe6e-8c8d-465a-a395-b43ed13a127f";
        try {
            textToJsonUrl = new URI(
                    "https://text-to-json.com/api/v1/infer?uuid=" + modelUuid + "&apiToken=" + apiKey);
        } catch (URISyntaxException e) {
            throw new InternalServerException("Initialisierung der OCR-Engine fehlgeschlagen, URI war falsch");
        }
        unitTranslationTable = new HashMap<>();
        unitTranslationTable.put("g", AmountUnit.G);
        unitTranslationTable.put("gramm", AmountUnit.G);
        unitTranslationTable.put("gramms", AmountUnit.G);

        unitTranslationTable.put("pice", AmountUnit.PIECE);
        unitTranslationTable.put("stück", AmountUnit.PIECE);

        unitTranslationTable.put("lb", AmountUnit.LB);
        unitTranslationTable.put("pound", AmountUnit.LB);
        unitTranslationTable.put("pfund", AmountUnit.LB);

        unitTranslationTable.put("tsp", AmountUnit.TSP);
        unitTranslationTable.put("teaspoon", AmountUnit.TSP);
        unitTranslationTable.put("teelöffel", AmountUnit.TSP);
        unitTranslationTable.put("tl", AmountUnit.TSP);

        unitTranslationTable.put("tbsp", AmountUnit.TBSP);
        unitTranslationTable.put("tablespoon", AmountUnit.TBSP);
        unitTranslationTable.put("el", AmountUnit.TBSP);

        unitTranslationTable.put("kg", AmountUnit.KG);
        unitTranslationTable.put("kilo", AmountUnit.KG);
        unitTranslationTable.put("kilogramm", AmountUnit.KG);

        unitTranslationTable.put("fl", AmountUnit.Fl);

        unitTranslationTable.put("oz", AmountUnit.OZ);

        unitTranslationTable.put("ml", AmountUnit.ML);

        unitTranslationTable.put("cup", AmountUnit.CUP);

    }

    public String ocrFile(File imageFile) {
        // Set the tessdata path
        tesseract.setDatapath("./tessdata");
        tesseract.setLanguage("deu");
        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new InternalServerException("Etwas mit Tesseract-OCR ist fehlgeschlagen", e);
        }

    }

    public List<AmountDto> extractIngredientList(String recipeString) {

        // build new request for text to json service
        JSONObject requestBody = new JSONObject();
        requestBody.put("input", recipeString);

        HttpRequest request = HttpRequest.newBuilder().uri(textToJsonUrl)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .setHeader("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(300))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new InternalServerException("Etwas mit der Verbindung zu text-to-json.com ist fehlgeschlagen", e);
        }

        if (response.statusCode() == 504) {
            throw new InternalServerException("Die Anfrage zu text-to-json.com hat zu lange gedauert");
        } else if (response.statusCode() != 200) {
            throw new InternalServerException("Ein unbekannter Fehler auf der Seite von text-to-json.com ist aufgetreten" + response.statusCode());
        }
        JSONArray responseJson = new JSONArray(response.body());

        List<AmountDto> amounts = new LinkedList<AmountDto>();

        // convert amounts from json response to amountDTOs
        for (int i = 0; i < responseJson.length(); i++) {
            JSONObject obj = responseJson.getJSONObject(i);
            AmountDtoBuilder amountBuilder = new AmountDto.AmountDtoBuilder()
                    .withAmount(1.0)
                    .withIngredient(null)
                    .withUnit(AmountUnit.PIECE);

            try {
                String name = obj.getString("name");
                amountBuilder.withIngredient(new IngredientDetailsDto.IngredientDetailsDtoBuilder().withName(name)
                        .withCategory(null).build());

                // decode amount to long defaults to 1
                try {
                    amountBuilder.withAmount(Double.parseDouble(obj.getString("amount")));
                } catch (NumberFormatException e) {
                    // Ignore and Continue
                }

                // if unit is known as a synonym transform it to the correct unit default to
                // pice
                String unitString = obj.getString("unit");
                if (unitTranslationTable.containsKey(unitString.toLowerCase())) {
                    amountBuilder.withUnit(unitTranslationTable.get(unitString.toLowerCase()));
                }

            } catch (JSONException e) {
                // just ignore it
            }

            amounts.add(amountBuilder.build());

        }

        return amounts;
    }

}
