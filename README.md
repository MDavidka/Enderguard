# EnderGuard

Minecraft server plugin - Automatically collects and updates server statistics.

## Build Instructions

### Requirements:
- Java JDK (8 or higher)
- `javac` and `jar` available in terminal

### Build:
```bash
mkdir out
javac -d out $(find src/main/java -name "*.java")
jar cfm enderguard.jar src/main/resources/plugin.yml -C out .
```
