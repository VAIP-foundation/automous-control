package com.autonomous.pm.config;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.autonomous.pm.util.CryptoUtil;

/**
 * SqlMapper Configuration
 *
 * @author
 */
@Configuration
@MapperScan(value = { "com.autonomous.pm.dao" })
@PropertySource(value = "classpath:application.properties")
public class SqlMapperConfig {
	public static final Logger logger = LoggerFactory.getLogger(SqlMapperConfig.class);

//	@Value("${spring.datasource.password}")
	@Value("${mysql.password}")
	private String password;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {

		String url = AppConfig.instance().getProperty("database.url");
		String username = AppConfig.instance().getProperty("database.username");
		String dirverClassName = AppConfig.instance().getProperty("database.driver-class-name");

		logger.info("Data Source: driverClassName:   {}", dirverClassName);
		logger.info("Data Source: Database.url:      {}", url);
		logger.info("Data Source: Username:          {}", username);
		
		password = CryptoUtil.decryptAES256(password);
		
		// http://www.mybatis.org/mybatis-3/configuration.html#environments
		PooledDataSource dataSource = new PooledDataSource(dirverClassName, url, username, password);
		return dataSource;
	}

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactory() throws Exception {

		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));// spring boot mybatis settings 부분

		// Resource[] res = new
		// PathMatchingResourcePatternResolver().getResources("classpath:**/scity/sql/*Mapper.xml");
		// Resource[] res = new
		// PathMatchingResourcePatternResolver().getResources("classpath:com/uangel/scity/sql/*Mapper.xml");
		// Resource[] res = new
		// PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/**/*Mapper.xml");
		sessionFactory.setMapperLocations(res);

		return sessionFactory.getObject();
	}

	@Bean
	@Primary
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}

	@Bean
	@Primary
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}
