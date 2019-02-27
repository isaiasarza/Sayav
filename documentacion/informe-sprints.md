# Informe Final de Proyecto

## Sprint "0"

### Toma de Decisiones

En este primer Sprint, los integrantes del grupo nos vimos con la dificultad de tener que tomar varias decisiones con respecto al proyecto. 

Decisiones en cuanto a la tecnología a usar (para implementar las comunicaciones), y a como retomar el proyecto (seguir utilizando las mismas herramientas o buscar nuevas).

Por un tema de tiempos, se eligió seguir utilizando las mismas herramientas, ya que la mayor parte de la aplicación se tendría que rehacer desde cero.  Evidentemente tuvimos una mayor facilidad, pero también tuvimos un costo extra a la hora de integrar la nueva tecnología, y de eliminar o considerar obsoleta la anterior implementación.

Para la tecnología, se buscaron varios protocolos ya implementados para delegar las comunicaciones entre centrales. Esta búsqueda fue previa al Sprint,  de todas las tecnologías encontradas pocas estaban actualizadas y otras no eran acordes a nuestro contexto.

Entre ellas se tomaron en cuenta:
* JXSE: Es una implementación del protocolo JXTA. JXTA establece un protocolo de comunicación para conexiones de tipo Peer-To-Peer. La bibliografía era bastante convincente, pero no se lograron hacer pruebas debido a la desactualización del software.
* Tom P2P: Esta es una librería que, con fácil deducción, también implementa un protocolo Peer-To-Peer. A diferencia de la anterior, la bibliografía oficial era bastante escasa y tampoco se pudieron llevar a cabo pruebas.
* Paho: Esta librería, implementa el protocolo MQTT (Protocolo de envío de mensajes). Es algo que actualmente se encuentra actualizado, y se pudieron hacer pruebas rápidas.

### Planificación y Estimación

La planificación acordada, fue dejar funcionando todas las tareas livianas correspondientes a la central (datos, alarma y sectores) de forma independiente.  Y además poder implementar las tareas correspondientes a la administración de grupos (creación, adición de miembros, etc.).

Cabe destacar que a la hora de definir la lista de tareas no se tuvo en cuenta el hecho de tener que realizar un refactoring de lo hecho con anterioridad en la cursada para poder comenzar con este primer Sprint.

Como conclusión, las tareas de grupos fueron subestimadas ya que el tiempo estimado para cada una de las tarjetas correspondientes resultó insuficiente y se vio reflejado a la hora de comenzar con las tarjetas, ya que nos encontramos con tareas de gran importancia que no fueron agregadas (No contemplada a la hora de estimar) pero si se comenzó con su implementación. 

### Historias de Usuario (Tarjetas)

* Registrar Usuario
* Activar/Desactivar Sector
* Listar Grupos
* Crear Grupo
* Habilitar/Deshabilitar Alarma
* Renombrar Sectores
* Mostrar Sectores
* Login Usuario
* Actualizar Datos


