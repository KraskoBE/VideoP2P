package com.krasen.web.configuration;

import com.krasen.web.models.Role;
import com.krasen.web.models.RoleType;
import com.krasen.web.repositories.RoleRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = LoggerFactory.getLogger( SetupDataLoader.class );
    private final RoleRepository roleRepository;

    @Autowired
    public SetupDataLoader( RoleRepository roleRepository ) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent( @NonNull ContextRefreshedEvent event ) {
        initializeRoles();
    }

    private void initializeRoles() {
        Arrays.stream( RoleType.values() ).forEach( this::createRoleIfNotFound );
        logger.info( "Roles initialized" );
    }

    private void createRoleIfNotFound( RoleType name ) {
        if ( roleRepository.findByName( name ).isEmpty() ) {
            roleRepository.save( Role.builder().name( name ).build() );
        }
    }

}
