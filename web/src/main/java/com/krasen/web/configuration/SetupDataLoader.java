package com.krasen.web.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.NonNull;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import com.krasen.web.models.*;
import com.krasen.web.repositories.RoleRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = LoggerFactory.getLogger( SetupDataLoader.class );
    private final RoleRepository roleRepository;

    @Autowired
    public SetupDataLoader( RoleRepository roleRepository ) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event ) {
        initializeRoles();
        printActiveProperties((ConfigurableEnvironment) event.getApplicationContext().getEnvironment());
    }

    private void printActiveProperties(ConfigurableEnvironment env) {

        System.out.println("************************* ACTIVE APP PROPERTIES ******************************");

        List<MapPropertySource> propertySources = new ArrayList<>();

        env.getPropertySources().forEach(it -> {
            if (it instanceof MapPropertySource && it.getName().contains("applicationConfig")) {
                propertySources.add((MapPropertySource) it);
            }
        });

        propertySources.stream()
                .map(propertySource -> propertySource.getSource().keySet())
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .forEach(key -> {
                    try {
                        System.out.println(key + "=" + env.getProperty(key));
                    } catch (Exception e) {
                        logger.info("{} -> {}", key, e.getMessage());
                    }
                });
        System.out.println("******************************************************************************");
    }

    private void initializeRoles() {
        Arrays.stream( RoleType.values() ).forEach( this::createRoleIfNotFound );
        logger.info( "Roles initialized" );
    }

    private void createRoleIfNotFound( RoleType name ) {
        if( roleRepository.findByName( name ).isEmpty() ) {
            roleRepository.save( Role.builder().name( name ).build() );
        }
    }

}
