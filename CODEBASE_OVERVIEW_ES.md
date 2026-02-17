# Guía rápida del código base de RepairPlugin

## 1) Estructura general

- `src/main/java/xeliox/repairplugin/RepairPlugin.java`: punto de entrada (`JavaPlugin`), registro de comandos/listeners, carga de configuración y chequeo de actualizaciones.
- `src/main/java/xeliox/repairplugin/MainCommand.java`: concentra la lógica de comandos (`/repairplugin`, `/repair`, `/giveexp`).
- `src/main/java/xeliox/repairplugin/core/`
  - `ConfigManager.java`: carga, defaults y recarga de `config.yml`.
  - `Messages.java`: catálogo de mensajes configurable y traducido con colores.
- `src/main/java/xeliox/repairplugin/listener/RepairPluginListener.java`: lógica de reparación al interactuar con yunque.
- `src/main/java/xeliox/repairplugin/utils/`: utilidades (autoupdater, tab completion, traducción de colores, enum de versiones).
- `src/main/resources/`
  - `plugin.yml`: metadatos, comandos y permisos.
  - `config.yml`: costos y textos configurables.
- `src/main/java/com/iridium/iridiumcolorapi/`: clases de color API embebidas para procesar `&` y gradientes.

## 2) Flujo funcional principal

1. Bukkit llama `onEnable()` en `RepairPlugin`.
2. Se detecta la versión del servidor (`setVersion`) y se inicializa `ConfigManager`.
3. Se registran comando ejecutor + tab completer para `repairplugin`, `repair`, `giveexp`.
4. Se registra `RepairPluginListener` para lógica de yunque.
5. Se ejecuta `AutoUpdater.checkForUpdates()` de forma asíncrona.

## 3) Aspectos clave a entender primero

- **Configuración viva**:
  - `ConfigManager` inserta claves faltantes automáticamente y permite `/repairplugin reload`.
  - Los mensajes salen desde `Messages` (no hardcodear texto directamente en comandos/listeners).
- **Compatibilidad de versiones**:
  - Se usa `VersionUtils` + detección de versión para soporte cross-version.
  - Reparar ítems cambia entre APIs legacy (`getDurability`) y modernas (`Damageable`).
- **Permisos**:
  - Hay permisos separados para reparar uno, reparar todo, dar experiencia y administración.
- **Economía de niveles**:
  - Las reparaciones y `/giveexp` consumen/traspasan niveles; validar siempre suficientes niveles antes de mutar estado.

## 4) Puntos de atención técnica (deuda/riesgos)

- `MainCommand` y `RepairPluginListener` dependen de `VersionCheck`, pero esa clase no está en el árbol actual. Es importante revisar/añadir esa utilidad para compilación estable.
- La lógica de comando está muy concentrada en `MainCommand`; si crece, conviene separar por subcomando/servicio.
- El updater usa endpoint HTTP externo de Spigot; en entornos restringidos puede fallar y solo debe considerarse informativo.

## 5) Qué aprender después (ruta sugerida para onboarding)

1. **Spigot/Bukkit API básica**: ciclo de vida de plugins, `CommandExecutor`, `Listener`, `Player`, `ItemStack`.
2. **Configuración YAML con Simple-YAML**: patrón de defaults + recarga segura.
3. **Compatibilidad multi-versión** en plugins de Minecraft (legacy vs modern item damage APIs).
4. **Buenas prácticas de arquitectura**:
   - extraer servicios (`RepairService`, `ExpService`),
   - centralizar validaciones,
   - reducir duplicación entre comando y listener.
5. **Testing para plugins**: introducir pruebas unitarias para parseo/validaciones (por ejemplo en lógica pura separada de Bukkit).

## 6) Primeras tareas recomendadas para contribuir

- Añadir o restaurar `VersionCheck` y validar build CI.
- Cubrir validaciones de `/giveexp` y costos de reparación con tests unitarios en lógica desacoplada.
- Separar `MainCommand` en handlers por comando para mejorar mantenibilidad.
- Documentar en README una tabla clara de permisos y ejemplos de configuración.
