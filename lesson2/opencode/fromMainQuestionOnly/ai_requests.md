# История запросов к AI-инструменту (промпты)

Инструмент: **OpenCode** (CLI-агент), модель **big-pickle**.
Формат: полный текст запроса пользователя → что делал ассистент.

---

## 1. Формирование списка вопросов к заданию

**Промпт (полный текст):**

> создай список основных вопросов для решения @MainTask.md и сохрани его в MainQuestions.md

**Результат:** создан `MainQuestions.md` со списком вопросов по 10 направлениям
(выбор setup, стек, структура, backend, frontend, запуск, работа с инструментом,
артефакты, блоки REPORT.md, критерии готовности).

---

## 2. Сборка проекта по заданию и уточнениям

**Промпт (полный текст):**

> используя @MainTask.md и @MainAnswers.md собери проект

**Спецификация из `MainAnswers.md`, использованная как ТЗ:**

- AI-setup: IntelliJ IDEA + OpenCode + модель Big Pickle.
- Backend: Java 25, Spring 4 / Spring Boot, Spring WebFlux, сборка Gradle Kotlin DSL.
- Frontend: Dart / Flutter.
- Backend: `GET /questions` (3–5 вопросов из json-файла), `POST /answers`
  (хранение в памяти Map + логирование в stdout через logback).
- Протокол: OpenAPI, использовать для синхронизации бэка и фронта.
- Типы вопросов: текстовые и «чекбокс» с единственным выбором.
- Frontend: GET /questions, построение анкеты, отправка ответов, экран «Спасибо!»
  по центру.
- Сборка: Gradle-сборка для backend и Flutter; артефакт Flutter Web публиковать
  через maven publish в mavenLocal; в backend — отдельная конфигурация `flutterWeb`,
  задачи распаковки в `src/main/resources/static` и удаления артефакта.
- Ошибки сборки чинить самостоятельно (до 3 попыток), смотреть stdout.
- Git — не оформлять. Инструкцию сборки фронта/бэка — добавить.
- Скриншоты — не делать. Историю промптов — в `ai_requests.md`.

**Что сгенерировано (по этому ТЗ):**

1. `protocol/openapi.yaml` — контракт `GET /questions`, `POST /answers`,
   схемы `Question`, `QuestionOption`, `QuestionType`, `Answer`, `AnswerSubmission`,
   `SubmissionResult`.
2. `settings.gradle.kts`, `gradle.properties` — корневой Gradle-проект с `:backend` и `:frontend`.
3. `backend/build.gradle.kts` + Java-исходники (Spring WebFlux, logback, questions.json).
4. `frontend/build.gradle.kts` + Flutter-приложение (`frontend/app`).
5. Задачи генерации кода из OpenAPI: `generateSurveyApi` (Java и Dart).
6. Задачи сборки/публикации фронта и распаковки веб-артефакта в backend.

**Проблемы, которые пришлось решать в диалоге** (детали — в `REPORT.md`):
- Jackson 3 в Spring Boot 4 (пакеты `tools.jackson.*` вместо `com.fasterxml.jackson.*`);
- Dart-клиент тянет `dart:io` — несовместимо с web;
- `sourceFolder` в dart-генераторе привёл к вложенности `lib/lib/`;
- падение `flutter analyze` (analysis server) — проверяли через `dart analyze`.

---

## 3. Продолжение работы (три запроса «продолжай»)

**Промпт (полный текст):**

> продолжай

Продолжение сборки: правка Dart-шимов под web (`io_shims.dart` с `HttpStatus`,
`SocketException`, `TlsException`, `IOException`), сборка `flutter build web`.

**Промпт (полный текст):**

> продолжай

Публикация фронтенда в mavenLocal, распаковка артефакта в backend, сборка
`bootJar`, запуск приложения и сквозная проверка сценария
(GET /questions → POST /answers → «Спасибо!»).

**Промпт (полный текст):**

> продолжай

Завершение: проверка задачи `deleteFlutterWeb`, повторная сквозная сборка с чистого
состояния (`gradle clean :backend:bootJar`), подготовка `README.md` и `REPORT.md`.

---

## 4. Обновление журнала запросов

**Промпт (полный текст):**

> обнгови @ai_requests.md

**Результат:** дополнен раздел с тремя запросами «продолжай» и добавлен текущий
раздел; журнал приведён в соответствие с фактической историей сессии.
