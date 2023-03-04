package com.groyyo.order.management.config;


import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.groyyo.core.sqlPostgresJpa.CustomJpaVendor;
import com.groyyo.core.sqlPostgresJpa.JpaConfigUtil;
import com.groyyo.core.sqlPostgresJpa.RoutingDataSource;
import com.groyyo.core.sqlPostgresJpa.enums.DbType;
import com.mchange.v2.c3p0.ComboPooledDataSource;


@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(entityManagerFactoryRef = "entityManager", transactionManagerRef = "transactionManager", basePackages = "${packages.repository}")
@EnableTransactionManagement
public class DatabaseConfig {

	@Value("${jdbc.driver.class.name}")
	private String driverClassName;

	@Value("${jdbc.initial.pool.size}")
	private int initialPoolSize;

	@Value("${jdbc.min.pool.size}")
	private int minPoolSize;

	@Value("${jdbc.max.pool.size}")
	private int maxPoolSize;

	@Value("${jdbc.acquire.increment}")
	private int acquireIncrement;

	@Value("${jdbc.max.statements}")
	private int maxStatements;

	@Value("${jdbc.max.idle.timing}")
	private int maxIdleTime;

	@Value("${jdbc.max.idle.time.excess.connections}")
	private int maxIdleTimeExcessConnections;

	@Value("${jdbc.checkout.timeout}")
	private int checkoutTimeout;

	@Value("${jdbc.preferred.test.query}")
	private String preferredTestQuery;

	@Value("${jdbc.test.connection.on.checkin:true}")
	private boolean testConnectionOnCheckin;

	// Master property
	@Value("${jdbc.master.url}")
	private String masterUrl;

	@Value("${jdbc.master.username}")
	private String masterUsername;

	@Value("${jdbc.master.password}")
	private String masterPassword;

	// Slave property
	@Value("${jdbc.slave.url}")
	private String slaveUrl;

	@Value("${jdbc.slave.username}")
	private String slaveUsername;

	@Value("${jdbc.slave.password}")
	private String slavePassword;

	// Hibernate Configuration
	@Value("${hibernate.cache.provider_class}")
	private String hibernateCacheProviderClass;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;

	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;

	@Value("${hibernate.format_sql}")
	private String hibernateFormatSql;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;

	@Value("${hibernate.order_inserts}")
	private String hibernateOrderInserts;

	@Value("${hibernate.order_updates}")
	private String hibernateOrderUpdates;

	@Value("${packages.entitymanager}")
	private String entityManagerPackages;

	@Value("${packages.repository}")
	private String repositoryPackages;

	@Bean(name = "masterDataSource")
	public DataSource masterDataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = createPooledDataSource();

		JpaConfigUtil.configureDbCrendentials(dataSource, driverClassName, masterUrl, masterUsername, masterPassword);

		return dataSource;
	}

	@Bean(name = "slaveDataSource")
	public DataSource slaveDataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = createPooledDataSource();

		JpaConfigUtil.configureDbCrendentials(dataSource, driverClassName, slaveUrl, slaveUsername, slavePassword);

		return dataSource;
	}

	private ComboPooledDataSource createPooledDataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

		JpaConfigUtil.configureDbPool(dataSource, initialPoolSize, minPoolSize, maxPoolSize, acquireIncrement);
		JpaConfigUtil.configureDbConnection(dataSource, maxStatements, maxIdleTime, maxIdleTimeExcessConnections, checkoutTimeout, preferredTestQuery, testConnectionOnCheckin);

		return dataSource;
	}

	@Primary
	@Bean(name = "dataSource")
	@DependsOn(value = { "slaveDataSource", "masterDataSource" })
	public DataSource dataSource() throws PropertyVetoException {
		RoutingDataSource routingDataSource = new RoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(DbType.MASTER, masterDataSource());
		dataSourceMap.put(DbType.REPLICA, slaveDataSource());

		routingDataSource.setTargetDataSources(dataSourceMap);
		routingDataSource.setDefaultTargetDataSource(masterDataSource());
		routingDataSource.afterPropertiesSet();

		return routingDataSource;
	}

	@Bean(name = "entityManager")
	public LocalContainerEntityManagerFactoryBean entityManager() throws PropertyVetoException {

		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setJpaVendorAdapter(new CustomJpaVendor());
		entityManagerFactory.setPackagesToScan(entityManagerPackages);
		entityManagerFactory.setJpaPropertyMap(hibernateJpaProperties());
		return entityManagerFactory;
	}

	private Map<String, ?> hibernateJpaProperties() {

		return JpaConfigUtil.configureHibernateProperties(hibernateCacheProviderClass, hibernateDialect,
				hibernateShowSql, hibernateFormatSql, hibernateHbm2ddlAuto, hibernateOrderInserts,
				hibernateOrderUpdates);

	}

	@Bean(name = "transactionManager")
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(emf);
		return jpaTransactionManager;
	}

}