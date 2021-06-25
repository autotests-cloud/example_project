# Тестовый стенд в Digital Ocean

1. [Зайти и зарегистрироваться](https://digitalocean.com)
2. Добавить платёжную информацию (без этого никакого волшебства).
3. Желательно настроить двухфакторную аутентификацию.
   - Settings > Security > Two factor authentication

## Подготовка к работе

### Создаем ключи для аутентификации

На Windows лучше всего работать через GitBash из состава git for windows.

1. Запускаем git bash (win) или терминал (macOS, Linux)
2. Генерируем пару RSA ключей:
   - `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`
   - Генератор спросит нас, куда бы нам записать эти ключи. Используем папку, которую он хочет по умолчанию (просто Enter)
   - Генератор спросит про пароль для доступа к ключу. Если это боевые ключи, то пароль лучше задать.
3. Проверяем жив ли ssh агент
   - `eval $(ssh-agent -s)`
     - он жив, если виден вывод `pid 2134` (цифры у вас будут отличаться)
4. Добавляем ключи ssh агенту
   - `ssh-add ~/.ssh/id_rsa` 
5. Выполняем команду для вывода на экран **публичного ключа**:
   - `cat ~/.ssh/rsa/id_rsa.pub`
   - Копируем ключ от `ssh-rsa` до конца (email).
6. Заходим в настройки digitalocean: Settings > Security > SSH keys > Add SSH key
   - Вставляем ключ в окно
   - Даем ему имя
   - Жмём Add SSH key

Этот добавленный ключ мы будем использовать для доступа в дроплет (виртуальная машина).

### Создаем ВМ (droplet)

Там в UI есть минимум три варианта, куда нажать, чтобы это создалось. Я уверен, что вы сможете найти что-то типа Create > Droplets

Создать Droplet, комфортная конфигурация такая:
   - Distributions - Ubuntu 18.04 LTS
   - Choose a plan - Basic
   - CPU - Regular Intel with SSD
     - 4GB/2CPU, 80GB SSD, 4 TB transfer

После того, как поиграли - Destroy droplet, чтобы деньги не капали (при этом всё удалится!). Если надо - создаем снова, потом можно снова грохнуть.

Деньги с карты списываются только при достижении 5 USD. За 1 час работы с дроплетом конфигурации указанной выше, вам будет начислено порядка 5 центов.

После того, как дроплет создан, копируем его IP-адрес.

### Создаем конфигурацию ssh для более простого подключения

Если нет, то создаем, если есть - открываем для редактирования файл `config`, который находится у нас на локальной машине в папке `.ssh`.

- Windows: `C:\Users\<USERNAME>\.ssh`
- MacOS, Linux: `~/.ssh`

В файл добавляем следующее содержимое (строки внутри `<>` надо заменить на свои):

```bash

Host <myhost>
	Hostname <127.0.0.1> # тут добавить IP адрес дроплета
	User root # тут пока будет root, потом будет имя пользователя, которого мы создадим далее
	Port 22
`
```
### Заходим на созданный droplet

В `gitbash` выполняем команду:

```bash
ssh <myhost>
```

`<myhost>` -- Это то, как вы назвали свой хост на предыдущем шаге.

На вопрос добавить ли удаленный хост в доверенные отвечаем `Yes`.

### Задаем пароль для root

```bash
passwd
```

Вводим пароль 2 раза.

### Создаем пользователя, отличного от root, добавляем его в группу sudo, задаем пароль

```bash
useradd -m -g users <username>
usermod -aG sudo <username>
passwd <username>
```

### Обновление репозиториев, установка mc

```bash
apt update && apt upgrade -y
apt install mc
```
Открываем mc командой `mc`

### Добавить пользователю <username> ключи для работы с ssh

При помощи MC копируем (F5) всю папку `/root/.ssh` в `/home/<username>`

Переходим в `/home/<username>/` на папке .ssh в верхней панели File > chown меняем для папки владельца и группу:
- Owner > `<username>`
- Group > `users`

Переходим в `/home/<username>/.ssh` на файле `authorized_keys` в верхней панели File > chown меняем для файла владельца и группу:
- Owner > `<username>`
- Group > `users`

### Правим на локальной машине файл .ssh/config

Меняем `root` на `<username>`

Выходим из сессии - `exit` 

Снова заходим, но уже с не root пользователем

```bash
ssh <myhost>
```

### Первичные настройки

1. Задать пользователя (для локальных тестов можно покороче)
2. Задать пароль (для локальных тестов можно покороче)

### Ставим JDK

Проверяем, есть ли у нас Java:

```java --version```

Терминал нам ответит что-то похожее на это, если Java не установлена:
```
user@qa-test-bed:~$ java --version
Command 'java' not found, but can be installed with:
sudo apt install default-jre
sudo apt install openjdk-11-jre-headless
sudo apt install openjdk-8-jre-headless
```
### Обновляемся

```sudo apt-get update && sudo apt-get upgrade```

### Устанавливаем JDK

```sudo apt-get install default-jdk```

Соглашаемся на установку и потом ждем, пока вся эта радость установится.

Ещё раз проверяем версию Java:

```java --version```

```
    PS C:\Users\username> java --version
    java 11.0.7 2020-04-14 LTS
    Java(TM) SE Runtime Environment 18.9 (build 11.0.7+8-LTS)
    Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.7+8-LTS, mixed mode)
