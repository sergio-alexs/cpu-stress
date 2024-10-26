package com.example.cpu_stress.controller;

import com.example.cpu_stress.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class CpuStressTestComplexController {


    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("titulo", "Pagina inicial CPU Stress");
        model.addAttribute("product", new Product());
        model.addAttribute("boton", "stress");

        try {
            InetAddress containerIp = InetAddress.getLocalHost();
            System.out.println("La IP del contenedor es: " + containerIp.getHostAddress());
            model.addAttribute("ip", containerIp.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("No se pudo obtener la IP del contenedor: " + e.getMessage());
        }

        return Mono.just("index");
    }


    @PostMapping("/result")
    public Mono<String> result(Product producto, Model model) {
        int numThreads = Runtime.getRuntime().availableProcessors(); // Número de núcleos disponibles
        long duration = producto.getIterations() * 100; // Duración en milisegundos (10 segundos)
        System.out.println("Estresando la CPU durante " + (duration / 100) + " decisegundos usando " + numThreads + " hilos.");
        model.addAttribute("message", "Estresando la CPU durante " + (duration / 100) + " decisegundos usando " + numThreads + " hilos.");
        try {
            InetAddress containerIp = InetAddress.getLocalHost();
            System.out.println("La IP del contenedor es: " + containerIp.getHostAddress());
            model.addAttribute("ip", containerIp.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("No se pudo obtener la IP del contenedor: " + e.getMessage());
        }
        System.out.println(producto.getIterations());
        model.addAttribute("titulo", "Resultado de Iteraciones");
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
