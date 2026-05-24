package ru.multifriend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackForm {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Выберите способ связи")
    private String contactMethod;

    @NotBlank(message = "Укажите корректные данные")
    private String contactInfo;


    @Size(min = 10, message = "Сообщение должно быть не менее 10 символов")
    private String message;
}