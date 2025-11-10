package wasm.project.demo.wasmdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform