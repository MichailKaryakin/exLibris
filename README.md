# ExLibris

RESTful API для ведения личной библиотеки и отслеживания прогресса чтения.

## Быстрый старт

### 1. База данных

Запустить PostgreSQL (например, через Docker):

```bash
docker run -d --name exlibris-postgres \
  -e POSTGRES_DB=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=BYC-eM5-Mzw-Mp8 \
  -p 5432:5432 \
  postgres:16-alpine
```

### 2. Секрет

Создать файл `src/main/resources/application-secret.properties`:

```properties
jwt.secret=your-secret-key-min-32-chars-long!!
```

### 3. Запуск

```bash
./mvnw spring-boot:run
```

## Адреса

| Сервис       | URL                   |
|--------------|-----------------------|
| ExLibris API | http://localhost:8080 |