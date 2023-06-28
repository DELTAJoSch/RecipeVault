package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * This data generator generates author data.
 */
@Profile("generateData")
@Component
public class AuthorDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AuthorRepository authorRepository;
    private final ImageRepository imageRepository;

    public AuthorDataGenerator(AuthorRepository authorRepository, ImageRepository imageRepository) {
        this.authorRepository = authorRepository;
        this.imageRepository = imageRepository;
    }

    public void generate() {

        if (authorRepository.findAll().size() > 0) {
            LOGGER.debug("message already generated");
        } else {
            LOGGER.debug("generating default author");

            final Image image1 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author1.jpg")
                    .build();

            final Image image2 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author2.jpg")
                    .build();
            final Image image3 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author3.jpg")
                    .build();
            final Image image4 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author4.jpg")
                    .build();
            final Image image5 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author5.jpg")
                    .build();
            final Image image6 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author6.jpg")
                    .build();
            final Image image7 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author7.jpg")
                    .build();
            final Image image8 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author8.jpg")
                    .build();
            final Image image9 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author9.jpg")
                    .build();
            final Image image10 = new Image.ImageBuilder()
                    .setName("datageneratorImages/author10.jpg")
                    .build();

            final Author author1 = new Author("Jamie", "Oliver",
                    "Jamie Oliver ist ein britischer Koch, Autor und Fernsehmoderator, "
                            + "der für seine leidenschaftliche und zugängliche Herangehensweise an das Kochen bekannt ist. "
                            + "Geboren am 27. Mai 1975 in Clavering, Essex, begann Oliver seine Karriere in "
                            + "der Gastronomiebranche im Alter von 16 Jahren.\n"
                            + "Er wurde international bekannt durch seine Fernsehsendungen wie \"The Naked Chef\", in denen "
                            + "er einfache und köstliche Rezepte präsentierte und die Zuschauer dazu ermutigte, selbstbewusst "
                            + "in der Küche zu experimentieren. Olivers lockere Art und sein charismatisches Auftreten haben "
                            + "ihm eine große Fangemeinde eingebracht.\n"
                            + "Jamie Oliver hat mehrere Kochbücher veröffentlicht, die weltweit Bestseller wurden. Er ist für "
                            + "seine Bemühungen um eine gesunde Ernährung und die Förderung von frischen, saisonalen Zutaten bekannt. "
                            + "Oliver setzt sich auch für eine Verbesserung der Schulverpflegung und Ernährungsbildung ein und hat "
                            + "sich aktiv für Maßnahmen zur Bekämpfung von Fettleibigkeit bei Kindern eingesetzt.\n"
                            + "Als Unternehmer hat Jamie Oliver eine Reihe von Restaurants eröffnet, darunter das beliebte "
                            + "\"Fifteen\", das jungen Menschen mit schwierigem Hintergrund eine Ausbildung in der Gastronomie bietet. "
                            + "Er ist auch in sozialen Medien aktiv und teilt regelmäßig Rezepte, Tipps und Ratschläge mit seinen Followern.\n"
                            + "Jamie Oliver ist ein vielseitiger Koch, der sowohl traditionelle als auch moderne Küche beherrscht. "
                            + "Er hat einen unverwechselbaren Stil, der von frischen Zutaten, kreativen Geschmackskombinationen und "
                            + "einfachen Zubereitungsmethoden geprägt ist. Mit seinem Enthusiasmus und seiner Leidenschaft für gutes "
                            + "Essen hat er das Kochen für viele Menschen zugänglicher und unterhaltsamer gemacht.");

            final Author author2 = new Author("Nigella", "Lawson",
                    "Nigella Lawson ist eine britische Autorin, Fernsehmoderatorin und Gastrokritikerin, "
                            + "die für ihre sinnliche und genussvolle Herangehensweise an das Kochen bekannt ist. "
                            + "Geboren am 6. Januar 1960 in London, entdeckte Lawson ihre Leidenschaft für das Kochen "
                            + "früh und begann ihre Karriere in der Gastronomiebranche als Restaurantkritikerin.\n"
                            + "Sie erlangte internationale Bekanntheit durch ihre Fernsehsendungen wie \"Nigella Bites\", in denen "
                            + "sie mit Leidenschaft und Hingabe köstliche Rezepte präsentierte. Lawsons charismatische Persönlichkeit "
                            + "und ihre Fähigkeit, Essen mit einer sinnlichen Note zu präsentieren, haben ihr eine große Fangemeinde eingebracht.\n"
                            + "Nigella Lawson hat mehrere erfolgreiche Kochbücher veröffentlicht, die sowohl für ihre kulinarischen "
                            + "Kreationen als auch für ihre inspirierenden Texte bekannt sind. Sie verbindet gekonnt die Liebe zum Essen "
                            + "mit persönlichen Anekdoten und Erinnerungen, was ihre Bücher zu einzigartigen Leseerlebnissen macht.\n"
                            + "Als Befürworterin von Genuss und Wohlbefinden steht Nigella Lawson für eine entspannte und unkomplizierte "
                            + "Herangehensweise an das Kochen. Sie ermutigt die Menschen, das Essen in vollen Zügen zu genießen und "
                            + "sich von starren Regeln und Restriktionen zu lösen. Lawson betont die Bedeutung von hochwertigen Zutaten "
                            + "und einfachen Zubereitungsmethoden, um das Beste aus den Aromen herauszuholen.\n"
                            + "Abgesehen von ihrer Karriere als Autorin und Fernsehmoderatorin ist Nigella Lawson auch als Gastrokritikerin "
                            + "aktiv und hat einen scharfen Blick für gute Küche. Sie teilt regelmäßig ihre kulinarischen Entdeckungen "
                            + "und Empfehlungen mit ihren Lesern und Zuschauern. Lawson ist eine starke Befürworterin der Vielfalt "
                            + "in der Küche und setzt sich für die Wertschätzung und den Respekt vor unterschiedlichen Esskulturen ein");

            final Author author3 = new Author("Alfons", "Schubeck",
                    "Alfons Schubeck ist ein renommierter deutscher Koch, Autor und Fernsehmoderator, "
                            + "der für seine kulinarische Expertise und seine einzigartige Interpretation der deutschen Küche bekannt ist. "
                            + "Geboren am 2. Februar 1949 in Traunstein, Bayern, entwickelte Schubeck bereits in jungen Jahren eine Leidenschaft "
                            + "für das Kochen und begann seine Karriere in renommierten Restaurants.\n"
                            + "Er erlangte internationale Bekanntheit durch seine Fernsehsendungen wie \"Schuhbecks Küchenkabarett\", in denen "
                            + "er seine Kochkünste mit einem Hauch von Entertainment verbindet. Schubecks charmante Persönlichkeit und seine "
                            + "Fähigkeit, traditionelle Gerichte auf innovative Weise zu präsentieren, haben ihm eine treue Anhängerschaft eingebracht.\n"
                            + "Alfons Schubeck hat zahlreiche Kochbücher veröffentlicht, in denen er seine Kochphilosophie und seine einzigartigen "
                            + "Rezepte teilt. Seine Bücher sind für ihre Kreativität, Raffinesse und Liebe zum Detail bekannt. Schubeck ist "
                            + "bekannt für seine Fähigkeit, klassische Gerichte neu zu interpretieren und ihnen eine moderne Note zu verleihen.\n"
                            + "Als Botschafter der deutschen Küche setzt sich Alfons Schubeck dafür ein, regionale und saisonale Zutaten zu nutzen "
                            + "und die Vielfalt der deutschen Gastronomie zu würdigen. Er ist ein Verfechter der traditionellen Kochtechniken und "
                            + "hochwertiger Zutaten, die die Essenz der deutschen Küche einfangen. Gleichzeitig experimentiert er gerne mit "
                            + "internationalen Einflüssen und bringt neue Geschmackskombinationen in seine Gerichte ein.\n"
                            + "Neben seiner Karriere als Autor und Fernsehmoderator ist Alfons Schubeck auch als Berater und Unternehmer tätig. "
                            + "Er betreibt mehrere Restaurants und gastronomische Unternehmen, in denen er seine kulinarische Expertise einbringt. "
                            + "Schubeck engagiert sich auch für die Ausbildung junger Talente und unterstützt sie dabei, ihre Kochfähigkeiten zu "
                            + "entwickeln und erfolgreich in der Gastronomiebranche Fuß zu fassen.");

            final Author author4 = new Author("Amelia", "Wolfgang",
                    "Amelia Wolfgang ist eine charismatische Köchin, Autorin und Botschafterin der guten Küche, die für ihre herzliche Art und "
                            + "ihre köstlichen Kreationen bekannt ist. Geboren am 15. Juli 1985 in Wien, hat Wolfgang ihre Leidenschaft für das"
                            + " Kochen zu einer Karriere gemacht, die die Herzen der Menschen berührt.\n"
                            + "Ihre Gerichte sind ein Ausdruck von Liebe und Fürsorge. Wolfgang versteht es, ihre Gäste mit ihrem Essen zu umar"
                            + "men und ein Gefühl von Wärme und Geborgenheit zu vermitteln. Ihre Kreationen zeichnen sich durch ausgewogene Aro"
                            + "men, raffinierte Präsentation und die Verwendung hochwertiger Zutaten aus, die sie mit Sorgfalt auswählt.\n"
                            + "Als Autorin hat Amelia Wolfgang ihre kulinarische Expertise in mehreren Kochbüchern geteilt. Ihre Bücher sind wi"
                            + "e eine Unterhaltung mit einer guten Freundin, die ihre Geheimnisse und Lieblingsrezepte teilt. Wolfgang ermutigt"
                            + " ihre Leser, die Freude am Kochen zu entdecken und ihre eigenen Geschmackserlebnisse zu schaffen.\n"
                            + "Wolfgang ist eine Verfechterin der regionalen Küche und unterstützt lokale Produzenten und Bauern. Sie setzt sic"
                            + "h für eine nachhaltige und bewusste Ernährung ein und fördert den respektvollen Umgang mit Lebensmitteln. Durch "
                            + "ihre Zusammenarbeit mit lokalen Gemeinschaften schafft sie eine Verbindung zwischen Menschen und ihrer Umgebung.\n"
                            + "Neben ihrer Karriere als Autorin und Köchin engagiert sich Amelia Wolfgang für wohltätige Zwecke und setzt ihr K"
                            + "önnen ein, um Bedürftigen zu helfen. Sie unterstützt Suppenküchen, organisiert Kochworkshops für benachteiligte "
                            + "Menschen und setzt sich für eine bessere Ernährungsbildung ein. Sie glaubt fest daran, dass Essen die Kraft hat, Leben zu verändern.\n"
                            + "Mit ihrer ansteckenden Begeisterung für gutes Essen und ihrer herzlichen Persönlichkeit hat Amelia Wolfgang eine"
                            + " Küche geschaffen, die nicht nur den Gaumen, sondern auch die Seele berührt. Sie lädt die Menschen ein, sich an "
                            + "den einfachen Freuden des Essens zu erfreuen und die Verbindung zwischen Nahrung und Glück zu entdecken.");
            final Author author5 = new Author("Johannes", "Bauer",
                    "Johannes Bauer ist ein aufstrebender österreichischer Koch, Autor und Genussenthusiast, "
                            + "der für seine opulenten Kochorgien bekannt ist. Geboren am 10. März 1985 in Salzburg, wuchs Bauer "
                            + "mit einer Leidenschaft für das Kochen und die Freude am Essen auf.\n"
                            + "Er hat sich einen Namen gemacht durch seine einzigartigen Kochorgien, bei denen er extravagante Gerichte "
                            + "mit einer Fülle von Geschmackskombinationen und Aromen kreiert. Bauers Küche ist eine Hommage an "
                            + "den Überfluss und die Sinnesfreude, bei der jedes Mahl zu einem unvergesslichen Erlebnis wird.\n"
                            + "Johannes Bauer hat sein Talent und seine Kreativität in seinen Kochbüchern festgehalten, die als "
                            + "inspirierende Anleitungen für kulinarische Genüsse dienen. Seine Bücher zeichnen sich durch detaillierte "
                            + "Rezeptbeschreibungen, ansprechende Food-Fotografie und seine persönlichen Geschichten rund um das Thema "
                            + "Kochen und Genießen aus.\n"
                            + "Als Verfechter der Esskultur und des gesellschaftlichen Zusammenseins inszeniert Johannes Bauer "
                            + "Kochorgien, die nicht nur dem Gaumen schmeicheln, sondern auch ein soziales Ereignis sind. Er lädt "
                            + "Gleichgesinnte ein, die sich an langen Tafeln versammeln und gemeinsam in eine Welt des Genusses und "
                            + "des Austauschs eintauchen.\n"
                            + "Jenseits seiner kulinarischen Aktivitäten setzt sich Johannes Bauer für nachhaltige Landwirtschaft "
                            + "und regionale Produzenten ein. Er legt großen Wert auf hochwertige Zutaten aus der Umgebung und unterstützt "
                            + "kleinere Erzeuger, um die Vielfalt der regionalen Küche zu bewahren und zu fördern.\n"
                            + "Mit seinen Kochorgien und seinem Streben nach opulentem Genuss hat Johannes Bauer eine ganz eigene "
                            + "Kochphilosophie geschaffen. Seine unkonventionellen Kreationen, seine Liebe zum Detail und seine "
                            + "Leidenschaft für das gute Leben haben ihm eine wachsende Anhängerschaft beschert, die sich gerne auf "
                            + "seine kulinarischen Abenteuer einlässt.");
            final Author author6 = new Author("Leonhard", "Patoschka",
                    "Leonhard Patoschka ist ein visionärer Koch, Autor und Künstler,"
                            + " der für sein außergewöhnliches musikalisches Gefühl in der Küche bekannt ist. Geboren am 15. September 1980 i"
                            + "n Wien, hat Patoschka eine einzigartige Verbindung zwischen Musik und kulinarischer Kreativität geschaffen.\n"
                            + "Als passionierter Musiker nutzt Patoschka seine tiefe Liebe zur Musik, um seine Kochkreationen zu inspirieren."
                            + " Jedes Gericht wird zu einer harmonischen Symphonie aus Aromen, Texturen und Farben, die die Sinne der Gäste verzaubern.\n"
                            + "Leonhard Patoschka hat seinen künstlerischen Ansatz in seinen Kochbüchern festgehalten, in denen er seine einz"
                            + "igartigen Rezepte und kreativen Prozesse teilt"
                            + "Patoschka ist bekannt für seine Fähigkeit, die emotionale Kraft der Musik in seinen Gerichten widerzuspiegeln."
                            + " Durch die Verwendung von Geschmackspaletten, die an verschiedene Musikgenres erinnern, schafft er eine einzig"
                            + "artige sensorische Erfahrung für seine Gäste. Seine Kreationen erzählen Geschichten und erzeugen eine tiefe Ve"
                            + "rbindung zwischen Essen, Musik und Emotionen.\n"
                            + "Neben seiner Karriere als Autor und Koch ist Leonhard Patoschka auch als Musiker aktiv und hat seine Leidensch"
                            + "aft für die Musik auf die Bühne gebracht. Er kombiniert Live-Performances mit kulinarischen Darbietungen und s"
                            + "chafft so einzigartige Events, bei denen Musik und Essen zu einer symbiotischen Erfahrung verschmelzen.\n"
                            + "Patoschka engagiert sich auch für soziale Projekte, bei denen er Musik und kulinarische Künste nutzt, um Mensc"
                            + "hen zusammenzubringen und positive Veränderungen in der Gesellschaft zu bewirken. Er glaubt daran, dass Essen "
                            + "und Musik universelle Sprachen sind, die Grenzen überwinden und Menschen verbinden können.\n"
                            + "Mit seinem besonderen musikalischen Gefühl und seinem künstlerischen Ansatz hat Leonhard Patoschka eine neue D"
                            + "imension des Kochens erschaffen. Seine Kreationen sind nicht nur kulinarische Meisterwerke, sondern auch Ausdr"
                            + "uck von Emotionen und Schönheit, die die Gäste auf eine einzigartige Sinnesreise mitnehmen.");
            final Author author7 = new Author("Marit", "Baumbier",
                    "Marit Baumbier ist eine inspirierende Köchin, Autorin und Naturverbun"
                            + "dene, die für ihre Verbindung zur Natur in ihrer Kochkunst bekannt ist. Geboren am 8. Juli 1987 in Bergen, Nor"
                            + "wegen, hat Baumbier eine tiefe Leidenschaft für die Natur und nachhaltiges Kochen entwickelt.\n"
                            + "Ihre Kreationen sind von den Schätzen der Natur inspiriert und sie verwendet bevorzugt frische, saisonale Zuta"
                            + "ten, die sie häufig selbst anbaut oder aus lokalen Quellen bezieht. Marit Baumbiers Küche ist geprägt von Einfa"
                            + "chheit und natürlicher Schönheit, wobei sie die Essenz der Zutaten zum Vorschein bringt.\n"
                            + "Als Autorin hat Marit Baumbier ihre Philosophie und ihre einzigartigen Rezepte in mehreren Kochbüchern geteilt."
                            + " Ihre Bücher sind nicht nur kulinarische Anleitungen, sondern auch eine Ode an die Natur und ihre Vielfalt. Si"
                            + "e ermutigt die Leser dazu, eine tiefere Verbindung zur Natur herzustellen und bewusster mit den Nahrungsmitteln umzugehen.\n"
                            + "Baumbier ist bekannt für ihre Experimentierfreude und ihre Fähigkeit, unkonventionelle Zutaten und Aromen mitei"
                            + "nander zu kombinieren. Sie kreiert Gerichte, die sowohl den Gaumen als auch die Sinne ansprechen und eine Reis"
                            + "e durch die natürlichen Geschmackswelten bieten.\n"
                            + "Neben ihrer Karriere als Autorin und Köchin engagiert sich Marit Baumbier aktiv für den Umweltschutz und die Na"
                            + "chhaltigkeit in der Gastronomie. Sie unterstützt lokale Bauern, Fischer und Produzenten und setzt sich für ein"
                            + "e verantwortungsbewusste und ressourcenschonende Nutzung der natürlichen Ressourcen ein.\n"
                            + "Mit ihrer Verbundenheit zur Natur und ihrer kreativen Kochkunst hat Marit Baumbier eine ganz eigene Kochphiloso"
                            + "phie entwickelt. Sie bringt die Schönheit der Natur auf den Teller und inspiriert Menschen dazu, bewusster zu "
                            + "essen und eine nachhaltige Beziehung zur Umwelt aufzubauen.");
            final Author author8 = new Author("Sebastian", "Müller",
                    "Sebastian Müller ist ein innovativer Koch, Autor und Visionär, der für seine"
                            + " bahnbrechenden kulinarischen Kreationen bekannt ist. Geboren am 12. Oktober 1983 in Berlin, hat Müller eine lei"
                            + "denschaftliche Liebe zur Kochkunst entwickelt und seine Karriere auf kreative Weise vorangetrieben.\n"
                            + "Seine Kreationen sind das Ergebnis einer unermüdlichen Suche nach neuen Aromen, Texturen und Geschmackskombinat"
                            + "ionen. Müller ist stets auf der Suche nach innovativen Techniken und Zutaten, um seine Gerichte auf einzigartige"
                            + " Weise zu präsentieren und den Gaumen seiner Gäste zu überraschen.\n"
                            + "Als Autor hat Sebastian Müller seine kulinarische Expertise in mehreren Kochbüchern geteilt. Seine Bücher sind"
                            + " nicht nur Rezeptsammlungen, sondern auch inspirierende Quellen für kulinarische Abenteuer. Müller ermutigt sein"
                            + "e Leser, über traditionelle Grenzen hinauszugehen und ihre eigenen kreativen Ausdrucksformen in der Küche zu finden.\n"
                            + "Müller ist bekannt für seine enge Zusammenarbeit mit lokalen Produzenten und Landwirten. Er legt großen Wert a"
                            + "uf hochwertige, nachhaltig angebaute Zutaten und unterstützt die Bauern in ihrer Arbeit. Durch diese Zusammenarbe"
                            + "it schafft er eine Verbindung zur Natur und verleiht seinen Gerichten eine authentische und saisonale Note.\n"
                            + "Neben seiner Karriere als Autor und Koch engagiert sich Sebastian Müller für soziale Projekte und setzt seine "
                            + "kulinarischen Fähigkeiten ein, um Menschen in Not zu helfen. Er organisiert Wohltätigkeitsveranstaltungen und Koc"
                            + "hkurse, bei denen er sein Wissen und seine Leidenschaft weitergibt, um das Leben anderer zu bereichern.\n"
                            + "Mit seiner Innovationskraft und seinem unersättlichen Durst nach kulinarischer Weiterentwicklung hat Sebastian "
                            + "Müller neue Maßstäbe in der Kochwelt gesetzt. Seine Kreationen sind kühn, kreativ und unvergesslich und inspirie"
                            + "ren andere Köche und Food-Enthusiasten dazu, ihre eigenen kulinarischen Grenzen zu erweitern.");
            final Author author9 = new Author("Carmen", "Wachter",
                    "Carmen Wachter ist eine leidenschaftliche Köchin, Autorin und Genussliebhaberin, d"
                            + "ie für ihre authentische und vielfältige Küche bekannt ist. Geboren am 3. März 1990 in Zürich, hat Wachter eine ti"
                            + "efe Liebe zur Kulinarik entwickelt und ihre Karriere als Kochkünstlerin vorangetrieben.\n"
                            + "Ihre Kreationen spiegeln eine Kombination aus Tradition und Innovation wider. Wachter schöpft Inspiration aus ih"
                            + "rer multikulturellen Herkunft und vereint Aromen und Zutaten aus verschiedenen Küchen der Welt zu harmonischen Ge"
                            + "richten. Sie versteht es, Geschmackserlebnisse zu schaffen, die die Sinne verführen und Emotionen wecken.\n"
                            + "Als Autorin hat Carmen Wachter ihre kulinarische Expertise in mehreren Kochbüchern geteilt. Ihre Bücher sind ein"
                            + "e Einladung, auf eine kulinarische Reise mitzukommen und die Fülle der verschiedenen Geschmackswelten zu entdecke"
                            + "n. Wachter ermutigt ihre Leser, mutig zu sein und ihre eigenen kulinarischen Experimente zu wagen, um wahre Genussmomente zu erleben.\n"
                            + "Wachter ist bekannt für ihre Hingabe zur Qualität der Zutaten. Sie legt großen Wert auf frische, hochwertige und"
                            + " nachhaltig angebaute Produkte. Carmen pflegt enge Beziehungen zu lokalen Bauern und Produzenten, um sicherzustel"
                            + "len, dass ihre Gerichte immer von bester Qualität sind.\n"
                            + "Neben ihrer Karriere als Autorin und Köchin engagiert sich Carmen Wachter für den kulinarischen Austausch und di"
                            + "e Förderung des Genusses in der Gesellschaft. Sie veranstaltet Kochkurse, Events und kulinarische Workshops, um M"
                            + "enschen zusammenzubringen und ihnen die Freude am Essen und gemeinsamen Genießen zu vermitteln.\n"
                            + "Mit ihrer Leidenschaft für kulinarische Vielfalt und ihrem Engagement für authentischen Genuss hat Carmen Walse"
                            + "r eine einzigartige Kochkunst geschaffen. Ihre Kreationen sind Ausdruck ihrer Persönlichkeit und laden die Mensch"
                            + "en ein, den Reichtum der kulinarischen Welt zu erkunden und zu genießen.");
            final Author author10 = new Author("Elias", "Morgenstern",
                    "Elias Morgenstern ist ein faszinierender Küchenkünstler, Autor und Geschichtenerzähler, der für seine magischen kulinarisch"
                            + "en Kreationen bekannt ist. Sein genaues Geburtsdatum ist unbekannt, denn es heißt, er sei aus den Nebeln der Zeit"
                            + " aufgetaucht, um die Welt der Gastronomie zu verzaubern.\n"
                            + "Seine Gerichte sind keine bloßen Speisen, sondern wahre Kunstwerke, die eine Geschichte erzählen. Morgenstern ver"
                            + "steht es, Zutaten und Aromen zu einer Symphonie der Sinne zu vereinen, bei der jeder Bissen eine emotionale Reise"
                            + " darstellt. Seine Kreationen sind geheimnisvoll, überraschend und lassen die Gäste in eine andere Welt eintauchen.\n"
                            + "Als Autor hat Elias Morgenstern seine einzigartige Vision der Kochkunst in mehreren mystischen Kochbüchern festge"
                            + "halten. Seine Bücher sind keine herkömmlichen Rezeptsammlungen, sondern erzählen Geschichten von verzauberten Zut"
                            + "aten, geheimen Techniken und den Geheimnissen hinter seinen spektakulären Gerichten. Morgenstern ermutigt seine L"
                            + "eser, ihre eigene kulinarische Fantasie zu entfesseln und die Magie in der Küche zu entdecken.\n"
                            + "Morgenstern ist bekannt für seine Suche nach außergewöhnlichen Zutaten und seinen unkonventionellen Zubereitungsm"
                            + "ethoden. Er streift durch verborgene Wälder, erkundet vergessene Märkte und taucht in die Tiefen der Ozeane ein, "
                            + "um die Schätze der Natur zu finden. Seine Kreationen sind geprägt von Exotik, Raffinesse und einer Prise geheimnisvoller Energie.\n"
                            + "Neben seiner Karriere als Autor und Küchenkünstler ist Elias Morgenstern ein Reisender und Entdecker. Er bereist "
                            + "die Welt, um neue Inspirationen zu finden und sich mit anderen Kulturen und ihren kulinarischen Traditionen zu ve"
                            + "rbinden. Er teilt sein Wissen und seine Leidenschaft in Workshops und Veranstaltungen, bei denen er die Menschen "
                            + "dazu anregt, ihre Sinne zu öffnen und die Faszination der Küche zu erleben.\n"
                            + "Mit seiner einzigartigen Persönlichkeit und seinem geheimnisvollen Charisma hat Elias Morgenstern die Kochwelt mi"
                            + "t seinem Zauber erfüllt. Seine Kreationen sind mehr als nur Speisen - sie sind Portale zu einer anderen Dimension"
                            + ", in der die Fantasie fliegt und die Sinne berauscht werden.");

            final Author author11 = new Author("Joshua", "Weissman",
                "Joshua Weissman ist ein talentierter Koch und eine bekannte Persönlichkeit in der kulinarischen Welt. Er ist für seine leidenschaftliche Art des Kochens und seine Fähigkeit, komplexe"
                    + " Rezepte in verständliche Schritte zu zerlegen, bekannt. Mit seinem charismatischen Auftreten und seiner erfrischenden Herangehensweise hat er sich einen großen Namen auf YouTube und in den sozialen Medien gemacht.\n"
                    + "\n"
                    + "Joshua Weissman begann seine kulinarische Reise schon früh und hat im Laufe der Jahre ein breites Repertoire an Kochtechniken und -stilen entwickelt. Er ist bekannt für seine Fähigkeit, "
                    + "sowohl traditionelle als auch moderne Küche zu beherrschen und dabei seinen eigenen einzigartigen Twist einzubringen.\n"
                    + "\n"
                    + "Was Joshua Weissman besonders auszeichnet, ist seine Fähigkeit, komplexe Gerichte zu vereinfachen und sie für jedermann zugänglich zu machen. "
                    + "Seine Videos sind nicht nur informativ, sondern auch unterhaltsam, und er vermittelt sein Wissen und seine Leidenschaft für das Kochen auf eine mitreißende Art und Weise.\n"
                    + "\n"
                    + "Joshua Weissman hat eine große Online-Community aufgebaut, die ihn für seine Expertise, Kreativität und seine ermutigende Art bewundert. "
                    + "Sein Engagement für hochwertige Zutaten, handwerkliche Techniken und das Streben nach kulinarischer Exzellenz machen ihn zu einem beliebten Vorbild für Hobbyköche und Food-Enthusiasten weltweit.\n"
                    + "\n"
                    + "Insgesamt ist Joshua Weissman eine inspirierende Figur in der Kochwelt, die ihre Leidenschaft für gutes Essen teilt und Menschen dazu ermutigt,"
                    + " ihre Fähigkeiten in der Küche zu entwickeln und auszuprobieren. Mit seinem Talent und seinem einnehmenden Charakter wird er sicherlich weiterhin die Herzen und Gaumen von Menschen auf der ganzen Welt erobern.");

            imageRepository.save(image1);
            imageRepository.save(image2);
            imageRepository.save(image3);
            imageRepository.save(image4);
            imageRepository.save(image5);
            imageRepository.save(image6);
            imageRepository.save(image7);
            imageRepository.save(image8);
            imageRepository.save(image9);
            imageRepository.save(image10);

            author1.setImageId(image1.getId());
            author2.setImageId(image2.getId());
            author3.setImageId(image3.getId());
            author4.setImageId(image4.getId());
            author5.setImageId(image5.getId());
            author6.setImageId(image6.getId());
            author7.setImageId(image7.getId());
            author8.setImageId(image8.getId());
            author9.setImageId(image9.getId());
            author10.setImageId(image10.getId());

            authorRepository.save(author10);
            authorRepository.save(author9);
            authorRepository.save(author8);
            authorRepository.save(author6);
            authorRepository.save(author7);
            authorRepository.save(author5);
            authorRepository.save(author4);
            authorRepository.save(author3);
            authorRepository.save(author2);
            authorRepository.save(author1);
            authorRepository.save(author11);
        }
    }
}
