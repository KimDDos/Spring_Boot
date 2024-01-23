package com.example.demo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {

   @Autowired
   private ApplicationContext applicationContext;

   @Value("${mybatis.mapper-locations}")
   private String mapperLocations;

   @Bean
   @ConfigurationProperties(prefix = "spring.datasource")
   public HikariConfig hikariConfig() {
      return new HikariConfig();
   }

   @Bean
   public org.apache.ibatis.session.Configuration mybayisConfig() {
      return new org.apache.ibatis.session.Configuration();
   }

   @Bean
   public DataSource dataSource() throws Exception {
      DataSource dataSource = new HikariDataSource(hikariConfig());
      return dataSource;
   }

   @Bean
   public SqlSessionFactory sqlSessionFactory(DataSource datasource) throws Exception {
      SqlSessionFactoryBean SqlSessionFactoryBean = 
            new SqlSessionFactoryBean();
      SqlSessionFactoryBean.setDataSource(datasource);
      SqlSessionFactoryBean.setConfigLocation(
            applicationContext
            .getResource("classpath:/mappers/mybatis-config.xml"));
      SqlSessionFactoryBean.setMapperLocations(
            new PathMatchingResourcePatternResolver()
            .getResources(mapperLocations));
      return SqlSessionFactoryBean.getObject();
   }

   @Bean
   public SqlSessionTemplate sqlsessionTemplate(SqlSessionFactory sqlSessionFactory) {
      return new SqlSessionTemplate(sqlSessionFactory);
   }
   
}