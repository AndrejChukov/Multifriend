package ru.multifriend.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.multifriend.model.FeedbackForm;
import ru.multifriend.service.TelegramService;

@Controller
public class FormController {

    private final TelegramService telegramService;

    public FormController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("feedbackForm", new FeedbackForm());
        return "index";
    }

    @PostMapping("/send")
    public String processForm(@Valid @ModelAttribute("feedbackForm") FeedbackForm form,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        String message = String.format(
                "Новая заявка с сайта:\n" +
                        "Имя: \n" + form.getName() +
                        "Предпочитаемый тип связи: \n" + form.getContactMethod() +
                        "Контакты: \n" + form.getContactInfo() +
                        "Сообщение: " + form.getMessage()
        );

        telegramService.sendMessage(message);

        return "success";
    }
}