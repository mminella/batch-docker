/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.batch;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author Michael Minella
 */
@SpringBootApplication
@EnableBatchProcessing
public class Main {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("${job.email.to}")
	private String toAddress;

	@Bean
	public Step step(JavaMailSender mailSender) {
		return stepBuilderFactory.get("step1")
				.tasklet((contribution, chunkContext) -> {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
					SimpleMailMessage mail = new SimpleMailMessage();

					mail.setTo(toAddress);
					mail.setSubject(String.format("Direct from %s!", InetAddress.getLocalHost().getHostAddress()));
					mail.setText(String.format("The batch job has executed on Lattice on %s!", simpleDateFormat.format(new Date())));

					mailSender.send(mail);

					return RepeatStatus.FINISHED;
				}).build();
	}

	@Bean
	public Job job(Step step) {
		return jobBuilderFactory.get("lattice-email-job")
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
	}

	public static void main(String [] args) {
		SpringApplication.run(Main.class, args);
	}
}
