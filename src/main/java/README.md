# ENTREGA 1 PRÁCTICA SDIS

### 👨‍💻 Autores
- Daniel del Pozo Gómez
- Lucía Aguilar Seco
- Daniel Fernández Varona
- María Blanco Blanco

### 📂 Estructuración del proyecto

| Simbolo | Significado |
|-|-|
| 🔵 | Class |
| 🟣 | Enum |
| 🟢 | Interface |
| 🟡 | Exception |

- **sdis/**
    - **broker/**
        - **client/**
            - **unit/**
                - 🔵 `AddMsg.java`
                - 🔵 `Auth.java`
                - 🔵 `AuthAddMsg.java`
                - 🔵 `AuthDeleteQ.java`
                - 🔵 `AuthState.java`
                - 🔵 `DeleteQ.java`
                - 🔵 `ReadQ.java`
                - 🔵 `State.java`
            - 🔵 `Cliente.java`
            - 🔵 `ClienteInteractivo.java`
        - **common/**
            - **customexceptions/**
                - 🟡 `MalMensajeProtocoloException.java`
                - 🟡 `UnexpectedResponseException.java`
                - 🟡 `WelcomeException.java`
            - 🟢 `ClientPrimitives.java`
            - 🟢 `ServidorPrimitives.java`
            - 🔵 `MensajeProtocolo.java`
            - 🔵 `Strings.java`
            - 🟣 `Primitiva.java`
        - **server/**
            - 🔵 `BlacklistManager.java`
            - 🔵 `Servidor.java`
            - 🔵 `Sirviente.java`
            - 🔵 `User.java`
            - 🟣 `Status.java`
    - **utils/**
        - 🔵 `MultiMap.java`

### ⚙ Archivos de compilación y ejecución automatizados
`compileFiles.bat` <br>
`startServer.bat` <br>
`startInteractiveClient.bat` <br>
`compileAndRunInteractiveClient.bat`

### 🚀 Lanzar pruebas de cliente unitarias
- Compilar proyecto (`compileFiles.bat`)
- Iniciar el servidor (`startServer.bat`)
- Cambiar al directorio de compilados: `cd out`
- Ejecutar prueba unitaria: `java sdis/broker/client/unit/{PRUEBA_UNITARIA}.java args`


### ⌨️ Compilar y ejecutar a mano
```bat
rem Compilar todos los archivos y lanzar un Servidor y un ClienteInteractivo

javac -d out sdis/broker/server/Servidor.java
javac -d out sdis/broker/client/ClienteInteractivo.java

rem Compilar unitarias si se quieren usar
javac -d out sdis/broker/client/unit/AddMsg.java
javac -d out sdis/broker/client/unit/Auth.java
javac -d out sdis/broker/client/unit/AuthAddMsg.java
javac -d out sdis/broker/client/unit/AuthDeleteQ.java
javac -d out sdis/broker/client/unit/AuthState.java
javac -d out sdis/broker/client/unit/DeleteQ.java
javac -d out sdis/broker/client/unit/ReadQ.java
javac -d out sdis/broker/client/unit/State.java

cd out
java sdis/broker/server/Servidor
java sdis/broker/client/ClienteInteractivo

rem Lanzar pruebas unitarias
java sdis/broker/client/unit/Auth "admin" "$%&/()="
```