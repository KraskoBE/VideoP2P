package com.krasen.web.configuration;

import java.util.Arrays;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
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
    public void onApplicationEvent( ContextRefreshedEvent event ) {
        initializeRoles();
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
