package com.example.cpu_stress.controller;

import com.example.cpu_stress.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CpuStressTestComplexController {


    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("titulo", "Pagina inicial CPU Stress");
        model.addAttribute("product", new Product());
        model.addAttribute("boton", "stress");

        String hostIP = System.getenv("MY_IP");

        // Comprobar si la variable está disponible
        if (hostIP != null) {
            System.out.println("El valor de MY_IP es: " + hostIP);
            model.addAttribute("ip", hostIP);
        } else {
            System.out.println("MY_IP no está definida.");
        }
        return Mono.just("index");
    }


    @PostMapping("/result")
    public Mono<String> result(Product producto, Model model) {
        int numThreads = Runtime.getRuntime().availableProcessors(); // Número de núcleos disponibles
        long duration = producto.getIterations() * 1000; // Duración en milisegundos (10 segundos)
        System.out.println("Estresando la CPU durante " + (duration / 1000) + " segundos usando " + numThreads + " hilos.");
        model.addAttribute("message", "Estresando la CPU durante " + (duration / 100) + " segundos usando " + numThreads + " hilos.");

        String hostIP = System.getenv("MY_IP");
        // Comprobar si la variable está disponible
        if (hostIP != null) {
            System.out.println("El valor de MY_IP es: " + hostIP);
            model.addAttribute("ip", hostIP);
        } else {
            System.out.println("MY_VARIABLE no está definida.");
        }

        System.out.println(producto.getIterations());
        model.addAttribute("titulo", "Resultado");
        model.addAttribute("iterations", producto.getIterations());
        model.addAttribute("result", duration);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                long endTime = System.currentTimeMillis() + duration; // Calcula el tiempo final
                while (System.currentTimeMillis() < endTime) {
                    double result = Math.pow(Math.random(), Math.random()); // Cálculo intensivo
                }
            }).start();
        }

        return Mono.just("result");
    }
}
