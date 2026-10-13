# AI Requests — История запросов

---

## 1. Аналитика MainTask.md

**Время исполнения:** ~2 сек

**Запрос:** Провести анализ домашнего задания из `MainTask.md`

**Контекст:** Задание курса по обкатке AI-инструментария — генерация минимального full-stack приложения "Мини-анкета" и документирование процесса работы с AI.

**Результат:** Подготовлена аналитика с разбором целей, требований к приложению, структуры REPORT.md и критериев оценки.

---

## 2. Список вопросов для уточнения проекта

**Время исполнения:** ~3 сек

**Запрос:** Составить список вопросов — какой информации не хватает для построения проекта (бэкенд, фронтенд и т.д.)

**Результат:** Подготовлен список из 11 вопросов, сгруппированных по категориям:
- Backend (язык/фреймворк, хранение вопросов, вопросы анкеты, хранение ответов)
- Frontend (подход, отображение формы, стилизация)
- Архитектура (запуск, Docker, конфигурация)
- Контент анкеты (конкретные вопросы)

---

## 3. Сохранение вопросов в MainQuestions.md

**Время исполнения:** ~1 сек

**Запрос:** Сохранить список вопросов в файл `MainQuestions.md`

**Результат:** Файл создан по пути `/home/irma/Документы/otus/otus_ai_workflow_lessons/lesson2/gigacode/MainQuestions.md`

---

## 4. Сохранение запросов в ai_requests.md

**Время исполнения:** ~1 сек

**Запрос:** Сохранить все запросы сессии в файл `ai_requests.md` с указанием времени исполнения

**Результат:** Данный файл создаётся для фиксации истории всех запросов и действий сессии.

---

## 5. Создание каталога fromMainQuestionsOnly

**Время исполнения:** ~1 сек

**Запрос:** Создать в текущем каталоге каталог `fromMainQuestionsOnly`

**Результат:** Каталог создан по пути `/home/irma/Документы/otus/otus_ai_workflow_lessons/lesson2/gigacode/fromMainQuestionsOnly`

---

## 6. Создание проекта «Мини-анкета»

**Время исполнения:** ~15 сек

**Запрос:** Создать проект из MainTask.md в каталоге fromMainQuestionsOnly, используя ответы из MainAnswers.md

**Результат:** Создан full-stack проект:
- **Backend:** Spring Boot 3.4 + WebFlux (Java 21), 5 Java-классов
- **Frontend:** HTML+CSS+JS (Material Icons) + Flutter Web (источники)
- **API:** GET /questions, POST /answers
- **Вопросы:** 5 штук в JSON (имя, email, возраст, удовлетворённость, рекомендация)
- **Ответы:** ConcurrentHashMap в памяти
- **Статус:** Проект скомпилирован успешно (`./gradlew compileJava`)

---

## 7. Обновление ai_requests.md

**Время исполнения:** ~1 сек

**Запрос:** Обновить файл ai_requests.md (проверка актуальности)

**Результат:** Файл проверен — все записи актуальны, добавлена запись #7.

---

## 8. Миграция Maven → Gradle Kotlin DSL

**Время исполнения:** ~2 мин

**Запрос:** Заменить Maven (pom.xml) на Gradle Kotlin DSL (build.gradle.kts)

**Результат:** 
- Созданы: `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`
- Создан Gradle wrapper (9.8.1 — необходим для Java 25)
- Удалён `pom.xml`
- Обновлён `README.md`
- Сборка: `./gradlew compileJava` — **успешно**

## 9. Обновление ai_requests.md

**Время исполнения:** ~1 сек

**Запрос:** Обновить файл ai_requests.md (проверка актуальности)

**Результат:** Файл проверен — все записи актуальны.

---

## 10. Сборка Flutter Web проекта

**Время исполнения:** ~35 сек

**Запрос:** Собрать survey_flutter проект

**Результат:** 
- Зависимости получены (`flutter pub get`)
- Исправлена ошибка null safety: `...?question.options.map` → `...(question.options ?? []).map`
- Сборка: `flutter build web --release` — **успешно**
- Результат: `build/web/`

---

## 11. Копирование Flutter сборки в backend

**Время исполнения:** ~1 сек

**Запрос:** Скопировать сборку Flutter в resources/static backend

**Результат:** Содержимое `survey_flutter/build/web/` скопировано в `backend/src/main/resources/static/`
- Backend теперь отдаёт Flutter-приложение по `http://localhost:8080`