```

### Прописываем переменную окружения JAVA_HOME (понадобится)

Джава вероятнее всего установилась вот так:

```/usr/lib/jvm/java-11-openjdk-amd64``` в JAVA_HOME необходимо прописывать именно этот путь, который не включает папку `bin`.

#### Редактируем файл с переменными окружения

```sudo nano /etc/environment```

добавляем на последнюю строку:

```JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64```

Нажимаем последовательно Ctrl+S (сохранить), Ctrl+X (выйти из редактора nano).

#### Обновляем сведения о переменных окружения

```source /etc/environment```

#### Проверяем, прописалась ли переменная окружения

```echo $JAVA_HOME```

```
    username@pc:$ source /etc/environment
    username@pc:$ echo $JAVA_HOME
    /usr/lib/jvm/java-11-openjdk-amd64
```
Всё ок.

## Ставим все через docker-compose

### Selenoid

Описано здесь: https://aerokube.com/selenoid/latest/#_selenoid_with_docker_compose

#### Готовим конфиг для docker-compose

В папке проекта создаем файл `docker-compose.yml`

```yaml
version: '3.4'

services:
  selenoid:
    image: aerokube/selenoid:latest-release
    volumes:
      - "${PWD}/init/selenoid:/etc/selenoid"
      - "${PWD}/work/selenoid/video:/opt/selenoid/video"
      - "${PWD}/work/selenoid/logs:/opt/selenoid/logs"
      - "/var/run/docker.sock:/var/run/docker.sock"
    environment:
      - OVERRIDE_VIDEO_OUTPUT_DIR=work/selenoid/video
    command: ["-conf", "/etc/selenoid/browsers.json", "-video-output-dir", "/opt/selenoid/video", "-log-output-dir", "/opt/selenoid/logs"]
    ports:
      - "4444:4444"
    network_mode: bridge
```

${PWD} - текущая директория

`volumes` позволяет монтировать папки файловой системы локальной машины в контейнер.

Для строки типа `"${PWD}/init/selenoid:/etc/selenoid"` слева от двоеточия находится папка, которая лежит на локальной машине (в Linux distro), справа — на файловой системе контейнера.

##### В папке проекта создаём подпапки
- init/selenoid
- work/selenoid/video
- work/selenoid/logs

#### Файлы внутри init/selenoid

Создаём `browsers.json`

```json
{
    "chrome": {
      "default": "88.0",
        "88.0": {
          "env" : ["LANG=ru_RU.UTF-8", "LANGUAGE=ru:en", "LC_ALL=ru_RU.UTF-8", "TZ=Europe/Moscow"],
          "image": "selenoid/vnc:chrome_88.0",
          "tmpfs": {"/tmp": "size=512m"},
          "port": "4444"
        }
      }
    }
  }
```
#### Образы браузеров

Образы браузеров необходимо скачивать самостоятельно. Для примера выше потребуется образ браузера `selenoid/vnc:chrome_88.0`, он скачивается следующим образом:

```bash
docker pull selenoid/vnc:chrome_88.0
```
После этого, можно попробовать запустить то, что получилось.

#### Запуск через docker-compose

В папке проекта выполняем:

```bash
    docker-compose up
