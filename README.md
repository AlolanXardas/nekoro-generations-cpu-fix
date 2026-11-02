# Nekoro Generations CPU Fix

A tiny fix for Generations-Core 1.2.8 (Minecraft 1.21.1, NeoForge and Fabric). Its background thread that prepares Pokémon models never rests and keeps one CPU core at 100% for the whole game. This mod changes only that loop with a mixin: the thread now sleeps until a model really needs preparing and then works exactly as before.

Made mainly for my Nekoro Pokemon modpack, but it works in any pack with Generations-Core.

Download: [CurseForge](https://www.curseforge.com/minecraft/search?search=nekoro+generations+cpu+fix)

## Building

Needs JDK 21. NeoForge: run `gradlew build` in this folder. Fabric: run `gradlew build` in `fabric-1.21.1` (it uses the same mixin from `src`). The jar name comes from `mod_version` in each `gradle.properties`.

## License

MIT, see [LICENSE](LICENSE). Mod by Alolan Xardas, code written with Claude.