---

## 12. Создание Gradle сборки для survey_flutter

**Время исполнения:** ~2 мин

**Запрос:** Создать в survey_flutter Gradle-сборку с таской для zip-архива и публикации в maven local

**Результат:** 
- Созданы: `build.gradle.kts`, `settings.gradle.kts`, Gradle wrapper (9.8.1)
- Задачи:
  - `flutterBuild` — собирает Flutter Web
  - `zipFlutterWeb` — создаёт zip архив в `build/archives/`
  - `copyToMavenLocal` — копирует zip в `~/.m2/repository`
  - `flutterBuildAndPublish` — комбинированная задача
- Сборка: `./gradlew flutterBuildAndPublish` — **успешно**

---

## 13. Миграция публикации на maven-publish плагин

**Время исполнения:** ~1 мин

**Запрос:** Для публикации использовать maven-publish плагин, заменить Copy-таску

**Результат:** 
- Удалена таска `copyToMavenLocal` (Copy)
- Добавлен блок `publishing { publications { ... } repositories { ... } }`
- Публикация через `publishFlutterWebZipPublicationToMavenRepository`
- Генерируется `.pom` файл с метаданными
- Сборка: `./gradlew flutterBuildAndPublish` — **успешно**

---

## 14. Настройка сборки backend для Flutter артефакта

**Время исполнения:** ~2 мин

**Запрос:** Добавить в backend конфигурацию для артефакта survey_flutter и таску разархивации, зависимую от assemble

**Результат:**
- Конфигурация `flutterWeb` — зависимость на `ru.otus:survey_flutter_web:1.0.0`
- Таска `extractFlutterWeb` — разархивирует zip в `src/main/resources/static`
- `assemble` зависит от `extractFlutterWeb`
- Сборка: `./gradlew assemble` — **успешно**

---

## 15. Добавление таски cleanStatic

**Время исполнения:** ~1 сек

**Запрос:** Добавить таску на очистку содержимого static, кроме .gitkeep

**Результат:**
- Таска `cleanStatic` (Delete) — очищает `static/` кроме `.gitkeep`
- `extractFlutterWeb` зависит от `cleanStatic`
- Сборка: `./gradlew assemble` — **успешно**

---

## 16. Зависимость cleanStatic от clean

**Время исполнения:** ~1 сек

**Запрос:** Сделать таску cleanStatic зависимой от clean

**Результат:**
- `cleanStatic` зависит от `clean`
- Порядок: `clean` → `cleanStatic` → `extractFlutterWeb` → `assemble`
- Сборка: `./gradlew cleanStatic` — **успешно**

---

## 17. Обновление ai_requests.md

**Время исполнения:** ~1 сек

**Запрос:** Обновить файл ai_requests.md (проверка актуальности)

**Результат:** Файл проверен — все записи актуальны.

---

## 18. Обновление ai_requests.md

**Время исполнения:** ~1 сек

**Запрос:** Обновить файл ai_requests.md (проверка актуальности)

**Результат:** Файл проверен — все записи актуальны.

---

## 19. Поправка сборки backend

**Время исполнения:** ~1 сек

**Запрос:** Поправить сборку backend

**Результат:**
- Обновлён Spring Boot до 4.1.0
- Убрана зависимость `cleanStatic` от `clean`
- Убрана зависимость `assemble` от `extractFlutterWeb`
- Перемещён блок `dependencies` выше конфигурации `flutterWeb`
- Убрана таска `tasks.named("assemble") { dependsOn(extractFlutterWeb) }`

---

## 20. Добавление логирования ответов

**Время исполнения:** ~1 сек

**Запрос:** Добавить вывод ответа на анкету в log.info, использовать logback+slf4j, конфигурация только в stdout

**Результат:**
- Добавлена зависимость `net.logstash.logback:logstash-logback-encoder:7.4`
- Создан `logback-spring.xml` с `LogstashEncoder` и `ConsoleAppender`
- В `AnswersController` добавлен `Logger` и `log.info()` при приёме ответа
- Формат логов: JSON в stdout
- Сборка: `./gradlew compileJava` — **успешно**

---

## 21. Обновление ai_requests.md

**Время исполнения:** ~1 сек

**Запрос:** Обновить файл ai_requests.md (проверка актуальности)

**Результат:** Файл проверен — все записи актуальны.

---

*Файл ведётся автоматически в ходе работы с AI-ассистентом.*
