package test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;


/**
 * Created by Joachim on 20.08.2015.
 */

public class HibernateUtility {

    private static final SessionFactory sessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static{
        try{
            Configuration config = getConfiguration();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
            config.setSessionFactoryObserver(new SessionFactoryObserver() {

                private static final long serialVersionUID = 1L;

                @Override
                public void sessionFactoryCreated(SessionFactory arg0) {

                }

                @Override
                public void sessionFactoryClosed(SessionFactory arg0) {
                    ServiceRegistryBuilder.destroy(serviceRegistry);
                }
            });
            sessionFactory = config.buildSessionFactory(serviceRegistry);


        }catch(Throwable ex){
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }

    public static Session openSession(){
        return sessionFactory.openSession();
    }

    private static Configuration getConfiguration(){
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Mensch.class);
        cfg.addAnnotatedClass(Tier.class);
        cfg.setProperty("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        cfg.setProperty("hibernate.connection.url", "jdbc:sqlserver://nbj:1434;databaseName=Pets;instanceName=SQLEXPRESS;integratedSecurity=true;");
        //cfg.setProperty("hibernate.connection.username", "web614");
        //cfg.setProperty("hibernate.connection.password", "9qesU4d!Ul");
        cfg.setProperty("hibernate.show_sql", "true");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "update");
        cfg.setProperty("hibernate.cache.provider_class","org.hibernate.cache.NoCacheProvider");
        cfg.setProperty("hibernate.current_session_context_class", "thread");
        return cfg;
    }

}
