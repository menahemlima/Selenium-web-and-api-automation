package tests;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTests {
    private WebDriver navegador;

    /* locators dos elementos */
    public By btnAdd = By.xpath("//a[contains(text()[2], 'Add Record')]");
    public By cmbSelect = By.xpath("//select[@id='switch-version-select']/option[text()='Bootstrap V4 Theme']");
    public By btnSave = By.xpath("//button[@id='form-button-save']");
    public By btnGoTo = By.linkText("Go back to list");
    public By btnClearSearch = By.xpath("//i[contains(@class, 'fa fa-times el el-remove clear-search')]");
    public By btnDelete = By.xpath("//a[contains(@class, 'btn btn-outline-dark delete-selected-button')]");
    public By btnDelete2 = By.xpath("//button[contains(@class, 'btn btn-danger delete-multiple-confirmation-button')]");
    public By selectItem = By.xpath("//input[contains(@class, 'select-row')]");
    public By txtName = By.xpath("//input[@id='field-customerName']");
    public By txtLastName = By.xpath("//input[@id='field-contactLastName']");
    public By txtContactFirstName = By.xpath("//input[@id='field-contactFirstName']");
    public By txtPhone = By.xpath("//input[@id='field-phone']");
    public By txtAddress1 = By.xpath("//input[@id='field-addressLine1']");
    public By txtAddress2 = By.xpath("//input[@id='field-addressLine2']");
    public By txtCity = By.xpath("//input[@id='field-city']");
    public By txtState = By.xpath("//input[@id='field-state']");
    public By txtPostalCode = By.xpath("//input[@id='field-postalCode']");
    public By txtCountry = By.xpath("//input[@id='field-country']");
    public By txtEmployeer = By.xpath("//input[@id='field-salesRepEmployeeNumber']");
    public By txtCredit = By.xpath("//input[@id='field-creditLimit']");
    public By txtSearch = By.xpath("//input[@name='customerName']");
    public By popConfirm = By.xpath("//span[@data-growl='message']/p");
    public By popMessage = By.xpath("//div[@id='report-success']/p");

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");
        navegador = new ChromeDriver();
        navegador.get("https://www.grocerycrud.com/v1.x/demo/my_boss_is_in_a_hurry/bootstrap");
        navegador.manage().window().maximize();
    }

    /* Dados para preencher o registro */
    public void PreencheDados(){

        navegador.findElement(cmbSelect).click();
        navegador.findElement(txtName).sendKeys("Teste");
        navegador.findElement(txtLastName).sendKeys("Teste");
        navegador.findElement(txtContactFirstName).sendKeys("Usuario Lima");
        navegador.findElement(txtPhone).sendKeys("51 9999-9999");
        navegador.findElement(txtAddress1).sendKeys("Av Assis Brasil,3970");
        navegador.findElement(txtAddress2).sendKeys("Torre D");
        navegador.findElement(txtCity).sendKeys("Porto Alegre");
        navegador.findElement(txtState).sendKeys("RS");
        navegador.findElement(txtPostalCode).sendKeys("91000-000");
        navegador.findElement(txtCountry).sendKeys("Brasil");
        navegador.findElement(txtEmployeer).sendKeys("Fixter");  // bloqueado para texto, validado com número
        navegador.findElement(txtEmployeer).sendKeys("1");
        navegador.findElement(txtCredit).sendKeys("200");
    }

    /*Espera o elemento aparecer na tela */
    public void WebDriverWaitElement (By element) {
        int timeout = 10;
        new WebDriverWait(navegador, timeout).until(ExpectedConditions.presenceOfElementLocated(element));
        new WebDriverWait(navegador, timeout).until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    /* Procura registro */
    public void ProcuraRegistro(String item) {
        navegador.findElement(txtSearch).sendKeys(item);
        navegador.findElement(txtSearch).sendKeys(Keys.ENTER);
        this.WebDriverWaitElement(btnClearSearch);
    }

    /* Para deletar o Registro */
    public void DeletarRegistro() {
        navegador.findElement(selectItem).click();
        navegador.findElement(btnDelete).click();
        this.WebDriverWaitElement(btnDelete);
    }

    @Test
    public void NovoRegistroTest() throws Exception {
        cmbSelect.findElement(navegador).click();
        btnAdd.findElement(navegador).click();
        this.PreencheDados();
        navegador.findElement(btnSave).click();
        this.WebDriverWaitElement(popMessage);
        String messageReg = "Your data has been successfully stored into the database.".trim();
        assert (navegador.findElement(By.xpath("//div[@id='report-success']/p")).getText().trim().contains(messageReg));
    }

    @Test
    public void DeletaRegistroTest() throws Exception {
        cmbSelect.findElement(navegador).click();
        navegador.findElement(btnAdd).click();
        this.PreencheDados();
        navegador.findElement(btnSave).click();
        this.WebDriverWaitElement(btnGoTo);
        navegador.findElement(btnGoTo).click();
        this.ProcuraRegistro("Teste Sicredi");
        this.DeletarRegistro();
        String messageConf = "Are you sure that you want to delete this 1 item?".trim();
        assert (navegador.findElement(By.xpath("//p[@class=\"alert-delete-multiple-one\"]")).getText().trim().contains(messageConf));
        navegador.findElement(btnDelete2).click();
        this.WebDriverWaitElement(popConfirm);
        String messageCerta = "Your data has been successfully deleted from the database.".trim();
        assert (navegador.findElement(By.xpath("//span[@data-growl='message']/p")).getText().trim().contains(messageCerta));
    }

    @After
    public void tearDown() throws Exception {
        navegador.close();
    }
}