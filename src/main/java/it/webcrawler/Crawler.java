package it.webcrawler;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import it.webcrawler.extractor.DocumentDataExtractor;

public class Crawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|mp3|mp3|zip|gz))$");
	private final static Pattern SPECIFIC_DOCUMENT_MODEL_URL = Pattern.
			compile("https\\:\\/\\/www\\.consilium\\.europa\\.eu\\/prado\\/en\\/([a-z]{3})-[a-z]{2}-[0-9]+\\/index\\.html");
	private final static String DOCUMENTS_URL = "https://www.consilium.europa.eu/prado/en/prado-documents/";

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && (href.startsWith(DOCUMENTS_URL) || SPECIFIC_DOCUMENT_MODEL_URL.matcher(href).matches());
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL().toLowerCase();

		if (SPECIFIC_DOCUMENT_MODEL_URL.matcher(url).matches() && page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String htmlPage = htmlParseData.getHtml();
			new DocumentDataExtractor().extract(htmlPage);
		}
	}


	public static void main(String[] args) throws Exception {

		/*
		 * Configurazione della cartella del db
		 */
		CrawlConfig config = new CrawlConfig();
		Properties properties = new Properties();
		properties.load(new FileInputStream("src/main/resources/config.properties"));
		config.setCrawlStorageFolder(properties.getProperty("STORAGE_FOLDER"));

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

		controller.addSeed(properties.getProperty("SEED_URL")); // da cambiare il seed

		/*
		 * Avvia 1 crawler in modo bloccante, ovvero il main termina solamente
		 * quando il processo di crawling termina.
		 */
		controller.start(Crawler.class, 1);

	}
}
