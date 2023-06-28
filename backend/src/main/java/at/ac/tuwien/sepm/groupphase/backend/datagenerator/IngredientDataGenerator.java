package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This data generator generates ingredient data.
 */
@Profile("generateData")
@Component
public class IngredientDataGenerator {
    private final IngredientRepository ingredientRepository;

    public IngredientDataGenerator(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void generate() {

        Ingredient egg = new Ingredient();
        egg.setCategory(IngredientMatchingCategory.PEPPERS_CHILLIS);
        egg.setName("Ei");
        insertIfNotExists(egg);

        Ingredient chili = new Ingredient();
        chili.setCategory(IngredientMatchingCategory.PEPPERS_CHILLIS);
        chili.setName("Chilipulver");
        insertIfNotExists(chili);

        Ingredient ginger = new Ingredient();
        ginger.setCategory(IngredientMatchingCategory.VEGETABLE_ROOTS);
        ginger.setName("Ingwer");
        insertIfNotExists(ginger);

        Ingredient avocado = new Ingredient();
        avocado.setCategory(IngredientMatchingCategory.UNDEFINED);
        avocado.setName("Avocado");
        insertIfNotExists(avocado);

        Ingredient jam = new Ingredient();
        jam.setCategory(IngredientMatchingCategory.UNDEFINED);
        jam.setName("Marmelade");
        insertIfNotExists(jam);

        Ingredient rice = new Ingredient();
        rice.setCategory(IngredientMatchingCategory.UNDEFINED);
        rice.setName("Reis");
        insertIfNotExists(rice);

        Ingredient yogurt = new Ingredient();
        yogurt.setCategory(IngredientMatchingCategory.UNDEFINED);
        yogurt.setName("Joghurt");
        insertIfNotExists(yogurt);

        Ingredient sesame = new Ingredient();
        sesame.setCategory(IngredientMatchingCategory.HERBS_AROMATIC);
        sesame.setName("Sesam");
        insertIfNotExists(sesame);

        Ingredient soySauce = new Ingredient();
        soySauce.setCategory(IngredientMatchingCategory.MARINADE_INTENSE);
        soySauce.setName("Sojasauce");
        insertIfNotExists(soySauce);

        Ingredient mozzarella = new Ingredient();
        mozzarella.setCategory(IngredientMatchingCategory.CHEESE_CREAM);
        mozzarella.setName("Mozzarella");
        insertIfNotExists(mozzarella);

        Ingredient semmelbroesel = new Ingredient();
        semmelbroesel.setCategory(IngredientMatchingCategory.UNDEFINED);
        semmelbroesel.setName("Semmelbrösel");
        insertIfNotExists(semmelbroesel);

        Ingredient flour = new Ingredient();
        flour.setCategory(IngredientMatchingCategory.UNDEFINED);
        flour.setName("Mehl");
        insertIfNotExists(flour);

        Ingredient oil = new Ingredient();
        oil.setCategory(IngredientMatchingCategory.UNDEFINED);
        oil.setName("Öl");
        insertIfNotExists(oil);

        Ingredient salt = new Ingredient();
        salt.setCategory(IngredientMatchingCategory.UNDEFINED);
        salt.setName("Salz");
        insertIfNotExists(salt);

        Ingredient spaghetti = new Ingredient();
        spaghetti.setCategory(IngredientMatchingCategory.UNDEFINED);
        spaghetti.setName("Spaghetti");
        insertIfNotExists(spaghetti);

        Ingredient bacon = new Ingredient();
        bacon.setCategory(IngredientMatchingCategory.MEAT_CURED);
        bacon.setName("Speck");
        insertIfNotExists(bacon);

        Ingredient turmeric = new Ingredient();
        turmeric.setCategory(IngredientMatchingCategory.HERBS_EXOTIC);
        turmeric.setName("Kurkuma");
        insertIfNotExists(turmeric);

        Ingredient oliveOil = new Ingredient();
        oliveOil.setCategory(IngredientMatchingCategory.UNDEFINED);
        oliveOil.setName("Olivenöl");
        insertIfNotExists(oliveOil);

        Ingredient grana = new Ingredient();
        grana.setCategory(IngredientMatchingCategory.CHEESE_INTENSE);
        grana.setName("Grana Padano Käse");
        insertIfNotExists(grana);

        Ingredient pepper = new Ingredient();
        pepper.setCategory(IngredientMatchingCategory.HERBS_DRY);
        pepper.setName("Pfeffer");
        insertIfNotExists(pepper);

        Ingredient steak = new Ingredient();
        steak.setCategory(IngredientMatchingCategory.MEAT_RED);
        steak.setName("Rindfleisch Filet");
        insertIfNotExists(steak);

        Ingredient butter = new Ingredient();
        butter.setCategory(IngredientMatchingCategory.UNDEFINED);
        butter.setName("Butter");
        insertIfNotExists(butter);

        Ingredient kohlrabi = new Ingredient();
        kohlrabi.setCategory(IngredientMatchingCategory.VEGETABLE_GREEN);
        kohlrabi.setName("Kohlrabi");
        insertIfNotExists(kohlrabi);

        Ingredient faschiertes = new Ingredient();
        faschiertes.setCategory(IngredientMatchingCategory.MEAT_RED);
        faschiertes.setName("Faschiertes");
        insertIfNotExists(faschiertes);

        Ingredient salsiccia = new Ingredient();
        salsiccia.setCategory(IngredientMatchingCategory.MEAT_CURED);
        salsiccia.setName("Salsiccia");
        insertIfNotExists(salsiccia);

        Ingredient tomaten = new Ingredient();
        tomaten.setCategory(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW);
        tomaten.setName("Tomaten");
        insertIfNotExists(tomaten);

        Ingredient passTomaten = new Ingredient();
        passTomaten.setCategory(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW);
        passTomaten.setName("passierte Tomaten");
        insertIfNotExists(passTomaten);

        Ingredient wasser = new Ingredient();
        wasser.setCategory(IngredientMatchingCategory.UNDEFINED);
        wasser.setName("Wasser");
        insertIfNotExists(wasser);

        Ingredient rotwein = new Ingredient();
        rotwein.setCategory(IngredientMatchingCategory.UNDEFINED);
        rotwein.setName("Rotwein");
        insertIfNotExists(rotwein);

        Ingredient weisswein = new Ingredient();
        weisswein.setCategory(IngredientMatchingCategory.UNDEFINED);
        weisswein.setName("Weißwein");
        insertIfNotExists(weisswein);

        Ingredient chicken = new Ingredient();
        chicken.setCategory(IngredientMatchingCategory.MEAT_WHITE);
        chicken.setName("Hühnchen");
        insertIfNotExists(chicken);

        Ingredient karotten = new Ingredient();
        karotten.setCategory(IngredientMatchingCategory.VEGETABLE_ROOTS);
        karotten.setName("Karotten");
        insertIfNotExists(karotten);

        Ingredient zwiebeln = new Ingredient();
        zwiebeln.setCategory(IngredientMatchingCategory.VEGETABLE_BULBOUS);
        zwiebeln.setName("Zwiebeln");
        insertIfNotExists(zwiebeln);

        Ingredient clover = new Ingredient();
        clover.setCategory(IngredientMatchingCategory.HERBS_AROMATIC);
        clover.setName("Bockshornklee-Pulver");
        insertIfNotExists(clover);

        Ingredient lemon = new Ingredient();
        lemon.setCategory(IngredientMatchingCategory.UNDEFINED);
        lemon.setName("Zitronensaft");
        insertIfNotExists(lemon);

        Ingredient sugar = new Ingredient();
        sugar.setCategory(IngredientMatchingCategory.UNDEFINED);
        sugar.setName("Zucker");
        insertIfNotExists(sugar);

        Ingredient ghee = new Ingredient();
        ghee.setCategory(IngredientMatchingCategory.UNDEFINED);
        ghee.setName("Ghee");
        insertIfNotExists(ghee);

        Ingredient sellerie = new Ingredient();
        sellerie.setCategory(IngredientMatchingCategory.VEGETABLE_CRUCIFEROUS);
        sellerie.setName("Sellerie");
        insertIfNotExists(sellerie);

        Ingredient salz = new Ingredient();
        salz.setCategory(IngredientMatchingCategory.UNDEFINED);
        salz.setName("Salz");
        insertIfNotExists(salz);

        Ingredient milch = new Ingredient();
        milch.setCategory(IngredientMatchingCategory.UNDEFINED);
        milch.setName("Milch");
        insertIfNotExists(milch);

        Ingredient mehl = new Ingredient();
        mehl.setCategory(IngredientMatchingCategory.UNDEFINED);
        mehl.setName("Mehl");
        insertIfNotExists(mehl);

        Ingredient muskatnuss = new Ingredient();
        muskatnuss.setCategory(IngredientMatchingCategory.HERBS_EXOTIC);
        muskatnuss.setName("Muskatnuss");
        insertIfNotExists(muskatnuss);

        Ingredient caraway = new Ingredient();
        caraway.setCategory(IngredientMatchingCategory.HERBS_DRY);
        caraway.setName("gemahlener Kreuzkümmel");
        insertIfNotExists(caraway);

        Ingredient garam = new Ingredient();
        garam.setCategory(IngredientMatchingCategory.HERBS_EXOTIC);
        garam.setName("Garam Masala");
        insertIfNotExists(garam);

        Ingredient parmesan = new Ingredient();
        parmesan.setCategory(IngredientMatchingCategory.CHEESE_DRY_SALTY);
        parmesan.setName("Parmesan");
        insertIfNotExists(parmesan);

        Ingredient peccorino = new Ingredient();
        peccorino.setCategory(IngredientMatchingCategory.CHEESE_DRY_SALTY);
        peccorino.setName("Peccorino");
        insertIfNotExists(peccorino);

        Ingredient lasagneSheets = new Ingredient();
        lasagneSheets.setCategory(IngredientMatchingCategory.UNDEFINED);
        lasagneSheets.setName("Lasagneblätter");
        insertIfNotExists(lasagneSheets);

        Ingredient ingredient;

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Süßkartoffeln");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Spinat");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Pinienkerne");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("rote Zwiebel");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Knoblauchzehe");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Feta");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Walnüsse");
        insertIfNotExists(ingredient);


        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Paprika");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Zitrone");
        insertIfNotExists(ingredient);


        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Kartoffeln");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Rosmarin");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Gemischter grüner Salat");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Hähnchenbrustfilet");
        insertIfNotExists(ingredient);


        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Balsamico-Essig");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Schlagobers");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.HERBS_BAKING);
        ingredient.setName("Hefe");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Sonnenblumenöl");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.VEGETABLE_GREEN);
        ingredient.setName("Pak Choi");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Reisessig");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.VEGETABLE_LEGUMES);
        ingredient.setName("Rote Linsen");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Kokosmilch");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.HERBS_EXOTIC);
        ingredient.setName("Currypaste");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.HERBS_AROMATIC);
        ingredient.setName("Koriander");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.HERBS_UMAMI);
        ingredient.setName("Gemüsebrühe");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.HERBS_AROMATIC);
        ingredient.setName("Kümmel");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Petersilie");
        insertIfNotExists(ingredient);

        Ingredient quinoa = new Ingredient();
        quinoa.setCategory(IngredientMatchingCategory.UNDEFINED);
        quinoa.setName("Quinoa");
        insertIfNotExists(quinoa);

        Ingredient paprikaRot = new Ingredient();
        paprikaRot.setCategory(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW);
        paprikaRot.setName("Rote Paprika");
        insertIfNotExists(paprikaRot);

        Ingredient paprikaGelb = new Ingredient();
        paprikaGelb.setCategory(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW);
        paprikaGelb.setName("Gelbe Paprika");
        insertIfNotExists(paprikaGelb);

        Ingredient zucchini = new Ingredient();
        zucchini.setCategory(IngredientMatchingCategory.VEGETABLE_GREEN);
        zucchini.setName("Zucchini");
        insertIfNotExists(zucchini);

        Ingredient aubergine = new Ingredient();
        aubergine.setCategory(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW);
        aubergine.setName("Aubergine");
        insertIfNotExists(aubergine);

        Ingredient paprikapulver = new Ingredient();
        paprikapulver.setCategory(IngredientMatchingCategory.HERBS_AROMATIC);
        paprikapulver.setName("Paprikapulver");
        insertIfNotExists(paprikapulver);

        Ingredient kreuzkuemmel = new Ingredient();
        kreuzkuemmel.setCategory(IngredientMatchingCategory.HERBS_AROMATIC);
        kreuzkuemmel.setName("Kreuzkümmel");
        insertIfNotExists(kreuzkuemmel);

        Ingredient blumenkohl = new Ingredient();
        blumenkohl.setCategory(IngredientMatchingCategory.UNDEFINED);
        blumenkohl.setName("Blumenkohl");
        insertIfNotExists(blumenkohl);

        Ingredient knoblauch = new Ingredient();
        knoblauch.setCategory(IngredientMatchingCategory.UNDEFINED);
        knoblauch.setName("Knoblauch");
        insertIfNotExists(knoblauch);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Currypulver");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.FISH);
        ingredient.setName("Lachs");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.HERBS_EXOTIC);
        ingredient.setName("Sesamöl");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Honig");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Sahne");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Schokolade");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Mascarpone");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Löffelbiskuits");
        insertIfNotExists(ingredient);

        ingredient = new Ingredient();
        ingredient.setCategory(IngredientMatchingCategory.UNDEFINED);
        ingredient.setName("Limoncello");
        insertIfNotExists(ingredient);

    }

    private void insertIfNotExists(Ingredient ingredient) {
        if (ingredientRepository.findByName(ingredient.getName()).isEmpty()) {
            ingredientRepository.saveAndFlush(ingredient);
        }
    }
}
