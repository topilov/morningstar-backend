package me.topilov.morningstar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MorningstarApplication

fun main(args: Array<String>) {
	runApplication<MorningstarApplication>(*args)
}
