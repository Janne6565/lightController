package com.janne.lightcontroller.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@Slf4j
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Bean
    public InetAddress broadcastIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                // Ignore loopback and down interfaces
                if (iface.isLoopback() || !iface.isUp()) continue;

                for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
                    InetAddress localAddr = addr.getAddress();

                    if (!(localAddr instanceof Inet4Address)) continue;

                    InetAddress broadcast = addr.getBroadcast();
                    if (broadcast == null) continue;
                    return broadcast;
                }
            }
        } catch (Exception e) {
            log.warn("Error while trying to broadcast IP address", e);
        }
        return null;
    }
}
