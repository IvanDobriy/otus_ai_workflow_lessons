# Мини-анкета (Mini Survey)

Учебный full-stack полигон для обкатки AI-инструментария.

- **Backend**: Java 25 + Spring Boot 4.1.1 + Spring WebFlux + Gradle Kotlin DSL
- **Frontend**: Dart 3.13 / Flutter 3.47 (Web)
- **Протокол**: единый OpenAPI-контракт `protocol/openapi.yaml`, из которого
  генерируются Java-модели/интерфейсы сервера и Dart-клиент фронтенда.
- **Сборка**: единый Gradle-проект с подпроектами `:backend` и `:frontend`.

## Требования

| Инструмент | Версия |
|---|---|
| JDK | 25 |
| Gradle | 9.6.1 (обёртка `./gradlew` входит в репозиторий, отдельная установка не нужна) |
| Flutter | 3.47.2 (stable), поддержка web — должен быть в `PATH` |

## Структура

```
.
├── protocol/openapi.yaml        # единый протокол (источник истины)
├── backend/                     # Spring WebFlux: GET /questions, POST /answers
│   └── src/main/resources/static  # сюда распаковывается собранный Flutter Web
├── frontend/
│   ├── build.gradle.kts         # сборка Flutter Web + публикация zip в mavenLocal
│   └── app/                     # Flutter-проект
│       └── generated_api/       # Dart-клиент, сгенерированный из OpenAPI
└── settings.gradle.kts          # подпроекты backend + frontend
```

## Сборка и запуск

### Быстрый старт

```bash
# 1. Собрать всё (backend + frontend) и упаковать исполняемый jar
./gradlew :backend:bootJar

# 2. Запустить приложение
java -jar backend/build/libs/backend-1.0.0.jar
```

Откройте `http://localhost:8080`.

### Полная пересборка с нуля

```bash
./gradlew clean :backend:bootJar
```

`clean` удаляет артефакты Gradle (`build/`, `.gradle/`) и результаты сборки
Flutter (`frontend/app/build`, `frontend/app/.dart_tool`), после чего `bootJar`
пересобирает всё заново.

Все команды выполняются из корня проекта. Порядок шагов важен, но Gradle выстраивает
зависимости задач сам — достаточно одной команды полной сборки.

### 1. Полная сборка и упаковка backend (с встроенным веб-приложением)

```bash
./gradlew :backend:bootJar
```

Эта команда последовательно:
1. `:backend:generateSurveyApi` — генерирует Java API/модели из `protocol/openapi.yaml`;
2. `:frontend:generateSurveyApi` — генерирует Dart-клиент из того же протокола;
3. `:frontend:flutterPubGet` + `:frontend:flutterBuildWeb` — собирает Flutter Web (release);
4. `:frontend:flutterWebZip` + `:frontend:publishToMavenLocal` — пакует в zip и публикует
   артефакт `com.example.survey:mini-survey-frontend:1.0.0:web@zip` в `~/.m2` (mavenLocal);
5. `:backend:unpackFlutterWeb` — скачивает артефакт из mavenLocal и распаковывает его
   в `backend/src/main/resources/static`;
6. `:backend:processResources` + `:backend:bootJar` — собирает исполняемый jar.

### 2. Запуск приложения

```bash
java -jar backend/build/libs/backend-1.0.0.jar
```

Приложение поднимается на `http://localhost:8080`. Откройте адрес в браузере:
отобразятся вопросы, после заполнения и отправки — экран «Спасибо!».

### 3. Отдельные задачи (при необходимости)

```bash
./gradlew :frontend:publishToMavenLocal   # собрать и опубликовать только фронтенд
./gradlew :backend:unpackFlutterWeb       # скачать и распаковать веб-артефакт в static
./gradlew :backend:deleteFlutterWeb       # удалить распакованный веб-артефакт из static
./gradlew :backend:bootRun                # запустить backend напрямую из Gradle
```

## Проверка API вручную

```bash
# Список вопросов
curl http://localhost:8080/questions

# Отправка ответов
curl -X POST http://localhost:8080/answers \
  -H 'Content-Type: application/json' \
  -d '{"respondent":"tester","answers":[{"questionId":"q1","textValue":"Иван"},{"questionId":"q3","selectedOptionId":"q3-opt-3"}]}'
```

## Как устроен протокол

`protocol/openapi.yaml` описывает:
- `GET /questions` → `Question[]` (типы `TEXT` и `SINGLE_CHOICE`), вопросы читаются
  backend-ом из `backend/src/main/resources/questions.json`;
- `POST /answers` → принимает `AnswerSubmission`, backend складывает ответы в
  in-memory `Map` и логирует их в stdout через logback, возвращает `SubmissionResult`.

Обе стороны (Java и Dart) генерируются из этого файла, поэтому имена полей и типы
на беке и фронте гарантированно совпадают.

## Заметки

- `frontend/app/generated_api/` и `backend/src/main/resources/static/` — результаты
  сборки, их не нужно редактировать руками.
- Backend отдаёт Flutter-приложение как статику из `classpath:/static/`, поэтому
  отдельный web-сервер для фронта при обычном запуске не требуется.
