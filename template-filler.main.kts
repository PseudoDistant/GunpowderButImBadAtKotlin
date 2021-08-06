import java.io.File

print("Mod ID (e.g. my-module for gunpowder-my-module): ")
val modId = readLine()!!
val modName = modId.split("-").joinToString(" ") { it.capitalize() }
val modEntryName = "Gunpowder${modName.split(" ").joinToString("")}Module"
val modMixinName = "mixins.$modId.gunpowder.json"
val modMixinPlugin = "${modName.split(" ").joinToString("")}ModulePlugin"
val markdownName = "Gunpowder${modName.split(" ").joinToString("")}"


println("Editing fabric.mod.json")
File("src/main/resources/fabric.mod.json").apply {
    val fabricJson = readText()
        .replace("gunpowder-template", "gunpowder-$modId")
        .replace("Gunpowder Template", "Gunpowder $modName")
        .replace("GunpowderTemplateModule", modEntryName)
        .replace("mixins.template.gunpowder.json", modMixinName)
    File("src/main/resources/fabric.mod.json").writeText(fabricJson)
}

println("Editing mixins.template.gunpowder.json")
File("src/main/resources/mixins.template.gunpowder.json").apply {
    val mixinJson = readText()
        .replace("io.github.gunpowder.mixin.template", "io.github.gunpowder.mixin.$modId")
        .replace("TemplateModulePlugin", modMixinPlugin)
    writeText(mixinJson)
    renameTo(File("src/main/resources/$modMixinName"))
}

println("Editing GunpowderTemplateModule.kt")
File("src/main/kotlin/io/github/gunpowder/GunpowderTemplateModule.kt").apply {
    val kotlinBody = readText()
        .replace("GunpowderTemplateModule", modEntryName)
        .replace("template", modId)
    writeText(kotlinBody)
    renameTo(File("src/main/kotlin/io/github/gunpowder/$modEntryName.kt"))
}

println("Editing TemplateModulePlugin.kt")
File("src/main/kotlin/io/github/gunpowder/mixin/plugin/TemplateModulePlugin.kt").apply {
    val kotlinBody = readText()
        .replace("TemplateModulePlugin", modMixinPlugin)
    writeText(kotlinBody)
    renameTo(File("src/main/kotlin/io/github/gunpowder/mixin/plugin/$modMixinPlugin.kt"))
}

println("Renaming mixin folder")
File("src/main/java/io/github/gunpowder/mixin/template").renameTo(File("src/main/java/io/github/gunpowder/mixin/$modId"))

println("Editing settings.gradle")
File("settings.gradle").apply {
    val gradleBody = readText()
        .replace("gunpowder-template", "gunpowder-$modId")
    writeText(gradleBody)
}

println("Editing README.md")
File("README.md").apply {
    val mdBody = readText()
        .replace("GunpowderTemplate", markdownName)
    writeText(mdBody)
}

println("Editing CHANGELOG.md")
File("CHANGELOG.md").apply {
    val mdBody = readText()
        .replace("GunpowderTemplate", markdownName)
    writeText(mdBody)
}

println("Detaching from git")
File(".git").deleteRecursively()
