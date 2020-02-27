
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BestBuy {
    private static List<String> products = new ArrayList<>();

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "d:\\Driver\\chromedriver.exe");
//        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.navigate().to("https://www.bestbuy.com/");
        driver.findElement(By.cssSelector("img[alt='United States']")).click();

        BestBuy ph = new BestBuy();
        ph.createProperties(driver, "D:\\java\\Links.txt", "D:\\java\\Fields7.txt");  //пишет файл с списком характеристик и через тире значение...

//        ph.createSetOfFields(driver, "D:\\java\\Links.txt", "D:\\java\\Fields.txt");  // пишет файлик только со списком характеристик...
/**
 * следующие пять строк запускают код, который собирает все ссылки на все товары
 * в выбранной подкатегории и парсит название характеристики и ее значение в Мапу,
 * после чего собирает все Мапы с Сэт.
 */
//        ph.getPages(driver, "https://www.bestbuy.com/site/ipad-tablets-ereaders/tablets/pcmcat209000050008.c?cp=1&id=pcmcat209000050008&intl=nosplash");
//        Set<Map<String, String>> items = new HashSet<>();
//        for (String product : products) {
//            items.add(ph.parseProduct(driver, product));
//        }

        driver.quit();
    }

    /**
     *
     * Создаем Сэт со списком уникальных значений характеристик товаров,
     * создаем список всех возможных характеристик на основе прочтения каждого товара в нашей подкатегории.
     *
     * @param driver
     * @param source
     * @param path
     */

    private void createSetOfFields(WebDriver driver, String source, String path) {
        Set<String> set = new TreeSet<>();
        for (String readLink : readLinks(source)) {
            driver.get(readLink);
            List<WebElement> elements = driver.findElements(By.cssSelector("div.title-container.col-xs-6.v-fw-medium"));
            for (WebElement element : elements) {
                set.add(element.getAttribute("textContent").trim().replace("Info", ""));
            }
        }
        writeLinks(set, path);
    }

    /**
     * метод создает Map с ключами в виде всех возможных характеристик товара на основе просмотра всех товаров в подкатегории
     * и сохраняет в общий список, значениями в Map выступают реальные значения поля характеристик. Метод не возвращает Мап,
     * а распечатывает весь список.
     *
     * @param driver наш веб драйвер передается как параметр
     * @param source txt текстовый файл с ссылками на каждый товар (парсится заранее)
     * @param path сюда записывается результат
     */
    private void createProperties(WebDriver driver, String source, String path){
        Map<String, String> map = new TreeMap<>();
        for (String readLink : readLinks(source)) {
            driver.get(readLink);
            List<WebElement> fieldName = driver.findElements(By.cssSelector(".title-container.col-xs-6.v-fw-medium"));
            List<WebElement> fieldValue = driver.findElements(By.cssSelector(".row-value.col-xs-6.v-fw-regular"));
            for (int i = 0; i<fieldName.size(); i++) {
                String textFieldName = fieldName.get(i).getAttribute("textContent").trim().replace("Info", "");
                String textFieldValue = fieldValue.get(i).getAttribute("textContent").trim();
                map.put(textFieldName, textFieldValue);
            }
        }
        writeMapOfFields(map, path);
    }

    /**
     *
     * Читаем из текстового файла список ссылок на продукты
     *
     * @param path путь к файлу в котором хранятся все ссылки на товары
     * @return возвращаем Лист с ссыками
     */

    private List<String> readLinks(String path){
        List<String> links = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String s;
            while ((s=br.readLine()) != null){
                links.add(s);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    /**
     *
     * Метод пишет Сэт в текстовый файл
     *
     * @param set
     * @param path путь к текстовому файлу в который будем писать
     */

    private void writeLinks(Set<String> set, String path){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
            for (String s : set) {
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Метод пишет Мап в текстовый файл
     *
     * @param map
     * @param path путь к текстовому файлу в который будем писать
     */
    private void writeMapOfFields(Map<String, String > map, String path){
        BufferedWriter bufw;
        try {
            bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String format = String.format("%40s --- %s%n", entry.getKey(), entry.getValue());
                bufw.write(format);
//                bufw.newLine();
            }
            bufw.flush();
            bufw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод парсит характеристики и их значения у каждого продукта
     *
     * @param d наш драйвер
     * @param url ссылка на продукт
     */

    private Map<String, String> parseProduct(WebDriver d, String url) {
        d.get(url);
        List<WebElement> fieldName = d.findElements(By.cssSelector(".title-container.col-xs-6.v-fw-medium"));
        List<WebElement> fieldValue = d.findElements(By.cssSelector(".row-value.col-xs-6.v-fw-regular"));
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i<fieldName.size(); i++) {
            String textFieldName = fieldName.get(i).getAttribute("textContent");
            String textFieldValue = fieldValue.get(i).getAttribute("textContent");
            map.put(textFieldName, textFieldValue);
            System.out.printf("%1$40s --- %2$s%n", textFieldName.trim().replace("Info", ""), textFieldValue.trim());
        }
        return map;
    }

    /**
     *
     * Метод собирает ссылки на все странички с товарами в подкатегории, т.к. на одной страничке отображается 25 товаров,
     * то большое количество товаров разбивается на странички по 25 товаров. Тут мы собираем все странички и сразу выдераем
     * с каждой странички все ссылки на все товары и сохраняем в глобальную переменную products.
     *
     * @param d наш драйвер
     * @param url ссылка на корень подкатегории.
     */

    private void getPages(WebDriver d, String url){
        d.get(url);
        List<WebElement> elements = d.findElements(By.cssSelector("div.sku-title > h4.sku-header > a"));
        for (WebElement element : elements) {
            products.add(element.getAttribute("href"));
        }
        String href = d.findElement(By.cssSelector("a.sku-list-page-next")).getAttribute("href");
        if ( href != null )
            getPages(d, href);
    }
}
