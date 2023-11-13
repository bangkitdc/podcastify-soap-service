package com.podcastify.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class EmailGenerator {
  public static String generateEmail(String from, String to) {
    TemplateEngine templateEngine = new TemplateEngine();

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding("UTF-8");

    templateEngine.addTemplateResolver(templateResolver);

    Context context = new Context();
    context.setVariable("From", from);
    context.setVariable("To", to);

    // Process the Thymeleaf template with dynamic data
    return templateEngine.process("email", context);
  }
}
