import { Pipe, PipeTransform } from '@angular/core';
import { WineCategory } from '../dtos/wine';

@Pipe({
  name: 'wineCategoryInfoPipe'
})
export class WineCategoryInfoPipePipe implements PipeTransform {

  transform(value: WineCategory, ...args: unknown[]): string {
    switch(value){
      case WineCategory.sparkling: {
        return 'Schaumweine sind prickelnde und erfrischende Getränke, die oft mit besonderen ' +
        'Anlässen und Feierlichkeiten in Verbindung gebracht werden. Sie zeichnen sich durch ihr' +
        'e lebendige Kohlensäure und ihre eleganten Perlage aus, die sie zu einer beliebten Wahl' +
        ' für festliche Momente machen.\n' +
        'Der bekannteste Schaumwein ist zweifellos Champagner, der aus der gleichnamigen Region ' +
        'in Frankreich stammt. Champagner wird nach der traditionellen Methode hergestellt, bei ' +
        'der die zweite Gärung in der Flasche erfolgt. Dadurch entsteht die charakteristische Fei' +
        'nperligkeit und die vielschichtigen Aromen, die Champagner so einzigartig machen.\n' +
        'Neben Champagner gibt es jedoch auch andere Schaumweine, die eine hervorragende Alte' +
        'rnative darstellen. Prosecco aus Italien beispielsweise ist ein spritziger Schaumwei' +
        'n, der oft als Aperitif serviert wird. Er ist bekannt für seine fruchtigen Aromen vo' +
        'n grünen Äpfeln und Zitrusfrüchten.\n' +
        'Ein weiterer beliebter Schaumwein ist der Cava aus Spanien. Er wird nach der traditi' +
        'onellen Methode hergestellt und bietet ein hervorragendes Preis-Leistungs-Verhältnis' +
        '. Cava zeichnet sich durch sein frisches und lebendiges Geschmacksprofil aus, das No' +
        'ten von weißen Blüten und Zitrusfrüchten umfasst.\n' +
        'Auch außerhalb Europas gibt es Schaumweine, die Aufmerksamkeit verdienen. Zum Beispiel' +
        ' kommt der Sekt aus Deutschland, der nach der Methode der Flaschengärung hergestellt w' +
        'ird. Er bietet eine breite Vielfalt an Geschmacksrichtungen, von trocken bis süß, und ' +
        'wird sowohl als Aperitif als auch zum Anstoßen auf besondere Ereignisse genossen.\n' +
        'Egal, ob Champagner, Prosecco, Cava oder Sekt, Schaumweine sind eine wunderbare Ergänz' +
        'ung zu festlichen Anlässen. Ihr prickelnder Charakter und ihre vielfältigen Geschmacksp' +
        'rofile machen sie zu einer beliebten Wahl, um besondere Momente zu feiern und das Leben' +
        ' zu genießen. Prost!';
      };
      case WineCategory.lightWhite: {
        return 'Leichte Weißweine sind erfrischende und zugängliche Getränke, die' +
        ' sich durch ihre helle Farbe und ihre leichten, subtilen Aromen auszeichnen. ' +
        'Sie sind eine großartige Wahl für warme Sommertage oder als Begleitung zu leichten Speisen.\n' +
        'Ein Beispiel für einen leichten Weißwein ist der Sauvignon Blanc. Dieser populäre' +
        ' Wein stammt ursprünglich aus dem französischen Loiretal, wird aber heutzutage in v' +
        'ielen Weinregionen auf der ganzen Welt angebaut. Der Sauvignon Blanc zeichnet sich d' +
        'urch seine knackige Säure, seine frischen Aromen von Zitrusfrüchten, Gras und Stache' +
        'lbeeren sowie seinen leichten Körper aus.\n' +
        'Ein weiterer leichter Weißwein, der an Beliebtheit gewonnen hat, ist der Pinot Grigio' +
        '. Ursprünglich aus Italien stammend, wird er mittlerweile in vielen Weinregionen, wi' +
        'e zum Beispiel in den USA und Deutschland, angebaut. Pinot Grigio ist für seinen mil' +
        'den Geschmack bekannt, der Noten von Birnen, Äpfeln und Zitrusfrüchten aufweist. Er ' +
        'ist ein äußerst vielseitiger Wein, der gut zu leichten Gerichten wie Salaten, Fisch o' +
        'er Meeresfrüchten passt\n' +
        'Ein dritter leichter Weißwein, der erwähnenswert ist, ist der Riesling. Riesling-Wein' +
        'e werden in vielen Teilen der Welt hergestellt, aber sie sind besonders bekannt für ' +
        'ihre Herkunft aus Deutschland. Riesling ist für seine lebendige Säure, sein fruchtige' +
        's Aroma und seine Fähigkeit, sowohl trocken als auch lieblich zu sein, bekannt. Dieser' +
        ' Wein passt gut zu einer Vielzahl von Gerichten, von asiatischen Küchen bis hin zu w' +
        'ürzigen Speisen.\n' +
        'Leichte Weißweine sind eine hervorragende Wahl für Weinliebhaber, die einen erfrische' +
        'nden und zugänglichen Wein suchen. Mit ihrer hellen Farbe, den subtilen Aromen und ihr' +
        'er angenehmen Säure bieten sie eine erfrischende Alternative, die sich gut zum Entspanne' +
        'n oder zur Begleitung von leichten Speisen eignet. Egal, ob Sie sich für Sauvignon Blanc,' +
        ' Pinot Grigio oder Riesling entscheiden, diese Weine sind eine wunderbare Wahl für sommerli' +
        'che Genussmomente. Cheers!';
      };
      case WineCategory.fullWhite: {
        return 'Volle Weißweine sind reichhaltige und körperreiche Weine, die mit ihrer ' +
        'intensiven Aromatik und Komplexität beeindrucken. Diese Weine zeichnen sich durch ' +
        'ihre tiefe goldene Farbe, ihre cremige Textur und ihre geschmacksintensive Präsenz' +
        ' aus. Sie eignen sich ideal für Genießer, die kräftige und vollmundige Weine bevorzugen.\n' +
        'Ein herausragendes Beispiel für einen vollen Weißwein ist der Chardonnay. Diese Rebs' +
        'orte ist weltweit verbreitet und hat ihren Ursprung in der französischen Weinregion' +
        ' Burgund. Chardonnay-Weine können je nach Anbau- und Ausbauweise unterschiedliche A' +
        'romen entwickeln, von tropischen Früchten wie Ananas und Mango bis hin zu Butterscot' +
        'ch, Toast oder Vanille. Sie sind oft im Holzfass gereift, was ihnen zusätzliche Komp' +
        'lexität und Struktur verleiht.\n' +
        'Ein weiterer voller Weißwein, der an Beliebtheit gewonnen hat, ist der Viognier. Dies' +
        'e Rebsorte stammt ursprünglich aus dem Rhônetal in Frankreich und hat eine reiche Ar' +
        'omatik von Pfirsichen, Aprikosen, Blüten und Gewürzen. Viognier-Weine sind oft vollmu' +
        'ndig und können eine leicht ölige Textur haben, die dem Gaumen eine angenehme Fülle verleiht.\n' +
        'Gewürztraminer ist eine weitere Rebsorte, die für ihre vollen Weißweine bekannt ist.' +
        ' Ursprünglich aus dem Elsass stammend, bietet der Gewürztraminer eine intensive Ar' +
        'omenvielfalt, die an Rosenblüten, Litschi, Gewürze und exotische Früchte erinnert. ' +
        'Die Weine haben eine kräftige Textur und einen süßen Charakter, der gut zu würzige' +
        'n Gerichten oder kräftigem Käse passt.\n' +
        'Volle Weißweine sind eine ausgezeichnete Wahl für Weinliebhaber, die nach intensi' +
        'ven Geschmackserlebnissen suchen. Mit ihrer reichen Aromatik, ihrer komplexen St' +
        'ruktur und ihrer Fülle bieten sie eine Genussreise für den Gaumen. Ob Chardonnay,' +
        ' Viognier oder Gewürztraminer, diese Weine sind perfekt geeignet, um einen besond' +
        'eren Anlass zu feiern oder ein reichhaltiges kulinarisches Erlebnis zu begleiten. Prost!';
      };
      case WineCategory.aromaticWhite: {
        return 'Aromatische Weißweine sind faszinierende und duftende Weine, ' +
        'die für ihre intensiven Aromen und ausgeprägte Fruchtigkeit bekannt sind' +
        '. Sie zeichnen sich durch eine breite Palette an Aromen aus, die von bl' +
        'umigen und fruchtigen Noten bis hin zu exotischen Gewürzen reichen. Di' +
        'ese Weine bieten ein olfaktorisches Erlebnis und sind eine wunderbare ' +
        'Wahl für Weinliebhaber, die nach intensiven sensorischen Eindrücken suchen.\n' +
        'Ein herausragendes Beispiel für einen aromatischen Weißwein ist der Gewü' +
        'rztraminer. Ursprünglich aus dem Elsass stammend, präsentiert sich diese' +
        'r Wein mit einem üppigen Bukett von Rosenblüten, Litschi, Gewürzen und ex' +
        'otischen Früchten. Der Gewürztraminer hat oft einen charakteristisch inte' +
        'nsiven Duft und eine kräftige Geschmacksfülle, die ihn zu einem idealen ' +
        'Begleiter für würzige Gerichte macht.\n' +
        'Ein weiterer aromatischer Weißwein, der viel Beachtung findet, ist der M' +
        'oscato. Die Moscatotraube erzeugt Weine mit einem unverwechselbaren blum' +
        'igen Aroma, das an Rosen, Orangenblüten und Trauben erinnert. Diese Wein' +
        'e sind oft halbtrocken bis süß und haben eine leichte Spritzigkeit. Mosca' +
        'to-Weine sind perfekt als Aperitif oder als Begleitung zu fruchtigen Dess' +
        'erts geeignet.\n' +
        'Riesling ist eine weitere Rebsorte, die für ihre aromatischen Weißweine ' +
        'bekannt ist. Riesling-Weine können je nach Anbaugebiet und Reifegrad ei' +
        'ne breite Palette von Aromen aufweisen, von Zitrusfrüchten und grünen Ä' +
        'pfeln bis hin zu mineralischen Noten und blumigen Nuancen. Die charakte' +
        'ristische lebendige Säure des Rieslings trägt zur Frische und Ausdruck' +
        'skraft des Weins bei.\n' +
        'Aromatische Weißweine sind eine spannende Wahl für Weinliebhaber, die ' +
        'nach einer intensiven Aromenvielfalt suchen. Mit ihren betörenden Düf' +
        'ten und ihrer Geschmacksfülle bieten sie ein sensorisches Erlebnis, d' +
        'as die Sinne erfreut. Ob Gewürztraminer, Moscato oder Riesling, dies' +
        'e Weine eignen sich hervorragend, um exotische Geschmackskombinatione' +
        'n zu erkunden und besondere Genussmomente zu erleben. Cheers!';
      };
      case WineCategory.rose: {
        return 'Rosé-Weine sind erfrischende und vielseitige Getränke, die durch ihre' +
        ' zarte Farbe und ihren fruchtigen Geschmack begeistern. Sie bieten eine gelunge' +
        'ne Kombination aus den fruchtigen Aromen von Rotwein und der Frische von Weißw' +
        'ein. Rosé-Weine sind eine beliebte Wahl für den Sommer und eignen sich hervorr' +
        'agend, um gemütliche Momente im Freien zu genießen.\n' +
        'Die Herstellung von Rosé-Wein erfolgt durch eine kurze Mazeration der Traubensc' +
        'halen mit dem Most. Dieser Prozess verleiht dem Wein seine charakteristische ro' +
        'safarbene Nuance. Je nach Traubensorte und Herstellungsmethode kann die Farbpal' +
        'ette von blassrosa bis zu kräftigem Pink variieren.\n' +
        'Rosé-Weine können unterschiedliche Geschmacksprofile aufweisen, von trocken und' +
        ' fruchtig bis hin zu halbtrocken oder lieblich. Sie bieten eine breite Auswahl a' +
        'n Aromen, darunter Erdbeeren, Himbeeren, rote Johannisbeeren und Grapefruit. Di' +
        'e leichte bis mittlere Säure verleiht ihnen eine erfrischende Note und macht sie' +
        ' zu einem angenehmen Begleiter für leichtere Gerichte.\n' +
        'Rosé-Weine werden weltweit produziert und haben ihre Ursprünge in Regionen wie ' +
        'der Provence in Frankreich, wo sie als traditionelle Sommerweine bekannt sind' +
        '. Italienische Rosatos, spanische Rosados und amerikanische Rosés sind ebenfa' +
        'lls populäre Varianten, die mit ihrer eigenen Charakteristik und regionalen B' +
        'esonderheiten überzeugen.\n' +
        'Rosé-Weine sind vielseitige Begleiter zu einer Vielzahl von Gerichten. Sie p' +
        'assen wunderbar zu leichten Salaten, Fisch und Meeresfrüchten, gegrilltem Ge' +
        'müse oder frischem Ziegenkäse. Sie sind auch eine ausgezeichnete Wahl für i' +
        'nformelle Zusammenkünfte oder als Aperitif an warmen Sommertagen.\n' +
        'Mit ihrer zarten Farbe, den fruchtigen Aromen und ihrer erfrischenden Natu' +
        'r sind Rosé-Weine die perfekte Wahl für all jene, die leichte und geschmack' +
        'volle Weine bevorzugen. Egal, ob Sie einen trockenen Rosé aus der Provence, ' +
        'einen fruchtigen Rosato aus Italien oder einen knackigen Rosado aus Spanien ' +
        'wählen, diese Weine sind ideal, um das Leben zu feiern und den Sommer in vol' +
        'len Zügen zu genießen. Prost!';
      };
      case WineCategory.lightRed: {
        return 'Leichte Rotweine sind erfrischende und zugängliche Weine, die sich durch' +
        ' ihre helle Farbe, ihre lebendige Säure und ihre sanften Tannine auszeichnen. Sie' +
        ' sind eine großartige Wahl für Weinliebhaber, die leichte und fruchtige Weine bevo' +
        'rzugen oder nach einem angenehmen Rotweinerlebnis suchen, ohne die Schwere oder In' +
        'tensität von kräftigen Rotweinen.\n' +
        'Ein Beispiel für einen leichten Rotwein ist der Pinot Noir. Diese Rebsorte ist beka' +
        'nnt für ihre elegante und zarte Natur. Pinot Noir-Weine haben eine helle bis mittl' +
        'ere Farbtiefe und präsentieren eine feine Aromenvielfalt von roten Beeren, Kirschen' +
        ' und Gewürzen. Sie sind leicht im Körper, haben eine lebendige Säure und weiche Tan' +
        'nine, was sie zu einem angenehm trinkbaren und vielseitigen Wein macht.\n' +
        'Ein weiterer leichter Rotwein, der an Beliebtheit gewonnen hat, ist der Gamay' +
        '. Dieser Wein stammt hauptsächlich aus der französischen Region Beaujolais un' +
        'd ist für seinen frischen und fruchtigen Charakter bekannt. Gamay-Weine bieten' +
        ' lebhafte Aromen von roten Früchten wie Erdbeeren und Himbeeren, eine gute Säur' +
        'estruktur und seidige Tannine. Sie sind leicht im Körper und perfekt für leichte' +
        ' Speisen oder als leichter Rotwein zum Entspannen geeignet.\n' +
        'Ein dritter leicher Rotwein, der erwähnenswert ist, ist der Bardolino aus Italie' +
        'n. Dieser Wein wird hauptsächlich aus den Rebsorten Corvina und Rondinella herg' +
        'estellt und zeichnet sich durch seine helle rubinrote Farbe und sein fruchtiges ' +
        'Aroma von Kirschen, Beeren und Kräutern aus. Bardolino-Weine sind leicht im Körp' +
        'er, haben eine angenehme Säure und weiche Tannine, was sie zu einer großartigen' +
        ' Wahl für den täglichen Genuss macht.\n' +
        'Leichte Rotweine bieten eine wunderbare Alternative zu schweren und tanninreich' +
        'en Rotweinen. Mit ihrer hellen Farbe, ihren fruchtigen Aromen und ihrer zugängl' +
        'ichen Natur sind sie vielseitig einsetzbar. Ob Pinot Noir, Gamay oder Bardolino' +
        ', diese leichten Rotweine sind perfekt für entspannte Abende, gesellige Zusamme' +
        'nkünfte und die Begleitung von leichten Gerichten. Genießen Sie die Leichtigkei' +
        't und Frische dieser Weine. Zum Wohl!';
      };
      case WineCategory.middleRed: {
        return 'Mittelschwere Rotweine bieten eine gelungene Balance zwischen Frische und Struktur, wodurch sie v' +
        'ielseitige Begleiter für unterschiedliche Gelegenheiten sind. Sie zeichnen sich durch eine tiefere Farbe, ei' +
        'nen volleren Körper und ein breiteres Spektrum an Aromen aus. Diese Weine sind eine beliebte Wahl für Weinl' +
        'iebhaber, die nach einer gewissen Tiefe und Komplexität suchen, ohne dabei die Schwere und Intensität von k' +
        'räftigen Rotweinen zu erreichen.\n' +
        'Ein Beispiel für einen mittelschweren Rotwein ist der Merlot. Diese Rebsorte ist bekannt für ihre weichen T' +
        'annine, ihre moderate Säure und ihren fruchtigen Charakter. Merlot-Weine bieten eine Fülle von Aromen, darun' +
        'ter reife rote und dunkle Früchte wie Kirschen, Pflaumen und Beeren, sowie subtile Gewürz- und Schokoladenn' +
        'oten. Sie haben oft einen mittelkräftigen Körper und eine angenehme Geschmeidigkeit, was sie zu einem viels' +
        'eitigen Begleiter für eine Vielzahl von Gerichten macht.\n' +
        'Ein weiterer mittelschwerer Rotwein ist der Sangiovese. Diese Rebsorte ist die Hauptkomponente vieler tos' +
        'kanischer Weine, wie dem Chianti. Sangiovese-Weine zeichnen sich durch ihre lebhafte Säure, ihre herzhaften' +
        ' Aromen von Kirschen, roten Beeren, Gewürzen und einer erdigen Note aus. Sie haben oft eine mittlere bis voll' +
        'ere Struktur, moderate Tannine und einen charakteristischen Geschmack, der gut zu italienischer Küche passt. ' +
        'Der Grenache ist eine weitere Rebsorte, die mittelschwere Rotweine hervorbringt. Diese Traube wird weltweit ' +
        'angebaut und bietet eine breite Aromenvielfalt, von roten und dunklen Früchten bis hin zu würzigen Noten und' +
        ' einem Hauch von Kräutern. Grenache-Weine haben oft einen mittelkräftigen Körper, weiche Tannine und eine an' +
        'genehme Fruchtfülle, die sie zu einer idealen Wahl für den täglichen Genuss machen.\n' +
        'Mittelschwere Rotweine sind vielseitige Begleiter zu verschiedenen Gerichten. Sie passen gut zu gegrilltem ' +
        'Fleisch, Pasta, reifem Käse oder auch zu einem entspannten Abend mit Freunden. Mit ihrer tiefen Farbe, ihre' +
        'n vielschichtigen Aromen und ihrer ausgewogenen Struktur bieten sie ein angenehmes und zugängliches Weinerleb' +
        'nis. Egal ob Merlot, Sangiovese oder Grenache, diese mittelschweren Rotweine sind perfekt, um den Genuss von' +
        ' Rotwein in seiner ganzen Vielfalt zu erkunden. Prost!';
      };
      case WineCategory.fullRed: {
        return 'Volle Rotweine sind kraftvolle und intensiv-aromatische Weine, die ' +
        'durch ihre tiefe Farbe, ihre reiche Struktur und ihre komplexe Aromenvielfa' +
        'lt beeindrucken. Sie sind ideal für Weinliebhaber, die kräftige und geschmack' +
        'sintensive Weine mit einer beeindruckenden Tiefe suchen. Diese Weine sind of' +
        't reich an Tanninen und bieten eine lange Lagerfähigkeit.\n' +
        'Ein herausragendes Beispiel für einen vollen Rotwein ist der Cabernet Sauvi' +
        'gnon. Diese weltweit bekannte Rebsorte erzeugt Weine mit einer tiefen, dunkl' +
        'en Farbe und einer komplexen Aromatik von schwarzen Johannisbeeren, schwarz' +
        'en Kirschen, Zedernholz und Gewürzen. Cabernet Sauvignon-Weine haben oft ei' +
        'nen kräftigen Körper, eine ausgeprägte Tanninstruktur und eine bemerkenswerte' +
        ' Langlebigkeit. Sie sind ideal, um kräftige Fleischgerichte oder reifen Käse ' +
        'zu begleiten.\n' +
        'Ein weiterer voller Rotwein ist der Syrah, auch Shiraz genannt. Diese Rebs' +
        'orte erzeugt kraftvolle und würzige Weine mit einer dunklen Farbe und eine' +
        'r intensiven Aromenvielfalt von dunklen Beeren, Pfeffer, schwarzen Oliven ' +
        'und geräuchertem Fleisch. Syrah-Weine haben oft einen vollen Körper, eine h' +
        'ohe Tanninstruktur und eine bemerkenswerte Tiefe. Sie sind ideal, um kräfti' +
        'ge Gerichte wie Wild oder gegrilltes Fleisch zu begleiten.\n' +
        'Ein dritter voller Rotwein, der Erwähnung verdient, ist der Malbec. Diese ' +
        'Rebsorte stammt ursprünglich aus Frankreich, hat aber in Argentinien große' +
        ' Bekanntheit erlangt. Malbec-Weine sind dunkelviolett, vollmundig und reic' +
        'h an Aromen von schwarzen Kirschen, Pflaumen, Schokolade und Gewürzen. Sie ' +
        'haben eine samtige Textur, gut eingebundene Tannine und eine bemerkenswerte ' +
        'Geschmacksfülle. Sie sind ideal für Grillabende oder als Begleitung zu deft' +
        'igen Gerichten.\n' +
        'Volle Rotweine bieten ein intensives und genussvolles Geschmackserlebnis' +
        '. Mit ihrer tiefen Farbe, ihrer komplexen Aromatik und ihrer kraftvollen' +
        ' Struktur sind sie perfekt für besondere Anlässe oder um die Sinne zu v' +
        'erwöhnen. Egal ob Cabernet Sauvignon, Syrah oder Malbec, diese vollen R' +
        'otweine sind ideal, um eine bemerkenswerte Gaumenreise zu erleben. Geni' +
        'eßen Sie die Kraft und Intensität dieser Weine. Zum Wohl!';
      };
      case WineCategory.dessert: {
        return 'Dessert- und Süßweine sind köstliche Begleiter für den krönenden Absc' +
        'hluss einer Mahlzeit oder als eigenständiges Genusserlebnis. Sie zeichnen sich ' +
        'durch ihre süße Geschmacksfülle und ihre faszinierende Aromenvielfalt aus, die ' +
        'von fruchtigen Nuancen bis hin zu reichhaltigen, karamellisierten Noten reicht.' +
        ' Diese Weine sind eine wunderbare Wahl für Weinliebhaber, die nach einer süßen V' +
        'ersuchung suchen.\n' +
        'Ein klassisches Beispiel für einen Dessertwein ist der Sauternes aus der Regi' +
        'on Bordeaux in Frankreich. Sauternes-Weine werden aus edelfaulen Trauben herge' +
        'stellt, die von dem Edelpilz Botrytis cinerea befallen sind. Dieser Pilz konz' +
        'entriert den Zucker in den Trauben und verleiht dem Wein eine unglaubliche Süß' +
        'e und Aromenvielfalt. Sauternes-Weine präsentieren komplexe Aromen von Honig,' +
        ' Aprikose, Pfirsich, exotischen Früchten und einer angenehmen Säure, die für e' +
        'ine ausgewogene Süße sorgt.\n' +
        'Ein weiterer Dessertwein, der große Aufmerksamkeit erregt, ist der Eiswein.' +
        ' Eisweine werden aus Trauben hergestellt, die spät im Winter geerntet werden' +
        ', wenn sie gefroren sind. Durch diesen Prozess werden die Wassermoleküle in' +
        ' den Trauben eingefroren und bei der Pressung wird nur der konzentrierte Sa' +
        'ft extrahiert. Eisweine sind für ihre intensive Süße, ihr volles Aroma von' +
        ' tropischen Früchten, Honig und Gewürzen sowie ihre frische Säurestruktur b' +
        'ekannt. Sie sind eine perfekte Ergänzung zu fruchtigen Desserts, Obstkuchen' +
        ' oder Blauschimmelkäse.\n' +
        'Ein dritter süßer Wein, der Erwähnung verdient, ist der Portwein. Portwein ' +
        'stammt aus Portugal und wird in der Regel aus einer Mischung von Rebsorten ' +
        'hergestellt, darunter Touriga Nacional, Tinta Roriz und Touriga Franca. Port' +
        'wein hat einen hohen Alkoholgehalt, eine reiche Süße und eine komplexe Aro' +
        'matik von dunklen Beeren, getrockneten Früchten, Schokolade und Gewürzen. E' +
        'r kann in verschiedenen Stilen, von trocken bis süß, hergestellt werden und i' +
        'st eine beliebte Wahl, um den Abend in Gesellschaft eines guten Gesprächs un' +
        'd einer Auswahl von Käse oder Schokolade ausklingen zu lassen.\n' +
        'Dessert- und Süßweine bieten ein wunderbares Geschmackserlebnis für Nas' +
        'chkatzen und Weinliebhaber. Mit ihrer süßen Fülle, ihren komplexen Arome' +
        'n und ihrer ausgewogenen Struktur sind sie die perfekte Ergänzung zu köst' +
        'lichen Desserts oder um einen genussvollen Moment für sich selbst zu scha' +
        'ffen. Egal ob Sauternes, Eiswein oder Portwein, diese süßen Weine sind ' +
        'ideal, um den Abschluss einer Mahlzeit in vollen Zügen zu genießen. Cheers!';
      };
      default: return '';
    }
  }

}
