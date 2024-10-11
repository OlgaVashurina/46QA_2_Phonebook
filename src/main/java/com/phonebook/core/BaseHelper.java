package com.phonebook.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BaseHelper {

    Logger logger = LoggerFactory.getLogger(BaseHelper.class);

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseHelper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isElementPresent(By locator) {
        // logger.info("Проверка есть ли элемент [" + locator + "] на странице");
        return driver.findElements(locator).size() > 0;
    }

    protected void click(By locator) {
        driver.findElement(locator).click();
        //logger.info("[" + locator + "] is pressed");
    }

    protected void type(By locator, String text) {
        if (text != null) {
            click(locator);
            driver.findElement(locator).clear();
            driver.findElement(locator).sendKeys(text);
        }
    }

    public boolean isAlertPresent() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        try {
            logger.warn("Alert has text: [" + alert.getText() + "]");
            alert.accept();
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }


    public String alertTextPresent() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }
    public String takeScreenshot() {
        // Создаем объект File для сохранения скриншота, добавляя текущую метку времени в название файла
        File screenshot = new File("src/test_screenshots/screen-" + System.currentTimeMillis() + ".png");
        try {
            // Получаем временный файл скриншота с помощью интерфейса TakesScreenshot
            File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // Копируем временный файл в постоянное место назначения (в созданный ранее файл screenshot)
            Files.copy(tmp.toPath(), screenshot.toPath());
        } catch (NoSuchSessionException e) {
            logger.error("WebDriver session is closed, cannot take screenshot", e);
            return ""; // Возвращаем пустую строку, чтобы тест не падал
        } catch (IOException e) {
            // Логируем ошибку при сохранении скриншота и выбрасываем исключение RuntimeException
            logger.error("Failed to save screenshot", e);
            throw new RuntimeException(e);
        }
        // Возвращаем путь к сохраненному скриншоту
        return screenshot.getAbsolutePath();
    }

}
