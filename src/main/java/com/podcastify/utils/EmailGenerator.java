package com.podcastify.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class EmailGenerator {
  public static String generateEmail(String from, String to, String url) {
    TemplateEngine templateEngine = new TemplateEngine();

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    // templateResolver.setPrefix("/classpath:/templates/"); // doesnt work
    templateResolver.setPrefix("/templates/"); // doesnt work
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");

    templateEngine.addTemplateResolver(templateResolver);

    Context context = new Context();
    context.setVariable("From", from);
    context.setVariable("To", to);
    context.setVariable("Action", url);

    // Process the Thymeleaf template with dynamic data
    return templateEngine.process("email", context);
  }
}
