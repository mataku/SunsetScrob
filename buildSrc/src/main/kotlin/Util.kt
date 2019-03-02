import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import java.io.FileInputStream
import java.util.Properties

fun Project.loadProperties(path: String, extra: ExtraPropertiesExtension) {
    val file = file(path)
    Properties().also {
        it.load(FileInputStream(file))
        it.forEach { (key, value) ->
            extra.set(key.toString(), value)
        }
    }
}