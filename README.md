### Introducción

Este proyecto intenta simular la inscripción de alumnos a materias de una universidad, para su correcto funcionamiento se implemento dos tipos de usuarios los cuales podran realizar diferentes operaciones dependiendo de su rol asignado.
Este sistema esta desarrollado en Java 8, Spring Boot, Spring Security, Thymeleaf, Bootstrap, JPA, MySQL 8.


# Instalación

Para el correcto funcionamiento de este sistema se debe tener previamente instalada algunas tecnologías las cuales se detallan a continuación.

------------

- JDK 8 [Descargar][1]
- MySQL 8  [Descargar][3]
- IDE a elección en mi caso ApacheNetbeans 12 [Descargar][4]

------------

Luego de estas instalaciones se podra clonar el repositorio mediante el comando.

> git clone url_proyecto.

------------

El paso siguiente es la configuración a la base de datos, para esto se debe ingresar al archivo application.properties y configurar las credenciales personales de acceso.

------------

Aclaración el sistema no cuenta con un registro de usuario por lo cual se deberá agregar los usuarios manualmente en la base de datos. También en el caso de no contar con una base de datos ya cargada se podra modificar el archivo appication.properties para la creacion de las tablas y sus relaciones de manera automática  solamente teniendo una base de datos previamente creada y modificando la siguiente linea.

> spring.jpa.hibernate.ddl-auto=update 

por 

> spring.jpa.hibernate.ddl-auto=create .

------------

Debido a que el sistema no cuenta con un registro de usuarios y cuenta con la seguridad de Spring Security implementada si se desea crear un nuevo usuario en el metodo main del sistema se encuentra comentado un fragmento de código el cual se encarga de encriptar una contraseña para poder visualizarla por la consola y de esa manera se podra copiar la contraseña elegida encriptada en la base de datos ya que el sistema esta preparado para buscar una contraseña encriptada de otra manera no se podra logear en el sistema.

------------

Por último se deberá descargar las dependencias de maven en el caso de utilizar el IDE NetBeans se deberá seleccionar la raiz del proyecto con un click derecho y seleccionar la opción Clean and Build de esta forma se descargarán todas las dependencias y se creará el jar del proyecto para finalmente poder ejecutarlo.

------------
