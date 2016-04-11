package com.example.ol.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/*
 * Annotation driven JPA configuration
 */

@Configuration
@EnableJpaRepositories("com.example.ol.db") //Scanning for class(es) implementing Repository interface
public class DataConfig {

	@Bean
	public DataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:./testdb;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
		ds.setUsername("sa");
		ds.setPassword("");
		
		return ds;
	}
	
	@Bean
	public JpaTransactionManager transactionManager() {
	    return new JpaTransactionManager();
	  }
	  
	 @Bean
	 public HibernateJpaVendorAdapter jpaVendorAdapter() {
	    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
	    adapter.setDatabase(Database.H2);
	    adapter.setShowSql(true);
	    adapter.setGenerateDdl(true);
	    //adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
	    return adapter;
	  }
	  
	  @Bean(name="entityManagerFactory")
	  public LocalContainerEntityManagerFactoryBean emf(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
	    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
	    emf.setDataSource(dataSource);
	    emf.setPersistenceUnitName("ol");
	    emf.setPackagesToScan("com.example.ol.domain");//Scanning for Entities class(es)
	    emf.setJpaVendorAdapter(jpaVendorAdapter);
	    return emf;
	  }
}
