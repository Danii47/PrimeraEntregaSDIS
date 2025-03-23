javac -d out sdis/broker/server/Servidor.java sdis/broker/client/ClienteInteractivo.java sdis/broker/client/unit/AddMsg.java sdis/broker/client/unit/Auth.java sdis/broker/client/unit/AuthAddMsg.java sdis/broker/client/unit/AuthDeleteQ.java sdis/broker/client/unit/AuthState.java sdis/broker/client/unit/DeleteQ.java sdis/broker/client/unit/ReadQ.java sdis/broker/client/unit/State.java
cd out

start cmd /k "java sdis/broker/server/Servidor"
start cmd /k "java sdis/broker/client/ClienteInteractivo"