```
В консоли будет что-то вроде вот этого:

```
user@pc:/path/project$ docker-compose up
Starting project_selenoid_1 ... done
Attaching to project_selenoid_1
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Loading configuration files...]
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Loaded configuration from /etc/selenoid/browsers.json]
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Video Dir: /opt/selenoid/video]
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Logs Dir: /opt/selenoid/logs]
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Your Docker API version is 1.41]
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Timezone: UTC]
selenoid_1     | 2021/03/03 20:33:19 [-] [INIT] [Listening on :4444]
```

В браузере заходим `http://your-ip-address:4444/status`

Должна быть видна JSON строка `{"total":5,"used":0,"queued":0,"pending":0,"browsers":{"chrome":{"75.0":{},"76.0":{},"87.0":{},"88.0":{}}}}`

Выключаем при помощи Ctrl+C, переходим к след. шагу.

### Selenoid UI

Описано здесь: http://aerokube.com/selenoid-ui/latest/#_with_docker_compose

Добавляем в файл `docker-compose.yml` необходимое для запуска selenoid-ui:

```yaml
  selenoid-ui:
    image: "aerokube/selenoid-ui:latest-release"
    network_mode: bridge
    links:
      - selenoid
    ports:
      - "8080:8080"
    command: ["--selenoid-uri", "http://selenoid:4444"]
```
#### Проверяем работоспособность

```bash
    docker-compose up
```
В браузере заходим `http://your-ip-address:8080`

Мы должны видеть веб-страницу selenoid-ui.

 ![Selenoid-ui up and running](./img/selenoid-ui-main.png)

1. Переходим на вкладку Capabilities
2. Выбираем браузер
3. Запускаем ручную сессию

Чтобы иметь возможность управлять браузером внутри контейнера, нужно его разблокировать (1) и лучше раскрыть VNC окно на максимум (2).

 ![Selenoid-ui up and running](./img/selenoid-manual-session.png)

Если все работает, то останавливаем docker-compose и переходим к установке Jenkins.  

### Jenkins

гуглим конфиг: https://is.gd/LKXZ9k

1. Добавляем к `docker-compose.yml`

```yaml
  jenkins:
    image: jenkins/jenkins:lts
    user: root
    volumes:
      - ${PWD}/work/jenkins:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
    ports:
      - 8888:8080
    network_mode: bridge
```

2. Запускаем docker-compose

```bash
  docker-compose up
```
Ждём, когда закачается образ и Jenkins стартует. В логах должно появиться следующее сообщение:

```bash
jenkins_1      | Jenkins initial setup is required. An admin user has been created and a password generated.
jenkins_1      | Please use the following password to proceed to installation:
jenkins_1      |
jenkins_1      | 32d64223e62945fe8c4c1ed050360700
jenkins_1      |
jenkins_1      | This may also be found at: /var/jenkins_home/secrets/initialAdminPassword
jenkins_1      |
jenkins_1      | *************************************************************
jenkins_1      | *************************************************************
jenkins_1      | *************************************************************
```
Этот пароль понадобится для самого первого входа Jenkins, потом про него можно забыть.

3. Устанавливаем плагины по умолчанию (потом можно удалить то, что ненужно).
4. Создаём администратора и переходим к начальной конфигурации Jenkins.

#### Начальная конфигурация

##### Global tool configuration :: JDK > Add JDK

1. Убираем галку Install Automatically.
2. Name: можно написать что угодно, но лучше правду: OpenJDK 11.
3. JAVA_HOME вставляем ```$JAVA_HOME```.
4. Жмём APPLY внизу страницы.


##### Global tool configuration :: Gradle
1. Add gradle
2. Отмечаем Install Automatically.
3. Name: что угодно
4. Install from Gradle.org и выбрать нужную версию.

### Запуск стенда в detached режиме.

Этот режим нужен для того, чтобы вы не видели весь-весь консольный вывод приложений, которые у вас сейчас запущены в докере.

Останавливаем docker-compose Ctrl+C.

#### Запускаем в detached

```bash
    docker-compose up -d
```
Вы увидите в консоли только вот это:

```
user@pc:/path$ docker-compose up -d
Starting cm-local-test-bed-wsl_jenkins_1     ... done
Starting cm-local-test-bed-wsl_selenoid_1 ... done
Starting cm-local-test-bed-wsl_selenoid-ui_1 ... done
```
Далее вы вернётесь в консоль, а docker-compose продолжит свою работу.

#### Остановка docker-compose

Чтобы остановить все запущенные приложения, необходимо выполнить следующую команду:

```bash
  docker-compose down
```

Вот и всё.