# ok-marketplace

Учебный проект курса
[Kotlin Backend Developer](https://otus.ru/lessons/kotlin/?int_source=courses_catalog&int_term=programming).

Marketplace -- это площадка, на которой пользователи выставляют предложения и потребности. Задача
площадки -- предоставить наиболее подходящие варианты в обоих случаях: для предложения -- набор вариантов с
потребностями, для потребностей -- набор вариантов с предложениями.

## Визуальная схема фронтенда

![Макет фронта](imgs/design-layout.png)

## Документация

1. Маркетинг
   1. [Заинтересанты](./docs/01-marketing/02-stakeholders.md)
   2. [Целевая аудитория](./docs/01-marketing/01-target-audience.md)
   3. [Конкурентный анализ](./docs/01-marketing/03-concurrency.md)
   4. [Анализ экономики](./docs/01-marketing/04-economy.md)
   5. [Пользовательские истории](./docs/01-marketing/05-user-stories.md)

2. DevOps
   1. [Схема инфраструктуры](./docs/02-devops/01-infrastruture.md)
   2. [Схема мониторинга](./docs/02-devops/02-monitoring.md)

3. Приемочные тесты - [ok-marketplace-acceptance](ok-marketplace-acceptance)

4. Архитектура
   1. [Компонентная схема](./docs/04-architecture/01-arch.md)
   2. [Интеграционная схема](./docs/04-architecture/02-integration.md)
   3. [Описание API](./docs/04-architecture/03-api.md)

# Структура проекта

1. Транспортные модели и мапперы
   1. [specs](specs) - спецификации openapi
   2. [ok-marketplace-api-v1-jackson](ok-marketplace-api-v1-jackson) Версия 1, используется jackson
   3. [ok-marketplace-api-v2-kmp](ok-marketplace-api-v2-kmp) Версия 2, используется kotlinx serialization
   4. [ok-marketplace-mappers-v1](ok-marketplace-mappers-v1) Мапперы из транспортных моделей v1 во внутренние модели
   5. [ok-marketplace-mappers-v2](ok-marketplace-mappers-v2) Мапперы из транспортных моделей v2 во внутренние модели

2. Бизнес-логика и внутренние модели
   1. [ok-marketplace-common](ok-marketplace-common) Внутренние модели, общие хелперы и интерфейсы
   2. [ok-marketplace-stubs](ok-marketplace-stubs) Стабы
   3. [ok-marketplace-lib-cor](ok-marketplace-lib-cor) Библиотека для построения бизнес-логики

3. Приложения (точки входа)
   1. [ok-marketplace-app-common](ok-marketplace-app-common) Общий код для приложений
   2. [ok-marketplace-app-spring](ok-marketplace-app-spring) Spring
   3. [ok-marketplace-app-ktor](ok-marketplace-app-ktor) Ktor
   4. [ok-marketplace-app-serverless](ok-marketplace-app-serverless) Яндекс-облако
   5. [ok-marketplace-app-rabbit](ok-marketplace-app-rabbit) RabbitMq
   6. [ok-marketplace-app-kafka](ok-marketplace-app-kafka) Kafka

4. Логирование
   1. [specs-ad-log.yaml](specs%2Fspecs-ad-log.yaml) Модели логирования (openapi)
   2. [ok-marketplace-api-log1](ok-marketplace-api-log1) Модели логирования
   3. [ok-marketplace-mappers-log1](ok-marketplace-mappers-log1) Маперы для моделей логирования
   4. [ok-marketplace-lib-logging-common](ok-marketplace-lib-logging-common) Интерфейс логирования
   5. [ok-marketplace-lib-logging-kermit](ok-marketplace-lib-logging-kermit) Логирование через kermit
   6. [ok-marketplace-lib-logging-logback](ok-marketplace-lib-logging-logback) Логирование через logback

5. Репозитарии
   1. [ok-marketplace-repo-in-memory](ok-marketplace-repo-in-memory) Репозитарий в памяти
   2. [ok-marketplace-repo-stubs](ok-marketplace-repo-stubs) Репозитарий-заглушка
   3. [ok-marketplace-repo-tests](ok-marketplace-repo-tests) Проект с тестами для репозитариев

## Подпроекты для занятий по языку Kotlin

1. [m1l1-hello](m1l1-hello) - Вводное занятие, создание первой программы на Kotlin
2. [m1l2-basic](m1l2-basic) - Основные конструкции Kotlin
3. [m1l3-oop](m1l3-oop) - Объектно-ориентированное программирование
4. [m1l4-dsl](m1l4-dsl) - Предметно ориентированные языки (DSL)
5. [m1l5-coroutines](m1l5-coroutines) - Корутины, ч.1
6. [m1l6-flows-and-channels](m1l6-flows-and-channels) - Корутины, ч.2 - flow, channels
7. [m1l7-kmp](m1l7-kmp) - Kotlin MultiPlatform
