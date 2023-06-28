package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.WineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * This data generator generates wine data.
 */
@Profile("generateData")
@Component
public class WineDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final WineRepository wineRepository;

    public WineDataGenerator(UserRepository userRepository, WineRepository wineRepository) {
        this.userRepository = userRepository;
        this.wineRepository = wineRepository;
    }

    public void generate() {
        LOGGER.trace("generate()");
        var admin = userRepository.findByEmail("admin@example.com");

        if (admin != null) {
            Wine nerodavola = new Wine.WineBuilder()
                .setCategory(WineCategory.FULL_RED)
                .setName("Nero d'Avola")
                .setDescription("Ein kräftiger Rotwein mit tiefroter Farbe. Nero d'Avola"
                    + " zeichnet sich durch Aromen von schwarzen Kirschen, Pflaumen und"
                    + " Gewürzen aus. Er hat eine ausgeprägte Tanninstruktur und einen"
                    + " vollmundigen Geschmack. Nero d'Avola passt gut zu gegrilltem Fleisch"
                    + " und reifem Käse.")
                .setTemperature(18.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Nero d'Avola")
                .setCountry("IT")
                .build();
            this.addIfNotExists(nerodavola);

            Wine moscatel = new Wine.WineBuilder()
                .setCategory(WineCategory.DESSERT)
                .setName("Moscatel de Setubal")
                .setDescription("Ein süßer Dessertwein mit goldener Farbe. Moscatel zeichnet"
                    + " sich durch Aromen von Orangenblüten, Honig und exotischen Früchten aus."
                    + " Er hat eine hohe Restsüße und einen intensiven Geschmack. Moscatel passt"
                    + " gut zu Desserts, Obstkuchen und Blauschimmelkäse.")
                .setTemperature(13.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Moscatel")
                .setCountry("ES")
                .build();
            this.addIfNotExists(moscatel);

            Wine cabernet = new Wine.WineBuilder()
                .setCategory(WineCategory.FULL_RED)
                .setName("Cabernet Sauvignon")
                .setDescription("Ein kräftiger Rotwein mit dunkler Farbe. "
                    + "Cabernet Sauvignon zeichnet sich durch Aromen von schw"
                    + "arzen Johannisbeeren, schwarzen Kirschen und Gewürzen aus. "
                    + "Er hat oft gut strukturierte Tannine und eine längere Lagerfähigkeit.")
                .setTemperature(11.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Cabernet Sauvignon")
                .setCountry("FR")
                .build();
            this.addIfNotExists(cabernet);

            Wine chardonnay = new Wine.WineBuilder()
                .setCategory(WineCategory.FULL_WHITE)
                .setName("Chardonnay")
                .setDescription("Ein eleganter Weißwein mit goldgelber Farbe. Chardonnay"
                    + " zeichnet sich durch Aromen von tropischen Früchten, Äpfeln und"
                    + " Vanille aus. Er hat oft eine cremige Textur und passt gut zu Fisch"
                    + " und Geflügelgerichten.")
                .setTemperature(12.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Chardonnay")
                .setCountry("FR")
                .build();
            this.addIfNotExists(chardonnay);

            Wine grenacheRose = new Wine.WineBuilder()
                .setCategory(WineCategory.ROSE)
                .setName("Grenache Rosé")
                .setDescription("Ein erfrischender Roséwein mit einer blassrosa Farbe."
                    + " Grenache Rosé zeichnet sich durch Aromen von Erdbeeren, Himbeeren"
                    + " und Zitrusfrüchten aus. Er ist leicht und fruchtig und eignet sich"
                    + " gut als Aperitif oder zu leichten Sommergerichten.")
                .setTemperature(10.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Grenache")
                .setCountry("FR")
                .build();
            this.addIfNotExists(grenacheRose);

            Wine champagne = new Wine.WineBuilder()
                .setCategory(WineCategory.SPARKLING)
                .setName("Champagner")
                .setDescription("Ein luxuriöser Schaumwein mit einer hellgelben Farbe."
                    + " Champagner zeichnet sich durch Aromen von grünen Äpfeln, Zitrusfrüchten"
                    + " und Hefenoten aus. Er hat feine Perlage und passt gut zu festlichen"
                    + " Anlässen oder als Begleitung zu Meeresfrüchten.")
                .setTemperature(7.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Chardonnay, Pinot Noir, Pinot Meunier")
                .setCountry("FR")
                .build();
            this.addIfNotExists(champagne);

            Wine merlot = new Wine.WineBuilder()
                .setCategory(WineCategory.MIDDLE_RED)
                .setName("Merlot")
                .setDescription("Ein geschmeidiger Rotwein mit rubinroter Farbe. Merlot"
                    + " zeichnet sich durch Aromen von roten Früchten, Pflaumen und Gewürzen"
                    + " aus. Er hat eine weiche Tanninstruktur und passt gut zu Fleischgerichten"
                    + " und Käse.")
                .setTemperature(16.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Merlot")
                .setCountry("FR")
                .build();
            this.addIfNotExists(merlot);

            Wine sauvignonBlanc = new Wine.WineBuilder()
                .setCategory(WineCategory.FULL_WHITE)
                .setName("Sauvignon Blanc")
                .setDescription("Ein erfrischender Weißwein mit blassgelber Farbe. Sauvignon"
                    + " Blanc zeichnet sich durch Aromen von Stachelbeeren, Zitrusfrüchten und"
                    + " frischem Gras aus. Er hat eine lebendige Säure und passt gut zu"
                    + " Salaten und leichten Fischgerichten.")
                .setTemperature(10.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Sauvignon Blanc")
                .setCountry("NZ")
                .build();
            this.addIfNotExists(sauvignonBlanc);

            Wine syrahRose = new Wine.WineBuilder()
                .setCategory(WineCategory.ROSE)
                .setName("Syrah/Shiraz Rosé")
                .setDescription("Ein fruchtiger Roséwein mit einer zarten rosa Farbe. Syrah"
                    + " Rosé zeichnet sich durch Aromen von Himbeeren, Wassermelone und"
                    + " Kräutern aus. Er ist gut ausbalanciert und passt gut zu gegrilltem"
                    + " Gemüse und leichten Vorspeisen.")
                .setTemperature(7.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Syrah")
                .setCountry("AU")
                .build();
            this.addIfNotExists(syrahRose);

            Wine sauternes = new Wine.WineBuilder()
                .setCategory(WineCategory.DESSERT)
                .setName("Sauternes")
                .setDescription("Ein edelsüßer Dessertwein mit einer goldenen Farbe. Sauternes"
                    + " zeichnet sich durch Aromen von Honig, getrockneten Früchten und"
                    + " Botrytis aus. Er ist reichhaltig und süß und passt gut zu"
                    + " Foie Gras und Desserts.")
                .setTemperature(10.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Sémillon, Sauvignon Blanc, Muscadelle")
                .setCountry("FR")
                .build();
            this.addIfNotExists(sauternes);

            Wine pinotNoir = new Wine.WineBuilder()
                .setCategory(WineCategory.LIGHT_RED)
                .setName("Pinot Noir")
                .setDescription("Ein eleganter Rotwein mit heller rubinroter Farbe. Pinot Noir"
                    + " zeichnet sich durch Aromen von roten Beeren, Kirschen und Gewürzen aus."
                    + " Er hat eine feine Tanninstruktur und passt gut zu Geflügel, Fisch und"
                    + " Pilzgerichten.")
                .setTemperature(14.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Vitis Vinifera")
                .setCountry("FR")
                .build();
            this.addIfNotExists(pinotNoir);

            Wine riesling = new Wine.WineBuilder()
                .setCategory(WineCategory.AROMATIC_WHITE)
                .setName("Riesling")
                .setDescription("Ein frischer Weißwein mit blassgelber Farbe. Riesling"
                    + " zeichnet sich durch Aromen von Zitrusfrüchten, Pfirsichen und"
                    + " mineralischen Noten aus. Er hat eine lebendige Säure und passt"
                    + " gut zu asiatischer Küche und scharfen Gerichten.")
                .setTemperature(8.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Riesling")
                .setCountry("AT")
                .build();
            this.addIfNotExists(riesling);

            Wine provenceRose = new Wine.WineBuilder()
                .setCategory(WineCategory.ROSE)
                .setName("Provence Rosé")
                .setDescription("Ein leichter und aromatischer Roséwein mit blassrosa Farbe."
                    + " Provence Rosé zeichnet sich durch Aromen von Erdbeeren, Pfirsichen und"
                    + " Kräutern aus. Er ist trocken und erfrischend, perfekt für den"
                    + " Sommergenuss.")
                .setTemperature(10.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Grenache, Cinsault, Syrah, Mourvèdre")
                .setCountry("FR")
                .build();
            this.addIfNotExists(provenceRose);

            Wine port = new Wine.WineBuilder()
                .setCategory(WineCategory.DESSERT)
                .setName("Portwein (Rot)")
                .setDescription("Ein kräftiger und süßer Dessertwein mit dunkler rubinroter"
                    + " Farbe. Portwein zeichnet sich durch Aromen von dunklen Früchten,"
                    + " Schokolade und Gewürzen aus. Er hat einen hohen Alkoholgehalt und passt"
                    + " gut zu Käse und Schokoladendesserts.")
                .setTemperature(16.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Touriga Nacional, Touriga Franca, Tinta Roriz")
                .setCountry("FR")
                .build();
            this.addIfNotExists(port);

            Wine shiraz = new Wine.WineBuilder()
                .setCategory(WineCategory.FULL_RED)
                .setName("Shiraz/Syrah")
                .setDescription("Ein kräftiger Rotwein mit dunkelroter Farbe. Shiraz"
                    + " zeichnet sich durch Aromen von schwarzen Früchten, Pfeffer und"
                    + " Gewürzen aus. Er hat eine reiche Textur und passt gut zu gegrilltem"
                    + " Fleisch und würzigen Speisen.")
                .setTemperature(18.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Shiraz/Syrah")
                .setCountry("AU")
                .build();
            this.addIfNotExists(shiraz);

            Wine gewurztraminer = new Wine.WineBuilder()
                .setCategory(WineCategory.AROMATIC_WHITE)
                .setName("Gewürztraminer")
                .setDescription("Ein intensiver Weißwein mit goldgelber Farbe. Gewürztraminer"
                    + " zeichnet sich durch Aromen von Litschi, Rosenblüten und exotischen"
                    + " Gewürzen aus. Er hat einen vollen Körper und passt gut zu würzigen"
                    + " asiatischen Gerichten und kräftigem Käse.")
                .setTemperature(12.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Gewürztraminer")
                .setCountry("FR")
                .build();
            this.addIfNotExists(gewurztraminer);

            Wine tempranilloRosado = new Wine.WineBuilder()
                .setCategory(WineCategory.ROSE)
                .setName("Tempranillo Rosado")
                .setDescription("Ein fruchtiger und würziger Roséwein mit einer hellen"
                    + " rosa Farbe. Tempranillo Rosado zeichnet sich durch Aromen von"
                    + " Erdbeeren, Kirschen und Kräutern aus. Er ist erfrischend und passt"
                    + " gut zu mediterranen Gerichten und Tapas.")
                .setTemperature(9.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Tempranillo")
                .setCountry("ES")
                .build();
            this.addIfNotExists(tempranilloRosado);

            Wine icewine = new Wine.WineBuilder()
                .setCategory(WineCategory.DESSERT)
                .setName("Eiswein")
                .setDescription("Ein edelsüßer Dessertwein, der aus gefrorenen Trauben"
                    + " gewonnen wird. Icewine zeichnet sich durch Aromen von Honig,"
                    + " Aprikosen und exotischen Früchten aus. Er ist reich und süß und"
                    + " passt gut zu Desserts und kräftigem Käse.")
                .setTemperature(10.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Verschiedene Sorten")
                .setCountry("AT")
                .build();
            this.addIfNotExists(icewine);

            Wine prosecco = new Wine.WineBuilder()
                .setCategory(WineCategory.SPARKLING)
                .setName("Prosecco")
                .setDescription("Ein spritziger Schaumwein mit blassgelber Farbe. Prosecco"
                    + " zeichnet sich durch Aromen von grünen Äpfeln, Birnen und Zitrusfrüchten"
                    + " aus. Er hat eine lebhafte Perlage und eignet sich gut als Aperitif oder"
                    + " als Basis für spritzige Cocktails.")
                .setTemperature(6.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Glera")
                .setCountry("IT")
                .build();
            this.addIfNotExists(prosecco);

            Wine cava = new Wine.WineBuilder()
                .setCategory(WineCategory.SPARKLING)
                .setName("Cava")
                .setDescription("Ein spanischer Schaumwein mit blassgelber Farbe. Cava zeichnet"
                    + " sich durch Aromen von grünen Äpfeln, Zitrusfrüchten und Mandeln aus."
                    + " Er hat eine feine Perlage und ist eine erschwingliche Alternative zu"
                    + " Champagner.")
                .setTemperature(7.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Macabeo, Parellada, Xarel·lo")
                .setCountry("ES")
                .build();
            this.addIfNotExists(cava);

            Wine sekt = new Wine.WineBuilder()
                .setCategory(WineCategory.SPARKLING)
                .setName("Sekt")
                .setDescription("Ein deutscher Schaumwein mit einer hellgelben Farbe. Sekt"
                    + " zeichnet sich durch Aromen von grünen Äpfeln, Zitrusfrüchten und"
                    + " weißen Blüten aus. Er hat eine lebhafte Perlage und ist ein beliebtes"
                    + " Getränk für festliche Anlässe.")
                .setTemperature(7.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Verschiedene Sorten")
                .setCountry("DE")
                .build();
            this.addIfNotExists(sekt);

            Wine pinotGrigio = new Wine.WineBuilder()
                .setCategory(WineCategory.LIGHT_WHITE)
                .setName("Pinot Grigio")
                .setDescription("Ein leichter Weißwein mit blassgelber Farbe. Pinot Grigio"
                    + " zeichnet sich durch Aromen von grünen Äpfeln, Birnen und Zitrusfrüchten"
                    + " aus. Er hat eine dezente Säure und passt gut zu leichten Fischgerichten"
                    + " und Gemüse.")
                .setTemperature(8.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Pinot Grigio, Vitis Vinifera")
                .setCountry("IT")
                .build();
            this.addIfNotExists(pinotGrigio);

            Wine grunerVeltliner = new Wine.WineBuilder()
                .setCategory(WineCategory.LIGHT_WHITE)
                .setName("Grüner Veltliner")
                .setDescription("Ein frischer Weißwein mit blassgelber Farbe. Grüner Veltliner"
                    + " zeichnet sich durch Aromen von grünen Äpfeln, weißen Pfeffer und"
                    + " Zitrusfrüchten aus. Er hat eine lebhafte Säure und passt gut zu"
                    + " leichten Gerichten, Salaten und Gemüse.")
                .setTemperature(8.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Grüner Veltliner")
                .setCountry("AT")
                .build();
            this.addIfNotExists(grunerVeltliner);

            Wine vermentino = new Wine.WineBuilder()
                .setCategory(WineCategory.LIGHT_WHITE)
                .setName("Vermentino")
                .setDescription("Ein erfrischender Weißwein mit blassgelber Farbe. Vermentino"
                    + " zeichnet sich durch Aromen von Zitrusfrüchten, grünen Äpfeln und"
                    + " weißen Blüten aus. Er hat eine leichte Säure und passt gut zu"
                    + " Meeresfrüchten und leichten Sommergerichten.")
                .setTemperature(8.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Vermentino")
                .setCountry("IT")
                .build();
            this.addIfNotExists(vermentino);

            Wine lambrusco = new Wine.WineBuilder()
                .setCategory(WineCategory.LIGHT_RED)
                .setName("Lambrusco")
                .setDescription("Ein fruchtiger, spritziger Rotwein mit rubinroter Farbe."
                    + " Lambrusco zeichnet sich durch Aromen von dunklen Beeren, Kirschen und"
                    + " leichter Süße aus. Er hat eine lebhafte Perlage und ist erfrischend zu"
                    + " trinken. Lambrusco passt gut zu Pizza, Antipasti und pikanten Gerichten.")
                .setTemperature(11.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Lambrusco")
                .setCountry("IT")
                .build();
            this.addIfNotExists(lambrusco);

            Wine kalterersee = new Wine.WineBuilder()
                .setCategory(WineCategory.LIGHT_RED)
                .setName("Kalterersee")
                .setDescription("Ein leichter Rotwein mit rubinroter Farbe. Kalterersee"
                    + " zeichnet sich durch Aromen von roten Beeren, Kirschen und Gewürzen aus."
                    + " Er hat eine frische Säure und ist angenehm zu trinken. Kalterersee passt"
                    + " gut zu leichten Fleischgerichten, Pasta und Pizza.")
                .setTemperature(16.0)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Vernatsch")
                .setCountry("IT")
                .build();
            this.addIfNotExists(kalterersee);

            Wine amarone = new Wine.WineBuilder()
                .setCategory(WineCategory.FULL_RED)
                .setName("Amarone")
                .setDescription("Ein kräftiger Rotwein mit dunkler rubinroter Farbe. Amarone"
                    + " zeichnet sich durch Aromen von reifen Kirschen, getrockneten Früchten und"
                    + " Gewürzen aus. Er hat einen vollen Körper und eine komplexe Struktur."
                    + " Amarone passt gut zu reichhaltigen Fleischgerichten und würzigem Käse.")
                .setTemperature(12.5)
                .setVinyard("UNKNOWN")
                .setOwner(admin)
                .setGrape("Corvina, Rondinella, Molinara")
                .setCountry("IT")
                .build();
            this.addIfNotExists(amarone);
        }
    }

    /**
     * Adds the wine if a similar / equal wine doesn't already exist in the database.
     *
     * @param wine The wine to check for and insert.
     */
    private void addIfNotExists(Wine wine) {
        LOGGER.trace("addIfNotExists({})", wine);
        if (this.wineRepository.countOfFind(wine.getName(), wine.getVinyard(), null, null) == 0) {
            this.wineRepository.saveAndFlush(wine);
        }
    }
}
