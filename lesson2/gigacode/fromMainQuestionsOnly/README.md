# Мини-анкета — Full-Stack Application

Минимальное full-stack приложение «Мини-анкета» для обкатки AI-инструментария.

## Стек

| Компонент | Технология |
|-----------|------------|
| Backend | Spring Boot 3.4 + WebFlux (Java 21) + Gradle Kotlin DSL |
| Frontend (основной) | HTML + CSS + JavaScript (Material Icons) |
| Frontend (альтернатива) | Flutter Web (источники в `survey_flutter/`) |
| Хранение вопросов | JSON файл |
| Хранение ответов | Map в памяти (ConcurrentHashMap) |

## Структура проекта

```
fromMainQuestionsOnly/
├── backend/                          # Spring Boot backend
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/main/
│       ├── java/ru/otus/surveys/
│       │   ├── MiniSurveyApplication.java
│       │   ├── model/
│       │   │   ├── Question.java
│       │   │   └── Answer.java
│       │   ├── controller/
│       │   │   └── AnswersController.java
│       │   └── service/
│       │       └── QuestionService.java
│       └── resources/
│           ├── application.yml
│           ├── questions.json        # Вопросы анкеты
│           └── static/
│               └── index.html        # Frontend (HTML/JS)
├── survey_flutter/                   # Flutter Web (альтернативный фронтенд)
│   ├── pubspec.yaml
│   ├── lib/main.dart
│   └── web/index.html
├── .gitignore
└── README.md
```

## API

### GET /questions
Возвращает список вопросов анкеты.

**Response:**
```json
[
  {
    "id": "name",
    "type": "text",
    "label": "Как вас зовут?",
    "options": null,
    "required": true
  }
]
```

### POST /answers
Принимает ответы пользователя.

**Request Body:**
```json
{
  "id": "",
  "answers": {
    "name": "Иван",
    "email": "ivan@example.com",
    "age": "25–34 года",
    "course_satisfaction": "Доволен/довольна",
    "recommend": "Да, однозначно!"
  }
}
```

**Response:**
```json
{
  "message": "Thank you!",
  "id": "uuid-string",
  "timestamp": "2026-10-11T15:30:00"
}
```

## Запуск

### Требования
- Java 21+
- Gradle 9.8+

### Шаг 1: Запуск backend

```bash
cd backend
./gradlew bootRun
```

Backend запустится на `http://localhost:8080`.

### Шаг 2: Открытие приложения

Откройте в браузере: http://localhost:8080

### (Опционально) Запуск Flutter Web

Если установлен Flutter SDK:

```bash
cd survey_flutter
flutter pub get
flutter run -d chrome
```

## Вопросы анкеты

Вопросы хранятся в `backend/src/main/resources/questions.json`:

1. **Имя** — текстовое поле (обязательное)
2. **Email** — поле email (обязательное)
3. **Возраст** — выпадающий список (обязательный)
4. **Удовлетворённость курсом** — radio buttons (обязательный)
5. **Рекомендация друзьям** — чекбоксы (необязательный)

## Критерии выполнения задания

- ✅ Backend: GET /questions, POST /answers
- ✅ Frontend: загрузка вопросов, форма, отправка ответов, «Спасибо!»
- ✅ Стек: Java/Spring Boot + HTML/JS (Flutter Web как альтернатива)
- ✅ Локальный запуск
- ✅ Вопросы из JSON файла
- ✅ Ответы в памяти (ConcurrentHashMap)
