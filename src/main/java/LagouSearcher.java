/**
 * FileName: 爬取拉勾招聘
 * Author:   andelin
 * Date:     2020/8/25
 * Description:
 * version: 1.0
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.sound.midi.Soundbank;
import java.util.List;

public class LagouSearcher {
    public static void main(String[] args) {
        //设置webDriver路径
//        System.setProperty("webdriver.chrome.driver", "F:\\demo\\lagou-spider\\src\\main\\resources\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath());
        //创建WebDriver
        WebDriver driver = new ChromeDriver();
        //跳转页面
        driver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");
        driver.findElement(By.className("body-btn")).click();

        //通过xpath选中元素
        clickOption(driver, "工作经验", "应届毕业生");//ctrl+alt+n 还原  ctrl+alt+v提取
        clickOption(driver, "融资阶段", "未融资");
        clickOption(driver, "公司规模", "50-150人");
        clickOption(driver, "行业领域", "移动互联网");

        //解析页面数据
        extractJobsByPagination(driver);
    }

    private static void extractJobsByPagination(WebDriver driver) {
        List<WebElement> jobElements = driver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElements) {
            WebElement moneyElement = jobElement.findElement(By.className("position")).findElement(By.className("money"));
            WebElement companyName = jobElement.findElement(By.className("company_name"));
            System.out.println(companyName.getText()+":"+moneyElement.getText());
        }
        WebElement nextPageBtn = driver.findElement(By.className("pager_next"));
        if (nextPageBtn !=null && !nextPageBtn.getAttribute("class").contains("pager_next_disabled")){
            nextPageBtn.click();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            extractJobsByPagination(driver);
        }
    }

    private static void clickOption(WebDriver driver, String chosen, String option) { //ctrl+alt+m 抽取
        WebElement chosenElement = driver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + chosen + "')]"));
        WebElement optionElement = chosenElement.findElement(By.xpath("../a[contains(text(),'" + option + "')]"));
        optionElement.click();
    }
}
