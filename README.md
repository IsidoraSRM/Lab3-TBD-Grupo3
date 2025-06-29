# Lab3-TBD-Grupo3

Este repositorio contiene el proyecto del Lab3 de TBD, desarrollado por el Grupo 3. El proyecto se compone de dos módulos principales:

- **Backend:** Aplicación desarrollada en Spring Boot que provee los servicios REST y utiliza PostgreSQL con PostGIS y MongoDB para el manejo de datos geoespaciales.
- **Frontend:** Aplicación web (desarrollada con Vue.js) para interactuar con los servicios del backend.

## Índice

- [Requisitos Preliminares](#requisitos-preliminares)
- [Clonación del Repositorio](#clonación-del-repositorio)
- [Configuración de la Base de Datos](#configuración-de-la-base-de-datos)
- [Instalación y Ejecución del Backend](#instalación-y-ejecución-del-backend)
- [Instalación y Ejecución del Frontend](#instalación-y-ejecución-del-frontend)
- [Recomedaciones](#recomendaciones)

## Requisitos Preliminares

### Para el Backend

- **Java JDK 17**  
  [Descargar JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven**  
  [Descargar Maven](https://maven.apache.org/download.cgi)
- **MongoDB 8.0.11** (o superior) con **MongoDB Shell 2.5.3**
  [MongoDB](https://www.mongodb.com/try/download/community)
- **PostgreSQL 12** (o superior) con la extensión **PostGIS 3.3**  
  _Opcional:_ [pgAdmin4](https://www.pgadmin.org/download/) para administración gráfica.
- **Git** para clonar el repositorio.

### Para el Frontend

- **Node.js** (versión 16.14 o superior) y **npm**  
  [Descargar Node.js](https://nodejs.org/)
- **Vue CLI** (si el proyecto utiliza Vue.js)  
  Instalar globalmente ejecutando:
  ```bash
  npm install -g @vue/cli
  ```

## Configuración de la Base de Datos

1. Crear una base de datos con el nombre `Lab2TBD` en PostgreSQL (El nombre quedó de la entrega anterior)
2. Ejecutar el contenido del archivo `createLab2.sql`, para crear las tablas.
3. Ejecutar el archivo `dump.sql` para tener todas las funcionalidades del proyecto.
4. Insertar los datos de archivo `insertFinalLab2.sql`.
5. Insertar el resto de los datos del archivo `insert2FinalLab2.sql`.
6. Iniciar una servidor de MongoDB usando `mongod`
   ```bash
   mongod

   ```
7. Crear una base de datos con el nombre `Lab3` en MongoDB
8. Ejecutar el archivo `mongoLoad.js` dentro de la consola Mongosh.
   
   ```bash
   load("mongoLoad.js")

   ```

   ```bash
   load("mongoLoad.js")

   ```

## Instalación y Ejecución del Backend

1. Abir la carpeta backend en Intellij o algún otro editor de codigo.
2. Configurar el archivo application.properties con el usuario y contraseña de postgres.
3. Ejecutar `BackendApplication`

## Instalación y Ejecución del Frontend

1. Abrir en una terminal la carpeta frontend-delivery:

   ```bash
   npm install

   ```

2. Luego ejecutar le proyecto con:

   ```bash
   npm run dev

   ```

3. Abrir el link de localhost para vizualizar las funcionaliades.

## Recomendaciones

- **Backend** se recomienda usar Intellij para ejecutar el backend.
- **Frontend** Se recomienda usar visual studio code para ejecutar el frontend.
