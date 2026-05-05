MultiFriend

Сайт для заказа услуг

Структура проекта:
  /frontend — Статическая часть сайта (HTML/CSS/JS).
  /backend — Серверная часть на Java (Spring Boot), обработка форм и валидация.
  /bot — (В разработке) Логика Telegram-бота для взаимодействия с пользователями.

Технологии:
  Java 17/21
  Spring Boot 3.x (Starter Web, Validation, Thymeleaf)
  Lombok
  Telegram Bot API (через RestTemplate)
  Maven

Особенности реализации:
  Валидация: Используется spring-boot-starter-validation. 
  Если пользователь введет некорректные данные, форма подсветит ошибки.
  Гибкость: Поле "Контакты" позволяет пользователю самому выбирать способ связи (TG, Email, Телефон).
