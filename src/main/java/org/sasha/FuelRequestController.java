package org.sasha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FuelRequestController {

    private static final Logger logger = LoggerFactory.getLogger(FuelRequestController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping("/fuel-request")
    public String handleFuelRequest(@RequestBody FuelRequestDto fuelRequest) {
        logger.info("Получен запрос на поставку топлива");

        String fuelAmount = fuelRequest.getFuelAmount();
        String email = fuelRequest.getEmail();

        logger.info("Данные запроса: fuelAmount={}, email={}", fuelAmount, email);

        if (fuelAmount == null || email == null) {
            logger.error("Поля fuelAmount и email не заполнены");
            throw new IllegalArgumentException("Поля fuelAmount и email обязательны.");
        }

        try {
            emailService.sendEmail(
                    email,
                    "Запрос на поставку топлива",
                    "Запрос на " + fuelAmount + " литров топлива был успешно отправлен."
            );
            logger.info("Email отправлен на адрес: {}", email);
        } catch (Exception e) {
            logger.error("Ошибка при отправке email: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }

        return "Запрос обработан.";
    }
}
