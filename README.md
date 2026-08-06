# Yare Invasion. 
# by Reynaldo Caceres y Mariel Sanchez
**Yare Invasion** es un juego arcade estilo *Space Invaders / Arcade Shooter* inspirado en Galaxy y Twinbee, desarrollado en Java. Esquivar a los enemigos y destruirlos mientras se compite por obtener el mayor puntaje guardando tus iniciales en la tabla de clasificación

# Informacion general
* **Lenguaje:** Java 
* **Interfaz Gráfica:** Java Swing / AWT (Graphics2D)
* **Audio:** Java Sound API (`javax.sound.sampled`)
* **Patrón de Diseño:** **MVC** (*Model-View-Controller*)
* **Fuente:** Custom Typography (`ARCADECLASSIC.TTF`)

* ## Créditos y Autores
* **Programación y Desarrollo:** Mariel Sanchez y Reynaldo Caceres
* **Diseño de Personajes y Sprites:** Creada por Mariel Sanchez con revision de Reynaldo Caceres mediante el programa en linea "Piskel"
* **Musica Original:** Compuesta y producida por Mariel Sanchez mediante el programa Bosca Ceoil

* # Controles del juego
* **Moverse a la izquierda** ; `Flecha Izquierda` o `A` 
* **Moverse a la derecha** ; `Flecha Derecha` o `D` 
* **Moverse arriba** ; `Flecha Arriba` o `W` 
* **Moverse abajo** ; `Flecha Abajo` o `S` 
* **Disparar** ; `Espacio` 
* **Guardar Puntaje** ; `ENTER` (en la casilla de iniciales) 

## Mecanicas de Juego
* **Jugador:** Nave espacial con movimiento libre en la pantalla
* **Enemigos comunes (Denny):** Aparecen dinámicamente y suman +100 puntos al ser destruidos, estos no atacan sin embargo tienen un movimiento mas libre, siendo este el unico que se mueve tanto horizontal y verticalmente 
* **Enemigo tanque (Jaxi):** Enemigo más resistente que dispara proyectiles hacia el jugador y otorga +200 puntos. Este no se mueve a los lados, solo dispara, tiene una resistencia de 4 golpes para poder derribarlo. Reacciona con un empujón al recibir impactos
* **Tabla de Liderazgo (Top Scores):** Al perder, puedes ingresar tus iniciales (3 letras) para registrar tu puntuación máxima

## Patrones de Diseño y Arquitectura
* **MVC (Modelo-Vista-Controlador):** Separación total entre la lógica del juego (`models`) la interfaz gráfica (`views`) y la gestión de eventos/bucles (`controllers`).
* <img width="290" height="300" alt="Screenshot 2026-08-05 223350" src="https://github.com/user-attachments/assets/5d68daa2-8833-426f-82b0-7b919feb101d" />

* **Game Loop Pattern:** Implementado a ~60 FPS mediante `javax.swing.Timer` para mantener actualizaciones periódicas de física y renderizado.
* **Template / Orientación a Objetos (`GameObject`):** Clase abstracta base que encapsula las cajas de colisión y fuerza a cada entidad a implementar su propio método `update()`.


## Visuales del juego y funcion 
<img width="617" height="843" alt="Screenshot 2026-08-05 224124" src="https://github.com/user-attachments/assets/6a7dfe27-bcf1-427e-9a23-2d915e3cfb80" />

<img width="620" height="836" alt="Screenshot 2026-08-05 224134" src="https://github.com/user-attachments/assets/06a36544-6fd3-4977-bdee-ee604e62b7ca" />


