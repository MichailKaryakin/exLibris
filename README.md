# ExLibris API

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/Spring_Boot-3.x-green?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)

RESTful API для ведения личной библиотеки и отслеживания прогресса чтения.

---

## Основные возможности

* **Auth System** — При регистрации и логине используются JWT токены.
* **Book Management** — Контроль пользователем своей базы книг (CRUD операции + доп. функционал).
* **ISBN Integration** — Получение обложек книг по isbn через Open Library API.
* **Reading Management** — Работа с записями о чтении (CRUD операции + доп. функционал).
* **Reading Statistics** — Расчет статистики чтения пользователя.
* **Data Isolation** — Каждый пользователь работает только со своими данными.
* **Exception Handling** — Исключения обрабатываются GlobalExceptionHandler, формат ошибки единый.

---

## Технологический стек

| Технология          | Описание                          |
|:--------------------|:----------------------------------|
| **Java 21**         | Язык разработки                   |
| **Spring Boot 3**   | Фреймворк                         |
| **Spring Security** | Безопасность и JWT аутентификация |
| **Spring Data JPA** | Работа с базой данных             |
| **PostgreSQL**      | Реляционная база данных           |
| **Flyway**          | Миграции                          |
| **Lombok**          | Минимизация шаблонного кода       |

---

## Список эндпоинтов

### Authentication
* `POST /api/auth/register` — Регистрация нового пользователя
* `POST /api/auth/login` — Аутентификация и выдача пары токенов (Access, Refresh)
* `POST /api/auth/refresh` — Обновление Access токена по Refresh токену

### Books
* `POST /api/books` — Добавление новой книги в библиотеку
* `GET /api/books` — Получение списка книг (с пагинацией и фильтрами по автору/названию/серии)
* `GET /api/books/{id}` — Получение детальной информации о книге по ID
* `PUT /api/books/{id}` — Редактирование данных книги
* `DELETE /api/books/{id}` — Удаление книги из библиотеки
* `GET /api/books/isbn/{isbn}` — Поиск книги в базе по ISBN
* `GET /api/books/isbn/{isbn}/cover` — Редирект на URL обложки (Open Library API)

### Reading
* `POST /api/reading` — Регистрация начала чтения
* `GET /api/reading` — Список всех активностей чтения (фильтрация по статусу)
* `GET /api/reading/{id}` — Просмотр прогресса конкретной книги
* `PATCH /api/reading/{id}` — Обновление текущей страницы в записи о чтении
* `POST /api/reading/{id}/finish` — Завершение или забрасывание чтения (необязательно: оценка и заметки)
* `DELETE /api/reading/{id}` — Удаление записи о чтении
* `GET /api/reading/stats` — Сводная статистика пользователя (книги по статусам, кол-во страниц, средний балл)

---
