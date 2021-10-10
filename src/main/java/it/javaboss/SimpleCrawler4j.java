package it.javaboss;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class SimpleCrawler4j extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|mp3|mp3|zip|gz))$");
	private final static Pattern SPECIFIC_DOCUMENT_MODEL_URL = Pattern.compile("https\\:\\/\\/www\\.consilium\\.europa\\.eu\\/prado\\/en\\/([a-z]{3})-[a-z]{2}-[0-9]+\\/index\\.html");

	private final static String URL = "https://www.consilium.europa.eu/prado/en/prado-documents/";

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && (href.startsWith(URL) || SPECIFIC_DOCUMENT_MODEL_URL.matcher(href).matches());
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL().toLowerCase();

		if (SPECIFIC_DOCUMENT_MODEL_URL.matcher(url).matches() && page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String data = htmlParseData.getHtml();
			new SimpleExecutor().storeOperation(new ExtractDocumentDataOperation(new DocumentDataExtractor(), data));
		}
	}


	public static void main(String[] args) throws Exception {

		/*
		 * Configurazione della cartella del db
		 */
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder("C:\\Users\\Filippo-pc\\Desktop\\Cose\\Università\\Ths\\Codice\\temp");

		/*
		 * Inizializzazione del controller per il crawling.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * I seed url sono le prime pagine visitate. Ogni altra pagina
		 * e' ottenuta seguento i link nelle pagine visitate.
		 */

		String url = "https://www.consilium.europa.eu/prado/en/prado-documents/EUE/index.html";

		controller.addSeed(url); // da cambiare il seed

		/*
		 * Avvia 1 crawler in modo bloccante, ovvero il main termina solamente
		 * quando il processo di crawling termina.
		 */
		controller.start(SimpleCrawler4j.class, 1);



	}
}
