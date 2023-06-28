package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.engines.WineRecommendationEngine;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.repository.AmountRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This data generator generates recipe data.
 */
@Profile("generateData")
@Component
public class RecipeDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final AmountRepository amountRepository;
    private final IngredientRepository ingredientRepository;
    private final WineRecommendationEngine wineRecommendationEngine;
    private final AuthorRepository authorRepository;
    private final ImageRepository imageRepository;

    public RecipeDataGenerator(
        UserRepository userRepository,
        RecipeRepository recipeRepository,
        AmountRepository amountRepository,
        IngredientRepository ingredientRepository,
        WineRecommendationEngine wineRecommendationEngine,
        AuthorRepository authorRepository,
        ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.amountRepository = amountRepository;
        this.ingredientRepository = ingredientRepository;
        this.wineRecommendationEngine = wineRecommendationEngine;
        this.authorRepository = authorRepository;
        this.imageRepository = imageRepository;
    }

    public void generate() {
        var admin = userRepository.findByEmail("admin@example.com");

        if (admin != null) {

            if (recipeRepository.findByName("Spiegelei").isEmpty()) {
                var eggOpt = ingredientRepository.findByName("Ei");
                if (eggOpt.isPresent()) {
                    Amount eggAmount = new Amount();
                    eggAmount.setIngredient(eggOpt.get());
                    eggAmount.setUnit(AmountUnit.PIECE);
                    eggAmount.setAmount(2.0);

                    var amountList = Collections.singletonList(eggAmount);
                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Recipe spiegelei = new Recipe();
                    if (recommendation != null) {
                        spiegelei.setRecommendationConfidence(recommendation.getConfidence());
                        spiegelei.setRecommendedCategory(recommendation.getCategory());
                    }

                    Image spiegeleiImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/spiegelei.jpg")
                        .build();

                    imageRepository.save(spiegeleiImage);

                    spiegelei.setDifficulty(Difficulty.EASY);
                    spiegelei.setName("Spiegelei");
                    spiegelei.setShortDescription(
                        "Spiegeleier heißen Spiegeleier weil sie manchmal reflektieren wie ein Spiegel");
                    spiegelei.setOwner(admin);
                    spiegelei.setDescription(
                        "Die Eier einzeln aufschlagen und auf einen Teller gleiten lassen. So kann man durch Riechen und Betrachten des Rohzustandes die Frische des Produkts feststellen.\n"
                            + "\n"
                            + "Eine beschichtete Pfanne mit mittlerer Temperatur erhitzen, erst dann die Hälfte der Butter schmelzen. "
                            + "Eine Prise Salz an 2 Stellen über die geschmolzene Butter streuen und 2 Eier, genau auf dieser Prise Salz, hineingleiten lassen. Bei nicht zu hoher "
                            + "Temperatur soweit garen, dass einerseits die Rückseite nicht braun wird und das Eiweiß stockt, das Eigelb aber innen flüssig und wachsweich bleibt. "
                            + "Für die nächsten 2 Eier genauso verfahren oder gleich 2 Pfannen benutzen.\n"
                            + "\n"
                            + "Nimmt man Wachteleier, wird für 4 Stück eine 28er Pfanne ausreichen, die Garzeit aber etwas kürzer ausfallen.\n"
                            + "Man könnte auch, noch weniger Hitze vorausgesetzt, die Eier mit Deckel garen, muss dann aber sehr genau aufpassen, dass das Eigelb nicht stockt. "
                            + "So bekommt man einen Schleier über dem Eigelb. Da das Salz unter dem Ei ist, erhält man so ein gewürztes, aber dennoch naturbelassenes Produkt ohne optischen Makel.\n"
                            + "\n"
                            + "Wie man Spiegeleier serviert, bleibt jedem selbst überlassen. Ob auf Toast, mit Speck oder Schinken, "
                            + " zu Spinat oder Bratkartoffeln - selbst mit dünnen Trüffelscheiben sind unzählige Dinge möglich. ");
                    spiegelei.setIngredients(amountList);
                    spiegelei.setImageId(spiegeleiImage.getId());
                    eggAmount.setRecipe(spiegelei);

                    var author = authorRepository.findById(1L);
                    spiegelei.setAuthor(author.get());

                    recipeRepository.saveAndFlush(spiegelei);
                    amountRepository.save(eggAmount);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Spiegelei");
                }
            }

            if (recipeRepository.findByName("Kohlrabi-Risotto mit Pinienkernen").isEmpty()) {
                var kohlrabi = ingredientRepository.findByName("Kohlrabi");
                var bruehe = ingredientRepository.findByName("Gemüsebrühe");
                var zwiebel = ingredientRepository.findByName("Zwiebeln");
                var oel = ingredientRepository.findByName("Öl");
                var reis = ingredientRepository.findByName("Reis");
                var wein = ingredientRepository.findByName("Weißwein");
                var pinien = ingredientRepository.findByName("Pinienkerne");
                var petersilie = ingredientRepository.findByName("Petersilie");
                var butter = ingredientRepository.findByName("Butter");
                var parmesan = ingredientRepository.findByName("Parmesan");
                var salz = ingredientRepository.findByName("Salz");
                var pfeffer = ingredientRepository.findByName("Pfeffer");

                if (kohlrabi.isPresent()
                    && bruehe.isPresent()
                    && zwiebel.isPresent()
                    && oel.isPresent()
                    && reis.isPresent()
                    && wein.isPresent()
                    && pinien.isPresent()
                    && petersilie.isPresent()
                    && butter.isPresent()
                    && parmesan.isPresent()
                    && salz.isPresent()
                    && pfeffer.isPresent()
                ) {
                    Amount amount1 = new Amount();
                    amount1.setIngredient(kohlrabi.get());
                    amount1.setUnit(AmountUnit.PIECE);
                    amount1.setAmount(1.0);

                    Amount amount2 = new Amount();
                    amount2.setIngredient(bruehe.get());
                    amount2.setUnit(AmountUnit.ML);
                    amount2.setAmount(750.0);

                    Amount amount3 = new Amount();
                    amount3.setIngredient(zwiebel.get());
                    amount3.setUnit(AmountUnit.PIECE);
                    amount3.setAmount(1.0);

                    Amount amount4 = new Amount();
                    amount4.setIngredient(oel.get());
                    amount4.setUnit(AmountUnit.TBSP);
                    amount4.setAmount(2.0);

                    Amount amount5 = new Amount();
                    amount5.setIngredient(reis.get());
                    amount5.setUnit(AmountUnit.G);
                    amount5.setAmount(200.0);

                    Amount amount6 = new Amount();
                    amount6.setIngredient(wein.get());
                    amount6.setUnit(AmountUnit.ML);
                    amount6.setAmount(150.0);

                    Amount amount7 = new Amount();
                    amount7.setIngredient(pinien.get());
                    amount7.setUnit(AmountUnit.G);
                    amount7.setAmount(50.0);

                    Amount amount8 = new Amount();
                    amount8.setIngredient(petersilie.get());
                    amount8.setUnit(AmountUnit.G);
                    amount8.setAmount(10.0);

                    Amount amount9 = new Amount();
                    amount9.setIngredient(butter.get());
                    amount9.setUnit(AmountUnit.TBSP);
                    amount9.setAmount(2.0);

                    Amount amount10 = new Amount();
                    amount10.setIngredient(parmesan.get());
                    amount10.setUnit(AmountUnit.G);
                    amount10.setAmount(50.0);

                    Amount amount11 = new Amount();
                    amount11.setIngredient(salz.get());
                    amount11.setUnit(AmountUnit.TSP);
                    amount11.setAmount(1.0);

                    Amount amount12 = new Amount();
                    amount12.setIngredient(pfeffer.get());
                    amount12.setUnit(AmountUnit.TSP);
                    amount12.setAmount(0.5);


                    List<Amount> amountList = new ArrayList<>();
                    amountList.add(amount1);
                    amountList.add(amount2);
                    amountList.add(amount3);
                    amountList.add(amount4);
                    amountList.add(amount5);
                    amountList.add(amount6);
                    amountList.add(amount7);
                    amountList.add(amount8);
                    amountList.add(amount9);
                    amountList.add(amount10);
                    amountList.add(amount11);
                    amountList.add(amount12);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/risotto.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe risotto = new Recipe();
                    if (recommendation != null) {
                        risotto.setRecommendationConfidence(recommendation.getConfidence());
                        risotto.setRecommendedCategory(recommendation.getCategory());
                    }
                    risotto.setDifficulty(Difficulty.MEDIUM);
                    risotto.setShortDescription("Gesundes und mediterranes Gericht");
                    risotto.setName("Kohlrabi-Risotto mit Pinienkernen");
                    risotto.setOwner(admin);
                    risotto.setImageId(img.getId());
                    risotto.setDescription(
                        "Risotto mit Kohlrabi und Weißwein, garniert mit gebratenen Pinienkernen.\n\n\n"
                            + "Kohlrabi schälen und in 1 cm große Würfel schneiden. In der Gemüsebrühe etwa 10 Minuten garen, herausnehmen und die Brühe weiterhin warm halten.\n"
                            + "\n"
                            + "Zwiebel schälen und fein würfeln. In einer etwas größeren Pfanne das Öl erhitzen und die Zwiebelwürfel und den Reis darin glasig andünsten,"
                            + "anschließend mit dem Weißwein ablöschen. Wenn der Weißwein verkocht ist, nach und nach die Brühe hinzugeben, so dass der Reis immer leicht bedeckt ist."
                            + "Bei mittlerer Hitze 20 min. garen, dabei immer wieder umrühren.\n"
                            + "\n"
                            + "In der Zwischenzeit die Pinienkerne in einer kleinen Pfanne rösten. Petersilie waschen, trocken schütteln und fein hacken. \n"
                            + "\n"
                            + "Kohlrabiwürfel unter den Reis heben. Leicht pfeffern. Butter und Parmesan unterrühren. Mit Petersilie und Pinienkernen bestreut servieren.");
                    risotto.setIngredients(amountList);
                    amount1.setRecipe(risotto);
                    amount2.setRecipe(risotto);
                    amount3.setRecipe(risotto);
                    amount4.setRecipe(risotto);
                    amount5.setRecipe(risotto);
                    amount6.setRecipe(risotto);
                    amount7.setRecipe(risotto);
                    amount8.setRecipe(risotto);
                    amount9.setRecipe(risotto);
                    amount10.setRecipe(risotto);
                    amount11.setRecipe(risotto);
                    amount12.setRecipe(risotto);

                    var author = authorRepository.findById(4L);
                    risotto.setAuthor(author.get());

                    recipeRepository.saveAndFlush(risotto);
                    amountRepository.saveAll(amountList);
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Risotto");
                }
            }

            if (recipeRepository.findByName("Mozzarella gebacken").isEmpty()) {
                var eggOpt = ingredientRepository.findByName("Ei");
                var flourOpt = ingredientRepository.findByName("Mehl");
                var oilOpt = ingredientRepository.findByName("Öl");
                var breadcrOpt = ingredientRepository.findByName("Semmelbrösel");
                var saltOpt = ingredientRepository.findByName("Salz");
                if (eggOpt.isPresent() && flourOpt.isPresent() && oilOpt.isPresent() && breadcrOpt.isPresent()
                    && saltOpt.isPresent()) {
                    Amount eggAmount = new Amount();
                    eggAmount.setIngredient(eggOpt.get());
                    eggAmount.setUnit(AmountUnit.PIECE);
                    eggAmount.setAmount(1.0);

                    Amount flourAmount = new Amount();
                    flourAmount.setIngredient(flourOpt.get());
                    flourAmount.setUnit(AmountUnit.TBSP);
                    flourAmount.setAmount(1.0);

                    Amount oilAmount = new Amount();
                    oilAmount.setIngredient(oilOpt.get());
                    oilAmount.setUnit(AmountUnit.ML);
                    oilAmount.setAmount(150.0);

                    Amount breadcrAmount = new Amount();
                    breadcrAmount.setIngredient(breadcrOpt.get());
                    breadcrAmount.setUnit(AmountUnit.G);
                    breadcrAmount.setAmount(25.0);

                    Amount saltAmount = new Amount();
                    saltAmount.setIngredient(saltOpt.get());
                    saltAmount.setUnit(AmountUnit.TSP);
                    saltAmount.setAmount(1.0);

                    var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(eggAmount));
                    amountList.add(flourAmount);
                    amountList.add(oilAmount);
                    amountList.add(breadcrAmount);
                    amountList.add(saltAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/mozzarella.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe mozarella = new Recipe();
                    if (recommendation != null) {
                        mozarella.setRecommendationConfidence(recommendation.getConfidence());
                        mozarella.setRecommendedCategory(recommendation.getCategory());
                    }
                    mozarella.setDifficulty(Difficulty.MEDIUM);
                    mozarella.setName("Mozzarella gebacken");
                    mozarella.setShortDescription("Schnell und lecker");
                    mozarella.setOwner(admin);
                    mozarella.setImageId(img.getId());
                    mozarella.setDescription(
                        "Mozzarella gebacken schmeckt herrlich zu Tomatensalat, besonders Kinder lieben dieses Rezept.\n\n\n"
                            + "1) Die Mozzarellakugeln halbieren. Mehl, Eier und Brösel getrennt in je eine tiefe Schüssel geben. Eier mit einer Gabel verquirlen. Semmelbrösel mit Salz mischen. "
                            + "Mozzarella erst im Mehl wenden, dann durch das Ei ziehen und zum Schluss in den Bröseln wälzen. Nochmals durch das Ei ziehen und in den Bröseln wälzen. \n"
                            + "2) Das Öl in einem kleinen Topf erhitzen. Panierten Mozzarella nacheinander 5 Minuten im Öl goldbraun ausbacken. Herausheben und auf Küchenpapier abtropfen lassen.");
                    mozarella.setIngredients(amountList);
                    eggAmount.setRecipe(mozarella);
                    flourAmount.setRecipe(mozarella);
                    oilAmount.setRecipe(mozarella);
                    breadcrAmount.setRecipe(mozarella);
                    saltAmount.setRecipe(mozarella);

                    var author = authorRepository.findById(2L);
                    mozarella.setAuthor(author.get());

                    recipeRepository.saveAndFlush(mozarella);
                    amountRepository.save(eggAmount);
                    amountRepository.save(flourAmount);
                    amountRepository.save(oilAmount);
                    amountRepository.save(breadcrAmount);
                    amountRepository.save(saltAmount);
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Mozzarella gebacken");
                }
            }

            if (recipeRepository.findByName("Spaghetti Carbonara").isEmpty()) {
                var spaghettiOpt = ingredientRepository.findByName("Spaghetti");
                var baconOpt = ingredientRepository.findByName("Speck");
                var oliveOilOpt = ingredientRepository.findByName("Olivenöl");
                var granaOpt = ingredientRepository.findByName("Grana Padano Käse");
                var pepperOpt = ingredientRepository.findByName("Pfeffer");
                var parmesanOpt = ingredientRepository.findByName("Parmesan");
                if (spaghettiOpt.isPresent() && baconOpt.isPresent() && oliveOilOpt.isPresent() && granaOpt.isPresent()
                    && pepperOpt.isPresent() && parmesanOpt.isPresent()) {
                    Amount spaghettiAmount = new Amount();
                    spaghettiAmount.setIngredient(spaghettiOpt.get());
                    spaghettiAmount.setUnit(AmountUnit.G);
                    spaghettiAmount.setAmount(400.0);

                    Amount baconAmount = new Amount();
                    baconAmount.setIngredient(baconOpt.get());
                    baconAmount.setUnit(AmountUnit.G);
                    baconAmount.setAmount(100.0);

                    Amount oliveOilAmount = new Amount();
                    oliveOilAmount.setIngredient(oliveOilOpt.get());
                    oliveOilAmount.setUnit(AmountUnit.ML);
                    oliveOilAmount.setAmount(100.0);

                    Amount granaAmount = new Amount();
                    granaAmount.setIngredient(granaOpt.get());
                    granaAmount.setUnit(AmountUnit.G);
                    granaAmount.setAmount(100.0);

                    Amount pepperAmount = new Amount();
                    pepperAmount.setIngredient(pepperOpt.get());
                    pepperAmount.setUnit(AmountUnit.TSP);
                    pepperAmount.setAmount(1.0);

                    Amount parmesanAmount = new Amount();
                    parmesanAmount.setIngredient(parmesanOpt.get());
                    parmesanAmount.setUnit(AmountUnit.G);
                    parmesanAmount.setAmount(50.0);

                    var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(spaghettiAmount));
                    amountList.add(baconAmount);
                    amountList.add(oliveOilAmount);
                    amountList.add(granaAmount);
                    amountList.add(pepperAmount);
                    amountList.add(parmesanAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/carbonara.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe cabonara = new Recipe();
                    if (recommendation != null) {
                        cabonara.setRecommendationConfidence(recommendation.getConfidence());
                        cabonara.setRecommendedCategory(recommendation.getCategory());
                    }
                    cabonara.setDifficulty(Difficulty.EASY);
                    cabonara.setName("Spaghetti Carbonara");
                    cabonara.setShortDescription("Ein leckerer Klassiker");
                    cabonara.setOwner(admin);
                    cabonara.setImageId(img.getId());
                    cabonara.setDescription(
                        "Das italienische Rezept Spaghetti Carbonara ist überall bekannt und beliebt. Hier werden Speck, Eier und Parmesan verwendet.\n\n\n"
                            + "1) Für die Spaghetti Carbonara die Spaghettinudeln zuerst in gut gesalzenem Wasser bissfest (al dente) kochen. Bevor man die Spaghetti abseiht,"
                            + "etwas Kochwasser entnehmen und beiseite stellen. Erst danach die Spaghetti abseihen und abtropfen lassen. \n"
                            + "2) Nun den Speck in kleine Würfel oder feine Streifen schneiden und in einer größeren Pfanne in Olivenöl leicht ausbraten. \n"
                            + "3) Danach geriebenen Käse, rund 80 ml Kochwasser und versprudelte Eier der Pfanne hinzugeben. Mit Pfeffer würzen. \n"
                            + "4) Die Spaghetti auf den Tellern portionieren und gut mit der Sauce übergießen. Nach Wunsch geriebenen Parmesan darüber streuen und servieren.");
                    cabonara.setIngredients(amountList);
                    spaghettiAmount.setRecipe(cabonara);
                    baconAmount.setRecipe(cabonara);
                    oliveOilAmount.setRecipe(cabonara);
                    granaAmount.setRecipe(cabonara);
                    pepperAmount.setRecipe(cabonara);
                    parmesanAmount.setRecipe(cabonara);

                    var author = authorRepository.findById(4L);
                    cabonara.setAuthor(author.get());

                    recipeRepository.saveAndFlush(cabonara);
                    amountRepository.save(spaghettiAmount);
                    amountRepository.save(baconAmount);
                    amountRepository.save(oliveOilAmount);
                    amountRepository.save(granaAmount);
                    amountRepository.save(pepperAmount);
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Spaghetti Cabonara");
                }
            }

            if (recipeRepository.findByName("Steak").isEmpty()) {
                var steakOpt = ingredientRepository.findByName("Rindfleisch Filet");
                var butterOpt = ingredientRepository.findByName("Butter");
                var pepperOpt = ingredientRepository.findByName("Pfeffer");
                var saltOpt = ingredientRepository.findByName("Salz");
                if (steakOpt.isPresent() && butterOpt.isPresent() && pepperOpt.isPresent() && saltOpt.isPresent()) {
                    Amount steakAmount = new Amount();
                    steakAmount.setIngredient(steakOpt.get());
                    steakAmount.setUnit(AmountUnit.G);
                    steakAmount.setAmount(250.0);

                    Amount butterAmount = new Amount();
                    butterAmount.setIngredient(butterOpt.get());
                    butterAmount.setUnit(AmountUnit.G);
                    butterAmount.setAmount(25.0);

                    Amount pepperAmount = new Amount();
                    pepperAmount.setIngredient(pepperOpt.get());
                    pepperAmount.setUnit(AmountUnit.G);
                    pepperAmount.setAmount(1.0);

                    Amount saltAmount = new Amount();
                    saltAmount.setIngredient(saltOpt.get());
                    saltAmount.setUnit(AmountUnit.G);
                    saltAmount.setAmount(1.0);

                    var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(steakAmount));
                    amountList.add(butterAmount);
                    amountList.add(pepperAmount);
                    amountList.add(saltAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image steakImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/steak.jpg")
                        .build();

                    imageRepository.save(steakImage);

                    Recipe steak = new Recipe();
                    if (recommendation != null) {
                        steak.setRecommendationConfidence(recommendation.getConfidence());
                        steak.setRecommendedCategory(recommendation.getCategory());
                    }
                    steak.setDifficulty(Difficulty.EASY);
                    steak.setName("Steak");
                    steak.setOwner(admin);
                    steak.setImageId(steakImage.getId());
                    steak.setShortDescription("Ein saftiges Rindersteak");

                    steak.setDescription(
                        "Ein Rindersteak richtig zu braten, ist gar nicht mal so schwer. Wenn man einige Dinge beachtet, wird das Steak zum richtigen Gaumenschmaus.\n\n\n"
                            + "1) Für das perfekt gebratene Rindersteak nimmt man das Fleisch aus dem Kühlschrank und lässt es auf Zimmertemperatur erwärmen. \n"
                            + "2) Wenn man ein größeres Stück Fleisch hat, sollte man daraus 2 - 3 cm dicke Scheiben quer zur Faser schnei"
                            + "den oder es gleich vom Fleischer schneiden lassen. \n"
                            + "3) Jetzt die Butter oder Öl in eine Pfanne geben, heiß werden lassen und das Fleisch in die Pfanne geben. Wichtig ist d"
                            + "abei, dass das Fett in der Pfanne richtig heiß ist, bevor man das Fleisch hinein gibt. \n"
                            + "4) Die Steaks von jeder Seite je nach Gargrad (siehe Tipp unten) für ca. 2 - 3 Minuten scharf anbraten. "
                            + "Für das Wenden das Fleisch aber nicht mit einer Gabel anstechen, sondern am besten mit einem Bratenwender wenden.\n"
                            + "5) Danach das fertig gebratene Rindersteak mit Salz und Pfeffer würzen und servieren.");

                    steak.setIngredients(amountList);
                    steak.setImageId(steakImage.getId());
                    steakAmount.setRecipe(steak);
                    butterAmount.setRecipe(steak);
                    pepperAmount.setRecipe(steak);
                    saltAmount.setRecipe(steak);

                    var author = authorRepository.findById(1L);
                    steak.setAuthor(author.get());

                    recipeRepository.saveAndFlush(steak);
                    amountRepository.save(steakAmount);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Steak");
                }
            }

            if (recipeRepository.findByName("Knuspriges Zitronenhähnchen").isEmpty()) {
                var hahnchenbrustfilets = ingredientRepository.findByName("Hähnchenbrustfilet");
                var zitrone = ingredientRepository.findByName("Zitrone");
                var oliveOilOpt = ingredientRepository.findByName("Olivenöl");
                var knoblauchzehe = ingredientRepository.findByName("Knoblauchzehe");
                var paprika = ingredientRepository.findByName("Paprika");
                var kartoffeln = ingredientRepository.findByName("Kartoffeln");
                var rosmarin = ingredientRepository.findByName("Rosmarin");
                var salat = ingredientRepository.findByName("Gemischter grüner Salat");

                if (hahnchenbrustfilets.isPresent()
                    && zitrone.isPresent()
                    && oliveOilOpt.isPresent()
                    && knoblauchzehe.isPresent()
                    && paprika.isPresent()
                    && kartoffeln.isPresent()
                    && rosmarin.isPresent()
                    && salat.isPresent()) {

                    Amount amount = null;

                    amount = new Amount();
                    amount.setIngredient(hahnchenbrustfilets.get());
                    amount.setUnit(AmountUnit.PIECE);
                    amount.setAmount(4.0);
                    final var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(amount));

                    amount = new Amount();
                    amount.setIngredient(zitrone.get());
                    amount.setUnit(AmountUnit.PIECE);
                    amount.setAmount(2.0);
                    amountList.add(amount);

                    amount = new Amount();
                    amount.setIngredient(oliveOilOpt.get());
                    amount.setUnit(AmountUnit.TBSP);
                    amount.setAmount(3.0);
                    amountList.add(amount);

                    amount = new Amount();
                    amount.setIngredient(knoblauchzehe.get());
                    amount.setUnit(AmountUnit.PIECE);
                    amount.setAmount(2.0);
                    amountList.add(amount);

                    amount = new Amount();
                    amount.setIngredient(paprika.get());
                    amount.setUnit(AmountUnit.TSP);
                    amount.setAmount(1.0);
                    amountList.add(amount);

                    amount = new Amount();
                    amount.setIngredient(kartoffeln.get());
                    amount.setUnit(AmountUnit.PIECE);
                    amount.setAmount(4.0);
                    amountList.add(amount);

                    amount = new Amount();
                    amount.setIngredient(rosmarin.get());
                    amount.setUnit(AmountUnit.TSP);
                    amount.setAmount(2.0);
                    amountList.add(amount);

                    amount = new Amount();
                    amount.setIngredient(salat.get());
                    amount.setUnit(AmountUnit.G);
                    amount.setAmount(50.0);
                    amountList.add(amount);

                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/chicken.jpg")
                        .build();
                    imageRepository.save(img);

                    Recipe recipe = new Recipe();

                    recipe.setDifficulty(Difficulty.EASY);
                    recipe.setName("Knuspriges Zitronenhähnchen");
                    recipe.setShortDescription("Ein leckerer Klassiker");
                    recipe.setOwner(admin);
                    recipe.setImageId(img.getId());
                    recipe.setDescription(
                        "Heizen Sie den Ofen auf 200°C (Umluft) vor. In einer Schüssel den Zitronensaft, die Zitronenzeste, Olivenöl"
                            + ", gehackten Knoblauch, Paprika, Salz und Pfeffer vermischen. Die Hähnchenbrustfilets in die Marinade gebe"
                            + "n und gründlich damit bedecken. Mindestens 30 Minuten im Kühlschrank marinieren lassen."
                            + "Während das Hähnchen mariniert, die Kartoffelwürfel mit Olivenöl, frischem Rosmarin, Salz und Pfeffer in ein"
                            + "er Schüssel vermengen, bis sie gut bedeckt sind. Die Kartoffeln auf einem mit Backpapier ausgelegten Back"
                            + "blech verteilen und gleichmäßig im vorgeheizten Ofen ca. 25-30 Minuten backen, oder bis sie goldbraun und knusprig sind."
                            + "In einer heißen Pfanne die marinierten Hähnchenbrustfilets von beiden Seiten anbraten, bis sie goldbraun sin"
                            + "d. Die Pfanne anschließend in den vorgeheizten Ofen stellen und die Hähnchenbrus"
                            + "tfilets weitere 10-15 Minuten backen, oder bis sie durchgegart sind."
                            + "Während das Hähnchen im Ofen ist, den gemischten grünen Salat waschen und vorbereiten."
                            + "Das knusprige Zitronenhähnchen zusammen mit den Rosmarinkartoffeln und dem gemischten grünen Salat servieren.");

                    for (Amount a : amountList) {
                        a.setRecipe(recipe);
                    }
                    recipe.setIngredients(amountList);

                    var author = authorRepository.findById(4L);
                    recipe.setAuthor(author.get());


                    recipeRepository.saveAndFlush(recipe);
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Knuspriges Zitronenhähnchen mit Rosmarinkartoffeln");
                }
            }

            if (recipeRepository.findByName("Lasagne").isEmpty()) {
                var mincedMeatOpt = ingredientRepository.findByName("Faschiertes");
                var sausagesOpt = ingredientRepository.findByName("Salsiccia");
                var tomatoSauceOpt = ingredientRepository.findByName("Tomaten");
                var waterOpt = ingredientRepository.findByName("Wasser");
                var redWineOpt = ingredientRepository.findByName("Rotwein");
                var carrotsOpt = ingredientRepository.findByName("Karotten");
                var onionsOpt = ingredientRepository.findByName("Zwiebeln");
                var celeryOpt = ingredientRepository.findByName("Sellerie");
                var oliveOilOpt = ingredientRepository.findByName("Olivenöl");
                var saltOpt = ingredientRepository.findByName("Salz");
                var milkOpt = ingredientRepository.findByName("Milch");
                var flourOpt = ingredientRepository.findByName("Mehl");
                var butterOpt = ingredientRepository.findByName("Butter");
                var nutmegOpt = ingredientRepository.findByName("Muskatnuss");
                var parmesanOpt = ingredientRepository.findByName("Parmesan");
                var lasagnaSheetsOpt = ingredientRepository.findByName("Lasagneblätter");

                if (mincedMeatOpt.isPresent()
                    && sausagesOpt.isPresent()
                    && tomatoSauceOpt.isPresent()
                    && waterOpt.isPresent()
                    && redWineOpt.isPresent()
                    && carrotsOpt.isPresent()
                    && onionsOpt.isPresent()
                    && celeryOpt.isPresent()
                    && oliveOilOpt.isPresent()
                    && saltOpt.isPresent()
                    && milkOpt.isPresent()
                    && flourOpt.isPresent()
                    && butterOpt.isPresent()
                    && nutmegOpt.isPresent()
                    && parmesanOpt.isPresent()
                    && lasagnaSheetsOpt.isPresent()) {
                    LOGGER.trace("inserting Lasagne");

                    Amount mincedMeatAmount = new Amount();
                    mincedMeatAmount.setIngredient(mincedMeatOpt.get());
                    mincedMeatAmount.setUnit(AmountUnit.G);
                    mincedMeatAmount.setAmount(500.0);

                    Amount sausagesAmount = new Amount();
                    sausagesAmount.setIngredient(sausagesOpt.get());
                    sausagesAmount.setUnit(AmountUnit.G);
                    sausagesAmount.setAmount(200.0);

                    Amount tomatoSauceAmount = new Amount();
                    tomatoSauceAmount.setIngredient(tomatoSauceOpt.get());
                    tomatoSauceAmount.setUnit(AmountUnit.G);
                    tomatoSauceAmount.setAmount(400.0);

                    Amount waterAmount = new Amount();
                    waterAmount.setIngredient(waterOpt.get());
                    waterAmount.setUnit(AmountUnit.ML);
                    waterAmount.setAmount(100.0);

                    Amount redWineAmount = new Amount();
                    redWineAmount.setIngredient(redWineOpt.get());
                    redWineAmount.setUnit(AmountUnit.ML);
                    redWineAmount.setAmount(500.0);

                    Amount carrotsAmount = new Amount();
                    carrotsAmount.setIngredient(carrotsOpt.get());
                    carrotsAmount.setUnit(AmountUnit.PIECE);
                    carrotsAmount.setAmount(3.0);

                    Amount onionsAmount = new Amount();
                    onionsAmount.setIngredient(onionsOpt.get());
                    onionsAmount.setUnit(AmountUnit.PIECE);
                    onionsAmount.setAmount(2.0);

                    Amount celeryAmount = new Amount();
                    celeryAmount.setIngredient(celeryOpt.get());
                    celeryAmount.setUnit(AmountUnit.PIECE);
                    celeryAmount.setAmount(1.0);

                    Amount oliveOilAmount = new Amount();
                    oliveOilAmount.setIngredient(oliveOilOpt.get());
                    oliveOilAmount.setUnit(AmountUnit.TBSP);
                    oliveOilAmount.setAmount(3.0);

                    Amount saltAmount = new Amount();
                    saltAmount.setIngredient(saltOpt.get());
                    saltAmount.setUnit(AmountUnit.G);
                    saltAmount.setAmount(5.0);

                    Amount milkAmount = new Amount();
                    milkAmount.setIngredient(milkOpt.get());
                    milkAmount.setUnit(AmountUnit.ML);
                    milkAmount.setAmount(1000.0);

                    Amount flourAmount = new Amount();
                    flourAmount.setIngredient(flourOpt.get());
                    flourAmount.setUnit(AmountUnit.G);
                    flourAmount.setAmount(70.0);

                    Amount butterAmount = new Amount();
                    butterAmount.setIngredient(butterOpt.get());
                    butterAmount.setUnit(AmountUnit.G);
                    butterAmount.setAmount(70.0);

                    Amount nutmegAmount = new Amount();
                    nutmegAmount.setIngredient(nutmegOpt.get());
                    nutmegAmount.setUnit(AmountUnit.G);
                    nutmegAmount.setAmount(1.0);

                    Amount parmesanAmount = new Amount();
                    parmesanAmount.setIngredient(parmesanOpt.get());
                    parmesanAmount.setUnit(AmountUnit.G);
                    parmesanAmount.setAmount(25.0);

                    Amount lasagnaSheetsAmount = new Amount();
                    lasagnaSheetsAmount.setIngredient(lasagnaSheetsOpt.get());
                    lasagnaSheetsAmount.setUnit(AmountUnit.PIECE);
                    lasagnaSheetsAmount.setAmount(10.0);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(mincedMeatAmount);
                    amountList.add(sausagesAmount);
                    amountList.add(tomatoSauceAmount);
                    amountList.add(waterAmount);
                    amountList.add(redWineAmount);
                    amountList.add(carrotsAmount);
                    amountList.add(onionsAmount);
                    amountList.add(celeryAmount);
                    amountList.add(oliveOilAmount);
                    amountList.add(saltAmount);
                    amountList.add(milkAmount);
                    amountList.add(flourAmount);
                    amountList.add(butterAmount);
                    amountList.add(nutmegAmount);
                    amountList.add(parmesanAmount);
                    amountList.add(lasagnaSheetsAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    // Assuming the image for Lasagne is available in the image repository
                    Image lasagneImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/lasagne.jpg")
                        .build();

                    imageRepository.save(lasagneImage);

                    Recipe lasagne = new Recipe();
                    if (recommendation != null) {
                        lasagne.setRecommendationConfidence(recommendation.getConfidence());
                        lasagne.setRecommendedCategory(recommendation.getCategory());
                    }

                    lasagne.setDifficulty(Difficulty.MEDIUM);
                    lasagne.setName("Lasagne");
                    lasagne.setOwner(admin);
                    lasagne.setShortDescription("Eine köstliche Lasagne nach Massimo Spigaroli");
                    lasagne.setDescription(
                        "Lasagne ist ein klassisches italienisches Gericht, das aus geschichteten Nudeln, Bolognese-Sauce und Bechamelsauce besteht. Hier ist das Rezept für die Lasagne von Massimo Spigaroli.\n\n"
                            + "1. Bolognese:\n"
                            + "   - Karotten, Sellerie und Zwiebeln fein schneiden.\n"
                            + "   - Salsiccia schälen und klein schneiden / zerteilen.\n"
                            + "   - Gemüse anbraten, bis es leicht angeröstet ist.\n"
                            + "   - Fleisch hinzufügen und anbraten, bis das Wasser verdampft ist.\n"
                            + "   - Rotwein hinzufügen und verdampfen lassen.\n"
                            + "   - Tomatensauce hinzufügen und etwas köcheln lassen.\n"
                            + "   - Wasser hinzufügen.\n"
                            + "   - Salz hinzufügen.\n"
                            + "   - 3-4 Stunden köcheln lassen.\n"
                            + "2. Bechamel:\n"
                            + "   - Butter schmelzen.\n"
                            + "   - Mehl hinzufügen und kurz kochen (sog. Roux).\n"
                            + "   - Milch aufkochen lassen.\n"
                            + "   - Milch zum Roux hinzufügen.\n"
                            + "   - Salz und Muskatnuss hinzufügen.\n"
                            + "   - Kochen lassen - sonst wird es mehlig!\n"
                            + "3. Lasagne schichten:\n"
                            + "   - Eine Form mit geschmolzener Butter einfetten.\n"
                            + "   - Nudeln in die Form geben.\n"
                            + "   - Bechamel darauf verteilen.\n"
                            + "   - Dünne Schicht Bolognese darauf verteilen.\n"
                            + "   - Parmesan darüber streuen.\n"
                            + "   - Vorgang wiederholen.\n"
                            + "   - Oberste Schicht:\n"
                            + "     - Bechamel.\n"
                            + "     - Ein wenig Bolognese.\n"
                            + "     - Viel Parmesan.\n"
                            + "   - Ca. 40 Minuten im Ofen backen.\n"
                            + "   - 5 Minuten ruhen lassen.\n");

                    lasagne.setIngredients(amountList);
                    lasagne.setImageId(lasagneImage.getId());

                    mincedMeatAmount.setRecipe(lasagne);
                    sausagesAmount.setRecipe(lasagne);
                    tomatoSauceAmount.setRecipe(lasagne);
                    waterAmount.setRecipe(lasagne);
                    redWineAmount.setRecipe(lasagne);
                    carrotsAmount.setRecipe(lasagne);
                    onionsAmount.setRecipe(lasagne);
                    celeryAmount.setRecipe(lasagne);
                    oliveOilAmount.setRecipe(lasagne);
                    saltAmount.setRecipe(lasagne);
                    milkAmount.setRecipe(lasagne);
                    flourAmount.setRecipe(lasagne);
                    butterAmount.setRecipe(lasagne);
                    nutmegAmount.setRecipe(lasagne);
                    parmesanAmount.setRecipe(lasagne);
                    lasagnaSheetsAmount.setRecipe(lasagne);

                    var author = authorRepository.findById(4L);
                    lasagne.setAuthor(author.get());

                    recipeRepository.saveAndFlush(lasagne);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Lasagne");
                }
            }

            if (recipeRepository.findByName("Süßkartoffel-Spinat-Salat").isEmpty()) {
                var ingredient1 = ingredientRepository.findByName("Süßkartoffeln");
                var ingredient2 = ingredientRepository.findByName("Spinat");
                var ingredient3 = ingredientRepository.findByName("rote Zwiebel");
                var ingredient4 = ingredientRepository.findByName("Knoblauchzehe");
                var ingredient5 = ingredientRepository.findByName("Feta");
                var ingredient6 = ingredientRepository.findByName("Walnüsse");
                var ingredient7 = ingredientRepository.findByName("Olivenöl");
                var ingredient8 = ingredientRepository.findByName("Balsamico-Essig");

                if (ingredient1.isPresent() && ingredient2.isPresent() && ingredient3.isPresent()
                    && ingredient4.isPresent() && ingredient5.isPresent() && ingredient6.isPresent()
                    && ingredient7.isPresent() && ingredient8.isPresent()) {

                    Amount amount1 = new Amount();
                    amount1.setIngredient(ingredient1.get());
                    amount1.setUnit(AmountUnit.PIECE);
                    amount1.setAmount(2.0);

                    Amount amount2 = new Amount();
                    amount2.setIngredient(ingredient2.get());
                    amount2.setUnit(AmountUnit.G);
                    amount2.setAmount(200.0);

                    Amount amount3 = new Amount();
                    amount3.setIngredient(ingredient3.get());
                    amount3.setUnit(AmountUnit.PIECE);
                    amount3.setAmount(1.0);

                    Amount amount4 = new Amount();
                    amount4.setIngredient(ingredient4.get());
                    amount4.setUnit(AmountUnit.PIECE);
                    amount4.setAmount(1.0);

                    Amount amount5 = new Amount();
                    amount5.setIngredient(ingredient5.get());
                    amount5.setUnit(AmountUnit.G);
                    amount5.setAmount(100.0);

                    Amount amount6 = new Amount();
                    amount6.setIngredient(ingredient6.get());
                    amount6.setUnit(AmountUnit.G);
                    amount6.setAmount(50.0);

                    Amount amount7 = new Amount();
                    amount7.setIngredient(ingredient7.get());
                    amount7.setUnit(AmountUnit.TBSP);
                    amount7.setAmount(2.0);

                    Amount amount8 = new Amount();
                    amount8.setIngredient(ingredient8.get());
                    amount8.setUnit(AmountUnit.TBSP);
                    amount8.setAmount(1.0);

                    var amountList = new LinkedList<Amount>();
                    amountList.push(amount1);
                    amountList.push(amount2);
                    amountList.push(amount3);
                    amountList.push(amount4);
                    amountList.push(amount5);
                    amountList.push(amount6);
                    amountList.push(amount7);
                    amountList.push(amount8);
                    var recommendation = wineRecommendationEngine.generateRecommendation(new LinkedList<Amount>());

                    Recipe spiegelei = new Recipe();
                    if (recommendation != null) {
                        spiegelei.setRecommendationConfidence(recommendation.getConfidence());
                        spiegelei.setRecommendedCategory(recommendation.getCategory());
                    }

                    // Assuming the image for Lasagne is available in the image repository
                    Image spinatImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/spagel.jpg")
                        .build();

                    imageRepository.save(spinatImage);

                    spiegelei.setDifficulty(Difficulty.MEDIUM);
                    spiegelei.setName("Süßkartoffel-Spinat-Salat");
                    spiegelei.setShortDescription(
                        "Der Süßkartoffel-Spinat-Salat ist ein gesundes und köstliches Gericht, das mit frischen Zutaten zubereitet wird und voller Geschmack steckt.");
                    spiegelei.setOwner(admin);
                    spiegelei.setDescription(
                        "Die Süßkartoffeln schälen und in kleine Würfel schneiden. Die Zwiebel fein hacken und den Knoblauch pressen."
                            + "Eine Pfanne erhitzen und das Olivenöl hinzufügen. Die Süßkartoffelwürfel darin goldbraun braten, bis sie weich sind."
                            + "Den frischen Spinat gründlich waschen und abtropfen lassen. In eine große Salatschüssel geben."
                            + "Die gebratenen Süßkartoffeln zu dem Spinat in die Schüssel geben."
                            + "Den Feta-Käse in kleine Würfel schneiden und zusammen mit den Walnüssen zum Salat hinzufügen."
                            + "In einer kleinen Schüssel das Olivenöl, den Balsamico-Essig, den gehackten Knoblauch, Salz und Pfeffer vermischen. "
                            + "Die Dressing-Mischung über den Salat gießen und gut vermengen, bis alle Zutaten gleichmäßig bedeckt sind."
                            + "Den Süßkartoffel-Spinat-Salat für ca. 10 Minuten ziehen lassen, damit sich die Aromen verbinden."
                            + "Vor dem Servieren den Salat nochmals abschmecken und gegebenenfalls mit Salz und Pfeffer nachwürzen."
                            + "Genieße den Süßkartoffel-Spinat-Salat als leichte Hauptmahlzeit oder als Beilage zu gegrilltem Fleisch oder Fisch.");
                    spiegelei.setIngredients(amountList);
                    spiegelei.setImageId(spinatImage.getId());

                    amount1.setRecipe(spiegelei);
                    amount2.setRecipe(spiegelei);
                    amount3.setRecipe(spiegelei);
                    amount4.setRecipe(spiegelei);
                    amount5.setRecipe(spiegelei);
                    amount6.setRecipe(spiegelei);
                    amount7.setRecipe(spiegelei);
                    amount8.setRecipe(spiegelei);

                    var author = authorRepository.findById(3L);
                    spiegelei.setAuthor(author.get());

                    recipeRepository.saveAndFlush(spiegelei);
                    amountRepository.save(amount1);
                    amountRepository.save(amount2);
                    amountRepository.save(amount3);
                    amountRepository.save(amount4);
                    amountRepository.save(amount5);
                    amountRepository.save(amount6);
                    amountRepository.save(amount7);
                    amountRepository.save(amount8);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: SüßFkartoffel-Spinat-Salat");
                }
            }

            if (recipeRepository.findByName("Palatschinken").isEmpty()) {
                var flour = ingredientRepository.findByName("Mehl");
                var milk = ingredientRepository.findByName("Milch");
                var egg = ingredientRepository.findByName("Ei");
                var salt = ingredientRepository.findByName("Salz");
                var oil = ingredientRepository.findByName("Öl");
                var jam = ingredientRepository.findByName("Marmelade");

                if (flour.isPresent()
                    && milk.isPresent()
                    && egg.isPresent()
                    && salt.isPresent()
                    && jam.isPresent()
                    && oil.isPresent()) {
                    LOGGER.trace("inserting Palatschinken");

                    Amount flourAmount = new Amount();
                    flourAmount.setIngredient(flour.get());
                    flourAmount.setUnit(AmountUnit.G);
                    flourAmount.setAmount(100.0);

                    Amount milkAmount = new Amount();
                    milkAmount.setIngredient(milk.get());
                    milkAmount.setUnit(AmountUnit.ML);
                    milkAmount.setAmount(230.0);

                    Amount eggAmount = new Amount();
                    eggAmount.setIngredient(egg.get());
                    eggAmount.setUnit(AmountUnit.PIECE);
                    eggAmount.setAmount(2.0);

                    Amount saltAmount = new Amount();
                    saltAmount.setIngredient(salt.get());
                    saltAmount.setUnit(AmountUnit.G);
                    saltAmount.setAmount(0.5);

                    Amount oilAmount = new Amount();
                    oilAmount.setIngredient(oil.get());
                    oilAmount.setUnit(AmountUnit.ML);
                    oilAmount.setAmount(20.0);

                    Amount jamAmount = new Amount();
                    jamAmount.setIngredient(jam.get());
                    jamAmount.setUnit(AmountUnit.G);
                    jamAmount.setAmount(50.0);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(flourAmount);
                    amountList.add(eggAmount);
                    amountList.add(saltAmount);
                    amountList.add(milkAmount);
                    amountList.add(flourAmount);
                    amountList.add(jamAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    // Assuming the image for Palatschinken is available in the image repository
                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/palatschinken.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe palatschinken = new Recipe();
                    if (recommendation != null) {
                        palatschinken.setRecommendationConfidence(recommendation.getConfidence());
                        palatschinken.setRecommendedCategory(recommendation.getCategory());
                    }

                    palatschinken.setDifficulty(Difficulty.EASY);
                    palatschinken.setName("Palatschinken");
                    palatschinken.setOwner(admin);
                    palatschinken.setShortDescription("Eine süße Mehlspeise, die einfach und schnell gemacht ist.");
                    palatschinken.setDescription(
                        "1. Alle Zutaten (bis auf das Öl) in einer Schüssel klumpenfrei vermengen, und den Teig mindestens eine halbe Stunde ruhen lassen.\n"
                            + "2. Eine Pfanne erhitzen und den Boden mit Öl bedecken."
                            + " Etwas Teig mit einem Schöpfer in die Pfanne geben und schwenken, sodass eine dünne Schicht Teig den Pfannenboden bedeckt.\n"
                            + "3. Wenn der Boden der Palatschinke braun wird, wenden und nach Braunwerden der anderen Seite auf einem Teller ablegen.\n"
                            + "4. Schritte 2 und 3 wiederholen bis der Teig leer ist.\n"
                            + "5. Die Palatschinken mit Marmelade, oder anderen Füllungen nach Wahl, bestreichen, zusammenrollen und servieren.");
                    palatschinken.setIngredients(amountList);
                    palatschinken.setImageId(img.getId());

                    jamAmount.setRecipe(palatschinken);
                    oilAmount.setRecipe(palatschinken);
                    eggAmount.setRecipe(palatschinken);
                    saltAmount.setRecipe(palatschinken);
                    milkAmount.setRecipe(palatschinken);
                    flourAmount.setRecipe(palatschinken);

                    var author = authorRepository.findById(4L);
                    palatschinken.setAuthor(author.get());

                    recipeRepository.saveAndFlush(palatschinken);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Palatschinken");
                }
            }

            if (recipeRepository.findByName("Rote Linsen Dal").isEmpty()) {

                var redLentils = ingredientRepository.findByName("Rote Linsen");
                var onion = ingredientRepository.findByName("Zwiebeln");
                var garlic = ingredientRepository.findByName("Knoblauchzehe");
                var ginger = ingredientRepository.findByName("Ingwer");
                var coconutMilk = ingredientRepository.findByName("Kokosmilch");
                var tomatoes = ingredientRepository.findByName("Tomaten");
                var curryPaste = ingredientRepository.findByName("Currypaste");
                var cilantro = ingredientRepository.findByName("Koriander");
                var vegetableSoup = ingredientRepository.findByName("Gemüsebrühe");
                var salt = ingredientRepository.findByName("Salz");
                var oil = ingredientRepository.findByName("Öl");
                var cumin = ingredientRepository.findByName("Kümmel");

                if (redLentils.isPresent()
                    && onion.isPresent()
                    && garlic.isPresent()
                    && ginger.isPresent()
                    && coconutMilk.isPresent()
                    && tomatoes.isPresent()
                    && curryPaste.isPresent()
                    && cilantro.isPresent()
                    && vegetableSoup.isPresent()
                    && salt.isPresent()
                    && oil.isPresent()
                    && cumin.isPresent()) {
                    LOGGER.trace("inserting Rote Linsen Dal");

                    Amount redLentilAmount = new Amount();
                    redLentilAmount.setIngredient(redLentils.get());
                    redLentilAmount.setUnit(AmountUnit.G);
                    redLentilAmount.setAmount(250.0);

                    Amount onionAmount = new Amount();
                    onionAmount.setIngredient(onion.get());
                    onionAmount.setUnit(AmountUnit.PIECE);
                    onionAmount.setAmount(2.0);

                    Amount garlicAmount = new Amount();
                    garlicAmount.setIngredient(garlic.get());
                    garlicAmount.setUnit(AmountUnit.PIECE);
                    garlicAmount.setAmount(2.0);

                    Amount gingerAmount = new Amount();
                    gingerAmount.setIngredient(ginger.get());
                    gingerAmount.setUnit(AmountUnit.G);
                    gingerAmount.setAmount(20.0);

                    Amount coconutMilkAmount = new Amount();
                    coconutMilkAmount.setIngredient(coconutMilk.get());
                    coconutMilkAmount.setUnit(AmountUnit.ML);
                    coconutMilkAmount.setAmount(400.0);

                    Amount tomatoAmount = new Amount();
                    tomatoAmount.setIngredient(tomatoes.get());
                    tomatoAmount.setUnit(AmountUnit.G);
                    tomatoAmount.setAmount(350.0);

                    Amount curryPasteAmount = new Amount();
                    curryPasteAmount.setIngredient(curryPaste.get());
                    curryPasteAmount.setUnit(AmountUnit.TBSP);
                    curryPasteAmount.setAmount(1.0);

                    Amount cilantroAmount = new Amount();
                    cilantroAmount.setIngredient(cilantro.get());
                    cilantroAmount.setUnit(AmountUnit.G);
                    cilantroAmount.setAmount(20.0);

                    Amount vegetableSoupAmount = new Amount();
                    vegetableSoupAmount.setIngredient(vegetableSoup.get());
                    vegetableSoupAmount.setUnit(AmountUnit.ML);
                    vegetableSoupAmount.setAmount(500.0);

                    Amount saltAmount = new Amount();
                    saltAmount.setIngredient(salt.get());
                    saltAmount.setUnit(AmountUnit.G);
                    saltAmount.setAmount(0.5);

                    Amount oilAmount = new Amount();
                    oilAmount.setIngredient(oil.get());
                    oilAmount.setUnit(AmountUnit.ML);
                    oilAmount.setAmount(20.0);

                    Amount cuminAmount = new Amount();
                    cuminAmount.setIngredient(cumin.get());
                    cuminAmount.setUnit(AmountUnit.G);
                    cuminAmount.setAmount(0.2);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(redLentilAmount);
                    amountList.add(onionAmount);
                    amountList.add(garlicAmount);
                    amountList.add(gingerAmount);
                    amountList.add(coconutMilkAmount);
                    amountList.add(tomatoAmount);
                    amountList.add(curryPasteAmount);
                    amountList.add(cilantroAmount);
                    amountList.add(vegetableSoupAmount);
                    amountList.add(saltAmount);
                    amountList.add(oilAmount);
                    amountList.add(cuminAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    // Assuming the image for Daal is available in the image repository
                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/daal.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe dal = new Recipe();
                    if (recommendation != null) {
                        dal.setRecommendationConfidence(recommendation.getConfidence());
                        dal.setRecommendedCategory(recommendation.getCategory());
                    }
                    dal.setDifficulty(Difficulty.EASY);
                    dal.setName("Rote Linsen Dal");
                    dal.setOwner(admin);
                    dal.setShortDescription("Schnelle und einfache Mahlzeit, die immer schmeckt.");
                    dal.setDescription(
                        "1) Zwiebel, Knoblauch und Ingwer klein schneiden. In einem Topf das Öl erhitzen. \n "
                            + "2) Zwiebel im Öl anbraten, anschließend den Ingwer, Knoblauch und die Currypaste hinzufügen. Unter ständigem Umrühren weiter anbraten. \n "
                            + "3) Linsen in den Topf geben und alles mit der Gemüsebrühe ablöschen. Für 10-15 min köcheln lassen bis die Linsen weich werden.\n"
                            + "4) Tomaten und Kokosmilch hinzugeben. 5 min köcheln lassen und währendessen den Koriander klein schneiden. \n"
                            + "5) Koriander, Kümmel und Salz hinzufügen. Je nach Bedarf kann das Dal etwas länger gekocht werden um eine gute cremige Konsistenz zu erhalten. \n"
                            + "\n Statt den Koriander kann auch Petersilie verwendet werden. Das Rezept lässt sich auch gut mit Linsen und Tomaten aus der Dose zubereiten. "
                            + "In diesem Fall muss keine Gemüsebrühe mehr eingekocht werden, da die Linsen bereits gekocht sind.");
                    dal.setIngredients(amountList);
                    dal.setImageId(img.getId());

                    redLentilAmount.setRecipe(dal);
                    onionAmount.setRecipe(dal);
                    garlicAmount.setRecipe(dal);
                    gingerAmount.setRecipe(dal);
                    coconutMilkAmount.setRecipe(dal);
                    tomatoAmount.setRecipe(dal);
                    curryPasteAmount.setRecipe(dal);
                    cilantroAmount.setRecipe(dal);
                    vegetableSoupAmount.setRecipe(dal);
                    saltAmount.setRecipe(dal);
                    oilAmount.setRecipe(dal);
                    cuminAmount.setRecipe(dal);

                    var author = authorRepository.findById(2L);
                    dal.setAuthor(author.get());

                    recipeRepository.saveAndFlush(dal);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Rote Linsen Dal");
                }
            }

            if (recipeRepository.findByName("Spaghetti Cacio e Pepe").isEmpty()) {
                var spaghettiOpt = ingredientRepository.findByName("Spaghetti");
                var saltOpt = ingredientRepository.findByName("Salz");
                var pepperOpt = ingredientRepository.findByName("Pfeffer");
                var peccorinoOpt = ingredientRepository.findByName("Peccorino");
                if (spaghettiOpt.isPresent() && saltOpt.isPresent()
                    && pepperOpt.isPresent() && peccorinoOpt.isPresent()) {
                    Amount spaghettiAmount = new Amount();
                    spaghettiAmount.setIngredient(spaghettiOpt.get());
                    spaghettiAmount.setUnit(AmountUnit.G);
                    spaghettiAmount.setAmount(400.0);

                    Amount saltAmount = new Amount();
                    saltAmount.setIngredient(saltOpt.get());
                    saltAmount.setUnit(AmountUnit.G);
                    saltAmount.setAmount(1.0);

                    Amount pepperAmount = new Amount();
                    pepperAmount.setIngredient(pepperOpt.get());
                    pepperAmount.setUnit(AmountUnit.TBSP);
                    pepperAmount.setAmount(1.0);

                    Amount parmesanAmount = new Amount();
                    parmesanAmount.setIngredient(peccorinoOpt.get());
                    parmesanAmount.setUnit(AmountUnit.G);
                    parmesanAmount.setAmount(200.0);

                    var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(spaghettiAmount));
                    amountList.add(saltAmount);
                    amountList.add(pepperAmount);
                    amountList.add(parmesanAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/cacioepepe.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe cacioepepe = new Recipe();
                    if (recommendation != null) {
                        cacioepepe.setRecommendationConfidence(recommendation.getConfidence());
                        cacioepepe.setRecommendedCategory(recommendation.getCategory());
                    }
                    cacioepepe.setDifficulty(Difficulty.EASY);
                    cacioepepe.setName("Spaghetti Cacio e Pepe");
                    cacioepepe.setShortDescription(
                        "Ein italienischer Klassiker, welcher mit nur Wenigen Zutaten ein perfektes Gericht ist, wenns mal schnell gehen soll");
                    cacioepepe.setOwner(admin);
                    cacioepepe.setImageId(img.getId());
                    cacioepepe.setDescription(
                        "1) Spaghetti zuerst in gut gesalzenem Wasser bissfest (al dente) kochen. Bevor man die Spaghetti abseiht,"
                            + "etwas Kochwasser entnehmen und beiseite stellen. Erst danach die Spaghetti abseihen und abtropfen lassen. \n"
                            + "2) In der Zwischenzeit den Käse reiben und die Pfefferkörner in einem Mörser mahlen oder zerdrücken\n"
                            + "3) Den Pfeffer in einer Pfanne anrösten und mit etwas Nudelwasser ablöschen. \n"
                            + "4) Nach dem Abgießen der Nudeln, einen Teil der Nudeln in die Pfanne mit dem Pfeffer geben und gut umrühren. Es kann mehr Nudelwasser hinzugefügt werden wenn nötig\n"
                            + "5) Ungefähr die Hälfte des geriebenen Peccorino in eine Schüssel geben und mit Nudelwasser vermengen bis eine Creme entsteht\n"
                            + "6) Restliche Nudeln in die Pfanne geben und die Peccorino Creme dazu und gut mischen"
                            + "7) Beim Servieren etwas vom gerieben Peccorino über die Spagetti steuen");
                    cacioepepe.setIngredients(amountList);
                    spaghettiAmount.setRecipe(cacioepepe);
                    saltAmount.setRecipe(cacioepepe);
                    pepperAmount.setRecipe(cacioepepe);
                    parmesanAmount.setRecipe(cacioepepe);

                    var author = authorRepository.findById(4L);
                    cacioepepe.setAuthor(author.get());

                    recipeRepository.saveAndFlush(cacioepepe);
                    amountRepository.save(spaghettiAmount);
                    amountRepository.save(saltAmount);
                    amountRepository.save(pepperAmount);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Spaghetti Cacio e Pepe");
                }
            }

            if (recipeRepository.findByName("Butter Chicken").isEmpty()) {
                var rice = ingredientRepository.findByName("Reis");
                var salt = ingredientRepository.findByName("Salz");
                var water = ingredientRepository.findByName("Wasser");
                var chicken = ingredientRepository.findByName("Hühnchen");
                var yogurt = ingredientRepository.findByName("Joghurt");
                var chili = ingredientRepository.findByName("Chilipulver");
                var turmeric = ingredientRepository.findByName("Kurkuma");
                var lemon = ingredientRepository.findByName("Zitronensaft");
                var caraway = ingredientRepository.findByName("gemahlener Kreuzkümmel");
                var garam = ingredientRepository.findByName("Garam Masala");
                var ginger = ingredientRepository.findByName("Ingwer");
                var onion = ingredientRepository.findByName("Zwiebeln");
                var garlic = ingredientRepository.findByName("Knoblauchzehe");
                var clover = ingredientRepository.findByName("Bockshornklee-Pulver");
                var sugar = ingredientRepository.findByName("Zucker");
                var ghee = ingredientRepository.findByName("Ghee");
                var tomatoes = ingredientRepository.findByName("passierte Tomaten");
                var cream = ingredientRepository.findByName("Schlagobers");
                var butter = ingredientRepository.findByName("Butter");

                if (rice.isPresent()
                    && water.isPresent()
                    && butter.isPresent()
                    && cream.isPresent()
                    && tomatoes.isPresent()
                    && sugar.isPresent()
                    && clover.isPresent()
                    && garam.isPresent()
                    && garlic.isPresent()
                    && salt.isPresent()
                    && ginger.isPresent()
                    && onion.isPresent()
                    && yogurt.isPresent()
                    && caraway.isPresent()
                    && chicken.isPresent()
                    && chili.isPresent()
                    && turmeric.isPresent()
                    && lemon.isPresent()
                    && ghee.isPresent()) {
                    LOGGER.trace("inserting Butter Chicken");

                    Amount amount1 = new Amount();
                    amount1.setIngredient(rice.get());
                    amount1.setUnit(AmountUnit.CUP);
                    amount1.setAmount(1.0);

                    Amount amount2 = new Amount();
                    amount2.setIngredient(water.get());
                    amount2.setUnit(AmountUnit.CUP);
                    amount2.setAmount(1.0);

                    Amount amount3 = new Amount();
                    amount3.setIngredient(chicken.get());
                    amount3.setUnit(AmountUnit.G);
                    amount3.setAmount(680.0);

                    Amount amount4 = new Amount();
                    amount4.setIngredient(chili.get());
                    amount4.setUnit(AmountUnit.TSP);
                    amount4.setAmount(3.5);

                    Amount amount5 = new Amount();
                    amount5.setIngredient(turmeric.get());
                    amount5.setUnit(AmountUnit.TSP);
                    amount5.setAmount(1.5);

                    Amount amount6 = new Amount();
                    amount6.setIngredient(lemon.get());
                    amount6.setUnit(AmountUnit.TSP);
                    amount6.setAmount(2.0);

                    Amount amount7 = new Amount();
                    amount7.setIngredient(caraway.get());
                    amount7.setUnit(AmountUnit.TSP);
                    amount7.setAmount(1.0);

                    Amount amount8 = new Amount();
                    amount8.setIngredient(garam.get());
                    amount8.setUnit(AmountUnit.TBSP);
                    amount8.setAmount(1.5);

                    Amount amount9 = new Amount();
                    amount9.setIngredient(salt.get());
                    amount9.setUnit(AmountUnit.TSP);
                    amount9.setAmount(2.0);

                    Amount amount10 = new Amount();
                    amount10.setIngredient(ginger.get());
                    amount10.setUnit(AmountUnit.G);
                    amount10.setAmount(15.0);

                    Amount amount11 = new Amount();
                    amount11.setIngredient(onion.get());
                    amount11.setUnit(AmountUnit.PIECE);
                    amount11.setAmount(1.0);

                    Amount amount12 = new Amount();
                    amount12.setIngredient(clover.get());
                    amount12.setUnit(AmountUnit.TSP);
                    amount12.setAmount(0.5);

                    Amount amount13 = new Amount();
                    amount13.setIngredient(sugar.get());
                    amount13.setUnit(AmountUnit.TBSP);
                    amount13.setAmount(1.0);

                    Amount amount14 = new Amount();
                    amount14.setIngredient(ghee.get());
                    amount14.setUnit(AmountUnit.TBSP);
                    amount14.setAmount(2.0);

                    Amount amount15 = new Amount();
                    amount15.setIngredient(tomatoes.get());
                    amount15.setUnit(AmountUnit.G);
                    amount15.setAmount(400.0);

                    Amount amount16 = new Amount();
                    amount16.setIngredient(cream.get());
                    amount16.setUnit(AmountUnit.CUP);
                    amount16.setAmount(0.75);

                    Amount amount17 = new Amount();
                    amount17.setIngredient(butter.get());
                    amount17.setUnit(AmountUnit.TBSP);
                    amount17.setAmount(2.0);

                    var amountList = new java.util.ArrayList<Amount>();

                    amountList.add(amount1);
                    amountList.add(amount2);
                    amountList.add(amount3);
                    amountList.add(amount4);
                    amountList.add(amount5);
                    amountList.add(amount6);
                    amountList.add(amount7);
                    amountList.add(amount8);
                    amountList.add(amount9);
                    amountList.add(amount10);
                    amountList.add(amount11);
                    amountList.add(amount12);
                    amountList.add(amount13);
                    amountList.add(amount14);
                    amountList.add(amount15);
                    amountList.add(amount16);
                    amountList.add(amount17);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    // Assuming the image for Butter Chicken is available in the image repository
                    Image img = new Image.ImageBuilder()
                        .setName("datageneratorImages/butterchicken.jpg")
                        .build();

                    imageRepository.save(img);

                    Recipe butterchicken = new Recipe();
                    if (recommendation != null) {
                        butterchicken.setRecommendationConfidence(recommendation.getConfidence());
                        butterchicken.setRecommendedCategory(recommendation.getCategory());
                    }

                    butterchicken.setDifficulty(Difficulty.HARD);
                    butterchicken.setName("Butter Chicken");
                    butterchicken.setOwner(admin);
                    butterchicken.setShortDescription(
                        "Butter Chicken, auch bekannt als \"Murgh Makhani\", ist ein beliebtes indisches Gericht, das für seine reiche und cremige Textur sowie sein köstliches Aroma bekannt ist.");
                    butterchicken.setDescription(
                        "Den Reis vor dem Kochen gründlich 2-mal waschen.\n"
                            + "In einen Reiskocher geben und Wasser hinzufügen. (Alternativ kann ein Schnellkochtopf verwendet werden, wodurch sich die Kochzeit halbiert, etwa 15 Minuten.)\n"
                            + "Kochen, bis der Reis vollständig gar ist.\n\n"
                            + "Die Hähnchenschenkel in eine Schüssel geben, die restlichen Zutaten hinzufügen und gründlich mischen, um sicherzustellen, dass das Hähnchen vollständig mit der Marinade bedeckt ist.\n"
                            + "Mit Frischhaltefolie abdecken und beiseite stellen.\n\n"
                            + "Eine 12-Zoll (ca. 30 cm) große Antihaft-Pfanne bei mittlerer bis hoher Hitze erhitzen, Ghee hinzufügen und schmelzen lassen. Die marinierten Hühnerstücke hinzufügen und von allen Seiten anbraten,"
                            + " bis sie goldbraun sind. (Es ist in Ordnung, wenn sie nicht ganz durchgegart sind.)\n"
                            + "Das Hühnchen entfernen, auf einem Backblech platzieren und beiseite stellen.\n"
                            + "Die Zwiebeln hinzufügen, salzen, Knoblauch und Ingwer hinzufügen und in derselben Pfanne unter häufigem Rühren weich garen. Anschließend Garam Masala, Kashmir, Kurkuma, Bockshornklee-Pulver "
                            + "und Zucker hinzufügen\n\n"
                            + "Füge die passierten Tomaten hinzu und köchle sie für 2 Minuten. Gib die Zwiebelmischung in den Mixer und püriere sie, bis sie glatt ist.\n"
                            + "Gib die Soße zurück in die Pfanne, füge das Hühnchen hinzu, decke es ab und lass es sieben Minuten lang köcheln; füge Sahne hinzu und lass es eindicken.\n"
                            + "Nimm die Hitze weg, rühre Butter ein und würze nach Geschmack mit Salz.\n"
                            + "\n"
                            + "Serviere das Hühnchen über Reis, optional garniert mit Koriander.");
                    butterchicken.setIngredients(amountList);
                    butterchicken.setImageId(img.getId());

                    amount1.setRecipe(butterchicken);
                    amount2.setRecipe(butterchicken);
                    amount3.setRecipe(butterchicken);
                    amount4.setRecipe(butterchicken);
                    amount5.setRecipe(butterchicken);
                    amount6.setRecipe(butterchicken);
                    amount7.setRecipe(butterchicken);
                    amount8.setRecipe(butterchicken);
                    amount9.setRecipe(butterchicken);
                    amount10.setRecipe(butterchicken);
                    amount11.setRecipe(butterchicken);
                    amount12.setRecipe(butterchicken);
                    amount13.setRecipe(butterchicken);
                    amount14.setRecipe(butterchicken);
                    amount15.setRecipe(butterchicken);
                    amount16.setRecipe(butterchicken);
                    amount17.setRecipe(butterchicken);

                    var author = authorRepository.findByFirstnameAndLastname("Joshua", "Weissman");
                    butterchicken.setAuthor(author.get());

                    recipeRepository.saveAndFlush(butterchicken);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: butterchicken");
                }
            }

            if (recipeRepository.findByName("Bao Buns").isEmpty()) {
                var wasserOpt = ingredientRepository.findByName("Wasser");
                var hefeOpt = ingredientRepository.findByName("Hefe");
                var mehlOpt = ingredientRepository.findByName("Mehl");
                var zuckerOpt = ingredientRepository.findByName("Zucker");
                var sonnenblumenoellOpt = ingredientRepository.findByName("Sonnenblumenöl");
                if (wasserOpt.isPresent() && hefeOpt.isPresent() && mehlOpt.isPresent() && zuckerOpt.isPresent()
                    && sonnenblumenoellOpt.isPresent()) {
                    Amount wasserAmount = new Amount();
                    wasserAmount.setIngredient(wasserOpt.get());
                    wasserAmount.setUnit(AmountUnit.ML);
                    wasserAmount.setAmount(175.0);

                    Amount hefeAmount = new Amount();
                    hefeAmount.setIngredient(hefeOpt.get());
                    hefeAmount.setUnit(AmountUnit.G);
                    hefeAmount.setAmount(10.0);

                    Amount mehlAmount = new Amount();
                    mehlAmount.setIngredient(mehlOpt.get());
                    mehlAmount.setUnit(AmountUnit.G);
                    mehlAmount.setAmount(350.0);

                    Amount zuckerAmount = new Amount();
                    zuckerAmount.setIngredient(zuckerOpt.get());
                    zuckerAmount.setUnit(AmountUnit.TBSP);
                    zuckerAmount.setAmount(2.0);

                    Amount oelAmount = new Amount();
                    oelAmount.setIngredient(sonnenblumenoellOpt.get());
                    oelAmount.setUnit(AmountUnit.TBSP);
                    oelAmount.setAmount(2.0);

                    var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(wasserAmount));
                    amountList.add(hefeAmount);
                    amountList.add(mehlAmount);
                    amountList.add(zuckerAmount);
                    amountList.add(oelAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image baoBunImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/baoBun.jpg")
                        .build();

                    imageRepository.save(baoBunImage);

                    Recipe baoBun = new Recipe();
                    if (recommendation != null) {
                        baoBun.setRecommendationConfidence(recommendation.getConfidence());
                        baoBun.setRecommendedCategory(recommendation.getCategory());
                    }
                    baoBun.setDifficulty(Difficulty.MEDIUM);
                    baoBun.setName("Bao Buns");
                    baoBun.setOwner(admin);
                    baoBun.setImageId(baoBunImage.getId());
                    baoBun.setShortDescription("Taiwanesische Burger Buns");

                    baoBun.setDescription(
                        "Bao Buns sind wie Germknödel nur in salzig.\n\n\n"
                            + "1) Wasser mit Hefe und Zucker verrühren, bis sich die Hefe aufgelöst hat.\n"
                            + "2) Mehl in eine Schüssel sieben, Hefewasser und Sonnenblumenöl zugeben, salzen und zu einem glatten Teig kneten. \n"
                            + "3) Zugedeckt an einem warmen Ort 30min gehen lassen. \n"
                            + "4) Den gegangenen Teig kurz durchkneten und auf einer bemehlten Fläche fingerdick ausrollen. Danach wieder 15 min gehen lassen.\n"
                            + "5) Wasser in einem Wok oder einer Pfanne zum Kochen bringen. "
                            + "Mit einem Ring von 11cm Durchmesser 6 Buns ausstechen, leicht mit Öl einpinseln und einem locker falten "
                            + "Aus Backpapier 6 Untersetzer für die Buns ausschneiden, die Dampfbrötchen aufsetzen. \n"
                            + "6) Buns uf dem Papier portionsweise in einen Bambus- oder Metalldämpfer oder auf einem Lochgitter setzen. "
                            + "12-15 min zugedeckt garen, den Deckel dabei nicht entfernen, sonst fallen die Buns in sich zusammen, bevor sie gar sind");

                    baoBun.setIngredients(amountList);
                    baoBun.setImageId(baoBunImage.getId());
                    wasserAmount.setRecipe(baoBun);
                    hefeAmount.setRecipe(baoBun);
                    mehlAmount.setRecipe(baoBun);
                    zuckerAmount.setRecipe(baoBun);
                    oelAmount.setRecipe(baoBun);

                    var author = authorRepository.findById(1L);
                    baoBun.setAuthor(author.get());

                    recipeRepository.saveAndFlush(baoBun);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Bao Buns");
                }
            }

            if (recipeRepository.findByName("Eingelegter Senfkohl").isEmpty()) {
                var wasserOpt = ingredientRepository.findByName("Wasser");
                var pakchoiOpt = ingredientRepository.findByName("Pak Choi");
                var zwiebelOpt = ingredientRepository.findByName("Zwiebeln");
                var zuckerOpt = ingredientRepository.findByName("Zucker");
                var knoblauchOpt = ingredientRepository.findByName("Knoblauchzehe");
                var salzOpt = ingredientRepository.findByName("Salz");
                var reisessigOpt = ingredientRepository.findByName("Reisessig");
                if (wasserOpt.isPresent() && pakchoiOpt.isPresent() && zwiebelOpt.isPresent() && zuckerOpt.isPresent()
                    && knoblauchOpt.isPresent() && salzOpt.isPresent() && reisessigOpt.isPresent()) {
                    Amount wasserAmount = new Amount();
                    wasserAmount.setIngredient(wasserOpt.get());
                    wasserAmount.setUnit(AmountUnit.ML);
                    wasserAmount.setAmount(100.0);

                    Amount pakchoiAmount = new Amount();
                    pakchoiAmount.setIngredient(pakchoiOpt.get());
                    pakchoiAmount.setUnit(AmountUnit.G);
                    pakchoiAmount.setAmount(300.0);

                    Amount zwiebelAmount = new Amount();
                    zwiebelAmount.setIngredient(zwiebelOpt.get());
                    zwiebelAmount.setUnit(AmountUnit.PIECE);
                    zwiebelAmount.setAmount(1.0);

                    Amount zuckerAmount = new Amount();
                    zuckerAmount.setIngredient(zuckerOpt.get());
                    zuckerAmount.setUnit(AmountUnit.G);
                    zuckerAmount.setAmount(25.0);

                    Amount knoblauchAmount = new Amount();
                    knoblauchAmount.setIngredient(knoblauchOpt.get());
                    knoblauchAmount.setUnit(AmountUnit.PIECE);
                    knoblauchAmount.setAmount(1.0);

                    Amount reisessigAmount = new Amount();
                    reisessigAmount.setIngredient(reisessigOpt.get());
                    reisessigAmount.setUnit(AmountUnit.ML);
                    reisessigAmount.setAmount(50.0);

                    Amount salzAmount = new Amount();
                    salzAmount.setIngredient(salzOpt.get());
                    salzAmount.setUnit(AmountUnit.G);
                    salzAmount.setAmount(5.0);

                    var amountList = new java.util.ArrayList<Amount>(Collections.singletonList(wasserAmount));
                    amountList.add(pakchoiAmount);
                    amountList.add(zwiebelAmount);
                    amountList.add(zuckerAmount);
                    amountList.add(knoblauchAmount);
                    amountList.add(reisessigAmount);
                    amountList.add(salzAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image pakChoiImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/pakChoi.jpg")
                        .build();

                    imageRepository.save(pakChoiImage);

                    Recipe pakChoi = new Recipe();
                    if (recommendation != null) {
                        pakChoi.setRecommendationConfidence(recommendation.getConfidence());
                        pakChoi.setRecommendedCategory(recommendation.getCategory());
                    }
                    pakChoi.setDifficulty(Difficulty.EASY);
                    pakChoi.setName("Eingelegter Senfkohl");
                    pakChoi.setOwner(admin);
                    pakChoi.setImageId(pakChoiImage.getId());
                    pakChoi.setShortDescription("Eingelegter Senfkohl (Pak Choi) selber gemacht");

                    pakChoi.setDescription(
                        "Eingelegter Senfkohl (Pak Choi) selber gemacht.\n\n\n"
                            + "1) Pak Choi entstrunken, Stiele und Blätter sehr fein schneiden.\n"
                            + "2) Zwiebel fein würfeln, Knoblauchzehe sehr fein würfeln und mit Wasser, Reisessig, Zucker und salz aufkochen. \n"
                            + "3) 1 min kochen. Den Senfkohl zugeben und unter Rühren 1 min kochen. \n"
                            + "4) Aushühlen lassen.");

                    pakChoi.setIngredients(amountList);
                    pakChoi.setImageId(pakChoiImage.getId());
                    wasserAmount.setRecipe(pakChoi);
                    pakchoiAmount.setRecipe(pakChoi);
                    zwiebelAmount.setRecipe(pakChoi);
                    zuckerAmount.setRecipe(pakChoi);
                    knoblauchAmount.setRecipe(pakChoi);
                    reisessigAmount.setRecipe(pakChoi);
                    salzAmount.setRecipe(pakChoi);

                    var author = authorRepository.findById(4L);
                    pakChoi.setAuthor(author.get());

                    recipeRepository.saveAndFlush(pakChoi);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Eingelegter Senfkohl");
                }
            }

            if (recipeRepository.findByName("Quinoa-Gemüsepfanne").isEmpty()) {

                var quinoaOpt = ingredientRepository.findByName("Quinoa");
                var gemuesebrueheOpt = ingredientRepository.findByName("Gemüsebrühe");
                var olivenoelOpt = ingredientRepository.findByName("Olivenöl");
                var zwiebelOpt = ingredientRepository.findByName("Zwiebeln");
                var paprikaRotOpt = ingredientRepository.findByName("Rote Paprika");
                var paprikaGelbOpt = ingredientRepository.findByName("Gelbe Paprika");
                var zucchiniOpt = ingredientRepository.findByName("Zucchini");
                var aubergineOpt = ingredientRepository.findByName("Aubergine");
                var tomatenOpt = ingredientRepository.findByName("Tomaten");
                var paprikapulverOpt = ingredientRepository.findByName("Paprikapulver");
                var kreuzkuemmelOpt = ingredientRepository.findByName("Kreuzkümmel");
                var salzOpt = ingredientRepository.findByName("Salz");
                var pfefferOpt = ingredientRepository.findByName("Pfeffer");
                var petersilieOpt = ingredientRepository.findByName("Petersilie");
                var korianderOpt = ingredientRepository.findByName("Koriander");

                if (quinoaOpt.isPresent() && gemuesebrueheOpt.isPresent() && olivenoelOpt.isPresent() && zwiebelOpt.isPresent()
                    && paprikaRotOpt.isPresent() && paprikaGelbOpt.isPresent() && zucchiniOpt.isPresent() && aubergineOpt.isPresent()
                    && tomatenOpt.isPresent() && paprikapulverOpt.isPresent() && kreuzkuemmelOpt.isPresent() && salzOpt.isPresent()
                    && pfefferOpt.isPresent() && petersilieOpt.isPresent() && korianderOpt.isPresent()) {

                    Amount quinoaAmount = new Amount();
                    quinoaAmount.setIngredient(quinoaOpt.get());
                    quinoaAmount.setUnit(AmountUnit.CUP);
                    quinoaAmount.setAmount(1.0);

                    Amount gemuesebrueheAmount = new Amount();
                    gemuesebrueheAmount.setIngredient(gemuesebrueheOpt.get());
                    gemuesebrueheAmount.setUnit(AmountUnit.CUP);
                    gemuesebrueheAmount.setAmount(2.0);

                    Amount olivenoelAmount = new Amount();
                    olivenoelAmount.setIngredient(olivenoelOpt.get());
                    olivenoelAmount.setUnit(AmountUnit.TBSP);
                    olivenoelAmount.setAmount(2.0);

                    Amount zwiebelAmount = new Amount();
                    zwiebelAmount.setIngredient(zwiebelOpt.get());
                    zwiebelAmount.setUnit(AmountUnit.PIECE);
                    zwiebelAmount.setAmount(1.0);

                    Amount paprikaRotAmount = new Amount();
                    paprikaRotAmount.setIngredient(paprikaRotOpt.get());
                    paprikaRotAmount.setUnit(AmountUnit.PIECE);
                    paprikaRotAmount.setAmount(1.0);

                    Amount paprikaGelbAmount = new Amount();
                    paprikaGelbAmount.setIngredient(paprikaGelbOpt.get());
                    paprikaGelbAmount.setUnit(AmountUnit.PIECE);
                    paprikaGelbAmount.setAmount(1.0);

                    Amount zucchiniAmount = new Amount();
                    zucchiniAmount.setIngredient(zucchiniOpt.get());
                    zucchiniAmount.setUnit(AmountUnit.PIECE);
                    zucchiniAmount.setAmount(1.0);

                    Amount aubergineAmount = new Amount();
                    aubergineAmount.setIngredient(aubergineOpt.get());
                    aubergineAmount.setUnit(AmountUnit.PIECE);
                    aubergineAmount.setAmount(1.0);

                    Amount tomatenAmount = new Amount();
                    tomatenAmount.setIngredient(tomatenOpt.get());
                    tomatenAmount.setUnit(AmountUnit.CUP);
                    tomatenAmount.setAmount(1.0);

                    Amount paprikapulverAmount = new Amount();
                    paprikapulverAmount.setIngredient(paprikapulverOpt.get());
                    paprikapulverAmount.setUnit(AmountUnit.TSP);
                    paprikapulverAmount.setAmount(1.0);

                    Amount kreuzkuemmelAmount = new Amount();
                    kreuzkuemmelAmount.setIngredient(kreuzkuemmelOpt.get());
                    kreuzkuemmelAmount.setUnit(AmountUnit.TSP);
                    kreuzkuemmelAmount.setAmount(0.5);

                    Amount salzAmount = new Amount();
                    salzAmount.setIngredient(salzOpt.get());
                    salzAmount.setUnit(AmountUnit.TSP);
                    salzAmount.setAmount(0.5);

                    Amount pfefferAmount = new Amount();
                    pfefferAmount.setIngredient(pfefferOpt.get());
                    pfefferAmount.setUnit(AmountUnit.TSP);
                    pfefferAmount.setAmount(0.5);

                    Amount petersilieAmount = new Amount();
                    petersilieAmount.setIngredient(petersilieOpt.get());
                    petersilieAmount.setUnit(AmountUnit.TBSP);
                    petersilieAmount.setAmount(1.0);

                    Amount korianderAmount = new Amount();
                    korianderAmount.setIngredient(korianderOpt.get());
                    korianderAmount.setUnit(AmountUnit.TBSP);
                    korianderAmount.setAmount(1.0);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(quinoaAmount);
                    amountList.add(gemuesebrueheAmount);
                    amountList.add(olivenoelAmount);
                    amountList.add(zwiebelAmount);
                    amountList.add(paprikaRotAmount);
                    amountList.add(paprikaGelbAmount);
                    amountList.add(zucchiniAmount);
                    amountList.add(aubergineAmount);
                    amountList.add(tomatenAmount);
                    amountList.add(paprikapulverAmount);
                    amountList.add(kreuzkuemmelAmount);
                    amountList.add(salzAmount);
                    amountList.add(pfefferAmount);
                    amountList.add(petersilieAmount);
                    amountList.add(korianderAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image quinoaImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/quinoa.jpg")
                        .build();

                    imageRepository.save(quinoaImage);

                    Recipe quinoaGemuesepfanne = new Recipe();
                    if (recommendation != null) {
                        quinoaGemuesepfanne.setRecommendationConfidence(recommendation.getConfidence());
                        quinoaGemuesepfanne.setRecommendedCategory(recommendation.getCategory());
                    }
                    quinoaGemuesepfanne.setDifficulty(Difficulty.EASY);
                    quinoaGemuesepfanne.setOwner(admin);
                    quinoaGemuesepfanne.setImageId(quinoaImage.getId());
                    quinoaGemuesepfanne.setShortDescription("Einfache Gemüsepfanne mit Quinoa, lecker und schnell gemacht!");
                    quinoaGemuesepfanne.setName("Quinoa-Gemüsepfanne");

                    quinoaGemuesepfanne.setDescription(
                        "Quinoa-Gemüsepfanne.\n\n"
                            + "1) Quinoa gründlich abspülen.\n"
                            + "2) Gemüsebrühe in einem Topf zum Kochen bringen. Quinoa hinzufügen und bei mittlerer Hitze zugedeckt köcheln lassen, bis das Wasser absorbiert ist und der Quinoa weich und locker ist.\n"
                            + "3) Währenddessen in einer Pfanne Olivenöl erhitzen. Zwiebeln darin anbraten, bis sie glasig sind.\n"
                            + "4) Paprika, Zucchini und Aubergine hinzufügen und anbraten, bis das Gemüse leicht gebräunt ist.\n"
                            + "5) Gehackte Tomaten, Paprikapulver und Kreuzkümmel zum Gemüse geben und gut vermischen. Mit Salz und Pfeffer abschmecken.\n"
                            + "6) Die gekochte Quinoa zum Gemüse geben und gut vermischen.\n"
                            + "7) Vor dem Servieren mit frischer Petersilie und Koriander garnieren.\n"
                            + "8) Heiß servieren und genießen!"
                    );

                    quinoaGemuesepfanne.setIngredients(amountList);
                    quinoaGemuesepfanne.setImageId(quinoaImage.getId());

                    quinoaAmount.setRecipe(quinoaGemuesepfanne);
                    gemuesebrueheAmount.setRecipe(quinoaGemuesepfanne);
                    olivenoelAmount.setRecipe(quinoaGemuesepfanne);
                    zwiebelAmount.setRecipe(quinoaGemuesepfanne);
                    paprikaRotAmount.setRecipe(quinoaGemuesepfanne);
                    paprikaGelbAmount.setRecipe(quinoaGemuesepfanne);
                    zucchiniAmount.setRecipe(quinoaGemuesepfanne);
                    aubergineAmount.setRecipe(quinoaGemuesepfanne);
                    tomatenAmount.setRecipe(quinoaGemuesepfanne);
                    paprikapulverAmount.setRecipe(quinoaGemuesepfanne);
                    kreuzkuemmelAmount.setRecipe(quinoaGemuesepfanne);
                    salzAmount.setRecipe(quinoaGemuesepfanne);
                    pfefferAmount.setRecipe(quinoaGemuesepfanne);
                    petersilieAmount.setRecipe(quinoaGemuesepfanne);
                    korianderAmount.setRecipe(quinoaGemuesepfanne);

                    var author = authorRepository.findById(4L);
                    quinoaGemuesepfanne.setAuthor(author.get());

                    recipeRepository.saveAndFlush(quinoaGemuesepfanne);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Quinoa Gemüsepfanne");

                }
            }

            if (recipeRepository.findByName("Gebackener Blumenkohl").isEmpty()) {
                var blumenkohlOpt = ingredientRepository.findByName("Blumenkohl");
                var zwiebelOpt = ingredientRepository.findByName("Zwiebeln");
                var knoblauchOpt = ingredientRepository.findByName("Knoblauch");
                var olivenoelOpt = ingredientRepository.findByName("Olivenöl");
                var currypulverOpt = ingredientRepository.findByName("Currypulver");
                var kreuzkuemmelOpt = ingredientRepository.findByName("Kreuzkümmel");
                var salzOpt = ingredientRepository.findByName("Salz");
                var pfefferOpt = ingredientRepository.findByName("Pfeffer");
                var zitronensaftOpt = ingredientRepository.findByName("Zitronensaft");
                var korianderOpt = ingredientRepository.findByName("Koriander");

                if (blumenkohlOpt.isPresent() && zwiebelOpt.isPresent() && knoblauchOpt.isPresent() && olivenoelOpt.isPresent()
                    && currypulverOpt.isPresent() && kreuzkuemmelOpt.isPresent() && salzOpt.isPresent() && pfefferOpt.isPresent()
                    && zitronensaftOpt.isPresent() && korianderOpt.isPresent()) {

                    Amount blumenkohlAmount = new Amount();
                    blumenkohlAmount.setIngredient(blumenkohlOpt.get());
                    blumenkohlAmount.setUnit(AmountUnit.PIECE);
                    blumenkohlAmount.setAmount(1.0);

                    Amount zwiebelAmount = new Amount();
                    zwiebelAmount.setIngredient(zwiebelOpt.get());
                    zwiebelAmount.setUnit(AmountUnit.PIECE);
                    zwiebelAmount.setAmount(1.0);

                    Amount knoblauchAmount = new Amount();
                    knoblauchAmount.setIngredient(knoblauchOpt.get());
                    knoblauchAmount.setUnit(AmountUnit.PIECE);
                    knoblauchAmount.setAmount(2.0);

                    Amount olivenoelAmount = new Amount();
                    olivenoelAmount.setIngredient(olivenoelOpt.get());
                    olivenoelAmount.setUnit(AmountUnit.TBSP);
                    olivenoelAmount.setAmount(2.0);

                    Amount currypulverAmount = new Amount();
                    currypulverAmount.setIngredient(currypulverOpt.get());
                    currypulverAmount.setUnit(AmountUnit.TSP);
                    currypulverAmount.setAmount(1.0);

                    Amount kreuzkuemmelAmount = new Amount();
                    kreuzkuemmelAmount.setIngredient(kreuzkuemmelOpt.get());
                    kreuzkuemmelAmount.setUnit(AmountUnit.TSP);
                    kreuzkuemmelAmount.setAmount(0.5);

                    Amount salzAmount = new Amount();
                    salzAmount.setIngredient(salzOpt.get());
                    salzAmount.setUnit(AmountUnit.TSP);
                    salzAmount.setAmount(0.5);

                    Amount pfefferAmount = new Amount();
                    pfefferAmount.setIngredient(pfefferOpt.get());
                    pfefferAmount.setUnit(AmountUnit.TSP);
                    pfefferAmount.setAmount(0.5);

                    Amount zitronensaftAmount = new Amount();
                    zitronensaftAmount.setIngredient(zitronensaftOpt.get());
                    zitronensaftAmount.setUnit(AmountUnit.TBSP);
                    zitronensaftAmount.setAmount(1.0);

                    Amount korianderAmount = new Amount();
                    korianderAmount.setIngredient(korianderOpt.get());
                    korianderAmount.setUnit(AmountUnit.TSP);
                    korianderAmount.setAmount(0.5);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(blumenkohlAmount);
                    amountList.add(zwiebelAmount);
                    amountList.add(knoblauchAmount);
                    amountList.add(olivenoelAmount);
                    amountList.add(currypulverAmount);
                    amountList.add(kreuzkuemmelAmount);
                    amountList.add(salzAmount);
                    amountList.add(pfefferAmount);
                    amountList.add(zitronensaftAmount);
                    amountList.add(korianderAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image blumenkohlImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/blumenkohl.jpg")
                        .build();

                    imageRepository.save(blumenkohlImage);

                    Recipe gebackenerBlumenkohl = new Recipe();
                    if (recommendation != null) {
                        gebackenerBlumenkohl.setRecommendationConfidence(recommendation.getConfidence());
                        gebackenerBlumenkohl.setRecommendedCategory(recommendation.getCategory());
                    }
                    gebackenerBlumenkohl.setDifficulty(Difficulty.EASY);
                    gebackenerBlumenkohl.setOwner(admin);
                    gebackenerBlumenkohl.setImageId(blumenkohlImage.getId());
                    gebackenerBlumenkohl.setShortDescription("Köstliches Blumenkohl-Rezept, einfach und schnell zubereitet!");
                    gebackenerBlumenkohl.setName("Blumenkohl mit Curry und Kreuzkümmel");

                    gebackenerBlumenkohl.setDescription(
                        "Blumenkohl mit Curry und Kreuzkümmel.\n\n"
                            + "1) Blumenkohl in kleine Röschen teilen und gründlich waschen.\n"
                            + "2) In einem großen Topf Wasser zum Kochen bringen und etwas Salz hinzufügen. Die Blumenkohlröschen hineingeben und ca. 5 Minuten kochen lassen, bis sie bissfest sind.\n"
                            + "3) In der Zwischenzeit in einer Pfanne Olivenöl erhitzen. Zwiebeln und Knoblauch darin anbraten, bis sie glasig sind.\n"
                            + "4) Den gekochten Blumenkohl zu den Zwiebeln und Knoblauch in die Pfanne geben. Currypulver und Kreuzkümmel hinzufügen und alles gut vermischen.\n"
                            + "5) Mit Salz und Pfeffer abschmecken und ca. 2-3 Minuten weiter braten, bis der Blumenkohl leicht gebräunt ist.\n"
                            + "6) Zitronensaft über den Blumenkohl träufeln und mit frischem Koriander garnieren.\n"
                            + "7) Heiß servieren und genießen!"
                    );

                    gebackenerBlumenkohl.setIngredients(amountList);
                    gebackenerBlumenkohl.setImageId(blumenkohlImage.getId());

                    blumenkohlAmount.setRecipe(gebackenerBlumenkohl);
                    zwiebelAmount.setRecipe(gebackenerBlumenkohl);
                    knoblauchAmount.setRecipe(gebackenerBlumenkohl);
                    olivenoelAmount.setRecipe(gebackenerBlumenkohl);
                    currypulverAmount.setRecipe(gebackenerBlumenkohl);
                    kreuzkuemmelAmount.setRecipe(gebackenerBlumenkohl);
                    salzAmount.setRecipe(gebackenerBlumenkohl);
                    pfefferAmount.setRecipe(gebackenerBlumenkohl);
                    zitronensaftAmount.setRecipe(gebackenerBlumenkohl);
                    korianderAmount.setRecipe(gebackenerBlumenkohl);

                    var author = authorRepository.findById(4L);
                    gebackenerBlumenkohl.setAuthor(author.get());

                    recipeRepository.saveAndFlush(gebackenerBlumenkohl);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Gebackener Blumenkohl");

                }
            }

            if (recipeRepository.findByName("Gebackener Lachs").isEmpty()) {
                var lachsOpt = ingredientRepository.findByName("Lachs");
                var zitroneOpt = ingredientRepository.findByName("Zitrone");
                var knoblauchOpt = ingredientRepository.findByName("Knoblauch");
                var olivenoelOpt = ingredientRepository.findByName("Olivenöl");
                var honigOpt = ingredientRepository.findByName("Honig");
                var sojasauceOpt = ingredientRepository.findByName("Sojasauce");
                var salzOpt = ingredientRepository.findByName("Salz");
                var pfefferOpt = ingredientRepository.findByName("Pfeffer");
                var sesamoelOpt = ingredientRepository.findByName("Sesamöl");
                var korianderOpt = ingredientRepository.findByName("Koriander");

                if (lachsOpt.isPresent() && zitroneOpt.isPresent() && knoblauchOpt.isPresent() && olivenoelOpt.isPresent()
                    && honigOpt.isPresent() && sojasauceOpt.isPresent() && salzOpt.isPresent() && pfefferOpt.isPresent()
                    && sesamoelOpt.isPresent() && korianderOpt.isPresent()) {

                    Amount lachsAmount = new Amount();
                    lachsAmount.setIngredient(lachsOpt.get());
                    lachsAmount.setUnit(AmountUnit.PIECE);
                    lachsAmount.setAmount(2.0);

                    Amount zitroneAmount = new Amount();
                    zitroneAmount.setIngredient(zitroneOpt.get());
                    zitroneAmount.setUnit(AmountUnit.PIECE);
                    zitroneAmount.setAmount(1.0);

                    Amount knoblauchAmount = new Amount();
                    knoblauchAmount.setIngredient(knoblauchOpt.get());
                    knoblauchAmount.setUnit(AmountUnit.PIECE);
                    knoblauchAmount.setAmount(2.0);

                    Amount olivenoelAmount = new Amount();
                    olivenoelAmount.setIngredient(olivenoelOpt.get());
                    olivenoelAmount.setUnit(AmountUnit.TBSP);
                    olivenoelAmount.setAmount(2.0);

                    Amount honigAmount = new Amount();
                    honigAmount.setIngredient(honigOpt.get());
                    honigAmount.setUnit(AmountUnit.TBSP);
                    honigAmount.setAmount(1.0);

                    Amount sojasauceAmount = new Amount();
                    sojasauceAmount.setIngredient(sojasauceOpt.get());
                    sojasauceAmount.setUnit(AmountUnit.TBSP);
                    sojasauceAmount.setAmount(2.0);

                    Amount salzAmount = new Amount();
                    salzAmount.setIngredient(salzOpt.get());
                    salzAmount.setUnit(AmountUnit.TSP);
                    salzAmount.setAmount(1.0);

                    Amount pfefferAmount = new Amount();
                    pfefferAmount.setIngredient(pfefferOpt.get());
                    pfefferAmount.setUnit(AmountUnit.TSP);
                    pfefferAmount.setAmount(1.0);

                    Amount sesamoelAmount = new Amount();
                    sesamoelAmount.setIngredient(sesamoelOpt.get());
                    sesamoelAmount.setUnit(AmountUnit.TBSP);
                    sesamoelAmount.setAmount(1.0);

                    Amount korianderAmount = new Amount();
                    korianderAmount.setIngredient(korianderOpt.get());
                    korianderAmount.setUnit(AmountUnit.TBSP);
                    korianderAmount.setAmount(1.0);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(lachsAmount);
                    amountList.add(zitroneAmount);
                    amountList.add(knoblauchAmount);
                    amountList.add(olivenoelAmount);
                    amountList.add(honigAmount);
                    amountList.add(sojasauceAmount);
                    amountList.add(salzAmount);
                    amountList.add(pfefferAmount);
                    amountList.add(sesamoelAmount);
                    amountList.add(korianderAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image lachsImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/lachs.jpg")
                        .build();

                    imageRepository.save(lachsImage);

                    Recipe gebackenerLachs = new Recipe();
                    if (recommendation != null) {
                        gebackenerLachs.setRecommendationConfidence(recommendation.getConfidence());
                        gebackenerLachs.setRecommendedCategory(recommendation.getCategory());
                    }
                    gebackenerLachs.setDifficulty(Difficulty.MEDIUM);
                    gebackenerLachs.setOwner(admin);
                    gebackenerLachs.setImageId(lachsImage.getId());
                    gebackenerLachs.setShortDescription("Köstliches Lachsrezept, einfach und schnell zubereitet!");
                    gebackenerLachs.setName("Gebackener Lachs");

                    gebackenerLachs.setDescription(
                        "Gebackener Lachs mit Zitrone und Knoblauch.\n\n"
                            + "1) Den Lachs abspülen und trocken tupfen.\n"
                            + "2) Den Knoblauch fein hacken und die Zitrone in Scheiben schneiden.\n"
                            + "3) Den Lachs mit Salz und Pfeffer würzen.\n"
                            + "4) In einer Pfanne Olivenöl erhitzen und den Lachs darin von beiden Seiten anbraten.\n"
                            + "5) Den Knoblauch und die Zitronenscheiben auf den Lachs legen.\n"
                            + "6) Den Honig, die Sojasauce und das Sesamöl darüber gießen.\n"
                            + "7) Die Pfanne in den vorgeheizten Ofen geben und den Lachs bei 180°C ca. 15-20 Minuten backen, bis er gar ist.\n"
                            + "8) Mit frischem Koriander garnieren und servieren."
                    );

                    gebackenerLachs.setIngredients(amountList);
                    gebackenerLachs.setImageId(lachsImage.getId());

                    lachsAmount.setRecipe(gebackenerLachs);
                    zitroneAmount.setRecipe(gebackenerLachs);
                    knoblauchAmount.setRecipe(gebackenerLachs);
                    olivenoelAmount.setRecipe(gebackenerLachs);
                    honigAmount.setRecipe(gebackenerLachs);
                    sojasauceAmount.setRecipe(gebackenerLachs);
                    salzAmount.setRecipe(gebackenerLachs);
                    pfefferAmount.setRecipe(gebackenerLachs);
                    sesamoelAmount.setRecipe(gebackenerLachs);
                    korianderAmount.setRecipe(gebackenerLachs);

                    var author = authorRepository.findById(4L);
                    gebackenerLachs.setAuthor(author.get());

                    recipeRepository.saveAndFlush(gebackenerLachs);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Gebackener Lachs");
                }

            }

            if (recipeRepository.findByName("Schokoladenmousse").isEmpty()) {
                var schokoladeOpt = ingredientRepository.findByName("Schokolade");
                var eiOpt = ingredientRepository.findByName("Ei");
                var zuckerOpt = ingredientRepository.findByName("Zucker");
                var sahneOpt = ingredientRepository.findByName("Sahne");

                if (schokoladeOpt.isPresent() && eiOpt.isPresent() && zuckerOpt.isPresent() && sahneOpt.isPresent()) {

                    Amount schokoladeAmount = new Amount();
                    schokoladeAmount.setIngredient(schokoladeOpt.get());
                    schokoladeAmount.setUnit(AmountUnit.G);
                    schokoladeAmount.setAmount(200.0);

                    Amount eiAmount = new Amount();
                    eiAmount.setIngredient(eiOpt.get());
                    eiAmount.setUnit(AmountUnit.PIECE);
                    eiAmount.setAmount(4.0);

                    Amount zuckerAmount = new Amount();
                    zuckerAmount.setIngredient(zuckerOpt.get());
                    zuckerAmount.setUnit(AmountUnit.G);
                    zuckerAmount.setAmount(80.0);

                    Amount sahneAmount = new Amount();
                    sahneAmount.setIngredient(sahneOpt.get());
                    sahneAmount.setUnit(AmountUnit.ML);
                    sahneAmount.setAmount(200.0);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(schokoladeAmount);
                    amountList.add(eiAmount);
                    amountList.add(zuckerAmount);
                    amountList.add(sahneAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image schokoladenmousseImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/schokoladenmousse.jpg")
                        .build();

                    imageRepository.save(schokoladenmousseImage);

                    Recipe schokoladenmousse = new Recipe();
                    if (recommendation != null) {
                        schokoladenmousse.setRecommendationConfidence(recommendation.getConfidence());
                        schokoladenmousse.setRecommendedCategory(recommendation.getCategory());
                    }
                    schokoladenmousse.setDifficulty(Difficulty.MEDIUM);
                    schokoladenmousse.setOwner(admin);
                    schokoladenmousse.setImageId(schokoladenmousseImage.getId());
                    schokoladenmousse.setShortDescription("Luftig-leichtes Schokoladenmousse, ein wahrer Genuss!");
                    schokoladenmousse.setName("Schokoladenmousse");

                    schokoladenmousse.setDescription(
                        "Schokoladenmousse.\n\n"
                            + "1) Schokolade grob hacken und über einem Wasserbad schmelzen lassen.\n"
                            + "2) Eier trennen und das Eiweiß steif schlagen. Zucker nach und nach einrieseln lassen und weiter schlagen, bis eine glänzende Masse entsteht.\n"
                            + "3) Geschmolzene Schokolade etwas abkühlen lassen und dann die Eigelb unterrühren.\n"
                            + "4) Die geschlagene Sahne vorsichtig unterheben.\n"
                            + "5) Die Schokoladenmousse in Gläser oder Schälchen füllen und für mindestens 2 Stunden im Kühlschrank fest werden lassen.\n"
                            + "6) Vor dem Servieren das Schokoladenmousse nach Belieben mit geschlagener Sahne, Schokoladenraspeln oder frischen Früchten garnieren.\n"
                            + "7) Genießen Sie das luftig-leichte Schokoladenmousse!\n");

                    schokoladenmousse.setIngredients(amountList);
                    schokoladenmousse.setImageId(schokoladenmousseImage.getId());

                    schokoladeAmount.setRecipe(schokoladenmousse);
                    eiAmount.setRecipe(schokoladenmousse);
                    zuckerAmount.setRecipe(schokoladenmousse);
                    sahneAmount.setRecipe(schokoladenmousse);

                    var author = authorRepository.findById(2L);
                    schokoladenmousse.setAuthor(author.get());

                    recipeRepository.saveAndFlush(schokoladenmousse);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Schokoladenmousse");
                }
            }

            if (recipeRepository.findByName("Zitronen-Tiramisu").isEmpty()) {
                var mascarponeOpt = ingredientRepository.findByName("Mascarpone");
                var zuckerOpt = ingredientRepository.findByName("Zucker");
                var zitroneOpt = ingredientRepository.findByName("Zitrone");
                var loeffelbiskuitsOpt = ingredientRepository.findByName("Löffelbiskuits");
                var limoncelloOpt = ingredientRepository.findByName("Limoncello");
                var sahneOpt = ingredientRepository.findByName("Sahne");

                if (mascarponeOpt.isPresent() && zuckerOpt.isPresent() && zitroneOpt.isPresent()
                    && loeffelbiskuitsOpt.isPresent() && limoncelloOpt.isPresent() && sahneOpt.isPresent()) {

                    Amount mascarponeAmount = new Amount();
                    mascarponeAmount.setIngredient(mascarponeOpt.get());
                    mascarponeAmount.setUnit(AmountUnit.G);
                    mascarponeAmount.setAmount(250.0);

                    Amount zuckerAmount = new Amount();
                    zuckerAmount.setIngredient(zuckerOpt.get());
                    zuckerAmount.setUnit(AmountUnit.G);
                    zuckerAmount.setAmount(100.0);

                    Amount zitroneAmount = new Amount();
                    zitroneAmount.setIngredient(zitroneOpt.get());
                    zitroneAmount.setUnit(AmountUnit.PIECE);
                    zitroneAmount.setAmount(2.0);

                    Amount loeffelbiskuitsAmount = new Amount();
                    loeffelbiskuitsAmount.setIngredient(loeffelbiskuitsOpt.get());
                    loeffelbiskuitsAmount.setUnit(AmountUnit.PIECE);
                    loeffelbiskuitsAmount.setAmount(24.0);

                    Amount limoncelloAmount = new Amount();
                    limoncelloAmount.setIngredient(limoncelloOpt.get());
                    limoncelloAmount.setUnit(AmountUnit.TBSP);
                    limoncelloAmount.setAmount(4.0);

                    Amount sahneAmount = new Amount();
                    sahneAmount.setIngredient(sahneOpt.get());
                    sahneAmount.setUnit(AmountUnit.ML);
                    sahneAmount.setAmount(200.0);

                    var amountList = new java.util.ArrayList<Amount>();
                    amountList.add(mascarponeAmount);
                    amountList.add(zuckerAmount);
                    amountList.add(zitroneAmount);
                    amountList.add(loeffelbiskuitsAmount);
                    amountList.add(limoncelloAmount);
                    amountList.add(sahneAmount);

                    var recommendation = wineRecommendationEngine.generateRecommendation(amountList);

                    Image tiramisuImage = new Image.ImageBuilder()
                        .setName("datageneratorImages/zitronen-tiramisu.jpg")
                        .build();

                    imageRepository.save(tiramisuImage);

                    Recipe zitronenTiramisu = new Recipe();
                    if (recommendation != null) {
                        zitronenTiramisu.setRecommendationConfidence(recommendation.getConfidence());
                        zitronenTiramisu.setRecommendedCategory(recommendation.getCategory());
                    }
                    zitronenTiramisu.setDifficulty(Difficulty.MEDIUM);
                    zitronenTiramisu.setOwner(admin);
                    zitronenTiramisu.setImageId(tiramisuImage.getId());
                    zitronenTiramisu.setShortDescription("Erfrischendes Zitronen-Tiramisu mit Mascarponecreme");
                    zitronenTiramisu.setName("Zitronen-Tiramisu");

                    zitronenTiramisu.setDescription(
                        "Zitronen-Tiramisu ist ein erfrischendes und leichtes Dessert, perfekt für den Sommer.\n\n"
                            + "1) Zitrone abreiben und den Saft auspressen.\n"
                            + "2) In einer Schüssel Mascarpone, Zucker, Zitronensaft und abgeriebene Zitronenschale gut vermischen.\n"
                            + "3) Sahne steif schlagen und vorsichtig unter die Mascarponecreme heben.\n"
                            + "4) Löffelbiskuits in Limoncello tränken und in eine Auflaufform schichten.\n"
                            + "5) Die Hälfte der Mascarponecreme auf den Löffelbiskuits verteilen.\n"
                            + "6) Eine weitere Schicht getränkter Löffelbiskuits darauf legen und mit der restlichen Mascarponecreme bedecken.\n"
                            + "7) Das Zitronen-Tiramisu mindestens 2 Stunden im Kühlschrank ziehen lassen.\n"
                            + "8) Vor dem Servieren mit Zitronenzesten garnieren.\n"
                            + "9) Genießen Sie Ihr erfrischendes Zitronen-Tiramisu!"
                    );

                    zitronenTiramisu.setIngredients(amountList);
                    zitronenTiramisu.setImageId(tiramisuImage.getId());

                    mascarponeAmount.setRecipe(zitronenTiramisu);
                    zuckerAmount.setRecipe(zitronenTiramisu);
                    zitroneAmount.setRecipe(zitronenTiramisu);
                    loeffelbiskuitsAmount.setRecipe(zitronenTiramisu);
                    limoncelloAmount.setRecipe(zitronenTiramisu);
                    sahneAmount.setRecipe(zitronenTiramisu);

                    var author = authorRepository.findById(1L);
                    zitronenTiramisu.setAuthor(author.get());

                    recipeRepository.saveAndFlush(zitronenTiramisu);
                    amountRepository.saveAll(amountList);
                    imageRepository.flush();
                    recipeRepository.flush();
                    LOGGER.info("Inserted Recipe: Zitronen-Tiramisu");
                }
            }




        }
    }
}
