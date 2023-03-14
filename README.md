# java-explore-with-me
Template repository for ExploreWithMe project.


# Этап 1. Сервис статистики
Первый этап — реализация сервиса статистики. Его функционал достаточно прост и ограничен,
поэтому начать с него будет лучше всего. Реализация сервиса статистики позволит вам 
разобраться со спецификацией API и основными требованиями ТЗ, а также  подготовить сборку
проекта.

На первом этапе необходимо:  
1. Реализовать сервис статистики в соответствии со спецификацией:
[ewm-stats-service.json](ewm-stats-service-spec.json).
2. Реализовать HTTP-клиент для работы с сервисом статистики.
3. Подготовить сборку проекта.
4. Определиться с тематикой дополнительной функциональности, которую вы будете реализовывать.

### Базовые требования
Разработка должна вестись в публичном репозитории, созданном на основе
[шаблона](https://github.com/yandex-praktikum/java-explore-with-me).

Весь код первого этапа разместите в отдельной ветке с именем `stat_svc`.

### Что будет проверяться

* Работающая сборка проекта:
  * проект компилируется без ошибок;
  * сервис статистики успешно запускается в докер-контейнере;
  * экземпляр PostgreSQL для сервиса статистики успешно запускается в докер-контейнере.  
* Корректная работа сервиса статистики:
  * все эндпоинты отрабатывают в соответствии со спецификацией;
  * данные успешно сохраняются и выгружаются из базы данных;
  * реализован HTTP-клиент сервиса статистики.

##### 💡 На этом этапе у вас две итерации проверки работы.

### Как подготовить сборку проекта

1. Учитывайте многомодульность.

Приложение дипломного проекта должно состоять из двух отдельно запускаемых сервисов — в
контексте сборки проекта при помощи Maven это означает, что проект будет многомодульным.
Но это ещё не всё. Сами сервисы можно также разбить на подмодули.
 
Сервис статистики должен состоять из HTTP-сервиса и HTTP-клиента. Это значит, что модуль
статистики можно разделить на два подмодуля.

Механизм взаимодействия сервиса и клиента предполагает, что они будут использовать одни и
те же объекты для запросов и ответов. Исходя из этого, можно выделить еще один подмодуль,
в котором будут размещены общие классы DTO.

Структуру модулей и зависимостей можно представить в виде такой схемы.
![img.png](img.png)

2. Поработайте с файлами.
   *  модули основного сервиса и сервиса статистики должны содержать `dockerfile`;
   * в корне проекта должен быть создан файл `docker-compose.yml`, описывающий запуск
   контейнеров с сервисами проекта и базами данных для них.
   * файл `pom.xml`, описывающий сборку основного сервиса, на данном этапе должен содержать
   только указание на родительский модуль и идентификатор артефакта.

3. Проверьте обязательные зависимости.

Одной из обязательных зависимостей в каждом из сервисов должен быть `Spring BootActuator`.
Вот идентификаторы для её добавления.
```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### После завершения ревью
Когда все замечания ревьюера будут устранены и ваш Pull Request будет утверждён,
не забудьте сделать слияние изменений из ветки `stat-svc` в ветку `main`. Для этого
перейдите в ваш Pull Request на платформе GitHub и нажмите кнопку `Merge pull request`.

Начинать большой проект всегда сложно, но важно сделать первый шаг.

Приступайте к первому этапу! (^_-)≡☆
